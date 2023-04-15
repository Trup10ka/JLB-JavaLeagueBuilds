package me.trup10ka.jlb.app;

import animatefx.animation.FadeIn;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.trup10ka.jlb.web.Page;

import java.io.IOException;

/**
 * @author Lukas Friedl
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
     * Determines what page has been chosen by the user
     * <br>Only possible values are:
     * <ul>
     *     <li>U.GG</li>
     *     <li>LoG</li>
     *     <li>Mobafire</li>
     * </ul>
     */
    public static Page chosenPage;


    public JavaLeagueBuilds()
    {
        instance = this;

        Scene mainScene = null;
        Scene champions = null;
        Scene buildScene = null;
        Scene loadingScene = null;
        try
        {
            mainScene = new Scene(FXMLLoader.load(getClass().getResource("/scenes/MainScene.fxml")));
            champions = new Scene(FXMLLoader.load(getClass().getResource("/scenes/ChampionsScene.fxml")));
            buildScene = new Scene(FXMLLoader.load(getClass().getResource("/scenes/BuildScene.fxml")));
            loadingScene = new Scene(FXMLLoader.load(getClass().getResource("/scenes/LoadingScene.fxml")));
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        this.mainScene = mainScene;
        this.championScene = champions;
        this.buildScene = buildScene;
        this.loadingScene = loadingScene;
    }

    @Override
    public void start(Stage stage)
    {
        this.stage = stage;

        stage.setScene(mainScene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/icon.png")));
        stage.setResizable(false);
        stage.initStyle(StageStyle.TRANSPARENT);
        mainScene.setFill(Color.TRANSPARENT);
        championScene.setFill(Color.TRANSPARENT);
        buildScene.setFill(Color.TRANSPARENT);

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

    public void setOffSets(MouseEvent mouseEvent)
    {
        xOffSet = mouseEvent.getSceneX();
        yOffSet = mouseEvent.getSceneY();
    }

    public void moveStage(MouseEvent mouseEvent)
    {
        stage.setX(mouseEvent.getScreenX() - xOffSet);
        stage.setY(mouseEvent.getScreenY() - yOffSet);
    }

    public void terminate()
    {
        stage.close();
    }

    public static JavaLeagueBuilds getInstance()
    {
        return instance;
    }
}
