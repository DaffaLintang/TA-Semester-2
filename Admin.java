/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package baksopuas;

import chart.ModelChart;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import java.awt.Color;
import java.security.Timestamp;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Date;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Daffa Lintang
 */
public class Admin extends javax.swing.JFrame {
   private DefaultTableModel modelHistori;
    /**
     * Creates new form Admin
     */
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
        String sql = "SELECT SUM(menu.harga_beli) AS total_pengeluaran, SUM(menu.harga_jual) AS total_pendapatan, SUM(menu.harga_jual) - SUM(menu.harga_beli) AS profit, DATE_FORMAT(tanggal,'%d-%M-%Y') as `Date` FROM detail_transaksi  JOIN menu ON detail_transaksi.kode_makanan = menu.kode_makanan JOIN transaksi ON detail_transaksi.kode_transaksi = transaksi.kode_transaksi group by DATE_FORMAT(tanggal,'%d%M%Y') order by tanggal DESC limit 7;";
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
        initcomponent();
        try {
            Connection conn = (Connection) Koneksi.getKoneksi();
            Statement stmt = (Statement) conn.createStatement();
            String sql = "SELECT ROW_NUMBER() OVER (ORDER BY detail_transaksi.kode_transaksi) AS nomor, detail_transaksi.kode_transaksi, menu.nama_makanan, COUNT(menu.nama_makanan) AS jumlah, menu.harga_beli, menu.harga_jual, transaksi.tanggal FROM detail_transaksi INNER JOIN transaksi ON detail_transaksi.kode_transaksi = transaksi.kode_transaksi INNER JOIN menu ON detail_transaksi.kode_makanan = menu.kode_makanan GROUP BY detail_transaksi.kode_transaksi, menu.nama_makanan, menu.harga_beli, menu.harga_jual, transaksi.tanggal";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int no = rs.getInt("no");
                int kode_transaksi = rs.getInt("kode_transaksi");
                String nama_makanan = rs.getString("nama_makanan");
                int jumlah = rs.getInt("jumlah");
                int harga_beli = rs.getInt("harga_beli");
                int harga_jual = rs.getInt("harga_jual");
                Timestamp datetime = resultSet.getTimestamp("tanggal");
                
                // Lakukan sesuatu dengan data yang diperoleh, misalnya tampilkan di konsol
                System.out.println("Nomor: " + no + ", Kode Transaksi: " + kode_transaksi + ", Nama Makanan: " + nama_makanan + ", Jumlah: " + jumlah + ", Harga Beli: " + harga_beli + ", Harga Jual: " + harga_jual + ", Tanggal: " + datetime);
        }

            // Jangan lupa untuk menutup koneksi dan objek statement serta result set setelah selesai
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }    
    }
    public Admin() {
        initComponents();
        chart.setTitle("Grafik Penjualan");
        chart.addLegend("Pengeluaran", Color.decode("#7b4397"), Color.decode("#dc2430"));
        chart.addLegend("Pendapatan", Color.decode("#e65c00"), Color.decode("#F9D423"));
        chart.addLegend("Profit", Color.decode("#0099F7"), Color.decode("#F11712"));
       hitungKeuntungan();
       chartData();
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
        jPanel1 = new javax.swing.JPanel();
        sideBar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        inputMenuBtn = new javax.swing.JButton();
        logOutBtn = new javax.swing.JButton();
        homeBtn = new javax.swing.JButton();
        historiBtn = new javax.swing.JButton();
        karyawanBtn = new javax.swing.JButton();
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
        inputMenu = new javax.swing.JPanel();
        PanelInputMenu1 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        PanelInputMenu2 = new javax.swing.JPanel();
        FieldInputMenu1 = new javax.swing.JTextField();
        LabelInputMenu1 = new javax.swing.JLabel();
        FieldInputMenu2 = new javax.swing.JTextField();
        LabelInputMenu2 = new javax.swing.JLabel();
        FieldInputMenu3 = new javax.swing.JTextField();
        LabelInputMenu3 = new javax.swing.JLabel();
        FieldInputMenu4 = new javax.swing.JTextField();
        LabelInputMenu5 = new javax.swing.JLabel();
        FieldInputMenu5 = new javax.swing.JTextField();
        LabelInputMenu6 = new javax.swing.JLabel();
        FieldInputMenu6 = new javax.swing.JTextField();
        ButtonCari = new javax.swing.JButton();
        ButtonSimpan = new javax.swing.JButton();
        ButtonEdit = new javax.swing.JButton();
        ButtonHapus = new javax.swing.JButton();
        TabelInputMenu = new javax.swing.JScrollPane();
        Tabel1 = new javax.swing.JTable();
        histori = new javax.swing.JPanel();
        historiNav = new javax.swing.JPanel();
        historiLabel = new javax.swing.JLabel();
        historiPanel = new javax.swing.JPanel();
        historiScrollPane = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        cariTx = new javax.swing.JTextField();
        btncari = new javax.swing.JButton();
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
        ButtonInputUserSimpan = new javax.swing.JButton();
        ButtonInputUserEdit = new javax.swing.JButton();
        ButtonInputUserHapus = new javax.swing.JButton();
        roleTx = new javax.swing.JTextField();
        inputRole = new javax.swing.JLabel();
        TabelInputUser = new javax.swing.JScrollPane();
        TabelInputUser1 = new javax.swing.JTable();
        cetakBarcode = new javax.swing.JPanel();
        ceteakBarccodePanel = new javax.swing.JPanel();
        inputDiskonBarcode = new javax.swing.JTextField();
        inputKodeBarcode = new javax.swing.JTextField();
        searchInputBarcode = new javax.swing.JTextField();
        saveBtnBarcode = new java.awt.Button();
        EditBtnBarcode = new java.awt.Button();
        hapusBtnBarcode = new java.awt.Button();
        cariBtnBarcode = new java.awt.Button();
        kodeBarcode = new javax.swing.JLabel();
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
        sideBar.add(inputMenuBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 120, 40));

        logOutBtn.setBackground(new java.awt.Color(124, 183, 89));
        logOutBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        logOutBtn.setText("Log Out");
        logOutBtn.setBorder(null);
        logOutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logOutBtnActionPerformed(evt);
            }
        });
        sideBar.add(logOutBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 120, 40));

        homeBtn.setBackground(new java.awt.Color(124, 183, 89));
        homeBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        homeBtn.setText("Dashboard");
        homeBtn.setBorder(null);
        homeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeBtnActionPerformed(evt);
            }
        });
        sideBar.add(homeBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 120, 40));

        historiBtn.setBackground(new java.awt.Color(124, 183, 89));
        historiBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        historiBtn.setText("Histori");
        historiBtn.setBorder(null);
        historiBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                historiBtnActionPerformed(evt);
            }
        });
        sideBar.add(historiBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 160, 120, 40));

        karyawanBtn.setBackground(new java.awt.Color(124, 183, 89));
        karyawanBtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        karyawanBtn.setText("Input User");
        karyawanBtn.setBorder(null);
        karyawanBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                karyawanBtnActionPerformed(evt);
            }
        });
        sideBar.add(karyawanBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, 120, 40));

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
                    .addComponent(profitTx, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE))
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
                    .addComponent(pengeluaranTx, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE)
                    .addGroup(pengeluaranLayout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 35, Short.MAX_VALUE)))
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
                        .addGap(0, 32, Short.MAX_VALUE)))
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

        javax.swing.GroupLayout grafikPanelLayout = new javax.swing.GroupLayout(grafikPanel);
        grafikPanel.setLayout(grafikPanelLayout);
        grafikPanelLayout.setHorizontalGroup(
            grafikPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grafikPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chart, javax.swing.GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE)
                .addContainerGap())
        );
        grafikPanelLayout.setVerticalGroup(
            grafikPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, grafikPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chart, javax.swing.GroupLayout.DEFAULT_SIZE, 252, Short.MAX_VALUE))
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

        FieldInputMenu3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FieldInputMenu3ActionPerformed(evt);
            }
        });

        LabelInputMenu3.setText("Foto Makanan");

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

        javax.swing.GroupLayout PanelInputMenu2Layout = new javax.swing.GroupLayout(PanelInputMenu2);
        PanelInputMenu2.setLayout(PanelInputMenu2Layout);
        PanelInputMenu2Layout.setHorizontalGroup(
            PanelInputMenu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInputMenu2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(PanelInputMenu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FieldInputMenu1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelInputMenu3)
                    .addComponent(FieldInputMenu3, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelInputMenu2)
                    .addComponent(FieldInputMenu2, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelInputMenu1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addContainerGap(221, Short.MAX_VALUE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(LabelInputMenu3)
                .addGap(4, 4, 4)
                .addGroup(PanelInputMenu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FieldInputMenu3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(FieldInputMenu6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ButtonCari))
                .addGap(30, 30, 30)
                .addGroup(PanelInputMenu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonSimpan)
                    .addComponent(ButtonEdit)
                    .addComponent(ButtonHapus))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        Tabel1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Kode Transaksi", "Nama Makanan", "Harga Beli", "Harga Jual", "Foto Makanan"
            }
        ));
        TabelInputMenu.setViewportView(Tabel1);

        javax.swing.GroupLayout inputMenuLayout = new javax.swing.GroupLayout(inputMenu);
        inputMenu.setLayout(inputMenuLayout);
        inputMenuLayout.setHorizontalGroup(
            inputMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelInputMenu1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(inputMenuLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(inputMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(PanelInputMenu2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(TabelInputMenu))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        inputMenuLayout.setVerticalGroup(
            inputMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(inputMenuLayout.createSequentialGroup()
                .addComponent(PanelInputMenu1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(PanelInputMenu2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(TabelInputMenu, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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

        jTable1.setForeground(new java.awt.Color(255, 255, 255));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No.", "Kode Transaksi", "Produk", "Jumlah", "Harga Jual", "Harga Beli", "Tanggal"
            }
        ));
        historiScrollPane.setViewportView(jTable1);

        cariTx.setForeground(new java.awt.Color(80, 80, 80));

        btncari.setText("Cari");
        btncari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncariActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout historiPanelLayout = new javax.swing.GroupLayout(historiPanel);
        historiPanel.setLayout(historiPanelLayout);
        historiPanelLayout.setHorizontalGroup(
            historiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historiPanelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(historiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(historiScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 645, Short.MAX_VALUE)
                    .addGroup(historiPanelLayout.createSequentialGroup()
                        .addComponent(cariTx, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btncari)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(28, 28, 28))
        );
        historiPanelLayout.setVerticalGroup(
            historiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(historiPanelLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(historiPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cariTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btncari))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(historiScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 328, Short.MAX_VALUE)
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

        ButtonInputUserCari.setText("Cari");
        ButtonInputUserCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonInputUserCariActionPerformed(evt);
            }
        });

        ButtonInputUserSimpan.setText("Simpan");
        ButtonInputUserSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonInputUserSimpanActionPerformed(evt);
            }
        });

        ButtonInputUserEdit.setText("Edit");

        ButtonInputUserHapus.setText("Hapus");

        roleTx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roleTxActionPerformed(evt);
            }
        });

        inputRole.setForeground(new java.awt.Color(80, 80, 80));
        inputRole.setText("Role");

        javax.swing.GroupLayout PanelInputUser2Layout = new javax.swing.GroupLayout(PanelInputUser2);
        PanelInputUser2.setLayout(PanelInputUser2Layout);
        PanelInputUser2Layout.setHorizontalGroup(
            PanelInputUser2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelInputUser2Layout.createSequentialGroup()
                .addGap(31, 31, 31)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(PanelInputUser2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(FieldInputUser3, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LabelInputUser4)
                    .addComponent(roleTx, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(inputRole))
                .addGap(25, 25, 25))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelInputUser2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ButtonInputUserSimpan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ButtonInputUserEdit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ButtonInputUserHapus)
                .addGap(35, 35, 35))
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
                            .addComponent(FieldInputUser5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(PanelInputUser2Layout.createSequentialGroup()
                        .addComponent(inputRole)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(roleTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(38, 38, 38)
                .addGroup(PanelInputUser2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(FieldInputUser6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ButtonInputUserCari))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PanelInputUser2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonInputUserSimpan)
                    .addComponent(ButtonInputUserEdit)
                    .addComponent(ButtonInputUserHapus))
                .addContainerGap(15, Short.MAX_VALUE))
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
        TabelInputUser.setViewportView(TabelInputUser1);

        javax.swing.GroupLayout InputUserLayout = new javax.swing.GroupLayout(InputUser);
        InputUser.setLayout(InputUserLayout);
        InputUserLayout.setHorizontalGroup(
            InputUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(PanelInputUser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(InputUserLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(InputUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TabelInputUser)
                    .addComponent(PanelInputUser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        InputUserLayout.setVerticalGroup(
            InputUserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InputUserLayout.createSequentialGroup()
                .addComponent(PanelInputUser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(PanelInputUser2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(TabelInputUser, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        mainPanel.add(InputUser, "card5");

        ceteakBarccodePanel.setBackground(new java.awt.Color(254, 239, 208));

        inputDiskonBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputDiskonBarcodeActionPerformed(evt);
            }
        });

        inputKodeBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inputKodeBarcodeActionPerformed(evt);
            }
        });

        searchInputBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchInputBarcodeActionPerformed(evt);
            }
        });

        saveBtnBarcode.setLabel("Simpan");
        saveBtnBarcode.setName("Simpan"); // NOI18N

        EditBtnBarcode.setLabel("Edit");

        hapusBtnBarcode.setLabel("Hapus");

        cariBtnBarcode.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        cariBtnBarcode.setLabel("Cari");
        cariBtnBarcode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cariBtnBarcodeActionPerformed(evt);
            }
        });

        kodeBarcode.setForeground(new java.awt.Color(80, 80, 80));
        kodeBarcode.setText("Kode Barcode");

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
                        .addComponent(inputKodeBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(searchInputBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cariBtnBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(ceteakBarccodePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(ceteakBarccodePanelLayout.createSequentialGroup()
                            .addComponent(saveBtnBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(EditBtnBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(hapusBtnBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(inputDiskonBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(kodeBarcode))
                .addContainerGap(207, Short.MAX_VALUE))
        );
        ceteakBarccodePanelLayout.setVerticalGroup(
            ceteakBarccodePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ceteakBarccodePanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(kodeBarcode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ceteakBarccodePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ceteakBarccodePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(inputKodeBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(searchInputBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(cariBtnBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(diskonBarcode)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(inputDiskonBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(ceteakBarccodePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saveBtnBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(EditBtnBarcode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(jScrollPaneBarcode, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
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
        chartData();
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

    private void FieldInputMenu3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FieldInputMenu3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FieldInputMenu3ActionPerformed

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
    }//GEN-LAST:event_ButtonSimpanActionPerformed

    private void ButtonEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonEditActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ButtonEditActionPerformed

    private void ButtonHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonHapusActionPerformed
        // TODO add your handling code here:
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

    private void ButtonInputUserSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonInputUserSimpanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ButtonInputUserSimpanActionPerformed

    private void searchInputBarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchInputBarcodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchInputBarcodeActionPerformed

    private void inputKodeBarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputKodeBarcodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputKodeBarcodeActionPerformed

    private void inputDiskonBarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inputDiskonBarcodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_inputDiskonBarcodeActionPerformed

    private void cariBtnBarcodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cariBtnBarcodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cariBtnBarcodeActionPerformed

    private void roleTxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roleTxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_roleTxActionPerformed

    private void btncariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncariActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btncariActionPerformed


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
    private javax.swing.JButton ButtonInputUserCari;
    private javax.swing.JButton ButtonInputUserEdit;
    private javax.swing.JButton ButtonInputUserHapus;
    private javax.swing.JButton ButtonInputUserSimpan;
    private javax.swing.JButton ButtonSimpan;
    private java.awt.Button EditBtnBarcode;
    private javax.swing.JTextField FieldInputMenu1;
    private javax.swing.JTextField FieldInputMenu2;
    private javax.swing.JTextField FieldInputMenu3;
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
    private javax.swing.JLabel LabelInputMenu1;
    private javax.swing.JLabel LabelInputMenu2;
    private javax.swing.JLabel LabelInputMenu3;
    private javax.swing.JLabel LabelInputMenu5;
    private javax.swing.JLabel LabelInputMenu6;
    private javax.swing.JLabel LabelInputUser1;
    private javax.swing.JLabel LabelInputUser2;
    private javax.swing.JLabel LabelInputUser3;
    private javax.swing.JLabel LabelInputUser4;
    private javax.swing.JLabel LabelInputUser5;
    private javax.swing.JLabel LabelInputUser6;
    private javax.swing.JPanel PanelInputMenu1;
    private javax.swing.JPanel PanelInputMenu2;
    private javax.swing.JPanel PanelInputUser1;
    private javax.swing.JPanel PanelInputUser2;
    private javax.swing.JTable Tabel1;
    private javax.swing.JScrollPane TabelInputMenu;
    private javax.swing.JScrollPane TabelInputUser;
    private javax.swing.JTable TabelInputUser1;
    private javax.swing.JPanel barcodeNav;
    private javax.swing.JLabel barcodeNavLabel;
    private javax.swing.JTable barcodeTabel;
    private javax.swing.JButton btncari;
    private java.awt.Button cariBtnBarcode;
    private javax.swing.JTextField cariTx;
    private javax.swing.JPanel cetakBarcode;
    private javax.swing.JPanel ceteakBarccodePanel;
    private chart.CurveLineChart chart;
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
    private javax.swing.JTextField inputKodeBarcode;
    private javax.swing.JPanel inputMenu;
    private javax.swing.JButton inputMenuBtn;
    private javax.swing.JLabel inputRole;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPaneBarcode;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JButton karyawanBtn;
    private javax.swing.JLabel kodeBarcode;
    private javax.swing.JButton logOutBtn;
    private javax.swing.JPanel mainPanel;
    private baksopuas.roundedJpanelShadow pemasukan;
    private javax.swing.JLabel pemasukanTx;
    private baksopuas.roundedJpanelShadow pengeluaran;
    private javax.swing.JLabel pengeluaranTx;
    private baksopuas.roundedJpanelShadow profit;
    private javax.swing.JLabel profitTx;
    private javax.swing.JTextField roleTx;
    private java.awt.Button saveBtnBarcode;
    private javax.swing.JTextField searchInputBarcode;
    private javax.swing.JPanel sideBar;
    private javax.swing.JPanel statistikPanel;
    // End of variables declaration//GEN-END:variables

    private void initcomponent() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
