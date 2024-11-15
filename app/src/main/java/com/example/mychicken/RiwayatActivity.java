package com.example.mychicken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mychicken.Adapter.MenuAdapter;
import com.example.mychicken.Adapter.NotaAdapter;
import com.example.mychicken.Class.DaftarMenu;
import com.example.mychicken.Class.DaftarNota;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RiwayatActivity extends AppCompatActivity {

    NotaAdapter notaAdapter;
    ArrayList<DaftarNota> listNota;
    RecyclerView recView;
    String urlStr, urlTampil, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);

        SharedPreferences prefer =
                getSharedPreferences("MYDATA",MODE_PRIVATE);
        urlStr = prefer.getString("urlStr","");
        username = prefer.getString("username","");
        urlTampil = urlStr + "/android/tampil_nota2.php";
        recView = findViewById(R.id.recView);
        loadJson();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,1,GridLayoutManager.VERTICAL,false);
        recView.setLayoutManager(gridLayoutManager);
    }

    private void loadJson() {
        RequestQueue queue = Volley.newRequestQueue(RiwayatActivity.this);

        StringRequest sendData = new StringRequest(Request.Method.POST, urlTampil,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    ArrayList<DaftarNota> stocks = new ArrayList<>();
                    for(int i =0; i < jsonArray.length();i++)
                    {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        stocks.add(new DaftarNota(obj.getInt("no_nota"), obj.getString("tgl_jual"), obj.getDouble("total_biaya"),obj.getDouble("pembayaran"), obj.getInt("status")));
                    }
                    notaAdapter = new NotaAdapter(RiwayatActivity.this, stocks);
                    listNota = new ArrayList<>(stocks);
                    recView.setAdapter(notaAdapter);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley", "error : " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("username", username);
                return map;
            }
        };
        queue.add(sendData);
    }

    private void downloadJSON(final String urlWebService) {
        class DownloadJSON extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    loadIntoListView(s);
//                    Toast.makeText(getApplicationContext(), "Behasil Mengambil Data", Toast.LENGTH_SHORT).show();
                    Log.d("Response: ", "Berhasil");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new
                            InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json+"\n");
                        Log.d("Response: ", "> " + json);
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return "";
                }
            }
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }

    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        ArrayList<DaftarNota> stocks = new ArrayList<>();
        for(int i =0;i < jsonArray.length();i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            stocks.add(new DaftarNota(obj.getInt("no_nota"), obj.getString("tgl_jual"), obj.getDouble("total_biaya"),obj.getDouble("pembayaran"), obj.getInt("status")));
        }
        listNota = new ArrayList<>(stocks);
        notaAdapter = new NotaAdapter(this, listNota);
        recView.setAdapter(notaAdapter);
//        this.pilih = new boolean[jsonArray.length()];
    }
}