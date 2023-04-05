package me.trup10ka.qtjambitest;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;


public class MyWeatherApp extends Application {
    private static MyWeatherApp instance;
    private final Scene mainScene;

    public MyWeatherApp() {
        instance = this;

        Scene mainScene = null;

        try {
            mainScene = new Scene(FXMLLoader.load(getClass().getResource("src/main/resources/scenes/MainScene.fxml")));
            System.out.println(mainScene);
        }
        catch (IOException e) {e.printStackTrace();}

        this.mainScene = mainScene;
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("WeatherApp");
        stage.getIcons().add(new Image("cloudy.png"));
        stage.setScene(mainScene);
        stage.show();
    }
    public MyWeatherApp getInstance() {
        return instance;
    }
}