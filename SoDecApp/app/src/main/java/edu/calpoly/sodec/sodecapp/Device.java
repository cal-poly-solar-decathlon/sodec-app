package edu.calpoly.sodec.sodecapp;


public class Device {

    /* Power Generated */
    public static final String GEN_MAIN = "s-elec-gen-main-array";
    public static final String GEN_BIFACIAL = "s-elec-gen-bifacial";
    public static final String[] GEN_DEVICES = {
            GEN_MAIN,
            GEN_BIFACIAL
    };

    /* Power Used */
    public static final String DEVICE_LAUNDRY = "s-elec-used-laundry";
    public static final String DEVICE_DISHWASHER = "s-elec-used-dishwasher";
    public static final String DEVICE_REFRIGERATOR = "s-elec-used-refrigerator";
    public static final String DEVICE_INDUCTION_STOVE = "s-elec-used-induction-stove";
    public static final String DEVICE_EWH_SOLAR_WATER_HEATER = "s-elec-used-ewh-solar-water-heater";
    public static final String DEVICE_KITCHEN_RECEPS_1 = "s-elec-used-kitchen-receps-1";
    public static final String DEVICE_KITCHEN_RECEPS_2 = "s-elec-used-kitchen-receps-2";
    public static final String DEVICE_LIVING_RECEPS = "s-elec-used-living-receps";
    public static final String DEVICE_DINING_RECEPS_1 = "s-elec-used-dining-receps-1";
    public static final String DEVICE_DINING_RECEPS_2 = "s-elec-used-dining-receps-2";
    public static final String DEVICE_BATHROOM_RECEPS = "s-elec-used-bathroom-receps";
    public static final String DEVICE_BEDROOM_RECEPS_1 = "s-elec-used-bedroom-receps-1";
    public static final String DEVICE_BEDROOM_RECEPS_2 = "s-elec-used-bedroom-receps-2";
    public static final String DEVICE_MECHANICAL_RECEPS = "s-elec-used-mechanical-receps";
    public static final String DEVICE_ENTRY_RECEPS = "s-elec-used-entry-receps";
    public static final String DEVICE_EXTERIOR_RECEPS = "s-elec-used-exterior-receps";
    public static final String DEVICE_GREY_WATER_PUMP_RECEP = "s-elec-used-grey-water-pump-recep";
    public static final String DEVICE_BLACK_WATER_PUMP_RECEP = "s-elec-used-black-water-pump-recep";
    public static final String DEVICE_THERMAL_LOOP_PUMP_RECEP = "s-elec-used-thermal-loop-pump-recep";
    public static final String DEVICE_WATER_SUPPLY_PUMP_RECEP = "s-elec-used-water-supply-pump-recep";
    public static final String DEVICE_WATER_SUPPLY_BOOSTER_PUMP_RECEP = "s-elec-used-water-supply-booster-pump-recep";
    public static final String DEVICE_VEHICLE_CHARGING_RECEP = "s-elec-used-vehicle-charging-recep";
    public static final String DEVICE_HEAT_PUMP_RECEP = "s-elec-used-heat-pump-recep";
    public static final String DEVICE_AIR_HANDLER_RECEP = "s-elec-used-air-handler-recep";

    public static final String[] USE_DEVICES = {
            DEVICE_LAUNDRY, DEVICE_DISHWASHER, DEVICE_REFRIGERATOR, DEVICE_INDUCTION_STOVE,
            DEVICE_EWH_SOLAR_WATER_HEATER, DEVICE_KITCHEN_RECEPS_1, DEVICE_KITCHEN_RECEPS_2,
            DEVICE_LIVING_RECEPS, DEVICE_DINING_RECEPS_1, DEVICE_DINING_RECEPS_2,
            DEVICE_BATHROOM_RECEPS, DEVICE_BEDROOM_RECEPS_1, DEVICE_BEDROOM_RECEPS_2,
            DEVICE_MECHANICAL_RECEPS, DEVICE_ENTRY_RECEPS, DEVICE_EXTERIOR_RECEPS,
            DEVICE_GREY_WATER_PUMP_RECEP, DEVICE_BLACK_WATER_PUMP_RECEP, DEVICE_THERMAL_LOOP_PUMP_RECEP,
            DEVICE_WATER_SUPPLY_PUMP_RECEP, DEVICE_WATER_SUPPLY_BOOSTER_PUMP_RECEP,
            DEVICE_VEHICLE_CHARGING_RECEP, DEVICE_HEAT_PUMP_RECEP, DEVICE_AIR_HANDLER_RECEP
    };
}
