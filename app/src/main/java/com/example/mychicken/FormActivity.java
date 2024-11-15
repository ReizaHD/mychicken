package com.example.mychicken;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.example.mychicken.Util.*;

public class FormActivity extends AppCompatActivity {

    int bayar, kembali, noNota;
    TextView status, tanggal, nota, username, total;
    ImageView imgStatus, gambar;
    Button btnsimpan, btncetak, btngallery;
    ProgressDialog pd;
    DecimalFormat decimalFormat;
    GalleryPhoto mGalery;
    String encode_image = null;
    String urlStr, urlTampil, urlUpdate;
    private final int TAG_GALLERY = 2200;
    String selected_photo = null;
    public static final String TAG_USERNAME = "username";
    String date;
    int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        SharedPreferences prefer =
                getSharedPreferences("MYDATA",MODE_PRIVATE);
        urlStr = prefer.getString("urlStr","");

        urlUpdate = urlStr + "/android/update_nota.php";
        urlTampil = urlStr + "/android/tampil_nota.php";

        noNota = getIntent().getIntExtra("nota",0);
        mode = getIntent().getIntExtra("mode",0);

        status = (TextView) findViewById(R.id.text_status);
        tanggal = (TextView) findViewById(R.id.tanggal);
        nota = (TextView) findViewById(R.id.no_nota);
        username = (TextView) findViewById(R.id.user);

        username.setText(getIntent().getStringExtra(TAG_USERNAME));
        total = (TextView) findViewById(R.id.total_biaya);
        imgStatus = (ImageView) findViewById(R.id.img_status);
        gambar = (ImageView) findViewById(R.id.inp_gambar);
        btngallery = (Button) findViewById(R.id.btn_gallery);
        btncetak = (Button) findViewById(R.id.btn_cetak);
        btnsimpan = (Button) findViewById(R.id.btn_simpan);

        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        decimalFormat = new DecimalFormat("#,##0.00");
        pd = new ProgressDialog(FormActivity.this);
        mGalery = new GalleryPhoto(getApplicationContext());
        loadJson(mode);

        btngallery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivityForResult(mGalery.openGalleryIntent(), TAG_GALLERY);
            }
        });
        btncetak.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                createPDF1();
            }
        });
        btnsimpan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (selected_photo != null) {
                    simpanData();
                }
                else {
                    Toast.makeText(getApplicationContext(),
                            "Upload bukti pembayaran terlebih dahulu",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void loadJson(int mode) {
        RequestQueue queue = Volley.newRequestQueue(FormActivity.this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        StringRequest sendData = new StringRequest(Request.Method.POST, urlTampil,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.cancel();
                try {
                    JSONArray obj = new JSONArray(response);
                    JSONObject data = obj.getJSONObject(0);

                    Log.d("JSON", String.valueOf(data));
                    tanggal.setText(data.getString("tgl_jual"));
                    nota.setText(data.getString("no_nota"));
                    if(mode==1)
                        username.setText(data.getString("kd_kons"));
                    bayar = data.getInt("pembayaran");
                    kembali = data.getInt("kembalian");
                    total.setText("Rp. " +  decimalFormat.format(data.getInt("total_biaya")));
                    if (data.getInt("status") == 1) {
                        status.setText("Sudah Dibayar");
                        status.setTextColor(Color.GREEN);
                        imgStatus.setImageResource(R.drawable.ic_berhasil);
                        btngallery.setVisibility(View.GONE);
                        btnsimpan.setVisibility(View.GONE);

                        findViewById(R.id.rekening).setVisibility(View.GONE);
                    } else if (data.getInt("status") == 0) {
                        status.setText("Belum Dibayar");
                                status.setTextColor(Color.RED);
                        imgStatus.setImageResource(R.drawable.ic_gagal);
                        btncetak.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.cancel();
                Log.d("volley", "error : " + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                if(mode==0)
                    map.put("username", username.getText().toString());
                else if(mode == 1)
                    map.put("no_nota", String.valueOf(noNota));
                return map;
            }
        };
        queue.add(sendData);
    }

    private void simpanData() {
        RequestQueue queue = Volley.newRequestQueue(FormActivity.this);
        pd.setMessage("Mengirim Data");
        pd.setCancelable(false);
        pd.show();
        try {
            Bitmap bitmap = ImageLoader.init().from(selected_photo).requestSize(1024, 1024).getBitmap();
            encode_image = ImageBase64.encode(bitmap);
            Log.d("ENCODER", encode_image);
            StringRequest sendData = new StringRequest(Request.Method.POST, urlUpdate, new Response.Listener<String>() {
                @Override
                public void onResponse(String response){
                    pd.cancel();
                    try {
                        JSONObject res = new
                                JSONObject(response);
                        Toast.makeText(FormActivity.this, "pesan : " + res.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(getApplicationContext(), FormActivity.class);
                    intent.putExtra(TAG_USERNAME,username.getText().toString());
                    startActivity(intent);
                    finish();
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pd.cancel();
                            Toast.makeText(FormActivity.this,"pesan : Gagal Kirim Data", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> map = new HashMap<>();
                    map.put("no_nota", nota.getText().toString());
                    map.put("gambar", encode_image);
                    return map;
                }
            };

            queue.add(sendData);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == TAG_GALLERY) {
                Uri uri_path = data.getData();
                mGalery.setPhotoUri(uri_path);
                String path = mGalery.getPath();
                selected_photo = path;
                try {
                    Bitmap bitmap;
                    bitmap = ImageLoader.init().from(path).requestSize(512, 512).getBitmap();
                    gambar.setImageBitmap(bitmap);
                    Snackbar.make(findViewById(android.R.id.content), "Success Loader Image", Snackbar.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Snackbar.make(findViewById(android.R.id.content), "Something Wrong", Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void createPDF() {
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        String format = s.format(new Date());
        String file = Environment.getExternalStorageDirectory().getPath() +"/"+ format;
        Document document = new Document();
        try {
            PdfWriter.getInstance(document,new FileOutputStream(file));
            document.open();
            Paragraph p = new Paragraph("HALLOOOOO");
            document.add(p);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void createPDF1() {
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        String format = s.format(new Date());
        Document doc = new Document();
        String outPath = FileUtils.getAppPath(getApplicationContext()) + "/" + format +	".pdf";
        Log.d("TEST BUTTON","KOKOKOK");
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(outPath));
            doc.open();
            // Document Settings
            doc.setPageSize(PageSize.A4);
            doc.addCreationDate();
            doc.addAuthor("Ivan");
            doc.addCreator("Ivan");
            /**
             * How to USE FONT....
             */
            BaseFont urName =
                    BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8",
                            BaseFont.EMBEDDED);
            /***
             * Variables for further use....
             */
            BaseColor mColorAccent = new BaseColor(0, 153, 204,	255);
            float mHeadingFontSize = 20.0f;
            float mValueFontSize = 26.0f;
            Font mTitleFont = new Font(urName, 36.0f,Font.NORMAL, BaseColor.BLACK);
            Font mHeadingFont = new Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent);
            Font mValueFont = new Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK);
            // LINE SEPARATOR
            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
            // Order Details...
            // Title
            Chunk mTitleChunk = new Chunk("Invoice", mTitleFont);
            Paragraph mTitleParagraph = new	Paragraph(mTitleChunk);
            mTitleParagraph.setAlignment(Element.ALIGN_CENTER);
            doc.add(mTitleParagraph);
            // Adding Line Breakable Space....
            doc.add(new Paragraph(""));
            // Adding Horizontal Line...
            doc.add(new Chunk(lineSeparator));
            // Adding Line Breakable Space....
            doc.add(new Paragraph(""));
            // Tanggal
            Chunk mDateChunk = new Chunk("Order Date:", mHeadingFont);
            Paragraph mOrderDateParagraph = new Paragraph(mDateChunk);
            doc.add(mOrderDateParagraph);
            Chunk mDateValueChunk = new Chunk(date,	mValueFont);
            Paragraph mDateValueParagraph = new	Paragraph(mDateValueChunk);
            doc.add(mDateValueParagraph);
            doc.add(new Paragraph(""));
            doc.add(new Chunk(lineSeparator));
            doc.add(new Paragraph(""));
            // Akun
            Chunk mAcNameChunk = new Chunk("Account Name:",	mHeadingFont);
            Paragraph mAcNameParagraph = new Paragraph(mAcNameChunk);
            doc.add(mAcNameParagraph);
            Chunk mAcNameValueChunk = new Chunk(username.getText().toString(), mValueFont);
            Paragraph mAcNameValueParagraph = new Paragraph(mAcNameValueChunk);
            doc.add(mAcNameValueParagraph);
            //adds paragraph and line seperator
            doc.add(new Paragraph(""));
            doc.add(new Chunk(lineSeparator));
            doc.add(new Paragraph(""));
            // Total
            Chunk mAmountChunk = new Chunk("Total Amount:",	mHeadingFont);
            Paragraph mAmountParagraph = new Paragraph(mAmountChunk);
            doc.add(mAmountParagraph);
            Chunk mAmountValueChunk = new Chunk(total.getText().toString(), mValueFont);
            Paragraph mAmountValueParagraph = new Paragraph(mAmountValueChunk);
            doc.add(mAmountValueParagraph);
            //adds paragraph and line seperator
            doc.add(new Paragraph(""));
            doc.add(new Chunk(lineSeparator));
            doc.add(new Paragraph(""));
            // Pembayaran
            Chunk mCashChunk = new Chunk("Cash:", mHeadingFont);
            Paragraph mCashParagraph = new Paragraph(mCashChunk);
            doc.add(mCashParagraph);
            Chunk mCashValueChunk = new Chunk("Rp. " +	decimalFormat.format(bayar), mValueFont);
            Paragraph mCashValueParagraph = new Paragraph(mCashValueChunk);
            doc.add(mCashValueParagraph);
            //adds paragraph and line seperator
            doc.add(new Paragraph(""));
            doc.add(new Chunk(lineSeparator));
            doc.add(new Paragraph(""));
            // Kembalian
            Chunk mChangeChunk = new Chunk("Change:", mHeadingFont);
            Paragraph mChangeParagraph = new Paragraph(mChangeChunk);
            doc.add(mChangeParagraph);
            Chunk mChangeValueChunk = new Chunk("Rp. " + decimalFormat.format(kembali), mValueFont);
            Paragraph mChangeValueParagraph = new Paragraph(mChangeValueChunk);
            doc.add(mChangeValueParagraph);
            //adds paragraph and line seperator
            doc.add(new Paragraph(""));
            doc.add(new Chunk(lineSeparator));
            doc.add(new Paragraph(""));
            doc.close();
            FileUtils.openFile(getApplicationContext(), new File(outPath));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}