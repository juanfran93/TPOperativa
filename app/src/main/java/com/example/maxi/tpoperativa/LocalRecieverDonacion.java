package com.example.maxi.tpoperativa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by Flia. Ferreira on 26/01/2018.
 */

class LocalRecieverDonacion extends BroadcastReceiver {

    private DonationActivity activityDonation;
    private static final String TAG = LocalBroadcastManager.class.getCanonicalName();
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    public LocalRecieverDonacion(DonationActivity activityDonation) {
        this.activityDonation = activityDonation;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onReceive(Context context, Intent intent) {
        String operation = intent.getStringExtra(ServiceCaller.OPERACION);
        switch (operation) {
            case "getresources":

                try {

                    JSONObject json = new JSONObject(intent.getStringExtra(ServiceCaller.RESPONSE));

                    JSONArray jsonArray = new JSONArray(json.getString("resources"));

                    HashMap<String, Integer> recursos = new HashMap<>();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonRecurso = jsonArray.getJSONObject(i);

                        Integer id = jsonRecurso.getInt("id");
                        String recurso = jsonRecurso.getString("nombre");

                        recursos.put(recurso, id);
                    }
                    activityDonation.setAutoTextResources(recursos);


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "addPackage":
                Log.e(TAG, intent.getStringExtra(ServiceCaller.RESPONSE));
                JSONObject json = null;
                Bitmap bitmap = null;
                try {
                    json = new JSONObject(intent.getStringExtra(ServiceCaller.RESPONSE));

                    int id = json.getInt("id");
                    Log.d(TAG, Integer.toString(id));

                    QRCodeWriter qrCodeWriter = new QRCodeWriter();
                    BitMatrix bitMatrix = qrCodeWriter.encode(Integer.toString(id), BarcodeFormat.QR_CODE, 200, 200);
                    bitmap = createBitmap(bitMatrix);
                    activityDonation.notifySuccess(json.getString("mensaje"), bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
        }
    }

    private Bitmap createBitmap(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = matrix.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
