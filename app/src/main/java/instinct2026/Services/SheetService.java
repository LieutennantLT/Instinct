package instinct2026.Services;

import instinct2026.API.GoogleSheetWriter;

public class SheetService {

    private GoogleSheetWriter writer;

    public SheetService(String sheetId, String credentialsPath) {
        try {
            this.writer = new GoogleSheetWriter(sheetId, credentialsPath);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to initialize GoogleSheetWriter.");
            this.writer = null;
        }
    }

    public void logTeamEPA(int team, double unitless, double epa) {
        if (writer == null) return;

        try {
            writer.appendRow(team, unitless, epa);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to write to Google Sheets.");
        }
    }
    
}