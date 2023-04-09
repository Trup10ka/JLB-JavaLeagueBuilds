package me.trup10ka.jlb.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import me.trup10ka.jlb.app.JLB;
import me.trup10ka.jlb.web.parser.HtmlChampionsPageParser;
import me.trup10ka.jlb.web.parser.lographs.HtmlChampionsLoGParser;
import me.trup10ka.jlb.web.parser.mobafire.HtmlChampionsMobafireParser;
import me.trup10ka.jlb.web.parser.ugg.HtmlChampionsUGGParser;

import java.util.concurrent.CompletableFuture;

public class MainScene {
    @FXML
    private ProgressIndicator indicator;
    @FXML
    private Pane applicationHeader;
    public void initialize() {
        applicationHeader.setOnMousePressed(event -> JLB.getInstance().setOffSets(event));
        applicationHeader.setOnMouseDragged(event -> JLB.getInstance().moveStage(event));
    }

    @FXML
    private void setChampionsParserUGG() {
        JLB.getInstance().switchToChampions();
        ChampionsScene.getInstance().setChampionsPageParser(new HtmlChampionsUGGParser());
    }
    @FXML
    private void setChampionsParserLeagueOfGraphs() {
        JLB.getInstance().switchToChampions();
        CompletableFuture.runAsync(() -> ChampionsScene.getInstance().setChampionsPageParser(new HtmlChampionsLoGParser()));

    }
    @FXML
    private void setChampionsParserMobafire() {
        JLB.getInstance().switchToChampions();
        CompletableFuture.runAsync(() -> ChampionsScene.getInstance().setChampionsPageParser(new HtmlChampionsMobafireParser()));
    }
    @FXML
    private void terminate() {
        JLB.getInstance().terminate();
    }
}
