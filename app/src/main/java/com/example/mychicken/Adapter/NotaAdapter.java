package com.example.mychicken.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mychicken.Class.DaftarNota;
import com.example.mychicken.FormActivity;
import com.example.mychicken.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class NotaAdapter extends RecyclerView.Adapter<NotaAdapter.ViewHolder> {
    Context ctx;
    ArrayList<DaftarNota> daftarNota;
    LayoutInflater inflater;

    public NotaAdapter(Context ctx, ArrayList<DaftarNota> daftarNota){
        this.ctx = ctx;
        this.inflater = LayoutInflater.from(ctx);
        this.daftarNota=daftarNota;
    }

    @NonNull
    @Override
    public NotaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_nota,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotaAdapter.ViewHolder holder, int position) {
        DaftarNota nota = daftarNota.get(position);
        int status = nota.getStatus();
        holder.mNota.setText(String.valueOf(nota.getNo_nota()));
        holder.mTgl.setText(nota.getTgl_jual());
        holder.mBiaya.setText(String.valueOf(nota.getTotal_biaya()));
        holder.mBayar.setText(String.valueOf(nota.getPembayaran()));
        if(status==1){
            holder.imgStatus.setImageResource(R.drawable.ic_berhasil);
            holder.btnBayar.setVisibility(View.GONE);
        } else {
            holder.imgStatus.setImageResource(R.drawable.ic_gagal);
        }
        holder.btnBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iBayar = new Intent(ctx, FormActivity.class);
                iBayar.putExtra("nota",nota.getNo_nota());
                iBayar.putExtra("mode",1);
                ctx.startActivity(iBayar);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (daftarNota != null) ? daftarNota.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView mNota, mTgl, mBiaya, mBayar;
        ImageView imgStatus;
        Button btnBayar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mNota = itemView.findViewById(R.id.noNota);
            mTgl = itemView.findViewById(R.id.tgl);
            mBiaya = itemView.findViewById(R.id.totalBiaya);
            mBayar = itemView.findViewById(R.id.pembayaran);
            imgStatus = itemView.findViewById(R.id.status);
            btnBayar = itemView.findViewById(R.id.btnBayar);
        }
    }
}
