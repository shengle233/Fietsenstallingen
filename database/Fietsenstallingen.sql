-- MySQL dump 10.13  Distrib 8.0.26, for Win64 (x86_64)
--
-- Host: localhost    Database: fietsenstallingen_beheer
-- ------------------------------------------------------
-- Server version	8.0.26

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
-- Table structure for table `fietsenstalling`
--

DROP TABLE IF EXISTS `fietsenstalling`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fietsenstalling` (
  `fietsenstallingId` int NOT NULL,
  `naam` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `adres` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`fietsenstallingId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fietsenstalling`
--

LOCK TABLES `fietsenstalling` WRITE;
/*!40000 ALTER TABLE `fietsenstalling` DISABLE KEYS */;
INSERT INTO `fietsenstalling` VALUES (1,'Teststalling 1','Testadres 1, Eindhoven'),(2,'Teststalling 2','Testadres 2, Eindhoven'),(3,'Teststalling 3','Testadres 3, Eindhoven'),(4,'Teststalling 4','Testadres 4, Eindhoven'),(5,'Teststalling 5','Testadres 5, Eindhoven'),(6,'Teststalling 6','Testadres 6, Eindhoven'),(7,'Teststalling 7','Testadres 7, Eindhoven');
/*!40000 ALTER TABLE `fietsenstalling` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fietsenstalling_dataset`
--

DROP TABLE IF EXISTS `fietsenstalling_dataset`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `fietsenstalling_dataset` (
  `objectid` int NOT NULL,
  `naam_fietsenstalling` text COLLATE utf8mb4_unicode_ci,
  `adres_omschrijving` text COLLATE utf8mb4_unicode_ci,
  `type_stalling` text COLLATE utf8mb4_unicode_ci,
  `openingstijden_maandag` text COLLATE utf8mb4_unicode_ci,
  `openingstijden_dinsdag` text COLLATE utf8mb4_unicode_ci,
  `openingstijden_woensdag` text COLLATE utf8mb4_unicode_ci,
  `openingstijds_donderdag` text COLLATE utf8mb4_unicode_ci,
  `openingstijden_vrijdag` text COLLATE utf8mb4_unicode_ci,
  `openingstijden_zaterdag` text COLLATE utf8mb4_unicode_ci,
  `openingstijden_zondag` text COLLATE utf8mb4_unicode_ci,
  `meer_informatie` text COLLATE utf8mb4_unicode_ci,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `geo_shape` json DEFAULT NULL,
  `geo_point_2d` text COLLATE utf8mb4_unicode_ci,
  `geopoint` text COLLATE utf8mb4_unicode_ci,
  PRIMARY KEY (`objectid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fietsenstalling_dataset`
--

LOCK TABLES `fietsenstalling_dataset` WRITE;
/*!40000 ALTER TABLE `fietsenstalling_dataset` DISABLE KEYS */;
INSERT INTO `fietsenstalling_dataset` VALUES (1,'Fietsenstalling Heuvel','Ten Hagestraat 14, 5611 EJ Eindhoven','Gratis fietsenstalling met toezicht','08:00-22:30uur','08:00-22:30uur','08:00-22:30uur','08:00-22:30uur','08:00-4:30uur','08:00-4:30uur','08:00-22:30uur','https://www.eindhoven.nl/stad-en-wonen/verkeer/bereikbaarheid/fietsenstallingen',5.48045823,51.43785041,'{\"type\": \"Point\", \"coordinates\": [5.480458223533857, 51.43785041990533]}','51.437850419905324, 5.480458223533857','51.43785041, 5.48045823'),(2,'Fietsenstalling Winkelcentrum WoensXL','Winkelcentrum Woensel 500-510, 5625 AA Eindhoven','Gratis fietsenstalling met toezicht','08:00-19:00uur','08:00-19:00uur','08:00-19:00uur','08:00-19:00uur','08:00-21:30uur','08:00-18:00uur','alleen op koopzondagen toezicht, 11:00-18:00uur','https://www.eindhoven.nl/stad-en-wonen/verkeer/bereikbaarheid/fietsenstallingen',5.47328846,51.46978532,'{\"type\": \"Point\", \"coordinates\": [5.4732884460401, 51.46978533335247]}','51.46978533335247, 5.4732884460401','51.46978532, 5.47328846'),(3,'Fietsenstalling 18 Septemberplein','18 Septemberplein, 5611 AK, Eindhoven','Gratis fietsenstalling met toezicht','07:00-01:00uur','07:00-01:00uur','07:00-01:00uur','07:00-01:00uur','07:00-01:00uur','07:00-01:00uur','11:00-01:00uur','https://www.eindhoven.nl/stad-en-wonen/verkeer/bereikbaarheid/fietsenstallingen',5.47707071,51.44122886,'{\"type\": \"Point\", \"coordinates\": [5.477070707060177, 51.44122887025675]}','51.44122887025675, 5.477070707060177','51.44122886, 5.47707071'),(4,'Fietsenstalling NS-station zuidzijde','Stationsplein 22, 5611 AC Eindhoven','Bewaakte NS stalling (tegen betaling)','04:55-01:25uur','04:55-01:25uur','04:55-01:25uur','04:55-01:25uur','04:55-01:25uur','05:50-01:25uur','06:25-01:25uur','https://www.ns.nl/fietsenstallingen/info/eindhoven-zuidzijde',5.48039389,51.44242743,'{\"type\": \"Point\", \"coordinates\": [5.4803938804199985, 51.44242743933344]}','51.44242743933344, 5.4803938804199985','51.44242743, 5.48039389'),(5,'Fietsenstalling NS-station noordzijde','Neckerspoel, 5611 AD Eindhoven','Bewaakte NS stalling (tegen betaling)','06:30-20:00uur','06:30-20:00uur','06:30-20:00uur','06:30-20:00uur','06:30-20:00uur','gesloten','gesloten','https://www.ns.nl/fietsenstallingen/info/eindhoven-noordzijde',5.47830684,51.44333305,'{\"type\": \"Point\", \"coordinates\": [5.478306836629171, 51.44333306558167]}','51.44333306558167, 5.478306836629171','51.44333305, 5.47830684'),(6,'Pop-up fietsenstalling Kerkstraat, tijdelijke stalling zonder toezicht | 14 juni t/m 13 juli 2024','Kerkstraat, 5611 GH Eindhoven','Gratis fietsenstalling met toezicht','niet beschikbaar','niet beschikbaar','niet beschikbaar','niet beschikbaar','17:00-05:00 uur','17:00-05:00 uur','niet beschikbaar','https://www.eindhoven.nl/nieuws/proef-met-pop-up-fietsenstallingen',5.478883,51.437059,'{\"type\": \"Point\", \"coordinates\": [5.478882992782428, 51.43705901127496]}','51.43705901127496, 5.478882992782428','51.437059, 5.478883'),(7,'Pop-up fietsenstalling Mercado, tijdelijke stalling zonder toezicht | 14 juni t/m 13 juli 2024','Smalle Haven 109, 5611 EH Eindhoven','Gratis fietsenstalling met toezicht','niet beschikbaar','niet beschikbaar','niet beschikbaar','niet beschikbaar','17:00-05:00 uur','17:00-05:00 uur','niet beschikbaar','https://www.eindhoven.nl/nieuws/proef-met-pop-up-fietsenstallingen',5.481647,51.436996,'{\"type\": \"Point\", \"coordinates\": [5.481646994194696, 51.43699601116056]}','51.43699601116056, 5.481646994194696','51.436996, 5.481647');
/*!40000 ALTER TABLE `fietsenstalling_dataset` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medewerker`
--

DROP TABLE IF EXISTS `medewerker`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medewerker` (
  `medewerkerId` int NOT NULL AUTO_INCREMENT,
  `naam` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`medewerkerId`),
  UNIQUE KEY `uq_medewerker_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medewerker`
--

LOCK TABLES `medewerker` WRITE;
/*!40000 ALTER TABLE `medewerker` DISABLE KEYS */;
INSERT INTO `medewerker` VALUES (1,'Jan de Vries','jan.devries@example.org'),(2,'Lisa Jansen','lisa.jansen@example.org'),(3,'Mark Peters','mark.peters@example.org'),(4,'Sophie Bakker','sophie.bakker@example.org'),(5,'Tim van Dijk','tim.vandijk@example.org');
/*!40000 ALTER TABLE `medewerker` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `melding`
--

DROP TABLE IF EXISTS `melding`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `melding` (
  `meldingId` int NOT NULL AUTO_INCREMENT,
  `fietsenstallingId` int NOT NULL,
  `medewerker_medewerkerId` int DEFAULT NULL,
  `datum` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `omschrijving` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `probleemtype` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'OPEN',
  PRIMARY KEY (`meldingId`),
  KEY `idx_melding_medewerker` (`medewerker_medewerkerId`),
  KEY `idx_melding_stalling` (`fietsenstallingId`),
  CONSTRAINT `fk_melding_medewerker` FOREIGN KEY (`medewerker_medewerkerId`) REFERENCES `medewerker` (`medewerkerId`) ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `melding`
--

LOCK TABLES `melding` WRITE;
/*!40000 ALTER TABLE `melding` DISABLE KEYS */;
INSERT INTO `melding` VALUES (1,1,NULL,'2026-10-25 08:00:00','Lamp bij de ingang werkt niet','Verlichting','OPEN'),(2,2,4,'2026-10-26 07:00:00','Afval bij de fietsenrekken','Schoonmaak','OPEN'),(4,4,2,'2026-10-28 05:00:00','Donkere hoek bij de uitgang','Verlichting','IN_BEHANDELING'),(5,5,NULL,'2026-10-29 09:00:00','Vloer heeft veel bladeren','Schoonmaak','OPEN'),(6,6,2,'2026-10-30 08:00:00','Beschadigd informatiebord','Beschadiging','AFGEHANDELD'),(7,7,1,'2026-10-31 07:00:00','Meerdere lampen knipperen','Verlichting','IN_BEHANDELING'),(8,1,2,'2026-11-01 06:00:00','Gemors op de vloer','Schoonmaak','IN_BEHANDELING'),(9,2,3,'2026-11-02 05:00:00','Deur sluit niet goed','Beschadiging','IN_BEHANDELING'),(10,3,4,'2026-11-03 09:00:00','Noodverlichting is defect','Verlichting','IN_BEHANDELING'),(11,4,5,'2026-11-04 08:00:00','Prullenbakken zitten vol','Schoonmaak','AFGEHANDELD'),(12,5,1,'2026-11-05 07:00:00','Losse tegel bij de ingang','Beschadiging','IN_BEHANDELING'),(13,6,2,'2026-11-06 06:00:00','Verlichting bij trap is zwak','Verlichting','IN_BEHANDELING'),(14,7,3,'2026-11-07 05:00:00','Schoonmaak tijdelijk uitgesteld','Schoonmaak','ONDERBROKEN'),(15,1,4,'2026-11-08 09:00:00','Herstel fietsenrek wacht op materiaal','Beschadiging','ONDERBROKEN'),(16,2,5,'2026-11-09 08:00:00','Vervanging lamp wacht op levering','Verlichting','ONDERBROKEN'),(17,3,1,'2026-11-10 07:00:00','Afval bij ingang is verwijderd','Schoonmaak','AFGEHANDELD'),(18,4,2,'2026-11-11 06:00:00','Kapotte lamp is vervangen','Verlichting','AFGEHANDELD'),(19,5,3,'2026-11-12 05:00:00','Los onderdeel is hersteld','Beschadiging','AFGEHANDELD'),(20,6,4,'2026-11-13 09:00:00','Vloer is gereinigd','Schoonmaak','AFGEHANDELD'),(21,3,1,'2026-11-14 11:04:32','123','Verlichting','ONDERBROKEN'),(22,3,3,'2026-11-14 13:09:52','Stalling kapot','Beschadiging','IN_BEHANDELING');
/*!40000 ALTER TABLE `melding` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `taak`
--

DROP TABLE IF EXISTS `taak`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `taak` (
  `taakId` int NOT NULL AUTO_INCREMENT,
  `melding_meldingId` int NOT NULL,
  `omschrijving` text COLLATE utf8mb4_unicode_ci NOT NULL,
  `voltooid` tinyint(1) NOT NULL DEFAULT '0',
  `volgorde` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`taakId`),
  KEY `idx_taak_melding` (`melding_meldingId`),
  CONSTRAINT `fk_taak_melding` FOREIGN KEY (`melding_meldingId`) REFERENCES `melding` (`meldingId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `taak`
--

LOCK TABLES `taak` WRITE;
/*!40000 ALTER TABLE `taak` DISABLE KEYS */;
INSERT INTO `taak` VALUES (1,1,'Controleer de verlichting ter plaatse',0,1),(2,1,'Herstel of vervang de defecte lamp',0,2),(3,2,'Inspecteer de vervuilde locatie',0,1),(4,2,'Maak de locatie schoon',0,2),(7,4,'Controleer de verlichting ter plaatse',0,1),(8,4,'Herstel of vervang de defecte lamp',0,2),(9,5,'Inspecteer de vervuilde locatie',0,1),(10,5,'Maak de locatie schoon',0,2),(11,6,'Inspecteer de beschadiging',1,1),(12,6,'Herstel of vervang het beschadigde onderdeel',1,2),(13,7,'Controleer de verlichting ter plaatse',1,1),(14,7,'Herstel of vervang de defecte lamp',0,2),(15,8,'Inspecteer de vervuilde locatie',1,1),(16,8,'Maak de locatie schoon',0,2),(17,9,'Inspecteer de beschadiging',1,1),(18,9,'Herstel of vervang het beschadigde onderdeel',0,2),(19,10,'Controleer de verlichting ter plaatse',1,1),(20,10,'Herstel of vervang de defecte lamp',1,2),(21,11,'Inspecteer de vervuilde locatie',1,1),(22,11,'Maak de locatie schoon',1,2),(23,12,'Inspecteer de beschadiging',1,1),(24,12,'Herstel of vervang het beschadigde onderdeel',0,2),(25,13,'Controleer de verlichting ter plaatse',1,1),(26,13,'Herstel of vervang de defecte lamp',0,2),(27,14,'Inspecteer de vervuilde locatie',1,1),(28,14,'Maak de locatie schoon',0,2),(29,15,'Inspecteer de beschadiging',0,1),(30,15,'Herstel of vervang het beschadigde onderdeel',0,2),(31,16,'Controleer de verlichting ter plaatse',1,1),(32,16,'Herstel of vervang de defecte lamp',0,2),(33,17,'Inspecteer de vervuilde locatie',1,1),(34,17,'Maak de locatie schoon',1,2),(35,18,'Controleer de verlichting ter plaatse',1,1),(36,18,'Herstel of vervang de defecte lamp',1,2),(37,19,'Inspecteer de beschadiging',1,1),(38,19,'Herstel of vervang het beschadigde onderdeel',1,2),(39,20,'Inspecteer de vervuilde locatie',1,1),(40,20,'Maak de locatie schoon',1,2),(41,21,'Verzamelen greedschappen',1,1),(42,6,'Kosten afrekenen',1,3),(43,22,'Beschadiging inspecteren',0,1);
/*!40000 ALTER TABLE `taak` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'fietsenstallingen_beheer'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-14 14:18:11
