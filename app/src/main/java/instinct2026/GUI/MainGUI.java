package instinct2026.GUI;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import instinct2026.Constants.EPAConsts;
import instinct2026.Services.*;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

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
                    resultLabel.setText("API error");
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
        bg.setFitHeight(300);
        bg.setFitWidth(600);

        Pane overlay = new Pane();
        overlay.setPrefHeight(0);
        
        //Graph setup
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Score Range");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Frequency");

        BarChart<String, Number> scoreChart = new BarChart<>(xAxis, yAxis);
        scoreChart.setTitle("Projected Score Distribution");
        scoreChart.setAnimated(false);
        scoreChart.setCategoryGap(0);
        scoreChart.setBarGap(0);
        scoreChart.setPrefHeight(320);
        //Input setup
        TextField trials = new TextField();
        Label trialsLabel = new Label("# of sims:");

        TextField r1 = new TextField(); Label r1EPA = new Label("-");
        TextField r2 = new TextField(); Label r2EPA = new Label("-");
        TextField r3 = new TextField(); Label r3EPA = new Label("-");

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

        //Styling setup
        String redStyle = "-fx-background-color: rgba(255, 0, 0, 0.3);";
        String blueStyle = "-fx-background-color: rgba(0, 0, 255, 0.3);";

        r1.setStyle(redStyle); r2.setStyle(redStyle); r3.setStyle(redStyle);
        b1.setStyle(blueStyle); b2.setStyle(blueStyle); b3.setStyle(blueStyle);

        //Label setup
        Label redTotal = new Label("Red EPA: 0");
        Label blueTotal = new Label("Blue EPA: 0");
        Label winChance = new Label("Win %: -");

        Button simulateBtn = new Button("Simulate");

        //Position setup
        setRelativePosition(r1, overlay, 0.10, 0.20);
        setRelativePosition(r2, overlay, 0.10, 0.40);
        setRelativePosition(r3, overlay, 0.10, 0.60);

        setRelativePosition(r1EPA, overlay, 0.20, 0.20);
        setRelativePosition(r2EPA, overlay, 0.20, 0.40);
        setRelativePosition(r3EPA, overlay, 0.20, 0.60);

        setRelativePosition(b1, overlay, 0.65, 0.20);
        setRelativePosition(b2, overlay, 0.65, 0.40);
        setRelativePosition(b3, overlay, 0.65, 0.60);

        setRelativePosition(b1EPA, overlay, 0.75, 0.20);
        setRelativePosition(b2EPA, overlay, 0.75, 0.40);
        setRelativePosition(b3EPA, overlay, 0.75, 0.60);

        setRelativePosition(redTotal, overlay, 0.15, 0.80);
        setRelativePosition(blueTotal, overlay, 0.70, 0.80);
        setRelativePosition(winChance, overlay, 0.35, 0.80);
        setRelativePosition(trials, overlay, 0.30, 0.05);
        setRelativePosition(trialsLabel, overlay, 0.19, 0.05);
        setRelativePosition(simulateBtn, overlay, 0.65, 0.05);

        simulateBtn.setOnAction(e -> {
            winChance.setText("Simulating...");

            Task<MatchSimulator.Result> task = new Task<>() {
                @Override
                protected MatchSimulator.Result call() throws Exception {
                    int trialCount = Integer.parseInt(trials.getText());

                    double R1 = updateEPA(r1, r1EPA);
                    double R2 = updateEPA(r2, r2EPA);
                    double R3 = updateEPA(r3, r3EPA);

                    double B1 = updateEPA(b1, b1EPA);
                    double B2 = updateEPA(b2, b2EPA);
                    double B3 = updateEPA(b3, b3EPA);

                    return simulator.simulate(R1, R2, R3, B1, B2, B3, trialCount);
                }
            };

            task.setOnSucceeded(ev -> {
                MatchSimulator.Result result = task.getValue();

                redTotal.setText("Red EPA: " + round(result.redEPA));
                blueTotal.setText("Blue EPA: " + round(result.blueEPA));
                winChance.setText(String.format("Red Win Chance: %.1f%%", result.redWinProb));

                XYChart.Series<String, Number> redSeries = new XYChart.Series<>();
                redSeries.setName("Red Alliance");

                XYChart.Series<String, Number> blueSeries = new XYChart.Series<>();
                blueSeries.setName("Blue Alliance");

                int binSize = 10;

                Map<Integer, Integer> redBins = new TreeMap<>();
                Map<Integer, Integer> blueBins = new TreeMap<>();

                for (double score : result.redScores) {
                    int bin = ((int) score / binSize) * binSize;
                    redBins.put(bin, redBins.getOrDefault(bin, 0) + 1);
                }

                for (double score : result.blueScores) {
                    int bin = ((int) score / binSize) * binSize;
                    blueBins.put(bin, blueBins.getOrDefault(bin, 0) + 1);
                }

                Set<Integer> allBins = new TreeSet<>();
                allBins.addAll(redBins.keySet());
                allBins.addAll(blueBins.keySet());

                for (int bin : allBins) {
                    String label = bin + "-" + (bin + binSize);

                    redSeries.getData().add(new XYChart.Data<>(
                            label,
                            redBins.getOrDefault(bin, 0)
                    ));

                    blueSeries.getData().add(new XYChart.Data<>(
                            label,
                            blueBins.getOrDefault(bin, 0)
                    ));
                }

                scoreChart.getData().clear();
                scoreChart.getData().addAll(redSeries, blueSeries);
                
                Platform.runLater(() -> {
                    scoreChart.applyCss();
                    scoreChart.layout();

                    //Style every red bar
                    for (XYChart.Data<String, Number> data : redSeries.getData()) {
                        if (data.getNode() != null) {
                            data.getNode().setStyle("-fx-bar-fill: red;");
                        }
                    }

                    //Style every blue bar
                    for (XYChart.Data<String, Number> data : blueSeries.getData()) {
                        if (data.getNode() != null) {
                            data.getNode().setStyle("-fx-bar-fill: blue;");
                        }
                    }

                    //Color legend text
                    scoreChart.lookupAll(".chart-legend-item").forEach(node -> {
                    if (node instanceof Label label) {
                        Region symbol = (Region) label.lookup(".chart-legend-item-symbol");

                        if (label.getText().equals("Red Alliance")) {
                            label.setStyle("-fx-text-fill: red;");
                            if (symbol != null) {
                                symbol.setStyle("-fx-background-color: red;");
                            }

                        } else if (label.getText().equals("Blue Alliance")) {
                            label.setStyle("-fx-text-fill: blue;");
                            if (symbol != null) {
                                symbol.setStyle("-fx-background-color: blue;");
                            }
                        }
                    }
                });
            });

            });

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

        VBox simulatorLayout = new VBox(10, simulatorPane, scoreChart);
        simulatorLayout.setPadding(new Insets(10));

        Tab tab2 = new Tab("Match Simulator", simulatorLayout);

        // =========================
        // TAB 3: CACHE CONSOLE
        // =========================
        TextArea cacheView = new TextArea();
        cacheView.setEditable(false);

        Button refreshBtn = new Button("Refresh");
        Button clearBtn = new Button("Clear");
        Button restoreBtn = new Button("Restore");

        refreshBtn.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            epaService.getCacheSnapshot().forEach((team, epa) ->
                    sb.append("Team ").append(team)
                      .append(" -> ").append(epa).append("\n")
            );
            cacheView.setText(sb.toString());
        });

        clearBtn.setOnAction(e -> epaService.clearCache());
        restoreBtn.setOnAction(e -> epaService.retrieveBackupCache());

        VBox tab3Layout = new VBox(10, new HBox(10, refreshBtn, clearBtn, restoreBtn), cacheView);
        tab3Layout.setPadding(new Insets(10));

        Tab tab3 = new Tab("Cache Console", tab3Layout);

        // =========================
        // TAB 4: TEAM CONSISTENCY
        // =========================
        TextField consistencyInput = new TextField();
        consistencyInput.setPromptText("Enter team number");

        Button consistencyBtn = new Button("Calculate Consistency");

        Label consistencyLabel = new Label("Enter a team number.");
        ProgressIndicator spinner = new ProgressIndicator();
        spinner.setVisible(false);
        spinner.setMaxSize(40, 40);

        consistencyBtn.setOnAction(e -> {
            try {
                int team = Integer.parseInt(consistencyInput.getText());

                consistencyLabel.setText("Calculating...");
                spinner.setVisible(true);

                Task<Double> task = new Task<>() {
                    @Override
                    protected Double call() throws Exception {
                        return TBAService.getConsistency(team);
                    }
                };

                task.setOnSucceeded(ev -> {
                    double score = task.getValue();
                    spinner.setVisible(false);

                    String rating;
                    String color;

                    if (score >= 90) {
                        rating = "Crazy Consistent";
                        color = "darkgreen";
                    } else if (score >= 85) {
                        rating = "Extremely Consistent";
                        color = "green";
                    } else if (score >= 80) {
                        rating = "Very Consistent";
                        color = "limegreen";
                    } else if (score >= 75) {
                        rating = "Quite Consistent";
                        color = "gold";
                    } else if (score >= 70) {
                        rating = "Moderately Consistent";
                        color = "orange";
                    } else if (score >= 65) {
                        rating = "Poorly Consistent";
                        color = "darkorange";
                    } else if (score >= 60) {
                        rating = "Terribly Consistent";
                        color = "red";
                    } else {
                        rating = "Unpredictable";
                        color = "darkred";
                    }

                    consistencyLabel.setText(String.format("%.1f%% - %s", score, rating));
                    consistencyLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold;");

                    consistencyLabel.setText(
                            "Team " + team +
                            "\nConsistency: " + String.format("%.1f%%", score) +
                            "\nRating: " + rating
                    );
                });

                task.setOnFailed(ev -> {
                spinner.setVisible(false);

                Throwable ex = task.getException();
                ex.printStackTrace();

                consistencyLabel.setText("Error:\n" + ex.getMessage());
                });

                new Thread(task).start();

            } catch (NumberFormatException ex) {
                consistencyLabel.setText("Invalid team number.");
            }
        });

        VBox tab4Layout = new VBox(12,
                consistencyInput,
                consistencyBtn,
                spinner,
                consistencyLabel
        );
        tab4Layout.setPadding(new Insets(15));

        Tab tab4 = new Tab("Team Consistency", tab4Layout);

        // =========================
        TabPane tabPane = new TabPane(tab1, tab2, tab3, tab4);

        Scene scene = new Scene(tabPane, 600, 500);
        stage.setTitle("FRC Strategy Tool");
        stage.setScene(scene);
        stage.show();
    }

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
        double epa = EPAService.getEPA(team);

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