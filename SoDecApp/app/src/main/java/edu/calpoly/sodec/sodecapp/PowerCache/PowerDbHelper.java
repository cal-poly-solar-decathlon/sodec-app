package edu.calpoly.sodec.sodecapp.PowerCache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.calpoly.sodec.sodecapp.PowerCache.PowerContract.PowerUse;
import edu.calpoly.sodec.sodecapp.PowerCache.PowerContract.PowerGen;

public class PowerDbHelper extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_GEN_ENTRIES =
            "CREATE TABLE " + PowerGen.TABLE_NAME + " (" +
            PowerGen._ID + " INTEGER PRIMARY KEY," +
            PowerGen.COLUMN_BASE_TIMESTAMP  + INT_TYPE + COMMA_SEP +
            PowerGen.COLUMN_GEN_MAIN        + INT_TYPE + COMMA_SEP +
            PowerGen.COLUMN_GEN_BIFACIAL    + INT_TYPE + COMMA_SEP +
            " UNIQUE(" + PowerGen.COLUMN_BASE_TIMESTAMP + ") ON CONFLICT IGNORE)";

    private static final String SQL_CREATE_USE_ENTRIES =
            "CREATE TABLE " + PowerUse.TABLE_NAME + " (" +
            PowerUse._ID + " INTEGER PRIMARY KEY," +
            PowerUse.COLUMN_BASE_TIMESTAMP                          + INT_TYPE + COMMA_SEP +
            PowerUse.COLUMN_USE_LAUNDRY                             + INT_TYPE + COMMA_SEP +
            PowerUse.COLUMN_USE_DISHWASHER                          + INT_TYPE + COMMA_SEP +
            PowerUse.COLUMN_USE_REFRIGERATOR                        + INT_TYPE + COMMA_SEP +
            PowerUse.COLUMN_USE_INDUCTION_STOVE                     + INT_TYPE + COMMA_SEP +
            PowerUse.COLUMN_USE_EWH_SOLAR_WATER_HEATER              + INT_TYPE + COMMA_SEP +
            PowerUse.COLUMN_USE_KITCHEN_RECEPS_1                    + INT_TYPE + COMMA_SEP +
            PowerUse.COLUMN_USE_KITCHEN_RECEPS_2                    + INT_TYPE + COMMA_SEP +
            PowerUse.COLUMN_USE_LIVING_RECEPS                       + INT_TYPE + COMMA_SEP +
            PowerUse.COLUMN_USE_DINING_RECEPS_1                     + INT_TYPE + COMMA_SEP +
            PowerUse.COLUMN_USE_DINING_RECEPS_2                     + INT_TYPE + COMMA_SEP +
            PowerUse.COLUMN_USE_BATHROOM_RECEPS                     + INT_TYPE + COMMA_SEP +
            PowerUse.COLUMN_USE_BEDROOM_RECEPS_1                    + INT_TYPE + COMMA_SEP +
            PowerUse.COLUMN_USE_BEDROOM_RECEPS_2                    + INT_TYPE + COMMA_SEP +
            PowerUse.COLUMN_USE_MECHANICAL_RECEPS                   + INT_TYPE + COMMA_SEP +
            PowerUse.COLUMN_USE_ENTRY_RECEPS                        + INT_TYPE + COMMA_SEP +
            PowerUse.COLUMN_USE_EXTERIOR_RECEPS                     + INT_TYPE + COMMA_SEP +
            PowerUse.COLUMN_USE_GREY_WATER_PUMP_RECEP               + INT_TYPE + COMMA_SEP +
            PowerUse.COLUMN_USE_BLACK_WATER_PUMP_RECEP              + INT_TYPE + COMMA_SEP +
            PowerUse.COLUMN_USE_THERMAL_LOOP_PUMP_RECEP             + INT_TYPE + COMMA_SEP +
            PowerUse.COLUMN_USE_WATER_SUPPLY_PUMP_RECEP             + INT_TYPE + COMMA_SEP +
            PowerUse.COLUMN_USE_WATER_SUPPLY_BOOSTER_PUMP_RECEP     + INT_TYPE + COMMA_SEP +
            PowerUse.COLUMN_USE_VEHICLE_CHARGING_RECEP              + INT_TYPE + COMMA_SEP +
            PowerUse.COLUMN_USE_HEAT_PUMP_RECEP                     + INT_TYPE + COMMA_SEP +
            PowerUse.COLUMN_USE_AIR_HANDLER_RECEP                   + INT_TYPE + COMMA_SEP +
            " UNIQUE(" + PowerUse.COLUMN_BASE_TIMESTAMP + ") ON CONFLICT IGNORE)";

    private static final String SQL_DELETE_GEN_ENTRIES =
            "DROP TABLE IF EXISTS " + PowerGen.TABLE_NAME;

    private static final String SQL_DELETE_USE_ENTRIES =
            "DROP TABLE IF EXISTS " + PowerUse.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Power.db";

    public PowerDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_GEN_ENTRIES);
        db.execSQL(SQL_CREATE_USE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_GEN_ENTRIES);
        db.execSQL(SQL_DELETE_USE_ENTRIES);
        onCreate(db);
    }
}
