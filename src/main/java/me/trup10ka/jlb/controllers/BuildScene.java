package me.trup10ka.jlb.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import me.trup10ka.jlb.app.JavaLeagueBuilds;
import me.trup10ka.jlb.data.*;
import me.trup10ka.jlb.util.RoundCorners;
import me.trup10ka.jlb.web.Page;
import me.trup10ka.jlb.web.parser.HtmlBuildPageParser;
import me.trup10ka.jlb.web.parser.lographs.HtmlBuildLoGParser;
import me.trup10ka.jlb.web.parser.mobafire.HtmlBuildMobafireParser;
import me.trup10ka.jlb.web.parser.ugg.HtmlBuildUGGParser;

import java.util.*;


/**
 * @author Lukas Friedl
 */
public class BuildScene
{

    private static BuildScene instance;

    private Champion recentChampion;
    private Page recentlyUsedPage;
    @FXML
    private Pane applicationHeader;
    @FXML
    private Button goBack;
    @FXML
    private Label championName;
    @FXML
    private HBox itemsBox;
    @FXML
    private VBox summonersBox;
    @FXML
    private HBox mainRunesBox;
    @FXML
    private HBox secondaryRunesAndAttributesBox;

    public void initialize()
    {
        applicationHeader.setOnMousePressed(event -> JavaLeagueBuilds.getInstance().setOffSets(event));
        applicationHeader.setOnMouseDragged(event -> JavaLeagueBuilds.getInstance().moveStage(event));
        goBack.setOnMousePressed(event -> clearPageAndSwitchToChampions(itemsBox, summonersBox, mainRunesBox, secondaryRunesAndAttributesBox));
    }

    public BuildScene()
    {
        instance = this;
    }

    /**
     * Sets the adequate build website parser.
     * Which website is in use is determined by the {@link JavaLeagueBuilds#chosenPage chosenPage} property
     *
     * @param champion is champion whose build page will be loaded
     */
    public void setChampionToParse(Champion champion)
    {
        if (!(recentChampion != null
                && recentChampion.getName().equals(champion.getName())
                && recentlyUsedPage.equals(JavaLeagueBuilds.chosenPage)))
        {

            HtmlBuildPageParser buildParser = returnParserOfCurrentPageInUse(champion.getName());
            recentChampion = new Champion(
                    champion.getName(),
                    buildParser.itemBuild(),
                    buildParser.runePage(),
                    buildParser.summoners());
        }
        championName.setText(getNameOfChampionWithHisPredictedClass(recentChampion));
        build();
    }

    @FXML
    private void build()
    {
        arrangeSummonerSpells();
        arrangeItems(recentChampion.getItemBuild());
        arrangeRunes();
    }

    private void arrangeItems(ItemBuild itemBuild)
    {
        arrangeStartingItems(itemBuild);
        arrangeCoreItems(itemBuild);
        arrangeOtherItems(itemBuild);
    }

    private void arrangeRunes()
    {
        mainRunes();
        secondaryRunesAndAttributes();
    }

    private void arrangeSummonerSpells()
    {
        VBox summonersBox = new VBox();
        summonersBox.setSpacing(10);
        summonersBox.setAlignment(Pos.CENTER);
        for (SummonerSpell spell : recentChampion.getSummonerSpell())
        {
            ImageView image = new ImageView("images/summoners/" + spell.name().toLowerCase() + ".png");
            image.setFitHeight(48);
            image.setFitWidth(48);
            RoundCorners.setRoundedCornerImageView(image);
            summonersBox.getChildren().add(image);
        }
        this.summonersBox.getChildren().add(summonersBox);
    }

    private void arrangeStartingItems(ItemBuild itemBuild)
    {
        HBox allImages = createAllItemsInCategory(itemBuild.getStartItems());
        allImages.getStyleClass().add("filled-box");
        itemsBox.getChildren().add(allImages);
    }

    private void arrangeCoreItems(ItemBuild itemBuild)
    {
        HBox allImages = createAllItemsInCategory(itemBuild.getCoreItems());
        allImages.getStyleClass().add("filled-box");
        allImages.setId("core-item-box");
        itemsBox.getChildren().add(allImages);
    }

    private void arrangeOtherItems(ItemBuild itemBuild)
    {
        HBox allImagesAndOptions = createAllItemsInCategoryAndOptions(itemBuild.getEndItems());
        allImagesAndOptions.getStyleClass().add("filled-box");
        itemsBox.getChildren().add(allImagesAndOptions);
    }

    private HBox createAllItemsInCategory(Set<Item> items)
    {
        HBox allImages = new HBox();
        double width = 0;
        allImages.setSpacing(10); allImages.setAlignment(Pos.CENTER);
        for (Item item : items)
        {
            Pane itemBox = createItemImagePane(item);
            allImages.getChildren().add(itemBox);
            width += 50;
        }
        allImages.setMinWidth(width + 60);
        return allImages;
    }

    private HBox createAllItemsInCategoryAndOptions(Map<String, Set<Item>> otherItems)
    {
        HBox allImagesAndOptions = new HBox();
        double width = 0;
        allImagesAndOptions.setSpacing(50); allImagesAndOptions.setAlignment(Pos.CENTER);
        for (Set<Item> set : otherItems.values())
        {
            VBox allImages = new VBox();
            allImages.setSpacing(5);
            allImages.setAlignment(Pos.CENTER);
            for (Item item : set)
            {
                Pane itemBox = createItemImagePane(item);
                allImages.getChildren().add(itemBox);
            }
            allImagesAndOptions.getChildren().add(allImages);
            width += 100;
        }
        allImagesAndOptions.setMinWidth(width + 50); allImagesAndOptions.setMinHeight(180);
        return allImagesAndOptions;
    }

    private Pane createItemImagePane(Item item)
    {
        Pane pane = new Pane();
        pane.setStyle("-fx-background-image: url(\"images/items/" + item.imageName() + "\");" +
                "-fx-background-position: " + item.x() + " " + item.y() + ";" +
                "-fx-min-height: 48;" + "-fx-min-width: 48;" + "-fx-max-height: 48;" + "-fx-max-width: 48;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.5), 40, 0.1, 5, 5);");
        RoundCorners.setRoundedCornerToImagePane(pane);
        return pane;
    }

    private void mainRunes()
    {
        this.mainRunesBox.getChildren().add(keyStoneRune());
        this.mainRunesBox.getChildren().addAll(sideMainRunes());
    }

    private void secondaryRunesAndAttributes()
    {
        this.secondaryRunesAndAttributesBox.getChildren().addAll(secondaryRunes());
        this.secondaryRunesAndAttributesBox.getChildren().addAll(attributes());
    }

    private ImageView keyStoneRune()
    {
        ImageView image = new ImageView("images/runes/mainrunes/" +
                recentChampion.getRunePage().getKeyStoneRune().name().toLowerCase().replaceAll("[-: ]", "_") + ".png");
        image.setFitHeight(70);
        image.setFitWidth(70);
        return image;
    }

    private List<ImageView> sideMainRunes()
    {
        List<ImageView> runes = new ArrayList<>(3);
        for (Rune rune : recentChampion.getRunePage().getSecondaryMainRunes())
        {
            ImageView image = new ImageView("images/runes/secondaryrunes/"
                    + rune.name().toLowerCase().replaceAll("[: ]+", "_").replaceAll("'", "") + ".png");
            image.setFitHeight(40);
            image.setFitWidth(40);
            runes.add(image);
        }
        return runes;
    }

    private List<ImageView> secondaryRunes()
    {
        List<ImageView> runes = new ArrayList<>(2);
        for (Rune rune : recentChampion.getRunePage().getSecondaryRunes())
        {
            ImageView image = new ImageView("images/runes/secondaryrunes/"
                    + rune.name().toLowerCase().replaceAll("[: ]+", "_").replaceAll("'", "") + ".png");
            image.setFitHeight(40);
            image.setFitWidth(40);
            secondaryRunesAndAttributesBox.getChildren().add(image);
        }
        return runes;
    }

    private List<ImageView> attributes()
    {
        List<ImageView> runes = new ArrayList<>(3);
        for (Attribute attribute : recentChampion.getRunePage().getAttributes())
        {
            ImageView image = new ImageView("images/runes/attributes/"
                    + attribute.propertyName().toLowerCase().replaceAll("[: ]+", "_") + ".png");
            image.setFitHeight(27); image.setFitWidth(27);
            runes.add(image);
        }
        return runes;
    }

    private String getNameOfChampionWithHisPredictedClass(Champion champion)
    {
        StringBuilder name = new StringBuilder(champion.getName());
        String keyStoneRune = champion.getRunePage().getKeyStoneRune().name();
        switch (keyStoneRune)
        {
            case "Conqueror", "Grasp of the Undying" -> name.append("\nBruiser/Tank/Mage");
            case "Press the Attack", "Lethal Tempo", "Hail of Blades", "Fleet Footwork" -> name.append("\n- Marksman -");
            case "Electrocute", "Predator", "Dark Harvest", "First Strike" -> name.append("\n- Mage/Assassin -");
            case "Summon Aery", "Arcane Comet", "Phase Rush", "Aftershock", "Guardian", "Glacial Augment" -> name.append("\n").append("- Mage/Support -");
        }
        return name.toString();
    }
    private HtmlBuildPageParser returnParserOfCurrentPageInUse(String championName)
    {
        recentlyUsedPage = JavaLeagueBuilds.chosenPage;
        return switch (JavaLeagueBuilds.chosenPage)
        {
            case U_GG -> new HtmlBuildUGGParser(championName.toLowerCase().replaceAll("[. ]", ""));
            case LEAGUE_OF_GRAPHS -> new HtmlBuildLoGParser(championName.toLowerCase().replaceAll("[. ]", ""));
            case MOBAFIRE -> new HtmlBuildMobafireParser(championName.toLowerCase().replaceAll("[. ]", ""));
        };
    }

    @FXML
    private void terminate()
    {
        JavaLeagueBuilds.getInstance().terminate();
    }

    private void clearPageAndSwitchToChampions(Pane... boxes)
    {
        for (Pane box : boxes)
        {
            box.getChildren().clear();
        }
        JavaLeagueBuilds.getInstance().switchToChampions();
    }

    public static BuildScene getInstance()
    {
        return instance;
    }
}
