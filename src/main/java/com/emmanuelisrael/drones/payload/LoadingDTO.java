/*
 * Developer: Emmanuel Israel
 * Licensed: MIT
 */
package com.emmanuelisrael.drones.payload;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 *
 * @author Emmanuel W. Israel <israelecricket@gmail.com>
 */
@Data
public class LoadingDTO {

    @NotBlank(message = "field address cannot be blank")
    @NotEmpty(message = "field address cannot be empty or null")
    private String address;
    
    @NotBlank(message = "field droneSerialNumber cannot be blank")
    @NotEmpty(message = "field droneSerialNumber cannot be empty or null")
    private String droneSerialNumber;
    
    private List<MedicationDTO> medications;
}
