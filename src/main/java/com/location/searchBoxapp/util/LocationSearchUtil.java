package com.location.searchBoxapp.util;


import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import org.springframework.web.client.RestTemplate;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

public  class LocationSearchUtil {
    public static final String base_endpoint = "http://api.positionstack.com/v1/forward?";
    public static final String access_key = "0c53cb9356be4e3668b80a4c5ac8957f";
    public static final String max_records = "10";

    public static Set<String> getLocationSuggestions(String searchWord) {
        HashSet<String> res = new HashSet<>();
        if (searchWord.length() < 3)
            return res;
        String url = base_endpoint + "access_key=" + access_key + "&query=" + searchWord + "&limit=" + max_records;
        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.getForObject(url, String.class);
        JsonReader jsonReader = Json.createReader(new StringReader(result));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();
        try {
            if (object.get("data").asJsonArray().size() > 0) {
                for (JsonObject m : object.get("data").asJsonArray().getValuesAs(JsonObject.class)) {
                    res.add(m.getString("label"));
                }
            }
        }catch (Exception e){
            System.out.println("Error while parsing data " + e.getMessage() + " url: " + url + " result: " + result);
        }
        return res;
    }

}
