package application.maps;

import android.graphics.Color;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by Aleko on 11.07.2016.
 */

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private WebSocketClient mWebSocketClient;
    private GoogleMap mMap;
    private String sendcoordinate;
    private int counter = 0;
    private double [] provinceCenter;
    private static double[] coordinate = new double[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MultiDex.install(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng position)
            {
                counter++;
                if (counter > 2)
                {
                    Log.w("COUNTER",Double.toString(counter));
                    counter=0;
                    mMap.clear();
                }else if(counter == 1)
                {
                    Log.w("LOC","1");
                    coordinate[0]=position.longitude; //x1
                    coordinate[1]=position.latitude;  //y1
                    Log.w("loc1 x",Double.toString(coordinate[0]));
                    Log.w("loc1 y",Double.toString(coordinate[1]));
                    mMap.addMarker(new MarkerOptions().position(position));
                    Log.w("COUNTER",Double.toString(counter));
                }
                else
                {
                    Log.w("LOC","2");
                    coordinate[2]=position.longitude; //x2
                    coordinate[3]=position.latitude;  //y2
                    Log.w("loc2 x",Double.toString(coordinate[2]));
                    Log.w("loc2 y",Double.toString(coordinate[3]));
                    mMap.addMarker(new MarkerOptions().position(position));

                    // minx, miny, maxx, maxy
                    if(coordinate[0]<coordinate[2])
                        sendcoordinate=Double.toString(coordinate[0]);
                    else
                        sendcoordinate=Double.toString(coordinate[2]);
                    if(coordinate[1]<coordinate[3])
                        sendcoordinate+=", "+Double.toString(coordinate[1]);
                    else
                        sendcoordinate+=", "+Double.toString(coordinate[3]);
                    if(coordinate[0]>coordinate[2])
                        sendcoordinate+=", "+Double.toString(coordinate[0]);
                    else
                        sendcoordinate+=", "+Double.toString(coordinate[2]);
                    if (coordinate[1]>coordinate[3])
                        sendcoordinate+=", "+Double.toString(coordinate[1]);
                    else
                        sendcoordinate+=", "+Double.toString(coordinate[3]);
                    //  0      1    2     3
                    // minx, miny, maxx, maxy
                    /* String[] recCoor = sendcoordinate.split(",");
                    PolylineOptions rectOptions = new PolylineOptions()
                            .add(new LatLng(Double.valueOf(recCoor[0]), Double.valueOf(recCoor[3])))
                            .add(new LatLng(Double.valueOf(recCoor[0]), Double.valueOf(recCoor[1])))  // North of the previous point, but at the same longitude
                            .add(new LatLng(Double.valueOf(recCoor[2]), Double.valueOf(recCoor[1])))  // Same latitude, and 30km to the west
                            .add(new LatLng(Double.valueOf(recCoor[2]), Double.valueOf(recCoor[3])))  // Same longitude, and 16km to the south
                            .add(new LatLng(Double.valueOf(recCoor[0]), Double.valueOf(recCoor[3]))).color(Color.BLUE); // Closes the polyline.

                   0-3
                            0-1
                                    2-1
                                            2-3
                                                    0-3
                        // Get back the mutable Polyline
                    Polyline polyline = mMap.addPolyline(rectOptions);*/


                    Log.w("COORDİNATE ",sendcoordinate);
                    Log.w("COUNTER",Double.toString(counter));
                    connectWebSocket();
                    //mMap.addPolygon(new PolygonOptions().add(new LatLng(coordinate[0],coordinate[1])).add(new LatLng(coordinate[2],coordinate[3])).fillColor(Color.RED));
                }
                /*if (counter==2){
                Polygon polygon = mMap.addPolygon(new PolygonOptions()
                        .add(   new LatLng(coordinate[0],coordinate[1]),
                                new LatLng(coordinate[1],coordinate[2]),
                                new LatLng(coordinate[2],coordinate[3]),
                                new LatLng(coordinate[0],coordinate[0]))
                        .strokeColor(Color.YELLOW)
                        .fillColor(Color.TRANSPARENT));}*/
            }
        });
    }

    public boolean parseWKT(String message)
    {
        String[] hexORJ = splitStringEvery(message, 2);
        String[] hexREV = new String[16];
        long buf;
        Log.w("length",String.valueOf(message.length()));

        for (int j = 0, m = 0; j < message.length() / 60; j += 2, m++)    // LENGTH=480/60 =  J ARALIĞI 8
        {
            for (int i = 0; i < 8; i++) {
                hexREV[i] = hexORJ[(50 + (m * 60)) - i];
                hexREV[i + 8] = hexORJ[(58 + (m * 60)) - i];
                Log.w("ORJhex", hexREV[i]);
            }
            buf = parseUnsignedHex(hexREV[0] + hexREV[1] + hexREV[2] + hexREV[3] + hexREV[4] + hexREV[5] + hexREV[6] + hexREV[7]);
            provinceCenter[j] = Double.longBitsToDouble(buf);

            buf = parseUnsignedHex(hexREV[8] + hexREV[9] + hexREV[10] + hexREV[11] + hexREV[12] + hexREV[13] + hexREV[14] + hexREV[15]);
            provinceCenter[j + 1] = Double.longBitsToDouble(buf);

            Log.w("Şehir Sayısı:", Integer.toString(j));
           // Log.w("J+1", Integer.toString(j + 1));

            Log.w("coordinate 1", Double.toString(provinceCenter[j]));  // y
            Log.w("coordinate 2", Double.toString(provinceCenter[j + 1]));  // x
        }

        MapsActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int k = 0; k < provinceCenter.length ;k += 2)
                    mMap.addMarker(new MarkerOptions().position(new LatLng(provinceCenter[k+1], provinceCenter[k]))
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).flat(true));
            }
        });
        return true;
    }

    public String[] splitStringEvery(String s, int interval) {
        int arrayLength = (int) Math.ceil(((s.length() / (double)interval)));
        String[] result = new String[arrayLength];

        int j = 0;
        int lastIndex = result.length - 1;
        for (int i = 0; i < lastIndex; i++) {
            result[i] = s.substring(j, j + interval);
            j += interval;
        } //Add the last bit
        result[lastIndex] = s.substring(j);

        return result;
    }

    public static long parseUnsignedHex(String text) {
        if (text.length() == 16) {
            return (parseUnsignedHex(text.substring(0, 1)) << 60) | parseUnsignedHex(text.substring(1));
        }
        return Long.parseLong(text, 16);
    }

    private void connectWebSocket() {

        URI uri;
        try {
            Log.w("connect","SERVER");
            uri = new URI("ws://212.156.70.230:9060/TASK/service.js");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.w("Websocket", "Opened");
                mWebSocketClient.send(sendcoordinate);
            }

            @Override
            public void onMessage(String s) {
                String message = s;
                Log.w("RESPONSE",message);
                provinceCenter=new double[message.length()/60];
                Log.w("boyut",Integer.toString(message.length()/60));
                parseWKT(message);
               // mWebSocketClient.close();
              /*  MapsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(ret)
                            for (int k = 0; k < (message.length() / 60) ;k += 2)
                            mMap.addMarker(new MarkerOptions().position(new LatLng(provinceCenter[k+1], provinceCenter[k])));
                    }
                });*/
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }
}



