package edu.calpoly.sodec.sodecapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by JustineDunham on 4/12/15.
 */

public class LightingUtils {

    private static final boolean hasData = true;

    private static final String DEVICE_ID = "device-id";
    private static final String STATUS = "status";
    private static final String TIMESTAMP = "timestamp";
    private static Integer LIGHT_REQUESTS = 0;
    public List<LightDevice> lights = new ArrayList<LightDevice>();
    public Integer numLightsOn = 0;

    private String[] lightIds = new String[]{"s-light-entry-bookend-1A", "s-light-chandelier-1B", "s-light-tv-light-2A", "s-light-kitchen-uplight-3A", "s-light-under-counter-3B", "s-light-under-counter-3B", "s-light-under-counter-3B", "s-light-under-counter-3B", "s-light-flexspace-uplight-5A", "s-light-flexspace-uplight-5A", "s-light-bedroom-uplight-6A", "s-light-bedroom-cabinet-6B", "s-light-bedroom-cabinet-6B", "s-light-bedroom-cabinet-6B"};
    private String[] lightDescriptions = new String[]{"Entry Bookend Light", "Chandelier Light", "TV Light", "Kitchen Uplight Light", "Kitchen Under-Counter Light", "Kitchen Pendant Bar Lights", "Bathroom Light", "Bathroom Mirror Light", "Flexspace Uplight Light", "Flexspace Cabinet Light", "Bedroom Uplight Light", "Bedroom Cabinet Light", "Porch Lights", "Uplights/Pot Lights"};

    private void getLightDevices() {
        for(int idx = 0; idx < lightIds.length; idx++) {
            lights.add(new LightDevice(lightIds[idx], lightDescriptions[idx]));
        }
    }

    public LightingUtils() {
        getLightDevices();
    }

    /*public void getNumLightsOn(ServerConnection.ResponseCallback<String, String> onFinish) {
        //HashMap<AmbientLightDevice, Integer> lights = new HashMap<>();
        List<LightDevice> lights = new ArrayList<LightDevice>();
        Integer numLightsOn = 0;

        getAllLightData(lights);
        for (LightDevice light : lights) {
            if (light.isOn) {
                numLightsOn++;
            }
        }
        onFinish.execute(numLightsOn.toString());
    }*/

    public void getAllLightData(ServerConnection.ResponseCallback<String, String> onFinish) {
        numLightsOn = 0;
        for (edu.calpoly.sodec.sodecapp.LightDevice device: lights) {
            if (hasData) {
                getLightData(onFinish, device);
            } else {
                Random rand = new Random();
                device.isOn = rand.nextInt(2) == 0 ? false : true;
            }
        }
    }


    /*public static void getAllAmbientLightData(final List<LightDevice> lights) {
        for (AmbientLightDevice device : AmbientLightDevice.values()) {
            if (hasData) {
                getAmbientLightData(lights, device);
            } else {
                Random rand = new Random();
                lights.put(device, rand.nextInt(2));
            }
        }
    }*/

    /*public static void getAmbientLightData(final HashMap<AmbientLightDevice, Integer> lights, final AmbientLightDevice device) {

        new ServerConnection().getAmbientLight(new ServerConnection.ResponseCallback<String, String>() {

            /**
             * @param response Should include a base timestamp, base power generated/usage value,
             *                 and pairs of time deltas and power deltas.
             */
            /*@Override
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
    }*/

    private void getLightData(final ServerConnection.ResponseCallback<String, String> onFinish, final LightDevice device) {

        new ServerConnection().getLight(new ServerConnection.ResponseCallback<String, String>() {

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

                    device.isOn = status == 0 ? false : true;
                    if (device.isOn) {
                        numLightsOn++;
                    }
                    //lights.put(device, status);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LIGHT_REQUESTS += 1;
                if (LIGHT_REQUESTS >= lights.size()) {
                    LIGHT_REQUESTS = 0;
                    onFinish.execute(lights.toString());
                }
            }
        }, device);
    }
}