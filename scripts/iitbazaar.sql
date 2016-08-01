-- MySQL dump 10.13  Distrib 5.7.9, for Linux (x86_64)
--
-- Host: localhost    Database: jnosek_iitbazaar
-- ------------------------------------------------------
-- Server version	5.7.9

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item` (
  `item_number` int(11) NOT NULL AUTO_INCREMENT,
  `listing_start_date` bigint(20) NOT NULL,
  `listing_end_date` bigint(20) NOT NULL,
  `item_name` text NOT NULL,
  `description` text NOT NULL,
  `listing_user_email` varchar(254) NOT NULL,
  `item_picture` blob NOT NULL,
  `item_picture_thumbnail` blob NOT NULL,
  `item_price` varchar(25) NOT NULL,
  `category_number` int(11) DEFAULT NULL,
  PRIMARY KEY (`item_number`)
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `email` varchar(254) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `major_department` varchar(50) NOT NULL,
  `picture` blob NOT NULL,
  `picture_thumbnail` blob NOT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `watchlist`
--

DROP TABLE IF EXISTS `watchlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `watchlist` (
  `user_email` varchar(254) NOT NULL,
  `item_number` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `watchlist`
--

LOCK TABLES `watchlist` WRITE;
/*!40000 ALTER TABLE `watchlist` DISABLE KEYS */;
/*!40000 ALTER TABLE `watchlist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `category` (
  `category_number` int(11) NOT NULL AUTO_INCREMENT,
  `parent_category_number` int(11) NOT NULL,
  `category_name` text NOT NULL,
  PRIMARY KEY (`category_number`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO category values(-2,-2,' Select A Category');
INSERT INTO category values(0,-1,'Antiques, Collectibles & Art');
INSERT INTO category values(1,-1,'Books');
INSERT INTO category values(2,-1,'Cameras & Photo');
INSERT INTO category values(3,-1,'Cell Phones & Accessories');
INSERT INTO category values(4,-1,'Clothing, Shoes & Accessories');
INSERT INTO category values(5,-1,'Coins & Paper Money');
INSERT INTO category values(6,-1,'Computers/Tablets & Networking');
INSERT INTO category values(7,-1,'Consumer Electronics');
INSERT INTO category values(8,-1,'Crafts');
INSERT INTO category values(9,-1,'DVDs/Bluray & Movies');
INSERT INTO category values(10,-1,'Dolls & Bears');
INSERT INTO category values(11,-1,'Entertainment Memorabilia');
INSERT INTO category values(12,-1,'Fashion');
INSERT INTO category values(13,-1,'Gift Cards & Coupons');
INSERT INTO category values(14,-1,'Health & Beauty');
INSERT INTO category values(15,-1,'Home & Garden');
INSERT INTO category values(16,-1,'Jewelry & Watches');
INSERT INTO category values(17,-1,'Motors');
INSERT INTO category values(18,-1,'Music');
INSERT INTO category values(19,-1,'Musical Instruments & Gear');
INSERT INTO category values(20,-1,'Other');
INSERT INTO category values(21,-1,'Pet Supplies');
INSERT INTO category values(22,-1,'Pottery & Glass');
INSERT INTO category values(23,-1,'Real Estate and Rental');
INSERT INTO category values(24,-1,'Sporting Goods');
INSERT INTO category values(25,-1,'Tickets & Experiences');
INSERT INTO category values(26,-1,'Toys & Hobbies');
INSERT INTO category values(27,-1,'Video Games & Consoles');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `selling_item`
--

DROP TABLE IF EXISTS `selling_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `selling_item` (
 `item_number` int(11) NOT NULL AUTO_INCREMENT,
  `listing_start_date` bigint NOT NULL,
  `listing_end_date` bigint NOT NULL,
  `item_name` text NOT NULL,
  `description` text NOT NULL,
  `listing_user_email` varchar(254) NOT NULL,
  `item_picture` blob NOT NULL,
  `item_picture_thumbnail` blob NOT NULL,
  `item_price` varchar(25) NOT NULL,	
  PRIMARY KEY (`item_number`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `selling_item`
--

LOCK TABLES `selling_item` WRITE;
/*!40000 ALTER TABLE `selling_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `selling_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `log` (
  `user_email` varchar(254) NOT NULL,
  `log_date` bigint NOT NULL,
  `log_tag` varchar(23) NOT NULL,
  `log_message` text NOT NULL,
  `device_info` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log`
--

LOCK TABLES `log` WRITE;
/*!40000 ALTER TABLE `log` DISABLE KEYS */;
/*!40000 ALTER TABLE `log` ENABLE KEYS */;
UNLOCK TABLES;


/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-12-09  0:40:15
