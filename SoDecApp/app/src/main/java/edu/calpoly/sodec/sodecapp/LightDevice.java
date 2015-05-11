package edu.calpoly.sodec.sodecapp;

import android.graphics.Point;

/**
 * Created by JustineDunham on 4/26/15.
 */

public class LightDevice {
    String id;
    String description;
    Boolean isOn;
    Point location;

    public LightDevice(String id, String description, Boolean isOn, Point location) {
        this.id = id;
        this.description = description;
        this.isOn = isOn;
        this.location = location;
    }

    public LightDevice(String id, String description, Point location) {
        this.id = id;
        this.description = description;
        isOn = false;
        this.location = location;
    }
}
