/*
 * Developer: Emmanuel Israel
 * Licensed: MIT
 */
package com.emmanuelisrael.drones.repository;

import com.emmanuelisrael.drones.model.AuditLogs;
import com.emmanuelisrael.drones.model.Drones;
import com.emmanuelisrael.drones.model.Medications;
import com.emmanuelisrael.drones.model.Shipments;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Emmanuel W. Israel <emmanuel.israel@imalipay.com>
 */
@Repository
@Transactional
public class ApiRepositoryImpl implements ApiRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Drones createDroneRecord(Drones drone) {
        em.persist(drone);
        em.flush();
        return drone;
    }

    @Override
    public Drones updateDroneRecord(Drones drone) {
        em.merge(drone);
        em.flush();
        return drone;
    }

    @Override
    public List<Drones> getDroneList() {
        TypedQuery<Drones> query = em.createQuery("SELECT d from Drones AS d", Drones.class);
        List<Drones> drones = query.getResultList();
        if (drones.isEmpty()) {
            return new ArrayList<>();
        } else {
            return drones;
        }
    }

    @Override
    public List<Drones> getAvailableDronesForLoading() {
        TypedQuery<Drones> query = em.createQuery("SELECT d from Drones d WHERE d.state = 'IDLE' AND d.batteryCapacity >= 25", Drones.class);
        List<Drones> drones = query.getResultList();
        if (drones.isEmpty()) {
            return new ArrayList<>();
        } else {
            return drones;
        }
    }

    @Override
    public Drones getDroneWithSerialNumber(String serialNumber) {
        TypedQuery<Drones> query = em.createQuery("SELECT d from Drones d WHERE d.serialNumber = :serialNumber", Drones.class)
                .setParameter("serialNumber", serialNumber);
        List<Drones> drones = query.getResultList();
        if (drones.isEmpty()) {
            return null;
        } else {
            return drones.get(0);
        }
    }

    @Override
    public int checkIfDronesTableIsEmpty() {
        TypedQuery<Drones> query = em.createQuery("SELECT d from Drones AS d", Drones.class);
        List<Drones> drones = query.getResultList();
        if (drones.isEmpty()) {
            return 0;
        } else {
            return drones.size();
        }
    }

    @Override
    public int CheckIfMedicationTableIsEmpty() {
        TypedQuery<Medications> query = em.createQuery("SELECT d from Medications AS d", Medications.class);
        List<Medications> med = query.getResultList();
        if (med.isEmpty()) {
            return 0;
        } else {
            return med.size();
        }
    }

    @Override
    public Medications createdMedication(Medications med) {
        em.persist(med);
        em.flush();
        return med;
    }

    @Override
    public Medications getMedicationWithCode(String code) {
        TypedQuery<Medications> query = em.createQuery("SELECT d from Medications d WHERE d.code = :code", Medications.class)
                .setParameter("code", code);
        List<Medications> med = query.getResultList();
        if (med.isEmpty()) {
            return null;
        } else {
            return med.get(0);
        }
    }

    @Override
    public Shipments createShipmentRecord(Shipments shipment) {
        em.persist(shipment);
        em.flush();
        return shipment;
    }

    @Override
    public Shipments getShipmentRecordForDrone(Drones drone, String status) {
        TypedQuery<Shipments> query = em.createQuery("SELECT d from Shipments d WHERE d.drone = :drone AND d.status = :status", Shipments.class)
                .setParameter("drone", drone)
                .setParameter("status", status);
        List<Shipments> shipment = query.getResultList();
        if (shipment.isEmpty()) {
            return null;
        } else {
            return shipment.get(0);
        }
    }

    @Override
    public AuditLogs createAuditLog(AuditLogs log) {
        em.persist(log);
        em.flush();
        return log;
    }

    @Override
    public List<Medications> getMedicationList() {
        TypedQuery<Medications> query = em.createQuery("SELECT d from Medications AS d", Medications.class);
        List<Medications> meds = query.getResultList();
        if (meds.isEmpty()) {
            return new ArrayList<>();
        } else {
            return meds;
        }
    }
}
