package me.trup10ka.jlb.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;
import me.trup10ka.jlb.app.JavaLeagueBuilds;
import me.trup10ka.jlb.data.lolgame.*;
import me.trup10ka.jlb.util.FormattedString;
import me.trup10ka.jlb.util.Descriptions;
import me.trup10ka.jlb.util.RoundCorners;
import me.trup10ka.jlb.web.Page;
import me.trup10ka.jlb.web.parser.HtmlBuildPageParser;
import me.trup10ka.jlb.web.parser.lographs.HtmlBuildLoGParser;
import me.trup10ka.jlb.web.parser.ugg.HtmlBuildUGGParser;

import java.util.*;
import java.util.concurrent.CompletableFuture;


/**
 * Controller for BuilderScene fxml
 * @since 1.0.0
 * @author Lukas "Trup10ka" Friedl
 */
public class BuildScene
{

    private static BuildScene instance;
    /**
     * {@link Champion} which has user chosen, and from which data will be queried
     */
    private Champion recentChampion;
    /**
     * After first build shown, stores the used page to this variable, and whether is this and also {@link #recentChampion}
     * variables equal to the future page and future champion choice,
     * just uses this variable instead of connecting to page again
     */
    private Page recentlyUsedPage;
    /**
     * Part of the scene responsible for ability to move the stage; more information -> {@link JavaLeagueBuilds}
     */
    @FXML
    private Pane applicationHeader;
    /**
     * Button which takes you back to {@link ChampionsScene}
     */
    @FXML
    private Button goBack;
    @FXML
    private Label championName;
    @FXML
    private HBox startingItems;
    @FXML
    private HBox coreItems;
    @FXML
    private HBox otherItems;
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
        goBack.setOnMousePressed(event -> clearPageAndSwitchToChampions(true ,
                startingItems, coreItems, otherItems, summonersBox, mainRunesBox, secondaryRunesAndAttributesBox));
    }

    public BuildScene()
    {
        instance = this;
    }

    /**
     * Sets the adequate build website parser.
     * Which website is in use is determined by the {@link JavaLeagueBuilds#chosenPage chosenPage} property
     *
     * @param champion is {@link Champion champion} whose build page will be loaded
     * @param pageParser page parser which is going to be used to parse the data
     */
    public void setChampionToParse(Champion champion, HtmlBuildPageParser pageParser)
    {
        if (!(recentChampion != null
                && recentChampion.getName().equals(champion.getName())
                && recentlyUsedPage.equals(JavaLeagueBuilds.getChosenPage())))
        {

            recentlyUsedPage = JavaLeagueBuilds.getChosenPage();
            recentChampion = new Champion(
                    champion.getName(),
                    pageParser.queryItemBuild(),
                    pageParser.queryRunePage(),
                    pageParser.summoners());
        }
        Platform.runLater(this::build);
    }

    /**
     * Builds the entire Build page, by calling <strong>arranging</strong> methods
     * <ul>
     *     <li>{@link BuildScene#arrangeItems(ItemBuild) Arrange Items}</li>
     *     <li>{@link BuildScene#arrangeRunes() Arrange Runes}</li>
     *     <li>{@link BuildScene#arrangeSummonerSpells() Arrange Summoner Spells}</li>
     * </ul>
     */
    @FXML
    private void build()
    {
        championName.setText(getNameOfChampionWithHisPredictedClass(recentChampion));
        arrangeSummonerSpells();
        arrangeItems(recentChampion.getItemBuild());
        arrangeRunes();
    }

    /**
     * Arranges {@link BuildScene#arrangeStartingItems(ItemBuild) starting items}, {@link BuildScene#arrangeCoreItems(ItemBuild) core items}
     * and {@link BuildScene#arrangeOtherItems(ItemBuild) other items}
     * @param itemBuild chosen champion's {@link ItemBuild}
     */
    private void arrangeItems(ItemBuild itemBuild)
    {
        arrangeStartingItems(itemBuild);
        arrangeCoreItems(itemBuild);
        arrangeOtherItems(itemBuild);
    }

    /**
     * Arranges all champion's runes, including: <br> {@link BuildScene#mainRunes() Keystone and main runes} and
     * {@link BuildScene#secondaryRunesAndAttributes() secondary runes and atributes}
     */
    private void arrangeRunes()
    {
        mainRunes();
        secondaryRunesAndAttributes();
    }

    /**
     * Adds champion's summoner spells into the {@link JavaLeagueBuilds#buildScene BuildScene}
     */
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
    /**
     * Creates and adds all {@link ItemBuild#startItems starting items} of the champion into the Scene
     * @param itemBuild {@link ItemBuild} of the chosen champion
     */
    private void arrangeStartingItems(ItemBuild itemBuild)
    {
        HBox allImages = createAllItemsInCategory(itemBuild.getStartItems());
        allImages.getStyleClass().add("filled-box");
        startingItems.getChildren().add(allImages);
    }

    /**
     * Creates and adds all {@link ItemBuild#coreItems core items} of the champion into the Scene
     * @param itemBuild {@link ItemBuild} of the chosen champion
     */
    private void arrangeCoreItems(ItemBuild itemBuild)
    {
        HBox allImages = createAllItemsInCategory(itemBuild.getCoreItems());
        checkForBoots(allImages, itemBuild);
        allImages.getStyleClass().add("filled-box");
        allImages.setId("core-item-box");
        coreItems.getChildren().add(allImages);
    }

    /**
     * Arranges all {@link ItemBuild#endItems other (end) items} of the champion
     * @param itemBuild {@link ItemBuild} of the chosen champion
     */
    private void arrangeOtherItems(ItemBuild itemBuild)
    {
        HBox allImagesAndOptions = createAllItemsInCategoryAndOptions(itemBuild.getEndItems());
        allImagesAndOptions.getStyleClass().add("filled-box");
        otherItems.getChildren().add(allImagesAndOptions);
    }

    /**
     * A method for <strong>set of items</strong> which are need displayed <br>
     * Takes the {@link Item Items} wrapped in pane which is then added to the final collection (HBox) of items
     * @param items set of items to be displayed
     * @return HBox (collection) of images of the items
     */
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

    /**
     * A method for <strong>map of items</strong> which are need displayed <br>
     * @param otherItems map of items to be displayed
     * @return HBox (collection) of images of items
     */
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

    /**
     * Creates a styled pane which has a specified background image
     * <br> The "coordinates" approach is needed because of how the images are displayed on U.GG website
     * @param item item which is to be displayed in pane
     * @return styled pane with background image
     */
    private Pane createItemImagePane(Item item)
    {
        Pane pane = new Pane();
        createTooltipForItem(item, pane);
        pane.setStyle("-fx-background-image: url(\"images/items/" + item.imageName() + "\");" +
                "-fx-background-position: " + item.x() + " " + item.y() + ";" +
                "-fx-min-height: 48;" + "-fx-min-width: 48;" + "-fx-max-height: 48;" + "-fx-max-width: 48;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0, 0, 0, 0.5), 40, 0.1, 5, 5);");
        RoundCorners.setRoundedCornerToImagePane(pane);
        return pane;
    }

    /**
     * Arranges Keystone and main runes
     */
    private void mainRunes()
    {
        this.mainRunesBox.getChildren().add(returnKeyStoneRune());
        this.mainRunesBox.getChildren().addAll(returnSideMainRunes());
    }

    /**
     * Arranges secondary runes and attributes
     */
    private void secondaryRunesAndAttributes()
    {
        this.secondaryRunesAndAttributesBox.getChildren().addAll(returnSecondaryRunes());
        this.secondaryRunesAndAttributesBox.getChildren().addAll(returnAttributes());
    }

    /**
     * @return Image of {@link RunePage#keyStoneRune Keystone} rune
     */
    private Pane returnKeyStoneRune()
    {
        Rune keystone = recentChampion.getRunePage().getKeyStoneRune();
        String rune = FormattedString.URI_IMAGE_FORMAT.toFormat(keystone.name());

        ImageView image = new ImageView("images/runes/mainrunes/" + rune + ".png");
        image.setFitHeight(70);  image.setFitWidth(70);

        Pane container = new Pane(image);
        container.setMaxSize(70, 70);

        createTooltipForRune(keystone, container);
        return container;
    }

    /**
     * @return a list of image views with {@link RunePage#sideMainRunes main} runes
     */
    private List<Pane> returnSideMainRunes()
    {
        List<Pane> runes = new ArrayList<>(3);
        for (Rune rune : recentChampion.getRunePage().getSideMainRunes())
        {
            String path = FormattedString.URI_IMAGE_FORMAT.toFormat(rune.name());
            ImageView image = new ImageView("images/runes/secondaryrunes/" + path + ".png");
            image.setFitHeight(40); image.setFitWidth(40);
            Pane container = new Pane(image); container.setMaxSize(40, 40);
            createTooltipForRune(rune, container);
            runes.add(container);
        }
        return runes;
    }

    /**
     * @return a list of image views with {@link RunePage#secondaryRunes secondary} runes
     */
    private List<Pane> returnSecondaryRunes()
    {
        List<Pane> runes = new ArrayList<>(2);
        for (Rune rune : recentChampion.getRunePage().getSecondaryRunes())
        {
            String path = FormattedString.URI_IMAGE_FORMAT.toFormat(rune.name());
            ImageView image = new ImageView("images/runes/secondaryrunes/" + path + ".png");
            image.setFitHeight(40); image.setFitWidth(40);
            Pane container = new Pane(image); container.setMaxSize(40, 40);
            createTooltipForRune(rune, container);
            runes.add(container);
        }
        return runes;
    }

    /**
     * @return a list of image views with attributes
     */
    private List<Pane> returnAttributes()
    {
        List<Pane> runes = new ArrayList<>(3);
        for (Attribute attribute : recentChampion.getRunePage().getAttributes())
        {
            String path = FormattedString.ATTRIBUTE_NAME_URI_FORMAT.toFormat(attribute.propertyName());
            ImageView image = new ImageView("images/runes/attributes/" + path + ".png");
            image.setFitHeight(27); image.setFitWidth(27);
            Pane container = new Pane(image); container.setMaxSize(27, 27);
            createTooltipForAttribute(attribute, container);
            runes.add(container);
        }
        return runes;
    }

    /**
     * Takes in champion as parameter and gets his keystone rune from which it decides its possible class
     * @param champion champions build to be shown
     * @return name of the champion with his predicted class in one string
     */
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

    private void checkForBoots(HBox hBox, ItemBuild itemBuild)
    {
        if (itemBuild.getBoots() == null)
            return;
        double width = hBox.getMinWidth();
        hBox.getChildren().add(createItemImagePane(itemBuild.getBoots()));
        hBox.setMinWidth(width + 60);
    }

    /**
     * Clears the BuildScene page
     * @param boxes all nodes present and visible in BuildScene
     */
    private void clearPageAndSwitchToChampions(boolean shouldSwitch, Pane... boxes)
    {
        for (Pane box : boxes)
        {
            box.getChildren().clear();
        }
        if (shouldSwitch)
            JavaLeagueBuilds.getInstance().switchToChampions();
    }
    private void clearPageAndSwitchToChampions()
    {
        clearPageAndSwitchToChampions(false ,startingItems, coreItems, otherItems, summonersBox, mainRunesBox, secondaryRunesAndAttributesBox);
    }

    private void createTooltipForItem(Item item, Node node)
    {
        String description = FormattedString.ITEM_NAME_FORMAT.toFormat(item.name()) + "\n\n" + Descriptions.getDescriptionOfItem(item.name());
        assignTooltipForNode(node, description);
    }
    private void createTooltipForRune(Rune rune, Node node)
    {
        String description = rune.name() + "\n\n" + Descriptions.getDescriptionOfRune(rune.name());
        assignTooltipForNode(node, description);
    }

    private void createTooltipForAttribute(Attribute attribute, Node node)
    {
        String description = attribute.propertyName();
        assignTooltipForNode(node, description);
    }
    private void assignTooltipForNode(Node node, String description)
    {
        Tooltip tooltip = new Tooltip(description);
        tooltip.setShowDelay(new Duration(0));
        tooltip.setMaxWidth(300);
        tooltip.setWrapText(true);
        tooltip.getStyleClass().add("tool-tip-item");
        Tooltip.install(node, tooltip);
    }
    @FXML
    private void switchToUGG()
    {
        if (recentlyUsedPage == Page.U_GG)
            return;
        JavaLeagueBuilds.setChosenPage(Page.U_GG);
        CompletableFuture.runAsync(() -> {
            HtmlBuildPageParser parser = new HtmlBuildUGGParser(FormattedString.U_GG_HYPERLINK_FORMAT.toFormat(recentChampion.getName()));
            setChampionToParse(recentChampion, parser);
        });
        clearPageAndSwitchToChampions();
    }
    @FXML
    private void switchToLoG()
    {
        if (recentlyUsedPage == Page.LEAGUE_OF_GRAPHS)
            return;
        JavaLeagueBuilds.setChosenPage(Page.LEAGUE_OF_GRAPHS);
        CompletableFuture.runAsync(() -> {
            HtmlBuildPageParser parser = new HtmlBuildLoGParser(FormattedString.LOG_HYPERLINK_FORMAT.toFormat(recentChampion.getName()));
            setChampionToParse(recentChampion, parser);
        });
        clearPageAndSwitchToChampions();
    }
    public static BuildScene getInstance()
    {
        return instance;
    }
    @FXML
    private void terminate()
    {
        JavaLeagueBuilds.getInstance().terminate();
    }

    @FXML
    private void iconify()
    {
        JavaLeagueBuilds.getInstance().iconify();
    }
}
