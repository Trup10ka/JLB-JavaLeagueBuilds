package me.trup10ka.jlb.app;

import animatefx.animation.FadeIn;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


public class JLB extends Application {
    private static JLB instance;

    private Stage stage;

    private final Scene mainScene;
    private final Scene champions;

    public JLB() {
        instance = this;

        Scene mainScene = null;
        Scene champions = null;

        try {
            mainScene = new Scene(FXMLLoader.load(getClass().getResource("/scenes/MainScene.fxml")));
            champions = new Scene(FXMLLoader.load(getClass().getResource("/scenes/ChampionsScene.fxml")));
        }
        catch (IOException e) {e.printStackTrace();}

        this.mainScene = mainScene;
        this.champions = champions;
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        stage.setTitle("JLB");
        stage.setScene(mainScene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/icon.png")));
        stage.show();
        new FadeIn(mainScene.getRoot()).play();
    }

    public void switchToChampions() {
        stage.setScene(champions);
    }
    public JLB getInstance() {
        return instance;
    }

}
