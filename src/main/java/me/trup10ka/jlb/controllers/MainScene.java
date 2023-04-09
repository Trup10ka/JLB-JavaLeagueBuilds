package me.trup10ka.jlb.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import me.trup10ka.jlb.app.JLB;
import me.trup10ka.jlb.web.parser.HtmlChampionsPageParser;
import me.trup10ka.jlb.web.parser.lographs.HtmlChampionsLoGParser;
import me.trup10ka.jlb.web.parser.mobafire.HtmlChampionsMobafireParser;
import me.trup10ka.jlb.web.parser.ugg.HtmlChampionsUGGParser;

import java.util.concurrent.CompletableFuture;

public class MainScene {
    @FXML
    public ProgressIndicator indicator;

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
}
