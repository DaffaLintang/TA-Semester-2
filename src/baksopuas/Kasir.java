/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package baksopuas;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.mysql.jdbc.Blob;
import com.mysql.jdbc.PreparedStatement;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

/**
 *
 * @author Daffa Lintang
 */
public class Kasir extends javax.swing.JFrame {

    /**
     * Creates new form Kasir
     */
    public void loadMenu(){
        try{
        Connection c = Koneksi.getKoneksi();
        java.sql.Statement s = c.createStatement();
        
        String sql = "SELECT * FROM menu";
        ResultSet r = s.executeQuery(sql);
        
        while(r.next()){ 
                JPanel container = new JPanel();
            
                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new BorderLayout());
                // Buat border dengan padding dan garis hitam
                Border compoundBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(50, 15, 5, 15),
                BorderFactory.createLineBorder(Color.black)
                );
                itemPanel.setBorder(compoundBorder);
                // Menambahkan gambar (jika ada)
               Blob imageBlob = (Blob) r.getBlob("foto_makanan");
               byte[] imageBytes = imageBlob.getBytes(1, (int) imageBlob.length());
               Image image = Toolkit.getDefaultToolkit().createImage(imageBytes);
               Image resizedImage = image.getScaledInstance(150, 100, Image.SCALE_SMOOTH);

               ImageIcon imageIcon = new ImageIcon(resizedImage);
               JLabel labelGambar = new JLabel(imageIcon);
               itemPanel.add(labelGambar, BorderLayout.NORTH);

                // Menambahkan nama makanan
                JLabel labelNama = new JLabel(r.getString("nama_makanan"));
                labelNama.setHorizontalAlignment(SwingConstants.CENTER);
                itemPanel.add(labelNama, BorderLayout.CENTER);
                
                // Menambahkan harga
                Locale rupiah = new Locale("id", "ID");
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(rupiah);
            String harga = formatRupiah.format(r.getInt("harga_jual"));
                JLabel labelHarga = new JLabel(harga);
                labelHarga.setHorizontalAlignment(SwingConstants.CENTER);
                itemPanel.add(labelHarga, BorderLayout.SOUTH);
                
                container.add(itemPanel);
                  

                // Tambahkan panel item ke panel menuBtn
                menuContainer.add(itemPanel);
    
        }
 
        r.close();
        s.close();
    }catch(Exception e){
        System.out.println(e);
    }
    }
    
Map<String, Integer> dataSebelumnya = new HashMap<>();

public void addData() {
  // Pindahkan kode ini keluar dari loop for
  DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

  for (Component component : menuContainer.getComponents()) {
    if (component instanceof JPanel) {
      JPanel itemPanel = (JPanel) component;
      // Menambahkan event listener
      itemPanel.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          // Menambahkan data ke tabel
          String namaMakanan = ((JLabel) itemPanel.getComponent(1)).getText();
          String harga = ((JLabel) itemPanel.getComponent(2)).getText().trim();
            String cleanedInput = harga.replaceAll("\\,[\\d]*$", "").replace(".", "");
       cleanedInput = cleanedInput.replaceAll("^Rp", "");
          int intHargaMakanan = Integer.parseInt(cleanedInput);

          // Menghitung jumlah
          int jumlah;
          if (dataSebelumnya.containsKey(namaMakanan)) {
            jumlah = dataSebelumnya.get(namaMakanan) + 1;
          } else {
            jumlah = 1;
          }
          dataSebelumnya.put(namaMakanan, jumlah);

          // Update tabel
          for (int i = model.getRowCount() - 1; i >= 0; i--) {
            if (model.getValueAt(i, 0).equals(namaMakanan)) {
              model.removeRow(i);
            }
          }
          model.addRow(new Object[]{namaMakanan, jumlah, intHargaMakanan});
          totalBiaya();
        }
      });
    }
  }
}

public void totalBiaya() {
  int jumlahBaris = jTable1.getRowCount();
  int totalBiaya = 0;
  int jumlahBarang, hargaJual;
  for (int i = 0; i < jumlahBaris; i++) {
    jumlahBarang = Integer.parseInt(jTable1.getValueAt(i, 1).toString());
    hargaJual = Integer.parseInt(jTable1.getValueAt(i, 2).toString());
    totalBiaya = totalBiaya + (jumlahBarang * hargaJual);
  }
   Locale rupiah = new Locale("id", "ID");
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(rupiah);
            String harga = formatRupiah.format(totalBiaya);
  total.setText(harga);
}
  
    public void getDate(){
         Date today = new Date();
         SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
         String formattedDate = formatter.format(today);
         tanggal.setText(formattedDate);
    }

    public Kasir() {
        initComponents();
        loadMenu();
        getDate();
        addData();
setExtendedState(JFrame.MAXIMIZED_BOTH);
            jButton3.setEnabled(false);

   
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        roundedJpanel1 = new baksopuas.roundedJpanelShadow();
        panelRound3 = new test.PanelRound();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        menuContainer = new javax.swing.JPanel();
        roundedJpanel2 = new baksopuas.roundedJpanelShadow();
        panelRound4 = new test.PanelRound();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tanggal = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        diskonTx = new javax.swing.JTextField();
        total = new javax.swing.JTextField();
        bayarTx = new javax.swing.JTextField();
        kembalian = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 222, 197));

        roundedJpanel1.setBackground(new java.awt.Color(255, 222, 195));
        roundedJpanel1.setPreferredSize(new java.awt.Dimension(519, 36));

        panelRound3.setBackground(new java.awt.Color(255, 162, 92));
        panelRound3.setRoundTopLeft(20);
        panelRound3.setRoundTopRight(20);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("MENU");

        usernameKasir.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        usernameKasir.setText("Nama");

        javax.swing.GroupLayout panelRound3Layout = new javax.swing.GroupLayout(panelRound3);
        panelRound3.setLayout(panelRound3Layout);
        panelRound3Layout.setHorizontalGroup(
            panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(usernameKasir)
                .addGap(35, 35, 35))
        );
        panelRound3Layout.setVerticalGroup(
            panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                    .addComponent(usernameKasir))
                .addContainerGap())
        );

        jScrollPane2.setBackground(new java.awt.Color(255, 222, 195));
        jScrollPane2.setBorder(null);
        jScrollPane2.setForeground(new java.awt.Color(255, 222, 195));
        jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        menuContainer.setBackground(new java.awt.Color(255, 222, 195));
        menuContainer.setForeground(new java.awt.Color(255, 222, 195));
        menuContainer.setLayout(new java.awt.GridLayout(10, 3));
        jScrollPane2.setViewportView(menuContainer);

        javax.swing.GroupLayout roundedJpanel1Layout = new javax.swing.GroupLayout(roundedJpanel1);
        roundedJpanel1.setLayout(roundedJpanel1Layout);
        roundedJpanel1Layout.setHorizontalGroup(
            roundedJpanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedJpanel1Layout.createSequentialGroup()
                .addComponent(panelRound3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(11, 11, 11))
            .addGroup(roundedJpanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
                .addGap(21, 21, 21))
        );
        roundedJpanel1Layout.setVerticalGroup(
            roundedJpanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedJpanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(panelRound3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 412, Short.MAX_VALUE)
                .addGap(18, 18, 18))
        );

        roundedJpanel2.setBackground(new java.awt.Color(255, 222, 195));
        roundedJpanel2.setPreferredSize(new java.awt.Dimension(519, 36));

        panelRound4.setBackground(new java.awt.Color(255, 162, 92));
        panelRound4.setRoundTopLeft(20);
        panelRound4.setRoundTopRight(20);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("STRUK");

        javax.swing.GroupLayout panelRound4Layout = new javax.swing.GroupLayout(panelRound4);
        panelRound4.setLayout(panelRound4Layout);
        panelRound4Layout.setHorizontalGroup(
            panelRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelRound4Layout.createSequentialGroup()
                .addContainerGap(125, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(113, 113, 113))
        );
        panelRound4Layout.setVerticalGroup(
            panelRound4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setForeground(new java.awt.Color(80, 80, 80));
        jLabel3.setText("Tanggal :");

        tanggal.setForeground(new java.awt.Color(80, 80, 80));
        tanggal.setText("20/03/2024");

        jTable1.setForeground(new java.awt.Color(80, 80, 80));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nama Makanan", "Jumlah", "Harga",
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setGridColor(new java.awt.Color(255, 255, 255));
        jTable1.setSelectionBackground(new java.awt.Color(255, 255, 255));
        jTable1.setSelectionForeground(new java.awt.Color(80, 80, 80));
        jTable1.setShowGrid(false);
        jScrollPane1.setViewportView(jTable1);

        jLabel5.setForeground(new java.awt.Color(80, 80, 80));
        jLabel5.setText("Scan Barcode");

        jLabel6.setForeground(new java.awt.Color(80, 80, 80));
        jLabel6.setText("Total");

        jLabel7.setForeground(new java.awt.Color(80, 80, 80));
        jLabel7.setText("Bayar");

        jLabel8.setForeground(new java.awt.Color(80, 80, 80));
        jLabel8.setText("Kembalian");

        diskonTx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diskonTxActionPerformed(evt);
            }
        });

        total.setEditable(false);
        total.setForeground(new java.awt.Color(153, 153, 153));

        bayarTx.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bayarTxActionPerformed(evt);
            }
        });
        bayarTx.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                bayarTxKeyTyped(evt);
            }
        });

        kembalian.setEditable(false);
        kembalian.setForeground(new java.awt.Color(153, 153, 153));

        jButton1.setForeground(new java.awt.Color(80, 80, 80));
        jButton1.setText("Bayar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setForeground(new java.awt.Color(80, 80, 80));
        jButton2.setText("Hapus");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setForeground(new java.awt.Color(80, 80, 80));
        jButton3.setText("Cetak");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout roundedJpanel2Layout = new javax.swing.GroupLayout(roundedJpanel2);
        roundedJpanel2.setLayout(roundedJpanel2Layout);
        roundedJpanel2Layout.setHorizontalGroup(
            roundedJpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedJpanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundedJpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(roundedJpanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(roundedJpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, roundedJpanel2Layout.createSequentialGroup()
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, roundedJpanel2Layout.createSequentialGroup()
                            .addComponent(jLabel5)
                            .addGap(18, 18, 18)
                            .addComponent(diskonTx))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, roundedJpanel2Layout.createSequentialGroup()
                            .addGroup(roundedJpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel6)
                                .addComponent(jLabel7)
                                .addComponent(jLabel8))
                            .addGap(34, 34, 34)
                            .addGroup(roundedJpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(kembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(bayarTx, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(roundedJpanel2Layout.createSequentialGroup()
                .addComponent(panelRound4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        roundedJpanel2Layout.setVerticalGroup(
            roundedJpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedJpanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(panelRound4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(roundedJpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(tanggal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(roundedJpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(diskonTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(roundedJpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(roundedJpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(bayarTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(roundedJpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(kembalian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(roundedJpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(26, 26, 26))
        );

        jButton4.setText("logout");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(roundedJpanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(roundedJpanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addGap(19, 19, 19))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(roundedJpanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                    .addComponent(roundedJpanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addContainerGap())
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
         if(bayarTx.getText().length() > 0){
                  Date today = new Date();
            SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
            String formattedDate1 = formatter1.format(today);
        String bayar = bayarTx.getText().toString();
        int intBayar = Integer.parseInt(bayar);
         String cleanedInput = total.getText().replaceAll("\\,[\\d]*$", "").replace(".", "");
       cleanedInput = cleanedInput.replaceAll("^Rp", "");
        String totalBayar = cleanedInput;
        int intTotalbayar = Integer.parseInt(totalBayar);
        int totalKembalian =  intBayar - intTotalbayar; 
        if(totalKembalian < 0){
            JOptionPane.showMessageDialog(null, "Uang Pembayaran Kurang");
        }else{
            Locale rupiah = new Locale("id", "ID");
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(rupiah);
            String harga = formatRupiah.format(totalKembalian);
            kembalian.setText(harga);
            try {
                 Connection c = Koneksi.getKoneksi();
                 String kembalianInt = kembalian.getText().replaceAll("[^\\d.-]", "");
                 String totalnInt = total.getText().replaceAll("[^\\d.-]", "");
                String sql = "INSERT INTO transaksi( total, kembalian, tanggal) VALUES (?,?,?)";
                java.sql.PreparedStatement p = c.prepareStatement(sql);;
                p.setString(1, String.valueOf(totalnInt));
                p.setString(2, String.valueOf(kembalianInt));
                p.setString(3, String.valueOf(formattedDate1));
                p.executeUpdate();
                p.close();
                
             } catch(Exception ex) {
                 System.out.println(ex);
             }
            jButton3.setEnabled(true);
        }
             
         }else {
              JOptionPane.showMessageDialog(null, "Masukkan jumlah yang dibayar");
         }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
         DefaultTableModel model = (DefaultTableModel)jTable1.getModel();
         if( jTable1.getSelectedRow() != -1){
              int row = jTable1.getSelectedRow();
              model.removeRow(row);
              totalBiaya(); 
              dataSebelumnya.clear();
         }else {
              JOptionPane.showMessageDialog(null, "Pilih menu yanng akan dihapus");
         }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
      
       int kodeTransaksi = 0;
       DefaultTableModel model = (DefaultTableModel)jTable1.getModel();
        if(bayarTx.getText().length() > 0){
             try {
                 try{
                     Connection c = Koneksi.getKoneksi();
                      java.sql.Statement s = c.createStatement();
                      String sql3 = "SELECT kode_transaksi FROM transaksi ORDER BY kode_transaksi DESC LIMIT 1";
                      ResultSet r = s.executeQuery(sql3);
                      while(r.next()){
                         kodeTransaksi = r.getInt("kode_transaksi");
                          
                      }
                 }catch(Exception e){
                     System.out.println(e);
                 }
                 int totalRows = jTable1.getRowCount(); // Jumlah baris data
                 int headerHeight = 100; // Tinggi header
                 int footerHeight = 50; // Tinggi footer
                 int rowHeight = 20; // Tinggi setiap baris data
                 int tableBorder = 20; // Jarak antara tabel dan tepi halaman
                 int contentHeight = totalRows * rowHeight; // Tinggi konten data
                 int totalHeight = headerHeight + footerHeight + contentHeight + tableBorder; // Total tinggi halaman
            PDDocument document  = new PDDocument();
            PDPage page = new PDPage(new PDRectangle(350, totalHeight+100));
            Date today = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String formattedDate = formatter.format(today);
            document.addPage(page);
            int startX = 20;
            int startY = totalHeight-100;
            int startY1 = startY+60;
            int tableWidth = 350;
            int colWidth = tableWidth / 3;
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            PDType0Font font = PDType0Font.load(document, new File("C:\\Users\\Daffa Lintang\\Downloads\\font\\Helvetica.ttf"));
            
            contentStream.setFont(font, 20);
            contentStream.beginText();
            contentStream.newLineAtOffset(20, startY+180);
            contentStream.showText("                    Bakso Puas");
            contentStream.endText();
            
             contentStream.setNonStrokingColor(0,0,0);
             contentStream.setFont(font, 16);
             contentStream.beginText();
             contentStream.newLineAtOffset(20, startY+160);
             contentStream.showText("--------------------------------------------------------");
             contentStream.newLine();
             contentStream.endText();
             
             contentStream.setNonStrokingColor(0,0,0);
             contentStream.setFont(font, 16);
             contentStream.beginText();
             contentStream.newLineAtOffset(20, startY+140);
             contentStream.showText("Kode Transaksi : "+kodeTransaksi);
             contentStream.newLine();
             contentStream.endText();
             
             contentStream.setNonStrokingColor(0,0,0);
             contentStream.setFont(font, 16);
             contentStream.beginText();
             contentStream.newLineAtOffset(20, startY+120);
             contentStream.showText("Tanggal : "+formattedDate);
             contentStream.newLine();
             contentStream.endText();
             
             contentStream.setNonStrokingColor(0,0,0);
             contentStream.setFont(font, 16);
             contentStream.beginText();
             contentStream.newLineAtOffset(20, startY+100);
             contentStream.showText("Kasir : "+usernameKasir.getText());
             contentStream.newLine();
             contentStream.endText();
            
             contentStream.setNonStrokingColor(0,0,0);
             contentStream.setFont(font, 16);
             contentStream.beginText();
             contentStream.newLineAtOffset(20, startY+80);
             contentStream.showText("--------------------------------------------------------");
             contentStream.newLine();
             contentStream.endText();
            
            contentStream.setNonStrokingColor(0,0,0);
            contentStream.setFont(font, 16);
            contentStream.beginText();
            contentStream.newLineAtOffset(startX, startY1);
            contentStream.showText("Nama");
            contentStream.newLineAtOffset(colWidth, 0);
            contentStream.showText("Jumlah");
            contentStream.newLineAtOffset(colWidth, 0);
            contentStream.showText("Harga");
            contentStream.newLineAtOffset(colWidth, 0);  
            contentStream.endText();
            contentStream.setNonStrokingColor(0,0,0);
            contentStream.setFont(font, 12);
            for(int i = 0; i < jTable1.getRowCount(); i++){
                contentStream.beginText();
                contentStream.newLineAtOffset(startX, startY1 -(i + 1)* rowHeight);
                contentStream.showText(jTable1.getValueAt(i, 0).toString());
                contentStream.newLineAtOffset(colWidth, 0);
                contentStream.showText(jTable1.getValueAt(i, 1).toString());
                contentStream.newLineAtOffset(colWidth, 0);
                contentStream.showText(jTable1.getValueAt(i, 2).toString());
                contentStream.newLineAtOffset(colWidth, 0);
                contentStream.endText();
                
                try {
                     Connection c = Koneksi.getKoneksi();
                      String sql2 = "SELECT kode_makanan FROM menu WHERE nama_makanan = ?";
                      java.sql.PreparedStatement ps = c.prepareStatement(sql2);
                        String jumlah = jTable1.getValueAt(i, 1).toString();
                        for(int x =0; x < Integer.parseInt(jumlah); x++){
                            ps.setString(1, jTable1.getValueAt(i, 0).toString());
                            ResultSet r = ps.executeQuery();
                        while(r.next()){
                            
                            try{
                               String sql1 = "INSERT INTO detail_transaksi(kode_transaksi, kode_makanan) VALUES (?,?)";
                               java.sql.PreparedStatement p1 = c.prepareStatement(sql1);
                               p1.setString(1, String.valueOf(kodeTransaksi));
                               p1.setString(2, String.valueOf(r.getInt("kode_makanan")));
                               p1.executeUpdate();
                               p1.close(); 
                            }catch (Exception e){
                                System.out.println(e);   
                            }
                        }
                        
                    }
                }catch(Exception e){
                    System.out.println(e);
                }
                
            }
            
             contentStream.setNonStrokingColor(0,0,0);
             contentStream.setFont(font, 16);
             contentStream.beginText();
             contentStream.newLineAtOffset(startX, startY1 - (jTable1.getRowCount() +1)  * rowHeight);
             contentStream.showText("--------------------------------------------------------");
             contentStream.newLine();
             contentStream.endText();
             
             contentStream.setNonStrokingColor(0,0,0);
             contentStream.setFont(font, 16);
             contentStream.beginText();
             contentStream.newLineAtOffset(startX, startY1 - (jTable1.getRowCount() + 2)  * rowHeight);
             contentStream.showText("Total : " + total.getText());
             contentStream.newLine();
             contentStream.endText();
             
            Locale rupiah = new Locale("id", "ID");
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(rupiah);
            String bayar = formatRupiah.format(Integer.parseInt(bayarTx.getText()));
             contentStream.setNonStrokingColor(0,0,0);
             contentStream.setFont(font, 16);
             contentStream.beginText();
             contentStream.newLineAtOffset(startX, startY1 - (jTable1.getRowCount() + 3)  * rowHeight);
             contentStream.showText("Bayar : Rp " + bayar);
             contentStream.newLine();
             contentStream.endText();
             
             contentStream.setNonStrokingColor(0,0,0);
             contentStream.setFont(font, 16);
             contentStream.beginText();
             contentStream.newLineAtOffset(startX, startY1 - (jTable1.getRowCount() + 4)  * rowHeight);
             contentStream.showText("Kembalian :" + kembalian.getText());
             contentStream.newLine();
             contentStream.endText();
             
             contentStream.close();
             
             document.save("C:\\daffa\\Struk.pdf");

             document.close();
             FileDialog fileDialog = new FileDialog((Frame) null, "Open File", FileDialog.LOAD);
             fileDialog.setDirectory("C:\\daffa");
             fileDialog.setFile("Struk.pdf");
             JOptionPane.showMessageDialog(null, "Struk Berhasil Di Cetak");
             bayarTx.setText("");
             total.setText("");
             kembalian.setText("");
        } catch (IOException ex) {
            System.out.println(ex);
        }
               while(model.getRowCount() > 0){
             model.removeRow(0);
             dataSebelumnya.clear();
        }
        } else {
            JOptionPane.showMessageDialog(null, "Bayar Terlebih Dahulu");
            jButton3.setEnabled(false);
        }
      

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
         Login x = new Login();
        x.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void diskonTxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diskonTxActionPerformed
        // TODO add your handling code here:
       double diskon = Double.parseDouble(diskonTx.getText());
       
       String bayar = total.getText();
       String cleanedInput = bayar.replaceAll("\\,[\\d]*$", "");
       cleanedInput = cleanedInput.replaceAll("^Rp", "").replace(".", "");
        System.out.println(cleanedInput);
       double cleanedInput1 = Double.parseDouble(cleanedInput);
       double totalBayar = cleanedInput1;
        System.out.println(bayar);
        System.out.println(totalBayar);
       double totalDiskon = totalBayar * diskon;
       double diskonbayar = totalBayar - totalDiskon;
       int diskonBayar1 = (int) diskonbayar;
       Locale rupiah = new Locale("id", "ID");
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(rupiah);
            String harga = formatRupiah.format(diskonBayar1);
       total.setText(harga);
    }//GEN-LAST:event_diskonTxActionPerformed

    private void bayarTxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bayarTxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bayarTxActionPerformed

    private void bayarTxKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bayarTxKeyTyped
        // TODO add your handling code here:
//        Locale rupiah = new Locale("id", "ID");
//        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(rupiah);
//        try {
//    // Parse the text from bayarTx to a number
//    String bayarText = bayarTx.getText();
//    double bayarValue = Double.parseDouble(bayarText);
//
//    // Format the number to currency format
//    String harga = formatRupiah.format(bayarValue);
//
//    // Set the formatted currency string back to bayarTx
//    bayarTx.setText(harga);
//} catch (NumberFormatException e) {
//    // Handle the case where the text is not a valid number
//            System.out.println(e);
//}
    }//GEN-LAST:event_bayarTxKeyTyped

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Kasir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Kasir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Kasir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Kasir.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Kasir().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField bayarTx;
    private javax.swing.JTextField diskonTx;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JTable jTable1;
    private javax.swing.JTextField kembalian;
    private javax.swing.JPanel menuContainer;
    private test.PanelRound panelRound3;
    private test.PanelRound panelRound4;
    private baksopuas.roundedJpanelShadow roundedJpanel1;
    private baksopuas.roundedJpanelShadow roundedJpanel2;
    private javax.swing.JLabel tanggal;
    private javax.swing.JTextField total;
    public static final javax.swing.JLabel usernameKasir = new javax.swing.JLabel();
    // End of variables declaration//GEN-END:variables
}
