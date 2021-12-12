/*
 * Developer: Emmanuel Israel
 * Licensed: MIT
 */
package com.emmanuelisrael.drones.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 *
 * @author Emmanuel W. Israel <israelecricket@gmail.com>
 */
@Data
public class MedicationDTO {
    
    @NotBlank(message = "field code cannot be blank")
    @NotEmpty(message = "field code cannot be empty or null")
    private String code;
}
