package me.trup10ka.jlb.data;

/**
 * When Rating has both values of <b>-1</b>, rating is still being calculated
 */
public record Rating(short up, short down)
{
    public short calculateDifference()
    {
        return (short) (up - down);
    }
}
