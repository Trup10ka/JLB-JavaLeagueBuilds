package me.trup10ka.jlb.util;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.util.Duration;
import me.trup10ka.jlb.controllers.BuildScene;
import me.trup10ka.jlb.controllers.builds.BuildSceneCommunity;

public class ButtonBoxController
{

    private static BuildScene buildScene;

    public static void initializeController(BuildScene buildScene)
    {
        ButtonBoxController.buildScene = buildScene;
    }


    public static void transferBoxWithCommunityButtons()
    {
        if (buildScene.getCommunityButtonsWrapper().getLayoutX() == 300)
        {
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1.5), buildScene.getCommunityButtonsWrapper());
            translateTransition.setToX(0);
            translateTransition.setInterpolator(Interpolator.LINEAR);
            translateTransition.play();
        }
        else
        {

            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(1.5), buildScene.getCommunityButtonsWrapper());
            translateTransition.setInterpolator(Interpolator.EASE_IN);
            translateTransition.setToX(300);
            translateTransition.play();
        }

    }
}
