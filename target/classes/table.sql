CREATE TABLE hotel_chains
	(hotelChainID NUMERIC(2,0) PRIMARY KEY,
	 name VARCHAR(100) NOT NULL,
	 addressNumber INTEGER NOT NULL,
	 addressName VARCHAR(100) NOT NULL,
	 email VARCHAR(60) NOT NULL,
	 phone NUMERIC(10,0) NOT NULL,
	 numOfHotels INTEGER NOT NULL
	);

CREATE TABLE employees
	(ssn NUMERIC(9,0) PRIMARY KEY,
	 name VARCHAR(50) NOT NULL,
	 addressNumber INTEGER NOT NULL,
	 addressName VARCHAR(100) NOT NULL,
	 employeeID NUMERIC(10,0) UNIQUE NOT NULL,
	 hotelChainID NUMERIC(2,0) NOT NULL,
	 FOREIGN KEY (hotelChainID) references hotel_chains(hotelChainID)
	);

CREATE TABLE hotels
	(hotelID NUMERIC(3,0) PRIMARY KEY,
	 hotelChainID NUMERIC(2,0) NOT NULL,
	 stars NUMERIC(1,0) NOT NULL,
	 addressNumber INTEGER NOT NULL,
	 addressName VARCHAR(100) NOT NULL,
	 email VARCHAR(60) NOT NULL,
	 phone NUMERIC(10,0) NOT NULL,
	 numOfRooms INTEGER NOT NULL,
	 managerID NUMERIC(10,0) NOT NULL,
	 FOREIGN KEY (hotelChainID) references hotel_chains(hotelChainID),
	 FOREIGN KEY (managerID) references employees(employeeID)
	);

CREATE TYPE AMENITY AS ENUM ('TV', 'AC', 'Fridge');
CREATE TYPE VIEWTYPE AS ENUM ('Sea View', 'Mountain View');
CREATE TABLE rooms
	(roomID INTEGER PRIMARY KEY,
	 hotelID NUMERIC(3,0) NOT NULL,
	 hotelChainID NUMERIC(2,0) NOT NULL,
	 price NUMERIC(10,2) NOT NULL,
	 capacity VARCHAR(6) CHECK (capacity IN ('Single', 'Double', 'Triple', 'Quad')),
	 viewType VIEWTYPE NOT NULL,
	 amenities AMENITY[],
	 extendable BOOLEAN NOT NULL,
	 damages VARCHAR(100),
	 FOREIGN KEY (hotelID) references hotels(hotelID),
	 FOREIGN KEY (hotelChainID) references hotel_chains(hotelChainID)
	);

CREATE TABLE customers
	(ssn NUMERIC(9,0) PRIMARY KEY,
	 hotelID NUMERIC(3,0) NOT NULL,
	 hotelChainID NUMERIC(2,0) NOT NULL,
	 name VARCHAR(20) NOT NULL,
	 addressNumber INTEGER NOT NULL,
	 addressName VARCHAR(100) NOT NULL,
	 registrationDate DATE NOT NULL,
	 FOREIGN KEY (hotelID) references hotels(hotelID),
	 FOREIGN KEY (hotelChainID) references hotel_chains(hotelChainID)
	);

CREATE TABLE bookings
	(bookingID INTEGER PRIMARY KEY,
	 customerID INTEGER NOT NULL,
	 hotelID NUMERIC(3,0) NOT NULL,
	 hotelChainID NUMERIC(2,0) NOT NULL,
	 roomID INTEGER NOT NULL,
	 startDate DATE NOT NULL,
	 endDate DATE CHECK (endDate >= startDate),
	 FOREIGN KEY (hotelID) references hotels(hotelID),
	 FOREIGN KEY (hotelChainID) references hotel_chains(hotelChainID),
	 FOREIGN KEY (roomID) references rooms(roomID),
	 FOREIGN KEY (customerID) references customers(ssn)
	);

CREATE TABLE rentings
	(rentingID INTEGER PRIMARY KEY,
	 customerID INTEGER NOT NULL,
	 hotelID NUMERIC(3,0) NOT NULL,
	 hotelChainID NUMERIC(2,0) NOT NULL,
	 roomID INTEGER NOT NULL,
	 startDate DATE NOT NULL,
	 endDate DATE CHECK (endDate >= startDate),
	 FOREIGN KEY (hotelID) references hotels(hotelID),
	 FOREIGN KEY (hotelChainID) references hotel_chains(hotelChainID),
	 FOREIGN KEY (roomID) references rooms(roomID),
	 FOREIGN KEY (customerID) references customers(ssn)
	);

CREATE TABLE archives
	(archiveID INTEGER PRIMARY KEY,
	 hotelID NUMERIC(3,0) NOT NULL,
	 hotelChainID NUMERIC(2,0) NOT NULL,
	 roomID INTEGER NOT NULL,
	 startDate DATE NOT NULL,
	 endDate DATE CHECK (endDate >= startDate),
	 FOREIGN KEY (hotelID) references hotels(hotelID),
	 FOREIGN KEY (hotelChainID) references hotel_chains(hotelChainID)
	);