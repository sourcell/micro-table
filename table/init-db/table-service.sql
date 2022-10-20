CREATE DATABASE IF NOT EXISTS `service_table` CHARACTER SET utf8 COLLATE utf8_general_ci;
CREATE USER 'oenik'@'%' IDENTIFIED BY 'oenik';
GRANT ALL PRIVILEGES ON `service_table`.* TO `oenik`@`%`;
