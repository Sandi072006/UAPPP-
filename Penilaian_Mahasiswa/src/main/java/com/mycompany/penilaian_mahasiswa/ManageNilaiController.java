package com.mycompany.penilaian_mahasiswa;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ManageNilaiController implements Initializable {
    
    @FXML
    private ComboBox<String> mahasiswaComboBox;
    
    @FXML
    private ComboBox<String> mataKuliahComboBox;
    
    @FXML
    private Spinner<Integer> jumlahQuizSpinner;
    
    @FXML
    private Spinner<Integer> jumlahTugasSpinner;
    
    @FXML
    private Button simpanButton;
    
    @FXML
    private Button batalButton;
    
    private ObservableList<Mahasiswa> mahasiswaList = FXCollections.observableArrayList();
    private ObservableList<MataKuliah> mataKuliahList = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Setup Spinners
        SpinnerValueFactory<Integer> quizValueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1);
        jumlahQuizSpinner.setValueFactory(quizValueFactory);
        
        SpinnerValueFactory<Integer> tugasValueFactory = 
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1);
        jumlahTugasSpinner.setValueFactory(tugasValueFactory);
        
        // Load data
        loadMahasiswaData();
        loadMataKuliahData();
    }
    
    private void loadMahasiswaData() {
        try (Connection conn = KoneksiDB.getConnection()) {
            String query = "SELECT npm, nama FROM mahasiswa ORDER BY nama";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            
            ObservableList<String> mahasiswaItems = FXCollections.observableArrayList();
            
            while (rs.next()) {
                String npm = rs.getString("npm");
                String nama = rs.getString("nama");
                String item = npm + " - " + nama;
                mahasiswaItems.add(item);
                
                // Simpan ke list untuk referensi
                Mahasiswa mhs = new Mahasiswa(nama, npm, "", 0, "");
                mahasiswaList.add(mhs);
            }
            
            mahasiswaComboBox.setItems(mahasiswaItems);
            
        } catch (SQLException e) {
            showAlert("Error", "Gagal memuat data mahasiswa: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void loadMataKuliahData() {
        try (Connection conn = KoneksiDB.getConnection()) {
            String query = "SELECT id_matkul, nama_matkul, sks FROM mata_kuliah ORDER BY nama_matkul";
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            
            ObservableList<String> matkulItems = FXCollections.observableArrayList();
            
            while (rs.next()) {
                String idMatkul = rs.getString("id_matkul");
                String namaMatkul = rs.getString("nama_matkul");
                int sks = rs.getInt("sks");
                String item = idMatkul + " - " + namaMatkul + " (" + sks + " SKS)";
                matkulItems.add(item);
                
                // Simpan ke list untuk referensi
                MataKuliah mk = new MataKuliah(idMatkul, namaMatkul, sks);
                mataKuliahList.add(mk);
            }
            
            mataKuliahComboBox.setItems(matkulItems);
            
        } catch (SQLException e) {
            showAlert("Error", "Gagal memuat data mata kuliah: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    public void handleSimpan() {
        // Validasi input
        if (mahasiswaComboBox.getValue() == null) {
            showAlert("Error", "Silakan pilih mahasiswa terlebih dahulu!", Alert.AlertType.ERROR);
            return;
        }
        
        if (mataKuliahComboBox.getValue() == null) {
            showAlert("Error", "Silakan pilih mata kuliah terlebih dahulu!", Alert.AlertType.ERROR);
            return;
        }
        
        // Ambil data yang dipilih
        int mahasiswaIndex = mahasiswaComboBox.getSelectionModel().getSelectedIndex();
        int matkulIndex = mataKuliahComboBox.getSelectionModel().getSelectedIndex();
        
        String npm = mahasiswaList.get(mahasiswaIndex).getNpm();
        String namaMahasiswa = mahasiswaList.get(mahasiswaIndex).getNama();
        String idMatkul = mataKuliahList.get(matkulIndex).getIdMatkul();
        String namaMatkul = mataKuliahList.get(matkulIndex).getNamaMatkul();
        
        int jumlahQuiz = jumlahQuizSpinner.getValue();
        int jumlahTugas = jumlahTugasSpinner.getValue();
        
        // Cek apakah data sudah ada
        if (checkDataExists(npm, idMatkul)) {
            showAlert("Warning", "Data nilai untuk mahasiswa ini pada mata kuliah ini sudah ada!\nSilakan gunakan fitur Edit.", Alert.AlertType.WARNING);
            return;
        }
        
        // Pindah ke halaman Input Nilai
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/penilaian_mahasiswa/input_nilai.fxml"));
            Parent root = loader.load();
            
            // Pass data ke controller InputNilai
            InputNilaiController controller = loader.getController();
            controller.initData(npm, namaMahasiswa, idMatkul, namaMatkul, jumlahQuiz, jumlahTugas);
            
            Stage stage = (Stage) simpanButton.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 700));
            stage.setTitle("Input Nilai - " + namaMahasiswa + " - " + namaMatkul);
            
        } catch (Exception e) {
            showAlert("Error", "Gagal membuka halaman input nilai: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    private boolean checkDataExists(String npm, String idMatkul) {
        try (Connection conn = KoneksiDB.getConnection()) {
            String query = "SELECT COUNT(*) as count FROM nilai_akhir_mahasiswa WHERE npm = ? AND id_matkul = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, npm);
            pst.setString(2, idMatkul);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking data: " + e.getMessage());
        }
        return false;
    }
    
    @FXML
    public void handleBatal() {
        try {
            Stage stage = (Stage) batalButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/mycompany/penilaian_mahasiswa/primary.fxml"));
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Manajemen Nilai Akademik");
        } catch (Exception e) {
            showAlert("Error", "Gagal kembali ke halaman utama: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}