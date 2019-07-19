package utils;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class EnvConfig {
    private InputStream stream;

    public EnvConfig(InputStream stream) {
        this.stream = stream;
    }

    /**
     * Return project configuration
     * @return config
     */
    public HashMap<String, String> getConfig() throws Exception {
        return this.parseEnv();
    }

    /**
     * Parse given stream of .env file to config.
     * @return configuration hash map
     * @throws Exception when we can't load .env or .env is malformed
     */
    private HashMap<String, String> parseEnv() throws Exception {
        if (stream == null) {
            throw new Exception("Can't load .env file");
        }

        Scanner input;

        try {
            input = new Scanner(stream);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        HashMap<String, String> config = new HashMap<>();

        while (input.hasNextLine()) {
            String configLine = input.nextLine();
            String[] configArr = configLine.split("=");

            if (configArr.length < 2) {
                throw new Exception(".env file is malformed");
            }

            if (configArr.length > 2) {
                configArr[1] = String.join("", Arrays.copyOfRange(configArr, 1, configArr.length));
            }

            String key = configArr[0].replaceAll("(^\"|\"$)", "");
            String value = configArr[1].replaceAll("(^\"|\"$)", "");
            config.put(key, value);
        }

        return config;
    }
}
