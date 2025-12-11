module com.mycompany.penilaian_mahasiswa {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.base;
    requires java.desktop;
    requires java.logging;
     
    opens com.mycompany.penilaian_mahasiswa to javafx.fxml;
    exports com.mycompany.penilaian_mahasiswa;
}