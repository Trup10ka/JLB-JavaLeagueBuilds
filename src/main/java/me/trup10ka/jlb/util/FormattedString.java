package me.trup10ka.jlb.util;

public enum FormattedString
{
    HYPERLINK_FORMAT
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
            };

    public abstract String toFormat(String toBeFormatted);
}
