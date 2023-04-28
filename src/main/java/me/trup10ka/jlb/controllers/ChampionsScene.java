package me.trup10ka.jlb.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import me.trup10ka.jlb.app.JavaLeagueBuilds;
import me.trup10ka.jlb.data.Champion;
import me.trup10ka.jlb.util.RoundCorners;
import me.trup10ka.jlb.web.parser.HtmlBuildPageParser;
import me.trup10ka.jlb.web.parser.HtmlChampionsPageParser;
import me.trup10ka.jlb.web.parser.lographs.HtmlBuildLoGParser;
import me.trup10ka.jlb.web.parser.mobafire.HtmlBuildMobafireParser;
import me.trup10ka.jlb.web.parser.ugg.HtmlBuildUGGParser;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

/**
 * Controller for ChampionScene fxml
 * @since 1.0.0
 * @author Lukas "Trup10ka" Friedl
 */
public class ChampionsScene
{
    private static ChampionsScene instance;
    /**
     * Html page parser for champions, determined by players choice in {@link MainScene MainScene} (one of the setChampionsParser methods)
     */
    private HtmlChampionsPageParser championsPageParser;
    /**
     * Part of the scene responsible for ability to move the stage; more information -> {@link JavaLeagueBuilds}
     */
    @FXML
    private Pane applicationHeader;
    /**
     * Pane where every champion is displayed
     */
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

    /**
     * In the previous Scene ({@link MainScene}) player chooses between League pages from which he wants the build.
     * <br> <br>
     * After the choice is made, creates parser for the desired page, and after it's created calls <strong>this method</strong>
     * to set the parser for this object
     * @param championsPageParser parser created in {@link MainScene} after users choice
     * @see MainScene#setChampionsParserUGG() Example of how setting the parser works
     */
    public void setChampionsPageParser(HtmlChampionsPageParser championsPageParser)
    {
        this.championsPageParser = championsPageParser;
    }

    /**
     * Displays all champions into the {@link ChampionsScene#championsPane}
     */
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

    /**
     * All champions are displayed with ImageView which is then wrapped in StackPane <br>
     * <br>
     * Firstly created the StackPane in which the future ImageView is going to be stored. Also, creates Label with
     * {@link Champion champion} name. Finally, creates a FlowPane in which is wrapped StackPane with image and the label
     * and adds it to the root {@link ChampionsScene#championsPane}
     * @param champion champion to be displayed
     */
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

    /**
     * @param node array of nodes to be added to the FlowPane (mostly champion image and name)
     * @return FlowPane which represents champion "card" (image and name)
     */
    private FlowPane createChampionCard(Node... node)
    {
        FlowPane flowPane = new FlowPane();
        flowPane.getStyleClass().add("champion-card");
        for (Node child : node)
            flowPane.getChildren().add(child);
        return flowPane;
    }

    /**
     * @param champion {@link Champion} which is needed to be parsed for the possible future {@link BuildScene}
     * @return StackPane in which is ImageView wrapped
     */
    private StackPane createPaneForImageView(Champion champion)
    {
        StackPane imgViewPane = new StackPane();
        imgViewPane.getStyleClass().add("champion-image");
        imgViewPane.setOnMouseClicked(mouseEvent ->
        {
            JavaLeagueBuilds.getInstance().switchToLoading();
            CompletableFuture.runAsync(() -> 
            {
                HtmlBuildPageParser buildPage = switch (JavaLeagueBuilds.chosenPage)
                {
                    case U_GG -> new HtmlBuildUGGParser(champion.getName().toLowerCase());
                    case MOBAFIRE -> new HtmlBuildMobafireParser(champion.getName().toLowerCase());
                    case LEAGUE_OF_GRAPHS -> new HtmlBuildLoGParser(champion.getName().toLowerCase());
                };
                BuildScene.getInstance().setChampionToParse(champion, buildPage);
            }).thenRun(() -> Platform.runLater(() -> JavaLeagueBuilds.getInstance().switchToBuildScene()));
        });
        return imgViewPane;
    }

    /**
     * @param champion which champion is going to be put into the image view
     * @return styled image view with champion image
     */
    private ImageView createChampionImageView(Champion champion)
    {
        ImageView imageView = new ImageView("images/champions/" + champion.getName().toLowerCase().replaceAll("[. ]", "") + ".png");
        imageView.setFitHeight(60);
        imageView.setFitWidth(60);
        RoundCorners.setRoundedCornerImageView(imageView);
        return imageView;
    }

    /**
     * When parsing champions page from a website fails, prints out the error message into label
     * @param exception the reason for the failure
     */
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

    public static ChampionsScene getInstance()
    {
        return instance;
    }
}
