package me.trup10ka.jlb.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import me.trup10ka.jlb.app.JavaLeagueBuilds;
import me.trup10ka.jlb.data.*;
import me.trup10ka.jlb.util.RoundCorners;
import me.trup10ka.jlb.web.parser.HtmlBuildPageParser;
import me.trup10ka.jlb.web.parser.lographs.HtmlBuildLoGParser;
import me.trup10ka.jlb.web.parser.mobafire.HtmlBuildMobafireParser;
import me.trup10ka.jlb.web.parser.ugg.HtmlBuildUGGParser;

import javax.swing.*;
import java.util.Map;
import java.util.Set;


/**
 * @author Lukas Friedl
 */
public class BuildScene {

    private static BuildScene instance;

    private Champion recentChampion;
    private String recentlyUsedPage;
    @FXML
    private Pane applicationHeader;
    @FXML
    private HBox itemsBox;
    @FXML
    private VBox summonersBox;
    @FXML
    private Label championName;
    @FXML
    private ImageView goBack;
    public void initialize() {
        applicationHeader.setOnMousePressed(event -> JavaLeagueBuilds.getInstance().setOffSets(event));
        applicationHeader.setOnMouseDragged(event -> JavaLeagueBuilds.getInstance().moveStage(event));
        goBack.setOnMousePressed(event -> clearPageAndSwitchToChampions());
    }
    public BuildScene() {
        instance = this;
    }
    /**
     * Sets the adequate build website parser.
     * Which website is in use is determined by the {@link JavaLeagueBuilds#chosenPage chosenPage} property
     * @param champion is champion whose build page will be loaded
     */
    public void setChampionToParse(Champion champion) {
        if (!(recentChampion != null
                && recentChampion.getName().equals(champion.getName())
                && recentlyUsedPage.equals(JavaLeagueBuilds.chosenPage))) {

            HtmlBuildPageParser buildParser = returnParserOfCurrentPageInUse(champion.getName());
            recentChampion = new Champion(
                    champion.getName(),
                    buildParser.itemBuild(),
                    buildParser.runePage(),
                    buildParser.summoners());
        }
        championName.setText(recentChampion.getName());
        build();
    }
    @FXML
    private void build() {
        ItemBuild itemBuild = recentChampion.getItemBuild();
        arrangeStartingItems(itemBuild);
        arrangeCoreItems(itemBuild);
        arrangeOtherItems(itemBuild);
    }
    private void arrangeStartingItems(ItemBuild itemBuild) {
        HBox allImages = createAllItemsInCategory(itemBuild.getStartItems());
        itemsBox.getChildren().add(allImages);
    }
    private void arrangeCoreItems(ItemBuild itemBuild) {
        HBox allImages = createAllItemsInCategory(itemBuild.getCoreItems());
        itemsBox.getChildren().add(allImages);
    }
    private void arrangeOtherItems(ItemBuild itemBuild) {
        HBox allImagesAndOptions = createAllItemsInCategoryAndOptions(itemBuild.getEndItems());
        itemsBox.getChildren().add(allImagesAndOptions);
    }
    private HBox createAllItemsInCategory(Set<Item> items) {
        HBox allImages = new HBox(); allImages.setSpacing(10); allImages.setAlignment(Pos.CENTER);
        for (Item item : items) {
            Pane itemBox = createItemImagePane(item);
            allImages.getChildren().add(itemBox);
        }
        return allImages;
    }
    private HBox createAllItemsInCategoryAndOptions(Map<String, Set<Item>> otherItems) {
        HBox allImagesAndOptions = new HBox(); allImagesAndOptions.setSpacing(60);
        for (Set<Item> set : otherItems.values()) {
            VBox allImages = new VBox(); allImages.setSpacing(10); allImages.setAlignment(Pos.CENTER);
            for (Item item : set) {
                Pane itemBox = createItemImagePane(item);
                allImages.getChildren().add(itemBox);
            }
            allImagesAndOptions.getChildren().add(allImages);
        }
        return allImagesAndOptions;
    }
    private Pane createItemImagePane(Item item) {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-image: url(\"items/" + item.imageName() + "\");" +
                        "-fx-background-position: " + item.x() + " " + item.y() + ";" +
                        "-fx-min-height: 48;" + "-fx-min-width: 48;" + "-fx-max-height: 48;" + "-fx-max-width: 48;");
        RoundCorners.setRoundedCornerToItem(pane);
        return pane;
    }
    private HtmlBuildPageParser returnParserOfCurrentPageInUse(String championName) {
        return switch (JavaLeagueBuilds.chosenPage) {
            case "U.GG" -> new HtmlBuildUGGParser(championName.toLowerCase().replaceAll("[. ]", ""));
            case "LoG" -> new HtmlBuildLoGParser(championName.toLowerCase().replaceAll("[. ]", ""));
            case "Mobafire" -> new HtmlBuildMobafireParser(championName.toLowerCase().replaceAll("[. ]", ""));
            default -> throw new IllegalArgumentException("Unknown page");
        };
    }
    @FXML
    private void terminate() {
        JavaLeagueBuilds.getInstance().terminate();
    }
    private void clearPageAndSwitchToChampions() {
        JavaLeagueBuilds.getInstance().switchToChampions();
        itemsBox.getChildren().clear();
        summonersBox.getChildren().clear();

    }
    public static BuildScene getInstance() {
        return instance;
    }
}
