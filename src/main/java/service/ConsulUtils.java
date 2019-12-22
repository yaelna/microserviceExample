package service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ConsulUtils {
    private static String serviceId;

    static void deregisterFromConsul() {
        try {
            URL url = new URL("http://localhost:8500/v1/agent/service/deregister/" + serviceId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            Bootstrap.logger.info("deregister got code " + con.getResponseCode());
        } catch (IOException e) {
            Bootstrap.logger.info("failed to register with consul due to: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static void registerWithConsul() {
        try {
            URL url = new URL("http://localhost:8500/v1/agent/service/register");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = getJsonConfig().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            Bootstrap.logger.info("register got code " + con.getResponseCode());

        } catch (IOException e) {
            Bootstrap.logger.info("failed to register with consul due to: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static String getJsonConfig() {
        serviceId = "hello1";
        return "{\n" +
                "  \"ID\": \"" + serviceId + "\",\n" +
                "  \"Name\": \"helloService\",\n" +
                "  \"Tags\": [\n" +
                "    \"rest\",\n" +
                "    \"v1\"\n" +
                "  ],\n" +
                "  \"Port\": 8080\n" +
                "}\n" +
                "\n";
    }
}
