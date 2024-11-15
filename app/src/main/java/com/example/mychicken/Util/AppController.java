package com.example.mychicken.Util;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mychicken.Class.DataUser;
import com.example.mychicken.RegisterActivity;

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

public class AppController extends Application {

    //    static String urlStr, urlInsert, urlUpdate, urlDelete;
    static ProgressDialog pd;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static synchronized void downloadJSON(final String urlWebService, final ArrayList<DataUser> userData) {
        class DownloadJSON extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                try {
                    loadIntoListView(s, userData);
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

    private static void loadIntoListView(String json, ArrayList<DataUser> userData) throws JSONException {

        JSONArray jsonArray = new JSONArray(json);
        for(int i =0;i < jsonArray.length();i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            userData.add(new DataUser(obj.getString("nama"), obj.getString("user_id"), obj.getString("password")));
        }
    }

    public static void interactData(String URL, Context ctx, int mode, ArrayList<String> data) {
        pd = new ProgressDialog(ctx);
        RequestQueue queue = Volley.newRequestQueue(ctx);

        pd.setMessage("Menyimpan Data");
        pd.setCancelable(false);

        StringRequest sendData = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.cancel();
                Toast.makeText(ctx,"Mencoba", Toast.LENGTH_SHORT).show();
                try {
                    JSONObject res = new JSONObject(response);

                    Toast.makeText(ctx, "Pesan :" + res.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.cancel();
                Toast.makeText(ctx,"Pesan : Gagal Update Data", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                if(mode==1){
                    map.put("user_id",data.get(0));
                    map.put("nama",data.get(1));
                    map.put("pass",data.get(2));
                } else if(mode==2) {
                    map.put("user_id",data.get(0));
                    map.put("nama",data.get(1));
                    map.put("pass",data.get(2));
                    map.put("old_id",data.get(3));
                } else if(mode ==3) {
                    map.put("user_id",data.get(0));
                    map.put("total_biaya",data.get(1));
                    map.put("bayar",data.get(2));
                }
                return map;
            }
        };
        queue.add(sendData);
    }
}
