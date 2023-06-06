package me.trup10ka.jlb.util;


public enum FormattedString
{
    U_GG_CHAMPION_HYPERLINK_FORMAT
            {
                @Override
                public String toFormat(String toBeFormatted)
                {
                    return toBeFormatted
                            .replaceAll("'", "")
                            .replaceAll(" ", "")
                            .replaceAll("\\.", "")
                            .toLowerCase();
                }
            },
    LOG_CHAMPION_HYPERLINK_FORMAT
            {
                @Override
                public String toFormat(String toBeFormatted)
                {
                    String formatted = toBeFormatted
                            .replaceAll("'", "")
                            .replaceAll(" ", "")
                            .replaceAll("\\.", "")
                            .toLowerCase();
                    if (formatted.contains("&"))
                        formatted = formatted.split("&")[0];
                    else if (formatted.contains("glasc"))
                        formatted = formatted.split("glasc")[0];
                    else if (formatted.equals("wukong"))
                        formatted= "monkeyking";
                    return formatted;
                }
            },
    MOBAFIRE_CHAMPION_HYPERLINK_FORMAT
            {
                @Override
                public String toFormat(String toBeFormatted)
                {
                    return toBeFormatted
                            .replaceAll("\\.", "")
                            .replaceAll("'", "")
                            .replaceAll("&", "amp")
                            .replaceAll(" ", "-")
                            .toLowerCase();
                }
            },
    CHAMPION_IMAGE_NAME_FORMAT
            {
                @Override
                public String toFormat(String toBeFormatted)
                {
                    return toBeFormatted
                            .replaceAll("\\.", "")
                            .replaceAll(" ", "")
                            .toLowerCase();
                }
            },
    URI_IMAGE_FORMAT
            {
                @Override
                public String toFormat(String toBeFormatted)
                {
                    return toBeFormatted
                            .toLowerCase()
                            .replaceAll("'", "")
                            .replaceAll(":", "")
                            .replaceAll("[- ]", "_");
                }
            },
    CSV_NAME_FORMAT
            {
                @Override
                public String toFormat(String toBeFormatted)
                {
                    return toBeFormatted
                            .replaceAll(" ", "_")
                            .replaceAll("'", "")
                            .replaceAll(",", "")
                            .toLowerCase();
                }
            },
    ATTRIBUTE_NAME_URI_FORMAT
            {
                @Override
                public String toFormat(String toBeFormatted)
                {
                    String formatted = toBeFormatted
                            .replaceAll("The ", "")
                            .replaceAll("Scaling ", "")
                            .replaceAll("Bonus ", "")
                            .replaceAll(" Shard", "")
                            .replaceAll("[+%0-9]+", "")
                            .trim()
                            .replaceAll(" ", "_")
                            .toLowerCase();
                    if (formatted.equals("cdr"))
                        formatted = "ability_haste";
                    return formatted;
                }
            },
    ITEM_NAME_FORMAT
            {
                @Override
                public String toFormat(String toBeFormatted)
                {
                    String[] allWordsInName = toBeFormatted.split("_");
                    StringBuilder finalName = new StringBuilder();
                    for (String word : allWordsInName)
                    {
                        char[] charactersInWord = word.toCharArray();
                        charactersInWord[0] = Character.toUpperCase(charactersInWord[0]);
                        finalName.append(new String(charactersInWord)).append(" ");
                    }
                    return finalName.toString();
                }
            },
    ITEM_DESCRIPTION
    {
        @Override
        public String toFormat(String toBeFormatted)
        {
            return toBeFormatted
                    .replaceAll("<br>", "\n")
                    .replaceAll("\\.", "\n\n")
                    .replaceAll("[</]+[a-zA-Z]+>", "")
                    .trim();
        }
    },
    RUNE_DESCRIPTION
    {
        @Override
        public String toFormat(String toBeFormatted)
        {
            return toBeFormatted
                    .replaceAll("'", "")
                    .replaceAll("<br>", "\n")
                    .replaceAll("[</]+[a-z=0-9-_ A-Z#']+>", "")
                    .trim();
        }
    },
    MOBAFIRE_SUMMONERS_SPECIAL_CASE
            {
                @Override
                public String toFormat(String toBeFormatted)
                {
                    return toBeFormatted
                            .replaceAll("Unleashed ", "");
                }
            },
    MOBAFIRE_ATTRIBUTE_SPECIAL_CASE
            {
                @Override
                public String toFormat(String toBeFormatted)
                {
                    if (toBeFormatted.contains("Armor"))
                        return "armor";
                    else if (toBeFormatted.contains("Adaptive"))
                        return "adaptive_force";
                    else if (toBeFormatted.contains("Speed"))
                        return "attack_speed";
                    else if (toBeFormatted.contains("haste"))
                        return "ability_haste";
                    else if (toBeFormatted.contains("HP"))
                        return "health";
                    else if (toBeFormatted.contains("Resist"))
                        return "magic_resist";
                    else
                    {
                        System.err.println("FormattedString error: Did not found special case of attribute in Mobafire");
                        return null;
                    }
                }
            }
    ;


    public abstract String toFormat(String toBeFormatted);
}
