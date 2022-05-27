-- phpMyAdmin SQL Dump
-- version 4.9.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost:8889
-- Generation Time: May 27, 2022 at 02:49 PM
-- Server version: 5.7.26
-- PHP Version: 7.4.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `virtualbookcase`
--

-- --------------------------------------------------------

--
-- Table structure for table `book`
--

CREATE TABLE `book` (
  `id` int(11) NOT NULL,
  `available` bit(1) NOT NULL,
  `cond` int(11) NOT NULL DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `local_id` varchar(64) NOT NULL,
  `book_descriptionfk` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `book`
--

INSERT INTO `book` (`id`, `available`, `cond`, `created`, `local_id`, `book_descriptionfk`) VALUES
(1, b'0', 0, '2021-02-10 00:34:52', 'Z14', 1),
(2, b'1', 0, '2021-02-10 00:34:52', 'Z14', 1),
(3, b'0', 0, '2021-03-09 13:57:10', '3c', 5),
(4, b'1', 0, '2021-02-10 00:34:52', 'Z14', 1),
(5, b'1', 0, '2021-02-10 00:34:52', 'Z14', 1),
(6, b'1', 0, '2021-03-09 13:57:10', '3c', 5),
(7, b'1', 0, '2021-02-10 00:34:52', 'Z14', 1),
(8, b'1', 0, '2021-02-10 00:34:52', 'Z14', 5),
(9, b'1', 0, '2021-03-09 13:57:10', '3c', 5),
(10, b'0', 0, '2021-03-09 14:05:20', '4F', 2),
(11, b'0', 0, '2021-03-09 14:05:20', '4F', 2),
(13, b'0', 0, '2021-03-09 14:05:20', '4F', 2),
(14, b'1', 0, '2021-03-09 14:09:25', '7P', 4),
(15, b'1', 0, '2021-03-09 14:09:25', '7P', 4),
(16, b'1', 0, '2021-03-09 14:09:25', '7P', 4);

-- --------------------------------------------------------

--
-- Table structure for table `bookdescription`
--

CREATE TABLE `bookdescription` (
  `id` int(11) NOT NULL,
  `author_firstname` varchar(128) NOT NULL,
  `author_lastname` varchar(128) NOT NULL,
  `comment` text,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `edition_number` int(11) NOT NULL,
  `edition_year` year(4) NOT NULL,
  `editor` varchar(128) NOT NULL,
  `format` varchar(64) NOT NULL,
  `genre` varchar(64) NOT NULL,
  `isbn` varchar(13) NOT NULL,
  `title` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `bookdescription`
--

INSERT INTO `bookdescription` (`id`, `author_firstname`, `author_lastname`, `comment`, `created`, `edition_number`, `edition_year`, `editor`, `format`, `genre`, `isbn`, `title`) VALUES
(1, 'Claude', 'Abromont', NULL, '2021-02-09 20:59:53', 1, 2013, 'Fayard', 'Broché', 'Education', '9782213655727', 'Guide des formes de la musique occidentale'),
(2, 'Eriko', 'Sato', NULL, '2021-03-09 13:28:16', 1, 2008, 'Editions First', 'Livre de poche', 'Langues', '9782754006255', 'Le japonais pour les nuls'),
(3, 'Jean', 'Tulard', 'Association pour la sauvegarde des livres anciens de la bibliothèque de la Cour de cassation', '2021-03-09 13:28:16', 1, 1995, 'Ateliers Gustave Gernez', 'Agrafé', 'Histoire', '1234567891012', 'Ombres et Lumières de la Campagne d\'Egypte'),
(4, 'Gilbert', 'Guilleminault', NULL, '2021-03-09 13:36:48', 1, 1980, 'Julliard', 'broché', 'histoire', '2260001807', 'Le roman vrai de la Vème République. L\'Etat, c\'est lui. 1959-1960.'),
(5, 'Wladyslaw', 'Szpilman', NULL, '2021-03-09 13:40:46', 3, 2003, 'Robert Laffont', 'Poche', 'Témoignage', '9782266130950', 'Le pianiste');

-- --------------------------------------------------------

--
-- Table structure for table `loan`
--

CREATE TABLE `loan` (
  `id` int(11) NOT NULL,
  `comment` text,
  `extension_asked` bit(1) NOT NULL,
  `loan_end_date` date DEFAULT NULL,
  `loan_start_date` date NOT NULL,
  `bookfk` int(11) NOT NULL,
  `userfk` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `loan`
--

INSERT INTO `loan` (`id`, `comment`, `extension_asked`, `loan_end_date`, `loan_start_date`, `bookfk`, `userfk`) VALUES
(12, NULL, b'0', NULL, '2022-05-05', 10, 1),
(13, NULL, b'0', NULL, '2022-05-13', 13, 2),
(14, NULL, b'0', NULL, '2022-04-22', 11, 4),
(15, NULL, b'0', NULL, '2022-04-01', 3, 1),
(16, NULL, b'0', NULL, '2022-05-27', 1, 1),
(17, NULL, b'0', '2022-03-08', '2022-03-02', 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `preloan`
--

CREATE TABLE `preloan` (
  `id` int(11) NOT NULL,
  `pre_loan_start_date` date NOT NULL,
  `book_descriptionfk` int(11) NOT NULL,
  `userfk` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `birthdate` date NOT NULL,
  `city` varchar(256) NOT NULL,
  `country` varchar(128) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `email` varchar(256) NOT NULL,
  `firstname` varchar(256) NOT NULL,
  `lastname` varchar(256) NOT NULL,
  `login` varchar(64) NOT NULL,
  `membership` date NOT NULL,
  `password` varchar(64) NOT NULL,
  `postal_code` int(11) NOT NULL,
  `role` int(11) NOT NULL DEFAULT '2',
  `sex` int(11) NOT NULL DEFAULT '0',
  `street_name` varchar(256) NOT NULL,
  `street_nb` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `birthdate`, `city`, `country`, `created`, `email`, `firstname`, `lastname`, `login`, `membership`, `password`, `postal_code`, `role`, `sex`, `street_name`, `street_nb`) VALUES
(1, '1983-02-05', 'Grenoble', 'France', '2021-02-10 13:15:47', 'harry.potter@gmail.com', 'Harry', 'Potter', 'harry', '2021-02-10', '00000', 38000, 1, 1, 'rue de Poudlard', '1'),
(2, '1985-02-09', 'Lyon', 'France', '2021-03-08 12:47:04', 'hermione.granger@gmail.com', 'Hermione', 'Granger', 'hermione', '2021-03-08', '00000', 69007, 2, 0, 'rue de Poudlard', '2'),
(3, '1981-05-05', 'Lozann', 'France', '2021-03-08 13:21:51', 'minerva.mccgonagall@poudlard.fr', 'Minerva', 'McGonagall', 'minerva', '2021-01-03', '00000', 69320, 2, 0, 'route du paradis', '3'),
(4, '1932-07-31', 'Paris', 'France', '2021-03-08 13:23:57', 'a.dumbledore@poudlard.fr', 'Albus', 'Dumbledore', 'albus', '2020-04-02', '00000', 75016, 2, 1, 'rue du Faubourg Saint Honoré', '12');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK6o5cj6ltoxf4l8ihfdqwe8esx` (`book_descriptionfk`);

--
-- Indexes for table `bookdescription`
--
ALTER TABLE `bookdescription`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `loan`
--
ALTER TABLE `loan`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FKm191ke83kca08vfoi50nmn6l0` (`bookfk`),
  ADD KEY `FKd0ejmnk3g8pv2105s6mpxy7vp` (`userfk`);

--
-- Indexes for table `preloan`
--
ALTER TABLE `preloan`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK63ewxyuvncrtb4fvr3cy9yyw` (`book_descriptionfk`),
  ADD KEY `FKl9l4vf9d0eij8y5hecd89jcak` (`userfk`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `UK_ew1hvam8uwaknuaellwhqchhb` (`login`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `book`
--
ALTER TABLE `book`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `bookdescription`
--
ALTER TABLE `bookdescription`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `loan`
--
ALTER TABLE `loan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT for table `preloan`
--
ALTER TABLE `preloan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `book`
--
ALTER TABLE `book`
  ADD CONSTRAINT `FK6o5cj6ltoxf4l8ihfdqwe8esx` FOREIGN KEY (`book_descriptionfk`) REFERENCES `bookdescription` (`id`);

--
-- Constraints for table `loan`
--
ALTER TABLE `loan`
  ADD CONSTRAINT `FKd0ejmnk3g8pv2105s6mpxy7vp` FOREIGN KEY (`userfk`) REFERENCES `user` (`id`),
  ADD CONSTRAINT `FKm191ke83kca08vfoi50nmn6l0` FOREIGN KEY (`bookfk`) REFERENCES `book` (`id`);

--
-- Constraints for table `preloan`
--
ALTER TABLE `preloan`
  ADD CONSTRAINT `FK63ewxyuvncrtb4fvr3cy9yyw` FOREIGN KEY (`book_descriptionfk`) REFERENCES `bookdescription` (`id`),
  ADD CONSTRAINT `FKl9l4vf9d0eij8y5hecd89jcak` FOREIGN KEY (`userfk`) REFERENCES `user` (`id`);
