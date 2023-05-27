package me.trup10ka.jlb.web.parser;

import me.trup10ka.jlb.data.lolgame.CommunityBuild;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public interface HtmlAllBuildsPageParser
{
    Document parse();

    ArrayList<CommunityBuild> allCommunityBuilds();

    default boolean validate(Document document)
    {
        return document.select("div.page-404").size() != 0;
    }
}
