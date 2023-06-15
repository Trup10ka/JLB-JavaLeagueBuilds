package me.trup10ka.jlb.app;

import animatefx.animation.FadeIn;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.trup10ka.jlb.web.Page;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Class representing the JLB application
 * @author Lukas "Trup10ka" Friedl
 * @since 1.0.0
 */
public class JavaLeagueBuilds extends Application
{
    private static JavaLeagueBuilds instance;

    private Stage stage;
    private double xOffSet;
    private double yOffSet;
    private final Scene mainScene;
    private final Scene championScene;

    private final Scene buildScene;
    private final Scene loadingScene;

    /**
     * A wrap-up for all the Scenes above to handle them easier ({@link JavaLeagueBuilds#start(Stage) here; line 91})
     */
    private final List<Scene> scenes;
    /**
     * Determines what page has been chosen by the user
     * <br>Only possible values are:
     * <ul>
     *     <li>U.GG</li>
     *     <li>LoG</li>
     *     <li>Mobafire</li>
     * </ul>
     */
    private static Page chosenPage;

    @SuppressWarnings("DataFlowIssue")
    public JavaLeagueBuilds()
    {
        instance = this;

        Scene mainScene = null;
        Scene championsScene = null;
        Scene buildScene = null;
        Scene loadingScene = null;
        try
        {
            mainScene = new Scene(FXMLLoader.load(getClass().getResource("/scenes/MainScene.fxml")));
            championsScene = new Scene(FXMLLoader.load(getClass().getResource("/scenes/ChampionsScene.fxml")));
            buildScene = new Scene(FXMLLoader.load(getClass().getResource("/scenes/BuildScene.fxml")));
            loadingScene = new Scene(FXMLLoader.load(getClass().getResource("/scenes/LoadingScene.fxml")));
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        this.mainScene = mainScene;
        this.championScene = championsScene;
        this.buildScene = buildScene;
        this.loadingScene = loadingScene;
        this.scenes = Arrays.asList(mainScene, championsScene, buildScene, loadingScene);
    }

    /**
     * Initializes the stage with main scene
     * @param stage created window
     */
    @Override
    public void start(Stage stage)
    {
        this.stage = stage;

        stage.setTitle("Java League Builds");
        stage.setScene(mainScene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/icon.png")));
        stage.setResizable(false);
        stage.initStyle(StageStyle.TRANSPARENT);

        @SuppressWarnings("unused")
        final Font font = Font.loadFont(getClass().getResourceAsStream("/fonts/LeagueOfLegendsFont.ttf"), 14);

        scenes.forEach(scene -> scene.setFill(Color.TRANSPARENT));

        stage.show();
        new FadeIn(mainScene.getRoot()).play();
    }

    public void switchToChampions()
    {
        stage.setScene(championScene);
    }

    public void switchToBuildScene()
    {
        stage.setScene(buildScene);
    }

    public void switchToLoading()
    {
        stage.setScene(loadingScene);
    }

    /**
     * When stage pressed at the top, sets stage's offsets relative to the screen
     * @param mouseEvent mouse pressed event
     */
    public void setOffSets(MouseEvent mouseEvent)
    {
        xOffSet = mouseEvent.getSceneX();
        yOffSet = mouseEvent.getSceneY();
    }

    /**
     * When stage grabbed at the top, sets stage's position using offsets relative to the screen
     * @param mouseEvent mouse grab event
     */
    public void moveStage(MouseEvent mouseEvent)
    {
        stage.setX(mouseEvent.getScreenX() - xOffSet);
        stage.setY(mouseEvent.getScreenY() - yOffSet);
    }

    public void terminate()
    {
        stage.close();
    }
    public void iconify()
    {
        stage.setIconified(true);
    }

    public static Page getChosenPage()
    {
        return chosenPage;
    }
    public static void setChosenPage(Page chosenPage)
    {
        JavaLeagueBuilds.chosenPage = chosenPage;
    }
    public static JavaLeagueBuilds getInstance()
    {
        return instance;
    }
}
