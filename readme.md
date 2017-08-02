## Stocks CRUD REST application
Simple create/read/update/delete application for stocks

### Stock object
The stock object contains the fields:
1. id (Integer)
2. name (String)
3. price (Decimal) 
4. lastUpdate (Timestamp)

### Stock User Interface 
* GET / - simple UI based on thymeleaf provides with basic CRUD operations for Stock object. 

### Stock API:
* GET /api/stocks​ (get a list of stocks)

	`curl localhost:8080/api/stocks`

	Response:
	
	```json   
	[
		{
		  "id":1,
		  "name":"Twitter",
		  "price":17.09,
		  "lastUpdate":"2017-08-02 06:27:06"
		},
		{
		  "id":2,
		  "name":"Facebook",
		  "price":171.55,
		  "lastUpdate":"2017-08-02 06:27:06"
		},
		{
		  "id":3,
		  "name":"Google",
		  "price":946.56,
		  "lastUpdate":"2017-08-02 06:35:09"
		}
	]	
	```
* GET /api/stocks/1​ (get a stock by id)

	`curl localhost:8080/api/stocks`
	
	Response:
	
	```json 	
	{
	   "id":1,
	   "name":"Twitter",
	   "price":17.09,
	   "lastUpdate":"2017-08-02 06:27:06"
	}
	```

* POST /api/stocks​ (create a stock)

	`curl -X POST -v -H "Content-Type: application/json" --data '{"name": "Google", "price":946.56}' localhost:8080/api/stocks`

	Response - created stock:
	
	```json
	{
      "id":3,
      "name":"Google",
      "price":946.56,
      "lastUpdate":"2017-08-02 06:35:09"
    }
	``` 

* PUT /api/stocks/1​ (update the price of a stock by id)

	`curl -X PUT -H "Content-Type: application/json"  --data '{"price": 947.44}' localhost:8080/api/stocks/3`

	Response - updated stock:
	```json
	{
		"id":3,
		"name":"Google",
		"price":947.44,
		"lastUpdate":"2017-08-02 06:56:35"
	}
	```