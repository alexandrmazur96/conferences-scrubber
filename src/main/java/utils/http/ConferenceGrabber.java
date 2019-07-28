package utils.http;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class ConferenceGrabber {
    private String url;

    private static HttpURLConnection httpURLConnection;

    public void setUrl(String url) {
        this.url = url;
    }

    public Future<String> grabConferencesAsync(int year, String platform) {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        try {
            String scrubUrl = this.url + '/' + year + '/' + platform + ".json";
            URL url = new URL(scrubUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("GET");

            StringBuilder content = new StringBuilder();

            try (BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
                String line;

                while ((line = in.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            } catch (FileNotFoundException fnfE) {
                fnfE.printStackTrace();
            }

            System.out.println("Grabbed [ " + year + ", " + platform + "]");

            completableFuture.complete(content.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            httpURLConnection.disconnect();
        }

        return completableFuture;
    }

    /**
     * Make request to given URL to grab conferences list matched given year and platform
     *
     * @param year     for what year you want to grab conferences
     * @param platform conference platform, e.g., android, php, java, etc
     * @return conferences in JSON format
     * @throws IOException if request failed and we can't read stream.
     */
    public String grabConferences(int year, String platform) throws IOException {
        try {

            String scrubUrl = this.url + '/' + year + '/' + platform + ".json";
            URL url = new URL(scrubUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod("GET");

            StringBuilder content;

            try (BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {

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
