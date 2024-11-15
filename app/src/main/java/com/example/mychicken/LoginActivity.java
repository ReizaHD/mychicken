package com.example.mychicken;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.mychicken.Class.DataUser;
import com.example.mychicken.Util.AppController;
import com.example.mychicken.Util.MD5;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogin,btnRegis, btnConfirm;
    private EditText username, password, url;
    private View layoutLogin, layoutUrl;
    private String urlStr, urlKonsumen;
    private ArrayList<DataUser> userData;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        userData = new ArrayList<>();

        layoutLogin = getLayoutInflater().inflate(R.layout.activity_login,null);
        layoutUrl = getLayoutInflater().inflate(R.layout.url_form,null);

        SharedPreferences prefer =
                getSharedPreferences("MYDATA",MODE_PRIVATE);
        urlStr = prefer.getString("urlStr","");
        urlKonsumen = urlStr +"/android/tampil_konsumen.php";

        if(urlStr==null){
            setContentView(layoutUrl);
        }else{
            setContentView(layoutLogin);
        }

        getSupportActionBar().hide();
        AppController.downloadJSON(urlKonsumen, userData);
        int lol = userData.size();
        Log.d("Response",String.valueOf(lol));

        username = (EditText) layoutLogin.findViewById(R.id.user);
        password = (EditText) layoutLogin.findViewById(R.id.pass);
        url = (EditText) layoutUrl.findViewById(R.id.urlForm);
        btnLogin = (Button) layoutLogin.findViewById(R.id.btnLogin);
        btnRegis = (Button) layoutLogin.findViewById(R.id.btnRegis);
        btnConfirm = (Button) layoutUrl.findViewById(R.id.btnConfirm);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                user.replace(" ","");
                System.out.println("User : "+user+" = "+username + ", dan pass = "+pass+"="+
                        password);
                if(urlStr!=""){
                    if ((user != null && pass != null) && (searchID(user) &&
                            searchPass(pass)))
                    { //username and password are present, do your stuff
                        Intent iMain = new Intent(LoginActivity.this,
                                MainActivity.class);
                        Bundle extra = new Bundle();
                        extra.putSerializable("userdataBundle",userData.get(position));
                        iMain.putExtra("userdata",extra);
                        startActivity(iMain);
                    }else
                    {       Toast.makeText(getApplicationContext(),
                            "Belum register....!!! atau User & Password tidak cocok...!!",
                            Toast.LENGTH_SHORT).show();
                    }
                }else
                {
                    Toast.makeText(getApplicationContext(),
                            "URL masih kosong",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iRegis = new Intent(LoginActivity.this,
                        RegisterActivity.class);
                startActivity(iRegis);
                finish();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefer =
                        getSharedPreferences("MYDATA",MODE_PRIVATE);
                SharedPreferences.Editor edit = prefer.edit();
                urlStr = url.getText().toString();
                if(urlStr==""){
                    Toast.makeText(getApplicationContext(),
                            "URL belum diisi",
                            Toast.LENGTH_SHORT).show();
                } else {
                    edit.putString("urlData", urlStr);
                    urlStr = "http://" + urlStr;
                    urlKonsumen = urlStr +"/android/tampil_konsumen.php";
                    edit.putString("urlStr", urlStr);
                    edit.commit();
                    Log.d("urlString", urlStr);
                    AppController.downloadJSON(urlKonsumen, userData);
                    setContentView(layoutLogin);
                }
            }
        });



    }

    public boolean searchID(String user_id){
        int size = userData.size();
        boolean found = false;
        String idData;
        for(int i=0;i<size;i++){
            idData = userData.get(i).getId();
            if(user_id.equals(idData)) {
                Log.d("Response COCOK","Berhasil");
                position = i;
                found = true;
                break;
            }

        }
        return found;
    }

    public boolean searchPass(String pass){
        boolean found;
        String MD5PASS, passData;

        found= false;

        passData = userData.get(position).getPass();
        MD5PASS = MD5.getMd5(pass);

        if(MD5PASS.equals(passData)) {
            found = true;
        }
        return found;
    }


    public void viewUrl(View view){
        setContentView(layoutUrl);
        SharedPreferences prefer =
                getSharedPreferences("MYDATA",MODE_PRIVATE);
        String dataUrl = prefer.getString("urlData","");
        url.setText(dataUrl);
        Log.d("data URL",dataUrl);
    }

//    public void downloadJSON(final String urlWebService) {
//        class DownloadJSON extends AsyncTask<Void, Void, String> {
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//            }
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//
//                try {
//                    loadIntoListView(s);
//                    Log.d("Response: ", "Berhasil");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//            @Override
//            protected String doInBackground(Void... voids) {
//                try {
//                    URL url = new URL(urlWebService);
//                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                    StringBuilder sb = new StringBuilder();
//                    BufferedReader bufferedReader = new BufferedReader(new
//                            InputStreamReader(con.getInputStream()));
//                    String json;
//                    while ((json = bufferedReader.readLine()) != null) {
//                        sb.append(json+"\n");
//                        Log.d("Response: ", "> " + json);
//                    }
//                    return sb.toString();
//                } catch (Exception e) {
//                    return "";
//                }
//            }
//        }
//        DownloadJSON getJSON = new DownloadJSON();
//        getJSON.execute();
//    }
//
//    private void loadIntoListView(String json) throws JSONException {
//
//        JSONArray jsonArray = new JSONArray(json);
//        ArrayList<DataUser> stocks = new ArrayList<>();
//        for(int i =0;i < jsonArray.length();i++) {
//            JSONObject obj = jsonArray.getJSONObject(i);
//            userData.add(new DataUser(obj.getString("nama"), obj.getString("user_id"), obj.getString("password")));
//        }
//    }

}
