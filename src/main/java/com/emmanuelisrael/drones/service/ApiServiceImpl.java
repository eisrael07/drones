/*
 * Developer: Emmanuel Israel
 * Licensed: MIT
 */
package com.emmanuelisrael.drones.service;

import com.emmanuelisrael.drones.constants.DroneState;
import com.emmanuelisrael.drones.constants.Model;
import com.emmanuelisrael.drones.constants.RecordStatus;
import com.emmanuelisrael.drones.constants.StatusMessages;
import com.emmanuelisrael.drones.model.AuditLogs;
import com.emmanuelisrael.drones.model.Drones;
import com.emmanuelisrael.drones.model.Medications;
import com.emmanuelisrael.drones.model.Shipments;
import com.emmanuelisrael.drones.payload.DroneDTO;
import com.emmanuelisrael.drones.payload.LoadingDTO;
import com.emmanuelisrael.drones.payload.MedicationDTO;
import com.emmanuelisrael.drones.repository.ApiRepository;
import com.google.gson.Gson;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 *
 * @author Emmanuel W. Israel <israelecricket@gmail.com>
 */
@Service
public class ApiServiceImpl implements ApiService {

    @Autowired
    ApiRepository apiRepository;
    @Autowired
    MessageSource messageSource;
    Gson gson;
    StatusMessages statusMessages;
    private static final Logger LOGGER = Logger.getLogger(ApiServiceImpl.class.getName());

    ApiServiceImpl() {
        gson = new Gson();
        statusMessages = new StatusMessages();
    }

    private String determineDroneModel(double weight) {
        String model = "";
        if (weight <= 125) {
            model = Model.LIGHTWEIGHT.toString();
        } else if (weight > 125 && weight <= 250) {
            model = Model.MIDDLEWEIGHT.toString();
        } else if (weight > 250 && weight <= 375) {
            model = Model.CRUISERWEIGHT.toString();
        } else {
            model = Model.HEAVYWEIGHT.toString();
        }
        return model;
    }

    @Override
    public String registerADrone(DroneDTO droneDTO) {
        try {
            String serialNumber = droneDTO.getSerialNumber().toUpperCase();
            Drones drone = apiRepository.getDroneWithSerialNumber(serialNumber);
            if (drone != null) {
                String message = messageSource.getMessage("appMessages.duplicate.drone.record.exist", new Object[]{serialNumber}, Locale.getDefault());
                return statusMessages.errorMessage(RecordStatus.FAILED.toString(), message);
            } else {
                drone = new Drones();
                drone.setSerialNumber(droneDTO.getSerialNumber().toUpperCase());
                drone.setWeight(droneDTO.getWeight());
                drone.setModel(determineDroneModel(droneDTO.getWeight()));
                drone.setBatteryCapacity(100);
                drone.setState(DroneState.IDLE.toString());
                drone = apiRepository.createDroneRecord(drone);
                return statusMessages.successMessage(RecordStatus.SUCCESS.toString(), drone);
            }
        } catch (NoSuchMessageException e) {
            LOGGER.log(Level.SEVERE, "CREATE DRONE FULL EXCEPTION", e);
            LOGGER.log(Level.INFO, "CREATE DRONE SUMMARY EXCEPTION", e.getMessage());
            return statusMessages.exceptionMessage(e.getMessage());
        }
    }

    @Override
    public String fetchListOfRegisteredDrone() {
        try {
            List<Drones> drones = apiRepository.getDroneList();
            return statusMessages.successMessage(RecordStatus.SUCCESS.toString(), drones);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "FETCH LIST OF REGISTERED DRONE FULL EXCEPTION", e);
            LOGGER.log(Level.INFO, "FETCH LIST OF REGISTERED DRONE SUMMARY EXCEPTION", e.getMessage());
            return statusMessages.exceptionMessage(e.getMessage());
        }
    }

    private double getDroneLoadingMaxWeight(double weightOfDrone) {
        double droneWeight = weightOfDrone;
        double loadingMaxWeight = droneWeight * 3;
        return loadingMaxWeight;
    }

    @Override
    public String loadDroneWithMedications(LoadingDTO loadingDTO) {
        try {
            String droneSerialNumber = loadingDTO.getDroneSerialNumber();
            //get drone record
            Drones drone = apiRepository.getDroneWithSerialNumber(droneSerialNumber);
            if (drone == null) {
                String message = messageSource.getMessage("appMessages.drone.record.missing", new Object[]{droneSerialNumber}, Locale.getDefault());
                return statusMessages.errorMessage(RecordStatus.FAILED.toString(), message);
            }

            int droneBatteryLevel = drone.getBatteryCapacity();
            double droneWeight = drone.getWeight();
            String droneState = drone.getState();
            //check the state of the drone
            if (!droneState.equalsIgnoreCase(DroneState.IDLE.toString())) {
                String message = messageSource.getMessage("appMessages.drone.is.active", new Object[]{droneSerialNumber}, Locale.getDefault());
                return statusMessages.errorMessage(RecordStatus.FAILED.toString(), message);
            }

            //check the total weight of medication to be delivered
            List<MedicationDTO> meds = loadingDTO.getMedications();
            double totalMedWeight = 0;
            List<String> medList = new ArrayList<>();
            for (MedicationDTO med : meds) {
                String medCode = med.getCode();
                Medications medications = apiRepository.getMedicationWithCode(medCode);
                if (medications == null) {
                    String message = messageSource.getMessage("appMessages.medication.record.missing", new Object[]{medCode}, Locale.getDefault());
                    return statusMessages.errorMessage(RecordStatus.FAILED.toString(), message);
                }
                //add the weight of medication to totalMedWeight
                totalMedWeight += medications.getWeight();

                //add medication code to med list
                medList.add(medCode);
            }
            double droneLoadingMaxWeight = getDroneLoadingMaxWeight(droneWeight);
            if (totalMedWeight > droneLoadingMaxWeight) {
                String message = messageSource.getMessage("appMessages.drone.overloading", new Object[]{droneLoadingMaxWeight}, Locale.getDefault());
                return statusMessages.errorMessage(RecordStatus.FAILED.toString(), message);
            }

            //check the battery level of the drone
            if (droneBatteryLevel < 25) {
                //update the drone status
                drone.setState(DroneState.IDLE.toString());
                apiRepository.updateDroneRecord(drone);

                String message = messageSource.getMessage("appMessages.drone.low.battery.level", new Object[0], Locale.getDefault());
                return statusMessages.errorMessage(RecordStatus.FAILED.toString(), message);
            } else {
                //update the drone status to loading
                drone.setState(DroneState.LOADING.toString());
                apiRepository.updateDroneRecord(drone);
            }
            //update the drone status to loading
            drone.setState(DroneState.LOADED.toString());
            apiRepository.updateDroneRecord(drone);

            //create shipment record
            Shipments shipment = new Shipments();
            shipment.setAddress(loadingDTO.getAddress());
            shipment.setCreatedAt(ZonedDateTime.now());
            shipment.setDrone(drone);
            shipment.setMedications(medList);
            shipment.setStatus(RecordStatus.SHIPPING.toString());
            shipment.setTrackingId(UUID.randomUUID().toString());
            shipment.setTotalWeight(totalMedWeight);
            Shipments createdShipment = apiRepository.createShipmentRecord(shipment);

            //update the drone status to loading
            drone.setState(DroneState.DELIVERING.toString());
            apiRepository.updateDroneRecord(drone);
            return statusMessages.successMessage(RecordStatus.SUCCESS.toString(), createdShipment);
        } catch (NoSuchMessageException e) {
            LOGGER.log(Level.SEVERE, "DRONE MEDICATION LOADING FULL EXCEPTION", e);
            LOGGER.log(Level.INFO, "DRONE MEDICATION LOADING SUMMARY EXCEPTION", e.getMessage());
            return statusMessages.exceptionMessage(e.getMessage());
        }
    }

    private void createAuditLog(Drones drone, String action) {
        AuditLogs log = new AuditLogs();
        log.setAction(action);
        log.setDrone(drone);
        log.setScannedAt(ZonedDateTime.now());
        apiRepository.createAuditLog(log);
    }

    @Scheduled(fixedDelay = 3600000, initialDelay = 20000)
    public void periodicDroneBatteryLevelCheck() {
        LOGGER.log(Level.INFO, "INITIALIZING PERIODIC DRONE BATTERY LEVEL CHECK @{0}", ZonedDateTime.now());
        List<Drones> drones = apiRepository.getDroneList();
        if (drones != null) {
            int droneCount = 0;
            for (Drones drone : drones) {
                //check for battery level
                int droneBatteryLevel = drone.getBatteryCapacity();
                if (droneBatteryLevel < 25) {
                    //update the drone status
                    drone.setState(DroneState.IDLE.toString());
                    apiRepository.updateDroneRecord(drone);
                    //create log
                    createAuditLog(drone, "Drone status updated to IDLE");
                    createAuditLog(drone, "Drone needs to be charged");
                }

                droneCount++;
            }
            LOGGER.log(Level.INFO, "TOTAL NUMBER OF DRONES CHECKED - {0}", droneCount);
        }
        LOGGER.log(Level.INFO, "COMPLETED PERIODIC DRONE BATTERY LEVEL CHECK @{0}", ZonedDateTime.now());
    }

    @Override
    public String listOfMedications() {
        List<Medications> meds = apiRepository.getMedicationList();
        return statusMessages.successMessage(RecordStatus.SUCCESS.toString(), meds);
    }

    @Override
    public String getLoadedMedicationsForDrone(String droneSerialNumber) {
        try {
            //get drone record
            Drones drone = apiRepository.getDroneWithSerialNumber(droneSerialNumber);
            if (drone == null) {
                String message = messageSource.getMessage("appMessages.drone.record.missing", new Object[]{droneSerialNumber}, Locale.getDefault());
                return statusMessages.errorMessage(RecordStatus.FAILED.toString(), message);
            }
            Shipments shipment = apiRepository.getShipmentRecordForDrone(drone, RecordStatus.SHIPPING.toString());
            if (shipment == null) {
                String message = messageSource.getMessage("appMessages.shipment.shipping.record.missing", new Object[0], Locale.getDefault());
                return statusMessages.errorMessage(RecordStatus.FAILED.toString(), message);
            }
            List<Medications> medList = new ArrayList<>();
            List<String> medicationCodes = shipment.getMedications();
            medicationCodes.stream().map(code -> apiRepository.getMedicationWithCode(code.trim())).filter(m -> (m != null)).forEachOrdered(m -> {
                medList.add(m);
            });
            return statusMessages.successMessage(RecordStatus.SUCCESS.toString(), medList);
        } catch (NoSuchMessageException e) {
            LOGGER.log(Level.SEVERE, "GET DRONE LOADED MEDICATIONS FULL EXCEPTION", e);
            LOGGER.log(Level.INFO, "GET DRONE LOADED MEDICATIONS SUMMARY EXCEPTION", e.getMessage());
            return statusMessages.exceptionMessage(e.getMessage());
        }
    }

    @Override
    public String getDroneBatteryLevel(String droneSerialNumber) {
        try {
            //get drone record
            Drones drone = apiRepository.getDroneWithSerialNumber(droneSerialNumber);
            if (drone == null) {
                String message = messageSource.getMessage("appMessages.drone.record.missing", new Object[]{droneSerialNumber}, Locale.getDefault());
                return statusMessages.errorMessage(RecordStatus.FAILED.toString(), message);
            }
            DroneDTO d = new DroneDTO();
            d.setBatteryLevel(drone.getBatteryCapacity());
            d.setSerialNumber(droneSerialNumber);
            d.setWeight(drone.getWeight());

            return statusMessages.successMessage(RecordStatus.SUCCESS.toString(), d);
        } catch (NoSuchMessageException e) {
            LOGGER.log(Level.SEVERE, "GET DRONE BATTERY LEVEL FULL EXCEPTION", e);
            LOGGER.log(Level.INFO, "GET DRONE BATTERY LEVEL SUMMARY EXCEPTION", e.getMessage());
            return statusMessages.exceptionMessage(e.getMessage());
        }
    }

    @Override
    public String getAvailableDronesForLoading() {
        try {
            List<Drones> drones = apiRepository.getAvailableDronesForLoading();
            return statusMessages.successMessage(RecordStatus.SUCCESS.toString(), drones);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "GET AVAILABLE DRONES FOR LOADING FULL EXCEPTION", e);
            LOGGER.log(Level.INFO, "GET AVAILABLE DRONES FOR LOADING SUMMARY EXCEPTION", e.getMessage());
            return statusMessages.exceptionMessage(e.getMessage());
        }
    }

    @Scheduled(fixedDelay = Long.MAX_VALUE)
    public void dataInsert() {
        int defaultDroneCount = apiRepository.checkIfDronesTableIsEmpty();
        if (defaultDroneCount == 0) {
            //data insert for drones
            apiRepository.createDroneRecord(new Drones(null, "958d3ebb-56e8-4", "LIGHTWEIGHT", 125, 100, "IDLE"));
            LOGGER.log(Level.INFO, "DRONE RECORD - {0} CREATED SUCESSFULLY", new Object[]{"958d3ebb-56e8-4"});
            apiRepository.createDroneRecord(new Drones(null, "0c2b93a4-78d8-4", "LIGHTWEIGHT", 125, 100, "IDLE"));
            LOGGER.log(Level.INFO, "DRONE RECORD - {0} CREATED SUCESSFULLY", new Object[]{"0c2b93a4-78d8-4"});
            apiRepository.createDroneRecord(new Drones(null, "b3badb19-a6a4-4", "MIDDLEWEIGHT", 240, 100, "IDLE"));
            LOGGER.log(Level.INFO, "DRONE RECORD - {0} CREATED SUCESSFULLY", new Object[]{"b3badb19-a6a4-4"});
            apiRepository.createDroneRecord(new Drones(null, "f599a84d-2b4e-4", "MIDDLEWEIGHT", 250, 100, "IDLE"));
            LOGGER.log(Level.INFO, "DRONE RECORD - {0} CREATED SUCESSFULLY", new Object[]{"f599a84d-2b4e-4"});
            apiRepository.createDroneRecord(new Drones(null, "7f69bbeb-7068-4", "CRUISERWEIGHT", 318, 100, "IDLE"));
            LOGGER.log(Level.INFO, "DRONE RECORD - {0} CREATED SUCESSFULLY", new Object[]{"7f69bbeb-7068-4"});
            apiRepository.createDroneRecord(new Drones(null, "7ef2e357-cdd6-4", "CRUISERWEIGHT", 375, 100, "IDLE"));
            LOGGER.log(Level.INFO, "DRONE RECORD - {0} CREATED SUCESSFULLY", new Object[]{"7ef2e357-cdd6-4"});
            apiRepository.createDroneRecord(new Drones(null, "e1013ea5-ab42-4", "LIGHTWEIGHT", 100, 100, "IDLE"));
            LOGGER.log(Level.INFO, "DRONE RECORD - {0} CREATED SUCESSFULLY", new Object[]{"e1013ea5-ab42-4"});
            apiRepository.createDroneRecord(new Drones(null, "248bd253-521c-4", "HEAVYWEIGHT", 500, 100, "IDLE"));
            LOGGER.log(Level.INFO, "DRONE RECORD - {0} CREATED SUCESSFULLY", new Object[]{"248bd253-521c-4"});
            apiRepository.createDroneRecord(new Drones(null, "9b884616-9b84-4", "HEAVYWEIGHT", 450, 100, "IDLE"));
            LOGGER.log(Level.INFO, "DRONE RECORD - {0} CREATED SUCESSFULLY", new Object[]{"9b884616-9b84-4"});
        }

        int defaultMedicationCount = apiRepository.CheckIfMedicationTableIsEmpty();
        if (defaultMedicationCount == 0) {
            //data insert for medications
            apiRepository.createdMedication(new Medications(null, "Rialpixel", 70, "ASDXFC56", "http://placeimg.com/640/480"));
            apiRepository.createdMedication(new Medications(null, "panelmicrochip", 170, "KJHG456", "http://placeimg.com/640/480"));
            apiRepository.createdMedication(new Medications(null, "Cornersmicrochip", 70, "RTE4577", "http://placeimg.com/640/480"));
            apiRepository.createdMedication(new Medications(null, "Kronealarm", 109, "JHG23454", "http://placeimg.com/640/480"));
            apiRepository.createdMedication(new Medications(null, "Mobilitycard", 70, "WERTY54", "http://placeimg.com/640/480"));
            apiRepository.createdMedication(new Medications(null, "JBODpanel", 70, "R45TRGH", "http://placeimg.com/640/480"));
            apiRepository.createdMedication(new Medications(null, "Pantssensor", 90, "GHFDH457", "http://placeimg.com/640/480"));
            apiRepository.createdMedication(new Medications(null, "Cottonmonitor", 40, "XZCVB57IU", "http://placeimg.com/640/480"));
            apiRepository.createdMedication(new Medications(null, "Seniorpixel", 150, "5S4DTCYVH", "http://placeimg.com/640/480"));
            apiRepository.createdMedication(new Medications(null, "AccountAvon", 70, "WEXCGHVY7", "http://placeimg.com/640/480"));
            apiRepository.createdMedication(new Medications(null, "mobileWyoming", 100, "HRZE568G", "http://placeimg.com/640/480"));
            apiRepository.createdMedication(new Medications(null, "FacilitatorBerkshire", 50, "ZXCTVIU7", "http://placeimg.com/640/480"));
            apiRepository.createdMedication(new Medications(null, "COMQuality", 120, "RX54OUPI", "http://placeimg.com/640/480"));
            apiRepository.createdMedication(new Medications(null, "Ariaryreboot", 160, "BPVCUXF7", "http://placeimg.com/640/480"));
            apiRepository.createdMedication(new Medications(null, "engageup", 200, "ZX6CUVI0", "http://placeimg.com/640/480"));
            apiRepository.createdMedication(new Medications(null, "Keyboardinfomediaries", 100, "KJH76664", "http://placeimg.com/640/480"));
            apiRepository.createdMedication(new Medications(null, "protocoloptical", 70, "VXEZ56YU", "http://placeimg.com/640/480"));
            LOGGER.log(Level.INFO, "MEDICATIONS RECORD CREATED SUCESSFULLY");
        }
    }
}
