package com.example.mychicken.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychicken.Class.DaftarMenu;
import com.example.mychicken.R;

import java.util.ArrayList;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {

    ArrayList<DaftarMenu> dataList;
    LayoutInflater inflater;
    Context ctx;

    public CheckoutAdapter(Context ctx, ArrayList<DaftarMenu> dataList){
        this.ctx = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_checkout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DaftarMenu menu = dataList.get(position);
        holder.mNama.setText(menu.getNama());
        holder.mHarga.setText(String.valueOf(menu.getHrg()));
        holder.mJumlah.setText(String.valueOf(menu.getJml()));
        holder.mTotal.setText(String.valueOf(menu.getTotal()));
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout mLayout;
        TextView mNama, mJumlah, mHarga, mTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mLayout = itemView.findViewById(R.id.relLayout);
            mNama = itemView.findViewById(R.id.nmMenu);
            mJumlah = itemView.findViewById(R.id.jmlMenu);
            mHarga = itemView.findViewById(R.id.hrgMenu);
            mTotal = itemView.findViewById(R.id.jmlHrgMenu);
        }
    }
}
