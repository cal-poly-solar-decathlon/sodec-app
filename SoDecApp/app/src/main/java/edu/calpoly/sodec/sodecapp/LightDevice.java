package edu.calpoly.sodec.sodecapp;

/**
 * Created by JustineDunham on 4/26/15.
 */

public class LightDevice {
    String id;
    String description;
    Boolean isOn;

    public LightDevice(String id, String description, Boolean isOn) {
        this.id = id;
        this.description = description;
        this.isOn = isOn;
    }

    public LightDevice(String id, String description) {
        this.id = id;
        this.description = description;
        isOn = false;
    }
}
