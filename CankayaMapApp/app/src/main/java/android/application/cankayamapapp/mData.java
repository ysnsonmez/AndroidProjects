package android.application.cankayamapapp;

import java.util.ArrayList;
import java.util.HashMap;

public class mData extends HashMap<String, String> {

    private HashMap<String, String> myHashMapData;


    public mData(String id,String eposta, String aciklama, String a_onemi, String kordinat, String tarihsaat) {

        super();
        myHashMapData = new HashMap<String, String>();
        this.myHashMapData.put("id",id);
        this.myHashMapData.put("eposta", eposta);
        this.myHashMapData.put("aciklama", aciklama);
        this.myHashMapData.put("a_onemi", a_onemi);
        this.myHashMapData.put("kordinat", kordinat);
        this.myHashMapData.put("tarih", tarihsaat);
    }
    // GETTER
    public HashMap getmyHashMapData() {
        return myHashMapData;
    }
    public String getID() {
        return myHashMapData.get("id");
    }
    public String getEposta() {
        return myHashMapData.get("eposta");
    }
    public String getAciklama() {
        return myHashMapData.get("aciklama");
    }
    public String getKoordinat() {
        return myHashMapData.get("kordinat");
    }
    public String getTarihsaat() {
        return myHashMapData.get("tarih");
    }
    public String getA_onemi() {
        return myHashMapData.get("a_onemi");
    }


    // SETTER
    public void setID(String id) {
        this.myHashMapData.put("id",id);
    }
    public void setEposta(String eposta) {
        this.myHashMapData.put("eposta", eposta);
    }
    public void setAciklama(String aciklama) {
        this.myHashMapData.put("aciklama", aciklama);
    }
    public void setKoordinat(String kordinat) {
        this.myHashMapData.put("kordinat", kordinat);
    }
    public void setTarihsaat(String tarihsaat) {
        this.myHashMapData.put("tarihsaat", tarihsaat);
    }
    public void setA_onemi(String a_onemi) {
        this.myHashMapData.put("a_onemi", a_onemi);
    }

}
