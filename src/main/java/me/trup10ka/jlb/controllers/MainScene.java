package me.trup10ka.jlb.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import me.trup10ka.jlb.app.JavaLeagueBuilds;
import me.trup10ka.jlb.web.Page;
import me.trup10ka.jlb.web.parser.lographs.HtmlChampionsLoGParser;
import me.trup10ka.jlb.web.parser.mobafire.HtmlChampionsMobafireParser;
import me.trup10ka.jlb.web.parser.ugg.HtmlChampionsUGGParser;

import java.util.concurrent.CompletableFuture;

public class MainScene
{

    @FXML
    private Pane applicationHeader;

    public void initialize()
    {
        applicationHeader.setOnMousePressed(event -> JavaLeagueBuilds.getInstance().setOffSets(event));
        applicationHeader.setOnMouseDragged(event -> JavaLeagueBuilds.getInstance().moveStage(event));
    }

    @FXML
    private void setChampionsParserUGG()
    {
        JavaLeagueBuilds.chosenPage = Page.U_GG;
        JavaLeagueBuilds.getInstance().switchToLoading();
        CompletableFuture.runAsync(() -> ChampionsScene.getInstance().setChampionsPageParser(new HtmlChampionsUGGParser()))
                .thenRun(() -> Platform.runLater(() -> {
                    ChampionsScene.getInstance().fillChampionsPane();
                    JavaLeagueBuilds.getInstance().switchToChampions();
                }));
    }

    @FXML
    private void setChampionsParserLeagueOfGraphs()
    {
        JavaLeagueBuilds.chosenPage = Page.LEAGUE_OF_GRAPHS;
        JavaLeagueBuilds.getInstance().switchToLoading();
        CompletableFuture.runAsync(() -> ChampionsScene.getInstance().setChampionsPageParser(new HtmlChampionsLoGParser()))
                .thenRun(() -> Platform.runLater(() -> {
                    ChampionsScene.getInstance().fillChampionsPane();
                    JavaLeagueBuilds.getInstance().switchToChampions();
                }));
    }

    @FXML
    private void setChampionsParserMobafire()
    {
        JavaLeagueBuilds.chosenPage = Page.MOBAFIRE;
        JavaLeagueBuilds.getInstance().switchToLoading();
        CompletableFuture.runAsync(() -> ChampionsScene.getInstance().setChampionsPageParser(new HtmlChampionsMobafireParser()))
                .thenRun(() -> Platform.runLater(() -> {
                    ChampionsScene.getInstance().fillChampionsPane();
                    JavaLeagueBuilds.getInstance().switchToChampions();
                }));
    }

    @FXML
    private void terminate()
    {
        JavaLeagueBuilds.getInstance().terminate();
    }

}
