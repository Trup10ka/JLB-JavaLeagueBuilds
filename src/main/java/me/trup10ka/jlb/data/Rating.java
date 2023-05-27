package me.trup10ka.jlb.data;

public record Rating(short up, short down)
{
    public short calculateDifference()
    {
        return (short) (up - down);
    }
}
