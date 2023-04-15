package me.trup10ka.jlb.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import me.trup10ka.jlb.app.JavaLeagueBuilds;

public class LoadingScene
{
    @FXML
    private Pane applicationHeader;

    public void initialize()
    {
        applicationHeader.setOnMousePressed(event -> JavaLeagueBuilds.getInstance().setOffSets(event));
        applicationHeader.setOnMouseDragged(event -> JavaLeagueBuilds.getInstance().moveStage(event));
    }
    @FXML
    private void terminate()
    {
        JavaLeagueBuilds.getInstance().terminate();
    }
}
