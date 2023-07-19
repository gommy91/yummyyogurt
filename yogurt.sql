-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 16, 2021 at 01:05 PM
-- Server version: 10.4.13-MariaDB
-- PHP Version: 7.2.32


SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";



/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `yogurt`
--


-- --------------------------------------------------------


--
-- Table structure for table `yogurt`
--


CREATE TABLE `yogurt` (
 `SellID` char(5) NOT NULL,
 `BuyerName` varchar(200) NOT NULL,
 `YogurtType` varchar(50) NOT NULL,
 `Size` char(1) NOT NULL,
 `Topping` varchar(100) NOT NULL,
 `Qty` int(11) NOT NULL,
 `TotalPrice` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Dumping data for table `yogurt`
--


INSERT INTO `yogurt` (`SellID`, `BuyerName`, `YogurtType`, `Size`, `Topping`, `Qty`, `TotalPrice`) VALUES
('SS001', 'Bryan', 'Spesial', 'S', '-', 1, 30000),
('SS002', 'Gracia', 'Spesial', 'L', 'Jelly, Chocolate, Fruits', 2, 126000),
('YS001', 'Anton', 'Standard', 'M', 'Chocolate, Fruits', 3, 127000),
('YS002', 'Ayu', 'Standard', 'L', 'Jelly', 2, 88000);


--
-- Indexes for dumped tables
--


--
-- Indexes for table `yogurt`
--
ALTER TABLE `yogurt`
 ADD PRIMARY KEY (`SellID`);
COMMIT;


/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
--