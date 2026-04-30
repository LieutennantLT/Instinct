package instinct2026.Services;

import java.util.Map;

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

    public void logTeamEPA(int team, double epa) {
        if (writer == null) return;

        try {
            writer.appendEPARow(team, epa);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to write to Google Sheets.");
        }
    }

    public void logCache(Map<Integer, Double> cache){
        if (writer == null) return;

        try {
            writer.pushCache(cache);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to write to Google Sheets.");
        }

    }

    public Map<Integer,Double> pullCache() throws Exception{
            return writer.pullCache();
            
    }


    
}