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
    RESOURCES_IMAGE_FORMAT
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
            };

    public abstract String toFormat(String toBeFormatted);
}
