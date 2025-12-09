package com.mycompany.penilaian_mahasiswa;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InputNilaiController implements Initializable {
    
    @FXML
    private Label mahasiswaLabel;
    
    @FXML
    private Label mataKuliahLabel;
    
    @FXML
    private TextField nilaiUtsField;
    
    @FXML
    private TextField nilaiUasField;
    
    @FXML
    private TextField nilaiResponsiField;
    
    @FXML
    private TextField jumlahPertemuanField;
    
    @FXML
    private TextField jumlahHadirField;
    
    @FXML
    private GridPane quizGridPane;
    
    @FXML
    private GridPane tugasGridPane;
    
    @FXML
    private Button simpanButton;
    
    @FXML
    private Button batalButton;
    
    private String npm;
    private String namaMahasiswa;
    private String idMatkul;
    private String namaMatkul;
    private int jumlahQuiz;
    private int jumlahTugas;
    
    private List<TextField> quizFields = new ArrayList<>();
    private List<TextField> tugasFields = new ArrayList<>();
    
    // Persentase dari database
    private float persentaseAbsensi;
    private float persentaseTugas;
    private float persentaseQuiz;
    private float persentaseUts;
    private float persentaseUas;
    private Float persentaseResponsi;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inisialisasi akan dilakukan di initData()
    }
    
    public void initData(String npm, String namaMahasiswa, String idMatkul, String namaMatkul, int jumlahQuiz, int jumlahTugas) {
        this.npm = npm;
        this.namaMahasiswa = namaMahasiswa;
        this.idMatkul = idMatkul;
        this.namaMatkul = namaMatkul;
        this.jumlahQuiz = jumlahQuiz;
        this.jumlahTugas = jumlahTugas;
        
        // Set labels
        mahasiswaLabel.setText(npm + " - " + namaMahasiswa);
        mataKuliahLabel.setText(namaMatkul);
        
        // Load persentase dari database
        loadPersentaseMatkul();
        
        // Generate dynamic fields
        generateQuizFields();
        generateTugasFields();
    }
    
    private void loadPersentaseMatkul() {
        try (Connection conn = KoneksiDB.getConnection()) {
            String query = "SELECT * FROM persentase_matkul WHERE id_matkul = ?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, idMatkul);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                persentaseAbsensi = rs.getFloat("persentase_absensi");
                persentaseTugas = rs.getFloat("persentase_tugas");
                persentaseQuiz = rs.getFloat("persentase_quiz");
                persentaseUts = rs.getFloat("persentase_uts");
                persentaseUas = rs.getFloat("persentase_uas");
                persentaseResponsi = rs.getFloat("persentase_responsi");
                if (rs.wasNull()) {
                    persentaseResponsi = null;
                }
            } else {
                showAlert("Error", "Persentase mata kuliah tidak ditemukan!", Alert.AlertType.ERROR);
            }
            
        } catch (SQLException e) {
            showAlert("Error", "Gagal memuat persentase mata kuliah: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void generateQuizFields() {
        quizGridPane.getChildren().clear();
        quizFields.clear();
        
        for (int i = 0; i < jumlahQuiz; i++) {
            Label label = new Label("Nilai Quiz " + (i + 1) + ":");
            label.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");
            
            TextField textField = new TextField();
            textField.setPromptText("0-100");
            textField.setPrefWidth(150);
            textField.setStyle("-fx-font-size: 13px;");
            
            quizGridPane.add(label, 0, i);
            quizGridPane.add(textField, 1, i);
            
            quizFields.add(textField);
        }
        
        quizGridPane.setHgap(10);
        quizGridPane.setVgap(10);
    }
    
    private void generateTugasFields() {
        tugasGridPane.getChildren().clear();
        tugasFields.clear();
        
        for (int i = 0; i < jumlahTugas; i++) {
            Label label = new Label("Nilai Tugas " + (i + 1) + ":");
            label.setStyle("-fx-font-size: 13px; -fx-font-weight: bold;");
            
            TextField textField = new TextField();
            textField.setPromptText("0-100");
            textField.setPrefWidth(150);
            textField.setStyle("-fx-font-size: 13px;");
            
            tugasGridPane.add(label, 0, i);
            tugasGridPane.add(textField, 1, i);
            
            tugasFields.add(textField);
        }
        
        tugasGridPane.setHgap(10);
        tugasGridPane.setVgap(10);
    }
    
    @FXML
    public void handleSimpan() {
        // Validasi input
        if (!validateInput()) {
            return;
        }
        
        try (Connection conn = KoneksiDB.getConnection()) {
            conn.setAutoCommit(false);
            
            try {
                // 1. Simpan Nilai UTS
                saveNilaiUts(conn);
                
                // 2. Simpan Nilai UAS
                saveNilaiUas(conn);
                
                // 3. Simpan Nilai Quiz Detail & Rata-rata
                saveNilaiQuiz(conn);
                
                // 4. Simpan Nilai Tugas Detail & Rata-rata
                saveNilaiTugas(conn);
                
                // 5. Simpan Absensi
                saveAbsensi(conn);
                
                // 6. Simpan Nilai Responsi (jika ada)
                if (persentaseResponsi != null && !nilaiResponsiField.getText().isEmpty()) {
                    saveNilaiResponsi(conn);
                }
                
                // 7. Hitung dan Simpan Nilai Akhir
                saveNilaiAkhir(conn);
                
                conn.commit();
                
                showAlert("Success", "Data nilai berhasil disimpan!", Alert.AlertType.INFORMATION);
                
                // Kembali ke halaman utama
                handleBatal();
                
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
            
        } catch (SQLException e) {
            showAlert("Error", "Gagal menyimpan data nilai: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    
    private boolean validateInput() {
        // Validasi UTS
        if (nilaiUtsField.getText().isEmpty()) {
            showAlert("Error", "Nilai UTS harus diisi!", Alert.AlertType.ERROR);
            return false;
        }
        
        // Validasi UAS
        if (nilaiUasField.getText().isEmpty()) {
            showAlert("Error", "Nilai UAS harus diisi!", Alert.AlertType.ERROR);
            return false;
        }
        
        // Validasi Quiz
        for (int i = 0; i < quizFields.size(); i++) {
            if (quizFields.get(i).getText().isEmpty()) {
                showAlert("Error", "Nilai Quiz " + (i + 1) + " harus diisi!", Alert.AlertType.ERROR);
                return false;
            }
        }
        
        // Validasi Tugas
        for (int i = 0; i < tugasFields.size(); i++) {
            if (tugasFields.get(i).getText().isEmpty()) {
                showAlert("Error", "Nilai Tugas " + (i + 1) + " harus diisi!", Alert.AlertType.ERROR);
                return false;
            }
        }
        
        // Validasi Absensi
        if (jumlahPertemuanField.getText().isEmpty() || jumlahHadirField.getText().isEmpty()) {
            showAlert("Error", "Jumlah Pertemuan dan Jumlah Hadir harus diisi!", Alert.AlertType.ERROR);
            return false;
        }
        
        // Validasi range nilai
        try {
            float nilaiUts = Float.parseFloat(nilaiUtsField.getText());
            float nilaiUas = Float.parseFloat(nilaiUasField.getText());
            
            if (nilaiUts < 0 || nilaiUts > 100 || nilaiUas < 0 || nilaiUas > 100) {
                showAlert("Error", "Nilai UTS dan UAS harus antara 0-100!", Alert.AlertType.ERROR);
                return false;
            }
            
            for (TextField field : quizFields) {
                float nilai = Float.parseFloat(field.getText());
                if (nilai < 0 || nilai > 100) {
                    showAlert("Error", "Nilai Quiz harus antara 0-100!", Alert.AlertType.ERROR);
                    return false;
                }
            }
            
            for (TextField field : tugasFields) {
                float nilai = Float.parseFloat(field.getText());
                if (nilai < 0 || nilai > 100) {
                    showAlert("Error", "Nilai Tugas harus antara 0-100!", Alert.AlertType.ERROR);
                    return false;
                }
            }
            
            int jumlahPertemuan = Integer.parseInt(jumlahPertemuanField.getText());
            int jumlahHadir = Integer.parseInt(jumlahHadirField.getText());
            
            if (jumlahHadir > jumlahPertemuan) {
                showAlert("Error", "Jumlah Hadir tidak boleh lebih besar dari Jumlah Pertemuan!", Alert.AlertType.ERROR);
                return false;
            }
            
        } catch (NumberFormatException e) {
            showAlert("Error", "Pastikan semua input berupa angka yang valid!", Alert.AlertType.ERROR);
            return false;
        }
        
        return true;
    }
    
    private void saveNilaiUts(Connection conn) throws SQLException {
        String query = "INSERT INTO nilai_uts (id_matkul, npm, nilai_uts) VALUES (?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, idMatkul);
        pst.setString(2, npm);
        pst.setFloat(3, Float.parseFloat(nilaiUtsField.getText()));
        pst.executeUpdate();
    }
    
    private void saveNilaiUas(Connection conn) throws SQLException {
        String query = "INSERT INTO nilai_uas (id_matkul, npm, nilai_uas) VALUES (?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, idMatkul);
        pst.setString(2, npm);
        pst.setFloat(3, Float.parseFloat(nilaiUasField.getText()));
        pst.executeUpdate();
    }
    
    private void saveNilaiQuiz(Connection conn) throws SQLException {
        float totalQuiz = 0;
        
        // Simpan detail quiz
        for (int i = 0; i < quizFields.size(); i++) {
            float nilaiQuiz = Float.parseFloat(quizFields.get(i).getText());
            totalQuiz += nilaiQuiz;
            
            String query = "INSERT INTO nilai_quiz_detail (id_matkul, npm, quiz_number, nilai_quiz) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, idMatkul);
            pst.setString(2, npm);
            pst.setInt(3, i + 1);
            pst.setFloat(4, nilaiQuiz);
            pst.executeUpdate();
        }
        
        // Simpan rata-rata quiz
        float rataRataQuiz = totalQuiz / quizFields.size();
        String query = "INSERT INTO nilai_quiz (id_matkul, npm, rata_rata_quiz) VALUES (?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, idMatkul);
        pst.setString(2, npm);
        pst.setFloat(3, rataRataQuiz);
        pst.executeUpdate();
    }
    
    private void saveNilaiTugas(Connection conn) throws SQLException {
        float totalTugas = 0;
        
        // Simpan detail tugas
        for (int i = 0; i < tugasFields.size(); i++) {
            float nilaiTugas = Float.parseFloat(tugasFields.get(i).getText());
            totalTugas += nilaiTugas;
            
            String query = "INSERT INTO nilai_tugas_detail (id_matkul, npm, tugas_number, nilai_tugas) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, idMatkul);
            pst.setString(2, npm);
            pst.setInt(3, i + 1);
            pst.setFloat(4, nilaiTugas);
            pst.executeUpdate();
        }
        
        // Simpan rata-rata tugas
        float rataRataTugas = totalTugas / tugasFields.size();
        String query = "INSERT INTO nilai_tugas (id_matkul, npm, rata_rata_tugas) VALUES (?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(query);
        pst.setString(1, idMatkul);
        pst.setString(2, npm);
        pst.setFloat(3, rataRataTugas);
        pst.executeUpdate();
    }
    
    private void saveAbsensi(Connection conn) throws SQLException {
    int jumlahPertemuan = Integer.parseInt(jumlahPertemuanField.getText());
    int jumlahHadir = Integer.parseInt(jumlahHadirField.getText());
    
    // Hitung nilai absensi
    float nilaiAbsensi = ((float) jumlahHadir / jumlahPertemuan) * 100;
    
    // Hitung nilai akhir absensi berdasarkan persentase
    float nilaiAkhirAbsensi = nilaiAbsensi * (persentaseAbsensi / 100);
    
    // Query untuk menyimpan data absensi
    String query = "INSERT INTO absensi (id_matkul, npm, jumlah_pertemuan, jumlah_hadir, nilai_absensi, nilai_akhir_absensi) "
                 + "VALUES (?, ?, ?, ?, ?, ?)";
    
    try (PreparedStatement pst = conn.prepareStatement(query)) {
        pst.setString(1, idMatkul); // ID Mata Kuliah
        pst.setString(2, npm); // NPM Mahasiswa
        pst.setInt(3, jumlahPertemuan); // Jumlah Pertemuan
        pst.setInt(4, jumlahHadir); // Jumlah Hadir
        pst.setFloat(5, nilaiAbsensi); // Nilai Absensi (Persentase Kehadiran)
        pst.setFloat(6, nilaiAkhirAbsensi); // Nilai Akhir Absensi (dikalkulasi dengan persentase)

        // Eksekusi query
        pst.executeUpdate();
    }
}

// Menyimpan Nilai Responsi (jika ada)
private void saveNilaiResponsi(Connection conn) throws SQLException {
    String query = "INSERT INTO nilai_responsi (id_matkul, npm, nilai_responsi) VALUES (?, ?, ?)";
    PreparedStatement pst = conn.prepareStatement(query);
    pst.setString(1, idMatkul); // ID Mata Kuliah
    pst.setString(2, npm); // NPM Mahasiswa
    pst.setFloat(3, Float.parseFloat(nilaiResponsiField.getText())); // Nilai Responsi
    pst.executeUpdate();
}

// Menyimpan Nilai Akhir Mahasiswa
private void saveNilaiAkhir(Connection conn) throws SQLException {
    // Ambil semua nilai
    float nilaiUts = Float.parseFloat(nilaiUtsField.getText());
    float nilaiUas = Float.parseFloat(nilaiUasField.getText());

    // Rata-rata nilai quiz
    float totalQuiz = 0;
    for (TextField field : quizFields) {
        totalQuiz += Float.parseFloat(field.getText());
    }
    float rataRataQuiz = totalQuiz / quizFields.size();

    // Rata-rata nilai tugas
    float totalTugas = 0;
    for (TextField field : tugasFields) {
        totalTugas += Float.parseFloat(field.getText());
    }
    float rataRataTugas = totalTugas / tugasFields.size();

    // Nilai absensi
    int jumlahPertemuan = Integer.parseInt(jumlahPertemuanField.getText());
    int jumlahHadir = Integer.parseInt(jumlahHadirField.getText());
    float nilaiAbsensi = ((float) jumlahHadir / jumlahPertemuan) * 100;

    // Hitung nilai akhir total
    float nilaiAkhirTotal = 0;
    nilaiAkhirTotal += nilaiUts * (persentaseUts / 100);
    nilaiAkhirTotal += nilaiUas * (persentaseUas / 100);
    nilaiAkhirTotal += rataRataQuiz * (persentaseQuiz / 100);
    nilaiAkhirTotal += rataRataTugas * (persentaseTugas / 100);
    nilaiAkhirTotal += nilaiAbsensi * (persentaseAbsensi / 100);

    // Tambahkan nilai responsi jika ada
    if (persentaseResponsi != null && !nilaiResponsiField.getText().isEmpty()) {
        float nilaiResponsi = Float.parseFloat(nilaiResponsiField.getText());
        nilaiAkhirTotal += nilaiResponsi * (persentaseResponsi / 100);
    }

    // Konversi nilai akhir ke huruf
    String hurufMatkul = konversiNilaiKeHuruf(nilaiAkhirTotal);
    float bobotMatkul = konversiNilaiKeBobot(hurufMatkul);

    // Simpan nilai akhir ke database
    String query = "INSERT INTO nilai_akhir_mahasiswa (id_matkul, npm, nilai_akhir_total, huruf_matkul, bobot_matkul) VALUES (?, ?, ?, ?, ?)";
    PreparedStatement pst = conn.prepareStatement(query);
    pst.setString(1, idMatkul); // ID Mata Kuliah
    pst.setString(2, npm); // NPM Mahasiswa
    pst.setFloat(3, nilaiAkhirTotal); // Nilai Akhir Total
    pst.setString(4, hurufMatkul); // Nilai Huruf
    pst.setFloat(5, bobotMatkul); // Bobot Nilai
    pst.executeUpdate();
}

// Konversi nilai ke huruf
private String konversiNilaiKeHuruf(float nilai) {
    if (nilai >= 85) return "A";
    else if (nilai >= 80) return "A-";
    else if (nilai >= 75) return "B+";
    else if (nilai >= 70) return "B";
    else if (nilai >= 65) return "B-";
    else if (nilai >= 60) return "C+";
    else if (nilai >= 55) return "C";
    else if (nilai >= 50) return "D";
    else return "E";
}

// Konversi nilai huruf ke bobot
private float konversiNilaiKeBobot(String huruf) {
    switch (huruf) {
        case "A": return 4.0f;
        case "A-": return 3.7f;
        case "B+": return 3.3f;
        case "B": return 3.0f;
        case "B-": return 2.7f;
        case "C+": return 2.3f;
        case "C": return 2.0f;
        case "D": return 1.0f;
        case "E": return 0.0f;
        default: return 0.0f;
    }
}

// Menangani tindakan batal, kembali ke halaman utama
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

// Menampilkan alert dengan jenis pesan
private void showAlert(String title, String message, Alert.AlertType type) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}

}