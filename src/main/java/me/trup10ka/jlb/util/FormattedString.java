package me.trup10ka.jlb.util;


public enum FormattedString
{
    U_GG_HYPERLINK_FORMAT
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
    LOG_HYPERLINK_FORMAT
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
    };


    public abstract String toFormat(String toBeFormatted);
}
