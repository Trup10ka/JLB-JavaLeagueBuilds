package me.trup10ka.jlb.data.esport;


import java.util.Date;

public class Match
{
    private final Team blueTeam;

    private final Team redTeam;

    private final MatchState state;

    private final Date time;

    public Match(Team blueTeam, Team redTeam, MatchState state, Date time)
    {
        this.blueTeam = blueTeam;
        this.redTeam = redTeam;
        this.state = state;
        this.time = time;
    }
    public Match(Team blueTeam, Team redTeam, MatchState state)
    {
        this.blueTeam = blueTeam;
        this.redTeam = redTeam;
        this.state = state;
        this.time = null;
    }

    @Override
    public String toString()
    {
        return "\nMatch:" + "\nblueTeam = " + blueTeam + "\nredTeam=" + redTeam + "\nstate =" + state + "\ntime =" + time + "\n";
    }

    public Date getTime()
    {
        return time;
    }

    public Team getBlueTeam()
    {
        return blueTeam;
    }

    public Team getRedTeam()
    {
        return redTeam;
    }

    public MatchState getState()
    {
        return state;
    }
}
