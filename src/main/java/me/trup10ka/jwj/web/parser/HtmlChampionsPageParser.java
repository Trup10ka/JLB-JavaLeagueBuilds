package me.trup10ka.jwj.web.parser;

import me.trup10ka.jwj.data.Champion;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

public interface HtmlChampionsPageParser {
    Document parse();
    ArrayList<Champion> champions();
}
