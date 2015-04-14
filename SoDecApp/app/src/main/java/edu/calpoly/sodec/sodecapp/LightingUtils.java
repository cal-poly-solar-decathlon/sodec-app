package edu.calpoly.sodec.sodecapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

import edu.calpoly.sodec.sodecapp.ServerConnection.AmbientLightDevice;
/**
 * Created by JustineDunham on 4/12/15.
 */

public class LightingUtils {

    private static final boolean hasData = false;

    private static final String DEVICE_ID = "device-id";
    private static final String STATUS = "status";
    private static final String TIMESTAMP = "timestamp";

    public static void getAllAmbientLightData(final HashMap<AmbientLightDevice,  Integer> lights) {
        for (AmbientLightDevice device : AmbientLightDevice.values()) {
            if (hasData) {
                getAmbientLightData(lights, device);
            } else {
                Random rand = new Random();
                lights.put(device, rand.nextInt(2));
            }
        }
    }

    public static void getAmbientLightData(final HashMap<AmbientLightDevice, Integer> lights, final AmbientLightDevice device) {

        new ServerConnection().getAmbientLight(new ServerConnection.ResponseCallback<String, String>() {

            /**
             * @param response Should include a base timestamp, base power generated/usage value,
             *                 and pairs of time deltas and power deltas.
             */
            @Override
            public void execute(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int status = (int) jsonResponse.get(STATUS);
                    String id = (String) jsonResponse.get(DEVICE_ID);

                    lights.put(device, status);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, device);
    }
}