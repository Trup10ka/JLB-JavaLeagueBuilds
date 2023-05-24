package me.trup10ka.jlb.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import me.trup10ka.jlb.app.JavaLeagueBuilds;
import static me.trup10ka.jlb.web.Page.*;
import me.trup10ka.jlb.web.Page;
import me.trup10ka.jlb.web.parser.HtmlChampionsPageParser;
import me.trup10ka.jlb.web.parser.lographs.HtmlChampionsLoGParser;
import me.trup10ka.jlb.web.parser.mobafire.HtmlChampionsMobafireParser;
import me.trup10ka.jlb.web.parser.ugg.HtmlChampionsUGGParser;

import java.util.concurrent.CompletableFuture;

/**
 * Controller for scene where player choose from which page he wants to parse the champions and their builds
 * @since 1.0.0
 * @author Lukas "Trup10ka" Friedl
 */
public class MainScene
{
    /**
     * Part of the scene responsible for ability to move the stage; more information -> {@link JavaLeagueBuilds}
     */
    @FXML
    private Pane applicationHeader;

    public void initialize()
    {
        applicationHeader.setOnMousePressed(event -> JavaLeagueBuilds.getInstance().setOffSets(event));
        applicationHeader.setOnMouseDragged(event -> JavaLeagueBuilds.getInstance().moveStage(event));
    }

    /**
     * One of the three methods which are assigned to buttons - {@link Page#U_GG U.GG}. <br> <br>
     * Sets the {@link JavaLeagueBuilds#chosenPage chosenPage} and the {@link me.trup10ka.jlb.web.parser.HtmlChampionsPageParser HtmlChampions} parser
     * for the {@link ChampionsScene}. Then switches to {@link LoadingScene} and after the creation is finished switches to {@link ChampionsScene}
     * @see ChampionsScene#setChampionsPageParser(HtmlChampionsPageParser) Setting the page parser
     */
    @FXML
    private void setChampionsParserUGG()
    {
        JavaLeagueBuilds.setChosenPage(U_GG);
        JavaLeagueBuilds.getInstance().switchToLoading();
        CompletableFuture.runAsync(() -> ChampionsScene.getInstance().setChampionsPageParser(new HtmlChampionsUGGParser()))
                .thenRun(() -> Platform.runLater(() -> {
                    ChampionsScene.getInstance().fillChampionsPane();
                    JavaLeagueBuilds.getInstance().switchToChampions();
                }));
    }

    /**
     * One of the three methods which are assigned to buttons - {@link Page#LEAGUE_OF_GRAPHS LoG}.
     * @see MainScene#setChampionsParserUGG() How it works
     */
    @FXML
    private void setChampionsParserLeagueOfGraphs()
    {
        JavaLeagueBuilds.setChosenPage(LEAGUE_OF_GRAPHS);
        JavaLeagueBuilds.getInstance().switchToLoading();
        CompletableFuture.runAsync(() -> ChampionsScene.getInstance().setChampionsPageParser(new HtmlChampionsLoGParser()))
                .thenRun(() -> Platform.runLater(() -> {
                    ChampionsScene.getInstance().fillChampionsPane();
                    JavaLeagueBuilds.getInstance().switchToChampions();
                }));
    }
    /**
     * One of the three methods which are assigned to buttons - {@link Page#MOBAFIRE Mobafire}.
     * @see MainScene#setChampionsParserUGG() How it works
     */
    @FXML
    private void setChampionsParserMobafire()
    {
        JavaLeagueBuilds.setChosenPage(MOBAFIRE);
        JavaLeagueBuilds.getInstance().switchToChooseBuildScene();
    }

    @FXML
    private void terminate()
    {
        JavaLeagueBuilds.getInstance().terminate();
    }

    @FXML
    private void iconify()
    {
        JavaLeagueBuilds.getInstance().iconify();
    }

}
