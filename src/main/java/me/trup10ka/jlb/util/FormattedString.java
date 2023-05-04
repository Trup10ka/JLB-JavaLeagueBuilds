package me.trup10ka.jlb.util;

public enum FormattedString
{
    HYPERLINK_FORMAT
            {
                @Override
                public String toForm(String toBeFormatted)
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
                public String toForm(String toBeFormatted)
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
                public String toForm(String toBeFormatted)
                {
                    return toBeFormatted
                            .toLowerCase()
                            .replaceAll(" ", "_")
                            .replaceAll("'", "")
                            .replaceAll(",", "");
                }
            };

    public abstract String toForm(String toBeFormatted);
}
