

CREATE TABLE mahasiswa (
    npm VARCHAR(20) PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    jurusan VARCHAR(100) NOT NULL,
    angkatan INT NOT NULL,
    program_studi VARCHAR(100) NOT NULL
);

CREATE TABLE mata_kuliah (
    id_matkul INT AUTO_INCREMENT PRIMARY KEY,
    nama_matkul VARCHAR(100) NOT NULL,
    sks INT NOT NULL
);





CREATE TABLE absensi (
    id_absensi INT AUTO_INCREMENT PRIMARY KEY,
    id_matkul INT NOT NULL,
    npm VARCHAR(20) NOT NULL,
    jumlah_pertemuan INT NOT NULL,
    jumlah_hadir INT NOT NULL,
    nilai_absensi FLOAT DEFAULT NULL,
    nilai_akhir_absensi FLOAT DEFAULT NULL,
    FOREIGN KEY (id_matkul) REFERENCES mata_kuliah(id_matkul) ON DELETE CASCADE,
    FOREIGN KEY (npm) REFERENCES mahasiswa(npm) ON DELETE CASCADE
);

CREATE TABLE nilai_tugas_detail (
    id_tugas_detail INT AUTO_INCREMENT PRIMARY KEY,
    id_matkul INT NOT NULL,
    npm VARCHAR(20) NOT NULL,
    tugas_number INT NOT NULL,
    nilai_tugas FLOAT NOT NULL DEFAULT 0,
    FOREIGN KEY (id_matkul) REFERENCES mata_kuliah(id_matkul) ON DELETE CASCADE,
    FOREIGN KEY (npm) REFERENCES mahasiswa(npm) ON DELETE CASCADE
);

CREATE TABLE nilai_tugas (
    id_tugas INT AUTO_INCREMENT PRIMARY KEY,
    id_matkul INT NOT NULL,
    npm VARCHAR(20) NOT NULL,
    rata_rata_tugas FLOAT NOT NULL,
    UNIQUE KEY uniq_matkul_npm_tugas (id_matkul, npm),
    FOREIGN KEY (id_matkul) REFERENCES mata_kuliah(id_matkul) ON DELETE CASCADE,
    FOREIGN KEY (npm) REFERENCES mahasiswa(npm) ON DELETE CASCADE
);

CREATE TABLE nilai_quiz_detail (
    id_quiz_detail INT AUTO_INCREMENT PRIMARY KEY,
    id_matkul INT NOT NULL,
    npm VARCHAR(20) NOT NULL,
    quiz_number INT NOT NULL,
    nilai_quiz FLOAT NOT NULL DEFAULT 0,
    FOREIGN KEY (id_matkul) REFERENCES mata_kuliah(id_matkul) ON DELETE CASCADE,
    FOREIGN KEY (npm) REFERENCES mahasiswa(npm) ON DELETE CASCADE
);

CREATE TABLE nilai_quiz (
    id_quiz INT AUTO_INCREMENT PRIMARY KEY,
    id_matkul INT NOT NULL,
    npm VARCHAR(20) NOT NULL,
    rata_rata_quiz FLOAT NOT NULL,
    UNIQUE KEY uniq_matkul_npm_quiz (id_matkul, npm),
    FOREIGN KEY (id_matkul) REFERENCES mata_kuliah(id_matkul) ON DELETE CASCADE,
    FOREIGN KEY (npm) REFERENCES mahasiswa(npm) ON DELETE CASCADE
);

CREATE TABLE nilai_uts (
    id_uts INT AUTO_INCREMENT PRIMARY KEY,
    id_matkul INT NOT NULL,
    npm VARCHAR(20) NOT NULL,
    nilai_uts FLOAT NOT NULL,
    FOREIGN KEY (id_matkul) REFERENCES mata_kuliah(id_matkul) ON DELETE CASCADE,
    FOREIGN KEY (npm) REFERENCES mahasiswa(npm) ON DELETE CASCADE
);

CREATE TABLE nilai_uas (
    id_uas INT AUTO_INCREMENT PRIMARY KEY,
    id_matkul INT NOT NULL,
    npm VARCHAR(20) NOT NULL,
    nilai_uas FLOAT NOT NULL,
    FOREIGN KEY (id_matkul) REFERENCES mata_kuliah(id_matkul) ON DELETE CASCADE,
    FOREIGN KEY (npm) REFERENCES mahasiswa(npm) ON DELETE CASCADE
);

CREATE TABLE nilai_responsi (
    id_responsi INT AUTO_INCREMENT PRIMARY KEY,
    id_matkul INT NOT NULL,
    npm VARCHAR(20) NOT NULL,
    nilai_responsi FLOAT,
    FOREIGN KEY (id_matkul) REFERENCES mata_kuliah(id_matkul) ON DELETE CASCADE,
    FOREIGN KEY (npm) REFERENCES mahasiswa(npm) ON DELETE CASCADE
);

CREATE TABLE persentase_matkul (
    id_persentase INT AUTO_INCREMENT PRIMARY KEY,
    id_matkul INT NOT NULL,
    persentase_absensi FLOAT NOT NULL,
    persentase_tugas FLOAT NOT NULL,
    persentase_quiz FLOAT NOT NULL,
    persentase_uts FLOAT NOT NULL,
    persentase_uas FLOAT NOT NULL,
    persentase_responsi FLOAT,
    FOREIGN KEY (id_matkul) REFERENCES mata_kuliah(id_matkul) ON DELETE CASCADE
);

CREATE TABLE nilai_akhir_mahasiswa (
    id_nilai INT AUTO_INCREMENT PRIMARY KEY,
    id_matkul INT NOT NULL,
    npm VARCHAR(20) NOT NULL,
    nilai_akhir_total FLOAT,
    huruf_matkul VARCHAR(2),
    bobot_matkul FLOAT,
    UNIQUE KEY uniq_nilai_matkul_npm (id_matkul, npm),
    FOREIGN KEY (id_matkul) REFERENCES mata_kuliah(id_matkul) ON DELETE CASCADE,
    FOREIGN KEY (npm) REFERENCES mahasiswa(npm) ON DELETE CASCADE
);

yang akan kita gunakan sebelum lanjut mengembangkan manage nilai 

perhatikan DATABASE db_penilaian
 

CREATE TABLE mahasiswa (
    npm VARCHAR(20) PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    jurusan VARCHAR(100) NOT NULL,
    angkatan INT NOT NULL,
    program_studi VARCHAR(100) NOT NULL
);

CREATE TABLE mata_kuliah (
    id_matkul INT AUTO_INCREMENT PRIMARY KEY,
    nama_matkul VARCHAR(100) NOT NULL,
    sks INT NOT NULL
);





CREATE TABLE absensi (
    id_absensi INT AUTO_INCREMENT PRIMARY KEY,
    id_matkul INT NOT NULL,
    npm VARCHAR(20) NOT NULL,
    jumlah_pertemuan INT NOT NULL,
    jumlah_hadir INT NOT NULL,
    nilai_absensi FLOAT DEFAULT NULL,
    nilai_akhir_absensi FLOAT DEFAULT NULL,
    FOREIGN KEY (id_matkul) REFERENCES mata_kuliah(id_matkul) ON DELETE CASCADE,
    FOREIGN KEY (npm) REFERENCES mahasiswa(npm) ON DELETE CASCADE
);

CREATE TABLE nilai_tugas_detail (
    id_tugas_detail INT AUTO_INCREMENT PRIMARY KEY,
    id_matkul INT NOT NULL,
    npm VARCHAR(20) NOT NULL,
    tugas_number INT NOT NULL,
    nilai_tugas FLOAT NOT NULL DEFAULT 0,
    FOREIGN KEY (id_matkul) REFERENCES mata_kuliah(id_matkul) ON DELETE CASCADE,
    FOREIGN KEY (npm) REFERENCES mahasiswa(npm) ON DELETE CASCADE
);

CREATE TABLE nilai_tugas (
    id_tugas INT AUTO_INCREMENT PRIMARY KEY,
    id_matkul INT NOT NULL,
    npm VARCHAR(20) NOT NULL,
    rata_rata_tugas FLOAT NOT NULL,
    UNIQUE KEY uniq_matkul_npm_tugas (id_matkul, npm),
    FOREIGN KEY (id_matkul) REFERENCES mata_kuliah(id_matkul) ON DELETE CASCADE,
    FOREIGN KEY (npm) REFERENCES mahasiswa(npm) ON DELETE CASCADE
);

CREATE TABLE nilai_quiz_detail (
    id_quiz_detail INT AUTO_INCREMENT PRIMARY KEY,
    id_matkul INT NOT NULL,
    npm VARCHAR(20) NOT NULL,
    quiz_number INT NOT NULL,
    nilai_quiz FLOAT NOT NULL DEFAULT 0,
    FOREIGN KEY (id_matkul) REFERENCES mata_kuliah(id_matkul) ON DELETE CASCADE,
    FOREIGN KEY (npm) REFERENCES mahasiswa(npm) ON DELETE CASCADE
);

CREATE TABLE nilai_quiz (
    id_quiz INT AUTO_INCREMENT PRIMARY KEY,
    id_matkul INT NOT NULL,
    npm VARCHAR(20) NOT NULL,
    rata_rata_quiz FLOAT NOT NULL,
    UNIQUE KEY uniq_matkul_npm_quiz (id_matkul, npm),
    FOREIGN KEY (id_matkul) REFERENCES mata_kuliah(id_matkul) ON DELETE CASCADE,
    FOREIGN KEY (npm) REFERENCES mahasiswa(npm) ON DELETE CASCADE
);

CREATE TABLE nilai_uts (
    id_uts INT AUTO_INCREMENT PRIMARY KEY,
    id_matkul INT NOT NULL,
    npm VARCHAR(20) NOT NULL,
    nilai_uts FLOAT NOT NULL,
    FOREIGN KEY (id_matkul) REFERENCES mata_kuliah(id_matkul) ON DELETE CASCADE,
    FOREIGN KEY (npm) REFERENCES mahasiswa(npm) ON DELETE CASCADE
);

CREATE TABLE nilai_uas (
    id_uas INT AUTO_INCREMENT PRIMARY KEY,
    id_matkul INT NOT NULL,
    npm VARCHAR(20) NOT NULL,
    nilai_uas FLOAT NOT NULL,
    FOREIGN KEY (id_matkul) REFERENCES mata_kuliah(id_matkul) ON DELETE CASCADE,
    FOREIGN KEY (npm) REFERENCES mahasiswa(npm) ON DELETE CASCADE
);

CREATE TABLE nilai_responsi (
    id_responsi INT AUTO_INCREMENT PRIMARY KEY,
    id_matkul INT NOT NULL,
    npm VARCHAR(20) NOT NULL,
    nilai_responsi FLOAT,
    FOREIGN KEY (id_matkul) REFERENCES mata_kuliah(id_matkul) ON DELETE CASCADE,
    FOREIGN KEY (npm) REFERENCES mahasiswa(npm) ON DELETE CASCADE
);

CREATE TABLE persentase_matkul (
    id_persentase INT AUTO_INCREMENT PRIMARY KEY,
    id_matkul INT NOT NULL,
    persentase_absensi FLOAT NOT NULL,
    persentase_tugas FLOAT NOT NULL,
    persentase_quiz FLOAT NOT NULL,
    persentase_uts FLOAT NOT NULL,
    persentase_uas FLOAT NOT NULL,
    persentase_responsi FLOAT,
    FOREIGN KEY (id_matkul) REFERENCES mata_kuliah(id_matkul) ON DELETE CASCADE
);

CREATE TABLE nilai_akhir_mahasiswa (
    id_nilai INT AUTO_INCREMENT PRIMARY KEY,
    id_matkul INT NOT NULL,
    npm VARCHAR(20) NOT NULL,
    nilai_akhir_total FLOAT,
    huruf_matkul VARCHAR(2),
    bobot_matkul FLOAT,
    UNIQUE KEY uniq_nilai_matkul_npm (id_matkul, npm),
    FOREIGN KEY (id_matkul) REFERENCES mata_kuliah(id_matkul) ON DELETE CASCADE,
    FOREIGN KEY (npm) REFERENCES mahasiswa(npm) ON DELETE CASCADE
);