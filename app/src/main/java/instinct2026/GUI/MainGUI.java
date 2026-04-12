package instinct2026.GUI;

import instinct2026.Constants.EPAConsts;
import instinct2026.Services.*;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.concurrent.Task;

public class MainGUI extends Application {

    private static final String SHEET_ID = "19hl5J7xm4Fv2H4aRIOIRHEUCnv1vjzn4pPNrc_67r1g";
    private static final String CREDENTIALS_PATH =
            "C:\\Users\\FRC\\Instinct2026\\credentials.json";

    private final EPAService epaService = new EPAService();
    private final MatchSimulator simulator = new MatchSimulator();
    private SheetService sheetService;

    @Override
    public void start(Stage stage) {

        EPAConsts.EPA_Conversion_Tree.setupMaps();

        try {
            sheetService = new SheetService(SHEET_ID, CREDENTIALS_PATH);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to initialize Google Sheets service.");
        }

        // =========================
        // TAB 1: SINGLE TEAM EPA
        // =========================
        TextField teamInput = new TextField();
        teamInput.setPromptText("Enter team number");

        Button fetchButton = new Button("Get EPA");
        Label resultLabel = new Label("Enter a team number and press the button");

        fetchButton.setOnAction(e -> {
            try {
                int team = Integer.parseInt(teamInput.getText());

                resultLabel.setText("Loading...");

                Task<Double> task = new Task<>() {
                    @Override
                    protected Double call() throws Exception {
                        return epaService.getEPA(team);
                    }
                };

                task.setOnSucceeded(ev -> {
                    double epa = task.getValue();

                    resultLabel.setText(
                            "Team " + team + "\nEPA: " + String.format("%.2f", epa)
                    );

                    if (sheetService != null) {
                        sheetService.logTeamEPA(team, epa, epa);
                    }
                });

                task.setOnFailed(ev -> {
                    resultLabel.setText("API error (Statbotics may be down)");
                    task.getException().printStackTrace();
                });

                new Thread(task).start();

            } catch (NumberFormatException ex) {
                resultLabel.setText("Invalid input.");
            }
            });

        VBox tab1Layout = new VBox(10, teamInput, fetchButton, resultLabel);
        tab1Layout.setPadding(new Insets(10));

        Tab tab1 = new Tab("Single Team EPA", tab1Layout);

        // =========================
        // TAB 2: MATCH SIMULATOR
        // =========================

        Image fieldImage = new Image("file:C:/Users/FRC/Instinct2026/Images/Field.png");
        ImageView bg = new ImageView(fieldImage);

        bg.setPreserveRatio(false);
        bg.fitWidthProperty().bind(stage.widthProperty());
        bg.fitHeightProperty().bind(stage.heightProperty());

        Pane overlay = new Pane();

        // Trial number
        TextField trials = new TextField(); Label trialsLabel = new Label("# of sims:");

        // Red alliance
        TextField r1 = new TextField(); Label r1EPA = new Label("-");
        TextField r2 = new TextField(); Label r2EPA = new Label("-");
        TextField r3 = new TextField(); Label r3EPA = new Label("-");

        // Blue alliance
        TextField b1 = new TextField(); Label b1EPA = new Label("-");
        TextField b2 = new TextField(); Label b2EPA = new Label("-");
        TextField b3 = new TextField(); Label b3EPA = new Label("-");

        r1.setPromptText("R1");
        r2.setPromptText("R2");
        r3.setPromptText("R3");

        b1.setPromptText("B1");
        b2.setPromptText("B2");
        b3.setPromptText("B3");

        trials.setPromptText("simulations");

        // Styling
        String redStyle = "-fx-background-color: rgba(255, 0, 0, 0.3);";
        String blueStyle = "-fx-background-color: rgba(0, 0, 255, 0.3);";

        r1.setStyle(redStyle); r2.setStyle(redStyle); r3.setStyle(redStyle);
        b1.setStyle(blueStyle); b2.setStyle(blueStyle); b3.setStyle(blueStyle);

        // Labels
        Label redTotal = new Label("Red EPA: 0");
        Label blueTotal = new Label("Blue EPA: 0");
        Label winChance = new Label("Win %: -");

        winChance.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        trials.setStyle("-fx-font-size: 14px;");
        trialsLabel.setStyle("-fx-font-size: 14px;");

        Button simulateBtn = new Button("Simulate");

        // =========================
        // POSITION ELEMENTS
        // =========================

        // RED
        setRelativePosition(r1, overlay, 0.10, 0.20);
        setRelativePosition(r2, overlay, 0.10, 0.40);
        setRelativePosition(r3, overlay, 0.10, 0.60);

        setRelativePosition(r1EPA, overlay, 0.20, 0.20);
        setRelativePosition(r2EPA, overlay, 0.20, 0.40);
        setRelativePosition(r3EPA, overlay, 0.20, 0.60);

        // BLUE
        setRelativePosition(b1, overlay, 0.65, 0.20);
        setRelativePosition(b2, overlay, 0.65, 0.40);
        setRelativePosition(b3, overlay, 0.65, 0.60);

        setRelativePosition(b1EPA, overlay, 0.75, 0.20);
        setRelativePosition(b2EPA, overlay, 0.75, 0.40);
        setRelativePosition(b3EPA, overlay, 0.75, 0.60);

        // Bottom
        setRelativePosition(redTotal, overlay, 0.15, 0.80);
        setRelativePosition(blueTotal, overlay, 0.70, 0.80);
        setRelativePosition(winChance, overlay, 0.35, 0.80);
        setRelativePosition(trials, overlay, 0.30, 0.05);
        setRelativePosition(trialsLabel, overlay, 0.19, 0.05);

        setRelativePosition(simulateBtn, overlay, 0.65, 0.05);

        //ProgressIndicator spinner = new ProgressIndicator();
        //spinner.setVisible(false);

        // =========================
        // SIMULATION LOGIC
        // =========================
        simulateBtn.setOnAction(e -> {

            winChance.setText("Simulating...");
            //spinner.setVisible(true);

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                            
                    int trialsnums = Integer.parseInt(trials.getText());

                    double R1 = updateEPA(r1, r1EPA);
                    double R2 = updateEPA(r2, r2EPA);
                    double R3 = updateEPA(r3, r3EPA);

                    double B1 = updateEPA(b1, b1EPA);
                    double B2 = updateEPA(b2, b2EPA);
                    double B3 = updateEPA(b3, b3EPA);

                    MatchSimulator.Result result = simulator.simulate(R1, R2, R3, B1, B2, B3, trialsnums);

                    javafx.application.Platform.runLater(() -> {
                        //spinner.setVisible(false);
                        redTotal.setText("Red EPA: " + round(result.redEPA));
                        blueTotal.setText("Blue EPA: " + round(result.blueEPA));
                        winChance.setText(String.format("Red Win Chance: %.1f%%", result.redWinProb));
                });

            return null;
        }
    };

        task.setOnFailed(ev -> {
            winChance.setText("Simulation error");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    });

        overlay.getChildren().addAll(
                r1, r2, r3, r1EPA, r2EPA, r3EPA,
                b1, b2, b3, b1EPA, b2EPA, b3EPA,
                redTotal, blueTotal, winChance,
                simulateBtn, trials, trialsLabel
        );

        StackPane simulatorPane = new StackPane(bg, overlay);

        Tab tab2 = new Tab("Match Simulator", simulatorPane);

        // =========================
        // TAB 3: CACHE CONSOLE
        // =========================

        TextArea cacheView = new TextArea();
        cacheView.setEditable(false);
        cacheView.setPrefHeight(300);

        Button refreshBtn = new Button("Refresh Cache");
        Button clearBtn = new Button("Clear Cache (Backup)");
        Button restoreBtn = new Button("Restore Backup");

        Label statusLabel = new Label("");

        // Format cache nicely
        Runnable refreshCacheDisplay = () -> {
            StringBuilder sb = new StringBuilder();

            sb.append("=== CURRENT CACHE ===\n");
            epaService.getCacheSnapshot().forEach((team, epa) ->
                    sb.append("Team ").append(team)
                    .append(" -> ").append(String.format("%.2f", epa))
                    .append("\n")
            );

            sb.append("\n=== BACKUP CACHE ===\n");
            epaService.getBackupSnapshot().forEach((team, epa) ->
                    sb.append("Team ").append(team)
                    .append(" -> ").append(String.format("%.2f", epa))
                    .append("\n")
            );

            cacheView.setText(sb.toString());
        };

        // Button actions
        refreshBtn.setOnAction(e -> {
            refreshCacheDisplay.run();
            statusLabel.setText("Cache refreshed");
        });

        clearBtn.setOnAction(e -> {
            epaService.clearCache();
            refreshCacheDisplay.run();
            statusLabel.setText("Cache cleared (moved to backup)");
        });

        restoreBtn.setOnAction(e -> {
            epaService.retrieveBackupCache();
            refreshCacheDisplay.run();
            statusLabel.setText("Backup restored to cache");
        });

        // Layout
        HBox buttons = new HBox(10, refreshBtn, clearBtn, restoreBtn);

        VBox tab3Layout = new VBox(10, buttons, cacheView, statusLabel);
        tab3Layout.setPadding(new Insets(10));

        Tab tab3 = new Tab("Cache Console", tab3Layout);

        // =========================
        TabPane tabPane = new TabPane(tab1, tab2, tab3);

        Scene scene = new Scene(tabPane, 600, 500);
        stage.setTitle("FRC Strategy Tool");
        stage.setScene(scene);
        stage.show();
    }

    

    // =========================
    // HELPERS
    // =========================

    private void setRelativePosition(Control node, Pane parent, double x, double y) {
        node.layoutXProperty().bind(parent.widthProperty().multiply(x));
        node.layoutYProperty().bind(parent.heightProperty().multiply(y));
    }

    private void setRelativePosition(Label node, Pane parent, double x, double y) {
        node.layoutXProperty().bind(parent.widthProperty().multiply(x));
        node.layoutYProperty().bind(parent.heightProperty().multiply(y));
    }

    private double updateEPA(TextField field, Label label) throws Exception {
        if (field.getText().isEmpty()) {
            label.setText("-");
            return 0;
        }

        int team = Integer.parseInt(field.getText());
        double epa = epaService.getEPA(team);

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