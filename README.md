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
### Registering a drone
> **Method**: POST

> **Endpoint**: /drone/registration
#### Sample Request
```
{
    "serialNumber":"{{$randomUUID}}",
    "weight": 500
}
```
#### Sample success respone
```
{
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
```
#### Sample failed respone
```
{
    "statusCode": "90",
    "statusMessage": "FAILED",
    "error": "Duplicate entry. A drone with serial number 113A575E-DCAA-4946-9E0D-E8BF48B1885C already registered."
}
```
### Loading a drone with medication items
>**Method:** _POST_

>**Endpoint:** /drones/load/meds
#### Sample Request
```
{
    "address": "{{$randomStreetAddress}}, {{$randomCity}}, {{$randomCountry}}",
    "droneSerialNumber": "b3badb19-a6a4-4",
    "medications": [
        {
            "code": "KJHG456"
        },
        {
            "code": "WERTY54"
        },
        {
            "code": "R45TRGH"
        },
        {
            "code": "GHFDH457"
        }
    ]
}
```
#### Sample success respone
```
{
    "statusCode": "00",
    "statusMessage": "SUCCESS",
    "data": {
        "id": 1,
        "drone": {
            "id": 3,
            "serialNumber": "b3badb19-a6a4-4",
            "model": "MIDDLEWEIGHT",
            "weight": 240.0,
            "batteryCapacity": 100,
            "state": "DELIVERING"
        },
        "trackingId": "fc53ed6a-3fb4-4dcd-946b-cdfeb315844a",
        "status": "SHIPPING",
        "address": "12103 Kody Unions, Queenborough, Palestinian Territory",
        "medications": [
            "KJHG456",
            "WERTY54",
            "R45TRGH",
            "GHFDH457"
        ],
        "createdAt": {
            "dateTime": {
                "date": {
                    "year": 2021,
                    "month": 12,
                    "day": 12
                },
                "time": {
                    "hour": 23,
                    "minute": 49,
                    "second": 20,
                    "nano": 425050300
                }
            },
            "offset": {
                "totalSeconds": 3600
            },
            "zone": {
                "id": "Africa/Lagos"
            }
        },
        "totalWeight": 400.0
    }
}
```
#### Sample failed respone
```
{
    "statusCode": "90",
    "statusMessage": "FAILED",
    "error": "Drone b3badb19-a6a4-4 cannot make delivery at this time. Drone needs to be idle with battery level above 25% to make a delivery"
}
```

### Checking loaded medication items for a given drone
> **Method**: GET

> **Endpoint**: /meds/loaded/drone/{droneSerialNumber}
#### Sample success respone
```
{
    "statusCode": "00",
    "statusMessage": "SUCCESS",
    "data": [
        {
            "id": 2,
            "name": "panelmicrochip",
            "weight": 170.0,
            "code": "KJHG456",
            "image": "http://placeimg.com/640/480"
        },
        {
            "id": 5,
            "name": "Mobilitycard",
            "weight": 70.0,
            "code": "WERTY54",
            "image": "http://placeimg.com/640/480"
        }
    ]
}
```
#### Sample failed respone
```
{
    "statusCode": "90",
    "statusMessage": "FAILED",
    "error": "Unable to locate drone with serial number - b3badb19-a6a4-5"
}
```

### Checking available drones for loading
> **Method**: GET

> **Endpoint**: /drones/list/loading
#### Sample success respone
```
{
    "statusCode": "00",
    "statusMessage": "SUCCESS",
    "data": [
        {
            "id": 1,
            "serialNumber": "958d3ebb-56e8-4",
            "model": "LIGHTWEIGHT",
            "weight": 125.0,
            "batteryCapacity": 100,
            "state": "IDLE"
        },
        {
            "id": 2,
            "serialNumber": "0c2b93a4-78d8-4",
            "model": "LIGHTWEIGHT",
            "weight": 125.0,
            "batteryCapacity": 100,
            "state": "IDLE"
        },
        {
            "id": 4,
            "serialNumber": "f599a84d-2b4e-4",
            "model": "MIDDLEWEIGHT",
            "weight": 250.0,
            "batteryCapacity": 100,
            "state": "IDLE"
        }
    ]
}
```

### Check drone battery level for a given drone
> **Method**: GET

> **Endpoint**: /drone/battery-level/{droneSerialNumber}
#### Sample success respone
```
{
    "statusCode": "00",
    "statusMessage": "SUCCESS",
    "data": {
        "serialNumber": "b3badb19-a6a4-4",
        "weight": 240.0,
        "batteryLevel": 100
    }
}
```
#### Sample failed respone
```
{
    "statusCode": "90",
    "statusMessage": "FAILED",
    "error": "Unable to locate drone with serial number - b3badb19-a6a4-48"
}
```

### List of Medication
> **Method**: GET

> **Endpoint**: /meds/list
#### Sample Response
```
{
    "statusCode": "00",
    "statusMessage": "SUCCESS",
    "data": [
        {
            "id": 1,
            "name": "Rialpixel",
            "weight": 70.0,
            "code": "ASDXFC56",
            "image": "http://placeimg.com/640/480"
        },
        {
            "id": 2,
            "name": "panelmicrochip",
            "weight": 170.0,
            "code": "KJHG456",
            "image": "http://placeimg.com/640/480"
        },
        {
            "id": 3,
            "name": "Cornersmicrochip",
            "weight": 70.0,
            "code": "RTE4577",
            "image": "http://placeimg.com/640/480"
        },
        {
            "id": 4,
            "name": "Kronealarm",
            "weight": 109.0,
            "code": "JHG23454",
            "image": "http://placeimg.com/640/480"
        }
    ]
}
```

### List of all Drones
> **Method**: GET

> **Endpoint**: /drones/list
#### Sample Response
```
{
    "statusCode": "00",
    "statusMessage": "SUCCESS",
    "data": [
        {
            "id": 1,
            "serialNumber": "958d3ebb-56e8-4",
            "model": "LIGHTWEIGHT",
            "weight": 125.0,
            "batteryCapacity": 100,
            "state": "IDLE"
        },
        {
            "id": 2,
            "serialNumber": "0c2b93a4-78d8-4",
            "model": "LIGHTWEIGHT",
            "weight": 125.0,
            "batteryCapacity": 100,
            "state": "IDLE"
        },
        {
            "id": 3,
            "serialNumber": "b3badb19-a6a4-4",
            "model": "MIDDLEWEIGHT",
            "weight": 240.0,
            "batteryCapacity": 100,
            "state": "DELIVERING"
        },
        {
            "id": 4,
            "serialNumber": "f599a84d-2b4e-4",
            "model": "MIDDLEWEIGHT",
            "weight": 250.0,
            "batteryCapacity": 100,
            "state": "IDLE"
        },
        {
            "id": 5,
            "serialNumber": "7f69bbeb-7068-4",
            "model": "CRUISERWEIGHT",
            "weight": 318.0,
            "batteryCapacity": 100,
            "state": "IDLE"
        }
    ]
}
```