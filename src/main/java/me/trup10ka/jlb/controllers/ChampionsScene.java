package me.trup10ka.jlb.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import me.trup10ka.jlb.app.JLB;
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

    public ChampionsScene() {
        instance = this;
    }
    public void initialize() {
        applicationHeader.setOnMousePressed(event -> JLB.getInstance().setOffSets(event));
        applicationHeader.setOnMouseDragged(event -> JLB.getInstance().moveStage(event));
    }

    public void setChampionsPageParser(HtmlChampionsPageParser championsPageParser) {
        this.championsPageParser = championsPageParser;
        this.progressIndicator.setVisible(false);
        fillChampionsPane();
    }
    public void fillChampionsPane() {
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
            JLB.getInstance().switchToBuildScene();
            BuildScene.getInstance().setChampionToParse(champion);
        });

        this.championsPane.getChildren().add(flowPane);
    }
    public static ChampionsScene getInstance() {
        return instance;
    }

    public void executeErrorLabel(Exception exception) {
        System.out.println(exception.getMessage());
    }
    @FXML
    private void terminate() {
        JLB.getInstance().terminate();
    }
}
