package me.trup10ka.jlb.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import me.trup10ka.jlb.app.JavaLeagueBuilds;
import me.trup10ka.jlb.controllers.builds.BuildSceneCommunity;
import me.trup10ka.jlb.data.lolgame.Champion;
import me.trup10ka.jlb.data.lolgame.CommunityBuild;
import me.trup10ka.jlb.util.ButtonBoxController;
import me.trup10ka.jlb.web.parser.HtmlAllBuildsPageParser;
import me.trup10ka.jlb.web.parser.HtmlBuildPageParser;

import java.util.List;

public class BuildScene
{
    private static BuildScene instance;
    /**
     * All possible {@link CommunityBuild community builds} parsed for latest patch
     */
    private List<CommunityBuild> allCommunityBuilds;

    private Champion curentChampion;

    private CommunityBuild currentCommunitybuild;

    private ButtonBoxController buttonBoxController;

    @FXML
    private Pane applicationHeader;

    @FXML
    private Button goBack;

    @FXML
    private Label buildName;

    @FXML
    private VBox summonersBox;

    @FXML
    private HBox mainRunesBox;

    @FXML
    private HBox secondaryRunesAndAttributesBox;

    @FXML
    private HBox items;

    @FXML
    private VBox communityBuildsVBox;

    public void initialize()
    {
        this.buttonBoxController = new ButtonBoxController(this);
        applicationHeader.setOnMousePressed(event -> JavaLeagueBuilds.getInstance().setOffSets(event));
        applicationHeader.setOnMouseDragged(event -> JavaLeagueBuilds.getInstance().moveStage(event));
        goBack.setOnMousePressed(event -> clearPageAndSwitchToChampions(true , summonersBox, mainRunesBox, secondaryRunesAndAttributesBox, items, communityBuildsVBox));
    }

    public void setChampionToParse(Champion champion, HtmlAllBuildsPageParser allBuilds, HtmlBuildPageParser buildParser)
    {

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
    private void clearPage()
    {
        clearPageAndSwitchToChampions(false , items, summonersBox, mainRunesBox, secondaryRunesAndAttributesBox);
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
    public static BuildScene getInstance()
    {
        return instance;
    }
}
