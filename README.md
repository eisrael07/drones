# drones

## Introduction
A restful service that allows clients to communicate with the drones to deliver small items that are (urgently) needed in locations with difficult access.

Drones is a Jave Maven project built using the Java Spring framework. Compiled with JDK version 13. H2 is drones database engine which runs locally.

## Features
Drones application is a restFul solution which exchange request and response data in **Json** format via **Rest API** calls. The solution has the following features:
- registering a drone;
- loading a drone with medication items;
- checking loaded medication items for a given drone; 
- checking available drones for loading;
- check drone battery level for a given drone;
- fetch list of medications
- fetch list of all registered drones

## Assumptions
They following assumption were made and was implemented some of which are default take on data preloaded in the database.
- A drone can lift objects at most 3 times its weight
- 9 drones of different model, on start up will be preloaded in the db.
- 17 Medications is preloaded into the db.

## API Docs.
Base url is he url prefix for every endpoint. **Base Url is "/drones/api/gateway"**
### Rgistering a drone
**Method:** _POST_
**Endpoint:** /drone/registration
#### Sample Request
```{
    "serialNumber":"{{$randomUUID}}",
    "weight": 500
}

#### Sample respone
```{
    "statusCode": "00",
    "statusMessage": "SUCCESS",
    "data": {
        "id": 10,
        "serialNumber": "113A575E-DCAA-4946-9E0D-E8BF48B1885C",
        "model": "HEAVYWEIGHT",
        "weight": 500.0,
        "batteryCapacity": 100,
        "state": "IDLE"
    }
}
