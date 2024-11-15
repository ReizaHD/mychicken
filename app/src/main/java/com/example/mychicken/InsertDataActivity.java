package com.example.mychicken;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InsertDataActivity extends AppCompatActivity {

    int hrg, action;
    EditText kode,nama,hrgStr,ket;
//    EditText grb;
    Button insertBtn;
    ProgressDialog pd;
    String urlStr,urlInsert, urlUpdate, urlDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        SharedPreferences prefer =
                getSharedPreferences("MYDATA",MODE_PRIVATE);
        urlStr = prefer.getString("urlStr","");
        urlInsert = urlStr + "/android/insert.php";
        urlUpdate = urlStr + "/android/update.php";

        action = getIntent().getIntExtra("action",0);
        kode = findViewById(R.id.kd_menu);
        nama = findViewById(R.id.nm_menu);
        hrgStr = findViewById(R.id.hrg_menu);
        hrg = getIntent().getIntExtra("hrg",0);
//        gbr = findViewById(R.id.gbr_menu);
        ket = findViewById(R.id.ket_menu);
        insertBtn = findViewById(R.id.btn_insert);
        pd = new ProgressDialog(InsertDataActivity.this);

        if(action==1) {
            insertBtn.setText("UPDATE");
            kode.setEnabled(false);
            kode.setTextColor(getResources().getColor(R.color.gray));
            kode.setText(getIntent().getStringExtra("kode"));
            nama.setText(getIntent().getStringExtra("nama"));
            hrgStr.setText(String.valueOf(hrg));
            ket.setText(getIntent().getStringExtra("ket"));
        }
        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(kode.getText().toString()=="" || nama.getText().toString()=="" || hrgStr.getText().toString() == "" || ket.getText().toString()=="") {
                    if(action==1) {
                        interactData(urlUpdate);
                    }else{
                        interactData(urlInsert);
                    }
                } else {
                    Toast.makeText(InsertDataActivity.this,"Kolom ada yang kosong", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void interactData(String URL) {
        RequestQueue queue = Volley.newRequestQueue(InsertDataActivity.this);

        pd.setMessage("Menyimpan Data");
        pd.setCancelable(false);
        pd.show();

        StringRequest sendData = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.cancel();
                Toast.makeText(InsertDataActivity.this,"Mencoba", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject res = new JSONObject(response);
                    Toast.makeText(InsertDataActivity.this, "Pesan :" + res.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(InsertDataActivity.this, MainActivity.class));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.cancel();
                Toast.makeText(InsertDataActivity.this,"Pesan : Gagal Update Data", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id_menu",kode.getText().toString());
                map.put("nama",nama.getText().toString());
                map.put("harga",hrgStr.getText().toString());
                map.put("ket",ket.getText().toString());
                return map;
            }
        };
        queue.add(sendData);
    }

}