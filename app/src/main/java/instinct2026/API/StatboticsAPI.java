package instinct2026.API;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.wpi.first.math.interpolation.InterpolatingDoubleTreeMap;
import edu.wpi.first.math.interpolation.InterpolatingTreeMap;
import instinct2026.Constants.EPAConsts;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class StatboticsAPI {

    private static final String TEAM_URL = "https://api.statbotics.io/v3/site/team/%d";

    /**
     * Returns unitless EPA for a team
     */
    public static double getUnitlessEPA(int teamNumber) throws Exception {
        String urlString = String.format(TEAM_URL, teamNumber);
        String response = fetch(urlString);

        if (response == null) {
            throw new RuntimeException("Failed to fetch data from Statbotics.");
        }

        JsonObject json = JsonParser.parseString(response).getAsJsonObject();
        JsonObject team = json.getAsJsonObject("team");

        // Try team_years first (most accurate)
        if (json.has("team_years")) {
            JsonArray years = json.getAsJsonArray("team_years");
            int currentYear = 2026;

            for (int i = 0; i < years.size(); i++) {
                JsonObject y = years.get(i).getAsJsonObject();
                if (y.get("year").getAsInt() == currentYear &&
                    y.has("unitless_epa") &&
                    !y.get("unitless_epa").isJsonNull()) {

                    return y.get("unitless_epa").getAsDouble();
                }
            }
        }

        // Fallback to norm_epa
        if (team.has("norm_epa")) {
            JsonObject norm = team.getAsJsonObject("norm_epa");
            if (norm.has("current")) {
                return norm.get("current").getAsDouble();
            }
        }

        throw new RuntimeException("No EPA data found for team " + teamNumber);
    }

    /**
     * Returns regular EPA based on interpolated values from unitless EPA
     */
    public static double getEPA(double unitlessEPA){

      return Math.round((EPAConsts.EPA_Conversion_Tree.epaConversionTable.get(unitlessEPA) * 10.0))/10.0;
    }

    /**
     * Helper method for HTTP GET
     */
    private static String fetch(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        if (con.getResponseCode() != 200) {
            System.out.println("Statbotics API returned: " + con.getResponseCode());
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder content = new StringBuilder();
        String line;

        while ((line = in.readLine()) != null) {
            content.append(line);
        }

        in.close();
        con.disconnect();

        return content.toString();
    }
}