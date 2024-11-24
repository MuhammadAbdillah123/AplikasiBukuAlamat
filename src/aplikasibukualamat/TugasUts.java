/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package aplikasibukualamat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.PrintWriter;
import java.sql.*;
/**
 *
 * @author DELL
 */
public class TugasUts extends javax.swing.JFrame {
    private Connection conn;
    private DefaultTableModel modelTabel;
    /**
     * Creates new form TugasUts
     */
    public TugasUts() {
        initComponents();
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
        new Object[][]{},
        new String[]{"ID", "Nama", "Alamat", "Telepon", "Email"}
    ));
        modelTabel = (DefaultTableModel) jTable1.getModel(); // Hubungkan model tabel dengan JTable
        connectDatabase(); // Koneksi ke database
        createTableIfNotExists(); // Pastikan tabel ada
        loadContacts(); // Muat data awal dari database

    }
    
    private void createTableIfNotExists() {
        try (Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS contacts (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "address TEXT NOT NULL, " +
                    "phone TEXT NOT NULL, " +
                    "email TEXT)";
            stmt.execute(sql);
            System.out.println("Tabel 'contacts' berhasil dibuat atau sudah ada.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal membuat tabel: " + e.getMessage());
        }
    }
    
    private void connectDatabase() {
        try {
            String url = "jdbc:sqlite:addressbook.db"; // Lokasi database
            conn = DriverManager.getConnection(url);
            System.out.println("Koneksi berhasil!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal terhubung ke database: " + e.getMessage());
        }
        if (conn != null) {
            System.out.println("Koneksi ke database berhasil!");
        } else {
            System.err.println("Koneksi ke database gagal!");
        }
    }
    
    private void loadContacts() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM contacts")) {

            modelTabel.setRowCount(0); // Kosongkan tabel sebelum memuat data baru
            while (rs.next()) {
                modelTabel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("email")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage());
        }
    }
    
   private void addContact() {
    if (!validateInput()) return;

    String name = jTextField1.getText();
    String address = jTextField2.getText();
    String phone = jTextField3.getText();
    String email = jTextField4.getText();

    try (PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO contacts (name, address, phone, email) VALUES (?, ?, ?, ?)")) {
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, phone);
        ps.setString(4, email);
        ps.executeUpdate();
        loadContacts();
        JOptionPane.showMessageDialog(this, "Kontak berhasil ditambahkan!");
        clearInputFields();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gagal menambahkan kontak: " + e.getMessage());
        e.printStackTrace();
    }
        jButton1.addActionListener(e -> {
            System.out.println("Tombol Tambah diklik"); // Debug
            addContact();
        });

    }
    
    private void editSingleField() {
    // Pastikan ada baris yang dipilih
    int selectedRow = jTable1.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Pilih kontak yang ingin diubah!");
        return;
    }

    // Ambil ID kontak dari baris yang dipilih
    int id = (int) modelTabel.getValueAt(selectedRow, 0);

    // Pilih kolom yang ingin diedit
    String[] fields = {"Nama", "Alamat", "Telepon", "Email"};
    String selectedField = (String) JOptionPane.showInputDialog(
            this,
            "Pilih field yang ingin diedit:",
            "Edit Field",
            JOptionPane.QUESTION_MESSAGE,
            null,
            fields,
            fields[0]
    );

    if (selectedField == null) {
        // Jika user membatalkan dialog
        return;
    }

    // Masukkan nilai baru untuk field yang dipilih
    String newValue = JOptionPane.showInputDialog(this, "Masukkan nilai baru untuk " + selectedField + ":");
    if (newValue == null || newValue.trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Nilai baru tidak boleh kosong!");
        return;
    }

    // Tentukan kolom database yang sesuai dengan field yang dipilih
    String column = null;
    switch (selectedField) {
        case "Nama":
            column = "name";
            break;
        case "Alamat":
            column = "address";
            break;
        case "Telepon":
            column = "phone";
            break;
        case "Email":
            column = "email";
            break;
    }

    // Update hanya kolom yang dipilih di database
    if (column != null) {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE contacts SET " + column + " = ? WHERE id = ?")) {
            ps.setString(1, newValue);
            ps.setInt(2, id);
            ps.executeUpdate();
            loadContacts(); // Perbarui tabel setelah update
            JOptionPane.showMessageDialog(this, selectedField + " berhasil diubah!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengubah " + selectedField + ": " + e.getMessage());
            e.printStackTrace(); // Debugging
        }
    }
}

    
     private void deleteContact() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih kontak yang ingin dihapus!");
            return;
        }

        int id = (int) modelTabel.getValueAt(selectedRow, 0);

        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM contacts WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
            loadContacts();
            JOptionPane.showMessageDialog(this, "Kontak berhasil dihapus!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus kontak: " + e.getMessage());
        }
    }
     
     private boolean validateInput() {
        if (jTextField1.getText().isEmpty() || jTextField2.getText().isEmpty() ||
                jTextField3.getText().isEmpty() || jTextField4.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!");
            return false;
        }
        return true;
    }
    
    private void saveTableToCSV() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Pilih lokasi untuk menyimpan file CSV");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().endsWith(".csv")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
            }

            try (PrintWriter writer = new PrintWriter(fileToSave)) {
                for (int i = 0; i < modelTabel.getColumnCount(); i++) {
                    writer.print(modelTabel.getColumnName(i));
                    if (i < modelTabel.getColumnCount() - 1) writer.print(",");
                }
                writer.println();

                for (int i = 0; i < modelTabel.getRowCount(); i++) {
                    for (int j = 0; j < modelTabel.getColumnCount(); j++) {
                        writer.print(modelTabel.getValueAt(i, j));
                        if (j < modelTabel.getColumnCount() - 1) writer.print(",");
                    }
                    writer.println();
                }

                JOptionPane.showMessageDialog(this, "File CSV berhasil disimpan!");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan file: " + e.getMessage());
            }
        }
    }
    
    private void clearInputFields() {
        jTextField1.setText("");
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField4.setText("");
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
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 255, 255));

        jLabel1.setFont(new java.awt.Font("NSimSun", 1, 24)); // NOI18N
        jLabel1.setText("Aplikasi Buku Alamat");

        jLabel2.setText("Nama");

        jLabel3.setText("Alamat");

        jLabel4.setText("Telepon");

        jLabel5.setText("Email");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(jTable1);

        jButton1.setText("Tambah");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Edit");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Hapus");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Simpan");
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(317, 317, 317)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(87, 87, 87)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 752, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(264, 264, 264)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField1)
                                    .addComponent(jTextField2)
                                    .addComponent(jTextField3)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(236, 236, 236)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton4)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addGap(120, 120, 120)
                                .addComponent(jButton2)))
                        .addGap(110, 110, 110)
                        .addComponent(jButton3)))
                .addContainerGap(68, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel1)
                .addGap(75, 75, 75)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(47, 47, 47)
                .addComponent(jButton4)
                .addContainerGap(51, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        saveTableToCSV();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jButton1.addActionListener(e -> addContact());
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
       jButton2.addActionListener(e -> editSingleField());
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        jButton3.addActionListener(e -> deleteContact()); // Tombol Hapus
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(TugasUts.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TugasUts.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TugasUts.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TugasUts.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TugasUts().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}
