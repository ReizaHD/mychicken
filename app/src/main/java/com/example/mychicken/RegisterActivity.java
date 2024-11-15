package com.example.mychicken;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mychicken.Util.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    ProgressDialog pd;
    Button btnLogin,btnDaftar;
    EditText nama,username,password;
    String urlStr, urlDaftar, userStr, namaStr, passStr;
    ArrayList<String> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        data = new ArrayList<>();
        pd = new ProgressDialog(RegisterActivity.this);

        SharedPreferences prefer =
                getSharedPreferences("MYDATA",MODE_PRIVATE);
        urlStr = prefer.getString("urlStr",null);
        urlDaftar = urlStr+"/android/tambah_konsumen.php";

        nama = (EditText) findViewById(R.id.inputNama);
        username = (EditText) findViewById(R.id.inputUser);
        password = (EditText) findViewById(R.id.inputPass);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnDaftar = (Button) findViewById(R.id.btnDaftar);

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userStr = username.getText().toString();
                namaStr = nama.getText().toString();
                passStr = password.getText().toString();

                Log.d("LENGTH", String.valueOf(userStr.trim().length()));
                Log.d("LENGTH", String.valueOf(namaStr.trim().length()));
                Log.d("LENGTH", String.valueOf(passStr.trim().length()));
                if(userStr.trim().length() > 0 && namaStr.trim().length() > 0 && passStr.trim().length() > 0) {
                    data.add(userStr);
                    data.add(namaStr);
                    data.add(passStr);
                    AppController.interactData(urlDaftar, RegisterActivity.this, 1, data);
                    Intent iLogin = new Intent(RegisterActivity.this,
                            LoginActivity.class);
                    startActivity(iLogin);
                } else  {
                    Toast.makeText(RegisterActivity.this, "Semua form harus diisi", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iLogin = new Intent(RegisterActivity.this,
                        LoginActivity.class);
                startActivity(iLogin);
            }
        });
    }

//    private void interactData(String URL) {
//        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
//
//        pd.setMessage("Menyimpan Data");
//        pd.setCancelable(false);
//        pd.show();
//
//        StringRequest sendData = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                pd.cancel();
//                Toast.makeText(RegisterActivity.this,"Mencoba", Toast.LENGTH_SHORT).show();
//                try {
//                    JSONObject res = new JSONObject(response);
//                    Toast.makeText(RegisterActivity.this, "Pesan :" + res.getString("message"), Toast.LENGTH_SHORT).show();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                pd.cancel();
//                Toast.makeText(RegisterActivity.this,"Pesan : Gagal Update Data", Toast.LENGTH_SHORT).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> map = new HashMap<>();
//                map.put("user_id",username.getText().toString());
//                map.put("nama",nama.getText().toString());
//                map.put("pass",password.getText().toString());
//                return map;
//            }
//        };
//        queue.add(sendData);
//    }
}