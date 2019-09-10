package com.skripsi.kml;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.skripsi.kml.adapter.RegionsAdapter;
import com.skripsi.kml.model.Regions;

import org.json.JSONArray;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listview;
    String info, tutorial, region;
    Context context;
    ArrayList<Regions> list = new ArrayList<Regions>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (ListView)findViewById(R.id.listview);
        Bundle extras = getIntent().getExtras();
        info = extras.getString("info");
        tutorial = extras.getString("tutorial");
        region = extras.getString("region");
        context = this;
        try {
            JSONArray array = new JSONArray(region);
            for(int i=0;i<array.length();i++){
                String name = array.getJSONObject(i).getString("name");
                String description = array.getJSONObject(i).getString("description");
                String file = array.getJSONObject(i).getString("url");
                String points = array.getJSONObject(i).getString("points");
                list.add(new Regions(name, description, file, points));
            }
            listview.setAdapter(new RegionsAdapter(context, list));
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    intent.putExtra("title", list.get(position).getTitle());
                    intent.putExtra("description", list.get(position).getDescription());
                    intent.putExtra("file", list.get(position).getFile());
                    intent.putExtra("points", list.get(position).getPoints());
                    startActivity(intent);
                }
            });
        }catch (Exception e){

        }

    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        switch (id) {
            case R.id.info:
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                intent.putExtra("content",info);
                startActivity(intent);
                break;
            case R.id.tutorial:
                Intent intent1 = new Intent(MainActivity.this, TutorialActivity.class);
                intent1.putExtra("content",tutorial);
                startActivity(intent1);
                break;
            case R.id.warning:
                Intent intent2 = new Intent(MainActivity.this, WarningActivity.class);
                intent2.putExtra("content",tutorial);
                startActivity(intent2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
