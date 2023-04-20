package me.trup10ka.jlb.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import me.trup10ka.jlb.app.JavaLeagueBuilds;
import me.trup10ka.jlb.data.Champion;
import me.trup10ka.jlb.util.RoundCorners;
import me.trup10ka.jlb.web.parser.HtmlChampionsPageParser;

import java.util.ArrayList;

public class ChampionsScene
{
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

    public ChampionsScene()
    {
        instance = this;
    }

    public void initialize()
    {
        applicationHeader.setOnMousePressed(event -> JavaLeagueBuilds.getInstance().setOffSets(event));
        applicationHeader.setOnMouseDragged(event -> JavaLeagueBuilds.getInstance().moveStage(event));
    }

    public void setChampionsPageParser(HtmlChampionsPageParser championsPageParser)
    {
        this.championsPageParser = championsPageParser;
        this.progressIndicator.setVisible(false);
    }

    public void fillChampionsPane()
    {
        if (championsPageParser.champions() == null)
            return;
        ArrayList<Champion> champions = championsPageParser.champions();
        for (Champion champion : champions)
        {
            addToTilePane(champion);
        }
    }

    private void addToTilePane(Champion champion)
    {
        StackPane imgViewPane = createPaneForImageView(champion);

        ImageView imageView = createChampionImageView(champion);

        imgViewPane.getChildren().add(imageView);

        Label nameLabel = new Label(champion.getName());
        nameLabel.getStyleClass().add("champion-name");

        FlowPane flowPane = createChampionCard(imgViewPane, nameLabel);

        this.championsPane.getChildren().add(flowPane);
    }

    private FlowPane createChampionCard(Node... node)
    {
        FlowPane flowPane = new FlowPane();
        flowPane.getStyleClass().add("champion-card");
        for (Node child : node)
            flowPane.getChildren().add(child);
        return flowPane;
    }
    private StackPane createPaneForImageView(Champion champion)
    {
        StackPane imgViewPane = new StackPane();
        imgViewPane.getStyleClass().add("champion-image");
        imgViewPane.setOnMouseClicked(mouseEvent ->
        {
            JavaLeagueBuilds.getInstance().switchToBuildScene();
            BuildScene.getInstance().setChampionToParse(champion);
        });
        return imgViewPane;
    }
    private ImageView createChampionImageView(Champion champion)
    {
        ImageView imageView = new ImageView("images/champions/" + champion.getName().toLowerCase().replaceAll("[. ]", "") + ".png");
        imageView.setFitHeight(60);
        imageView.setFitWidth(60);
        RoundCorners.setRoundedCornerImageView(imageView);
        return imageView;
    }

    public static ChampionsScene getInstance()
    {
        return instance;
    }

    public void executeErrorLabel(Exception exception)
    {
        exception.printStackTrace();
        Platform.runLater(() -> errorLabel.setText(exception.getMessage()));
    }

    @FXML
    private void terminate()
    {
        JavaLeagueBuilds.getInstance().terminate();
    }
}
