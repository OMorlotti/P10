-- phpMyAdmin SQL Dump
-- version 4.9.3
-- https://www.phpmyadmin.net/
--
-- Host: localhost:8889
-- Generation Time: Jun 03, 2022 at 12:22 PM
-- Server version: 5.7.26
-- PHP Version: 7.4.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `virtualbookcase_test`
--

-- --------------------------------------------------------

--
-- Table structure for table `book`
--

CREATE TABLE `book` (
  `id` int(11) NOT NULL,
  `available` int(11) NOT NULL DEFAULT '1',
  `cond` int(11) NOT NULL DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `local_id` varchar(64) NOT NULL,
  `book_descriptionfk` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `book`
--

INSERT INTO `book` (`id`, `available`, `cond`, `created`, `local_id`, `book_descriptionfk`) VALUES
(1, 0, 0, '2022-05-29 13:58:32', '123ABC', 1),
(2, 0, 0, '2022-05-29 13:58:32', '123ABC', 1),
(3, 1, 0, '2022-05-29 13:58:32', '123ABC', 2);

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
(1, 'Hergé', 'Hergé', 'Blabla', '2022-05-29 13:58:32', 1, 2012, 'Casterman', '24x32', 'BD-enfant', '9781484469064', 'Tintin et le Lotus bleu'),
(2, 'Peyo', 'Peyo', 'Blabla', '2022-05-29 13:58:32', 2, 2020, 'Dupuis', '24x32', 'BD-enfant', '9782800132884', 'Les Schtroumpfs Olympiques');

-- --------------------------------------------------------

--
-- Table structure for table `loan`
--

CREATE TABLE `loan` (
  `id` int(11) NOT NULL,
  `comment` text,
  `extension_asked` bit(1) NOT NULL DEFAULT b'0',
  `loan_end_date` date DEFAULT NULL,
  `bookfk` int(11) NOT NULL,
  `userfk` int(11) NOT NULL,
  `loan_start_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
(1, '1953-08-28', 'Londres', 'UK', '2022-05-29 13:58:32', 'albus.dumbledor@poudlard.uk', 'Albus', 'Dumbledor', 'albus', '2001-02-05', '$2a$10$XEiBzaZLVM7ilRhFb0rgwOppqXxoP//r8xZfE.hWTDhrasYlamtEK', 38000, 0, 1, 'route de Poudlard', '28'),
(2, '1995-05-18', 'Londres', 'UK', '2022-05-29 13:58:32', 'hermione.granger@poudlard.uk', 'Hermione', 'Granger', 'hermione', '2010-02-05', '$2a$10$wj5d6fx7FvxALpwLusb.OeB3IQGODyJLg8sJrHoUm1wmCCkufbbma', 38000, 1, 1, 'route de Poudlard', '28'),
(3, '1997-07-12', 'londres', 'UK', '2022-06-02 15:25:54', 'harry.p@gmail.com', 'Harry', 'Potter', 'harry', '2022-03-10', '00000', 12324, 2, 0, 'rue des Embrumes', '2'),
(4, '1935-07-12', 'londres', 'UK', '2022-06-02 15:25:54', 'minerva.m@gmail.com', 'Minerva', 'McGonagall', 'minerva', '2022-03-10', '00000', 12324, 2, 0, 'rue des Embrumes', '23');

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `bookdescription`
--
ALTER TABLE `bookdescription`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT for table `loan`
--
ALTER TABLE `loan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=47;

--
-- AUTO_INCREMENT for table `preloan`
--
ALTER TABLE `preloan`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

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
