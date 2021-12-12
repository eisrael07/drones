/*
 * Developer: Emmanuel Israel
 * Licensed: MIT
 */
package com.emmanuelisrael.drones.constants;

/**
 *
 * @author Emmanuel W. Israel <emmanuel.israel@imalipay.com>
 */
public enum StatusCodes {

    SUCCESS("00"),
    FAILED("90"),
    SYSTEM_MALFUNCTION("90");

    public final String code;

    private StatusCodes(String code) {
        this.code = code;
    }
}
