package com.mycompany.penilaian_mahasiswa;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.net.URL;

public class PrimaryController {
    
    @FXML
    private Button manageMahasiswaButton;
    
    @FXML
    private Button manageMataKuliahButton;
    
    @FXML
    private Button manageNilaiButton;

    /**
     * Event handler untuk navigasi ke halaman Manage Mahasiswa
     */
    @FXML
    public void manageMahasiswa() {
        try {
            navigateToPage("mahasiswa", "Kelola Data Mahasiswa", 800, 600);
        } catch (Exception e) {
            showError("Gagal membuka halaman Manage Mahasiswa", e);
        }
    }

    /**
     * Event handler untuk navigasi ke halaman Manage Mata Kuliah
     */
    @FXML
    public void manageMataKuliah() {
        try {
            navigateToPage("matakuliah", "Kelola Data Mata Kuliah", 800, 600);
        } catch (Exception e) {
            showError("Gagal membuka halaman Manage Mata Kuliah", e);
        }
    }

    /**
     * Event handler untuk navigasi ke halaman Manage Nilai
     */
    @FXML
    public void manageNilai() {
        System.out.println("=== MANAGE NILAI CLICKED ===");
        try {
            // Cek apakah file FXML ada
            String fxmlPath = "/com/mycompany/penilaian_mahasiswa/manage_nilai.fxml";
            URL fxmlUrl = getClass().getResource(fxmlPath);
            
            System.out.println("FXML Path: " + fxmlPath);
            System.out.println("FXML URL: " + fxmlUrl);
            
            if (fxmlUrl == null) {
                // Jika manage_nilai.fxml tidak ada, coba pakai input_nilai.fxml langsung
                showAlert("Info", 
                    "File manage_nilai.fxml tidak ditemukan.\n" +
                    "Mencoba membuka input_nilai.fxml...", 
                    Alert.AlertType.INFORMATION);
                
                // Langsung ke input nilai (sementara untuk testing)
                openInputNilaiDirect();
                return;
            }
            
            navigateToPage("manage_nilai", "Kelola Nilai Mahasiswa", 900, 700);
            
        } catch (Exception e) {
            System.err.println("Error in manageNilai: " + e.getMessage());
            e.printStackTrace();
            showError("Gagal membuka halaman Manage Nilai", e);
        }
    }
    
    /**
     * Buka input nilai langsung (untuk testing jika manage_nilai.fxml tidak ada)
     */
    private void openInputNilaiDirect() {
        try {
            String fxmlPath = "/com/mycompany/penilaian_mahasiswa/input_nilai.fxml";
            URL fxmlUrl = getClass().getResource(fxmlPath);
            
            if (fxmlUrl == null) {
                showAlert("Error", 
                    "File input_nilai.fxml juga tidak ditemukan!\n" +
                    "Pastikan file FXML ada di folder resources.", 
                    Alert.AlertType.ERROR);
                return;
            }
            
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            
            // Get controller dan init data dummy untuk testing
            InputNilaiController controller = loader.getController();
            controller.initData("DUMMY001", "Test Mahasiswa", "DUMMY01", "Test Matkul", 3, 3);
            
            Stage stage = (Stage) manageNilaiButton.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 700));
            stage.setTitle("Input Nilai - Testing");
            stage.show();
            
        } catch (Exception e) {
            showError("Gagal membuka input nilai", e);
        }
    }

    /**
     * Helper method untuk navigasi ke halaman lain
     */
    private void navigateToPage(String fxmlFile, String title, int width, int height) throws IOException {
        String fxmlPath = "/com/mycompany/penilaian_mahasiswa/" + fxmlFile + ".fxml";
        System.out.println("Attempting to load: " + fxmlPath);
        
        URL fxmlUrl = getClass().getResource(fxmlPath);
        System.out.println("Resource URL: " + fxmlUrl);
        
        if (fxmlUrl == null) {
            throw new IOException("Cannot find FXML file: " + fxmlPath);
        }
        
        FXMLLoader loader = new FXMLLoader(fxmlUrl);
        Parent root = loader.load();

        Stage stage = getStageFromButtons();
        if (stage == null) {
            throw new IOException("Cannot get stage from buttons");
        }
        
        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
        
        System.out.println("Successfully loaded: " + title);
    }
    
    /**
     * Get stage dari button yang tersedia
     */
    private Stage getStageFromButtons() {
        if (manageMahasiswaButton != null && manageMahasiswaButton.getScene() != null) {
            return (Stage) manageMahasiswaButton.getScene().getWindow();
        }
        if (manageMataKuliahButton != null && manageMataKuliahButton.getScene() != null) {
            return (Stage) manageMataKuliahButton.getScene().getWindow();
        }
        if (manageNilaiButton != null && manageNilaiButton.getScene() != null) {
            return (Stage) manageNilaiButton.getScene().getWindow();
        }
        return null;
    }
    
    /**
     * Menampilkan error alert dengan detail
     */
    private void showError(String message, Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        
        String errorDetail = e.getMessage();
        if (errorDetail == null || errorDetail.isEmpty()) {
            errorDetail = e.getClass().getName();
        }
        
        alert.setContentText(errorDetail);
        alert.showAndWait();
        
        System.err.println("ERROR: " + message);
        e.printStackTrace();
    }
    
    /**
     * Menampilkan alert biasa
     */
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}