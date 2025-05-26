package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.*;



public class AvoidOrderDuplicationTest {
 
        //in-memory chached No duplication map
        private static final Map<String, Set<String>> noDuplicationMap = new HashMap<>(); 
    
        public static void main(String[] args) throws Exception {
          if (args.length < 1) {
              System.out.println("Missing Args. Application Usage: java AvoidOrderDuplicationTest <argsJSON> ");
              System.out.println("Please Enter JSON payload to process: ");
              System.out.println("Sample JSON payload will be used if no input is provided.");
              AvoidOrderDuplicationTest.processOrder(jsonPayload);            
              //System.exit(1);
          }else{
              String jsonPayload = args[0];
              AvoidOrderDuplicationTest.processOrder(jsonPayload);
          }
        }
    
        private static void processOrder(String jsonPayload) throws Exception{
          ObjectMapper mapper = new ObjectMapper();
          JsonNode root = mapper.readTree(jsonPayload);
          String outletId = root.get("outletId").asText();
          LocalDate today = LocalDate.now();
  
          List<String> productQuantityList = new ArrayList<>();
          for (JsonNode item : root.get("item")) {
              String upc = item.get("UPC_Code").asText().replaceAll("\\s", "");
              String qty = item.get("qty").asText();
              productQuantityList.add(upc + ":" + qty);
          }
  
          Collections.sort(productQuantityList);
          String fingerprint = String.join("|", productQuantityList);
          String hash = sha256(fingerprint);
  
          String key = outletId + "::" + today;
  
          noDuplicationMap.putIfAbsent(key, new HashSet<>());
          if (noDuplicationMap.get(key).contains(hash)) {
              System.out.println("ATTENTION: Duplicate order detected for outlet " + outletId);
          }else{
              noDuplicationMap.get(key).add(hash);
              System.out.println("SUCCESS: Order accepted for outlet " + outletId);
          }

        }
        private static String sha256(String input) throws Exception {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        }

        private static String jsonPayload = """
          {
            "totalOrderValue": "80.00",
            "totalDiscount": "0.0",
            "totalTax": "4.00",
            "movFee": "15.00",
            "deliveryFee": "10.00",
            "netInvoice": "109.00",
            "outletId": "001234567",
            "item": [
              {
                "UPC_Code": "049000012781",
                "desc": "Coca-Cola Soda, 12 Fl. Oz., 24 Count",
                "qty": "50",
                "basePrice": "2.00",
                "itemPrice": "2.00",
                "adjustments": []
              },
              {
                "UPC_Code": "049000064971",
                "desc": "Coca-Cola Zero, 24 ct, 7.5 FL OZ Mini-Can",
                "qty": "30",
                "basePrice": "2.00",
                "itemPrice": "0.00",
                "adjustments": [
                  {
                    "adjustment": "Buy one get one",
                    "amount": "-5.00",
                    "desc": ""
                  }
                ]
              }
            ]
          }
          """;

}
        