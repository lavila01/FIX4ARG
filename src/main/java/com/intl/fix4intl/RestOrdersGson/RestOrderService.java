package com.intl.fix4intl.RestOrdersGson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import quickfix.ConfigError;
import quickfix.SessionSettings;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
            con.addRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36");
            con.setDoInput(true);
            String s;
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                while ((s = in.readLine()) != null) {
                    data.append(s);
                }
            } catch (IOException ex) {
                System.out.println("ERROR: " + ex.getMessage());
                ex.printStackTrace();
            }
        }catch (IOException | ConfigError ex){
            System.out.println("ERROR: " + ex.getMessage());
            ex.printStackTrace();
        }
    return data;
    }

    public StringBuilder postData(ResponseDTO responseDTO) {
        StringBuilder response = new StringBuilder();
        try {
            HttpURLConnection con = (HttpURLConnection) ((new URL(PRODUCT_URL.concat(settings.getString("Wfsystems-post"))).openConnection()));
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.addRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36");
            con.setDoOutput(true);

            String cursado = new Gson().toJson(responseDTO);
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = cursado.getBytes(CHARSET);
                os.write(input, 0, input.length);
            }

            try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), CHARSET))) {

                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response);
            }
        }catch (IOException | ConfigError ex){
            ex.printStackTrace();
        }
        return response;
    }

    public List<OrderDTO> getOrdersGson() throws IOException {
        Type collectionType = new TypeToken<List<OrderDTO>>(){}.getType();
        List<OrderDTO> resp = new Gson().fromJson( getData().toString() , collectionType);
        return resp;
    }

}
