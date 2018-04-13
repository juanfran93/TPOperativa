package Funcionalidad;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lucho on 13/04/2018.
 */

public class PointInfoList extends ArrayList {

    private ArrayList<PointInfo> nodos;
    private HashMap<Integer,ArrayList<PointInfo>> paquetesHASH;
    private HashMap<Integer,Pair<Integer,Integer>> padreHijos;

    public PointInfoList(ArrayList<PointInfo> nodos){
        this.nodos = nodos;
        crearHashs();
    }

    private void crearHashs(){

        ArrayList<PointInfo> nodosDeKey = new ArrayList<PointInfo>();

        for (PointInfo p : nodos){
            Integer key = p.getId_paquete();
            if (paquetesHASH.containsKey(key)){
                nodosDeKey.add(p);
            }else{

            }
        }
    }
}
