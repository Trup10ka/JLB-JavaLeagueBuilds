package me.trup10ka.jlb.util;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.util.Duration;
import me.trup10ka.jlb.controllers.BuildScene;

import java.util.List;
import java.util.stream.Collectors;

public class ButtonBoxController
{

    private static BuildScene buildScene;

    private static boolean notActive;

    public static void initializeController(BuildScene buildScene)
    {
        ButtonBoxController.buildScene = buildScene;
        ButtonBoxController.notActive = false;
    }


    public static void transferBoxWithCommunityButtons()
    {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.7), buildScene.getCommunityButtonsWrapper());
        if (notActive)
        {
            translateTransition.setToX(0);
            translateTransition.setInterpolator(Interpolator.LINEAR);
            translateTransition.play();
            ButtonBoxController.notActive = false;
        }
        else
        {
            translateTransition.setInterpolator(Interpolator.LINEAR);
            translateTransition.setToX(300);
            translateTransition.play();
            ButtonBoxController.notActive = true;
        }

    }
}
