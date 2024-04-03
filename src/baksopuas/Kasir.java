/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package baksopuas;

import com.mysql.jdbc.Blob;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Daffa Lintang
 */
public class Kasir extends javax.swing.JFrame {

    /**
     * Creates new form Kasir
     */
    private DefaultTableModel model;
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
               ImageIcon imageIcon = new ImageIcon(image);
               JLabel labelGambar = new JLabel(imageIcon);
               itemPanel.add(labelGambar, BorderLayout.NORTH);

                // Menambahkan nama makanan
                JLabel labelNama = new JLabel(r.getString("nama_makanan"));
                labelNama.setHorizontalAlignment(SwingConstants.CENTER);
                itemPanel.add(labelNama, BorderLayout.CENTER);
                
                // Menambahkan harga
                JLabel labelHarga = new JLabel("Rp " + r.getInt("harga_jual"));
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
          String hargaMakanan = harga.replace("Rp ", "");
          int intHargaMakanan = Integer.parseInt(hargaMakanan);

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
  total.setText("Rp "+ totalBiaya+ ",00");
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
        jTextField1 = new javax.swing.JTextField();
        total = new javax.swing.JTextField();
        bayarTx = new javax.swing.JTextField();
        kembalian = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 222, 197));

        roundedJpanel1.setBackground(new java.awt.Color(255, 222, 195));
        roundedJpanel1.setPreferredSize(new java.awt.Dimension(519, 36));

        panelRound3.setBackground(new java.awt.Color(255, 162, 92));
        panelRound3.setRoundTopLeft(20);
        panelRound3.setRoundTopRight(20);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("MENU");

        javax.swing.GroupLayout panelRound3Layout = new javax.swing.GroupLayout(panelRound3);
        panelRound3.setLayout(panelRound3Layout);
        panelRound3Layout.setHorizontalGroup(
            panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addContainerGap(420, Short.MAX_VALUE))
        );
        panelRound3Layout.setVerticalGroup(
            panelRound3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRound3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                "Nama Makanan", "Jumlah", "Harga"
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

        total.setEditable(false);
        total.setForeground(new java.awt.Color(153, 153, 153));

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

        javax.swing.GroupLayout roundedJpanel2Layout = new javax.swing.GroupLayout(roundedJpanel2);
        roundedJpanel2.setLayout(roundedJpanel2Layout);
        roundedJpanel2Layout.setHorizontalGroup(
            roundedJpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(roundedJpanel2Layout.createSequentialGroup()
                .addComponent(panelRound4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(11, 11, 11))
            .addGroup(roundedJpanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(roundedJpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(roundedJpanel2Layout.createSequentialGroup()
                        .addGap(89, 89, 89)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(roundedJpanel2Layout.createSequentialGroup()
                        .addGroup(roundedJpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(roundedJpanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tanggal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(roundedJpanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(roundedJpanel2Layout.createSequentialGroup()
                                .addGroup(roundedJpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel8))
                                .addGap(34, 34, 34)
                                .addGroup(roundedJpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(kembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(bayarTx, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(20, Short.MAX_VALUE))
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(roundedJpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(jButton2))
                .addGap(26, 26, 26))
        );

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
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(roundedJpanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE)
                    .addComponent(roundedJpanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE))
                .addGap(15, 15, 15))
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
        String bayar = bayarTx.getText().toString();
        int intBayar = Integer.parseInt(bayar);
        String totalBayar = total.getText().replace("Rp ", "").replace(",00", "");
        int intTotalbayar = Integer.parseInt(totalBayar);
        int totalKembalian =  intBayar - intTotalbayar; 
        if(totalKembalian < 0){
            JOptionPane.showMessageDialog(null, "Uang Pembayaran Kurang");
        }else{
            kembalian.setText("Rp "+String.valueOf(totalKembalian));
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
         DefaultTableModel model = (DefaultTableModel)jTable1.getModel();
        int row = jTable1.getSelectedRow();
        model.removeRow(row);
        total.setText("0");
       
    }//GEN-LAST:event_jButton2ActionPerformed

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
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
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
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField kembalian;
    private javax.swing.JPanel menuContainer;
    private test.PanelRound panelRound3;
    private test.PanelRound panelRound4;
    private baksopuas.roundedJpanelShadow roundedJpanel1;
    private baksopuas.roundedJpanelShadow roundedJpanel2;
    private javax.swing.JLabel tanggal;
    private javax.swing.JTextField total;
    // End of variables declaration//GEN-END:variables
}
