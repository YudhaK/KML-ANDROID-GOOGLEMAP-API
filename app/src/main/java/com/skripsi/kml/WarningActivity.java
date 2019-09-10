package com.skripsi.kml;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.util.Base64;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.skripsi.kml.utils.Constants;
import com.squareup.picasso.Downloader;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.toolbox.JsonObjectRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class WarningActivity extends AppCompatActivity {
    EditText namaET, nikET, deskripsiET;
    Button takePictBtn,sendBtn;
    ImageView imageIV;
    Context context;
    String tag_json_obj = "json_obj_req";
    ProgressDialog dialog;
    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning);
        context = this;
        dialog = new ProgressDialog(this);
        dialog.setMessage("Mengirim data..");
        dialog.setCancelable(false);
        namaET = (EditText)findViewById(R.id.namaET);
        nikET = (EditText)findViewById(R.id.nikET);
        deskripsiET = (EditText)findViewById(R.id.deskripsiET);
        takePictBtn = (Button)findViewById(R.id.takePictBtn);
        sendBtn = (Button)findViewById(R.id.SendBtn);
        imageIV = (ImageView)findViewById(R.id.imageIV);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(namaET.getText().toString().length()<3){
                    Toast.makeText(context, "Nama minimal 3 karakter!",Toast.LENGTH_LONG).show();
                }else if(nikET.getText().toString().length()<5){
                    Toast.makeText(context, "NIK minimal 5 karakter!",Toast.LENGTH_LONG).show();
                }else if(deskripsiET.getText().toString().length()<5){
                    Toast.makeText(context, "Deskripsi minimal 5 karakter!",Toast.LENGTH_LONG).show();
                }else if(bmp==null){
                    Toast.makeText(context, "Unggah gambar !",Toast.LENGTH_LONG).show();
                }else {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    bmp.recycle();
                    dialog.show();
                    sendData(namaET.getText().toString(),nikET.getText().toString(), deskripsiET.getText().toString(),byteArray);
                }
            }
        });
        takePictBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),11);
            }
        });
    }
    @Override
    public void onResume(){
        super.onResume();
        getSupportActionBar().setTitle("Kirim Laporan");
    }
    private void sendData(String nama, String nik, String deskripsi, byte[] image) {
        JSONObject object = new JSONObject();
        try {
            object.put("nama", nama);
            object.put("nik", nik);
            object.put("content", deskripsi);
            object.put("image", Base64.encodeToString(image, Base64.DEFAULT));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                Constants.LAPORAN, object,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context, "Data terkirim", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        finish();
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                showAlert(error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void showAlert(String message) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                if (data != null)
                {
                    try
                    {

                        bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
                        imageIV.setImageBitmap(bmp);
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED)
            {
                Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
