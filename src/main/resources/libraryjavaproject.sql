-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 01 Mar 2023, 02:05
-- Wersja serwera: 10.4.27-MariaDB
-- Wersja PHP: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `libraryjavaproject`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `tbook`
--

CREATE TABLE `tbook` (
  `id` int(11) NOT NULL,
  `title` varchar(70) NOT NULL,
  `author` varchar(50) NOT NULL,
  `isbn` bigint(14) NOT NULL,
  `is_rented` tinyint(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Zrzut danych tabeli `tbook`
--

INSERT INTO `tbook` (`id`, `title`, `author`, `isbn`, `is_rented`) VALUES
(1, 'Harry Potter and the Philosopher’s Stone (1997)', 'J. K. Rowling', 1111111111111, 0),
(2, 'Harry Potter and the Chamber of Secrets (1998)', 'J. K. Rowling', 1111111122222, 1),
(3, 'Harry Potter and the Prisoner of Azkaban (1999)', 'J. K. Rowling', 1111111133333, 0),
(4, 'The Warded Man (2008)', 'Peter V. Brett', 2222222211111, 0),
(5, 'The Desert Spear (2010)', 'Peter V. Brett', 2222222222222, 0),
(6, 'The Warded Man Part 2 (2008)', 'Peter V. Brett', 2222222233333, 1),
(7, 'Solaris (1961)', 'Stanislaw Lem', 3333333311111, 1);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `tbookrenter`
--

CREATE TABLE `tbookrenter` (
  `id` int(11) NOT NULL,
  `renter_id` int(11) NOT NULL,
  `book_id` int(11) NOT NULL,
  `rent_date` date NOT NULL,
  `return_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Zrzut danych tabeli `tbookrenter`
--

INSERT INTO `tbookrenter` (`id`, `renter_id`, `book_id`, `rent_date`, `return_date`) VALUES
(1, 3, 2, '2022-11-22', '2022-12-06'),
(3, 3, 7, '2023-02-25', '2023-03-11'),
(6, 4, 6, '2022-07-17', '2022-07-31');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `tuser`
--

CREATE TABLE `tuser` (
  `id` int(11) NOT NULL,
  `login` varchar(30) NOT NULL,
  `password` varchar(33) NOT NULL,
  `name` varchar(40) NOT NULL,
  `surname` varchar(40) NOT NULL,
  `role` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Zrzut danych tabeli `tuser`
--

INSERT INTO `tuser` (`id`, `login`, `password`, `name`, `surname`, `role`) VALUES
(1, 'root', 'f2c0da939878247ef0a36457abef6b4c', 'Krzysztof', 'Kolega', 'ADMIN'),
(3, 'jan', '97be997c4fbc8fca330ee5c2fc839b28', 'Jan', 'Kowalski', 'USER'),
(4, 'maciek', '3de5ec258b40e57de9e9d38f378d0022', 'Maciek', 'Nowak', 'USER'),
(5, 'zbyszek', '375eff8d5f6b10d65483938069d4f931', 'Zbyszek', 'Kowal', 'USER');

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `tbook`
--
ALTER TABLE `tbook`
  ADD PRIMARY KEY (`id`);

--
-- Indeksy dla tabeli `tbookrenter`
--
ALTER TABLE `tbookrenter`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `book_id` (`book_id`);

--
-- Indeksy dla tabeli `tuser`
--
ALTER TABLE `tuser`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `login` (`login`);

--
-- AUTO_INCREMENT dla zrzuconych tabel
--

--
-- AUTO_INCREMENT dla tabeli `tbook`
--
ALTER TABLE `tbook`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT dla tabeli `tbookrenter`
--
ALTER TABLE `tbookrenter`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT dla tabeli `tuser`
--
ALTER TABLE `tuser`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
