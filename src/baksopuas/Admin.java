/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package baksopuas;

import static baksopuas.Kasir.usernameKasir;
import chart.ModelChart;
import datechooser.listener.DateChooserAction;
import datechooser.listener.DateChooserAdapter;

import com.barcodelib.barcode.Linear;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import datechooser.DateChooser;
import datechooser.listener.DateChooserListener;
import datechooser.DateBetween;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.awt.Graphics2D;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
/**
 *
 * @author Daffa Lintang
 */
public class Admin extends javax.swing.JFrame {
    
    private DefaultTableModel modelMenu;
    private DefaultTableModel modelHistori;
    private DefaultTableModel barcodeModel;
    private DefaultTableModel userModel;
    /**
     * Creates new form Admin
     */
    
    String pathMenu = null;
    
    
    public void autoNumberMenu(){
        try{
            java.sql.Connection c = Koneksi.getKoneksi();
            java.sql.Statement s = c.createStatement();
            
            String sql = "SELECT kode_makanan FROM menu order by kode_makanan DESC";
            ResultSet r = s.executeQuery(sql);
            
            if(r.next()){
                String kodeMakanan = r.getString("kode_makanan");
                int kdMakanan = Integer.parseInt(kodeMakanan);
                FieldInputMenu1.setText(String.valueOf(kdMakanan +1));
            }else{
                FieldInputMenu1.setText(String.valueOf(1));
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    public void autoNumberUser(){
        try{
            java.sql.Connection c = Koneksi.getKoneksi();
            java.sql.Statement s = c.createStatement();
            
            String sql = "SELECT kode_user FROM user order by kode_user DESC";
            ResultSet r = s.executeQuery(sql);
            
            if(r.next()){
                String kodeUser = r.getString("kode_user");
                int kdUser = Integer.parseInt(kodeUser);
                FieldInputUser1.setText(String.valueOf(kdUser +1));
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    
    
    public void clearBarcode(){
        inputDiskonBarcode.setText("");
    }
    
    
    public void loadDataMenu(){
        modelMenu.getDataVector().removeAllElements();
        modelMenu.fireTableDataChanged();
        
        try{
            java.sql.Connection c = Koneksi.getKoneksi();
            java.sql.Statement s = c.createStatement();
            
            String sql = "SELECT * FROM menu order by kode_makanan DESC";
            ResultSet r = s.executeQuery(sql);
            
            while(r.next()){
                Object[] o = new Object[5];
            o[0] = r.getString("kode_makanan");
            o[1] = r.getString("nama_makanan");
            o[2] = r.getString("harga_beli");
            o[3] = r.getString("harga_jual");
            byte[] imageData = r.getBytes("foto_makanan");
            ImageIcon imageIcon = new ImageIcon(imageData);
            o[4] = imageIcon;

            modelMenu.addRow(o);    
            }
            r.close();
            s.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
     public void loadDataUser(){
        userModel.getDataVector().removeAllElements();
        userModel.fireTableDataChanged();
        
        try{
            java.sql.Connection c = Koneksi.getKoneksi();
            java.sql.Statement s = c.createStatement();
            
            String sql = "SELECT * FROM user order by kode_user DESC";
            ResultSet r = s.executeQuery(sql);
            
            while(r.next()){
                Object[] o = new Object[6];
            o[0] = r.getString("kode_user");
            o[1] = r.getString("username");
            o[2] = r.getString("password");
            o[3] = r.getString("role");
            o[4] = r.getString("Alamt");
            o[5] = r.getString("No_Hp");
            
            userModel.addRow(o);    
            }
            r.close();
            s.close();
        }catch(Exception e){
            System.out.println(e);
        }
    }
    
    public void setTableRenderMenu(){
        class CustomRenderer extends DefaultTableCellRenderer {
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if(value instanceof ImageIcon){
             ImageIcon icon =(ImageIcon) value;
             Image originImage =icon.getImage();
             
             int width = 200;
             int height = 120;
             
             Image resizedImage = originImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
             ImageIcon resizedIcon = new ImageIcon(resizedImage);
             
             JLabel label = new JLabel(resizedIcon);
             label.setHorizontalAlignment(JLabel.CENTER);
             
             return label;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
        tabelMenu.getColumnModel().getColumn(4).setCellRenderer(new CustomRenderer());
    }
     
    
    public void hitungKeuntungan(){
        try{
        java.sql.Connection c = Koneksi.getKoneksi();
        java.sql.Statement s = c.createStatement();
        
        String sql = "SELECT SUM(menu.harga_beli) AS total_pengeluaran, SUM(menu.harga_jual) AS total_pendapatan, SUM(menu.harga_jual) - SUM(menu.harga_beli) AS profit FROM detail_transaksi INNER JOIN menu ON detail_transaksi.kode_makanan = menu.kode_makanan";
        ResultSet r = s.executeQuery(sql);
        
        while(r.next()){
            Locale rupiah = new Locale("id", "ID");
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(rupiah);
            String profit = formatRupiah.format(r.getInt("profit"));
            String pengeluaran = formatRupiah.format(r.getInt("total_pengeluaran"));
            String pemasukan = formatRupiah.format(r.getInt("total_pendapatan"));
            profitTx.setText(profit);
            pemasukanTx.setText(pemasukan);
            pengeluaranTx.setText(pengeluaran);
        }  
        }catch(Exception e){
            System.out.println(e);
        }
        
    }
    
    public void chartData(){
        try{
        java.sql.Connection c = Koneksi.getKoneksi();
        java.sql.Statement s = c.createStatement();
        List<ModelData> list = new ArrayList<>();
        String sql = "SELECT SUM(menu.harga_beli) AS total_pengeluaran, SUM(menu.harga_jual) AS total_pendapatan, SUM(menu.harga_jual) - SUM(menu.harga_beli) AS profit, DATE_FORMAT(transaksi.tanggal,'%d-%M-%Y') as `Date` FROM detail_transaksi JOIN menu ON detail_transaksi.kode_makanan = menu.kode_makanan JOIN transaksi ON detail_transaksi.kode_transaksi = transaksi.kode_transaksi GROUP BY DATE_FORMAT(transaksi.tanggal,'%d-%M-%Y'), transaksi.tanggal ORDER BY transaksi.tanggal DESC LIMIT 7";
        ResultSet r = s.executeQuery(sql);
        while(r.next()){
             String month = r.getString("Date");
             double pengeluaran = (double) r.getInt("total_pengeluaran");
             double pendapatan = (double) r.getInt("total_pendapatan");
             double profit = (double) r.getInt("profit");
             list.add(new ModelData(month, pengeluaran, pendapatan, profit));
        }
        r.close();
        s.close();
        for(int i = list.size()-1; i>=0 ; i--){
            ModelData d = list.get(i);
            chart.addData(new ModelChart(d.getMonth(), new double[]{d.getPengeluaran(), d.getPendapatan(), d.getProfit()}));
        }
        chart.start();
        } catch(Exception e){
            System.out.println(e);
        }
    }
    
    public void loadHistori(){
        try {
            java.sql.Connection c = Koneksi.getKoneksi();
            java.sql.Statement s = c.createStatement();
            String sql = "SELECT ROW_NUMBER() OVER (ORDER BY detail_transaksi.kode_transaksi) AS nomor, detail_transaksi.kode_transaksi, menu.nama_makanan, COUNT(menu.nama_makanan) AS jumlah, menu.harga_beli, menu.harga_jual, transaksi.tanggal FROM detail_transaksi INNER JOIN transaksi ON detail_transaksi.kode_transaksi = transaksi.kode_transaksi INNER JOIN menu ON detail_transaksi.kode_makanan = menu.kode_makanan GROUP BY detail_transaksi.kode_transaksi, menu.nama_makanan, menu.harga_beli, menu.harga_jual, transaksi.tanggal";
            ResultSet rs = s.executeQuery(sql);

            while (rs.next()) {
                Object[] o = new Object[7];
                o[0] = rs.getInt("nomor");
                o[1] = rs.getInt("kode_transaksi");
                o[2] = rs.getString("nama_makanan");
                o[3] = rs.getInt("jumlah");
                o[4] = rs.getInt("harga_beli");
                o[5] = rs.getInt("harga_jual");
                o[6] = rs.getString("tanggal");
                
                modelHistori.addRow(o);
        }

            // Jangan lupa untuk menutup koneksi dan objek statement serta result set setelah selesai
            rs.close();
           s.close();
        } catch (Exception e) {
            e.printStackTrace();
        }    
    }
    
     public void cari(){
        String key = searchInputBarcode.getText();
        DefaultTableModel tabel = new DefaultTableModel();
         tabel.addColumn("Kode Barcode");
        tabel.addColumn("Diskon");
        tabel.addColumn("Barcode");

          try{
            java.sql.Connection c = Koneksi.getKoneksi();
            java.sql.Statement s = c.createStatement();
            
            String sql = "SELECT * FROM barcode where kode_barcode like '%"+key+"%' or diskon like '%"+key+"%'";
            ResultSet r = s.executeQuery(sql);
           
            while(r.next()){
                byte[] imageData = r.getBytes("barcode");
                ImageIcon imageIcon = new ImageIcon(imageData);
              
                tabel.addRow(new Object[]{
               r.getString(1),
               r.getString(2),
               imageIcon
       
            });
            barcodeTabel.setModel(tabel);
            }
            r.close();
            s.close();
        }catch(Exception e){
            System.out.println(e);
        }
       loadDataBarcode();
       setTableRenderBarcode();
    }
     
     public void loadDataBarcode() {
        barcodeModel.getDataVector().removeAllElements();
        barcodeModel.fireTableDataChanged();
        
        try {
            Connection c = (Connection) Koneksi.getKoneksi();
            Statement s = (Statement) c.createStatement();
            
            String sql = "SELECT * FROM barcode";
            ResultSet r = s.executeQuery(sql);
            
            while (r.next()) {
                Object[] o = new Object[3];
                o [0] = r.getString("kode_barcode");
                o [1] = r.getString("diskon");
                byte[] imageData = r.getBytes("barcode");
            ImageIcon imageIcon = new ImageIcon(imageData);
                o [2] = imageIcon;
                
                barcodeModel.addRow(o);
            }
            r.close();
            s.close();
        } catch (Exception e) {
            System.out.println("Terjadi Kesalahan");
    }
    }
    
     public void setTableRenderBarcode(){
        class CustomRenderer extends DefaultTableCellRenderer {
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if(value instanceof ImageIcon){
             ImageIcon icon =(ImageIcon) value;
             Image originImage =icon.getImage();
             
             int width = 200;
             int height = 120;
             
             Image resizedImage = originImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
             ImageIcon resizedIcon = new ImageIcon(resizedImage);
             
             JLabel label = new JLabel(resizedIcon);
             label.setHorizontalAlignment(JLabel.CENTER);
             
             return label;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
}
}
       barcodeTabel.getColumnModel().getColumn(2).setCellRenderer(new CustomRenderer());
} 
    public void cariTanggalHistori(String sql){
        try{
            java.sql.Connection c = Koneksi.getKoneksi();
            modelHistori.setRowCount(0);
            SimpleDateFormat df = new SimpleDateFormat("dd-MMMM-yyyy");
            DecimalFormat f = new DecimalFormat("$ #,##0.##");
            java.sql.PreparedStatement p = c.prepareStatement(sql);
            ResultSet r = p.executeQuery();
            while(r.next()){
                Object[] o = new Object[7];
                o[0] = r.getInt("nomor");
                o[1] = r.getInt("kode_transaksi");
                o[2] = r.getString("nama_makanan");
                o[3] = r.getInt("jumlah");
                o[4] = r.getInt("harga_beli");
                o[5] = r.getInt("harga_jual");
                o[6] = r.getString("tanggal");
                
                modelHistori.addRow(o); 
            }
            r.close();
            p.close();
        } catch (Exception e){
            System.out.println();
        }
    }
    
   public void cariChartData(String sql) {
    try {
        chart.clear();
        java.sql.Connection c = Koneksi.getKoneksi();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMMM-yyyy");
        DecimalFormat f = new DecimalFormat("$ #,##0.##");
        List<ModelData> list = new ArrayList<>();
        java.sql.PreparedStatement s = c.prepareStatement(sql);
        ResultSet r = s.executeQuery();
        while (r.next()) {
            String month = r.getString("Date");
            double pengeluaran = (double) r.getInt("total_pengeluaran");
            double pendapatan = (double) r.getInt("total_pendapatan");
            double profit = (double) r.getInt("profit");
            list.add(new ModelData(month, pengeluaran, pendapatan, profit));
        }
        r.close();
        s.close();
        for (int i = list.size() - 1; i >= 0; i--) {
            ModelData d = list.get(i);
            chart.addData(new ModelChart(d.getMonth(), new double[]{d.getPengeluaran(), d.getPendapatan(), d.getProfit()}));
        }
        chart.start();
    } catch (Exception e) {
        System.out.println(e);
    }
}

     
    
    private DateChooser chDate = new DateChooser();
    private DateChooser chDate1 = new DateChooser();

    public Admin() {
        initComponents();
       
        chDate.setTextField(cariTx);
        chDate.setDateSelectionMode(DateChooser.DateSelectionMode.BETWEEN_DATE_SELECTED);
        chDate.setLabelCurrentDayVisible(false);
        chDate.setDateFormat(new SimpleDateFormat("dd-MMMM-yyyy"));
        modelHistori=(DefaultTableModel)tabelHistori.getModel();
        chDate.addActionDateChooserListener((DateChooserListener) new DateChooserAdapter() {
            @Override
            public void dateBetweenChanged(DateBetween date, DateChooserAction action){
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String dateFrom = df.format(date.getFromDate());
                String toDate = df.format(date.getToDate());
                cariTanggalHistori("SELECT ROW_NUMBER() OVER (ORDER BY detail_transaksi.kode_transaksi) AS nomor, detail_transaksi.kode_transaksi, menu.nama_makanan, COUNT(menu.nama_makanan) AS jumlah, menu.harga_beli, menu.harga_jual, transaksi.tanggal FROM detail_transaksi INNER JOIN transaksi ON detail_transaksi.kode_transaksi = transaksi.kode_transaksi INNER JOIN menu ON detail_transaksi.kode_makanan = menu.kode_makanan where transaksi.tanggal BETWEEN '"+dateFrom+"' AND '"+toDate+"' GROUP BY detail_transaksi.kode_transaksi, menu.nama_makanan, menu.harga_beli, menu.harga_jual, transaksi.tanggal ");
            }
        }); 
        
        chDate1.setTextField(btnCariDashboard);
chDate1.setDateSelectionMode(DateChooser.DateSelectionMode.BETWEEN_DATE_SELECTED);
chDate1.setLabelCurrentDayVisible(false);
chDate1.setDateFormat(new SimpleDateFormat("dd-MMMM-yyyy"));
chDate1.addActionDateChooserListener(new DateChooserAdapter() {
    @Override
    public void dateBetweenChanged(DateBetween date, DateChooserAction action) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String dateFrom = df.format(date.getFromDate());
        String toDate = df.format(date.getToDate());
        cariChartData("SELECT SUM(menu.harga_beli) AS total_pengeluaran, SUM(menu.harga_jual) AS total_pendapatan, SUM(menu.harga_jual) - SUM(menu.harga_beli) AS profit, DATE_FORMAT(transaksi.tanggal,'%d-%M-%Y') as `Date` FROM detail_transaksi JOIN menu ON detail_transaksi.kode_makanan = menu.kode_makanan JOIN transaksi ON detail_transaksi.kode_transaksi where transaksi.tanggal BETWEEN '"+dateFrom+"' AND '"+toDate+"' GROUP BY DATE_FORMAT(transaksi.tanggal,'%d-%M-%Y'), transaksi.tanggal ORDER BY transaksi.tanggal DESC LIMIT 7");
    }
});
        
//        chDate.setTextField(cariTx);
//        chDate.setDateSelectionMode(DateChooser.DateSelectionMode.BETWEEN_DATE_SELECTED);
//        chDate.setLabelCurrentDayVisible(false);
//        chDate.setDateFormat(new SimpleDateFormat("dd-MMMM-yyyy"));
//        modelHistori=(DefaultTableModel)tabelHistori.getModel();
//        chDate.addActionDateChooserListener((DateChooserListener) new DateChooserAdapter() {
//            @Override
//            public void dateBetweenChanged(DateBetween date, DateChooserAction action){
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                String dateFrom = df.format(date.getFromDate());
//                String toDate = df.format(date.getToDate());
//                cariTanggalHistori("SELECT SUM(menu.harga_beli) AS total_pengeluaran, SUM(menu.harga_jual) AS total_pendapatan, SUM(menu.harga_jual) - SUM(menu.harga_beli) AS profit, DATE_FORMAT(transaksi.tanggal,'%d-%M-%Y') as `Date` FROM detail_transaksi JOIN menu ON detail_transaksi.kode_makanan = menu.kode_makanan JOIN transaksi ON detail_transaksi.kode_transaksi = transaksi.kode_transaksi WHERE  BETWEEN '"+dateFrom+"' AND '"+toDate+"' GROUP BY DATE_FORMAT(transaksi.tanggal,'%d-%M-%Y'), transaksi.tanggal ORDER BY transaksi.tanggal DESC LIMIT 7");
//            }
//        }); 
        
        chart.setTitle("Grafik Penjualan");
        chart.addLegend("Pengeluaran", Color.decode("#7b4397"), Color.decode("#dc2430"));
        chart.addLegend("Pendapatan", Color.decode("#e65c00"), Color.decode("#F9D423"));
        chart.addLegend("Profit", Color.decode("#0099F7"), Color.decode("#F11712"));
       hitungKeuntungan();
       chartData();
       
       modelMenu = new DefaultTableModel();
       tabelMenu.setRowHeight(100);
       tabelMenu.setModel(modelMenu);
       
       modelMenu.addColumn("Kode Makanan");
       modelMenu.addColumn("Nama Makanan");
       modelMenu.addColumn("Harga Beli");
       modelMenu.addColumn("Harga Jual");
       modelMenu.addColumn("Foto Makanan");
       loadDataMenu();
       setTableRenderMenu();
       autoNumberMenu();
       
       modelHistori = new DefaultTableModel();
       tabelHistori.setModel(modelHistori);
       modelHistori.addColumn("No");
       modelHistori.addColumn("Kode Transaksi");
       modelHistori.addColumn("Nama Makanan");
       modelHistori.addColumn("Jumlah");
       modelHistori.addColumn("Harga Beli");
       modelHistori.addColumn("Harga Jual");
       modelHistori.addColumn("Tanggal");
       loadHistori();
       autoNumberMenu();
       
       
       barcodeModel = new DefaultTableModel();
       barcodeTabel.setRowHeight(100);
       barcodeTabel.setModel(barcodeModel);
       barcodeModel.addColumn("Kode_Barcode");
       barcodeModel.addColumn("Diskon");
       barcodeModel.addColumn("Barcode");
       loadDataBarcode();
       setTableRenderBarcode();
       
       userModel = new DefaultTableModel();
       TabelInputUser1.setModel(userModel);
       userModel.addColumn("Kode_User");
       userModel.addColumn("Username");
       userModel.addColumn("Password");
       userModel.addColumn("Role");
       userModel.addColumn("Alamat");
       userModel.addColumn("No_Hp");
       loadDataUser();
       autoNumberUser();
       
       setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        jPanel1 = new javax.swing.JPanel();
        sideBar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        inputMenuBtn = new javax.swing.JButton();
        logOutBtn = new javax.swing.JButton();
        homeBtn = new javax.swing.JButton();
        historiBtn = new javax.swing.JButton();
        karyawanBtn = new javax.swing.JButton();
        cetakBarcodeBtn = new javax.swing.JButton();
        KasirBtn = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        dashboard = new javax.swing.JPanel();
        dashboardNav = new javax.swing.JPanel();
        dashboardLabel = new javax.swing.JLabel();
        statistikPanel = new javax.swing.JPanel();
        profit = new baksopuas.roundedJpanelShadow();
        jLabel9 = new javax.swing.JLabel();
        profitTx = new javax.swing.JLabel();
        pengeluaran = new baksopuas.roundedJpanelShadow();
        jLabel11 = new javax.swing.JLabel();
        pengeluaranTx = new javax.swing.JLabel();
        pemasukan = new baksopuas.roundedJpanelShadow();
        jLabel8 = new javax.swing.JLabel();
        pemasukanTx = new javax.swing.JLabel();
        grafikPanel = new javax.swing.JPanel();
        chart = new chart.CurveLineChart();
        btnCariDashboard = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        inputMenu = new javax.swing.JPanel();
        PanelInputMenu1 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        PanelInputMenu2 = new javax.swing.JPanel();
        FieldInputMenu1 = new javax.swing.JTextField();
        LabelInputMenu1 = new javax.swing.JLabel();
        FieldInputMenu2 = new javax.swing.JTextField();
        LabelInputMenu2 = new javax.swing.JLabel();
        FieldInputMenu4 = new javax.swing.JTextField();
        LabelInputMenu5 = new javax.swing.JLabel();
        FieldInputMenu5 = new javax.swing.JTextField();
        LabelInputMenu6 = new javax.swing.JLabel();
        FieldInputMenu6 = new javax.swing.JTextField();
        ButtonCari = new javax.swing.JButton();
        ButtonSimpan = new javax.swing.JButton();
        ButtonEdit = new javax.swing.JButton();
        ButtonHapus = new javax.swing.JButton();
        uploadBtnMenu = new javax.swing.JButton();
        LbFoto_Makanan = new javax.swing.JLabel();
        ButtonHapus1 = new javax.swing.JButton();
        TabelInputMenu = new javax.swing.JScrollPane();
        tabelMenu = new javax.swing.JTable();
        histori = new javax.swing.JPanel();
        historiNav = new javax.swing.JPanel();
        historiLabel = new javax.swing.JLabel();
        historiPanel = new javax.swing.JPanel();
        historiScrollPane = new javax.swing.JScrollPane();
        tabelHistori = new javax.swing.JTable();
        cariTx = new javax.swing.JTextField();
        InputUser = new javax.swing.JPanel();
        PanelInputUser1 = new javax.swing.JPanel();
        LabelInputUser1 = new javax.swing.JLabel();
        PanelInputUser2 = new javax.swing.JPanel();
        FieldInputUser1 = new javax.swing.JTextField();
        LabelInputUser2 = new javax.swing.JLabel();
        FieldInputUser2 = new javax.swing.JTextField();
        FieldInputUser3 = new javax.swing.JTextField();
        LabelInputUser3 = new javax.swing.JLabel();
        LabelInputUser4 = new javax.swing.JLabel();
        FieldInputUser4 = new javax.swing.JTextField();
        LabelInputUser5 = new javax.swing.JLabel();
        FieldInputUser5 = new javax.swing.JTextField();
        LabelInputUser6 = new javax.swing.JLabel();
        FieldInputUser6 = new javax.swing.JTextField();
        ButtonInputUserCari = new javax.swing.JButton();
        ButtonInputUserSimpn = new javax.swing.JButton();
        ButtonInputUserEdit = new javax.swing.JButton();
        ButtonInputUserHapus = new javax.swing.JButton();
        inputRole = new javax.swing.JLabel();
        cmbBoxUser = new javax.swing.JComboBox<>();
        ButtonInputUserBatal = new javax.swing.JButton();
        TabelInputUser = new javax.swing.JScrollPane();
        TabelInputUser1 = new javax.swing.JTable();
        cetakBarcode = new javax.swing.JPanel();
        ceteakBarccodePanel = new javax.swing.JPanel();
        inputDiskonBarcode = new javax.swing.JTextField();
        searchInputBarcode = new javax.swing.JTextField();
        saveBtnBarcode = new java.awt.Button();
        hapusBtnBarcode = new java.awt.Button();
        cariBtnBarcode = new java.awt.Button();
        diskonBarcode = new javax.swing.JLabel();
        jScrollPaneBarcode = new javax.swing.JScrollPane();
        barcodeTabel = new javax.swing.JTable();
        barcodeNav = new javax.swing.JPanel();
        barcodeNavLabel = new javax.swing.JLabel();

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable2);

        jToolBar1.setRollover(true);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        sideBar.setBackground(new java.awt.Color(255, 222, 197));
        sideBar.setPreferredSize(new java.awt.Dimension(130, 510));
        sideBar.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setBackground(new java.awt.Color(80, 80, 80));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(80, 80, 80));
        jLabel1.setText("BAKSO PUAS");
        sideBar.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, -1, -1));

        inputMenuBtn.setBackground(new java.awt.Color(124, 183, 89));
        inputMenuBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        inputMenuBtn.setText("Input Menu");
        inputMenuBtn.setBorder(null);
        inputMenuBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputMenuBtnActionPerformed(evt);
            }
        });
        sideBar.add(inputMenuBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 120, 40));

        logOutBtn.setBackground(new java.awt.Color(124, 183, 89));
        logOutBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        logOutBtn.setText("Log Out");
        logOutBtn.setBorder(null);
        logOutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logOutBtnActionPerformed(evt);
            }
        });
        sideBar.add(logOutBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 380, 120, 40));

        homeBtn.setBackground(new java.awt.Color(124, 183, 89));
        homeBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        homeBtn.setText("Dashboard");
        homeBtn.setBorder(null);
        homeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeBtnActionPerformed(evt);
            }
        });
        sideBar.add(homeBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 120, 40));

        historiBtn.setBackground(new java.awt.Color(124, 183, 89));
        historiBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        historiBtn.setText("Histori");
        historiBtn.setBorder(null);
        historiBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historiBtnActionPerformed(evt);
            }
        });
        sideBar.add(historiBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 120, 40));

        karyawanBtn.setBackground(new java.awt.Color(124, 183, 89));
        karyawanBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        karyawanBtn.setText("Input User");
        karyawanBtn.setBorder(null);
        karyawanBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                karyawanBtnActionPerformed(evt);
            }
        });
        sideBar.add(karyawanBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 120, 40));

        cetakBarcodeBtn.setBackground(new java.awt.Color(124, 183, 89));
        cetakBarcodeBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cetakBarcodeBtn.setText("Cetak Barcode");
        cetakBarcodeBtn.setBorder(null);
        cetakBarcodeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cetakBarcodeBtnActionPerformed(evt);
            }
        });
        sideBar.add(cetakBarcodeBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 120, 40));

        KasirBtn.setBackground(new java.awt.Color(124, 183, 89));
        KasirBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        KasirBtn.setText("Kasir");
        KasirBtn.setBorder(null);
        KasirBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KasirBtnActionPerformed(evt);
            }
        });
        sideBar.add(KasirBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, 120, 40));

        namaAdmin.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        namaAdmin.setForeground(new java.awt.Color(30, 30, 30));
        namaAdmin.setText("Nama");
        sideBar.add(namaAdmin, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 50, 50, -1));

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setLayout(new java.awt.CardLayout());

        dashboard.setBackground(new java.awt.Color(255, 255, 255));

        dashboardNav.setBackground(new java.awt.Color(255, 186, 134));

        dashboardLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        dashboardLabel.setText("Dashboard");

        javax.swing.GroupLayout dashboardNavLayout = new javax.swing.GroupLayout(dashboardNav);
        dashboardNav.setLayout(dashboardNavLayout);
        dashboardNavLayout.setHorizontalGroup(
            dashboardNavLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardNavLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dashboardLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        dashboardNavLayout.setVerticalGroup(
            dashboardNavLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dashboardNavLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dashboardLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        statistikPanel.setBackground(new java.awt.Color(255, 255, 255));

        profit.setBackground(new java.awt.Color(255, 241, 237));

        jLabel9.setBackground(new java.awt.Color(80, 80, 80));
        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(80, 80, 80));
        jLabel9.setText("Profit");

        profitTx.setBackground(new java.awt.Color(80, 80, 80));
        profitTx.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        profitTx.setForeground(new java.awt.Color(80, 80, 80));
        profitTx.setText("Rp. 90.000");

        javax.swing.GroupLayout profitLayout = new javax.swing.GroupLayout(profit);
        profit.setLayout(profitLayout);
        profitLayout.setHorizontalGroup(
            profitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profitLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(profitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(profitTx, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE))
                .addGap(18, 18, 18))
        );
        profitLayout.setVerticalGroup(
            profitLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profitLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(profitTx, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pengeluaran.setBackground(new java.awt.Color(236, 251, 255));
        pengeluaran.setForeground(new java.awt.Color(236, 251, 255));

        jLabel11.setBackground(new java.awt.Color(80, 80, 80));
        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(80, 80, 80));
        jLabel11.setText("Pengeluaran");

        pengeluaranTx.setBackground(new java.awt.Color(80, 80, 80));
        pengeluaranTx.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        pengeluaranTx.setForeground(new java.awt.Color(80, 80, 80));
        pengeluaranTx.setText("Rp. 90.000");

        javax.swing.GroupLayout pengeluaranLayout = new javax.swing.GroupLayout(pengeluaran);
        pengeluaran.setLayout(pengeluaranLayout);
        pengeluaranLayout.setHorizontalGroup(
            pengeluaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pengeluaranLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pengeluaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pengeluaranTx, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
                    .addGroup(pengeluaranLayout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 48, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pengeluaranLayout.setVerticalGroup(
            pengeluaranLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pengeluaranLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pengeluaranTx, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pemasukan.setBackground(new java.awt.Color(244, 242, 255));

        jLabel8.setBackground(new java.awt.Color(80, 80, 80));
        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(80, 80, 80));
        jLabel8.setText("Pemasukan");

        pemasukanTx.setBackground(new java.awt.Color(80, 80, 80));
        pemasukanTx.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        pemasukanTx.setForeground(new java.awt.Color(80, 80, 80));
        pemasukanTx.setText("Rp. 90.000");

        javax.swing.GroupLayout pemasukanLayout = new javax.swing.GroupLayout(pemasukan);
        pemasukan.setLayout(pemasukanLayout);
        pemasukanLayout.setHorizontalGroup(
            pemasukanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pemasukanLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(pemasukanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pemasukanTx, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pemasukanLayout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 43, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pemasukanLayout.setVerticalGroup(
            pemasukanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pemasukanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pemasukanTx, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout statistikPanelLayout = new javax.swing.GroupLayout(statistikPanel);
        statistikPanel.setLayout(statistikPanelLayout);
        statistikPanelLayout.setHorizontalGroup(
            statistikPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statistikPanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(profit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(pengeluaran, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(pemasukan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        statistikPanelLayout.setVerticalGroup(
            statistikPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statistikPanelLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(statistikPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pemasukan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pengeluaran, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(profit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18))
        );

        grafikPanel.setBackground(new java.awt.Color(255, 255, 255));

        chart.setBackground(new java.awt.Color(80, 80, 80));
        chart.setForeground(new java.awt.Color(80, 80, 80));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("Cari ");

        javax.swing.GroupLayout grafikPanelLayout = new javax.swing.GroupLayout(grafikPanel);
        grafikPanel.setLayout(grafikPanelLayout);
        grafikPanelLayout.setHorizontalGroup(
            grafikPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grafikPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(grafikPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chart, javax.swing.GroupLayout.DEFAULT_SIZE, 718, Short.MAX_VALUE)
                    .addGroup(grafikPanelLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCariDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        grafikPanelLayout.setVerticalGroup(
            grafikPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, grafikPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(grafikPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCariDashboard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chart, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout dashboardLayout = new javax.swing.GroupLayout(dashboard);
        dashboard.setLayout(dashboardLayout);
        dashboardLayout.setHorizontalGroup(
            dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dashboardNav, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(dashboardLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dashboardLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(grafikPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(dashboardLayout.createSequentialGroup()
                        .addComponent(statistikPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(10, 10, 10))))
        );
        dashboardLayout.setVerticalGroup(
            dashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardLayout.createSequentialGroup()
                .addComponent(dashboardNav, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(statistikPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(grafikPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        mainPanel.add(dashboard, "card2");

        PanelInputMenu1.setBackground(new java.awt.Color(255, 186, 134));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Input Menu");

        javax.swing.GroupLayout PanelInputMenu1Layout = new javax.swing.GroupLayout(PanelInputMenu1);
        PanelInputMenu1.setLayout(PanelInputMenu1Layout);
        PanelInputMenu1Layout.setHorizontalGroup(
            PanelInputMenu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInputMenu1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel14)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelInputMenu1Layout.setVerticalGroup(
            PanelInputMenu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelInputMenu1Layout.createSequentialGroup()
                .addContainerGap(7, Short.MAX_VALUE)
                .addComponent(jLabel14)
                .addContainerGap())
        );

        PanelInputMenu2.setBackground(new java.awt.Color(254, 239, 208));

        FieldInputMenu1.setEditable(false);
        FieldInputMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldInputMenu1ActionPerformed(evt);
            }
        });

        LabelInputMenu1.setText("Kode Makanan");

        FieldInputMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldInputMenu2ActionPerformed(evt);
            }
        });

        LabelInputMenu2.setText("Nama Makanan");

        FieldInputMenu4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldInputMenu4ActionPerformed(evt);
            }
        });

        LabelInputMenu5.setText("Harga Beli");

        FieldInputMenu5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldInputMenu5ActionPerformed(evt);
            }
        });

        LabelInputMenu6.setText("Harga Jual");

        FieldInputMenu6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldInputMenu6ActionPerformed(evt);
            }
        });
        FieldInputMenu6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                FieldInputMenu6KeyTyped(evt);
            }
        });

        ButtonCari.setText("Cari");
        ButtonCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonCariActionPerformed(evt);
            }
        });

        ButtonSimpan.setText("Simpan");
        ButtonSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonSimpanActionPerformed(evt);
            }
        });

        ButtonEdit.setText("Edit");
        ButtonEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonEditActionPerformed(evt);
            }
        });

        ButtonHapus.setText("Hapus");
        ButtonHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonHapusActionPerformed(evt);
            }
        });

        uploadBtnMenu.setText("upload");
        uploadBtnMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadBtnMenuActionPerformed(evt);
            }
        });

        LbFoto_Makanan.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        ButtonHapus1.setText("Batal");
        ButtonHapus1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonHapus1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelInputMenu2Layout = new javax.swing.GroupLayout(PanelInputMenu2);
        PanelInputMenu2.setLayout(PanelInputMenu2Layout);
        PanelInputMenu2Layout.setHorizontalGroup(
            PanelInputMenu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInputMenu2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(PanelInputMenu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FieldInputMenu1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PanelInputMenu2Layout.createSequentialGroup()
                        .addComponent(LbFoto_Makanan, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(uploadBtnMenu))
                    .addComponent(LabelInputMenu2)
                    .addComponent(FieldInputMenu2, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelInputMenu1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 212, Short.MAX_VALUE)
                .addGroup(PanelInputMenu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FieldInputMenu4, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelInputMenu5)
                    .addComponent(FieldInputMenu5, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelInputMenu6)
                    .addGroup(PanelInputMenu2Layout.createSequentialGroup()
                        .addComponent(FieldInputMenu6, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ButtonCari, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(41, 41, 41))
            .addGroup(PanelInputMenu2Layout.createSequentialGroup()
                .addGap(214, 214, 214)
                .addComponent(ButtonSimpan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ButtonEdit)
                .addGap(14, 14, 14)
                .addComponent(ButtonHapus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ButtonHapus1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelInputMenu2Layout.setVerticalGroup(
            PanelInputMenu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInputMenu2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelInputMenu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelInputMenu1)
                    .addComponent(LabelInputMenu5))
                .addGap(2, 2, 2)
                .addGroup(PanelInputMenu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FieldInputMenu1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FieldInputMenu4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelInputMenu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelInputMenu2)
                    .addComponent(LabelInputMenu6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelInputMenu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FieldInputMenu2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FieldInputMenu5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(PanelInputMenu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PanelInputMenu2Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(PanelInputMenu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(FieldInputMenu6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ButtonCari)
                            .addComponent(uploadBtnMenu))
                        .addGap(41, 41, 41)
                        .addGroup(PanelInputMenu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ButtonSimpan)
                            .addComponent(ButtonEdit)
                            .addComponent(ButtonHapus)
                            .addComponent(ButtonHapus1)))
                    .addGroup(PanelInputMenu2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(LbFoto_Makanan, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabelMenu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Kode Transaksi", "Nama Makanan", "Harga Beli", "Harga Jual", "Foto Makanan"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabelMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelMenuMouseClicked(evt);
            }
        });
        TabelInputMenu.setViewportView(tabelMenu);

        javax.swing.GroupLayout inputMenuLayout = new javax.swing.GroupLayout(inputMenu);
        inputMenu.setLayout(inputMenuLayout);
        inputMenuLayout.setHorizontalGroup(
            inputMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelInputMenu1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(inputMenuLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(inputMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(PanelInputMenu2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TabelInputMenu))
                .addGap(45, 45, 45))
        );
        inputMenuLayout.setVerticalGroup(
            inputMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputMenuLayout.createSequentialGroup()
                .addComponent(PanelInputMenu1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PanelInputMenu2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(TabelInputMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        mainPanel.add(inputMenu, "card3");

        histori.setBackground(new java.awt.Color(255, 255, 255));

        historiNav.setBackground(new java.awt.Color(255, 185, 132));

        historiLabel.setBackground(new java.awt.Color(255, 255, 255));
        historiLabel.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        historiLabel.setForeground(new java.awt.Color(255, 255, 255));
        historiLabel.setText("Laporan Transaksi");

        javax.swing.GroupLayout historiNavLayout = new javax.swing.GroupLayout(historiNav);
        historiNav.setLayout(historiNavLayout);
        historiNavLayout.setHorizontalGroup(
            historiNavLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historiNavLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(historiLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        historiNavLayout.setVerticalGroup(
            historiNavLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, historiNavLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(historiLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                .addContainerGap())
        );

        historiPanel.setBackground(new java.awt.Color(254, 239, 210));

        tabelHistori.setForeground(new java.awt.Color(40, 40, 40));
        tabelHistori.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No.", "Kode Transaksi", "Produk", "Jumlah", "Harga Jual", "Harga Beli", "Total", "Tanggal"
            }
        ));
        historiScrollPane.setViewportView(tabelHistori);

        cariTx.setForeground(new java.awt.Color(80, 80, 80));
        cariTx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cariTxActionPerformed(evt);
            }
        });
        cariTx.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cariTxKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout historiPanelLayout = new javax.swing.GroupLayout(historiPanel);
        historiPanel.setLayout(historiPanelLayout);
        historiPanelLayout.setHorizontalGroup(
            historiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historiPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(historiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(historiPanelLayout.createSequentialGroup()
                        .addComponent(historiScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 662, Short.MAX_VALUE)
                        .addGap(28, 28, 28))
                    .addGroup(historiPanelLayout.createSequentialGroup()
                        .addComponent(cariTx, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        historiPanelLayout.setVerticalGroup(
            historiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historiPanelLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(cariTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(historiScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 325, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout historiLayout = new javax.swing.GroupLayout(histori);
        histori.setLayout(historiLayout);
        historiLayout.setHorizontalGroup(
            historiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(historiNav, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, historiLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(historiPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(25, 25, 25))
        );
        historiLayout.setVerticalGroup(
            historiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historiLayout.createSequentialGroup()
                .addComponent(historiNav, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addComponent(historiPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(25, 25, 25))
        );

        mainPanel.add(histori, "card4");

        PanelInputUser1.setBackground(new java.awt.Color(255, 186, 134));

        LabelInputUser1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        LabelInputUser1.setForeground(new java.awt.Color(255, 255, 255));
        LabelInputUser1.setText("Input User");

        javax.swing.GroupLayout PanelInputUser1Layout = new javax.swing.GroupLayout(PanelInputUser1);
        PanelInputUser1.setLayout(PanelInputUser1Layout);
        PanelInputUser1Layout.setHorizontalGroup(
            PanelInputUser1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInputUser1Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(LabelInputUser1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        PanelInputUser1Layout.setVerticalGroup(
            PanelInputUser1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInputUser1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(LabelInputUser1)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        PanelInputUser2.setBackground(new java.awt.Color(254, 239, 208));

        FieldInputUser1.setEditable(false);
        FieldInputUser1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldInputUser1ActionPerformed(evt);
            }
        });

        LabelInputUser2.setForeground(new java.awt.Color(80, 80, 80));
        LabelInputUser2.setText("Kode User");

        FieldInputUser2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldInputUser2ActionPerformed(evt);
            }
        });

        FieldInputUser3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldInputUser3ActionPerformed(evt);
            }
        });

        LabelInputUser3.setForeground(new java.awt.Color(80, 80, 80));
        LabelInputUser3.setText("No. Hp");

        LabelInputUser4.setForeground(new java.awt.Color(80, 80, 80));
        LabelInputUser4.setText("Alamat");

        FieldInputUser4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldInputUser4ActionPerformed(evt);
            }
        });

        LabelInputUser5.setForeground(new java.awt.Color(80, 80, 80));
        LabelInputUser5.setText("Username");

        FieldInputUser5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldInputUser5ActionPerformed(evt);
            }
        });

        LabelInputUser6.setForeground(new java.awt.Color(80, 80, 80));
        LabelInputUser6.setText("Password");

        FieldInputUser6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldInputUser6ActionPerformed(evt);
            }
        });
        FieldInputUser6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                FieldInputUser6KeyTyped(evt);
            }
        });

        ButtonInputUserCari.setText("Cari");
        ButtonInputUserCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonInputUserCariActionPerformed(evt);
            }
        });

        ButtonInputUserSimpn.setText("Simpan");
        ButtonInputUserSimpn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonInputUserSimpnActionPerformed(evt);
            }
        });

        ButtonInputUserEdit.setText("Edit");
        ButtonInputUserEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonInputUserEditActionPerformed(evt);
            }
        });

        ButtonInputUserHapus.setText("Hapus");
        ButtonInputUserHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonInputUserHapusActionPerformed(evt);
            }
        });

        inputRole.setForeground(new java.awt.Color(80, 80, 80));
        inputRole.setText("Role");

        cmbBoxUser.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "karyawan", "admin" }));

        ButtonInputUserBatal.setText("Batal");
        ButtonInputUserBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonInputUserBatalActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PanelInputUser2Layout = new javax.swing.GroupLayout(PanelInputUser2);
        PanelInputUser2.setLayout(PanelInputUser2Layout);
        PanelInputUser2Layout.setHorizontalGroup(
            PanelInputUser2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInputUser2Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(PanelInputUser2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(PanelInputUser2Layout.createSequentialGroup()
                        .addComponent(ButtonInputUserSimpn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ButtonInputUserEdit)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ButtonInputUserHapus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ButtonInputUserBatal))
                    .addGroup(PanelInputUser2Layout.createSequentialGroup()
                        .addGroup(PanelInputUser2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(PanelInputUser2Layout.createSequentialGroup()
                                .addComponent(FieldInputUser6, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ButtonInputUserCari, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(FieldInputUser1, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LabelInputUser2)
                            .addComponent(FieldInputUser4, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LabelInputUser5))
                        .addGap(31, 31, 31)
                        .addGroup(PanelInputUser2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(FieldInputUser2, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LabelInputUser3)
                            .addComponent(LabelInputUser6)
                            .addComponent(FieldInputUser5, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(PanelInputUser2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(FieldInputUser3, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                            .addComponent(LabelInputUser4)
                            .addComponent(inputRole)
                            .addComponent(cmbBoxUser, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        PanelInputUser2Layout.setVerticalGroup(
            PanelInputUser2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInputUser2Layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addGroup(PanelInputUser2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LabelInputUser2)
                    .addComponent(LabelInputUser3)
                    .addComponent(LabelInputUser4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelInputUser2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FieldInputUser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FieldInputUser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FieldInputUser3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(PanelInputUser2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(PanelInputUser2Layout.createSequentialGroup()
                        .addGroup(PanelInputUser2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelInputUser5)
                            .addComponent(LabelInputUser6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(PanelInputUser2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(FieldInputUser4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(FieldInputUser5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbBoxUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(PanelInputUser2Layout.createSequentialGroup()
                        .addComponent(inputRole)
                        .addGap(32, 32, 32)))
                .addGap(38, 38, 38)
                .addGroup(PanelInputUser2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FieldInputUser6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ButtonInputUserCari))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PanelInputUser2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonInputUserSimpn)
                    .addComponent(ButtonInputUserEdit)
                    .addComponent(ButtonInputUserHapus)
                    .addComponent(ButtonInputUserBatal))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        TabelInputUser1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Kode User", "Username", "No Hp", "Alamat", "Role"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TabelInputUser1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabelInputUser1MouseClicked(evt);
            }
        });
        TabelInputUser.setViewportView(TabelInputUser1);

        javax.swing.GroupLayout InputUserLayout = new javax.swing.GroupLayout(InputUser);
        InputUser.setLayout(InputUserLayout);
        InputUserLayout.setHorizontalGroup(
            InputUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelInputUser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(InputUserLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(InputUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TabelInputUser)
                    .addComponent(PanelInputUser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(25, 25, 25))
        );
        InputUserLayout.setVerticalGroup(
            InputUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InputUserLayout.createSequentialGroup()
                .addComponent(PanelInputUser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(PanelInputUser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(TabelInputUser, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        mainPanel.add(InputUser, "card5");

        ceteakBarccodePanel.setBackground(new java.awt.Color(254, 239, 208));

        inputDiskonBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputDiskonBarcodeActionPerformed(evt);
            }
        });

        searchInputBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchInputBarcodeActionPerformed(evt);
            }
        });
        searchInputBarcode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchInputBarcodeKeyTyped(evt);
            }
        });

        saveBtnBarcode.setLabel("Simpan");
        saveBtnBarcode.setName("Simpan"); // NOI18N
        saveBtnBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnBarcodeActionPerformed(evt);
            }
        });

        hapusBtnBarcode.setLabel("Hapus");
        hapusBtnBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hapusBtnBarcodeActionPerformed(evt);
            }
        });

        cariBtnBarcode.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        cariBtnBarcode.setLabel("Cari");
        cariBtnBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cariBtnBarcodeActionPerformed(evt);
            }
        });

        diskonBarcode.setForeground(new java.awt.Color(80, 80, 80));
        diskonBarcode.setText("Diskon");

        javax.swing.GroupLayout ceteakBarccodePanelLayout = new javax.swing.GroupLayout(ceteakBarccodePanel);
        ceteakBarccodePanel.setLayout(ceteakBarccodePanelLayout);
        ceteakBarccodePanelLayout.setHorizontalGroup(
            ceteakBarccodePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ceteakBarccodePanelLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(ceteakBarccodePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(diskonBarcode)
                    .addGroup(ceteakBarccodePanelLayout.createSequentialGroup()
                        .addComponent(inputDiskonBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(searchInputBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cariBtnBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ceteakBarccodePanelLayout.createSequentialGroup()
                        .addComponent(saveBtnBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hapusBtnBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(243, Short.MAX_VALUE))
        );
        ceteakBarccodePanelLayout.setVerticalGroup(
            ceteakBarccodePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ceteakBarccodePanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(diskonBarcode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ceteakBarccodePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ceteakBarccodePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(searchInputBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(inputDiskonBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cariBtnBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                .addGroup(ceteakBarccodePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saveBtnBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hapusBtnBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(54, 54, 54))
        );

        barcodeTabel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Kode Barcode", "Diskon", "Barcode"
            }
        ));
        barcodeTabel.setCellSelectionEnabled(true);
        jScrollPaneBarcode.setViewportView(barcodeTabel);

        barcodeNav.setBackground(new java.awt.Color(255, 186, 134));

        barcodeNavLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        barcodeNavLabel.setForeground(new java.awt.Color(255, 255, 255));
        barcodeNavLabel.setText("Cetak Barcode");

        javax.swing.GroupLayout barcodeNavLayout = new javax.swing.GroupLayout(barcodeNav);
        barcodeNav.setLayout(barcodeNavLayout);
        barcodeNavLayout.setHorizontalGroup(
            barcodeNavLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(barcodeNavLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(barcodeNavLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        barcodeNavLayout.setVerticalGroup(
            barcodeNavLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(barcodeNavLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(barcodeNavLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout cetakBarcodeLayout = new javax.swing.GroupLayout(cetakBarcode);
        cetakBarcode.setLayout(cetakBarcodeLayout);
        cetakBarcodeLayout.setHorizontalGroup(
            cetakBarcodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cetakBarcodeLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(cetakBarcodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ceteakBarccodePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPaneBarcode))
                .addGap(23, 23, 23))
            .addComponent(barcodeNav, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        cetakBarcodeLayout.setVerticalGroup(
            cetakBarcodeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(cetakBarcodeLayout.createSequentialGroup()
                .addComponent(barcodeNav, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ceteakBarccodePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPaneBarcode, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addGap(24, 24, 24))
        );

        mainPanel.add(cetakBarcode, "card6");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(sideBar, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sideBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void inputMenuBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputMenuBtnActionPerformed
        // TODO add your handling code here:
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
        
        mainPanel.add(inputMenu);
        mainPanel.repaint();
        mainPanel.revalidate();
        loadDataMenu();
    }//GEN-LAST:event_inputMenuBtnActionPerformed

    private void logOutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logOutBtnActionPerformed
        // TODO add your handling code here:
        Login x = new Login();
        x.setVisible(true);
        dispose();
    }//GEN-LAST:event_logOutBtnActionPerformed

    private void homeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeBtnActionPerformed
        // TODO add your handling code here:
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
        
        mainPanel.add(dashboard);
        mainPanel.repaint();
        mainPanel.revalidate();
        hitungKeuntungan();
//        chartData();
    }//GEN-LAST:event_homeBtnActionPerformed

    private void historiBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_historiBtnActionPerformed
        // TODO add your handling code here:
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
        
        mainPanel.add(histori);
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_historiBtnActionPerformed

    private void karyawanBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_karyawanBtnActionPerformed
        // TODO add your handling code here:
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
        
        mainPanel.add(InputUser);
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_karyawanBtnActionPerformed


    private void FieldInputMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldInputMenu1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldInputMenu1ActionPerformed

    private void FieldInputMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldInputMenu2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldInputMenu2ActionPerformed

    private void FieldInputMenu4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldInputMenu4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldInputMenu4ActionPerformed

    private void FieldInputMenu5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldInputMenu5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldInputMenu5ActionPerformed

    private void FieldInputMenu6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldInputMenu6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldInputMenu6ActionPerformed

    private void ButtonCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonCariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ButtonCariActionPerformed

    private void ButtonSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonSimpanActionPerformed
        // TODO add your handling code here:
        String id = FieldInputMenu1.getText();
        String namaMakanan = FieldInputMenu2.getText();
        String hargaBeli = FieldInputMenu4.getText();
        String hargaJual = FieldInputMenu5.getText();
        
        try{
            if (FieldInputMenu1.getText().length() > 0 && FieldInputMenu2.getText().length() > 0 && FieldInputMenu4.getText().length() > 0  && FieldInputMenu5.getText().length() > 0 && pathMenu != null) {
                java.sql.Connection c = Koneksi.getKoneksi();
                String sql = "INSERT INTO menu (kode_makanan, nama_makanan, harga_beli, harga_jual, foto_makanan) values(?,?,?,?,?)";
                java.sql.PreparedStatement p = c.prepareStatement(sql);
                p.setString(1, id);
                p.setString(2, namaMakanan);
                p.setString(3, hargaBeli);
                p.setString(4, hargaJual);
                 InputStream is = new FileInputStream(new File(pathMenu));
                p.setBlob(5, is);

                p.executeUpdate();
                p.close();
                JOptionPane.showMessageDialog(null, "Data Tersimpan");
                loadDataMenu();
                FieldInputMenu1.setText("");
                FieldInputMenu2.setText("");
                FieldInputMenu4.setText("");
                FieldInputMenu5.setText("");
                LbFoto_Makanan.setIcon(null);
                autoNumberMenu();
            }else{
                JOptionPane.showMessageDialog(null, "Data Tidak Lengkap");
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }//GEN-LAST:event_ButtonSimpanActionPerformed

    private void ButtonEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonEditActionPerformed
        // TODO add your handling code here:
        int i = tabelMenu.getSelectedRow();
        if(i == -1 ){
            JOptionPane.showMessageDialog(null, "Pilih Data Terlebih Dahulu");
            return;
        }
        String id = (String)modelMenu.getValueAt(i,0);
        String namaMakanan = FieldInputMenu2.getText();
        String hargaBeli = FieldInputMenu4.getText();
        String hargaJual = FieldInputMenu5.getText();
        
        if (FieldInputMenu1.getText().length() > 0 && FieldInputMenu2.getText().length() > 0 && FieldInputMenu4.getText().length() > 0  && FieldInputMenu5.getText().length() > 0) {
        try{
          java.sql.Connection c = Koneksi.getKoneksi();
          String sql = "UPDATE menu SET nama_makanan = ?, harga_beli = ?, harga_jual = ?, foto_makanan = ? where kode_makanan = ?";
          java.sql.PreparedStatement p = c.prepareStatement(sql);
          p.setString(5, id);
          p.setString(1, namaMakanan);
          p.setString(2, hargaBeli);
          p.setString(3, hargaJual);
         Icon icon = LbFoto_Makanan.getIcon();
            ImageIcon imageIcon = (ImageIcon) icon;
            Image image = imageIcon.getImage();
            BufferedImage bufferedImage = new BufferedImage(imageIcon.getIconWidth(), imageIcon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
Graphics2D g2d = bufferedImage.createGraphics();
g2d.drawImage(image, 0, 0, null);
g2d.dispose();

ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
ImageIO.write(bufferedImage, "jpg", outputStream);
byte[] imageBytes = outputStream.toByteArray();
          p.setBytes(4, imageBytes);
          
          p.executeUpdate();
          p.close();
          JOptionPane.showMessageDialog(null, "Data Berhasil Di Edit");
          FieldInputMenu1.setText("");
          FieldInputMenu2.setText("");
          FieldInputMenu4.setText("");
          FieldInputMenu5.setText("");
          LbFoto_Makanan.setIcon(null);
          loadDataMenu();
            System.out.println(imageBytes);
          autoNumberMenu();
          ButtonSimpan.setEnabled(true);
        }catch(Exception e){
            System.out.println(e);
        }
        } else {
             JOptionPane.showMessageDialog(null, "Data Tidak Lengkap");
        }
        
    }//GEN-LAST:event_ButtonEditActionPerformed

    private void ButtonHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonHapusActionPerformed
        // TODO add your handling code here:
        int i = tabelMenu.getSelectedRow();
        if(i == -1 ){
            JOptionPane.showMessageDialog(null, "Pilih Data Terlebih Dahulu");
            return;
        }
        String id = (String)modelMenu.getValueAt(i,0);
        int question = JOptionPane.showConfirmDialog(null, "Yakin ingin mneghapus data?", "konfirmasi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(question == JOptionPane.OK_OPTION){
            try{
                 java.sql.Connection c = Koneksi.getKoneksi();
                 String sql = "DELETE from menu where kode_makanan = ?";
                 java.sql.PreparedStatement p = c.prepareStatement(sql);
                 p.setString(1, id);
                 p.execute();
                 p.close();
                 JOptionPane.showMessageDialog(null, "Data Terhapus");
                 loadDataMenu();
                 autoNumberMenu();
                 ButtonSimpan.setEnabled(true);
            }catch(Exception e){
                System.out.println(e);
            }
           FieldInputMenu1.setText("");
          FieldInputMenu2.setText("");
          FieldInputMenu4.setText("");
          FieldInputMenu5.setText("");
          LbFoto_Makanan.setIcon(null);
          loadDataMenu();
          autoNumberMenu();
        }
        if(question == JOptionPane.CANCEL_OPTION){
            
        }
    }//GEN-LAST:event_ButtonHapusActionPerformed

    private void FieldInputUser1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldInputUser1ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_FieldInputUser1ActionPerformed

    private void FieldInputUser2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldInputUser2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldInputUser2ActionPerformed

    private void FieldInputUser3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldInputUser3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldInputUser3ActionPerformed

    private void FieldInputUser4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldInputUser4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldInputUser4ActionPerformed

    private void FieldInputUser5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldInputUser5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldInputUser5ActionPerformed

    private void FieldInputUser6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldInputUser6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldInputUser6ActionPerformed

    private void ButtonInputUserCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInputUserCariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ButtonInputUserCariActionPerformed

    private void ButtonInputUserSimpnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInputUserSimpnActionPerformed
        // TODO add your handling code here:
//        String id = FieldInputUser1.getText();
        String username = FieldInputUser4.getText();
        String noHp = FieldInputUser2.getText();
        String password = FieldInputUser5.getText();
        String alamat = FieldInputUser3.getText();
        String role = (String) cmbBoxUser.getSelectedItem();
        
        try{
            if (FieldInputUser4.getText().length() > 0 && FieldInputUser2.getText().length() > 0 && FieldInputUser5.getText().length() > 0  && FieldInputUser3.getText().length() > 0) {
                java.sql.Connection c = Koneksi.getKoneksi();
                String sql = "INSERT INTO user ( username, password, No_Hp, Alamt, Role) values(?,?,?,?,?)";
                java.sql.PreparedStatement p = c.prepareStatement(sql);
                p.setString(1, username);
                p.setString(2, password);
                p.setString(3, noHp);
                p.setString(4, alamat);
                p.setString(5, role);
                
                p.executeUpdate();
                p.close();
                JOptionPane.showMessageDialog(null, "Data Tersimpan");
                loadDataUser();
                FieldInputUser1.setText("");
                FieldInputUser4.setText("");
                FieldInputUser2.setText("");
                FieldInputUser5.setText("");
                FieldInputUser3.setText("");
                autoNumberUser();
            }else{
                JOptionPane.showMessageDialog(null, "Data Tidak Lengkap");
            }
        }catch(Exception e){
            System.out.println(e);
        }
    }//GEN-LAST:event_ButtonInputUserSimpnActionPerformed

    private void searchInputBarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchInputBarcodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchInputBarcodeActionPerformed

    private void inputDiskonBarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputDiskonBarcodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputDiskonBarcodeActionPerformed

    private void cariBtnBarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cariBtnBarcodeActionPerformed
        // TODO add your handling code here:
        cari();
    }//GEN-LAST:event_cariBtnBarcodeActionPerformed

    private void uploadBtnMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadBtnMenuActionPerformed
            // TODO add your handling code here:
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(null);
        File f = chooser.getSelectedFile();
        String path = f.getAbsolutePath();
        try{
            BufferedImage bi  = ImageIO.read(new File(path));
            Image img = bi.getScaledInstance(168, 99, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(img);
            LbFoto_Makanan.setIcon(icon);
            pathMenu = path;
        }catch(Exception e){
            System.out.println(e);
        }
    }//GEN-LAST:event_uploadBtnMenuActionPerformed

    private void tabelMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelMenuMouseClicked
        // TODO add your handling code here:
        ButtonSimpan.setEnabled(false);
        int i = tabelMenu.getSelectedRow();
        if(i == -1 ){
            return;
        }
        String id = (String) modelMenu.getValueAt(i,0);
        String nama_makanan = (String) modelMenu.getValueAt(i,1);
        String harga_beli = (String) modelMenu.getValueAt(i,2);
        String harga_jual = (String) modelMenu.getValueAt(i,3);
        ImageIcon icon = (ImageIcon) modelMenu.getValueAt(i, 4);
        Image image = icon.getImage();
        int width = 200;
             int height = 120;
             
             Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        Icon newIcon = new ImageIcon(resizedImage);
//        System.out.println(foto);
        
        FieldInputMenu1.setText(id);
        FieldInputMenu2.setText(nama_makanan);
        FieldInputMenu4.setText(harga_beli);
        FieldInputMenu5.setText(harga_jual);
        LbFoto_Makanan.setIcon(newIcon);
    }//GEN-LAST:event_tabelMenuMouseClicked

    private void ButtonHapus1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonHapus1ActionPerformed
        // TODO add your handling code here:
         FieldInputMenu1.setText("");
          FieldInputMenu2.setText("");
          FieldInputMenu4.setText("");
          FieldInputMenu5.setText("");
          LbFoto_Makanan.setIcon(null);
          autoNumberMenu();
          ButtonSimpan.setEnabled(true);
    }//GEN-LAST:event_ButtonHapus1ActionPerformed

    private void cetakBarcodeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cetakBarcodeBtnActionPerformed
        // TODO add your handling code here:
        mainPanel.removeAll();
        mainPanel.repaint();
        mainPanel.revalidate();
        
        mainPanel.add(cetakBarcode);
        mainPanel.repaint();
        mainPanel.revalidate();
    }//GEN-LAST:event_cetakBarcodeBtnActionPerformed

    private void saveBtnBarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnBarcodeActionPerformed
        // TODO add your handling code here: 
        try {
            Linear barcode = new Linear();
            barcode.setType(Linear.CODE39);
            barcode.setData(inputDiskonBarcode.getText());
            barcode.setI(11.0f);
            
            String fname = inputDiskonBarcode.getText();
            barcode.renderBarcode("C:\\daffa\\" + fname + ".png" );
            File barcodeFile = new File("C:\\daffa\\" + fname + ".png");
    
    // Membaca data gambar sebagai byte array
    FileInputStream fis = new FileInputStream(barcodeFile);
    byte[] imageData = new byte[(int) barcodeFile.length()];
    fis.read(imageData);
    fis.close();

                
    // Memasukkan data gambar ke dalam database
    java.sql.Connection c = Koneksi.getKoneksi();
    String sql = "INSERT INTO barcode (diskon, barcode) VALUES (?, ?)";
    java.sql.PreparedStatement p = c.prepareStatement(sql);
    p.setString(1, inputDiskonBarcode.getText());
    p.setBytes(2, imageData);
    p.executeUpdate();
    p.close();

    JOptionPane.showMessageDialog(null, "Data Tersimpan");
        } catch (Exception e) {
            System.out.println("Terjadi Kesalahan" + e.getMessage());
        }finally{
            clearBarcode();
            inputDiskonBarcode.setText("");
           loadDataBarcode();
        }
    }//GEN-LAST:event_saveBtnBarcodeActionPerformed

    private void hapusBtnBarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hapusBtnBarcodeActionPerformed
        // TODO add your handling code here:
        int i = barcodeTabel.getSelectedRow();
        if(i == -1 ){
            JOptionPane.showMessageDialog(null, "Pilih Data Terlebih Dahulu");
            return;
        }
        String id = (String)barcodeModel.getValueAt(i,0);
        int question = JOptionPane.showConfirmDialog(null, "Yakin ingin mneghapus data?", "konfirmasi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(question == JOptionPane.OK_OPTION){
            try{
                 java.sql.Connection c = Koneksi.getKoneksi();
                 String sql = "DELETE from barcode where kode_barcode = ? ";
                 java.sql.PreparedStatement p = c.prepareStatement(sql);
                 p.setString(1, id);
                 p.execute();
                 p.close();
                 JOptionPane.showMessageDialog(null, "Data Terhapus");
                 loadDataMenu();
                 autoNumberMenu();
                 hapusBtnBarcode.setEnabled(true);
            }catch(Exception e){
                System.out.println(e);
            }
          loadDataBarcode();
        }
        if(question == JOptionPane.CANCEL_OPTION){
            
        }
    }//GEN-LAST:event_hapusBtnBarcodeActionPerformed

    private void FieldInputMenu6KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FieldInputMenu6KeyTyped
        // TODO add your handling code here:
        String key = FieldInputMenu6.getText();
        DefaultTableModel tabel = new DefaultTableModel();
        tabel.addColumn("Kode Makanan");
        tabel.addColumn("Nama Makanan");
        tabel.addColumn("Harga Beli");
        tabel.addColumn("Harga Jual");
        tabel.addColumn("Foto Makanan");
          try{
            java.sql.Connection c = Koneksi.getKoneksi();
            java.sql.Statement s = c.createStatement();
            
            String sql = "SELECT * FROM menu WHERE nama_makanan LIKE '%" + key + "%' ORDER BY nama_makanan";
            ResultSet r = s.executeQuery(sql);
           
            while(r.next()){
                byte[] imageData = r.getBytes("foto_makanan");
                ImageIcon imageIcon = new ImageIcon(imageData);
                tabel.addRow(new Object[]{
               r.getString(1),
               r.getString(2),
               r.getString(3),
               r.getString(4),
               imageIcon
            }); 
            tabelMenu.setModel(tabel);
            }
            r.close();
            s.close();
        }catch(Exception e){
            System.out.println(e);
        }
          loadDataMenu();
           setTableRenderMenu();
    }//GEN-LAST:event_FieldInputMenu6KeyTyped

    private void cariTxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cariTxKeyTyped
        // TODO add your handling code here:
        String key = cariTx.getText();
        DefaultTableModel tabel = new DefaultTableModel();
         tabel.addColumn("No");
        tabel.addColumn("Kode Transaksi");
        tabel.addColumn("Nama Makanan");
        tabel.addColumn("Jumlah");
        tabel.addColumn("Harga Beli");
        tabel.addColumn("Harga Jual");
        tabel.addColumn("Tanggal");
          try{
            java.sql.Connection c = Koneksi.getKoneksi();
            java.sql.Statement s = c.createStatement();
            
            String sql = "SELECT ROW_NUMBER() OVER (ORDER BY detail_transaksi.kode_transaksi) AS nomor,\n" +
"    detail_transaksi.kode_transaksi,\n" +
"    menu.nama_makanan,\n" +
"    COUNT(menu.nama_makanan) AS jumlah,\n" +
"    menu.harga_beli,\n" +
"    menu.harga_jual,\n" +
"    transaksi.tanggal\n" +
"FROM detail_transaksi\n" +
"INNER JOIN transaksi ON detail_transaksi.kode_transaksi = transaksi.kode_transaksi\n" +
"INNER JOIN menu ON detail_transaksi.kode_makanan = menu.kode_makanan\n" +
"WHERE detail_transaksi.kode_transaksi LIKE '%"+key+"%' \n" +
"    OR menu.nama_makanan LIKE '%"+key+"%'\n" +
"    OR transaksi.tanggal LIKE '%"+key+"%'\n" +
"GROUP BY detail_transaksi.kode_transaksi,\n" +
"    menu.nama_makanan,\n" +
"    menu.harga_beli,\n" +
"    menu.harga_jual,\n" +
"    transaksi.tanggal;";
            ResultSet r = s.executeQuery(sql);
           
            while(r.next()){
                tabel.addRow(new Object[]{
               r.getString(1),
               r.getString(2),
               r.getString(3),
               r.getString(4),
               r.getString(5),
               r.getString(6),
               r.getString(7),
            });
            tabelHistori.setModel(tabel);
            }
            r.close();
            s.close();
        }catch(Exception e){
            System.out.println(e);
        }
       loadHistori();
  
    }//GEN-LAST:event_cariTxKeyTyped

    private void searchInputBarcodeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchInputBarcodeKeyTyped
        // TODO add your handling code here:
         cari();
    }//GEN-LAST:event_searchInputBarcodeKeyTyped

    private void ButtonInputUserEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInputUserEditActionPerformed
        // TODO add your handling code here:
         int i = TabelInputUser1.getSelectedRow();
        if(i == -1 ){
            JOptionPane.showMessageDialog(null, "Pilih Data Terlebih Dahulu");
            return;
        }
        String id = (String)userModel.getValueAt(i,0);
        String username = FieldInputUser4.getText();
        String noHp = FieldInputUser2.getText();
        String password = FieldInputUser5.getText();
        String alamat = FieldInputUser3.getText();
        String role = (String) cmbBoxUser.getSelectedItem();
        
        if (FieldInputUser4.getText().length() > 0 && FieldInputUser2.getText().length() > 0 && FieldInputUser5.getText().length() > 0  && FieldInputUser3.getText().length() > 0) {
        try{
          java.sql.Connection c = Koneksi.getKoneksi();
         String sql = "UPDATE user SET username = ?, password = ?, No_Hp = ?, Alamt = ?, Role = ? where kode_user = ?";
                java.sql.PreparedStatement p = c.prepareStatement(sql);
                p.setString(1, username);
                p.setString(2, password);
                p.setString(3, noHp);
                p.setString(4, alamat);
                p.setString(5, role);
                p.setString(6, id);
          p.executeUpdate();
          p.close();
          JOptionPane.showMessageDialog(null, "Data Berhasil Di Edit");
          FieldInputUser4.setText("");
          FieldInputUser2.setText("");
          FieldInputUser5.setText("");
          FieldInputUser3.setText("");
          LbFoto_Makanan.setIcon(null);
          loadDataUser();
          autoNumberUser();
          ButtonSimpan.setEnabled(true);
        }catch(Exception e){
            System.out.println(e);
        }
        } else {
             JOptionPane.showMessageDialog(null, "Data Tidak Lengkap");
        }
        
    }//GEN-LAST:event_ButtonInputUserEditActionPerformed

    private void TabelInputUser1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabelInputUser1MouseClicked
        // TODO add your handling code here:
        ButtonInputUserSimpn.setEnabled(false);
        int i = TabelInputUser1.getSelectedRow();
        if(i == -1 ){
            return;
        }
        String id = (String) userModel.getValueAt(i,0);
        String username = (String) userModel.getValueAt(i,1);
        String password = (String) userModel.getValueAt(i,2);
        String No_Hp = (String) userModel.getValueAt(i,3);
        String alamat = (String) userModel.getValueAt(i,4);
        
        
        FieldInputUser1.setText(id);
        FieldInputUser4.setText(username);
        FieldInputUser5.setText(password);
        FieldInputUser2.setText(No_Hp);
        FieldInputUser3.setText(alamat);
        
    }//GEN-LAST:event_TabelInputUser1MouseClicked

    private void ButtonInputUserHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInputUserHapusActionPerformed
        // TODO add your handling code here:
        int i = TabelInputUser1.getSelectedRow();
        if(i == -1 ){
            JOptionPane.showMessageDialog(null, "Pilih Data Terlebih Dahulu");
            return;
        }
        String id = (String)userModel.getValueAt(i,0);
        int question = JOptionPane.showConfirmDialog(null, "Yakin ingin mneghapus data?", "konfirmasi", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if(question == JOptionPane.OK_OPTION){
            try{
                 java.sql.Connection c = Koneksi.getKoneksi();
                 String sql = "DELETE from user where kode_user = ?";
                 java.sql.PreparedStatement p = c.prepareStatement(sql);
                 p.setString(1, id);
                 p.execute();
                 p.close();
                 JOptionPane.showMessageDialog(null, "Data Terhapus");
                 loadDataMenu();
                 autoNumberMenu();
                 ButtonInputUserSimpn.setEnabled(true);
            }catch(Exception e){
                System.out.println(e);
            }
            FieldInputUser1.setText("");
        FieldInputUser4.setText("");
        FieldInputUser5.setText("");
        FieldInputUser2.setText("");
        FieldInputUser3.setText("");
          loadDataUser();
          autoNumberUser();
        }
        if(question == JOptionPane.CANCEL_OPTION){
            
        }
    }//GEN-LAST:event_ButtonInputUserHapusActionPerformed

    private void ButtonInputUserBatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInputUserBatalActionPerformed
        // TODO add your handling code here:
        FieldInputUser4.setText("");
          FieldInputUser5.setText("");
          FieldInputUser2.setText("");
          FieldInputUser3.setText("");
          autoNumberUser();
          ButtonInputUserSimpn.setEnabled(true);
    }//GEN-LAST:event_ButtonInputUserBatalActionPerformed

    private void KasirBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KasirBtnActionPerformed
        // TODO add your handling code here:
         Kasir kasir = new Kasir();
         kasir.setVisible(true);
         usernameKasir.setText(namaAdmin.getText());
    }//GEN-LAST:event_KasirBtnActionPerformed

    private void FieldInputUser6KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FieldInputUser6KeyTyped
        // TODO add your handling code here:
        String key = FieldInputUser6.getText();
        DefaultTableModel tabel = new DefaultTableModel();
        tabel.addColumn("Kode_User");
        tabel.addColumn("Username");
        tabel.addColumn("Password");
        tabel.addColumn("Role");
        tabel.addColumn("Alamat");
        tabel.addColumn("No_Hp");
          try{
            java.sql.Connection c = Koneksi.getKoneksi();
            java.sql.Statement s = c.createStatement();
            
            String sql = "SELECT * FROM user WHERE username LIKE '%" + key + "%' ORDER BY username";
            ResultSet r = s.executeQuery(sql);
           
            while(r.next()){
                tabel.addRow(new Object[]{
               r.getString(1),
               r.getString(2),
               r.getString(3),
               r.getString(4),
               r.getString(5),
               r.getString(6),
            }); 
            TabelInputUser1.setModel(tabel);
            }
            r.close();
            s.close();
        }catch(Exception e){
            System.out.println(e);
        }
          loadDataUser();
    }//GEN-LAST:event_FieldInputUser6KeyTyped

    private void cariTxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cariTxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cariTxActionPerformed


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
            new Admin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonCari;
    private javax.swing.JButton ButtonEdit;
    private javax.swing.JButton ButtonHapus;
    private javax.swing.JButton ButtonHapus1;
    private javax.swing.JButton ButtonInputUserBatal;
    private javax.swing.JButton ButtonInputUserCari;
    private javax.swing.JButton ButtonInputUserEdit;
    private javax.swing.JButton ButtonInputUserHapus;
    private javax.swing.JButton ButtonInputUserSimpn;
    private javax.swing.JButton ButtonSimpan;
    private javax.swing.JTextField FieldInputMenu1;
    private javax.swing.JTextField FieldInputMenu2;
    private javax.swing.JTextField FieldInputMenu4;
    private javax.swing.JTextField FieldInputMenu5;
    private javax.swing.JTextField FieldInputMenu6;
    private javax.swing.JTextField FieldInputUser1;
    private javax.swing.JTextField FieldInputUser2;
    private javax.swing.JTextField FieldInputUser3;
    private javax.swing.JTextField FieldInputUser4;
    private javax.swing.JTextField FieldInputUser5;
    private javax.swing.JTextField FieldInputUser6;
    private javax.swing.JPanel InputUser;
    private javax.swing.JButton KasirBtn;
    private javax.swing.JLabel LabelInputMenu1;
    private javax.swing.JLabel LabelInputMenu2;
    private javax.swing.JLabel LabelInputMenu5;
    private javax.swing.JLabel LabelInputMenu6;
    private javax.swing.JLabel LabelInputUser1;
    private javax.swing.JLabel LabelInputUser2;
    private javax.swing.JLabel LabelInputUser3;
    private javax.swing.JLabel LabelInputUser4;
    private javax.swing.JLabel LabelInputUser5;
    private javax.swing.JLabel LabelInputUser6;
    private javax.swing.JLabel LbFoto_Makanan;
    private javax.swing.JPanel PanelInputMenu1;
    private javax.swing.JPanel PanelInputMenu2;
    private javax.swing.JPanel PanelInputUser1;
    private javax.swing.JPanel PanelInputUser2;
    private javax.swing.JScrollPane TabelInputMenu;
    private javax.swing.JScrollPane TabelInputUser;
    private javax.swing.JTable TabelInputUser1;
    private javax.swing.JPanel barcodeNav;
    private javax.swing.JLabel barcodeNavLabel;
    private javax.swing.JTable barcodeTabel;
    private javax.swing.JTextField btnCariDashboard;
    private java.awt.Button cariBtnBarcode;
    private javax.swing.JTextField cariTx;
    private javax.swing.JPanel cetakBarcode;
    private javax.swing.JButton cetakBarcodeBtn;
    private javax.swing.JPanel ceteakBarccodePanel;
    private chart.CurveLineChart chart;
    private javax.swing.JComboBox<String> cmbBoxUser;
    private javax.swing.JPanel dashboard;
    private javax.swing.JLabel dashboardLabel;
    private javax.swing.JPanel dashboardNav;
    private javax.swing.JLabel diskonBarcode;
    private javax.swing.JPanel grafikPanel;
    private java.awt.Button hapusBtnBarcode;
    private javax.swing.JPanel histori;
    private javax.swing.JButton historiBtn;
    private javax.swing.JLabel historiLabel;
    private javax.swing.JPanel historiNav;
    private javax.swing.JPanel historiPanel;
    private javax.swing.JScrollPane historiScrollPane;
    private javax.swing.JButton homeBtn;
    private javax.swing.JTextField inputDiskonBarcode;
    private javax.swing.JPanel inputMenu;
    private javax.swing.JButton inputMenuBtn;
    private javax.swing.JLabel inputRole;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPaneBarcode;
    private javax.swing.JTable jTable2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JButton karyawanBtn;
    private javax.swing.JButton logOutBtn;
    private javax.swing.JPanel mainPanel;
    public static final javax.swing.JLabel namaAdmin = new javax.swing.JLabel();
    private baksopuas.roundedJpanelShadow pemasukan;
    private javax.swing.JLabel pemasukanTx;
    private baksopuas.roundedJpanelShadow pengeluaran;
    private javax.swing.JLabel pengeluaranTx;
    private baksopuas.roundedJpanelShadow profit;
    private javax.swing.JLabel profitTx;
    private java.awt.Button saveBtnBarcode;
    private javax.swing.JTextField searchInputBarcode;
    private javax.swing.JPanel sideBar;
    private javax.swing.JPanel statistikPanel;
    private javax.swing.JTable tabelHistori;
    private javax.swing.JTable tabelMenu;
    private javax.swing.JButton uploadBtnMenu;
    // End of variables declaration//GEN-END:variables
}
