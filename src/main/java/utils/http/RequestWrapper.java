package utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestWrapper {
    private final String url = "https://raw.githubusercontent.com/tech-conferences/conference-data/master/conferences/";

    private static HttpURLConnection httpURLConnection;

    public String makeRequest(int year, String platform) throws IOException {
        try {

            String scrubUrl = this.url + '/' + year + '/' + platform + ".json";
            URL url = new URL(scrubUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("GET");

            StringBuilder content;

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpURLConnection.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = in.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            return content.toString();
        } finally {
            httpURLConnection.disconnect();
        }
    }
}
