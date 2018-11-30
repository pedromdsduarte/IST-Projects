DROP DATABASE IF EXISTS northwind_dw;
CREATE DATABASE northwind_dw;


USE northwind_dw;


-- Customer with Company name, city and country
CREATE TABLE dim_customer (
	CustomerID VARCHAR(5),

	CompanyName VARCHAR(40),
	City VARCHAR(15),
	Country VARCHAR(15),

	PRIMARY KEY (CustomerID)
);


-- Product with product name and category name (slowly-changing dimension)
CREATE TABLE dim_product (
	ProductKey INT,

	ProductID INT,
	ProductName VARCHAR(40),
	CategoryName VARCHAR(15),
	VERSION INT,
	DATE_FROM DATETIME,
	DATE_TO DATETIME,

	PRIMARY KEY (ProductKey)
);
 

-- Supplier with company name, city and country
CREATE TABLE dim_supplier (
	SupplierID INT,

	CompanyName VARCHAR(40),
	City VARCHAR(15),
	Country VARCHAR(15),

	PRIMARY KEY (SupplierID)
);


-- Time with day, month and year
CREATE TABLE dim_time (
	TimeID DATETIME,

	DayID INT,
	MonthID INT,
	MonthName VARCHAR(4),
	YearID INT,

	PRIMARY KEY (TimeID)
);


-- Fact table
-- Measures: Sales and Quantity
CREATE TABLE fact_order (
	OrderID INT,
	ProductID INT,	-- ProductID ou ProductKey (technical key)???

	Sales DOUBLE,
	Quantity INT,

	CustomerID VARCHAR(5),
	SupplierID INT,
	ProductKey INT,
	TimeID DATETIME,

	PRIMARY KEY (OrderID,ProductID),
	FOREIGN KEY (CustomerID) REFERENCES dim_customer (CustomerID),
	FOREIGN KEY (SupplierID) REFERENCES dim_supplier (SupplierID),
	FOREIGN KEY (ProductKey) REFERENCES dim_product (ProductKey),
	FOREIGN KEY (TimeID) REFERENCES dim_time (TimeID)
);

