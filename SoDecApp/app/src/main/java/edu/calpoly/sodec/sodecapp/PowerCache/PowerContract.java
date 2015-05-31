package edu.calpoly.sodec.sodecapp.PowerCache;

import android.provider.BaseColumns;

public final class PowerContract {
    public PowerContract() {}

    public static abstract class PowerGen implements BaseColumns {
        public static final String TABLE_NAME = "powerGen";
        public static final String COLUMN_BASE_TIMESTAMP = "baseTimestamp";
        public static final String COLUMN_GEN_MAIN = "genMain";
        public static final String COLUMN_GEN_BIFACIAL = "genBifacial";

        public static final String[] FULL_PROJECTION = {
                _ID,
                COLUMN_BASE_TIMESTAMP,
                COLUMN_GEN_MAIN,
                COLUMN_GEN_BIFACIAL
        };
    }

    public static abstract class PowerUse implements BaseColumns {
        public static final String TABLE_NAME = "powerUse";
        public static final String COLUMN_BASE_TIMESTAMP = "baseTimestamp";
        public static final String COLUMN_USE_LAUNDRY = "useLaundry";
        public static final String COLUMN_USE_DISHWASHER = "useDishwasher";
        public static final String COLUMN_USE_REFRIGERATOR = "useRefrigerator";
        public static final String COLUMN_USE_INDUCTION_STOVE = "useInductionStove";
        public static final String COLUMN_USE_EWH_SOLAR_WATER_HEATER = "useEwhSolarWaterHeater";
        public static final String COLUMN_USE_KITCHEN_RECEPS_1 = "useKitchenReceps1";
        public static final String COLUMN_USE_KITCHEN_RECEPS_2 = "useKitchenReceps2";
        public static final String COLUMN_USE_LIVING_RECEPS = "useLivingReceps";
        public static final String COLUMN_USE_DINING_RECEPS_1 = "useDiningReceps1";
        public static final String COLUMN_USE_DINING_RECEPS_2 = "useDiningReceps2";
        public static final String COLUMN_USE_BATHROOM_RECEPS = "useBathroomReceps";
        public static final String COLUMN_USE_BEDROOM_RECEPS_1 = "useBedroomReceps1";
        public static final String COLUMN_USE_BEDROOM_RECEPS_2 = "useBedroomReceps2";
        public static final String COLUMN_USE_MECHANICAL_RECEPS = "useMechanicalReceps";
        public static final String COLUMN_USE_ENTRY_RECEPS = "useEntryReceps";
        public static final String COLUMN_USE_EXTERIOR_RECEPS = "useExteriorReceps";
        public static final String COLUMN_USE_GREY_WATER_PUMP_RECEP = "useGreyWaterPumpRecep";
        public static final String COLUMN_USE_BLACK_WATER_PUMP_RECEP = "useBlackWaterPumpRecep";
        public static final String COLUMN_USE_THERMAL_LOOP_PUMP_RECEP = "useThermalLoopPumpRecep";
        public static final String COLUMN_USE_WATER_SUPPLY_PUMP_RECEP = "useWaterSupplyPumpRecep";
        public static final String COLUMN_USE_WATER_SUPPLY_BOOSTER_PUMP_RECEP = "useWaterSupplyBoosterPumpRecep";
        public static final String COLUMN_USE_VEHICLE_CHARGING_RECEP = "useVehicleChargingRecep";
        public static final String COLUMN_USE_HEAT_PUMP_RECEP = "useHeatPumpRecep";
        public static final String COLUMN_USE_AIR_HANDLER_RECEP = "useAirHandlerRecep";

        public static final String[] FULL_PROJECTION = {
                _ID,
                COLUMN_BASE_TIMESTAMP,
                COLUMN_USE_LAUNDRY,
                COLUMN_USE_DISHWASHER,
                COLUMN_USE_REFRIGERATOR,
                COLUMN_USE_INDUCTION_STOVE,
                COLUMN_USE_EWH_SOLAR_WATER_HEATER,
                COLUMN_USE_KITCHEN_RECEPS_1,
                COLUMN_USE_KITCHEN_RECEPS_2,
                COLUMN_USE_LIVING_RECEPS,
                COLUMN_USE_DINING_RECEPS_1,
                COLUMN_USE_DINING_RECEPS_2,
                COLUMN_USE_BATHROOM_RECEPS,
                COLUMN_USE_BEDROOM_RECEPS_1,
                COLUMN_USE_BEDROOM_RECEPS_2,
                COLUMN_USE_MECHANICAL_RECEPS,
                COLUMN_USE_ENTRY_RECEPS,
                COLUMN_USE_EXTERIOR_RECEPS,
                COLUMN_USE_GREY_WATER_PUMP_RECEP,
                COLUMN_USE_BLACK_WATER_PUMP_RECEP,
                COLUMN_USE_THERMAL_LOOP_PUMP_RECEP,
                COLUMN_USE_WATER_SUPPLY_PUMP_RECEP,
                COLUMN_USE_WATER_SUPPLY_BOOSTER_PUMP_RECEP,
                COLUMN_USE_VEHICLE_CHARGING_RECEP,
                COLUMN_USE_HEAT_PUMP_RECEP,
                COLUMN_USE_AIR_HANDLER_RECEP
        };
    }
}
