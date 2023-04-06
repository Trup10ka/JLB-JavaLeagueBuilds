package me.trup10ka.jlb.web.parser;

import me.trup10ka.jlb.data.Champion;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public interface HtmlChampionsPageParser {
    Document parse();
    ArrayList<Champion> champions();
}
