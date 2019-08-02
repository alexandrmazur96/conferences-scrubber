import utils.EnvConfig;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Map;

/**
 * Project - https://github.com/tech-conferences/conference-data
 */
public class Main {

    public static void main(String[] args) throws Exception {
        int year = Main.handleArgs(args);
        Map<String, String> config = Main.initiateConfig();

        Application app = new Application(config);

        if (year == 0) {
            app.setIsNeedHandleAllYears();
        } else {
            app.setHandleYear(year);
        }
        app.executeAsync();
//        app.execute();
    }

    /**
     * Parse .env project file
     *
     * @return configuration hash map
     * @throws Exception usually when .env file is not present in project.
     */
    private static Map<String, String> initiateConfig() throws Exception {
        InputStream stream = Main.class.getResourceAsStream("/.env");
        EnvConfig envConfigUtil = new EnvConfig(stream);

        return envConfigUtil.getConfig();
    }

    /**
     * Handle arguments passed into script.
     *
     * @param args passed arguments
     * @return handled year
     */
    private static int handleArgs(String[] args) {
        if (args.length == 0) {
            return Calendar.getInstance().get(Calendar.YEAR);
        }

        if (args[0].equals("all")) {
            return 0;
        }

        return Integer.parseInt(args[0]);
    }
}