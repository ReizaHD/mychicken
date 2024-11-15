package com.example.mychicken.Class;

import java.io.Serializable;

public class DataUser implements Serializable {

    private String nama, user_id, pass;

    public DataUser(String nama, String user_id, String pass){
        this.nama = nama;
        this.user_id = user_id;
        this.pass = pass;
    }

    public String getNama(){
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getId(){
        return user_id;
    }

    public void setId(String user_id) {
        this.user_id = user_id;
    }

    public String getPass(){
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
