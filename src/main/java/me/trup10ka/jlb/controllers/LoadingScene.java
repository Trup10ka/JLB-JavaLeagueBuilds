package me.trup10ka.jlb.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import me.trup10ka.jlb.app.JavaLeagueBuilds;

/**
 * Controller for scene that serves as a loading screen
 * @since 1.0.0
 * @author Lukas "Trup10ka" Friedl
 */
public class LoadingScene
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
