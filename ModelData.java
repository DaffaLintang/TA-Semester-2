/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package baksopuas;

/**
 *
 * @author Daffa Lintang
 */
public class ModelData {
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getPengeluaran() {
        return pengeluaran;
    }

    public void setPengeluaran(double pengeluaran) {
        this.pengeluaran = pengeluaran;
    }

    public double getPendapatan() {
        return pendapatan;
    }

    public void setPendapatan(double pendapatan) {
        this.pendapatan = pendapatan;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public ModelData(String month, double pengeluaran, double pendapatan, double profit) {
        this.month = month;
        this.pengeluaran = pengeluaran;
        this.pendapatan = pendapatan;
        this.profit = profit;
    }

    public ModelData() {
    }

    private String month;
    private double pengeluaran;
    private double pendapatan;
    private double profit;
}
