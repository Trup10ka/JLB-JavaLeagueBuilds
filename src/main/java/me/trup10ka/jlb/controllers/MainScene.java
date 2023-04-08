package me.trup10ka.jlb.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import me.trup10ka.jlb.web.parser.lographs.HtmlChampionsLoGParser;
import me.trup10ka.jlb.web.parser.mobafire.HtmlChampionsMobafireParser;
import me.trup10ka.jlb.web.parser.ugg.HtmlChampionsUGGParser;

import java.util.concurrent.CompletableFuture;

public class MainScene {
    @FXML
    public HBox buttonBox;
    @FXML
    public void initialize() {
        buttonBox.getChildren().add(initializeUGGButton());
        buttonBox.getChildren().add(initializeLeagueOfGraphsButton());
        buttonBox.getChildren().add(initializeMobafireButton());
    }
    public void loadingPage() {

    }
    private JFXButton initializeUGGButton() {
        JFXButton button = new JFXButton("U.GG");
        button.setOnAction(actionEvent ->
                CompletableFuture.runAsync(() -> ChampionsScene.getInstance().setChampionsPageParser(new HtmlChampionsUGGParser()))
                        .thenAccept(voids -> System.out.println("U.GG Loaded")));
        return button;
    }
    private JFXButton initializeLeagueOfGraphsButton() {
        JFXButton button = new JFXButton("League of Graphs");
        button.setOnAction(actionEvent ->
                CompletableFuture.runAsync(() -> ChampionsScene.getInstance().setChampionsPageParser(new HtmlChampionsLoGParser()))
                        .thenAccept(voids -> System.out.println("League of Graphs Loaded")));
        return button;
    }
    private JFXButton initializeMobafireButton() {
        JFXButton button = new JFXButton("Mobafire");
        button.setOnAction(actionEvent ->
                CompletableFuture.runAsync(() -> ChampionsScene.getInstance().setChampionsPageParser(new HtmlChampionsMobafireParser()))
                        .thenAccept(voids -> System.out.println("Mobafire Loaded")));
        return button;
    }
}
