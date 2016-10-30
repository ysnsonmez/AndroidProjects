package android.application.cankayamapapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Aleko on 28.07.2016.
 */
public class Database extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "sqlite_db";
    private static final String TABLE_NAME = "kayitlar";
    private static String ID = "id";
    private static String A_ONEMI = "a_onemi";
    private static String TARİH = "tarih";
    private static String KOORDİNAT = "kordinat";
    private static String ACIKLAMA = "aciklama";
    private static String EPOSTA = "eposta";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + ACIKLAMA + " TEXT,"
                + A_ONEMI + " TEXT,"
                + KOORDİNAT + " TEXT,"
                + TARİH + " DATE,"
                + EPOSTA + " TEXT" + ")";
        Log.d("SORGU", "SQL" + CREATE_TABLE);
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int Old, int New) {

        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        Log.w("del", "DBBB");
    }



    public void addRecord(String aciklama,String a_onemi,String koordinat,String eposta,String tarih) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(A_ONEMI, a_onemi);
        values.put(TARİH, tarih);
        values.put(KOORDİNAT, koordinat);
        values.put(ACIKLAMA, aciklama);
        values.put(EPOSTA, eposta);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void delRecord(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public ArrayList<HashMap<String,String>> getA_Onemi_Record(int a_onem) {

        ArrayList<HashMap<String,String>> recOnem = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE a_onemi=" + a_onem;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.e("lll",String.valueOf(cursor.getCount()));
        if(cursor.moveToFirst()){
            do {
                //                          String ID            String eposta,       String aciklama, String a_onemi,       String koordinat, String tarihsaat
                mData mDatatemp=new mData(cursor.getString(0),cursor.getString(5),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4));

                Log.e("****","**********");

                Log.e("cursor 0",cursor.getString(0));
                Log.e("cursor 1",cursor.getString(1));
                Log.e("cursor 2",cursor.getString(2));
                Log.e("cursor 3",cursor.getString(3));
                Log.e("cursor 4",cursor.getString(4));
                Log.e("cursor 5",cursor.getString(5));
                recOnem.add(mDatatemp.getmyHashMapData());

        }while (cursor.moveToNext());
        cursor.close();
        db.close();
        Log.e("data",recOnem.toString());

        }
        return recOnem;
    }

    public ArrayList<HashMap<String, String>> getRecordAList() {

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                arrayList.add(map);

            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return arrayList;
    }

    public void updateRecord(int id, String a_onemi, String tarih, String koordinat, String aciklama, String eposta) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(A_ONEMI, a_onemi);
        values.put(TARİH, tarih);
        values.put(KOORDİNAT, koordinat);
        values.put(ACIKLAMA, aciklama);
        values.put(EPOSTA, eposta);

        db.update(TABLE_NAME, values, ID + " = ?", new String[]{String.valueOf(id)});
    }

    public boolean delTable(String table_name) {

        SQLiteDatabase db =this.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(table_name, null, null);
            return true;
        }else
            return false;
    }

}
