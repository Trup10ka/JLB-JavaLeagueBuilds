package me.trup10ka.jlb.web.parser;

import me.trup10ka.jlb.data.lolgame.CommunityBuild;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

/**
 * Interface describing a behavior of a parser for All builds which are possible for certain champion
 * <p>
 *     Community builds are published by players, which means there is more than one option
 *     Class which is designed to parse these build must be able to return an <b>ArrayList of {@link CommunityBuild}s</b>
 * </p>
 * @see HtmlAllBuildsPageParser
 * @see CommunityBuild
 */
public interface HtmlAllBuildsPageParser
{
    /**
     * @return parsed HTML document
     */
    Document parse();

    /**
     * @return an ArrayList of Community builds
     */
    ArrayList<CommunityBuild> allCommunityBuilds();

    /**
     * Validates whether the page has been found
     * @param document parsed HTML document
     * @return true, if the document is invalid
     */
    default boolean validate(Document document)
    {
        return document.select("div.page-404").size() != 0;
    }
}
