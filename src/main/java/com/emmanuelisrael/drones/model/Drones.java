/*
 * Developer: Emmanuel Israel
 * Licensed: MIT
 */
package com.emmanuelisrael.drones.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Emmanuel W. Israel <israelecricket@gmail.com>
 */
@Entity
@Table(name = "drones")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Drones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "serial_number", length = 100, unique = true)
    private String serialNumber;
    @Column(name = "model")
    private String model;
    @Column(name = "weight")
    private double weight;
    @Column(name = "battery_capacity")
    private int batteryCapacity;
    @Column(name = "drone_state", columnDefinition = "varchar(20) default 'IDLE'")
    private String state;
}
