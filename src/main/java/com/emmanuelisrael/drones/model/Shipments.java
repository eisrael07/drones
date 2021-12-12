/*
 * Developer: Emmanuel Israel
 * Licensed: MIT
 */
package com.emmanuelisrael.drones.model;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author Emmanuel W. Israel <israelecricket@gmail.com>
 */
@Entity
@Table(name = "shipments")
@Data
public class Shipments implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @ManyToOne
    private Drones drone;
    @Column(name = "tracking_id")
    private String trackingId;
    @Column(name = "status")
    private String status;
    @Column(name = "address")
    private String address;
    @Column(name = "medications")
    @ElementCollection(targetClass = String.class)
    private List<String> medications;
    @Column(name = "created_at")
    private ZonedDateTime createdAt;
    @Column(name = "delivered_at")
    private ZonedDateTime deliveredAt;
    @Column(name = "total_weight")
    private double totalWeight;
}
