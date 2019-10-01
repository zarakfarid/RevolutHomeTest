# Revolut Home Test

The Application is based on Jetty and Jersey, by running the application it will intialize the in-memory H-2 database for persistence and insert a small data set of account information, which can be used for testing purposes. Jetty Server will be deployed on port 8080. 

## Getting Started

Download and Extract the Zip from gitHub. Go to the unzipped folder and execute the following command to run the application.

```
java -jar target/RevolutTest-jar-with-dependencies.jar
```

You can also change and compile the code with the help of the assembly plugin by running the command in the root folder

```
mvn clean compile assembly:single
```

This will create a fat-jar "RevolutTest-jar-with-dependencies.jar", which you can test. (maven and java should be installed on your operating system).

![](media/execute.gif)

## Test Dataset

On running the application it will insert a test dataset of 50 records which can be used for testing purposes. The dataset is present as json's, in a text file (src/main/resources/testData.txt).

## Executing Requests/Tests

The application exposes 3 RESTful API's, 2 of them are just for testing and verification purposes.

1. Transaction Commit service will transfer money from one account to another. 
```
//POST
http://localhost:8080/rev/v1/transactionCommit
```
Request Body (The request body should look like this):
```
{
  "from": "DE51907620994094703982",
  "to": "DE34516183496220590123",
  "amount": "55"
}
```
Response: (The response payload is a string with the transaction Id)
```
Transaction Started with Id:1
or
Transaction Queued with Id:1
```


2. Transaction Status service will provide you with the current status of the transaction with the help of the transaction Id.
```
//GET
http://localhost:8080/rev/v1/transactionStatus?transactionId=1
```
Response: (The response payload is a string with the transaction status, there are 3 types of response)
```
Transaction Successful or 
Transaction In-Process or 
Transaction Failed
```


3. Account Info service will provide you with the details of the current account (you can verify if your transaction has successfully ended and the money amount has been transferred).
```
//GET
http://localhost:8080/rev/v1/accountInfo?accountNumber=DE51907620994094703982
```
Response: (The response payload is a JSON with the account information)
```
{
  "name": "Michael",
  "accountNumber": "DE51907620994094703982",
  "address": "264-4156 Magna St.",
  "money": 394.82 
}
```

You can run tests by selecting any two accounts from the test dataset (testData.txt or the ones above) and transfer money from one account to the other, as a response you will be given a transaction id, you can take that id and verify your request by running transactionStatus service or you can see the change directly in the users account with the accountInfo service.

## Design

Due to consistency issues and high availablity, I have designed the application in a way that if a request with the same account information transfers money, it will be pushed into a queue if not it will be forwarded to a thread pool where it will be assigned a thread for executing that transaction (For testing purposes I have made the thread pool size set to 10 everything else will be queued automatically). 

The basic verification for the bank account is really simple I only check if the IBAN is length 22 and starts with DE (Bank Accounts in Germany), In real systems this will be something more complex (I Just implemented a simpler one for now).

In reality when a transaction finishes (the amount is transferred), the application should notify the user or the front-end.

