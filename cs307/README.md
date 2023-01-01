# CS307 - Principles of Database Systems

## **1 Basic Part** 

### **1.1 API for manipulating original data**

We did not put this part inside our front-end website. It is manipulated through java 

file.

#### **1.1.1 getEnterprise:**

Get data from database. And we create enterprise class to store data. 

#### **1.1.2 writeBackEnterprise:** 

Write the data back to database. 

#### **1.1.3 addEnterprise:** 

Input: String name,String country,String city,String supply_center,String industry 

Put this data to database. 

#### **1.1.4 deleteEnterprise:** 

Input: String name 

Remove data from database. 

#### **1.1.5 updateEnterprise:** 

Input: String name,String country,String city,String supply_center,String industry 

Update data from database. 

#### **1.1.6 selectEnterprise:** 

Input: boolean id,boolean name,boolean country, boolean city, boolean supply_center,boolean 

industry, int idC,String nameC,String couC,String citC,String supC,String indC

Boolean type to confirm whether you query according to this condition. 

The following data represents the content of your query. 

Returns a result of String type. 

### **1.2 APIs for data input** 

One button **impBasic** is used to import all original data and test data. The import step 

includes 5 detail parts of implementation. They are **API to import original data, API stockIn,** 

#### **API placeOrder, API updateOrder, API deleteOrder**. 

All of them read input from given csv files and operate on legal data which can be 

used to modify database.

![1672565327815](\picture\1672565327815.png)

### **1.3 APIs for getting statistical information** 

All APIs in this part require no input parameters and return a result of statistical mag

nitude. In our implementation, you can use a button to get the answer of these counted in

formation. The APIs are listed below. 

#### **1.3.1 getAllStaffCount:** 

Return numbers of staffs of all types. 

#### **1.3.2 getContractCount:** 

Return a total number of existing contracts. 

#### **1.3.3 getOrderCount:** 

Return a number of existing orders. 

#### **1.3.4 getNeverSoldProduct:** 

Return a number of the products that have never been sold. 

#### **1.3.5 getFavouriteProductModel** 

Return the models with highest sold quantity and its exact sales.

#### **1.3.6 getAvgStockNumber:** 

Return the average quantity of remaining product models for each supply center and is rounded 

to one decimal place. 

### **1.4 APIs for searching information** 

All APIs in this part require a input parameter and return multiple columns of informa

tion related to the search. 

#### **1.4.1 getProductNumber** 

**Input:** product number 

**Output:** A row of information containing supply center the product belongs and its num

ber, model name, purchase price and quantity. 

#### **1.4.2 getContractInfo** 

**Input:** contract number 

**Output:** Contract information including its number, relating manager name, relating en

terprise name and supply center. Also includes the informatino of all orders belong to this 

contract with the product’s name, salesman’s name, quantity, unit price, estimated delivery 

date and lodgement date of each specific order. 

For all APIs with output，we are allowed to get the result with a button and displayed as a 

table. See an example below.

![1672565366807](\picture\1672565366807.png)

## **2 Advanced Part** 

### **2.1 Query the order list based on multiple parameters** 

Our front page has an orderSelect window for users to query freely which allows mul

tiple input parameters.

![1672565377247](\picture\1672565377247.png)

### **2.2 Design the Bill Module** 

We provide a bill module for users to query the bill statistics of each month. The bill 

table is shown as below, including some columns with the highest sales information in dif

ferent aspects.

![1672565386000](\picture\1672565386000.png)

### **2.3 Database Connection Pool** 

We have added 8 basic connections to the connection pool, with a maximum of 12 

connections. And you can view the usage of the connection pool on the back end. 

![1672565393969](\picture\1672565393969.png)

### **2.4 Update Order Type According to Time** 

A function and a trigger is created to judge if the inserted or updated order is already 

passed lodgement date. If it is, then straightly change its type to ”Finished”. 

![1672565403247](\picture\1672565403247.png)

### **2.5 GUI Design** 

Vue framework is used for front-end design.

![1672565411243](\picture\1672565411243.png)

### **2.6 Back-end Server** 

The springboot framework is used for back-end design. 

Lombok is used to create entity for data storage, and directly transmit it to the front 

end for data display, simulating the table structure in the database. 

Based on websocket communication, we have realized accessing our web pages in the 

LAN of campus network which allows multiple hosts to visit at the same time. 

![1672565418411](\picture\1672565418411.png)

### **2.7 User Privileges** 

Our front-end provides a user permission switching interface. You can log in to the 

manager account or the staff account. When checking the order information, different data 

will be displayed due to different permissions. Staffs are only allowed to change the rows 

related to themselves by enabling row level security on tables. 

![1672565426657](\picture\1672565426657.png)

### **2.8 Index** 

Datagrip has generated indexes on primary key and columns constrainted with con

dition unique. In addition, we add more indexed according to our specific usage such as 

index on column lodgement date in table orders to speed the searching in the function men

tioned above.
