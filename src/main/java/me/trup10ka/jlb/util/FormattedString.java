package me.trup10ka.jlb.util;

import java.util.Arrays;

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
    ATTRIBUTE_NAME_IMAGE_FORMAT
            {
                @Override
                public String toFormat(String toBeFormatted)
                {
                    return toBeFormatted
                            .replaceAll("[+%0-9]+", "")
                            .trim()
                            .replaceAll(" ", "_")
                            .toLowerCase();
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
    };


    public abstract String toFormat(String toBeFormatted);
}
