package instinct2026.API;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

public class GoogleSheetWriter {

    private final String sheetId;
    private final Sheets service;

    public GoogleSheetWriter(String sheetId, String credentialsPath) throws Exception {
        this.sheetId = sheetId;

        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(credentialsPath))
                .createScoped(Collections.singletonList("https://www.googleapis.com/auth/spreadsheets"));

        service = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, credential)
                .setApplicationName("FRC Scouting Tool")
                .build();
    }

    // Updates a row: Team | Unitless EPA | Approx EPA
     
        public void appendEPARow(int teamNumber, double approxEPA) throws Exception {

        ValueRange body = new ValueRange().setValues(
        Collections.singletonList(
        Arrays.asList(
                teamNumber,
                approxEPA
        )
        )
        );

        service.spreadsheets().values().update(sheetId, "Sheet1!A2:B2", body)
        .setValueInputOption("RAW").execute();
        }

        public void appendCache(Integer key, Double value) throws Exception {

        ValueRange body = new ValueRange().setValues(
        Collections.singletonList(
        Arrays.asList(
               key,
               value
        )
        )
        );

        service.spreadsheets().values().append(sheetId, "Sheet1!E2:F2", body)
        .setValueInputOption("RAW").execute();
        }

        public void pushCache(Map<Integer,Double> cache) throws Exception {
                service.spreadsheets().values().clear(getSheetId(), "Sheet1!E2:F2", null).execute();

                for(Map.Entry<Integer,Double> entry : cache.entrySet()) {
                        appendCache(entry.getKey(), entry.getValue());
                }
        }

        public Map<Integer,Double> pullCache() throws Exception{
                Map<Integer, Double> newCache = new TreeMap<>();
                
                ValueRange values = readAll();
                if(values.getValues() == null) return newCache;

                for(var row : values.getValues()){
                        if(row.size() < 2) continue;

                        int team = Integer.parseInt(row.get(0).toString());
                        double epa = Double.parseDouble(row.get(1).toString());

                        newCache.put(team,epa);
                }
                return newCache;
                
        }

        public ValueRange readAll() throws Exception {
        return service.spreadsheets().values().get(sheetId, "Sheet1!E:F").execute();

        }

        public String getSheetId(){
        return sheetId;

        }

        public Sheets getService() {
        return service;

        }

}
