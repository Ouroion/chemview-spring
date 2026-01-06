package com.chemview.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class PubChemService {
    
    private static final String PC_URL = "https://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/name/";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> getChemicalInformation(String input) throws Exception {
        String pcURLParams = PC_URL + input + "/JSON";

        URL url = new URL(pcURLParams);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        JsonNode response = readResponse(conn);
        return interpretResponse(response);
    }

    private JsonNode readResponse(HttpURLConnection conn) throws Exception {
        int responseCode = conn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                return objectMapper.readTree(response.toString());
            }
        } else {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                StringBuilder errorResponse = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    errorResponse.append(inputLine);
                }
                throw new Exception("API request failed: " + errorResponse.toString());
            }
        }
    }

    private Map<String, Object> interpretResponse(JsonNode response) {
        JsonNode propsArray = response
                .get("PC_Compounds")
                .get(0)
                .get("props");

        Map<String, Object> result = new HashMap<>();

        for (JsonNode prop : propsArray) {
            String label = prop.get("urn").get("label").asText();

            if (label.equals("Molecular Formula")) {
                result.put("molecularFormula", prop.get("value").get("sval").asText());
            } else if (label.equals("Molecular Weight")) {
                result.put("molecularWeight", prop.get("value").get("sval").asText());
            } else if (label.equals("IUPAC Name")) {
                String name = prop.get("urn").get("name").asText();
                if (name.equals("Preferred")) {
                    result.put("iupacName", prop.get("value").get("sval").asText());
                }
            }
        }

        return result;
    }
}