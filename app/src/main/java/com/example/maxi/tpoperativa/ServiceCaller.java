package com.example.maxi.tpoperativa;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by lucho on 26/01/2018.
 */

public class ServiceCaller extends IntentService {

    public static final String RESPONSE_ACTION = "Respuesta del servidor";
    public static final String RESPONSE = "DATA RESPONSE";
    public static final String SERVICE_TYPE = "SERVICE_TYPE";
    private static final String OPERACION = "OPERATION_SERVICE";

    final String BASE_URL = "http://192.168.1.36/TrazaAppServer/trazaapp/";

    static final String TAG = ServiceCaller.class.getCanonicalName();

    public ServiceCaller() {
        super("CallerServiceTest");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String operation = intent.getStringExtra(OPERACION);
        String ruta = intent.getStringExtra("ruta");

        Uri builtURI = Uri.parse(BASE_URL + ruta).buildUpon().build();
        InputStream is = null;
        HttpURLConnection conn = null;
        Intent response;
        String contentAsString;
        int responseCode = -1;
        java.util.Calendar cal = java.util.Calendar.getInstance();
        SimpleDateFormat sdf = null;

        try {
            URL requestURL = new URL(builtURI.toString());
            conn = (HttpURLConnection) requestURL.openConnection();
            List<NameValuePair> params = new ArrayList<>();
            switch (operation) {
                case "addPackage":
                    params.add(new BasicNameValuePair("idResource",intent.getStringExtra("resource")));
                    params.add(new BasicNameValuePair("cantidad", intent.getStringExtra("cantidad")));


                    Log.d(TAG,params.toString());

                    response = new Intent(RESPONSE_ACTION);
                    response.putExtra(DonationActivity.OPERACION, operation);
                    response.putExtra(RESPONSE, this.post(BASE_URL + ruta,params));
                    LocalBroadcastManager.getInstance(this).sendBroadcast(response);
                break;

                default:
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();

                    responseCode = conn.getResponseCode();
                    is = conn.getInputStream();
                    contentAsString = convertIsToString(is);
                    Log.d(TAG, contentAsString);

                    response = new Intent(RESPONSE_ACTION);

                    response.putExtra(OPERACION, operation);
                    response.putExtra(RESPONSE, contentAsString);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(response);

                    break;
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    public String convertIsToString(InputStream stream)
            throws IOException, UnsupportedEncodingException {


        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        BufferedReader buffer = new BufferedReader(reader);
        String line = buffer.readLine();
        return line;
    }

    public String post(String posturl, List<NameValuePair> params){

        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(posturl);
            //AÑADIR PARAMETROS

            httppost.setEntity(new UrlEncodedFormEntity(params));

            //Finalmente ejecutamos enviando la info al server/
            HttpResponse resp = httpclient.execute(httppost);
            HttpEntity ent = resp.getEntity();/*y obtenemos una respuesta*/
            Log.d(TAG,resp.getEntity().toString() );

            String text = EntityUtils.toString(ent);
            Log.e(TAG,text);
            return text;

        }
        catch(Exception e) { return "error";}

    }

}

