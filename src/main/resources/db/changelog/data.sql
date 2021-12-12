/* 
 * Developer: Emmanuel Israel
 * Licensed: MIT
 */
/**
 * Author:  Emmanuel W. Israel <emmanuel.israel@imalipay.com>
 * Created: Dec 10, 2021
 */
CREATE TABLE IF NOT EXISTS drones (
  "id" INT AUTO_INCREMENT PRIMARY KEY,
  "serial_number" VARCHAR(100) NOT NULL UNIQUE,
  "model" VARCHAR(50) NOT NULL,
  "weight" FLOAT NOT NULL,
  "battery_capacity" INT NOT NULL,
  "drone_state" VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS medications (
  "id" INT AUTO_INCREMENT PRIMARY KEY,
  "med_name" VARCHAR(100) NOT NULL,
  "weight" FLOAT NOT NULL,
  "code" VARCHAR(50) NOT NULL UNIQUE,
  "image" VARCHAR(MAX) NOT NULL
);


INSERT INTO medications("med_name","weight","code","image")
VALUES('Rialpixel',70,'ASDXFC56','http://placeimg.com/640/480'),
('panelmicrochip',170,'KJHG456','http://placeimg.com/640/480'),
('Cornersmicrochip',70,'RTE4577','http://placeimg.com/640/480'),
('Kronealarm',109,'JHG23454','http://placeimg.com/640/480'),
('Mobilitycard',70,'WERTY54','http://placeimg.com/640/480'),
('JBODpanel',70,'R45TRGH','http://placeimg.com/640/480'),
('Pantssensor',90,'GHFDH457','http://placeimg.com/640/480'),
('Cottonmonitor',40,'XZCVB57IU','http://placeimg.com/640/480'),
('Seniorpixel',150,'5S4DTCYVH','http://placeimg.com/640/480'),
('AccountAvon',70,'WEXCGHVY7','http://placeimg.com/640/480'),
('mobileWyoming',100,'HRZE568G','http://placeimg.com/640/480'),
('FacilitatorBerkshire',50,'ZXCTVIU7','http://placeimg.com/640/480'),
('COMQuality',120,'RX54OUPI','http://placeimg.com/640/480'),
('Ariaryreboot',160,'BPVCUXF7','http://placeimg.com/640/480'),
('engageup',200,'ZX6CUVI0','http://placeimg.com/640/480'),
('Keyboardinfomediaries',100,'KJH76664','http://placeimg.com/640/480'),
('protocoloptical',70,'VXEZ56YU','http://placeimg.com/640/480');


INSERT INTO drones ("serial_number","model","weight","battery_capacity","drone_state")
VALUES(LEFT(UUID(), 15),'LIGHTWEIGHT',125,100,'IDLE'),
(LEFT(UUID(), 15),'LIGHTWEIGHT',125,100,'IDLE'),
(LEFT(UUID(), 15),'MIDDLEWEIGHT',250,100,'IDLE'),
(LEFT(UUID(), 15),'MIDDLEWEIGHT',240,100,'IDLE'),
(LEFT(UUID(), 15),'CRUISERWEIGHT',360,100,'IDLE'),
(LEFT(UUID(), 15),'LIGHTWEIGHT',100,100,'IDLE'),
(LEFT(UUID(), 15),'HEAVYWEIGHT',500,100,'IDLE'),
(LEFT(UUID(), 15),'CRUISERWEIGHT',318,100,'IDLE'),
(LEFT(UUID(), 15),'HEAVYWEIGHT',450,100,'IDLE');