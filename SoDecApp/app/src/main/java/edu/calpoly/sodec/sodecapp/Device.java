package edu.calpoly.sodec.sodecapp;

/** Holds constants for the many device/sensor names. */
public class Device {

    // Power Generated
    public static final String POW_GEN_MAIN = "s-elec-gen-main-array";
    public static final String POW_GEN_BIFACIAL = "s-elec-gen-bifacial";

    public static final String[] POW_GEN_DEVICES = {
            POW_GEN_MAIN, POW_GEN_BIFACIAL
    };

    // Power Used
    public static final String POW_USE_LAUNDRY = "s-elec-used-laundry";
    public static final String POW_USE_DISHWASHER = "s-elec-used-dishwasher";
    public static final String POW_USE_REFRIGERATOR = "s-elec-used-refrigerator";
    public static final String POW_USE_INDUCTION_STOVE = "s-elec-used-induction-stove";
    public static final String POW_USE_EWH_SOLAR_WATER_HEATER = "s-elec-used-ewh-solar-water-heater";
    public static final String POW_USE_KITCHEN_RECEPS_1 = "s-elec-used-kitchen-receps-1";
    public static final String POW_USE_KITCHEN_RECEPS_2 = "s-elec-used-kitchen-receps-2";
    public static final String POW_USE_LIVING_RECEPS = "s-elec-used-living-receps";
    public static final String POW_USE_DINING_RECEPS_1 = "s-elec-used-dining-receps-1";
    public static final String POW_USE_DINING_RECEPS_2 = "s-elec-used-dining-receps-2";
    public static final String POW_USE_BATHROOM_RECEPS = "s-elec-used-bathroom-receps";
    public static final String POW_USE_BEDROOM_RECEPS_1 = "s-elec-used-bedroom-receps-1";
    public static final String POW_USE_BEDROOM_RECEPS_2 = "s-elec-used-bedroom-receps-2";
    public static final String POW_USE_MECHANICAL_RECEPS = "s-elec-used-mechanical-receps";
    public static final String POW_USE_ENTRY_RECEPS = "s-elec-used-entry-receps";
    public static final String POW_USE_EXTERIOR_RECEPS = "s-elec-used-exterior-receps";
    public static final String POW_USE_GREY_WATER_PUMP_RECEP = "s-elec-used-grey-water-pump-recep";
    public static final String POW_USE_BLACK_WATER_PUMP_RECEP = "s-elec-used-black-water-pump-recep";
    public static final String POW_USE_THERMAL_LOOP_PUMP_RECEP = "s-elec-used-thermal-loop-pump-recep";
    public static final String POW_USE_WATER_SUPPLY_PUMP_RECEP = "s-elec-used-water-supply-pump-recep";
    public static final String POW_USE_WATER_SUPPLY_BOOSTER_PUMP_RECEP = "s-elec-used-water-supply-booster-pump-recep";
    public static final String POW_USE_VEHICLE_CHARGING_RECEP = "s-elec-used-vehicle-charging-recep";
    public static final String POW_USE_HEAT_PUMP_RECEP = "s-elec-used-heat-pump-recep";
    public static final String POW_USE_AIR_HANDLER_RECEP = "s-elec-used-air-handler-recep";

    public static final String[] POW_USE_DEVICES = {
            POW_USE_LAUNDRY, POW_USE_DISHWASHER, POW_USE_REFRIGERATOR, POW_USE_INDUCTION_STOVE,
            POW_USE_EWH_SOLAR_WATER_HEATER, POW_USE_KITCHEN_RECEPS_1, POW_USE_KITCHEN_RECEPS_2,
            POW_USE_LIVING_RECEPS, POW_USE_DINING_RECEPS_1, POW_USE_DINING_RECEPS_2,
            POW_USE_BATHROOM_RECEPS, POW_USE_BEDROOM_RECEPS_1, POW_USE_BEDROOM_RECEPS_2,
            POW_USE_MECHANICAL_RECEPS, POW_USE_ENTRY_RECEPS, POW_USE_EXTERIOR_RECEPS,
            POW_USE_GREY_WATER_PUMP_RECEP, POW_USE_BLACK_WATER_PUMP_RECEP, POW_USE_THERMAL_LOOP_PUMP_RECEP,
            POW_USE_WATER_SUPPLY_PUMP_RECEP, POW_USE_WATER_SUPPLY_BOOSTER_PUMP_RECEP,
            POW_USE_VEHICLE_CHARGING_RECEP, POW_USE_HEAT_PUMP_RECEP, POW_USE_AIR_HANDLER_RECEP
    };

    // Temperature
    public static final String TEMP_OUTSIDE = "s-temp-out";
    public static final String TEMP_BED = "s-temp-bed";
    public static final String TEMP_BATH = "s-temp-bath";
    public static final String TEMP_LIVINGROOM = "s-temp-lr";

    // Humidity
    public static final String HUM_BED = "s-hum-bed";
    public static final String HUM_BATH = "s-hum-bath";
    public static final String HUM_LIVINGROOM = "s-hum-lr";

    // Lights
    public static final String LIGHT_ENTRY_BOOKEND = "s-light-entry-bookend-1A";
    public static final String LIGHT_CHANDELIER = "s-light-chandelier-1B";
    public static final String LIGHT_TV = "s-light-tv-light-2A";
    public static final String LIGHT_KITCHEN = "s-light-kitchen-uplight-3A";
    public static final String LIGHT_UNDER_COUNTER = "s-light-under-counter-3B";
    public static final String LIGHT_PENDANT_BAR = "s-light-pendant-bar-lights-3C";
    public static final String LIGHT_BATHROOM_AMBIENT = "s-light-bathroom-ambient-4A";
    public static final String LIGHT_MIRROR = "s-light-mirror-4B";
    public static final String LIGHT_FLEXSPACE_UPLIGHT = "s-light-flexspace-uplight-5A";
    public static final String LIGHT_FLEXSPACE_CABINET = "s-light-flexspace-cabinet-5B";
    public static final String LIGHT_BEDROOM_UPLIGHT = "s-light-bedroom-uplight-6A";
    public static final String LIGHT_BEDROOM_CABINET = "s-light-bedroom-cabinet-6B";
    public static final String LIGHT_PORCH = "s-light-porch-lights-8A";
    public static final String LIGHT_UPLIGHTS_AND_POT = "s-light-uplights-and-pot-lights-8B";

    public static final String[] LIGHT_DEVICES = {
            LIGHT_ENTRY_BOOKEND, LIGHT_CHANDELIER, LIGHT_TV, LIGHT_KITCHEN, LIGHT_UNDER_COUNTER,
            LIGHT_PENDANT_BAR, LIGHT_BATHROOM_AMBIENT, LIGHT_MIRROR, LIGHT_FLEXSPACE_UPLIGHT,
            LIGHT_FLEXSPACE_CABINET, LIGHT_BEDROOM_UPLIGHT, LIGHT_BEDROOM_CABINET, LIGHT_PORCH,
            LIGHT_UPLIGHTS_AND_POT
    };
}
