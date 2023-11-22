package com.example.ecommerce.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;
import java.util.Map;
@Service
public class PaypalService {
    private final String  BASE = "https://api-m.sandbox.paypal.com";

    private String getAuth(String client_id, String app_secret) {
        String auth = client_id + ":" + app_secret;
        return Base64.getEncoder().encodeToString(auth.getBytes());
    }
    public String generateAccessToken() {
        String apiKey = "AebeC-DmDimgU0D7bOCorJNUBSo0a-IXOLm07H5NhDclVNwZsbEYfJoQqb_IaEJqA5Jiz4fX3pBHAoTJ";
        String secretKey = "EA1Esn9SGjs220tvL_8vzsF3QdIi0YpyhbS_Beof89j40DKp5CDZ5HC-LjENOY0dVBZz_9U59EBKerGw";
        String auth = this.getAuth(
                apiKey,
                secretKey
        );
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + auth);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        HttpEntity<?> request = new HttpEntity<>(requestBody, headers);
        requestBody.add("grant_type", "client_credentials");

        ResponseEntity<String> response = restTemplate.postForEntity(
                BASE +"/v1/oauth2/token",
                request,
                String.class
        );
        return new JSONObject(response.getBody()).getString("access_token");
    }

    public String createJsonPayload(Double total, String succesURL, String failedUrl) {

        JSONObject payload = new JSONObject();

        // Set the values for the JSON payload
        payload.put("intent", "CAPTURE");

        // Create the purchase_units object
        JSONObject purchaseUnit = new JSONObject();
        JSONArray itemsArray = new JSONArray();
        JSONObject item = new JSONObject();
        JSONObject unitAmount = new JSONObject();
        JSONObject amount = new JSONObject();
        JSONObject breakdown = new JSONObject();
        JSONObject itemTotal = new JSONObject();
        var amout =  Math.round(total*0.000043 * 100.0) / 100.0;
        unitAmount.put("currency_code", "USD");
        unitAmount.put("value", amout);

        item.put("name", "T-Shirt");
        item.put("description", "No description");
        item.put("quantity", "1");
        item.put("unit_amount", unitAmount);

        itemsArray.put(item);

        itemTotal.put("currency_code", "USD");
        itemTotal.put("value", amout);

        breakdown.put("item_total", itemTotal);

        amount.put("currency_code", "USD");
        amount.put("value", amout);
        amount.put("breakdown", breakdown);

        purchaseUnit.put("items", itemsArray);
        purchaseUnit.put("amount", amount);

        JSONArray purchaseUnitsArray = new JSONArray();
        purchaseUnitsArray.put(purchaseUnit);

        payload.put("purchase_units", purchaseUnitsArray);

        JSONObject applicationContext = new JSONObject();
        applicationContext.put("return_url", succesURL);
        applicationContext.put("cancel_url", failedUrl);

        payload.put("application_context", applicationContext);

        return payload.toString();
    }
    public ResponseEntity<Object> createOrder(String requestJson) {
        String accessToken = generateAccessToken();
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        //JSON String
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                BASE + "/v2/checkout/orders",
                HttpMethod.POST,
                entity,
                Object.class
        );

            return response;

    }

    public String capturePayment(@PathVariable("orderId") String orderId) {
        String accessToken = generateAccessToken();
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();

        headers.set("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                BASE + "/v2/checkout/orders/" + orderId + "/capture",
                HttpMethod.POST,
                entity,
                Object.class
        );
        System.out.println(response);

        if (response.getStatusCode() == HttpStatus.CREATED) {
            return getApproveLink(response);
        } else {
            return "Unavailable to get CREATE AN ORDER, STATUS CODE " + response.getStatusCode();
        }
    }
    public String getApproveLink(ResponseEntity<Object> response) {
        String approveLink = null;

        // Get the response body
        Object responseBody = response.getBody();

        if (responseBody instanceof Map) {
            Map<String, Object> responseMap = (Map<String, Object>) responseBody;

            List<Map<String, String>> links = (List<Map<String, String>>) responseMap.get("links");

            for (Map<String, String> link : links) {
                if ("approve".equals(link.get("rel"))) {
                    approveLink = link.get("href");
                    break;
                }
            }
        }

        return approveLink;
    }

}
