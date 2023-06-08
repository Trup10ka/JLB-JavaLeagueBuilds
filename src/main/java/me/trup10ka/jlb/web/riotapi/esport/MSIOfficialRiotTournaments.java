package me.trup10ka.jlb.web.riotapi.esport;

import me.trup10ka.jlb.data.esport.Match;
import me.trup10ka.jlb.data.esport.MatchState;
import me.trup10ka.jlb.data.esport.Schedule;
import me.trup10ka.jlb.data.esport.Team;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Class which can provide you with data about current International League of Legends matches
 */
public class MSIOfficialRiotTournaments
{
    /**
     * API Riot endpoint for MSI schedule
     */
    private final URL url;

    private final String xAPIKey;
    /**
     * File from which is API key taken
     */
    private final File config;

    public MSIOfficialRiotTournaments()
    {
        URL url = null;
        String xAPIKey = null;
        this.config = new File("config.cg");
        try
        {
            url = new URL("https://esports-api.lolesports.com/persisted/gw/getSchedule?hl=en-GB");
            xAPIKey = getxAPIKeyFromFile();
        } catch (MalformedURLException e)
        {
            System.err.println("Wrong URL: " + url);
        }
        this.url = url;
        this.xAPIKey = xAPIKey;
    }

    /**
     * Reads JSON from API endpoint and converts it into a Schedule table
     * @return schedule containing current live match (if exists) and two upcoming matches (if exists)
     * @see Schedule
     */
    public Schedule getSchedule()
    {
        StringBuilder jsonFileString = new StringBuilder();
        try
        {
            HttpURLConnection getRequest = (HttpURLConnection) this.url.openConnection();
            getRequest.setRequestProperty("x-api-key", xAPIKey);

            try (BufferedReader br = new BufferedReader(new InputStreamReader(getRequest.getInputStream())))
            {
                String line;
                while ((line = br.readLine()) != null)
                    jsonFileString.append(line).append("\n");
            }
        } catch (IOException e)
        {
            System.err.println("Connection failed");
        }
        return parseScheduleFromJson(new JSONObject(jsonFileString.toString()));
    }

    /**
     * Takes in the JSON file and parses it into one live match and two (list of) upcoming matches
     * @param jsonObject JSON file containing whole not parsed MSI schedule
     * @return parsed schedule containing live match and two upcoming matches
     * @see Match
     * @see Schedule
     */
    private Schedule parseScheduleFromJson(JSONObject jsonObject)
    {
        Match liveMatch = parseLiveMatchFromJson(jsonObject);
        List<Match> matches = parseListOfUpcomingMatches(jsonObject);
        return new Schedule(liveMatch, matches);
    }

     /**
     * @param jsonObject schedule in JSON format
     * @return live Match parsed from JSON <br> if it does not exist, returns null
     * @see Match
     * @see MatchState
     */
    private Match parseLiveMatchFromJson(JSONObject jsonObject)
    {
        List<Match> matches = parseMatches(jsonObject, MatchState.INPROGRESS);
        if (matches.size() == 0)
            return null;
        return matches.get(0);
    }
    /**
     * @param jsonObject schedule in JSON format
     * @return live Match parsed from JSON
     * @see Match
     * @see MatchState
    */
    private List<Match> parseListOfUpcomingMatches(JSONObject jsonObject)
    {
        return parseMatches(jsonObject, MatchState.UNSTARTED);
    }

    /**
     * Parses matches with desired MatchState and returns them in a list
     * @param jsonObject schedule JSON file
     * @param matchState state, in which parsed Matches should be
     * @return list of matches in desired match state
     * @see Match
     * @see MatchState
     * @see Team
     */
    private List<Match> parseMatches(JSONObject jsonObject, MatchState matchState)
    {

        List<Match> matches = new ArrayList<>();
        for (Object event : jsonObject.getJSONObject("data").getJSONObject("schedule").getJSONArray("events"))
        {
            JSONObject jsonEvent = (JSONObject) event;
            MatchState state = MatchState.valueOf(jsonEvent.getString("state").toUpperCase());
            if (state != matchState || isAShow(jsonEvent) || isNotAnMSIEvent(jsonEvent))
                continue;
            Team blueTeam = parseTeamFromJSON(jsonEvent, 0);
            Team redTeam = parseTeamFromJSON(jsonEvent, 1);
            Date time = Date.from(Instant.parse(((JSONObject) event).getString("startTime")));
            if (matches.size() == 2)
                break;
            matches.add(new Match(blueTeam, redTeam, state, time));
        }
        return matches;
    }

    /**
     * Parses one of the Teams from JSON Match file
     * @param json JSON format Match
     * @param teamIndex 0 or 1, team index in JSON array
     * @return team parsed on index position
     */
    private Team parseTeamFromJSON(JSONObject json, int teamIndex)
    {
        if (teamIndex > 1 || teamIndex < 0)
            throw new IllegalArgumentException("Invalid team index");
        JSONObject team = (JSONObject) json.getJSONObject("match").getJSONArray("teams").get(teamIndex);
        return new Team(team.getString("name"));
    }

    /**
     * @return API key from config file
     */
    private String getxAPIKeyFromFile()
    {
        String key = null;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(config)))
        {
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                String[] property = line.split(":");
                if (property[0].equals("key"))
                    key = property[1].trim();
            }
        }
        catch (IOException ioException)
        {
            System.err.println("IO error during manipulation with file, reason: " + ioException.getCause().toString());
        }
        if (key == null)
            System.err.println("Error unknown when parsing api key from configuration file");
        return key;
    }

    /**
     * Checks if event is a Show
     * @param jsonObject JSON format Match
     * @return true if it is a show, else false
     */
    private boolean isAShow(JSONObject jsonObject)
    {
        return jsonObject.getString("type").equals("show");
    }

    /**
     * Checks if event match is from MSI league
     * @param jsonObject JSON format Match
     * @return true if it is a not MSI league match, else false
     */
    private boolean isNotAnMSIEvent(JSONObject jsonObject)
    {
        return !(jsonObject.getJSONObject("league").getString("name").equals("MSI"));
    }
}
