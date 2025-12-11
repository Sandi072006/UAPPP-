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
    
    @FXML
    private Button kembaliButton;
    
    @FXML
    private Button refreshButton;
    
    @FXML
    private Label totalDataLabel;
    
    @FXML
    private TextField searchField;
    
    private ObservableList<NilaiAkhir> nilaiList = FXCollections.observableArrayList();
    private ObservableList<NilaiAkhir> filteredList = FXCollections.observableArrayList();
    
    public void initialize() {
        System.out.println("=== NilaiAkhirController initialized ===");
        
        // Setup table columns
        npmColumn.setCellValueFactory(new PropertyValueFactory<>("npm"));
        namaColumn.setCellValueFactory(new PropertyValueFactory<>("namaMahasiswa"));
        matkulColumn.setCellValueFactory(new PropertyValueFactory<>("namaMatkul"));
        sksColumn.setCellValueFactory(new PropertyValueFactory<>("sks"));
        nilaiColumn.setCellValueFactory(new PropertyValueFactory<>("nilaiAkhirTotal"));
        hurufColumn.setCellValueFactory(new PropertyValueFactory<>("hurufMatkul"));
        bobotColumn.setCellValueFactory(new PropertyValueFactory<>("bobotMatkul"));
        
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
        
        // Add search listener
        if (searchField != null) {
            searchField.textProperty().addListener((obs, old, newVal) -> applyFilter());
        }
        
        // Load data
        loadData();
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
    
    @FXML
    public void handleRefresh() {
        System.out.println("Refresh button clicked");
        loadData();
        showAlert("Success", "Data berhasil di-refresh!", Alert.AlertType.INFORMATION);
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
    
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}