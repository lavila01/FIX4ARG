package com.intl.fix4intl.RestOrdersGson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import quickfix.ConfigError;
import quickfix.SessionSettings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class RestOrderService {
    // API endpoint
    private static String PRODUCT_URL = "https://trading.wfsystems.com.ar/api/";
    private static final String CHARSET = "UTF-8";
    private SessionSettings settings;
    public RestOrderService(SessionSettings settings) throws ConfigError {
        this.settings = settings;
        if(StringUtils.isNotEmpty(settings.getString("Wfsystems-api"))){
            PRODUCT_URL = settings.getString("Wfsystems-api");
        }
    }

    private StringBuilder getData() {
        StringBuilder data = new StringBuilder();
        try {

            HttpURLConnection con = (HttpURLConnection) ((new URL(PRODUCT_URL.concat(settings.getString("Wfsystems-get"))).openConnection()));
            con.setRequestMethod("GET");

            con.setDoInput(true);
            String s;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                while ((s = in.readLine()) != null) {
                    data.append(s);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }catch (IOException | ConfigError ex){
            ex.printStackTrace();
        }
    return data;
    }

    private StringBuilder postData() {
        StringBuilder data = new StringBuilder();
        try {

            HttpURLConnection con = (HttpURLConnection) ((new URL(PRODUCT_URL.concat(settings.getString("Wfsystems-post"))).openConnection()));
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String s;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                while ((s = in.readLine()) != null) {
                    data.append(s);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }catch (IOException | ConfigError ex){
            ex.printStackTrace();
        }
        return data;
    }

    public List<OrderDTO> getOrdersGson() throws IOException {
        Type collectionType = new TypeToken<List<OrderDTO>>(){}.getType();
        List<OrderDTO> resp = new Gson().fromJson( getData().toString() , collectionType);
        return resp;
    }

}
