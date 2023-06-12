package me.trup10ka.jlb.controllers;

import com.sun.javafx.geom.Line2D;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import me.trup10ka.jlb.app.JavaLeagueBuilds;
import me.trup10ka.jlb.data.lolgame.*;
import me.trup10ka.jlb.util.ButtonBoxController;
import me.trup10ka.jlb.util.Descriptions;
import me.trup10ka.jlb.util.FormattedString;
import me.trup10ka.jlb.util.RoundCorners;
import me.trup10ka.jlb.web.parser.HtmlAllBuildsPageParser;
import me.trup10ka.jlb.web.parser.HtmlBuildPageParser;
import static javafx.scene.paint.Color.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BuildScene
{
    private static BuildScene instance;
    /**
     * All possible {@link CommunityBuild community builds} parsed for latest patch
     */
    private List<CommunityBuild> allCommunityBuilds;

    private Champion curentChampion;

    private CommunityBuild currentCommunitybuild;

    private ButtonBoxController buttonBoxController;

    @FXML
    private Pane applicationHeader;

    @FXML
    private Button goBack;

    @FXML
    private Label nameOfBuild;

    @FXML
    private VBox summonersBox;

    @FXML
    private HBox mainRunesBox;

    @FXML
    private HBox secondaryRunesAndAttributesBox;

    @FXML
    private HBox items;

    public BuildScene()
    {
        instance = this;
    }

    public void initialize()
    {
        this.buttonBoxController = new ButtonBoxController(this);
        applicationHeader.setOnMousePressed(event -> JavaLeagueBuilds.getInstance().setOffSets(event));
        applicationHeader.setOnMouseDragged(event -> JavaLeagueBuilds.getInstance().moveStage(event));
        goBack.setOnMousePressed(event ->
                clearPageAndSwitchToChampions(true , summonersBox, mainRunesBox, secondaryRunesAndAttributesBox, items));
    }

    public void setChampionToParse(Champion champion, HtmlAllBuildsPageParser allBuilds, HtmlBuildPageParser buildParser)
    {
        this.curentChampion = new Champion(champion.getName(),
                buildParser.queryItemBuild(),
                buildParser.queryRunePage(),
                buildParser.summoners());
        if (allBuilds == null)
            Platform.runLater(this::setLayoutForStatisticallyCreatedBuild);
        else {
            this.allCommunityBuilds = allBuilds.allCommunityBuilds();
            this.currentCommunitybuild = allBuilds.allCommunityBuilds().get(0);
            Platform.runLater(this::setLayoutForCommunityCreatedBuild);
        }
    }

    private void setLayoutForStatisticallyCreatedBuild()
    {
        arrangeItemsCategory(curentChampion.getItemBuild().getStartItems(), "Starting Items");
        arrangeItemsCategory(curentChampion.getItemBuild().getCoreItems(), "Core Items");
        arrangeOtherItems(curentChampion.getItemBuild().getEndItems());
        arrangeRunes(Set.of(curentChampion.getRunePage().getKeyStoneRune()), "mainrunes", mainRunesBox);
        arrangeRunes(curentChampion.getRunePage().getSideMainRunes(), "secondaryrunes", mainRunesBox);
        arrangeRunes(curentChampion.getRunePage().getSecondaryRunes(), "secondaryrunes", secondaryRunesAndAttributesBox);
        arrangeAttributes(curentChampion.getRunePage().getAttributes());
        arrangeSummoners(curentChampion.getSummonerSpell());
    }

    private void setLayoutForCommunityCreatedBuild()
    {
        arrangeItemsCategory(curentChampion.getItemBuild().getCoreItems(), "Recommended Items");
        arrangeRunes(Set.of(curentChampion.getRunePage().getKeyStoneRune()), "mainrunes", mainRunesBox);
        arrangeRunes(curentChampion.getRunePage().getSideMainRunes(), "secondaryrunes", mainRunesBox);
        arrangeRunes(curentChampion.getRunePage().getSecondaryRunes(), "secondaryrunes", secondaryRunesAndAttributesBox);
        arrangeAttributes(curentChampion.getRunePage().getAttributes());
        arrangeSummoners(curentChampion.getSummonerSpell());
    }

    private void arrangeSummoners(List<SummonerSpell> summonerSpell)
    {
        for (SummonerSpell summoner : summonerSpell)
        {
            ImageView imageView = new ImageView("images/summoners/" + FormattedString.MOBAFIRE_SUMMONERS_SPECIAL_CASE.toFormat(summoner.name()) + ".png");
            imageView.setFitHeight(48);
            imageView.setFitWidth(48);
            RoundCorners.setRoundedCornerImageView(imageView);
            this.summonersBox.getChildren().add(imageView);
        }
    }

    private void arrangeRunes(Set<Rune> runes, String pathForRunes, Pane group)
    {
        for (Rune rune : runes)
        {
            String runeImageName = FormattedString.URI_IMAGE_FORMAT.toFormat(rune.name());
            ImageView runeImage = new ImageView("images/runes/" + pathForRunes + "/" + runeImageName + ".png");
            Pane container = new Pane(runeImage);
            container.setMaxSize(40, 40);
            runeImage.setFitWidth(40); runeImage.setFitHeight(40);
            if (pathForRunes.equalsIgnoreCase("mainrunes"))
            {
                runeImage.setFitWidth(70);
                runeImage.setFitHeight(70);
                container.setMaxSize(70, 70);
            }
            createTooltipForRune(rune, container);
            group.getChildren().add(container);
        }
    }

    private void arrangeAttributes(ArrayList<Attribute> attributes)
    {
        for (Attribute attribute : attributes)
        {
            String runeImageName = adjustStringForPath(attribute.propertyName());
            ImageView runeImage = new ImageView("images/runes/attributes/" + runeImageName + ".png");
            runeImage.setFitHeight(27); runeImage.setFitWidth(27);

            Pane container = new Pane(runeImage);
            container.setMaxSize(27, 27);

            createTooltipForAttribute(attribute, container);
            this.secondaryRunesAndAttributesBox.getChildren().add(container);
        }
    }

    private void arrangeItemsCategory(Set<Item> items, String category)
    {
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setPrefHeight(156);
        hbox.setFillHeight(true);
        hbox.setAlignment(Pos.CENTER);
        hbox.getStyleClass().add("filled-box");
        double width = 0;
        if (category.contains("Core"))
            hbox.setId("core-item-box");
        for (Item item : items)
        {
            Pane pane = createItemImagePane(item);
            hbox.getChildren().add(pane);
            width += 50;
        }
        hbox.setMinWidth(width + 60);
        generateWrapperAroundBoxWithItems(hbox, category);
    }

    private void arrangeOtherItems(Map<String, Set<Item>> items)
    {
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.setMinHeight(220);
        hbox.getStyleClass().add("filled-box");
        double width = 0;
        for (Set<Item> itemSet : items.values())
        {
            VBox vbox = new VBox();
            vbox.setSpacing(15);
            vbox.setAlignment(Pos.CENTER);
            for (Item item : itemSet)
            {
                Pane pane = createItemImagePane(item);
                vbox.getChildren().add(pane);
            }
            width += 50;
            hbox.getChildren().add(vbox);
        }
        hbox.setMinWidth(width + 60);
        generateWrapperAroundBoxWithItems(hbox, "Other Items");
    }

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

    private void generateWrapperAroundBoxWithItems(HBox hbox, String additionalLabelForBox)
    {
        VBox vbox = new VBox();
        vbox.setSpacing(25);
        vbox.setAlignment(Pos.CENTER);
        VBox labelWithUnderLine = createLabelWithUnderLine(additionalLabelForBox);
        vbox.getChildren().addAll(labelWithUnderLine, hbox);
        this.items.getChildren().add(vbox);
    }

    private void createTooltipForItem(Item item, Node node)
    {
        String description = FormattedString.ITEM_NAME_FORMAT.toFormat(item.name()) + "\n\n" + item.description();
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

    private VBox createLabelWithUnderLine(String itemsClass)
    {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        Line line2D = new Line(0, 0,itemsClass.toCharArray().length * 10,0);
        line2D.setStrokeWidth(2);
        line2D.setStroke(rgb(70, 70, 70));
        Label label = new Label(itemsClass);
        label.getStyleClass().add("filled-box-label");
        vBox.getChildren().addAll(label, line2D);
        return vBox;
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

    private void clearPageAndSwitchToChampions(boolean shouldSwitch, Pane... boxes)
    {
        for (Pane box : boxes)
        {
            box.getChildren().clear();
        }
        if (shouldSwitch)
            JavaLeagueBuilds.getInstance().switchToChampions();
    }

    private void clearPage()
    {
        clearPageAndSwitchToChampions(false , items, summonersBox, mainRunesBox, secondaryRunesAndAttributesBox);
    }
    private String adjustStringForPath(String s)
    {
        return switch (JavaLeagueBuilds.getChosenPage())
        {
            case U_GG, LEAGUE_OF_GRAPHS -> FormattedString.ATTRIBUTE_NAME_URI_FORMAT.toFormat(s);
            case MOBAFIRE -> FormattedString.MOBAFIRE_ATTRIBUTE_SPECIAL_CASE.toFormat(s);
        };
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
    public static BuildScene getInstance()
    {
        return instance;
    }
}
