package me.trup10ka.jlb.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import me.trup10ka.jlb.app.JLB;
import me.trup10ka.jlb.data.Champion;
import me.trup10ka.jlb.web.parser.HtmlBuildPageParser;
import me.trup10ka.jlb.web.parser.ugg.HtmlBuildUGGParser;

public class BuildScene {

    private static BuildScene instance;

    private HtmlBuildPageParser buildParser;

    @FXML
    private Pane applicationHeader;
    public void initialize() {
        applicationHeader.setOnMousePressed(event -> JLB.getInstance().setOffSets(event));
        applicationHeader.setOnMouseDragged(event -> JLB.getInstance().moveStage(event));
    }

    public BuildScene() {
        instance = this;
    }

    public static BuildScene getInstance() {
        return instance;
    }

    public void setChampionToParse(Champion champion) {
        buildParser = new HtmlBuildUGGParser(champion.getName().toLowerCase().replaceAll("[. ]", ""));
        Champion championBuild = new Champion(champion.getName(),
                buildParser.itemBuild(),
                buildParser.runePage(),
                buildParser.summoners());
        System.out.println(championBuild);
    }
    @FXML
    private void terminate() {
        JLB.getInstance().terminate();
    }
}
