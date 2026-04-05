package instinct2026;

import instinct2026.Constants.EPAConsts;

public class Main {

    private static final String SHEET_ID = "19hl5J7xm4Fv2H4aRIOIRHEUCnv1vjzn4pPNrc_67r1g";
    private static final String CREDENTIALS_PATH =
    "C:\\Users\\FRC\\Instinct2026\\credentials.json";

    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("Usage: gradlew run --args=\"<team_number>\"");
            return;
        }

        int teamNumber = Integer.parseInt(args[0]);
        EPAConsts.EPA_Conversion_Tree.setupMaps();

        try {
            // Get unitless EPA
            double unitlessEPA = StatboticsAPI.getUnitlessEPA(teamNumber);

            // Convert to approximate standard EPA
            double approxEPA = StatboticsAPI.getEPA(unitlessEPA);
            //(unitlessEPA - 1500) + 43.8;

            System.out.println("Team " + teamNumber);
            System.out.println("Unitless EPA: " + unitlessEPA);
            System.out.println("Approx EPA: " + approxEPA);

            // Write to Google Sheets
            GoogleSheetWriter writer = new GoogleSheetWriter(SHEET_ID, CREDENTIALS_PATH);
            writer.appendRow(teamNumber, unitlessEPA, approxEPA);

            System.out.println("Data written to Google Sheet!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}