package me.trup10ka.jlb.data.esport;

import org.json.JSONObject;

import java.util.List;

public record Schedule(Match liveMatch, List<Match> listOfMatches)
{
}
