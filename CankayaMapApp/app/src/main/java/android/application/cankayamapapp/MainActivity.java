package android.application.cankayamapapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.StaticLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , OnMapReadyCallback {

    public static GoogleMap mMap;
    LatLng center;
    int counter = 0;

    static Database db;
    static ExpandListViewAdapter expandListViewAdapter;
    static ExpandableListView elvRecordList;
    NavigationView navigationView;
    Spinner sNavDrawer;
    mAdapter mAdapter;
    static ArrayList<mData> mDataArrayList;
    public static DrawerLayout drawer;
    static FloatingActionButton deleteButton;
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;

        db = new Database(getApplicationContext());
        deleteButton = (FloatingActionButton) findViewById(R.id.delete);
        final FloatingActionButton addingButton = (FloatingActionButton) findViewById(R.id.adding);

        //db.delTable("kayitlar");

        // map loading
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // nav view setting
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        // set spinner on nav drawer
        View hView =  navigationView.getHeaderView(0);
        sNavDrawer = (Spinner) hView.findViewById(R.id.sFiltre);
        setsFiltre(sNavDrawer);

        // get array list to records and set listview on nav drawer
        final ArrayList Recordlist = db.getRecordAList();
        elvRecordList = (ExpandableListView) navigationView.findViewById(R.id.explv);


        // aynı anda iki view açık olmasın.
        elvRecordList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousItem )
                    elvRecordList.collapseGroup(previousItem );
                previousItem = groupPosition;
            }
        });

        // mDataArrayList bellek ayırma
        mDataArrayList = new ArrayList<mData>(Recordlist.size());

        listviewInflate(Recordlist, mDataArrayList);

        // sFiltre filtering on nav drawer
        sNavDrawer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                final String items = adapterView.getSelectedItem().toString();
                ArrayList dbRecordAList = db.getRecordAList();
                Log.e("seçilen item",items);
                // Filtreleme
                if (items.equals("Bütün Kayıtlar")) {
                   // mDataArrayList = new ArrayList<mData>(dbRecordAList.size());
                    listviewInflate(dbRecordAList, mDataArrayList);
                } else {

                   // ExpandListViewAdapter mExpandListViewAdapter = new ExpandListViewAdapter(MainActivity.this, dbRecordAList);
                   // mExpandListViewAdapter.getFilter().filter(items);
                   // mDataArrayList = new ArrayList<mData>(mExpandListViewAdapter.mDataFiltered.size());

                    ArrayList<HashMap<String,String>> kayıtlar = db.getA_Onemi_Record(Integer.valueOf(items));
                    Log.e("kayıtlarr",kayıtlar.toString());
                    listviewInflate(kayıtlar, mDataArrayList);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        addingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final  FloatingActionButton deleteButton=(FloatingActionButton) findViewById(R.id.delete);
                deleteButton.setVisibility(View.VISIBLE);
                addingButton.setImageResource(R.drawable.check); // button stil değiştirme

                if (counter == 0) {

                    center = mMap.getProjection().getVisibleRegion().latLngBounds.getCenter();

                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(center.latitude, center.longitude))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                            .draggable(true));

                    // marker draggle
                    mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                        @Override
                        public void onMarkerDragStart(Marker marker) {
                        }

                        @Override
                        public void onMarkerDrag(Marker marker) {
                        }

                        @Override
                        public void onMarkerDragEnd(Marker marker) {
                            center = marker.getPosition();
                            marker.setPosition(new LatLng(center.latitude, center.longitude));
                        }
                    });
                    counter++;
                } else {

                    final Dialog customDialog = new Dialog(MainActivity.this);
                    customDialog.setContentView(R.layout.custom_dialog);
                    customDialog.setTitle("Eklenecekler");
                    customDialog.show();

                    // SET SPINNER ON CUSTOM DIALOG
                    final Spinner sAciklamaOnem = (Spinner) customDialog.findViewById(R.id.sAciklamaOnemi);
                    final List<String> list=new ArrayList<String>();
                    list.add("1");
                    list.add("2");
                    list.add("3");
                    list.add("4");
                    list.add("5");
                    ArrayAdapter<String> adp= new ArrayAdapter(customDialog.getContext(),android.R.layout.simple_list_item_1, list);
                    sAciklamaOnem.setAdapter(adp);

                    Button buttonKaydet = (Button) customDialog.findViewById(R.id.btKaydet);
                    buttonKaydet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            TextInputEditText etAciklama = (TextInputEditText ) customDialog.findViewById(R.id.etAciklama);
                            Bundle extras = getIntent().getExtras();
                            String emailandpass = extras.getString("send_string");

                            String email=emailandpass.split(" ")[0];
                            //String pass=emailandpass.split(" ")[1];

                            SimpleDateFormat dateFormat=new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                            GregorianCalendar gcalender=new GregorianCalendar();

                            String onem = sAciklamaOnem.getSelectedItem().toString();
                            String aciklama = etAciklama.getText().toString();
                            String tarih = dateFormat.format(gcalender.getTime());  // 24-8-2014 02:20:10
                            Log.e("deger",new DecimalFormat("##.######").format(center.latitude).toString());
                            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
                            otherSymbols.setDecimalSeparator('.');
                            otherSymbols.setGroupingSeparator(',');
                            double lati = Double.valueOf(new DecimalFormat("##.######",otherSymbols).format(center.latitude));
                            double longti = Double.valueOf(new DecimalFormat("##.######",otherSymbols).format(center.longitude));
                            String kordinat = Double.toString(lati) + ", " + Double.toString(longti);

                            //db recording
                            db.addRecord(aciklama, onem, kordinat, email, tarih);

                            // ARRAY LİST TO RECORDS
                            ArrayList list = db.getRecordAList();
                            listviewInflate(list, mDataArrayList);

                            Log.w("DB CREATE", "BAŞARILI");
                            customDialog.dismiss();
                            mMap.clear();
                            deleteButton.setVisibility(View.INVISIBLE);
                            addingButton.setImageResource(R.drawable.plus);
                        }
                    });
                }
            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (counter > 0) {
                    addingButton.setImageResource(R.drawable.plus); // button stil değiştirme
                    mMap.clear();
                    //final  FloatingActionButton deleteButton=(FloatingActionButton) findViewById(R.id.delete);
                    deleteButton.setVisibility(View.INVISIBLE);
                    counter = 0;
                } else {
                    mMap.clear();
                    deleteButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }


    public void setsFiltre(Spinner spinner) {
        final List<String> list=new ArrayList<String>();
        list.add("Bütün Kayıtlar");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        ArrayAdapter adp = new ArrayAdapter(spinner.getContext(), android.R.layout.simple_list_item_1, list);
        spinner.setAdapter(adp);
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
        expandListViewAdapter = new ExpandListViewAdapter(MainActivity.this, mDataArrayList);
        elvRecordList.setAdapter(expandListViewAdapter);
    }

    public GoogleMap GetMap() {
        return this.mMap;
    }
}


