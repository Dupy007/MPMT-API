-- Création de la base de donnée
CREATE DATABASE IF NOT EXISTS mpmt;
-- Création de l'utilisateur
CREATE USER if not exists 'mpmt'@'localhost' identified by 'mpmt';
GRANT ALL ON mpmt.* TO 'mpmt'@'localhost' WITH GRANT OPTION;
FLUSH PRIVILEGES;