package me.trup10ka.jlb.util;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import me.trup10ka.jlb.controllers.builds.BuildSceneStatic;
import me.trup10ka.jlb.data.lolgame.Champion;

import java.util.List;

/**
 * Utility class for making round corners for nodes which are not round-able with css.
 * @author Lukas "Trup10ka" Friedl
 * @see me.trup10ka.jlb.controllers.BuildScene#arrangeSummoners(List) Creating summoner spells
 * @see me.trup10ka.jlb.controllers.ChampionsScene#createChampionImageView(Champion) Creating champion images
 */
public class RoundCorners
{
    public static void setRoundedCornerImageView(ImageView imageView)
    {
        Rectangle clip = new Rectangle(
                imageView.getFitWidth(), imageView.getFitHeight()
        );
        clip.setArcWidth(12);
        clip.setArcHeight(12);
        imageView.setClip(clip);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = imageView.snapshot(parameters, null);

        imageView.setClip(null);
        imageView.setImage(image);
    }

    public static void setRoundedCornerToImagePane(Pane pane)
    {
        Rectangle rectangle = new Rectangle(48, 48);
        rectangle.setArcWidth(12);
        rectangle.setArcHeight(12);
        pane.setClip(rectangle);
    }
}
