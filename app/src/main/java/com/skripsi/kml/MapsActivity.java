package com.skripsi.kml;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.data.kml.KmlContainer;
import com.google.maps.android.data.kml.KmlLayer;
import com.google.maps.android.data.kml.KmlLineString;
import com.google.maps.android.data.kml.KmlPlacemark;
import com.google.maps.android.data.kml.KmlPoint;
import com.google.maps.android.data.kml.KmlPolygon;
import com.google.maps.android.data.kml.KmlRenderer;
import com.google.maps.android.data.kml.KmlStyle;

import org.json.JSONArray;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String title, description, file, points;
    ProgressDialog pDialog;
    Context context;
    String nameFile;
    ArrayList<LatLng> lang = new ArrayList<LatLng>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Bundle extras = getIntent().getExtras();
        context = this;
        title = extras.getString("title");
        description = extras.getString("description");
        file = extras.getString("file");
        points = extras.getString("points");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Mohon tunggu...");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        new DownloadFileFromURL().execute(file);


    }
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                long time= System.currentTimeMillis();
                String root = Environment.getExternalStorageDirectory().toString()+"/TraseDJKA/";
                File dir = new File(root);
                if(!dir.exists()){
                    dir.mkdir();
                }
                nameFile = root+"/"+time+".kml";
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), lenghtOfFile);
                OutputStream output = new FileOutputStream(nameFile);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();


            } catch (Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String file_url) {

            try {
                File file = new File(nameFile);
                FileInputStream fileInputStream = new FileInputStream(file);
                KmlLayer layer = new KmlLayer(mMap, fileInputStream, context);
                layer.addLayerToMap();

                try{
                    JSONArray pointsArray = new JSONArray(points);
                    for(int i=0;i<pointsArray.length();i++){
                        String name = pointsArray.getJSONObject(i).getString("name");
                        String description = pointsArray.getJSONObject(i).getString("description");
                        String latitude = pointsArray.getJSONObject(i).getString("latitude");
                        String longitude = pointsArray.getJSONObject(i).getString("longitude");
                        LatLng llg = new LatLng(Double.parseDouble(longitude), Double.parseDouble(latitude));
                        mMap.addMarker(new MarkerOptions().position(llg).title(name).snippet(description).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                        if(i==0){
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(llg));
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(llg, 10.0f));
                        }
                    }
                }catch (Exception e){

                }

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            pDialog.dismiss();
        }
    }
}
