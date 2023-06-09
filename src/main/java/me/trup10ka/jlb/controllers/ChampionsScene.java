package me.trup10ka.jlb.controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import me.trup10ka.jlb.app.JavaLeagueBuilds;
import me.trup10ka.jlb.controllers.builds.*;
import me.trup10ka.jlb.data.lolgame.Champion;
import me.trup10ka.jlb.util.*;
import me.trup10ka.jlb.web.Page;
import me.trup10ka.jlb.web.parser.*;
import me.trup10ka.jlb.web.parser.lographs.*;
import me.trup10ka.jlb.web.parser.mobafire.*;
import me.trup10ka.jlb.web.parser.ugg.*;



import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;

/**
 * Controller for ChampionScene fxml
 * @since 1.0.0
 * @author Lukas "Trup10ka" Friedl
 */
public class ChampionsScene
{
    private static ChampionsScene instance;
    /**
     * Html page parser for champions, determined by players choice in {@link MainScene MainScene} (one of the setChampionsParser methods)
     */
    private HtmlChampionsPageParser championsPageParser;

    private final Map<String, FlowPane> allChampionCardsByTheirNames;

    private ImageView recentActivePage;

    @FXML
    private JFXTextField searchBarForChampions;
    /**
     * Part of the scene responsible for ability to move the stage; more information -> {@link JavaLeagueBuilds}
     */
    @FXML
    private Pane applicationHeader;
    /**
     * Pane where every champion is displayed
     */
    @FXML
    private FlowPane championsPane;
    @FXML
    private Label errorLabel;
    @FXML
    private ImageView uggPageActive;
    @FXML
    private ImageView mobafirePageActive;
    @FXML
    private ImageView logPageActive;
    @FXML
    private ProgressIndicator progressIndicator;

    public ChampionsScene()
    {
        instance = this;
        allChampionCardsByTheirNames = new TreeMap<>();
    }

    public void initialize()
    {
        applicationHeader.setOnMousePressed(event -> JavaLeagueBuilds.getInstance().setOffSets(event));
        applicationHeader.setOnMouseDragged(event -> JavaLeagueBuilds.getInstance().moveStage(event));
    }

    /**
     * In the previous Scene ({@link MainScene}) player chooses between League pages from which he wants the build.
     * <br> <br>
     * After the choice is made, creates parser for the desired page, and after it's created calls <strong>this method</strong>
     * to set the parser for this object
     * @param championsPageParser parser created in {@link MainScene} after users choice
     * @see MainScene#setChampionsParserUGG() Example of how setting the parser works
     */
    public void setChampionsPageParser(HtmlChampionsPageParser championsPageParser)
    {
        this.championsPageParser = championsPageParser;
        allChampionCardsByTheirNames.clear();
    }

    /**
     * Displays all champions into the {@link ChampionsScene#championsPane}
     */
    public void fillChampionsPane()
    {
        setActivePageImageView();
        if (championsPageParser.champions() == null)
            return;
        ArrayList<Champion> champions = championsPageParser.champions();
        for (Champion champion : champions)
            addToTilePane(champion);
    }

    /**
     * All champions are displayed with ImageView which is then wrapped in StackPane <br>
     * <br>
     * Firstly created the StackPane in which the future ImageView is going to be stored. Also, creates Label with
     * {@link Champion champion} name. Finally, creates a FlowPane in which is wrapped StackPane with image and the label
     * and adds it to the root {@link ChampionsScene#championsPane}
     * @param champion champion to be displayed
     */
    private void addToTilePane(Champion champion)
    {
        StackPane imgViewPane = createPaneForImageView(champion);

        ImageView imageView = createChampionImageView(champion);

        imgViewPane.getChildren().add(imageView);

        Label nameLabel = new Label(champion.getName());
        nameLabel.getStyleClass().add("champion-name");

        FlowPane flowPane = createChampionCard(imgViewPane, nameLabel);

        this.allChampionCardsByTheirNames.put(champion.getName(), flowPane);
        this.championsPane.getChildren().add(flowPane);
    }

    /**
     * @param node array of nodes to be added to the FlowPane (mostly champion image and name)
     * @return FlowPane which represents champion "card" (image and name)
     */
    private FlowPane createChampionCard(Node... node)
    {
        FlowPane flowPane = new FlowPane();
        flowPane.getStyleClass().add("champion-card");
        for (Node child : node)
            flowPane.getChildren().add(child);
        return flowPane;
    }

    /**
     * @param champion {@link Champion} which is needed to be parsed for the possible future {@link BuildSceneStatic}
     * @return StackPane in which is ImageView wrapped
     */
    private StackPane createPaneForImageView(Champion champion)
    {
        StackPane imgViewPane = new StackPane();
        imgViewPane.getStyleClass().add("champion-image");

        if (!JavaLeagueBuilds.getChosenPage().IS_COMMUNITY_BUILD)
            imgViewPane.setOnMouseClicked(mouseEvent -> createAndShowStaticBuildParser(champion));
        else
            imgViewPane.setOnMouseClicked(mouseEvent -> createAndShowCommunityBuildParser(champion));
        return imgViewPane;
    }

    /**
     * @param champion which champion is going to be put into the image view
     * @return styled image view with champion image
     */
    private ImageView createChampionImageView(Champion champion)
    {
        String championName = FormattedString.CHAMPION_IMAGE_NAME_FORMAT.toFormat(champion.getName());
        URL url = getClass().getResource("/images/champions/" + championName + ".png");
        ImageView imageView = new ImageView(url.toExternalForm());
        imageView.setFitHeight(60); imageView.setFitWidth(60);
        RoundCorners.setRoundedCornerImageView(imageView);
        return imageView;
    }

    /**
     * When parsing champions page from a website fails, prints out the error message into label
     * @param exception the reason for the failure
     */
    public void executeErrorLabel(Exception exception)
    {
        exception.printStackTrace();
        Platform.runLater(() -> errorLabel.setText(exception.getMessage()));
    }

    /**
     * When searching for a champion, deletes all the champion cards, and then shows cards which are valid for the search value
     */
    @FXML
    private void refreshChampionPageWithSearchFilter()
    {
        championsPane.getChildren().clear();
        String searchingName = searchBarForChampions.getText();
        for (String key : allChampionCardsByTheirNames.keySet())
            if (key.toLowerCase().contains(searchingName.toLowerCase()))
                championsPane.getChildren().add(allChampionCardsByTheirNames.get(key));
    }

    /**
     * Creates a shows build parsed from statistics Pages
     * @param champion champion which build is being parsed
     * @see HtmlBuildPageParser
     */
    private void createAndShowStaticBuildParser(Champion champion)
    {
        JavaLeagueBuilds.getInstance().switchToLoading();
        CompletableFuture.runAsync(() ->
        {
            HtmlBuildPageParser buildPage = getBuildPageParser(JavaLeagueBuilds.getChosenPage(), champion);
            BuildSceneStatic.getInstance().setChampionToParse(champion, buildPage);
        }).thenRun(() -> Platform.runLater(() -> JavaLeagueBuilds.getInstance().switchToBuildSceneStatic()));
    }

    /**
     * Creates a shows build parsed from community Pages
     * @param champion champion which build is being parsed
     * @see HtmlBuildPageParser
     */
    private void createAndShowCommunityBuildParser(Champion champion)
    {
        JavaLeagueBuilds.getInstance().switchToLoading();
        CompletableFuture.runAsync(() ->
        {
            HtmlAllBuildsPageParser allBuildsPageParser = getAllBuildPageParser(JavaLeagueBuilds.getChosenPage(), champion);
            HtmlBuildPageParser buildPageParser = getBuildPageParser(JavaLeagueBuilds.getChosenPage(), allBuildsPageParser);

            BuildSceneCommunity.getInstance().setBuildToParse(allBuildsPageParser.allCommunityBuilds(), buildPageParser, champion);
        }).thenRun(() -> Platform.runLater(() -> JavaLeagueBuilds.getInstance().switchToBuildSceneCommunity()));
    }

    /**
     * If pages builds are community build, this method creates a parser for all the possible builds
     * @param page the page which is parsed from
     * @param champion champion where builds are looked up
     * @return the parser for the builds
     * @see HtmlAllBuildsPageParser
     */
    private HtmlAllBuildsPageParser getAllBuildPageParser(Page page, Champion champion)
    {
        return switch (page)
        {
            case MOBAFIRE -> new HtmlAllBuildsMobafireParser(champion.getWebBuildPagePath());
            default -> throw new IllegalArgumentException("ChampionsScene.java exception: Chosen page cannot be " + JavaLeagueBuilds.getChosenPage().name()
                    + " because " + JavaLeagueBuilds.getChosenPage().name() +" \"IS_COMMUNITY_BUILD\" is false");
        };
    }


    /**
     * @param page page which is parsed from
     * @param champion champion which is being parsed
     * @return A parser for builds created statistically
     */
    private HtmlBuildPageParser getBuildPageParser(Page page, Champion champion)
    {
        return getBuildPageParser(page, champion, null);
    }

    /**
     *
     * @param page page which is parsed from
     * @param allBuildsPageParser parser which parsed all the possible community builds
     * @return A parser for builds created by community
     */
    private HtmlBuildPageParser getBuildPageParser(Page page, HtmlAllBuildsPageParser allBuildsPageParser)
    {
        return getBuildPageParser(page, null, allBuildsPageParser);
    }

    /**
     * <p>
     *     Decides whether is page a community build or not, then creates a build parser for desired page.
     * </p>
     * <p>
     *     If page is community built, creates a parser for build that is <bold>FIRST</bold> in the list of community
     *     builds, which {@link HtmlAllBuildsPageParser HtmlParser} provided.
     * </p>
     * @param page page which is being parsed from
     * @param champion champion which we are parsing - is null in case of community built page
     * @param allBuildsPageParser all builds parser - is null in case of statistically built page
     * @return Parser for champion build
     * @see HtmlBuildPageParser
     */
    private HtmlBuildPageParser getBuildPageParser(Page page, Champion champion, HtmlAllBuildsPageParser allBuildsPageParser)
    {
        if (page.IS_COMMUNITY_BUILD)
            return switch (JavaLeagueBuilds.getChosenPage())
            {
                case MOBAFIRE -> new HtmlBuildMobafireParser(allBuildsPageParser.allCommunityBuilds().get(0).buildURL());
                default -> throw new IllegalArgumentException("ChampionsScene.java exception: Chosen page cannot be " + JavaLeagueBuilds.getChosenPage().name()
                        + " because " + JavaLeagueBuilds.getChosenPage().name() +" \"IS_COMMUNITY_BUILD\" is false");
            };
        else
            return switch (JavaLeagueBuilds.getChosenPage())
            {
                case U_GG -> new HtmlBuildUGGParser(champion.getWebBuildPagePath());
                case LEAGUE_OF_GRAPHS -> new HtmlBuildLoGParser(champion.getWebBuildPagePath());
                default -> throw new IllegalArgumentException("ChampionsScene.java exception: Chosen page cannot be " + JavaLeagueBuilds.getChosenPage().name()
                        + " because " + JavaLeagueBuilds.getChosenPage().name() +" \"IS_COMMUNITY_BUILD\" is true");
            };
    }

    /**
     * Refreshes the scroll pane filled with champion cards, with cards from {@link Page#U_GG U.GG}
     */
    @FXML
    private void refreshForUGG()
    {
        JavaLeagueBuilds.setChosenPage(Page.U_GG);
        switchToMinorLoading(true);
        setRecentActivePage(Page.U_GG);
        CompletableFuture.runAsync(() -> setChampionsPageParser(new HtmlChampionsUGGParser()))
                .thenRun(() -> Platform.runLater(() -> {
                    switchToMinorLoading(false);
                    fillChampionsPane();
                }));
    }
    /**
     * Refreshes the scroll pane filled with champion cards, with cards from {@link Page#MOBAFIRE Mobafire}
     */
    @FXML
    private void refreshForMobafire()
    {
        JavaLeagueBuilds.setChosenPage(Page.MOBAFIRE);
        switchToMinorLoading(true);
        setRecentActivePage(Page.MOBAFIRE);
        CompletableFuture.runAsync(() -> setChampionsPageParser(new HtmlChampionsMobafireParser()))
                .thenRun(() -> Platform.runLater(() -> {
                    switchToMinorLoading(false);
                    fillChampionsPane();
                }));
    }
    /**
     * Refreshes the scroll pane filled with champion cards, with cards from {@link Page#LEAGUE_OF_GRAPHS League of Graphs}
     */
    @FXML
    private void refreshForLoG()
    {
        JavaLeagueBuilds.setChosenPage(Page.LEAGUE_OF_GRAPHS);
        switchToMinorLoading(true);
        setRecentActivePage(Page.LEAGUE_OF_GRAPHS);
        CompletableFuture.runAsync(() -> setChampionsPageParser(new HtmlChampionsLoGParser()))
                .thenRun(() -> Platform.runLater(() -> {
                    switchToMinorLoading(false);
                    fillChampionsPane();
                }));
    }

    /**
     * Switches between loading and scroll pane with champion cards.
     * @param onOrOff determines whether loading should be turned on or off
     */
    private void switchToMinorLoading(boolean onOrOff)
    {
        if (onOrOff)
        {
            championsPane.getChildren().clear();
            championsPane.setVisible(false);
            progressIndicator.setVisible(true);
        }
        else
        {
            championsPane.setVisible(true);
            progressIndicator.setVisible(false);
        }
    }

    /**
     * Sets image in the button to full opacity for currently chosen page
     * @param page page which was currently chosen and now is {@link ChampionsScene#recentActivePage}
     */
    private void setRecentActivePage(Page page)
    {
        switch (page)
        {
            case U_GG -> {
                recentActivePage.setOpacity(0.3);
                recentActivePage = uggPageActive;
            }
            case MOBAFIRE -> {
                recentActivePage.setOpacity(0.3);
                recentActivePage = mobafirePageActive;
            }
            case LEAGUE_OF_GRAPHS -> {
                recentActivePage.setOpacity(0.3);
                recentActivePage = logPageActive;
            }
        }
    }
    /**
     * Sets image in the button to full opacity for currently chosen page
     */
    private void setActivePageImageView()
    {
        switch (JavaLeagueBuilds.getChosenPage())
        {
            case U_GG ->
            {
                uggPageActive.setOpacity(1);
                recentActivePage = uggPageActive;
            }
            case MOBAFIRE -> {
                mobafirePageActive.setOpacity(1);
                recentActivePage = mobafirePageActive;
            }
            case LEAGUE_OF_GRAPHS -> {
                logPageActive.setOpacity(1);
                recentActivePage = logPageActive;
            }
        }
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

    public static ChampionsScene getInstance()
    {
        return instance;
    }
}
