package com.example.mychicken;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mychicken.Class.DataUser;
import com.example.mychicken.Util.AppController;
import com.example.mychicken.Util.MD5;

import java.util.ArrayList;

public class UpdateActivity extends AppCompatActivity {
    private DataUser userData;
    private Button btnUpdate;
    private String urlStr, urlUpdate, oldId;
    private ArrayList<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        data = new ArrayList<>();
        SharedPreferences prefer =
                getSharedPreferences("MYDATA",MODE_PRIVATE);
        urlStr = prefer.getString("urlStr",null);
        urlUpdate = urlStr+"/android/update_konsumen.php";

        Bundle extra = getIntent().getBundleExtra("userDataArg");
        userData = (DataUser) extra.getSerializable("userData");

        final EditText nama,username,password,mOldPass;
        nama = (EditText) findViewById(R.id.inputNama);
        username = (EditText) findViewById(R.id.inputUser);
        password = (EditText) findViewById(R.id.inputPass);
        mOldPass = (EditText) findViewById(R.id.oldPass);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        nama.setText(userData.getNama());
        username.setText(userData.getId());
        oldId = userData.getId();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = mOldPass.getText().toString();
                String newNama = nama.getText().toString();
                String newUser = username.getText().toString();
                String newPass = password.getText().toString();
                if(MD5.getMd5(oldPass).equals(userData.getPass())) {
                    data.add(newUser);
                    data.add(newNama);
                    data.add(newPass);
                    data.add(oldId);

                    AppController.interactData(urlUpdate, UpdateActivity.this, 2, data);
                    Intent iLogin = new Intent(UpdateActivity.this,
                            LoginActivity.class);
                    startActivity(iLogin);
                } else if(oldPass.trim().length() <= 0 || newNama.trim().length() <= 0 || newUser.trim().length() <= 0 || newPass.trim().length() <= 0){
                    Toast.makeText(UpdateActivity.this,"Form tidak boleh ada yang kosong!!",Toast.LENGTH_SHORT).show();
                } else if(!MD5.getMd5(oldPass).equals(userData.getPass())){
                    Toast.makeText(UpdateActivity.this,"Password lama tidak cocok!!",Toast.LENGTH_SHORT).show();
                }
            }

        });

    }
}