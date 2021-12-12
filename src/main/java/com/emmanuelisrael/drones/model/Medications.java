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
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Emmanuel W. Israel <emmanuel.israel@imalipay.com>
 */
@Entity
@Table(name = "medications")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Medications implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "med_name")
    @Pattern(regexp = "[A-Za-z0-9_-]{2,}", message = "allowed only letters, numbers, '-', '_'")
    private String name;
    @Column(name = "weight", scale = 2)
    private double weight;
    @Column(name = "code", unique = true)
    @Pattern(regexp = "[A-Z0-9_]{2,}", message = "allowed only upper case letters, underscore and numbers")
    private String code;
    @Column(name = "image")
    private String image;
}
