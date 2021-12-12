/*
 * Developer: Emmanuel Israel
 * Licensed: MIT
 */
package com.emmanuelisrael.drones.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

/**
 *
 * @author Emmanuel W. Israel <emmanuel.israel@imalipay.com>
 */
@Data
public class DroneDTO {

    @NotBlank(message = "field serial number cannot be blank")
    @NotEmpty(message = "field serial number cannot be empty or null")
    @Length(max = 100, message = "allowed max length for field serial number is 100")
    private String serialNumber;

    @NotNull(message = "field weight cannot be null")
    @Range(max = 500, message = "allowed max weight for drone in field weight is 500")
    private double weight;
    
    private int batteryLevel;
}
