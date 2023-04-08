package me.trup10ka.jlb.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import me.trup10ka.jlb.web.parser.lographs.HtmlChampionsLoGParser;
import me.trup10ka.jlb.web.parser.mobafire.HtmlChampionsMobafireParser;
import me.trup10ka.jlb.web.parser.ugg.HtmlChampionsUGGParser;

import java.util.concurrent.CompletableFuture;

public class MainScene {
    @FXML
    private HBox buttonBox;
    @FXML
    public ProgressIndicator indicator;

    @FXML
    private void setChampionsParserUGG() {
        indicator.setVisible(true);
        buttonBox.setVisible(false);
        CompletableFuture.runAsync(() -> ChampionsScene.getInstance().setChampionsPageParser(new HtmlChampionsUGGParser()))
                .thenAccept(voids -> {
                    indicator.setVisible(false);
                    buttonBox.setVisible(true);
                    System.out.println("nigger");
                });
    }
    @FXML
    private void setChampionsParserLeagueOfGraphs() {
        indicator.setVisible(true);
        buttonBox.setVisible(false);
        CompletableFuture.runAsync(() -> ChampionsScene.getInstance().setChampionsPageParser(new HtmlChampionsLoGParser()))
                .thenAccept(voids -> {
                    indicator.setVisible(false);
                    buttonBox.setVisible(true);
                    System.out.println("nigger");
                });
    }
    @FXML
    private void setChampionsParserMobafire() {
        indicator.setVisible(true);
        buttonBox.setVisible(false);
        CompletableFuture.runAsync(() -> ChampionsScene.getInstance().setChampionsPageParser(new HtmlChampionsMobafireParser()))
                .thenAccept(voids -> {
                    indicator.setVisible(false);
                    buttonBox.setVisible(true);
                    System.out.println("nigger");
                });
    }
}
