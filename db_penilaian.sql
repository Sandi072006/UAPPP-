-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Dec 11, 2025 at 12:15 PM
-- Server version: 8.0.30
-- PHP Version: 8.1.10

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_penilaian`
--

-- --------------------------------------------------------

--
-- Table structure for table `absensi`
--

CREATE TABLE `absensi` (
  `id_absensi` int NOT NULL,
  `id_matkul` int NOT NULL,
  `npm` varchar(20) NOT NULL,
  `jumlah_pertemuan` int NOT NULL,
  `jumlah_hadir` int NOT NULL,
  `nilai_absensi` float DEFAULT NULL,
  `nilai_akhir_absensi` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `absensi`
--

INSERT INTO `absensi` (`id_absensi`, `id_matkul`, `npm`, `jumlah_pertemuan`, `jumlah_hadir`, `nilai_absensi`, `nilai_akhir_absensi`) VALUES
(1, 1, '2417052010', 15, 15, 100, 10),
(2, 2, '2417052010', 10, 10, 100, 10),
(3, 2, '24170520000', 15, 14, 93.3333, 9.33333);

-- --------------------------------------------------------

--
-- Table structure for table `ipk_mahasiswa`
--

CREATE TABLE `ipk_mahasiswa` (
  `npm` varchar(20) NOT NULL,
  `total_bobot_x_sks` float DEFAULT '0',
  `total_sks` int DEFAULT '0',
  `ipk` float DEFAULT '0',
  `jumlah_matkul` int DEFAULT '0',
  `last_updated` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- --------------------------------------------------------

--
-- Table structure for table `mahasiswa`
--

CREATE TABLE `mahasiswa` (
  `npm` varchar(20) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `jurusan` varchar(100) NOT NULL,
  `angkatan` int NOT NULL,
  `program_studi` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `mahasiswa`
--

INSERT INTO `mahasiswa` (`npm`, `nama`, `jurusan`, `angkatan`, `program_studi`) VALUES
('24170520000', 'Tesaja', 'Sastra Mesin', 2017, 'Sastra Mesin'),
('2417052010', 'Sandi GNTG', 'Ilmu Komputer', 2024, 'Sistem Informasi');

-- --------------------------------------------------------

--
-- Table structure for table `mata_kuliah`
--

CREATE TABLE `mata_kuliah` (
  `id_matkul` int NOT NULL,
  `nama_matkul` varchar(100) NOT NULL,
  `sks` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `mata_kuliah`
--

INSERT INTO `mata_kuliah` (`id_matkul`, `nama_matkul`, `sks`) VALUES
(1, 'PBOOO', 3),
(2, 'SBD', 3);

-- --------------------------------------------------------

--
-- Table structure for table `nilai_akhir_mahasiswa`
--

CREATE TABLE `nilai_akhir_mahasiswa` (
  `id_nilai` int NOT NULL,
  `id_matkul` int NOT NULL,
  `npm` varchar(20) NOT NULL,
  `nilai_akhir_total` float DEFAULT NULL,
  `huruf_matkul` varchar(2) DEFAULT NULL,
  `bobot_matkul` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `nilai_akhir_mahasiswa`
--

INSERT INTO `nilai_akhir_mahasiswa` (`id_nilai`, `id_matkul`, `npm`, `nilai_akhir_total`, `huruf_matkul`, `bobot_matkul`) VALUES
(1, 1, '2417052010', 91, 'A', 4),
(2, 2, '2417052010', 95, 'A', 4),
(3, 2, '24170520000', 94.6667, 'A', 4);

-- --------------------------------------------------------

--
-- Table structure for table `nilai_quiz`
--

CREATE TABLE `nilai_quiz` (
  `id_quiz` int NOT NULL,
  `id_matkul` int NOT NULL,
  `npm` varchar(20) NOT NULL,
  `rata_rata_quiz` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `nilai_quiz`
--

INSERT INTO `nilai_quiz` (`id_quiz`, `id_matkul`, `npm`, `rata_rata_quiz`) VALUES
(1, 1, '2417052010', 90),
(2, 2, '2417052010', 100),
(3, 2, '24170520000', 90);

-- --------------------------------------------------------

--
-- Table structure for table `nilai_quiz_detail`
--

CREATE TABLE `nilai_quiz_detail` (
  `id_quiz_detail` int NOT NULL,
  `id_matkul` int NOT NULL,
  `npm` varchar(20) NOT NULL,
  `quiz_number` int NOT NULL,
  `nilai_quiz` float NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `nilai_quiz_detail`
--

INSERT INTO `nilai_quiz_detail` (`id_quiz_detail`, `id_matkul`, `npm`, `quiz_number`, `nilai_quiz`) VALUES
(1, 1, '2417052010', 1, 90),
(2, 1, '2417052010', 2, 90),
(3, 1, '2417052010', 3, 90),
(4, 2, '2417052010', 1, 100),
(5, 2, '2417052010', 2, 100),
(6, 2, '24170520000', 1, 90),
(7, 2, '24170520000', 2, 90),
(8, 2, '24170520000', 3, 90);

-- --------------------------------------------------------

--
-- Table structure for table `nilai_responsi`
--

CREATE TABLE `nilai_responsi` (
  `id_responsi` int NOT NULL,
  `id_matkul` int NOT NULL,
  `npm` varchar(20) NOT NULL,
  `nilai_responsi` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `nilai_responsi`
--

INSERT INTO `nilai_responsi` (`id_responsi`, `id_matkul`, `npm`, `nilai_responsi`) VALUES
(1, 1, '2417052010', 90),
(2, 2, '2417052010', 90),
(3, 2, '24170520000', 100);

-- --------------------------------------------------------

--
-- Table structure for table `nilai_tugas`
--

CREATE TABLE `nilai_tugas` (
  `id_tugas` int NOT NULL,
  `id_matkul` int NOT NULL,
  `npm` varchar(20) NOT NULL,
  `rata_rata_tugas` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `nilai_tugas`
--

INSERT INTO `nilai_tugas` (`id_tugas`, `id_matkul`, `npm`, `rata_rata_tugas`) VALUES
(1, 1, '2417052010', 90),
(2, 2, '2417052010', 100),
(3, 2, '24170520000', 96.6667);

-- --------------------------------------------------------

--
-- Table structure for table `nilai_tugas_detail`
--

CREATE TABLE `nilai_tugas_detail` (
  `id_tugas_detail` int NOT NULL,
  `id_matkul` int NOT NULL,
  `npm` varchar(20) NOT NULL,
  `tugas_number` int NOT NULL,
  `nilai_tugas` float NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `nilai_tugas_detail`
--

INSERT INTO `nilai_tugas_detail` (`id_tugas_detail`, `id_matkul`, `npm`, `tugas_number`, `nilai_tugas`) VALUES
(1, 1, '2417052010', 1, 90),
(2, 1, '2417052010', 2, 90),
(3, 1, '2417052010', 3, 90),
(4, 2, '2417052010', 1, 100),
(5, 2, '2417052010', 2, 100),
(6, 2, '2417052010', 3, 100),
(7, 2, '24170520000', 1, 100),
(8, 2, '24170520000', 2, 100),
(9, 2, '24170520000', 3, 90);

-- --------------------------------------------------------

--
-- Table structure for table `nilai_uas`
--

CREATE TABLE `nilai_uas` (
  `id_uas` int NOT NULL,
  `id_matkul` int NOT NULL,
  `npm` varchar(20) NOT NULL,
  `nilai_uas` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `nilai_uas`
--

INSERT INTO `nilai_uas` (`id_uas`, `id_matkul`, `npm`, `nilai_uas`) VALUES
(1, 1, '2417052010', 90),
(2, 2, '2417052010', 90),
(3, 2, '24170520000', 100);

-- --------------------------------------------------------

--
-- Table structure for table `nilai_uts`
--

CREATE TABLE `nilai_uts` (
  `id_uts` int NOT NULL,
  `id_matkul` int NOT NULL,
  `npm` varchar(20) NOT NULL,
  `nilai_uts` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `nilai_uts`
--

INSERT INTO `nilai_uts` (`id_uts`, `id_matkul`, `npm`, `nilai_uts`) VALUES
(1, 1, '2417052010', 90),
(2, 2, '2417052010', 90),
(3, 2, '24170520000', 90);

-- --------------------------------------------------------

--
-- Table structure for table `persentase_matkul`
--

CREATE TABLE `persentase_matkul` (
  `id_persentase` int NOT NULL,
  `id_matkul` int NOT NULL,
  `persentase_absensi` float NOT NULL,
  `persentase_tugas` float NOT NULL,
  `persentase_quiz` float NOT NULL,
  `persentase_uts` float NOT NULL,
  `persentase_uas` float NOT NULL,
  `persentase_responsi` float DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `persentase_matkul`
--

INSERT INTO `persentase_matkul` (`id_persentase`, `id_matkul`, `persentase_absensi`, `persentase_tugas`, `persentase_quiz`, `persentase_uts`, `persentase_uas`, `persentase_responsi`) VALUES
(1, 1, 10, 20, 20, 20, 20, 10),
(2, 2, 10, 20, 20, 20, 20, 10);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `absensi`
--
ALTER TABLE `absensi`
  ADD PRIMARY KEY (`id_absensi`),
  ADD KEY `id_matkul` (`id_matkul`),
  ADD KEY `npm` (`npm`);

--
-- Indexes for table `ipk_mahasiswa`
--
ALTER TABLE `ipk_mahasiswa`
  ADD PRIMARY KEY (`npm`);

--
-- Indexes for table `mahasiswa`
--
ALTER TABLE `mahasiswa`
  ADD PRIMARY KEY (`npm`);

--
-- Indexes for table `mata_kuliah`
--
ALTER TABLE `mata_kuliah`
  ADD PRIMARY KEY (`id_matkul`);

--
-- Indexes for table `nilai_akhir_mahasiswa`
--
ALTER TABLE `nilai_akhir_mahasiswa`
  ADD PRIMARY KEY (`id_nilai`),
  ADD UNIQUE KEY `uniq_nilai_matkul_npm` (`id_matkul`,`npm`),
  ADD KEY `npm` (`npm`);

--
-- Indexes for table `nilai_quiz`
--
ALTER TABLE `nilai_quiz`
  ADD PRIMARY KEY (`id_quiz`),
  ADD UNIQUE KEY `uniq_matkul_npm_quiz` (`id_matkul`,`npm`),
  ADD KEY `npm` (`npm`);

--
-- Indexes for table `nilai_quiz_detail`
--
ALTER TABLE `nilai_quiz_detail`
  ADD PRIMARY KEY (`id_quiz_detail`),
  ADD KEY `id_matkul` (`id_matkul`),
  ADD KEY `npm` (`npm`);

--
-- Indexes for table `nilai_responsi`
--
ALTER TABLE `nilai_responsi`
  ADD PRIMARY KEY (`id_responsi`),
  ADD KEY `id_matkul` (`id_matkul`),
  ADD KEY `npm` (`npm`);

--
-- Indexes for table `nilai_tugas`
--
ALTER TABLE `nilai_tugas`
  ADD PRIMARY KEY (`id_tugas`),
  ADD UNIQUE KEY `uniq_matkul_npm_tugas` (`id_matkul`,`npm`),
  ADD KEY `npm` (`npm`);

--
-- Indexes for table `nilai_tugas_detail`
--
ALTER TABLE `nilai_tugas_detail`
  ADD PRIMARY KEY (`id_tugas_detail`),
  ADD KEY `id_matkul` (`id_matkul`),
  ADD KEY `npm` (`npm`);

--
-- Indexes for table `nilai_uas`
--
ALTER TABLE `nilai_uas`
  ADD PRIMARY KEY (`id_uas`),
  ADD KEY `id_matkul` (`id_matkul`),
  ADD KEY `npm` (`npm`);

--
-- Indexes for table `nilai_uts`
--
ALTER TABLE `nilai_uts`
  ADD PRIMARY KEY (`id_uts`),
  ADD KEY `id_matkul` (`id_matkul`),
  ADD KEY `npm` (`npm`);

--
-- Indexes for table `persentase_matkul`
--
ALTER TABLE `persentase_matkul`
  ADD PRIMARY KEY (`id_persentase`),
  ADD KEY `id_matkul` (`id_matkul`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `absensi`
--
ALTER TABLE `absensi`
  MODIFY `id_absensi` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `mata_kuliah`
--
ALTER TABLE `mata_kuliah`
  MODIFY `id_matkul` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `nilai_akhir_mahasiswa`
--
ALTER TABLE `nilai_akhir_mahasiswa`
  MODIFY `id_nilai` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `nilai_quiz`
--
ALTER TABLE `nilai_quiz`
  MODIFY `id_quiz` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `nilai_quiz_detail`
--
ALTER TABLE `nilai_quiz_detail`
  MODIFY `id_quiz_detail` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `nilai_responsi`
--
ALTER TABLE `nilai_responsi`
  MODIFY `id_responsi` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `nilai_tugas`
--
ALTER TABLE `nilai_tugas`
  MODIFY `id_tugas` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `nilai_tugas_detail`
--
ALTER TABLE `nilai_tugas_detail`
  MODIFY `id_tugas_detail` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `nilai_uas`
--
ALTER TABLE `nilai_uas`
  MODIFY `id_uas` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `nilai_uts`
--
ALTER TABLE `nilai_uts`
  MODIFY `id_uts` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `persentase_matkul`
--
ALTER TABLE `persentase_matkul`
  MODIFY `id_persentase` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `absensi`
--
ALTER TABLE `absensi`
  ADD CONSTRAINT `absensi_ibfk_1` FOREIGN KEY (`id_matkul`) REFERENCES `mata_kuliah` (`id_matkul`) ON DELETE CASCADE,
  ADD CONSTRAINT `absensi_ibfk_2` FOREIGN KEY (`npm`) REFERENCES `mahasiswa` (`npm`) ON DELETE CASCADE;

--
-- Constraints for table `ipk_mahasiswa`
--
ALTER TABLE `ipk_mahasiswa`
  ADD CONSTRAINT `ipk_mahasiswa_ibfk_1` FOREIGN KEY (`npm`) REFERENCES `mahasiswa` (`npm`) ON DELETE CASCADE;

--
-- Constraints for table `nilai_akhir_mahasiswa`
--
ALTER TABLE `nilai_akhir_mahasiswa`
  ADD CONSTRAINT `nilai_akhir_mahasiswa_ibfk_1` FOREIGN KEY (`id_matkul`) REFERENCES `mata_kuliah` (`id_matkul`) ON DELETE CASCADE,
  ADD CONSTRAINT `nilai_akhir_mahasiswa_ibfk_2` FOREIGN KEY (`npm`) REFERENCES `mahasiswa` (`npm`) ON DELETE CASCADE;

--
-- Constraints for table `nilai_quiz`
--
ALTER TABLE `nilai_quiz`
  ADD CONSTRAINT `nilai_quiz_ibfk_1` FOREIGN KEY (`id_matkul`) REFERENCES `mata_kuliah` (`id_matkul`) ON DELETE CASCADE,
  ADD CONSTRAINT `nilai_quiz_ibfk_2` FOREIGN KEY (`npm`) REFERENCES `mahasiswa` (`npm`) ON DELETE CASCADE;

--
-- Constraints for table `nilai_quiz_detail`
--
ALTER TABLE `nilai_quiz_detail`
  ADD CONSTRAINT `nilai_quiz_detail_ibfk_1` FOREIGN KEY (`id_matkul`) REFERENCES `mata_kuliah` (`id_matkul`) ON DELETE CASCADE,
  ADD CONSTRAINT `nilai_quiz_detail_ibfk_2` FOREIGN KEY (`npm`) REFERENCES `mahasiswa` (`npm`) ON DELETE CASCADE;

--
-- Constraints for table `nilai_responsi`
--
ALTER TABLE `nilai_responsi`
  ADD CONSTRAINT `nilai_responsi_ibfk_1` FOREIGN KEY (`id_matkul`) REFERENCES `mata_kuliah` (`id_matkul`) ON DELETE CASCADE,
  ADD CONSTRAINT `nilai_responsi_ibfk_2` FOREIGN KEY (`npm`) REFERENCES `mahasiswa` (`npm`) ON DELETE CASCADE;

--
-- Constraints for table `nilai_tugas`
--
ALTER TABLE `nilai_tugas`
  ADD CONSTRAINT `nilai_tugas_ibfk_1` FOREIGN KEY (`id_matkul`) REFERENCES `mata_kuliah` (`id_matkul`) ON DELETE CASCADE,
  ADD CONSTRAINT `nilai_tugas_ibfk_2` FOREIGN KEY (`npm`) REFERENCES `mahasiswa` (`npm`) ON DELETE CASCADE;

--
-- Constraints for table `nilai_tugas_detail`
--
ALTER TABLE `nilai_tugas_detail`
  ADD CONSTRAINT `nilai_tugas_detail_ibfk_1` FOREIGN KEY (`id_matkul`) REFERENCES `mata_kuliah` (`id_matkul`) ON DELETE CASCADE,
  ADD CONSTRAINT `nilai_tugas_detail_ibfk_2` FOREIGN KEY (`npm`) REFERENCES `mahasiswa` (`npm`) ON DELETE CASCADE;

--
-- Constraints for table `nilai_uas`
--
ALTER TABLE `nilai_uas`
  ADD CONSTRAINT `nilai_uas_ibfk_1` FOREIGN KEY (`id_matkul`) REFERENCES `mata_kuliah` (`id_matkul`) ON DELETE CASCADE,
  ADD CONSTRAINT `nilai_uas_ibfk_2` FOREIGN KEY (`npm`) REFERENCES `mahasiswa` (`npm`) ON DELETE CASCADE;

--
-- Constraints for table `nilai_uts`
--
ALTER TABLE `nilai_uts`
  ADD CONSTRAINT `nilai_uts_ibfk_1` FOREIGN KEY (`id_matkul`) REFERENCES `mata_kuliah` (`id_matkul`) ON DELETE CASCADE,
  ADD CONSTRAINT `nilai_uts_ibfk_2` FOREIGN KEY (`npm`) REFERENCES `mahasiswa` (`npm`) ON DELETE CASCADE;

--
-- Constraints for table `persentase_matkul`
--
ALTER TABLE `persentase_matkul`
  ADD CONSTRAINT `persentase_matkul_ibfk_1` FOREIGN KEY (`id_matkul`) REFERENCES `mata_kuliah` (`id_matkul`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
