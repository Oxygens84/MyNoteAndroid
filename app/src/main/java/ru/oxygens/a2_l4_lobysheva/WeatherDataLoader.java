package ru.oxygens.a2_l4_lobysheva;

import android.content.Context;
import android.support.annotation.Nullable;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by oxygens on 24/03/2018.
 */

class WeatherDataLoader {

    private static final String WEATHER_API_KEY = "556693c733016a677267eed8104d1de0";
    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f";
    private static final String KEY = "x-api-key";
    private static final String RESPONSE = "cod";
    private static final String NEW_LINE = "\n";
    private static final int RESULT_SUCCESS = 200;
    private static final int CAPACITY = 1024;

    @Nullable
    static JSONObject getJSONData(Context context, double lat, double lon) {

        try {
            URL url = new URL(String.format(WEATHER_API_URL, lat, lon));
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.addRequestProperty(KEY, WEATHER_API_KEY);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder rawData = new StringBuilder(CAPACITY);
            String tempVariable;

            while ((tempVariable = reader.readLine()) != null) {
                rawData.append(tempVariable).append(NEW_LINE);
            }

            reader.close();

            JSONObject jsonObject = new JSONObject(rawData.toString());

            if(jsonObject.getInt(RESPONSE) != RESULT_SUCCESS) {
                return null;
            } else {
                return jsonObject;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
