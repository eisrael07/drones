/*
 * Developer: Emmanuel Israel
 * Licensed: MIT
 */
package com.emmanuelisrael.drones.controller;

import com.emmanuelisrael.drones.constants.Endpoints;
import com.emmanuelisrael.drones.payload.DroneDTO;
import com.emmanuelisrael.drones.payload.LoadingDTO;
import com.emmanuelisrael.drones.service.ApiService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Emmanuel W. Israel <israelecricket@gmail.com>
 */
@RestController
@RequestMapping("/drones/api/gateway")
public class DispatchController {

    @Autowired
    ApiService apiService;

    @PostMapping(value = Endpoints.REGISTER_DRONE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCustomerEligibleProducts(@Valid @RequestBody DroneDTO droneDTO) {
        return apiService.registerADrone(droneDTO);
    }

    @GetMapping(value = Endpoints.LIST_OF_DRONES, produces = MediaType.APPLICATION_JSON_VALUE)
    public String fetchListOfRegisteredDrones() {
        return apiService.fetchListOfRegisteredDrone();
    }

    @PostMapping(value = Endpoints.LOAD_DRONE_WITH_MED, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String loadDroneWithMedications(@Valid @RequestBody LoadingDTO loadingDTO) {
        return apiService.loadDroneWithMedications(loadingDTO);
    }

    @GetMapping(value = Endpoints.LIST_OF_MEDICATIONS, produces = MediaType.APPLICATION_JSON_VALUE)
    public String fetchListOfMedications() {
        return apiService.listOfMedications();
    }

    @GetMapping(value = Endpoints.DRONE_LOADED_MEDICATIONS, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getLoadedMedicationsForDrone(@PathVariable(name = "droneSerialNumber", required = true) String droneSerialNumber) {
        return apiService.getLoadedMedicationsForDrone(droneSerialNumber);
    }

    @GetMapping(value = Endpoints.DRONE_BATTERY_LEVEL, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDroneBatteryLevel(@PathVariable(name = "droneSerialNumber", required = true) String droneSerialNumber) {
        return apiService.getDroneBatteryLevel(droneSerialNumber);
    }

    @GetMapping(value = Endpoints.AVAILABLE_DRONES_FOR_LOADING, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAvailableDronesForLoading() {
        return apiService.getAvailableDronesForLoading();
    }
}
