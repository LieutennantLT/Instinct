package instinct2026;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Main {

    // Statbotics API example URL for team 254 in 2026
    private static final String STATBOTICS_URL = "https://api.statbotics.io/v3/site/team/4256";
    private static final String SHEET_ID = "19hl5J7xm4Fv2H4aRIOIRHEUCnv1vjzn4pPNrc_67r1g"; // Replace with your sheet ID
    private static final String RANGE = "Sheet1!A1"; // Cell to write EPA

    public static void main(String[] args) {
        try {
            double epa = getEPAFromStatbotics();
            System.out.println("EPA pulled: " + epa);
            writeToGoogleSheet(epa);
            System.out.println("EPA written to Google Sheet!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double getEPAFromStatbotics() throws Exception {
        URL url = new URL(STATBOTICS_URL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int status = con.getResponseCode();
        if (status != 200) {
            throw new RuntimeException("HTTP GET failed: " + status);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();

        // Parse JSON
        JsonObject json = JsonParser.parseString(content.toString()).getAsJsonObject();

    // Navigate to team -> norm_epa -> current
    JsonObject team = json.getAsJsonObject("team");
    JsonObject normEpa = team.getAsJsonObject("norm_epa");
    double currentEPA = normEpa.get("current").getAsDouble();

return currentEPA;
    }

    private static void writeToGoogleSheet(double value) throws Exception {
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream("credentials.json"))
                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/spreadsheets"));

        Sheets service = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credential)
                .setApplicationName("FRC Scouting Tool")
                .build();

        ValueRange body = new ValueRange().setValues(Collections.singletonList(Collections.singletonList(value)));
        service.spreadsheets().values().update(SHEET_ID, RANGE, body)
                .setValueInputOption("RAW")
                .execute();
    }
}