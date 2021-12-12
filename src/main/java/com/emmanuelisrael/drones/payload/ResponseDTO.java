/*
 * Developer: Emmanuel Israel
 * Licensed: MIT
 */
package com.emmanuelisrael.drones.payload;

import lombok.Data;

/**
 *
 * @author Emmanuel W. Israel <emmanuel.israel@imalipay.com>
 */
@Data
public class ResponseDTO {
    
    private String statusCode;
    private String statusMessage;
    private Object data;
    private Object error;
}
