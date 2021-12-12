/*
 * Developer: Emmanuel Israel
 * Licensed: MIT
 */
package com.emmanuelisrael.drones.service;

import com.emmanuelisrael.drones.payload.DroneDTO;
import com.emmanuelisrael.drones.payload.LoadingDTO;

/**
 *
 * @author Emmanuel W. Israel <israelecricket@gmail.com>
 */
public interface ApiService {

    String registerADrone(DroneDTO droneDTO);

    String fetchListOfRegisteredDrone();

    String loadDroneWithMedications(LoadingDTO loadingDTO);

    String listOfMedications();

    String getLoadedMedicationsForDrone(String droneSerialNumber);

    String getDroneBatteryLevel(String droneSerialNumber);

    String getAvailableDronesForLoading();
}
