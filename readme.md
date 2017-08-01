## Stocks CRUD REST application
Simple create/read/update/delete application for stocks

### Stock object
The stock object contains the fields:
1. id (Number)
2. name (String)
3. price (Amount) 
4. lastUpdate (Timestamp)

### Stock User Interface 
* GET / - simple UI based on thymeleaf provides with basic CRUD operations for Stock object. 

### Stock API:
* GET /api/stocks​ (get a list of stocks)
* GET /api/stocks/1​ (get a stock by id)
* PUT /api/stocks/1​ (update the price of a stock by id)
* POST /api/stocks​ (create a stock)
