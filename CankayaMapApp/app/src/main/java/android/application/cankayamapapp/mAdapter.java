package android.application.cankayamapapp;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Aleko on 08.08.2016.
 */
public class mAdapter extends BaseAdapter implements Filterable {

    private LayoutInflater mInflater = null;
    private ArrayList<mData> mDataArrayList;
    private ArrayList<mData> mDataFiltered;
    private ItemFilter mFilter = new ItemFilter();
    public Resources res;
    mData tempValues = null;

    public mAdapter(Activity activity, ArrayList<mData> records) {

        super();
        //XML'i alıp View'a çevirecek inflater'ı örnekleyelim
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //gösterilecek listeyi de alalım
        this.mDataArrayList = records;
        this.mDataFiltered = records;
    }

    @Override
    public int getCount() {
        return mDataArrayList.size();
    }

    @Override
    public HashMap<String, String> getItem(int i) {
        return (HashMap) mDataArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View satirView;

        satirView = mInflater.inflate(R.layout.expandlv_header, null);

        TextView textViewEposta = (TextView) satirView.findViewById(R.id.textViewEposta);
        TextView textViewAciklama = (TextView) satirView.findViewById(R.id.textViewAciklama);
        TextView textViewKoordinat = (TextView) satirView.findViewById(R.id.textViewKoordinat);
        TextView textViewTarihSaat = (TextView) satirView.findViewById(R.id.textViewTarihSaat);

        mData mDataObj = mDataArrayList.get(i);

        textViewEposta.setText(mDataObj.getEposta());
        textViewAciklama.setText(mDataObj.getAciklama());
        textViewKoordinat.setText(mDataObj.getKoordinat());
        textViewTarihSaat.setText(mDataObj.getTarihsaat());
        //Log.e("tarih",mDataObj.getTarihsaat());

        return satirView;
    }

    public ArrayList<mData> getmDataFiltered() {
        return mDataFiltered;
    }

    @Override
    public Filter getFilter() {
        return this.mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.e("charseq",constraint.toString());
                String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            //Log.e("araylistesi",mAdapter.this.mDataArrayList.toString());

            final ArrayList<mData> list = mAdapter.this.mDataArrayList;
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

            results.values = nlist;
            results.count = nlist.size();
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
}