package me.trup10ka.jlb.controllers.builds;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import me.trup10ka.jlb.app.JavaLeagueBuilds;

public class BuildSceneCommunity
{

    private static BuildSceneCommunity instance;

    @FXML
    private Pane applicationHeader;

    @FXML
    private Button goBack;

    @FXML
    private VBox summonersBox;

    @FXML
    private HBox mainRunesBox;

    @FXML
    private HBox secondaryRunesAndAttributesBox;

    @FXML
    private HBox items;


    public void initialize()
    {
        applicationHeader.setOnMousePressed(event -> JavaLeagueBuilds.getInstance().setOffSets(event));
        applicationHeader.setOnMouseDragged(event -> JavaLeagueBuilds.getInstance().moveStage(event));
        goBack.setOnMousePressed(event -> clearPageAndSwitchToChampions(true , summonersBox, mainRunesBox, secondaryRunesAndAttributesBox));
    }

    public BuildSceneCommunity()
    {
        instance = this;
    }


    public static BuildSceneCommunity getInstance()
    {
        return instance;
    }
    @FXML
    private void terminate()
    {
        JavaLeagueBuilds.getInstance().terminate();
    }

    private void clearPageAndSwitchToChampions(boolean shouldSwitch, Pane... boxes)
    {
        for (Pane box : boxes)
        {
            box.getChildren().clear();
        }
        if (shouldSwitch)
            JavaLeagueBuilds.getInstance().switchToChampions();
    }
    private void clearPageAndSwitchToChampions()
    {
        clearPageAndSwitchToChampions(false , items, summonersBox, mainRunesBox, secondaryRunesAndAttributesBox);
    }

    @FXML
    private void iconify()
    {
        JavaLeagueBuilds.getInstance().iconify();
    }
}
