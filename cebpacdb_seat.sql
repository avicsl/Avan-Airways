-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: cebpacdb
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `seat`
--

DROP TABLE IF EXISTS `seat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `seat` (
  `Seat_ID` varchar(10) NOT NULL,
  `Flight_ID` varchar(7) NOT NULL,
  `Seat_Number` varchar(7) NOT NULL,
  `Seat_Class` enum('First Class','Business Class','Economy Class') NOT NULL,
  PRIMARY KEY (`Seat_ID`),
  UNIQUE KEY `Seat_Number` (`Seat_Number`),
  KEY `Flight_ID` (`Flight_ID`),
  CONSTRAINT `seat_ibfk_1` FOREIGN KEY (`Flight_ID`) REFERENCES `flight` (`flight_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `seat`
--

LOCK TABLES `seat` WRITE;
/*!40000 ALTER TABLE `seat` DISABLE KEYS */;
INSERT INTO `seat` VALUES ('AAS-011','AAF-002','B32CN','Business Class'),('AAS-012','AAF-002','F24HZ','First Class'),('AAS-013','AAF-002','B50DF','Business Class'),('AAS-014','AAF-002','B13OK','Business Class'),('AAS-015','AAF-002','B08CE','Business Class'),('AAS-016','AAF-002','B75ZM','Business Class'),('AAS-017','AAF-002','B27KE','Business Class'),('AAS-018','AAF-002','B53NY','Business Class'),('AAS-019','AAF-002','B78BY','Business Class'),('AAS-020','AAF-002','B22LO','Business Class'),('AAS-021','AAF-002','B86XZ','Business Class'),('AAS-022','AAF-002','B88MV','Business Class'),('AAS-023','AAF-002','B75LX','Business Class'),('AAS-024','AAF-002','B02IQ','Business Class'),('AAS-025','AAF-002','B79CE','Business Class'),('AAS-026','AAF-002','B82GR','Business Class');
/*!40000 ALTER TABLE `seat` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-03 23:10:48
