-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 04, 2025 at 09:25 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `toko_retail`
--

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `category` varchar(50) DEFAULT NULL,
  `stock` int(11) DEFAULT NULL,
  `price` double DEFAULT NULL,
  `supplier` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `name`, `category`, `stock`, `price`, `supplier`) VALUES
(1, 'Laptop Syariah', 'Electronics', 2, 6500000, 'Bapak Arudi'),
(2, 'Samsung LightSaber', 'Electronics', 40, 150000, 'Kamar Arudi'),
(3, 'POCO F5 Mecca', 'Electronics', 98, 5, 'Xiaomi'),
(4, 'Kemeja Polos H&M', 'Clothing', 49, 1500000, 'H&M'),
(5, 'Laptop Loading Lama', 'Electronics', 10, 12000000, 'Bapak Wahyu'),
(6, 'Laptop Gaming ROG Strix G15', 'Electronics', 14, 22500000, 'PT Asus Indonesia'),
(7, 'Smartphone Samsung Galaxy A55', 'Electronics', 55, 6800000, 'PT Samsung Electronics Indonesia'),
(8, 'Headphone Sony WH-1000XM5', 'Electronics', 30, 4500000, 'PT Sony Indonesia'),
(9, 'Charger Anker PowerPort III 65W', 'Electronics', 75, 350000, 'Distributor Anker Resmi'),
(10, 'Power Bank Xiaomi 20000mAh', 'Electronics', 60, 280000, 'PT Erajaya Swasembada'),
(11, 'Smartwatch Apple Watch SE', 'Electronics', 25, 5200000, 'iBox Indonesia'),
(12, 'LED TV LG 43 inch UHD', 'Electronics', 20, 5800000, 'PT LG Electronics Indonesia'),
(13, 'Bluetooth Speaker JBL Flip 6', 'Electronics', 40, 1800000, 'PT IMS Indonesia'),
(14, 'Mouse Logitech G Pro X Superlight', 'Electronics', 50, 1600000, 'PT Logitech Indonesia'),
(15, 'Keyboard Mechanical Keychron K2', 'Electronics', 35, 1350000, 'Distributor Keychron ID'),
(16, 'Kabel USB-C UGREEN 2M', 'Electronics', 100, 85000, 'UGREEN Official Store'),
(17, 'Webcam Logitech C920', 'Electronics', 40, 750000, 'PT Logitech Indonesia'),
(18, 'Kaos Polos Cotton Combed 30s Hitam', 'Clothing', 150, 65000, 'CV Kaos Kita'),
(19, 'Kemeja Batik Pria Lengan Panjang', 'Clothing', 70, 250000, 'Batik Keris Official'),
(20, 'Celana Jeans Pria Slim Fit Biru Tua', 'Clothing', 90, 320000, 'Levi\'s Store Indonesia'),
(21, 'Gaun Pesta Wanita Merah Marun', 'Clothing', 25, 550000, 'Butik Elegan'),
(22, 'Jaket Hoodie Pria Abu-abu', 'Clothing', 110, 180000, 'PT Fashion Maju'),
(23, 'Hijab Segi Empat Voal Motif Bunga', 'Clothing', 200, 45000, 'Hijab Cantik Store'),
(24, 'Daster Batik Ibu Rumah Tangga', 'Clothing', 120, 75000, 'Pasar Klewer Solo'),
(25, 'Celana Chino Pria Coklat', 'Clothing', 80, 280000, 'Uniqlo Indonesia'),
(26, 'Baju Koko Pria Putih Bordir', 'Clothing', 60, 220000, 'Busana Muslim Barokah'),
(27, 'Rok Plisket Wanita Hitam', 'Clothing', 75, 150000, 'PT Gaya Busana'),
(28, 'Pena Pilot G2 0.5mm Hitam (1 Lusin)', 'Stationery', 250, 120000, 'PT Standardpen Industries'),
(29, 'Buku Tulis Sinar Dunia 38 Lembar (1 Pak)', 'Stationery', 300, 35000, 'PT Pabrik Kertas Tjiwi Kimia'),
(30, 'Pensil Faber-Castell 2B (1 Kotak)', 'Stationery', 180, 30000, 'PT Faber-Castell International Indonesia'),
(31, 'Penghapus Steadler Putih Besar', 'Stationery', 220, 5000, 'Distributor ATK Jaya'),
(32, 'Penggaris Butterfly 30cm Plastik', 'Stationery', 150, 3000, 'CV ATK Lengkap'),
(33, 'Spidol Snowman Permanen Hitam', 'Stationery', 100, 8000, 'PT Snowman Indonesia'),
(34, 'Sticky Notes Post-it Kuning 76x76mm', 'Stationery', 90, 25000, '3M Indonesia Store'),
(35, 'Cutter Kenko L-500 Besar', 'Stationery', 70, 12000, 'PT Kenko Indonesia'),
(36, 'Lem Kertas Glukol Stik', 'Stationery', 130, 4000, 'Distributor Glukol'),
(37, 'Map Kertas Folio Biru (10 Pcs)', 'Stationery', 160, 15000, 'CV Kertas Jaya'),
(38, 'Mie Instan Indomie Goreng (1 Dus)', 'Food', 80, 105000, 'PT Indofood CBP Sukses Makmur'),
(39, 'Biskuit Roma Kelapa 300g', 'Food', 120, 12000, 'PT Mayora Indah Tbk'),
(40, 'Kopi Kapal Api Special Mix (1 Renteng)', 'Food', 200, 13000, 'PT Santos Jaya Abadi'),
(41, 'Teh Celup SariWangi Asli (Kotak Isi 25)', 'Food', 150, 7000, 'PT Unilever Indonesia Tbk'),
(42, 'Beras Sania Premium 5kg', 'Food', 90, 68000, 'PT Wilmar Padi Indonesia'),
(43, 'Minyak Goreng Bimoli Spesial 2L Pouch', 'Food', 70, 38000, 'PT Salim Ivomas Pratama Tbk'),
(44, 'Gula Pasir Gulaku Premium 1kg', 'Food', 100, 17000, 'PT Sugar Labinta'),
(45, 'Sarden ABC Saus Tomat 155g', 'Food', 130, 9000, 'PT Heinz ABC Indonesia'),
(46, 'Susu Kental Manis Frisian Flag Kaleng', 'Food', 85, 11000, 'PT Frisian Flag Indonesia'),
(47, 'Kecap Manis Bango Botol 275ml', 'Food', 110, 18000, 'PT Unilever Indonesia Tbk'),
(48, 'Novel \"Laskar Pelangi\" Andrea Hirata', 'Books', 40, 75000, 'Penerbit Bentang Pustaka'),
(49, 'Buku Pelajaran Matematika SMA Kelas X Kurikulum Merdeka', 'Books', 60, 95000, 'Penerbit Erlangga'),
(50, 'Komik \"One Piece\" Vol. 100', 'Books', 30, 40000, 'Elex Media Komputindo'),
(51, 'Buku Anak \"Cerita Nabi Muhammad SAW\"', 'Books', 50, 55000, 'Mizan Pustaka'),
(52, 'Kamus Inggris-Indonesia John Echols', 'Books', 25, 120000, 'Gramedia Pustaka Utama'),
(53, 'Buku Resep Masakan Nusantara', 'Books', 35, 88000, 'PT Buku Kita'),
(54, 'Deterjen Rinso Anti Noda 770g', 'Household', 95, 25000, 'PT Unilever Indonesia Tbk'),
(55, 'Sabun Mandi Lifebuoy Total 10 (4 Pcs)', 'Household', 140, 18000, 'PT Unilever Indonesia Tbk'),
(56, 'Shampoo Pantene Anti Ketombe 135ml', 'Household', 80, 22000, 'PT Procter & Gamble Home Products Indonesia'),
(57, 'Pasta Gigi Pepsodent Herbal 190g', 'Household', 110, 15000, 'PT Unilever Indonesia Tbk'),
(58, 'Lampu LED Philips 12W Putih', 'Household', 70, 35000, 'PT Philips Indonesia'),
(59, 'Baterai ABC Alkaline AA (Isi 4)', 'Household', 160, 16000, 'PT International Chemical Industry'),
(60, 'Pembersih Lantai Super Pell Lemon 770ml', 'Household', 60, 13000, 'PT Unilever Indonesia Tbk'),
(61, 'Obat Nyamuk Bakar Baygon Jumbo', 'Household', 100, 15000, 'PT Johnson Home Hygiene Products'),
(62, 'Hand Sanitizer Antis Jeruk Nipis 60ml', 'Health & Beauty', 120, 12000, 'PT Enesis Group'),
(63, 'Masker Medis Sensi Duckbill (Isi 50)', 'Health & Beauty', 90, 35000, 'PT Arista Latindo'),
(64, 'Vitamin C IPI Tablet (Botol Isi 45)', 'Health & Beauty', 200, 8000, 'PT Supra Ferbindo Farma'),
(65, 'Body Lotion Marina UV White 200ml', 'Health & Beauty', 70, 18000, 'PT Tempo Scan Pacific Tbk'),
(66, 'Minyak Kayu Putih Cap Lang 60ml', 'Health & Beauty', 80, 25000, 'PT Eagle Indo Pharma'),
(67, 'Pembalut Wanita Laurier Relax Night Isi 10', 'Health & Beauty', 100, 17000, 'PT Kao Indonesia');

-- --------------------------------------------------------

--
-- Table structure for table `sales`
--

CREATE TABLE `sales` (
  `sale_id` int(11) NOT NULL,
  `sale_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `total_amount` double NOT NULL,
  `cashier_name` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sales`
--

INSERT INTO `sales` (`sale_id`, `sale_date`, `total_amount`, `cashier_name`) VALUES
(1, '2025-06-04 15:43:40', 10000, 'Point of Sale - Cashier'),
(2, '2025-06-04 15:45:11', 24000, 'Point of Sale - Cashier'),
(3, '2025-06-04 15:46:57', 60000, 'Point of Sale - Cashier'),
(4, '2025-06-04 16:55:43', 1500000, 'Point of Sale - Cashier'),
(5, '2025-06-04 18:50:27', 1512000, 'Point of Sale - Cashier'),
(6, '2025-06-04 19:21:35', 22500000, 'Point of Sale - Cashier');

-- --------------------------------------------------------

--
-- Table structure for table `sale_items`
--

CREATE TABLE `sale_items` (
  `sale_item_id` int(11) NOT NULL,
  `sale_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity_sold` int(11) NOT NULL,
  `price_per_unit` double NOT NULL,
  `subtotal` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sale_items`
--

INSERT INTO `sale_items` (`sale_item_id`, `sale_id`, `product_id`, `quantity_sold`, `price_per_unit`, `subtotal`) VALUES
(1, 1, 3, 2, 5000, 10000),
(2, 2, 1, 2, 12000, 24000),
(3, 3, 1, 5, 12000, 60000),
(4, 4, 2, 10, 150000, 1500000),
(5, 5, 1, 1, 12000, 12000),
(6, 5, 4, 1, 1500000, 1500000),
(7, 6, 6, 1, 22500000, 22500000);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `sales`
--
ALTER TABLE `sales`
  ADD PRIMARY KEY (`sale_id`);

--
-- Indexes for table `sale_items`
--
ALTER TABLE `sale_items`
  ADD PRIMARY KEY (`sale_item_id`),
  ADD KEY `idx_sale_items_sale_id` (`sale_id`),
  ADD KEY `idx_sale_items_product_id` (`product_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=68;

--
-- AUTO_INCREMENT for table `sales`
--
ALTER TABLE `sales`
  MODIFY `sale_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT for table `sale_items`
--
ALTER TABLE `sale_items`
  MODIFY `sale_item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `sale_items`
--
ALTER TABLE `sale_items`
  ADD CONSTRAINT `sale_items_ibfk_1` FOREIGN KEY (`sale_id`) REFERENCES `sales` (`sale_id`) ON DELETE CASCADE,
  ADD CONSTRAINT `sale_items_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
