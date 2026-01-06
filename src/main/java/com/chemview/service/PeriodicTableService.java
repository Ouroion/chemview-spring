package com.chemview.service;

import com.chemview.model.Element;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PeriodicTableService {

    @Value("${periodic.table.api.key}")
    private String apiKey;

    @Value("${periodic.table.url}")
    private String ptURL;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<Element> elements = new ArrayList<>();

    @PostConstruct
    public void init() {
        loadElements();
    }

    private void loadElements() {
        try {
            InputStream inputStream = getClass().getClassLoader()
                    .getResourceAsStream("elements.txt");
            
            if (inputStream == null) {
                throw new RuntimeException("elements.txt not found in resources");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 2) {
                    String symbol = parts[0];
                    String name = parts[1];
                    elements.add(new Element(name, symbol));
                }
            }
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load elements", e);
        }
    }

    public String checkStringSymbol(String input) {
        if (input.length() == 1 || input.length() == 2) {
            for (Element element : elements) {
                if (element.getSymbol().equalsIgnoreCase(input)) {
                    return element.getName();
                }
            }
            return input + " is not a valid symbol in the periodic table.";
        }
        return input;
    }

    public Map<String, Object> getElementInfo(String input) throws Exception {
        String element = checkStringSymbol(input);
        String encodedElement = URLEncoder.encode(element, StandardCharsets.UTF_8);
        String ptURLParams = ptURL + "?name=" + encodedElement;

        URL url = new URL(ptURLParams);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("x-api-key", apiKey);
        conn.setRequestProperty("Accept", "application/json");

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
        JsonNode data = response.get("data");
        
        Map<String, Object> result = new HashMap<>();
        result.put("name", data.get("name").asText());
        result.put("appearance", data.get("appearance").asText());
        result.put("electronConfiguration", data.get("electron_configuration").asText());
        
        return result;
    }
}