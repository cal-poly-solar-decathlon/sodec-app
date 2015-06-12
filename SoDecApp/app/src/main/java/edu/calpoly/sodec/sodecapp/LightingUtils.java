package edu.calpoly.sodec.sodecapp;

import android.graphics.Point;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
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

    private String[] lightDescriptions = new String[]{"Entry Bookend Light", "Chandelier Light", "TV Light", "Kitchen Uplight Light", "Kitchen Under-Counter Light", "Kitchen Pendant Bar Lights", "Bathroom Light", "Bathroom Mirror Light", "Flexspace Uplight Light", "Flexspace Cabinet Light", "Bedroom Uplight Light", "Bedroom Cabinet Light", "Porch Lights", "Uplights/Pot Lights"};
    private String[] roomNumbers = new String[]{"1A", "1B", "2A", "3A", "3B", "3C", "4A", "4B", "5A", "5B", "6A", "6B", "8A", "8B"};
    private Point[] positions = new Point[]{new Point(70, 10), new Point(80, 20), new Point(50, 10), new Point(60, 50), new Point(67, 50), new Point(75, 50), new Point(40, 50), new Point(45, 50), new Point(50, 80), new Point(60, 80), new Point(30, 80), new Point(40, 80), new Point(20, 20), new Point(40, 20)};

    private void getLightDevices() {
        for(int idx = 0; idx < Device.LIGHT_DEVICES.length; idx++) {
            lights.add(new LightDevice(Device.LIGHT_DEVICES[idx], lightDescriptions[idx],
                    getPositionFromID(Device.LIGHT_DEVICES[idx])));
        }
    }

    public LightingUtils() {
        getLightDevices();
    }

    private Point getPositionFromID(String id) {
        String[] sections = id.split("-");
        String roomNum = sections[sections.length - 1];
        int positionIdx = Arrays.asList(roomNumbers).indexOf(roomNum);
        return positions[positionIdx];
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
        for (edu.calpoly.sodec.sodecapp.LightDevice device : lights) {
            if (hasData) {
                getLightData(onFinish, device);
            } else {
                Random rand = new Random();
                device.isOn = rand.nextInt(2) == 0 ? false : true;
                numLightsOn += device.isOn ? 1 : 0;
                LIGHT_REQUESTS++;
                if (LIGHT_REQUESTS >= lights.size()) {
                    LIGHT_REQUESTS = 0;
                    onFinish.execute(lights.toString());
                }
            }
        }
    }

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