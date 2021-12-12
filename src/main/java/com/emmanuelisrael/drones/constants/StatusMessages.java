/*
 * Developer: Emmanuel Israel
 * Licensed: MIT
 */
package com.emmanuelisrael.drones.constants;

import com.emmanuelisrael.drones.payload.ResponseDTO;
import com.google.gson.Gson;

/**
 *
 * @author Emmanuel W. Israel <israelecricket@gmail.com>
 */
public class StatusMessages {
    
    Gson gson;
    ResponseDTO response;
    
    public StatusMessages() {
        gson = new Gson();
    }
    
    public String successMessage(String responseMessage, Object data) {
        response = new ResponseDTO();
        response.setStatusCode(StatusCodes.SUCCESS.code);
        response.setStatusMessage(responseMessage);
        response.setData(data);
        return gson.toJson(response, ResponseDTO.class);
    }
    
    public String errorMessage(String responseMessage, Object cause) {
        response = new ResponseDTO();
        response.setStatusCode(StatusCodes.FAILED.code);
        response.setStatusMessage(responseMessage);
        response.setError(cause);
        return gson.toJson(response, ResponseDTO.class);
    }
    
    public String exceptionMessage(String responseMessage) {
        response = new ResponseDTO();
        response.setStatusCode(StatusCodes.SYSTEM_MALFUNCTION.code);
        response.setStatusMessage(responseMessage);
        return gson.toJson(response, ResponseDTO.class);
    }
}
