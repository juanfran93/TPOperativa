package Funcionalidad;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by lucho on 13/04/2018.
 */

public class PointInfoList {

    private ArrayList<PointInfo> nodos;
    private HashMap<Integer,ArrayList<PointInfo>> paquetesHASH ;
    private HashMap<Integer,Integer> hijoPadre;


    public PointInfoList(ArrayList<PointInfo> nodos){
    	this.paquetesHASH = new HashMap<>();
    	this.hijoPadre = new HashMap<>();
        this.nodos = nodos;
        crearHashs();

    }

    private void crearHashs(){
        //ArrayList<PointInfo> nodosDeKey = new ArrayList<PointInfo>();

        for (PointInfo p : nodos) {
            Integer idPaquete = p.getId_paquete();
            Integer padre = p.getId_padre();

            if (paquetesHASH.containsKey(idPaquete)) {
                paquetesHASH.get(idPaquete).add(p);
            } else {
                ArrayList<PointInfo> l = new ArrayList<PointInfo>();
                l.add(p);
                paquetesHASH.put(idPaquete, l);
            }

            if (!hijoPadre.containsKey(idPaquete) && (padre > 0)){
                hijoPadre.put(idPaquete,padre);
            }
        }
        
        System.out.println("hijos padres "+hijoPadre+"\n");
        System.out.println("paquetesHash: "+paquetesHASH+"\n");
    }

    public ArrayList<ArrayList<PointInfo>> ordenarNodos(){

        ArrayList<ArrayList<PointInfo>> salida = new ArrayList<ArrayList<PointInfo>>();
      // {[1,1,1];[1,3,3,3];[1,5,5,5];[3,7,7];[3,8]}
        ArrayList<Integer> keys = new ArrayList<Integer>(paquetesHASH.keySet());
        Collections.sort(keys);

        int nodoActual = 0;
        Integer idPaquete = nodos.get(nodoActual).getId_paquete();

        while (nodoActual < keys.size()){
        	idPaquete = keys.get(nodoActual);
        	//System.out.println("idPaq: "+idPaquete);
            ArrayList<PointInfo> lp = new ArrayList<PointInfo>();  // lista parcial
            if (hijoPadre.containsKey(idPaquete)){                 // si tengo padre, agrego el ultimo padre
                ArrayList<PointInfo> padres = paquetesHASH.get(hijoPadre.get(idPaquete));
                //System.out.println("padres: "+padres);
                PointInfo ultimoPadre = padres.get(padres.size()-1);
                lp.add(ultimoPadre);
            }
            lp.addAll((ArrayList<PointInfo>)paquetesHASH.get(idPaquete));
            System.out.println("lista Parcial: "+lp);
            salida.add(lp);
            nodoActual+=1;
        }
        return salida;
    }
    
    public void imprimir(ArrayList<ArrayList<PointInfo>> ll){
    	
    }

}


/*
*  lp.addAll(paquetesHASH.get(keyActual));                // agrego los primeros
            nodoActual+=paquetesHASH.get(keyActual).size();        //sumo la cantidad de los primeros
            salida.add(new ArrayList<PointInfo>(lp));              // sumo lista parcial a la salida

            lp.clear();

            List<Integer> hijos = padreHijos.get(keyActual);        //

            ArrayList<PointInfo> ul = salida.get(salida.size()); // ultima lista

            for (Integer i : hijos ) {

                lp.add(ul.get(ul.size()));                           // agrego a lp el ultimo point de la ultima lista
                lp.addAll(paquetesHASH.get(i));                       // agrego todos los hijos
            }



* */