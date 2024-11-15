package com.example.mychicken;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class InfoActivity extends AppCompatActivity {

    private ImageView mImage;
    private TextView mNamaMenu, mKetMenu;
    private String urlStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_info);

        SharedPreferences prefer =
                getSharedPreferences("MYDATA",MODE_PRIVATE);
        urlStr = prefer.getString("urlStr","");
        String srcImg = urlStr+"/uploads/";
        RequestOptions opt = new RequestOptions().centerCrop().placeholder(new ColorDrawable(Color.BLACK)).error(new ColorDrawable(Color.RED));

        String namaMenu = getIntent().getStringExtra("nama");
        String namaImg = getIntent().getStringExtra("gambar");
        String ketMenu = getIntent().getStringExtra("ket");
        mImage = (ImageView) findViewById(R.id.img_view);
        mNamaMenu = (TextView) findViewById(R.id.namaMenu);
        mKetMenu = (TextView) findViewById(R.id.ketMenu);

        mNamaMenu.setText(namaMenu);
        mKetMenu.setText(ketMenu);
        Glide.with(this).load(srcImg+namaImg).apply(opt).into(mImage);

//        switch(makanan){
//            case 1:
//                mImage.setImageResource(R.drawable.ayam_goreng);
//                mText.setText(R.string.ayam_grg_inf);
//                break;
//            case 2:
//                mImage.setImageResource(R.drawable.ayam_bakar);
//                mText.setText(R.string.ayam_bkr_inf);
//                break;
//            case 3:
//                mImage.setImageResource(R.drawable.ayam_saus_mentega);
//                mText.setText(R.string.ayam_mtg_inf);
//                break;
//            case 4:
//                mImage.setImageResource(R.drawable.nasi_putih);
//                mText.setText(R.string.nasi_inf);
//                break;
//            case 5:
//                mImage.setImageResource(R.drawable.air_mineral);
//                mText.setText(R.string.air_inf);
//                break;
//            case 6:
//                mImage.setImageResource(R.drawable.teh_manis);
//                mText.setText(R.string.teh_inf);
//                break;
//        }
    }
}