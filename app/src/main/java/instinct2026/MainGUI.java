package instinct2026;

import instinct2026.Constants.EPAConsts;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainGUI extends Application {

    private static final String SHEET_ID = "19hl5J7xm4Fv2H4aRIOIRHEUCnv1vjzn4pPNrc_67r1g";
    private static final String CREDENTIALS_PATH =
        "C:\\Users\\FRC\\Instinct2026\\credentials.json";

    @Override
    public void start(Stage stage) {

        // Setup EPA conversion table
        EPAConsts.EPA_Conversion_Tree.setupMaps();

        // UI Elements
        TextField teamInput = new TextField();
        teamInput.setPromptText("Enter team number");

        Button fetchButton = new Button("Get EPA");

        Label resultLabel = new Label("Enter a team number and press the button");

        // Button action
        fetchButton.setOnAction(e -> {
            try {
                int teamNumber = Integer.parseInt(teamInput.getText());

                // Get data
                double unitlessEPA = StatboticsAPI.getUnitlessEPA(teamNumber);
                double approxEPA = StatboticsAPI.getEPA(unitlessEPA);

                // Display results
                resultLabel.setText(
                    "Team " + teamNumber + "\n" +
                    "Unitless EPA: " + unitlessEPA + "\n" +
                    "Approx EPA: " + approxEPA
                );

                // Write to Google Sheets
                GoogleSheetWriter writer =
                    new GoogleSheetWriter(SHEET_ID, CREDENTIALS_PATH);

                writer.appendRow(teamNumber, unitlessEPA, approxEPA);

            } catch (NumberFormatException ex) {
                resultLabel.setText("Invalid input. Please enter a number.");
            } catch (Exception ex) {
                resultLabel.setText("Error fetching data or writing to sheet.");
                ex.printStackTrace();
            }
        });

        // Layout
        VBox layout = new VBox(10);
        layout.getChildren().addAll(teamInput, fetchButton, resultLabel);

        Scene scene = new Scene(layout, 350, 200);

        stage.setTitle("FRC Strategy Tool");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}