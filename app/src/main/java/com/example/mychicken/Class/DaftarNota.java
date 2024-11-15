package com.example.mychicken.Class;

import java.io.Serializable;

public class DaftarNota implements Serializable {
    String tgl_jual;
    int no_nota, status;
    double total_biaya, pembayaran;

    public DaftarNota(int no_nota, String tgl_jual, double total_biaya, double pembayaran, int status) {
        this.tgl_jual = tgl_jual;
        this.no_nota = no_nota;
        this.total_biaya = total_biaya;
        this.pembayaran = pembayaran;
        this.status = status;
    }

    public String getTgl_jual() {
        return tgl_jual;
    }

    public void setTgl_jual(String tgl_jual) {
        this.tgl_jual = tgl_jual;
    }

    public int getNo_nota() {
        return no_nota;
    }

    public void setNo_nota(int no_nota) {
        this.no_nota = no_nota;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getTotal_biaya() {
        return total_biaya;
    }

    public void setTotal_biaya(double total_biaya) {
        this.total_biaya = total_biaya;
    }

    public double getPembayaran() {
        return pembayaran;
    }

    public void setPembayaran(double pembayaran) {
        this.pembayaran = pembayaran;
    }
}
