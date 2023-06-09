package me.trup10ka.jlb.controllers.builds;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import me.trup10ka.jlb.app.JavaLeagueBuilds;
import me.trup10ka.jlb.data.lolgame.*;
import me.trup10ka.jlb.util.*;
import me.trup10ka.jlb.web.parser.HtmlBuildPageParser;
import me.trup10ka.jlb.web.parser.mobafire.HtmlBuildMobafireParser;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class BuildSceneCommunity
{

    private static BuildSceneCommunity instance;
    /**
     * All possible {@link CommunityBuild community builds} parsed for latest patch
     */
    private List<CommunityBuild> allCommunityBuilds;

    private Champion curentChampion;

    private CommunityBuild currentCommunitybuild;

    @FXML
    private Pane applicationHeader;

    @FXML
    private Button goBack;

    @FXML
    private Label buildName;

    @FXML
    private VBox summonersBox;

    @FXML
    private HBox mainRunesBox;

    @FXML
    private HBox secondaryRunesAndAttributesBox;

    @FXML
    private HBox items;

    @FXML
    private VBox communityBuildsVBox;


    public void initialize()
    {
        applicationHeader.setOnMousePressed(event -> JavaLeagueBuilds.getInstance().setOffSets(event));
        applicationHeader.setOnMouseDragged(event -> JavaLeagueBuilds.getInstance().moveStage(event));
        goBack.setOnMousePressed(event -> clearPageAndSwitchToChampions(true , summonersBox, mainRunesBox, secondaryRunesAndAttributesBox, items, communityBuildsVBox));
    }

    public BuildSceneCommunity()
    {
        instance = this;
    }

    public void setBuildToParse(List<CommunityBuild> allCommunityBuilds, HtmlBuildPageParser parser, Champion champion)
    {
        this.allCommunityBuilds = allCommunityBuilds;
        this.currentCommunitybuild = allCommunityBuilds.get(0);
        this.curentChampion = new Champion(champion.getName(),
                parser.queryItemBuild(),
                parser.queryRunePage(),
                parser.summoners());
        Platform.runLater(() ->
        {
            this.build();
            this.fillInRemainingBuilds();
        });
    }

    private void setBuildToParse(CommunityBuild communityBuild, HtmlBuildPageParser parser, Champion champion)
    {
        this.currentCommunitybuild = communityBuild;
        this.curentChampion = new Champion(champion.getName(),
                parser.queryItemBuild(),
                parser.queryRunePage(),
                parser.summoners());
        Platform.runLater(this::build);
    }

    private void build()
    {
        ItemBuild itemBuild = curentChampion.getItemBuild();
        RunePage runePage = curentChampion.getRunePage();
        arrangeSummonerSpells();
        arrangeItems(itemBuild);
        arrangeRunes(runePage);
        this.buildName.setText(currentCommunitybuild.nameOfTheBuild());
    }

    private void arrangeItems(ItemBuild itemBuild)
    {
        Set<Item> items = itemBuild.getCoreItems();
        HBox allImages = new HBox();
        double width = 0;
        allImages.setSpacing(10); allImages.setAlignment(Pos.CENTER); allImages.getStyleClass().add("filled-box");
        for (Item item : items)
        {
            allImages.getChildren().add(createItemImage(item));
            width += 50;
            if (allImages.getChildren().size() > 6)
                break;
        }
        allImages.setMinWidth(width + 60);
        this.items.getChildren().add(allImages);
    }

    private void arrangeRunes(RunePage runePage)
    {
        arrangeMainRunes(runePage.getKeyStoneRune(), runePage.getSideMainRunes());
        arrangeSecondaryRunes(runePage.getSecondaryRunes(), runePage.getAttributes());
    }

    private void arrangeSummonerSpells()
    {
        VBox summonersBox = new VBox();
        summonersBox.setSpacing(10);
        summonersBox.setAlignment(Pos.CENTER);
        for (SummonerSpell spell : curentChampion.getSummonerSpell())
        {
            ImageView image = new ImageView("images/summoners/" + spell.name().toLowerCase() + ".png");
            image.setFitHeight(48);
            image.setFitWidth(48);
            RoundCorners.setRoundedCornerImageView(image);
            summonersBox.getChildren().add(image);
        }
        this.summonersBox.getChildren().add(summonersBox);
    }

    private void arrangeMainRunes(Rune keyStoneRune, Set<Rune> sideMainRunes)
    {
        createRunesPane(Set.of(keyStoneRune), "mainrunes", mainRunesBox);
        createRunesPane(sideMainRunes, "secondaryrunes", mainRunesBox);
    }

    private void arrangeSecondaryRunes(Set<Rune> secondaryRunes, List<Attribute> attributes)
    {
        createRunesPane(secondaryRunes, "secondaryrunes", secondaryRunesAndAttributesBox);
        createAttributePane(attributes);
    }

    private void createRunesPane(Set<Rune> keyStoneRune, String pathForRunes, Pane group)
    {
        for (Rune rune : keyStoneRune)
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

    private void createAttributePane(List<Attribute> keyStoneRune)
    {
        for (Attribute attribute : keyStoneRune)
        {
            String runeImageName = FormattedString.MOBAFIRE_ATTRIBUTE_SPECIAL_CASE.toFormat(attribute.propertyName());
            ImageView runeImage = new ImageView("images/runes/attributes/" + runeImageName + ".png");
            runeImage.setFitHeight(27); runeImage.setFitWidth(27);

            Pane container = new Pane(runeImage);
            container.setMaxSize(27, 27);

            createTooltipForAttribute(attribute, container);
            this.secondaryRunesAndAttributesBox.getChildren().add(container);
        }
    }

    private Pane createItemImage(Item item)
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

    private void assignTooltipForNode(Node node, String description)
    {
        Tooltip tooltip = new Tooltip(description);
        tooltip.setShowDelay(new Duration(0));
        tooltip.setMaxWidth(300);
        tooltip.setWrapText(true);
        tooltip.getStyleClass().add("tool-tip-item");
        Tooltip.install(node, tooltip);
    }
    private void fillInRemainingBuilds()
    {
        for (CommunityBuild communitybuild : allCommunityBuilds)
        {
            JFXButton buildButton = new JFXButton(communitybuild.nameOfTheBuild());
            buildButton.setMinSize(200, 70);
            buildButton.setTextAlignment(TextAlignment.CENTER);
            buildButton.getStyleClass().add("comm-build-button");
            if (!(communitybuild.rating().up() == -1))
                buildButton.setText(communitybuild.nameOfTheBuild() + "\nBuild by: " +
                        communitybuild.creatorName() + "\n" + communitybuild.rating().up() + " upvotes " + communitybuild.rating().down() + " downvotes");
            else
                buildButton.setText(communitybuild.nameOfTheBuild() + "\nBuild from: " +
                        communitybuild.creatorName() + "\n" + "Rating is still pending");
            buildButton.setOnAction(actionEvent -> switchCommunityBuilds(communitybuild));
            this.communityBuildsVBox.getChildren().add(buildButton);
        }
    }

    private void switchCommunityBuilds(CommunityBuild communitybuild)
    {
        if (currentCommunitybuild == communitybuild)
            return;
        clearPage();
        CompletableFuture.runAsync(() ->
        {
            HtmlBuildPageParser buildPageParser = new HtmlBuildMobafireParser(communitybuild.buildURL());
            this.setBuildToParse(communitybuild, buildPageParser, this.curentChampion);
        });
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

    public static BuildSceneCommunity getInstance()
    {
        return instance;
    }
}
