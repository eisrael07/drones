/*
 * Developer: Emmanuel Israel
 * Licensed: MIT
 */
package com.emmanuelisrael.drones.constants;

/**
 *
 * @author Emmanuel W. Israel <israelecricket@gmail.com>
 */
public class Endpoints {
    
    public static final String REGISTER_DRONE = "/drone/registration";
    public static final String LIST_OF_DRONES = "/drones/list";
    public static final String LOAD_DRONE_WITH_MED = "/drones/load/meds";
    public static final String CREATE_MEDICATIONS = "/med/create";
    public static final String LIST_OF_MEDICATIONS = "/meds/list";
    public static final String DRONE_LOADED_MEDICATIONS = "/meds/loaded/drone/{droneSerialNumber}";
    public static final String DRONE_BATTERY_LEVEL = "/drone/battery-level/{droneSerialNumber}";
    public static final String AVAILABLE_DRONES_FOR_LOADING = "/drones/list/loading";
}
