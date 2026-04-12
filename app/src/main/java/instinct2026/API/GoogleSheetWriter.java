package instinct2026.API;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Collections;

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

    /**
     * Updates a row: Team | Unitless EPA | Approx EPA
     */
    public void appendRow(int teamNumber, double unitlessEPA, double approxEPA) throws Exception {

        ValueRange body = new ValueRange().setValues(
                Collections.singletonList(
                        Arrays.asList(
                                teamNumber,
                                unitlessEPA,
                                approxEPA
                        )
                )
        );

        service.spreadsheets().values()
                .update(sheetId, "Sheet1!A2:C2", body)
                .setValueInputOption("RAW")
                .execute();
    }
}