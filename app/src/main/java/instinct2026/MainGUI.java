package instinct2026;

import instinct2026.Constants.EPAConsts;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainGUI extends Application {

    private static final String SHEET_ID = "19hl5J7xm4Fv2H4aRIOIRHEUCnv1vjzn4pPNrc_67r1g";
    private static final String CREDENTIALS_PATH =
        "C:\\Users\\FRC\\Instinct2026\\credentials.json";

    @Override
    public void start(Stage stage) {

        EPAConsts.EPA_Conversion_Tree.setupMaps();

        // =========================
        // TAB 1: SINGLE TEAM EPA
        // =========================
        TextField teamInput = new TextField();
        teamInput.setPromptText("Enter team number");

        Button fetchButton = new Button("Get EPA");
        Label resultLabel = new Label("Enter a team number and press the button");

        fetchButton.setOnAction(e -> {
            try {
                int teamNumber = Integer.parseInt(teamInput.getText());

                double unitlessEPA = StatboticsAPI.getUnitlessEPA(teamNumber);
                double approxEPA = StatboticsAPI.getEPA(unitlessEPA);

                resultLabel.setText(
                    "Team " + teamNumber + "\n" +
                    "Unitless EPA: " + unitlessEPA + "\n" +
                    "Approx EPA: " + approxEPA
                );

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

        VBox tab1Layout = new VBox(10, teamInput, fetchButton, resultLabel);
        tab1Layout.setPadding(new Insets(10));

        Tab tab1 = new Tab("Single Team EPA", tab1Layout);

        // =========================
        // TAB 2: MATCH SIMULATOR
        // =========================

        // Background image
        Image fieldImage = new Image("file:C:/Users/FRC/Instinct2026/Images/Field.png");
        ImageView bg = new ImageView(fieldImage);

        bg.setPreserveRatio(false);
        bg.fitWidthProperty().bind(stage.widthProperty());
        bg.fitHeightProperty().bind(stage.heightProperty());

        Pane overlay = new Pane();

        // Red alliance inputs
        TextField r1 = new TextField(); Label r1EPA = new Label("-");
        TextField r2 = new TextField(); Label r2EPA = new Label("-");
        TextField r3 = new TextField(); Label r3EPA = new Label("-");

        // Blue alliance inputs
        TextField b1 = new TextField(); Label b1EPA = new Label("-");
        TextField b2 = new TextField(); Label b2EPA = new Label("-");
        TextField b3 = new TextField(); Label b3EPA = new Label("-");

        r1.setPromptText("R1");
        r2.setPromptText("R2");
        r3.setPromptText("R3");

        b1.setPromptText("B1");
        b2.setPromptText("B2");
        b3.setPromptText("B3");

        // Styling (semi-transparent boxes)
        String redStyle = "-fx-background-color: rgba(255,0,0,0.3);";
        String blueStyle = "-fx-background-color: rgba(0,0,255,0.3);";

        r1.setStyle(redStyle); r2.setStyle(redStyle); r3.setStyle(redStyle);
        b1.setStyle(blueStyle); b2.setStyle(blueStyle); b3.setStyle(blueStyle);

        // Totals + win %
        Label redTotal = new Label("Red EPA: 0");
        Label blueTotal = new Label("Blue EPA: 0");
        Label winChance = new Label("Win %: -");

        winChance.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button simulateBtn = new Button("Simulate");

        // =========================
        // POSITION ELEMENTS
        // =========================

        // RED SIDE
        setRelativePosition(r1, overlay, 0.10, 0.20);
        setRelativePosition(r2, overlay, 0.10, 0.40);
        setRelativePosition(r3, overlay, 0.10, 0.60);

        setRelativePosition(r1EPA, overlay, 0.20, 0.20);
        setRelativePosition(r2EPA, overlay, 0.20, 0.40);
        setRelativePosition(r3EPA, overlay, 0.20, 0.60);

        // BLUE SIDE
        setRelativePosition(b1, overlay, 0.65, 0.20);
        setRelativePosition(b2, overlay, 0.65, 0.40);
        setRelativePosition(b3, overlay, 0.65, 0.60);

        setRelativePosition(b1EPA, overlay, 0.75, 0.20);
        setRelativePosition(b2EPA, overlay, 0.75, 0.40);
        setRelativePosition(b3EPA, overlay, 0.75, 0.60);

        // Bottom info
        setRelativePosition(redTotal, overlay, 0.15, 0.80);
        setRelativePosition(blueTotal, overlay, 0.70, 0.80);
        setRelativePosition(winChance, overlay, 0.35, 0.80);

        // Button
        setRelativePosition(simulateBtn, overlay, 0.45, 0.05);

        // =========================
        // SIMULATION LOGIC
        // =========================
        simulateBtn.setOnAction(e -> {
            try {
                double redEPA = 0;
                double blueEPA = 0;

                redEPA += updateEPA(r1, r1EPA);
                redEPA += updateEPA(r2, r2EPA);
                redEPA += updateEPA(r3, r3EPA);

                blueEPA += updateEPA(b1, b1EPA);
                blueEPA += updateEPA(b2, b2EPA);
                blueEPA += updateEPA(b3, b3EPA);

                redTotal.setText("Red EPA: " + round(redEPA));
                blueTotal.setText("Blue EPA: " + round(blueEPA));

                double diff = redEPA - blueEPA;
                double k = 0.125;
                double redWinProb = 1.0 / (1.0 + Math.exp(-diff / (k * (redEPA+blueEPA))));

                winChance.setText(
                    String.format("Red Win Chance: %.1f%%", redWinProb * 100)
                );

            } catch (Exception ex) {
                winChance.setText("Error computing match.");
                ex.printStackTrace();
            }
        });

        // Add everything
        overlay.getChildren().addAll(
            r1, r2, r3, r1EPA, r2EPA, r3EPA,
            b1, b2, b3, b1EPA, b2EPA, b3EPA,
            redTotal, blueTotal, winChance,
            simulateBtn
        );

        StackPane simulatorPane = new StackPane(bg, overlay);

        Tab tab2 = new Tab("Match Simulator", simulatorPane);

        // =========================
        // TABPANE
        // =========================
        TabPane tabPane = new TabPane(tab1, tab2);

        Scene scene = new Scene(tabPane, 600, 500);
        stage.setTitle("FRC Strategy Tool");
        stage.setScene(scene);
        stage.show();
    }

    // =========================
    // HELPERS
    // =========================

    private void setRelativePosition(Control node, Pane parent, double xPercent, double yPercent) {
        node.layoutXProperty().bind(parent.widthProperty().multiply(xPercent));
        node.layoutYProperty().bind(parent.heightProperty().multiply(yPercent));
    }

    private void setRelativePosition(Label node, Pane parent, double xPercent, double yPercent) {
        node.layoutXProperty().bind(parent.widthProperty().multiply(xPercent));
        node.layoutYProperty().bind(parent.heightProperty().multiply(yPercent));
    }

    private double updateEPA(TextField field, Label label) throws Exception {
        if (field.getText().isEmpty()) {
            label.setText("-");
            return 0;
        }

        int team = Integer.parseInt(field.getText());
        double unitless = StatboticsAPI.getUnitlessEPA(team);
        double epa = StatboticsAPI.getEPA(unitless);

        label.setText(String.format("%.1f", epa));
        return epa;
    }

    private double round(double val) {
        return Math.round(val * 10.0) / 10.0;
    }

    public static void main(String[] args) {
        launch();
    }
}