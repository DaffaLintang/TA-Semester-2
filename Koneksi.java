/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package baksopuas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Daffa Lintang
 */
public class Koneksi {
    public static Connection koneksi;
    public static Connection getKoneksi(){
        if (koneksi == null){
     try {
            String url = "jdbc:mysql://localhost/baksopuas";
            String user = "root";
            String password = "123456789";
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            koneksi = DriverManager.getConnection(url, user, password);
            System.out.println("Berhasil");
        } catch (SQLException e) {
            System.out.println(e);
        }   
    }
 return koneksi;
    }
}
