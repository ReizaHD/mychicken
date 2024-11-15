package com.example.mychicken.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mychicken.Class.DaftarMenu;
import com.example.mychicken.InfoActivity;
import com.example.mychicken.InsertDataActivity;
import com.example.mychicken.MainActivity;
import com.example.mychicken.R;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    String srcImg, urlStr;
    ArrayList<DaftarMenu> dataList;
    LayoutInflater inflater;
    Context ctx;
    MainActivity main;
    int total = 0;

    public MenuAdapter(MainActivity main,Context ctx, ArrayList<DaftarMenu> dataList, String srcImg){
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(ctx);
        this.ctx = ctx;
        this.main = main;
        this.urlStr = srcImg;
        this.srcImg = srcImg+"/uploads/";
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_menu,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequestOptions opt = new RequestOptions().centerCrop().placeholder(new ColorDrawable(Color.BLACK)).error(R.drawable.ic_error_72);
        String namaImg, ket;
        DaftarMenu menu = dataList.get(position);
        int pos = position;

        holder.mNamaMenu.setText(menu.getNama());
        holder.mHarga.setText(String.valueOf(menu.getHrg()));
        namaImg = menu.getImg();
        ket = menu.getKet();

        Glide.with(ctx).load(srcImg+namaImg).apply(opt).into(holder.mGambar);

        holder.mText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ctx, "Behasil Menambahkan Menu ke Keranjang", Toast.LENGTH_SHORT).show();
                menu.addJml();
                if(!menu.getPilih())
                    menu.setPilih(true);
                total = total + (menu.getHrg());
                main.setTotal(total);
//                main.setDaftarMenu(dataList);
            }
        });

        holder.mGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent info = new Intent(ctx, InfoActivity.class);
                info.putExtra("nama",holder.mNamaMenu.getText());
                info.putExtra("harga",holder.mHarga.getText());
                info.putExtra("gambar",namaImg);
                info.putExtra("ket",ket);
                ctx.startActivity(info);
            }
        });

        /*holder.mMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(ctx,holder.mMore);
                popup.inflate(R.menu.menu_more);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        String kode, nama, ket, urlDelete;
                        int hrg, option;

                        option = menuItem.getItemId();
                        urlDelete = urlStr+"/android/delete.php";

                        kode = dataList.get(pos).getKode();
                        nama = dataList.get(pos).getNama();
                        hrg = dataList.get(pos).getHrg();
                        ket = dataList.get(pos).getKet();
                        Intent i = new Intent(ctx, InsertDataActivity.class);

                        if(option==R.id.update) {
                            i.putExtra("kode",kode);
                            i.putExtra("nama",nama);
                            i.putExtra("hrg",hrg);
                            i.putExtra("ket",ket);
                            i.putExtra("action",1);
                            ctx.startActivity(i);
                            return true;
                        }else if(option==R.id.delete) {
                            deleteData(urlDelete,kode);
                            return true;
                        }else
                            return false;
                    }
                });
                popup.show();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mNamaMenu, mHarga;
        ImageView mGambar, mMore;
        RelativeLayout mText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mNamaMenu = itemView.findViewById(R.id.namaMenu);
            mHarga = itemView.findViewById(R.id.hrgMenu);
            mGambar = itemView.findViewById(R.id.gambar);
            mMore = itemView.findViewById(R.id.more);
            mText = itemView.findViewById(R.id.text_view);
        }
    }

    public int getTotal(){
        return total;
    }

    public ArrayList<DaftarMenu> getDaftarMenu(){
        return this.dataList;
    }

    private void deleteData(String URL, String deleteKode) {
        RequestQueue queue = Volley.newRequestQueue(ctx);

        StringRequest sendData = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject res = new JSONObject(response);
                    Toast.makeText(ctx, "Pesan :" + res.getString("message"), Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                main.startActivity(new Intent(main, MainActivity.class));
                main.finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ctx,"Pesan : Gagal Update Data", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("id_menu", deleteKode);
                return map;
            }
        };
        queue.add(sendData);
    }
}
