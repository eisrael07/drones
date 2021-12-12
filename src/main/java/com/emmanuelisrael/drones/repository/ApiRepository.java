/*
 * Developer: Emmanuel Israel
 * Licensed: MIT
 */
package com.emmanuelisrael.drones.repository;

import com.emmanuelisrael.drones.model.AuditLogs;
import com.emmanuelisrael.drones.model.Drones;
import com.emmanuelisrael.drones.model.Medications;
import com.emmanuelisrael.drones.model.Shipments;
import java.util.List;

/**
 *
 * @author Emmanuel W. Israel <israelecricket@gmail.com>
 */
public interface ApiRepository {

    Drones createDroneRecord(Drones drone);

    Drones updateDroneRecord(Drones drone);

    List<Drones> getDroneList();
    
    List<Drones> getAvailableDronesForLoading();
    
    Drones getDroneWithSerialNumber(String serialNumber);
    
    int checkIfDronesTableIsEmpty();
    
    int CheckIfMedicationTableIsEmpty();
    
    Medications createdMedication(Medications med);
    
    Medications getMedicationWithCode(String code);
    
    List<Medications> getMedicationList();
    
    Shipments createShipmentRecord(Shipments shipment);
    
    Shipments getShipmentRecordForDrone(Drones drone, String status);
    
    AuditLogs createAuditLog(AuditLogs log);
}
