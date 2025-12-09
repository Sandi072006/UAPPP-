package com.mycompany.penilaian_mahasiswa;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SecondaryController {

    @FXML private TextField npmField, namaField, jurusanField, angkatanField, prodiField;
    @FXML private TableView<Mahasiswa> mahasiswaTable;
    @FXML private TableColumn<Mahasiswa, String> npmColumn, namaColumn, jurusanColumn, prodiColumn;
    @FXML private TableColumn<Mahasiswa, Integer> angkatanColumn;
    
    // Event handler untuk simpan mahasiswa
    @FXML
    private void saveMahasiswa() {
        String npm = npmField.getText();
        String nama = namaField.getText();
        String jurusan = jurusanField.getText();
        String prodi = prodiField.getText();
        int angkatan = 0;
        
        if (npm.isEmpty() || nama.isEmpty() || jurusan.isEmpty() || prodi.isEmpty() || angkatanField.getText().isEmpty()) {
            showAlert("Error", "Harap lengkapi semua data mahasiswa.", AlertType.ERROR);
            return;
        }

        try {
            angkatan = Integer.parseInt(angkatanField.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Angkatan harus berupa angka.", AlertType.ERROR);
            return;
        }

        Mahasiswa mahasiswa = new Mahasiswa(nama, npm, jurusan, angkatan, prodi);

        if (saveMahasiswaToDatabase(mahasiswa)) {
            mahasiswaTable.getItems().add(mahasiswa);
            showAlert("Success", "Mahasiswa berhasil ditambahkan.", AlertType.INFORMATION);
        } else {
            showAlert("Error", "Gagal menyimpan data mahasiswa.", AlertType.ERROR);
        }
    }

    private boolean saveMahasiswaToDatabase(Mahasiswa mahasiswa) {
        try (Connection conn = KoneksiDB.getConnection()) {
            String sql = "INSERT INTO mahasiswa (npm, nama, jurusan, angkatan, program_studi) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, mahasiswa.getNpm());
                pst.setString(2, mahasiswa.getNama());
                pst.setString(3, mahasiswa.getJurusan());
                pst.setInt(4, mahasiswa.getAngkatan());
                pst.setString(5, mahasiswa.getProdi());
                int result = pst.executeUpdate();
                return result > 0;
            }
        } catch (SQLException e) {
            showAlert("Error", "Terjadi kesalahan saat menyimpan data.", AlertType.ERROR);
            return false;
        }
    }

    // Navigasi ke halaman Manage Nilai
    @FXML
    private void navigateToManageNilai() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/penilaian_mahasiswa/manage_nilai.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) mahasiswaTable.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // Navigasi ke halaman Input Nilai
    @FXML
    private void navigateToInputNilai() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/penilaian_mahasiswa/input_nilai.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) mahasiswaTable.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // Menampilkan alert
    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
