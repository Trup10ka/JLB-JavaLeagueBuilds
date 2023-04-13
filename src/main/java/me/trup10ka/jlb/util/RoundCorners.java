package me.trup10ka.jlb.util;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;



public class RoundCorners {
    public static void setRoundedCornerImageView(ImageView imageView) {
        Rectangle clip = new Rectangle(
                imageView.getFitWidth(), imageView.getFitHeight()
        );
        clip.setArcWidth(15); clip.setArcHeight(15);
        imageView.setClip(clip);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = imageView.snapshot(parameters, null);

        imageView.setClip(null);
        imageView.setImage(image);
    }
    public static void setRoundedCornerToItem(Pane pane) {
        Rectangle rectangle = new Rectangle(48, 48);
        rectangle.setArcWidth(15); rectangle.setArcHeight(15);
        pane.setClip(rectangle);
    }
}