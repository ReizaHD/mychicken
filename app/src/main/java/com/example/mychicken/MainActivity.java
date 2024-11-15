package com.example.mychicken;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychicken.Adapter.MenuAdapter;
import com.example.mychicken.Class.DaftarMenu;
import com.example.mychicken.Class.DataUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView listMenu;
    private MenuAdapter menuAdapter;
    private LinearLayout checkoutBtn;
    private DataUser userData;
    private Bundle extra;
    private ArrayList<DaftarMenu> daftarMenu;

    private String urlStr;
    TextView mTotal;
    int total = 0;
    int[] count = {0,0,0,0,0,0};
    int[] harga = {15000,17000,22000,5000,3500,4000};
    boolean[] pilih;
    NumberFormat nf = NumberFormat.getInstance(Locale.US);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.drawable.bg_chicken);
        setContentView(R.layout.activity_main);

        daftarMenu = new ArrayList<>();
        extra = getIntent().getBundleExtra("userdata");
        userData = (DataUser) extra.getSerializable("userdataBundle");
        Log.d("DATAUSER", userData.getNama());

        SharedPreferences prefer =
                getSharedPreferences("MYDATA",MODE_PRIVATE);
        urlStr = prefer.getString("urlStr","");
        SharedPreferences.Editor edit = prefer.edit();
        edit.putString("username",userData.getId());
        edit.commit();



        mTotal = findViewById(R.id.checkout);
        listMenu = findViewById(R.id.recycleView);
        checkoutBtn = findViewById(R.id.checkoutBtn);
        downloadJSON(urlStr+"/android/tampil.php");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);
        listMenu.setLayoutManager(gridLayoutManager);

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                total = menuAdapter.getTotal();
                daftarMenu = new ArrayList<>(menuAdapter.getDaftarMenu());
                if(total>0) {
                    int iterate=0;
                    int size = daftarMenu.size();
                    while(iterate<size){
                        if(!daftarMenu.get(iterate).getPilih()){
                            daftarMenu.remove(iterate);
                            iterate = -1;
                            size = daftarMenu.size();
                        }
                        iterate++;
                    }
                    Log.d("DAFTAR MENU SIZEE",String.valueOf(daftarMenu.size()));
                    Log.d("DAFTAR MENU", String.valueOf(daftarMenu));
                    Intent i = new Intent(MainActivity.this, CheckoutActivity.class);
                    Bundle extra = new Bundle();
                    extra.putSerializable("daftarMenu",daftarMenu);
                    i.putExtra("daftarMenuArgs", extra);
                    i.putExtra("total", total);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(),"Keranjang masih kosong",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menu){
		switch(menu.getItemId()){
            case R.id.insert:
                startActivity(new Intent(MainActivity.this,InsertDataActivity.class));
                break;

            case R.id.call_center:
                Intent intentCall = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:085102316099"));
                if (intentCall.resolveActivity(getPackageManager()) != null) {
                    startActivity(intentCall);
                }
                return true;
            case R.id.sms_center:
                Intent intentSMS = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms","085102316099",null));
                startActivity(intentSMS);
                return true;
            case R.id.location:
                Uri location = Uri.parse("geo:0,0?q=Desa+Karangpakel,Trucuk,Klaten");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                startActivity(mapIntent);
                return true;
            case R.id.update_user:
                Intent iUpdate = new Intent(MainActivity.this, UpdateActivity.class);
                Bundle arg = new Bundle();
                arg.putSerializable("userData",userData);
                iUpdate.putExtra("userDataArg",arg);
                startActivity(iUpdate);
                return true;
            case R.id.history:
                Intent iHistory = new Intent(MainActivity.this, RiwayatActivity.class);
                Bundle extra = new Bundle();
                iHistory.putExtra("username",userData.getId());
                extra.putSerializable("userdataBundle",userData);
                iHistory.putExtra("userdata",extra);
                startActivity(iHistory);
                return true;
		}
        return false;
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
        ArrayList<DaftarMenu> stocks = new ArrayList<>();
        for(int i =0;i < jsonArray.length();i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            stocks.add(new DaftarMenu(obj.getString("id"), obj.getString("nama"), obj.getString("gambar"),obj.getInt("harga"), obj.getString("keterangan")));
        }
        menuAdapter = new MenuAdapter(this, this, stocks, urlStr);
        listMenu.setAdapter(menuAdapter);
//        this.pilih = new boolean[jsonArray.length()];
    }

    public void setTotal(int total) {
        this.total = total;
        String totalStr;
        nf.format(total);
        totalStr = "Rp "+nf.format(total);
        mTotal.setText(totalStr);
    }

    public void setDaftarMenu(ArrayList<DaftarMenu> dataList){
        this.daftarMenu = dataList;
    }

    //FUNGSI IMAGE
//    public void ayam_grg_txt(View view) {
//        Intent info = new Intent(MainActivity.this, InfoActivity.class);
//        info.putExtra("makanan",1);
//        startActivity(info);
//    }
//
//    public void ayam_bkr_txt(View view) {
//        Intent info = new Intent(MainActivity.this, InfoActivity.class);
//        info.putExtra("makanan",2);
//        startActivity(info);
//    }
//
//    public void mtg_txt(View view) {
//        Intent info = new Intent(MainActivity.this, InfoActivity.class);
//        info.putExtra("makanan",3);
//        startActivity(info);
//    }
//
//    public void nasi_putih_txt(View view) {
//        Intent info = new Intent(MainActivity.this, InfoActivity.class);
//        info.putExtra("makanan",4);
//        startActivity(info);
//    }
//
//    public void air_mineral_txt(View view) {
//        Intent info = new Intent(MainActivity.this, InfoActivity.class);
//        info.putExtra("makanan",5);
//        startActivity(info);
//    }
//
//    public void teh_txt(View view) {
//        Intent info = new Intent(MainActivity.this, InfoActivity.class);
//        info.putExtra("makanan",6);
//        startActivity(info);
//    }
//    //FUNGSI IMAGE
//    public void ayam_grg_img(View view) {
//        Toast.makeText(this, "Berhasil menambahkan Ayam Goreng ke dalam keranjang belanja", Toast.LENGTH_SHORT).show();
//        pilih[0] = true;
//        count[0]++;
//        Total();
//    }
//
//    public void ayam_bkr_img(View view) {
//        Toast.makeText(this, "Berhasil menambahkan Ayam Bakar ke dalam keranjang belanja", Toast.LENGTH_SHORT).show();
//        pilih[1] = true;
//        count[1]++;
//        Total();
//    }
//
//    public void mtg_img(View view) {
//        Toast.makeText(this, "Berhasil menambahkan Ayam Saus Mentega ke dalam keranjang belanja", Toast.LENGTH_SHORT).show();
//        pilih[2] = true;
//        count[2]++;
//        Total();
//    }
//
//    public void nasi_putih_img(View view) {
//        Toast.makeText(this, "Berhasil menambahkan Nasi Putih ke dalam keranjang belanja", Toast.LENGTH_SHORT).show();
//        pilih[3] = true;
//        count[3]++;
//        Total();
//    }
//
//    public void air_mineral_img(View view) {
//        Toast.makeText(this, "Berhasil menambahkan Air Mineral ke dalam keranjang belanja", Toast.LENGTH_SHORT).show();
//        pilih[4] = true;
//        count[4]++;
//        Total();
//    }
//
//    public void teh_img(View view) {
//        Toast.makeText(this, "Berhasil menambahkan Teh Manis ke dalam keranjang belanja", Toast.LENGTH_SHORT).show();
//        pilih[5] = true;
//        count[5]++;
//        Total();
//    }
//
//    public void Total(){
//        int sum = 0;
//        for(int i=0;i<count.length;i++){
//            sum = sum + (count[i]*harga[i]);
//        }
//        String NfSum = nf.format(sum);
//        String sumString = "Rp "+NfSum;
//        mTotal.setText(sumString);
//    }
//
//    public boolean isAllFalse() {
//        boolean allFalse = true;
//        for(int i=0;i<6;i++){
//            if(pilih[i]){
//                allFalse = false;
//                break;
//            }
//        }
//        return allFalse;
//    }
//
//    public void checkout(View view) {
//        if(isAllFalse()){
//            Toast.makeText(this, "Keranjang belanja masih kosong.", Toast.LENGTH_SHORT).show();
//        }else {
//            Intent checkout = new Intent(this, CheckoutActivity.class);
//            checkout.putExtra("count", count);
//            checkout.putExtra("pilih", pilih);
//            startActivity(checkout);
//        }
//    }

}