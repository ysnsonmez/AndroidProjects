package android.application.cankayamapapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class ExpandListViewAdapter extends BaseExpandableListAdapter implements Filterable
{
    public ArrayList<mData> list_header;
    public ArrayList<mData> mDataFiltered;
    private ItemFilter mFilter = new ItemFilter();
    public Context context;
    public TextView txtEposta;
    public TextView txtAciklama;
    public TextView txtKoordinat;
    public TextView txtTarih;
    public Button btnGun;
    public Button btnGit;
    public LayoutInflater inflater;

                                                        // mDataArrayList
    public ExpandListViewAdapter(Context context, ArrayList<mData> list_header) {
        super();
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.list_header = list_header;
    }

    @Override
    public int getGroupCount() {return list_header.size();}

    @Override
    public int getChildrenCount(int i) {return 1;}

    @Override
    public Object getGroup(int i) {return list_header.get(i);}

    @Override
    public Object getChild(int i, int i1) {return 1;}

    @Override
    public long getGroupId(int i) {return i;}

    @Override
    public long getChildId(int i, int i1) {return i1;}

    @Override
    public boolean hasStableIds() {return true;}

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if(view == null)
        {
            inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandlv_header,null);
        }
        txtEposta = (TextView) view.findViewById(R.id.textViewEposta);
        txtAciklama = (TextView) view.findViewById(R.id.textViewAciklama);
        txtTarih = (TextView) view.findViewById(R.id.textViewTarihSaat);
        txtKoordinat = (TextView) view.findViewById(R.id.textViewKoordinat);
        mData temp= (mData)this.getGroup(i);
        txtEposta.setText(temp.getEposta());
        txtAciklama.setText(temp.getAciklama());
        txtTarih.setText(temp.getTarihsaat());
        txtKoordinat.setText(temp.getKoordinat());
        return view;
    }

    @Override
    public View getChildView(final int i, int i1, boolean b, View view, final ViewGroup viewGroup) {
        if(view==null)
        {
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandlv_child, null);
        }
        ExpandListViewAdapter.this.btnGun = (Button) view.findViewById(R.id.btnGuncel);
        ExpandListViewAdapter.this.btnGit = (Button) view.findViewById(R.id.btnGit);

        btnGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mData mDataKordi = (mData) ExpandListViewAdapter.this.getGroup(i);
                    String[] kor = mDataKordi.getKoordinat().split(",");
                    double lati = Double.parseDouble(kor[0]);
                    double longti = Double.parseDouble(kor[1]);

                    Log.e("kordiler", String.valueOf(lati) + ", " + String.valueOf(longti));
                    MainActivity.mMap.addMarker(new MarkerOptions().position(new LatLng(lati, longti)));

                    MainActivity.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lati, longti), 15));
                    MainActivity.drawer.closeDrawer(GravityCompat.START);
                    MainActivity.deleteButton.setVisibility(View.VISIBLE);
            }
        });

        btnGun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final mData mDataKordi = (mData) ExpandListViewAdapter.this.getGroup(i);

                final Dialog customDialog = new Dialog(MainActivity.context);
                customDialog.setContentView(R.layout.custom_dialog);
                customDialog.setTitle("Güncelleme !");
                customDialog.show();
                final TextView etAciklama=(TextView) customDialog.findViewById(R.id.etAciklama);
                final Spinner sAciklamaOnem = (Spinner) customDialog.findViewById(R.id.sAciklamaOnemi);
                final List<String> list=new ArrayList<String>();
                list.add("1");
                list.add("2");
                list.add("3");
                list.add("4");
                list.add("5");
                ArrayAdapter<String> adp= new ArrayAdapter(customDialog.getContext(),android.R.layout.simple_list_item_1, list);
                sAciklamaOnem.setAdapter(adp);


                sAciklamaOnem.setSelection(((ArrayAdapter<String>)sAciklamaOnem.getAdapter()).getPosition(mDataKordi.getA_onemi()));
                etAciklama.setText(mDataKordi.getAciklama());

                Button buttonKaydet = (Button) customDialog.findViewById(R.id.btKaydet);
                buttonKaydet.setText("GÜNCELLE");
                buttonKaydet.setOnClickListener(new View.OnClickListener() {
                    @Override
                        public void onClick(View view) {
                        //TextInputEditText etAciklama = (TextInputEditText ) customDialog.findViewById(R.id.etAciklama);

                        //Bundle extras = MainActivity.getIntent().getExtras();
                        //String emailandpass = extras.getString("send_string");

                        //String email=emailandpass.split(" ")[0];
                        //String pass=emailandpass.split(" ")[1];
                        mData mDataOb = (mData) ExpandListViewAdapter.this.getGroup(i);
                        Log.e("11111111",String.valueOf(i));
                        SimpleDateFormat dateFormat=new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                        GregorianCalendar gcalender=new GregorianCalendar();


                        String onem = sAciklamaOnem.getSelectedItem().toString();
                        String aciklama=etAciklama.getText().toString();
                        String tarih = dateFormat.format(gcalender.getTime());  // 24-8-2014 02:20:10
                        String kordinat = mDataOb.getKoordinat();
                        Log.e("kordinatsss",kordinat);
                        String email=mDataOb.getEposta();
                        int id=Integer.valueOf(mDataOb.getID());

                        //db recording
                        //     public void updateRecord(int id, String a_onem, String tarih, String koordinat, String aciklama, String eposta)
                        MainActivity.db.updateRecord(id, onem, tarih, kordinat, aciklama, email);

                        // ARRAY LİST TO RECORDS
                        //ArrayList list = MainActivity.db.getRecordAList();
                        //MainActivity.listviewInflate(list, MainActivity.mDataArrayList);

                        customDialog.dismiss();
                        ExpandListViewAdapter.this.listviewInflate(MainActivity.db.getRecordAList(),MainActivity.mDataArrayList);

                    }
                });
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }


    // İTEM FİLTRELEME
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.e("charseq",constraint.toString());
            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            //Log.e("araylistesi",mAdapter.this.mDataArrayList.toString());

            final ArrayList<mData> list = ExpandListViewAdapter.this.list_header;
            int count = list.size();
            final ArrayList<mData> nlist = new ArrayList<mData>(count);

//String eposta, String aciklama, String a_onemi, String koordinat, String tarihsaat

            for (int i = 0; i < count; i++) {

                HashMap mDataTemp = list.get(i);
                Log.w("tamamı",mDataTemp.get("a_onemi").toString());

                if (mDataTemp.get("a_onemi").toString().toLowerCase().contains(filterString)) {

                    Log.e("if","if içi");

                    mData tmp = new mData(mDataTemp.get("id").toString(),
                            mDataTemp.get("eposta").toString(),
                            mDataTemp.get("aciklama").toString(),
                            mDataTemp.get("a_onemi").toString(),
                            mDataTemp.get("kordinat").toString(),
                            mDataTemp.get("tarih").toString());
                    nlist.add(tmp);

                    Log.e("tmp",tmp.getA_onemi().toString());
                }
            }

            synchronized (this) {
                results.values = nlist;
                results.count = nlist.size();
            }
            return results;

        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mDataFiltered.clear();
            mDataFiltered = (ArrayList<mData>) results.values;
            notifyDataSetChanged();

        }
    }

    //   Filtrelenmiş veri                    // Boş veri
    public void listviewInflate(ArrayList arrayList, ArrayList<mData> mDataArrayList) {

        mDataArrayList.clear();
        //mData objesi dolduruluyor adapter nesnesine ekleniyor
        for (int i = 0; i < arrayList.size(); i++) {

            HashMap<String,String> tmp= (HashMap<String, String>) arrayList.get(i);
//String id,String eposta, String aciklama, String a_onemi, String koordinat, String tarihsaat
            mData mDatatemp = new mData(tmp.get("id"),
                    tmp.get("eposta"),
                    tmp.get("aciklama"),
                    tmp.get("a_onemi"),
                    tmp.get("kordinat"),
                    tmp.get("tarih"));

            mDataArrayList.add(mDatatemp);
            //Log.e("Inflater",mDataArrayList.get(i).getmyHashMapData().get("kordinat").toString());
        }
        MainActivity.expandListViewAdapter = new ExpandListViewAdapter(MainActivity.context, mDataArrayList);
        MainActivity.elvRecordList.setAdapter(MainActivity.expandListViewAdapter);
    }
}



