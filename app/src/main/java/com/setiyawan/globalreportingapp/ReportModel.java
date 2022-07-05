package com.setiyawan.globalreportingapp;

public class ReportModel {

    // declare variables
    private String id;
    private String petugas;
    private String hari;
    private String tanggal;
    private String icao;
    private String nomor;
    private String kondisi;
    private String calc1;
    private String calc2;
    private String calc3;
    private String calc4;
    private String calc5;

    public ReportModel() {}

    public ReportModel(String id, String petugas, String hari, String tanggal, String icao, String nomor, String kondisi, String calc1, String calc2, String calc3, String calc4, String calc5) {
        this.id = id;
        this.petugas = petugas;
        this.hari = hari;
        this.tanggal = tanggal;
        this.icao = icao;
        this.nomor = nomor;
        this.kondisi = kondisi;
        this.calc1 = calc1;
        this.calc2 = calc2;
        this.calc3 = calc3;
        this.calc4 = calc4;
        this.calc5 = calc5;
    }

    public String getKondisi() {
        return kondisi;
    }

    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
    }

    public String getCalc1() {
        return calc1;
    }

    public void setCalc1(String calc1) {
        this.calc1 = calc1;
    }

    public String getCalc2() {
        return calc2;
    }

    public void setCalc2(String calc2) {
        this.calc2 = calc2;
    }

    public String getCalc3() {
        return calc3;
    }

    public void setCalc3(String calc3) {
        this.calc3 = calc3;
    }

    public String getCalc4() {
        return calc4;
    }

    public void setCalc4(String calc4) {
        this.calc4 = calc4;
    }

    public String getCalc5() {
        return calc5;
    }

    public void setCalc5(String calc5) {
        this.calc5 = calc5;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPetugas() {
        return petugas;
    }

    public void setPetugas(String petugas) {
        this.petugas = petugas;
    }

    public String getHari() {
        return hari;
    }

    public void setHari(String hari) {
        this.hari = hari;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getIcao() {
        return icao;
    }

    public void setIcao(String icao) {
        this.icao = icao;
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }
}
