package com.mycompany.penilaian_mahasiswa;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SecondaryController implements Initializable {

    // Field untuk input mahasiswa
    @FXML private TextField npmField;
    @FXML private TextField namaField;
    @FXML private TextField jurusanField;
    @FXML private TextField angkatanField;
    @FXML private TextField prodiField;
    
    // Table dan kolom untuk mahasiswa
    @FXML private TableView<Mahasiswa> mahasiswaTable;
    @FXML private TableColumn<Mahasiswa, String> npmColumn;
    @FXML private TableColumn<Mahasiswa, String> namaColumn;
    @FXML private TableColumn<Mahasiswa, String> jurusanColumn;
    @FXML private TableColumn<Mahasiswa, Integer> angkatanColumn;
    @FXML private TableColumn<Mahasiswa, String> prodiColumn;
    
    // Button untuk navigasi
    @FXML private Button kembaliButton;
    
    // Observable list untuk table
    private ObservableList<Mahasiswa> mahasiswaList = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Setup kolom table
        npmColumn.setCellValueFactory(new PropertyValueFactory<>("npm"));
        namaColumn.setCellValueFactory(new PropertyValueFactory<>("nama"));
        jurusanColumn.setCellValueFactory(new PropertyValueFactory<>("jurusan"));
        angkatanColumn.setCellValueFactory(new PropertyValueFactory<>("angkatan"));
        prodiColumn.setCellValueFactory(new PropertyValueFactory<>("prodi"));
        
        // Set items ke table
        mahasiswaTable.setItems(mahasiswaList);
        
        // Load data mahasiswa dari database
        loadMahasiswaData();
    }
    
    /**
     * Load data mahasiswa dari database ke table
     */
    private void loadMahasiswaData() {
        try (Connection conn = KoneksiDB.getConnection()) {
            String query = "SELECT npm, nama, jurusan, angkatan, program_studi FROM mahasiswa ORDER BY nama";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            
            mahasiswaList.clear();
            
            while (rs.next()) {
                String npm = rs.getString("npm");
                String nama = rs.getString("nama");
                String jurusan = rs.getString("jurusan");
                int angkatan = rs.getInt("angkatan");
                String prodi = rs.getString("program_studi");
                
                Mahasiswa mhs = new Mahasiswa(nama, npm, jurusan, angkatan, prodi);
                mahasiswaList.add(mhs);
            }
            
        } catch (SQLException e) {
            showAlert("Error", "Gagal memuat data mahasiswa: " + e.getMessage(), AlertType.ERROR);
        }
    }
    
    /**
     * Event handler untuk simpan mahasiswa
     */
    @FXML
    private void saveMahasiswa() {
        String npm = npmField.getText().trim();
        String nama = namaField.getText().trim();
        String jurusan = jurusanField.getText().trim();
        String prodi = prodiField.getText().trim();
        String angkatanStr = angkatanField.getText().trim();
        
        // Validasi input kosong
        if (npm.isEmpty() || nama.isEmpty() || jurusan.isEmpty() || 
            prodi.isEmpty() || angkatanStr.isEmpty()) {
            showAlert("Error", "Harap lengkapi semua data mahasiswa!", AlertType.ERROR);
            return;
        }

        // Validasi angkatan harus angka
        int angkatan = 0;
        try {
            angkatan = Integer.parseInt(angkatanStr);
            if (angkatan < 1900 || angkatan > 2100) {
                showAlert("Error", "Angkatan harus antara 1900-2100!", AlertType.ERROR);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Angkatan harus berupa angka!", AlertType.ERROR);
            return;
        }

        // Create object mahasiswa
        Mahasiswa mahasiswa = new Mahasiswa(nama, npm, jurusan, angkatan, prodi);

        // Simpan ke database
        if (saveMahasiswaToDatabase(mahasiswa)) {
            mahasiswaList.add(mahasiswa);
            showAlert("Success", "Mahasiswa " + nama + " berhasil ditambahkan!", AlertType.INFORMATION);
            clearForm();
        } else {
            showAlert("Error", "Gagal menyimpan data mahasiswa ke database!", AlertType.ERROR);
        }
    }

    /**
     * Simpan data mahasiswa ke database
     */
    private boolean saveMahasiswaToDatabase(Mahasiswa mahasiswa) {
        try (Connection conn = KoneksiDB.getConnection()) {
            conn.setAutoCommit(false);
            
            String sql = "INSERT INTO mahasiswa (npm, nama, jurusan, angkatan, program_studi) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, mahasiswa.getNpm());
                pst.setString(2, mahasiswa.getNama());
                pst.setString(3, mahasiswa.getJurusan());
                pst.setInt(4, mahasiswa.getAngkatan());
                pst.setString(5, mahasiswa.getProdi());
                
                int result = pst.executeUpdate();
                
                if (result > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }
            } catch (SQLException e) {
                conn.rollback();
                System.err.println("Error saving mahasiswa: " + e.getMessage());
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error connecting to database: " + e.getMessage());
            return false;
        }
    }

    /**
     * Clear semua input field
     */
    private void clearForm() {
        npmField.clear();
        namaField.clear();
        jurusanField.clear();
        angkatanField.clear();
        prodiField.clear();
    }

    /**
     * Navigasi kembali ke halaman utama
     */
    @FXML
    private void kembaliKeMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/mycompany/penilaian_mahasiswa/primary.fxml")
        );
        Parent root = loader.load();
        
        Stage stage = (Stage) kembaliButton.getScene().getWindow();
        stage.setScene(new Scene(root, 800, 600));
        stage.setTitle("Manajemen Nilai Akademik");
        stage.show();
    }

    /**
     * Menampilkan alert dialog
     */
    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}