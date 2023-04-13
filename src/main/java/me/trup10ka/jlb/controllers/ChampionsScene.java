package me.trup10ka.jlb.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import me.trup10ka.jlb.app.JavaLeagueBuilds;
import me.trup10ka.jlb.data.Champion;
import me.trup10ka.jlb.util.RoundCorners;
import me.trup10ka.jlb.web.parser.HtmlChampionsPageParser;

import java.util.ArrayList;

public class ChampionsScene {
    private static ChampionsScene instance;
    private HtmlChampionsPageParser championsPageParser;

    @FXML
    private Pane applicationHeader;

    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private FlowPane championsPane;
    @FXML
    private Label errorLabel;

    public ChampionsScene() {
        instance = this;
    }
    public void initialize() {
        applicationHeader.setOnMousePressed(event -> JavaLeagueBuilds.getInstance().setOffSets(event));
        applicationHeader.setOnMouseDragged(event -> JavaLeagueBuilds.getInstance().moveStage(event));
    }

    public void setChampionsPageParser(HtmlChampionsPageParser championsPageParser) {
        this.championsPageParser = championsPageParser;
        this.progressIndicator.setVisible(false);
    }
    public void fillChampionsPane() {
        if (championsPageParser.champions() == null)
            return;
        ArrayList<Champion> champions = championsPageParser.champions();
        for (Champion champion : champions) {
            addToTilePane(champion);
        }
    }
    private void addToTilePane(Champion champion) {
        StackPane imgViewPane = new StackPane();
        imgViewPane.getStyleClass().add("champion-image");

        ImageView imageView = new ImageView("champions/" + champion.getName().toLowerCase().replaceAll("[. ]", "") + ".png");
        imageView.setFitHeight(60); imageView.setFitWidth(60);
        RoundCorners.setRoundedCornerImageView(imageView);

        imgViewPane.getChildren().add(imageView);
        Label nameLabel = new Label(champion.getName());
        nameLabel.getStyleClass().add("champion-name");

        FlowPane flowPane = new FlowPane();
        flowPane.getStyleClass().add("champion-card");

        flowPane.getChildren().add(imgViewPane);
        flowPane.getChildren().add(nameLabel);
        flowPane.setOnMouseClicked(mouseEvent -> {
            JavaLeagueBuilds.getInstance().switchToBuildScene();
            BuildScene.getInstance().setChampionToParse(champion);
        });

        this.championsPane.getChildren().add(flowPane);
    }
    public static ChampionsScene getInstance() {
        return instance;
    }
    public void executeErrorLabel(Exception exception) {
        exception.printStackTrace();
        Platform.runLater(() -> errorLabel.setText(exception.getMessage()));
    }
    @FXML
    private void terminate() {
        JavaLeagueBuilds.getInstance().terminate();
    }
}
