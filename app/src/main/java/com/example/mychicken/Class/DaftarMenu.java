package com.example.mychicken.Class;

import java.io.Serializable;

public class DaftarMenu implements Serializable {
    private String kode,namaMenu,gambar,ket;
    private int hrgMenu;
    private int jumlah = 0;
    private boolean pilih = false;


    public DaftarMenu(String kode, String namaMenu, String gambar, int hrgMenu, String ket) {
        this.kode = kode;
        this.namaMenu = namaMenu;
        this.gambar = gambar;
        this.hrgMenu = hrgMenu;
        this.ket = ket;
    }
    public String getKode() {
        return kode;
    }
    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getNama() {
        return namaMenu;
    }
    public void setNama(String namaMenu) {
        this.namaMenu = namaMenu;
    }

    public String getKet() {
        return ket;
    }
    public void setKet(String namaMenu) {
        this.ket = ket;
    }

    public String getImg() {
        return gambar;
    }
    public void setImg(String gambar) {
        this.gambar = gambar;
    }

    public int getHrg() {
        return hrgMenu;
    }
    public void setHrg(int hrgMenu) {
        this.hrgMenu = hrgMenu;
    }

    public int getJml() {
        return jumlah;
    }
    public void addJml() {
        this.jumlah += 1;
    }
    public void setJml(int jml) {
        this.jumlah = jml;
    }

    public int getTotal(){
        return hrgMenu*jumlah;
    }

    public boolean getPilih() {
        return pilih;
    }
    public void setPilih(boolean pilih) {
        this.pilih = pilih;
    }
}
