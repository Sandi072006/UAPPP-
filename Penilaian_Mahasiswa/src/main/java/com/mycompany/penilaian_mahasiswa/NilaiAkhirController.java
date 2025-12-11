package com.mycompany.penilaian_mahasiswa;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NilaiAkhirController {
    
    @FXML
    private TableView<NilaiAkhir> nilaiAkhirTable;
    
    @FXML
    private TableColumn<NilaiAkhir, String> npmColumn;
    
    @FXML
    private TableColumn<NilaiAkhir, String> namaColumn;
    
    @FXML
    private TableColumn<NilaiAkhir, String> matkulColumn;
    
    @FXML
    private TableColumn<NilaiAkhir, Integer> sksColumn;
    
    @FXML
    private TableColumn<NilaiAkhir, Float> nilaiColumn;
    
    @FXML
    private TableColumn<NilaiAkhir, String> hurufColumn;
    
    @FXML
    private TableColumn<NilaiAkhir, Float> bobotColumn;
    
    // IPK Table
    @FXML
    private TableView<IPKMahasiswa> ipkTable;
    
    @FXML
    private TableColumn<IPKMahasiswa, String> ipkNpmColumn;
    
    @FXML
    private TableColumn<IPKMahasiswa, String> ipkNamaColumn;
    
    @FXML
    private TableColumn<IPKMahasiswa, Integer> ipkJumlahMatkulColumn;
    
    @FXML
    private TableColumn<IPKMahasiswa, Integer> ipkTotalSksColumn;
    
    @FXML
    private TableColumn<IPKMahasiswa, Float> ipkNilaiColumn;
    
    @FXML
    private Button kembaliButton;
    
    @FXML
    private Button refreshButton;
    
    @FXML
    private Button recalculateButton;
    
    @FXML
    private Label totalDataLabel;
    
    @FXML
    private Label totalMahasiswaLabel;
    
    @FXML
    private TextField searchField;
    
    @FXML
    private TextField searchIpkField;
    
    private ObservableList<NilaiAkhir> nilaiList = FXCollections.observableArrayList();
    private ObservableList<NilaiAkhir> filteredList = FXCollections.observableArrayList();
    
    private ObservableList<IPKMahasiswa> ipkList = FXCollections.observableArrayList();
    private ObservableList<IPKMahasiswa> filteredIpkList = FXCollections.observableArrayList();
    
    public void initialize() {
        System.out.println("=== NilaiAkhirController initialized ===");
        
        // Setup table columns untuk Nilai Akhir
        npmColumn.setCellValueFactory(new PropertyValueFactory<>("npm"));
        namaColumn.setCellValueFactory(new PropertyValueFactory<>("namaMahasiswa"));
        matkulColumn.setCellValueFactory(new PropertyValueFactory<>("namaMatkul"));
        sksColumn.setCellValueFactory(new PropertyValueFactory<>("sks"));
        nilaiColumn.setCellValueFactory(new PropertyValueFactory<>("nilaiAkhirTotal"));
        hurufColumn.setCellValueFactory(new PropertyValueFactory<>("hurufMatkul"));
        bobotColumn.setCellValueFactory(new PropertyValueFactory<>("bobotMatkul"));
        
        // Setup table columns untuk IPK
        ipkNpmColumn.setCellValueFactory(new PropertyValueFactory<>("npm"));
        ipkNamaColumn.setCellValueFactory(new PropertyValueFactory<>("namaMahasiswa"));
        ipkJumlahMatkulColumn.setCellValueFactory(new PropertyValueFactory<>("jumlahMatkul"));
        ipkTotalSksColumn.setCellValueFactory(new PropertyValueFactory<>("totalSks"));
        ipkNilaiColumn.setCellValueFactory(new PropertyValueFactory<>("ipk"));
        
        // Format nilai dengan 2 desimal
        nilaiColumn.setCellFactory(col -> new TableCell<NilaiAkhir, Float>() {
            @Override
            protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
            }
        });
        
        bobotColumn.setCellFactory(col -> new TableCell<NilaiAkhir, Float>() {
            @Override
            protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
            }
        });
        
        ipkNilaiColumn.setCellFactory(col -> new TableCell<IPKMahasiswa, Float>() {
            @Override
            protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
            }
        });
        
        // Add search listeners
        if (searchField != null) {
            searchField.textProperty().addListener((obs, old, newVal) -> applyFilter());
        }
        
        if (searchIpkField != null) {
            searchIpkField.textProperty().addListener((obs, old, newVal) -> applyIpkFilter());
        }
        
        // Load data
        loadData();
        loadIpkData();
    }
    
    private void loadData() {
        System.out.println("Loading data from database...");
        nilaiList.clear();
        
        try (Connection conn = KoneksiDB.getConnection()) {
            String query = "SELECT " +
                          "nam.npm, " +
                          "m.nama as nama_mahasiswa, " +
                          "mk.nama_matkul, " +
                          "mk.sks, " +
                          "nam.nilai_akhir_total, " +
                          "nam.huruf_matkul, " +
                          "nam.bobot_matkul " +
                          "FROM nilai_akhir_mahasiswa nam " +
                          "JOIN mahasiswa m ON nam.npm = m.npm " +
                          "JOIN mata_kuliah mk ON nam.id_matkul = mk.id_matkul " +
                          "ORDER BY m.nama, mk.nama_matkul";
            
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                NilaiAkhir nilai = new NilaiAkhir(
                    rs.getString("npm"),
                    rs.getString("nama_mahasiswa"),
                    "", // idMatkul tidak ditampilkan
                    rs.getString("nama_matkul"),
                    rs.getInt("sks"),
                    rs.getFloat("nilai_akhir_total"),
                    rs.getString("huruf_matkul"),
                    rs.getFloat("bobot_matkul")
                );
                nilaiList.add(nilai);
            }
            
            System.out.println("Loaded " + nilaiList.size() + " records");
            applyFilter();
            
        } catch (SQLException e) {
            System.err.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Gagal memuat data: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void loadIpkData() {
        System.out.println("Loading IPK data from database...");
        ipkList.clear();
        
        try (Connection conn = KoneksiDB.getConnection()) {
            String query = "SELECT " +
                          "i.npm, " +
                          "m.nama as nama_mahasiswa, " +
                          "i.total_bobot_x_sks, " +
                          "i.total_sks, " +
                          "i.ipk, " +
                          "i.jumlah_matkul " +
                          "FROM ipk_mahasiswa i " +
                          "JOIN mahasiswa m ON i.npm = m.npm " +
                          "ORDER BY i.ipk DESC, m.nama";
            
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                IPKMahasiswa ipk = new IPKMahasiswa(
                    rs.getString("npm"),
                    rs.getString("nama_mahasiswa"),
                    rs.getFloat("total_bobot_x_sks"),
                    rs.getInt("total_sks"),
                    rs.getFloat("ipk"),
                    rs.getInt("jumlah_matkul")
                );
                ipkList.add(ipk);
            }
            
            System.out.println("Loaded " + ipkList.size() + " IPK records");
            applyIpkFilter();
            
        } catch (SQLException e) {
            System.err.println("Error loading IPK data: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Gagal memuat data IPK: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void applyFilter() {
        filteredList.clear();
        
        String search = "";
        if (searchField != null && searchField.getText() != null) {
            search = searchField.getText().toLowerCase().trim();
        }
        
        for (NilaiAkhir nilai : nilaiList) {
            if (search.isEmpty() || 
                nilai.getNamaMahasiswa().toLowerCase().contains(search) ||
                nilai.getNpm().toLowerCase().contains(search) ||
                nilai.getNamaMatkul().toLowerCase().contains(search)) {
                filteredList.add(nilai);
            }
        }
        
        nilaiAkhirTable.setItems(filteredList);
        
        if (totalDataLabel != null) {
            totalDataLabel.setText(String.valueOf(filteredList.size()));
        }
    }
    
    private void applyIpkFilter() {
        filteredIpkList.clear();
        
        String search = "";
        if (searchIpkField != null && searchIpkField.getText() != null) {
            search = searchIpkField.getText().toLowerCase().trim();
        }
        
        for (IPKMahasiswa ipk : ipkList) {
            if (search.isEmpty() || 
                ipk.getNamaMahasiswa().toLowerCase().contains(search) ||
                ipk.getNpm().toLowerCase().contains(search)) {
                filteredIpkList.add(ipk);
            }
        }
        
        ipkTable.setItems(filteredIpkList);
        
        if (totalMahasiswaLabel != null) {
            totalMahasiswaLabel.setText(String.valueOf(filteredIpkList.size()));
        }
    }
    
    @FXML
    public void handleRefresh() {
        System.out.println("Refresh button clicked");
        loadData();
        loadIpkData();
        showAlert("Success", "Data berhasil di-refresh!", Alert.AlertType.INFORMATION);
    }
    
    @FXML
    public void handleRecalculate() {
        System.out.println("Recalculate IPK button clicked");
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmasi");
        confirm.setHeaderText("Hitung Ulang Semua IPK?");
        confirm.setContentText("Proses ini akan menghitung ulang IPK untuk semua mahasiswa.\nLanjutkan?");
        
        if (confirm.showAndWait().get() == ButtonType.OK) {
            // Recalculate all IPK
            IPK.recalculateAllIPK();
            
            // Reload data
            loadIpkData();
            
            showAlert("Success", "Semua IPK berhasil dihitung ulang!", Alert.AlertType.INFORMATION);
        }
    }
    
    @FXML
    public void handleKembali() {
        System.out.println("Kembali button clicked");
        try {
            Stage stage = (Stage) kembaliButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/com/mycompany/penilaian_mahasiswa/primary.fxml"));
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Manajemen Nilai Akademik");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            showAlert("Error", "Gagal kembali: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    public void handleDetail() {
        NilaiAkhir selected = nilaiAkhirTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Silakan pilih data terlebih dahulu!", Alert.AlertType.WARNING);
            return;
        }
        
        String detail = String.format(
            "NPM: %s\n" +
            "Nama: %s\n" +
            "Mata Kuliah: %s\n" +
            "SKS: %d\n" +
            "Nilai Akhir: %.2f\n" +
            "Nilai Huruf: %s\n" +
            "Bobot: %.2f",
            selected.getNpm(),
            selected.getNamaMahasiswa(),
            selected.getNamaMatkul(),
            selected.getSks(),
            selected.getNilaiAkhirTotal(),
            selected.getHurufMatkul(),
            selected.getBobotMatkul()
        );
        
        showAlert("Detail Nilai", detail, Alert.AlertType.INFORMATION);
    }
    
    @FXML
    public void handleDetailIPK() {
        IPKMahasiswa selected = ipkTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Warning", "Silakan pilih mahasiswa terlebih dahulu!", Alert.AlertType.WARNING);
            return;
        }
        
        try (Connection conn = KoneksiDB.getConnection()) {
            // Query untuk mendapatkan detail mata kuliah
            String query = "SELECT " +
                          "mk.nama_matkul, " +
                          "mk.sks, " +
                          "nam.nilai_akhir_total, " +
                          "nam.huruf_matkul, " +
                          "nam.bobot_matkul " +
                          "FROM nilai_akhir_mahasiswa nam " +
                          "JOIN mata_kuliah mk ON nam.id_matkul = mk.id_matkul " +
                          "WHERE nam.npm = ? " +
                          "ORDER BY mk.nama_matkul";
            
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, selected.getNpm());
            ResultSet rs = pst.executeQuery();
            
            StringBuilder detail = new StringBuilder();
            detail.append("=== DATA MAHASISWA ===\n");
            detail.append(String.format("Nama : %s\n", selected.getNamaMahasiswa()));
            detail.append(String.format("NPM  : %s\n\n", selected.getNpm()));
            detail.append("=== DAFTAR MATA KULIAH ===\n");
            
            while (rs.next()) {
                String namaMatkul = rs.getString("nama_matkul");
                int sks = rs.getInt("sks");
                float nilaiAkhir = rs.getFloat("nilai_akhir_total");
                String huruf = rs.getString("huruf_matkul");
                float bobot = rs.getFloat("bobot_matkul");
                
                detail.append(String.format("\nMata Kuliah : %s\n", namaMatkul));
                detail.append(String.format("SKS         : %d\n", sks));
                detail.append(String.format("Nilai Akhir : %.2f\n", nilaiAkhir));
                detail.append(String.format("Huruf       : %s\n", huruf));
                detail.append(String.format("Bobot       : %.2f\n", bobot));
                detail.append(String.format("Bobot × SKS : %.2f\n", bobot * sks));
            }
            
            detail.append("\n=== HASIL IPK ===\n");
            detail.append(String.format("Total Mata Kuliah : %d\n", selected.getJumlahMatkul()));
            detail.append(String.format("Total SKS         : %d\n", selected.getTotalSks()));
            detail.append(String.format("Total Bobot × SKS : %.2f\n", selected.getTotalBobotXSks()));
            detail.append(String.format("IPK               : %.2f\n\n", selected.getIpk()));
            detail.append(String.format("Perhitungan: %.2f / %d = %.2f", 
                selected.getTotalBobotXSks(), selected.getTotalSks(), selected.getIpk()));
            
            showAlert("Detail IPK Mahasiswa", detail.toString(), Alert.AlertType.INFORMATION);
            
        } catch (SQLException e) {
            System.err.println("Error loading detail: " + e.getMessage());
            showAlert("Error", "Gagal memuat detail: " + e.getMessage(), Alert.AlertType.ERROR);
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