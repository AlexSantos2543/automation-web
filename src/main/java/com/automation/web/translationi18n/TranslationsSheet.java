package com.automation.web.translationi18n;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import lombok.Getter;
import lombok.ToString;

@ToString
public class TranslationsSheet {
    private final TranslationSheetSchema schema;
    private final List<Integer> keyColumns = new LinkedList<>();
    private final Map<Language, Integer> languageColumns = new LinkedHashMap<>();

    /**
     * This list of languages includes everything that was found on the sheet.
     */
    @Getter
    private final List<Language> languages = new ArrayList<>();

    /**
     * This mapping is indexed by row first, then by column.
     * The first item in each row is the key. After the first one,
     * translated strings in the row are in order of this.getLanguages().
     */
    @Getter
    private final List<List<String>> translations = new ArrayList<>();

    public TranslationsSheet(TranslationSheetSchema schema, List<List<Object>> rows) {
        this.schema = schema;

        List<Object> headers = rows.remove(0); // first row
        setLanguageIndices(headers);
        parseLanguageHeaders(headers);

        int maxColumn = languageColumns.values().stream().reduce(Integer::max).orElse(1);

        for (List row : rows) {
            // only process rows that have enough values
            if (row.size() > maxColumn) {
                List<String> translationsRow = new ArrayList<>();
                translations.add(translationsRow);

                StringBuilder key = new StringBuilder();
                for (int keyColumn : keyColumns) {
                    if (keyColumn != keyColumns.get(0)) {
                        key.append(".");
                    }
                    key.append(row.get(keyColumn).toString());
                }
                translationsRow.add(key.toString());

                for (Language language : languages) {
                    int langIndex = languageColumns.get(language);
                    String translatedString = row.get(langIndex).toString();
                    translationsRow.add(translatedString);
                }
            }
        }
    }

    /**
     * Find key columns and save their indices
     *
     * @param headers list of column headers from first row of data
     */
    private void setLanguageIndices(List<Object> headers) {
        for (String keyColumnHeader : schema.getKeyColumnHeaders()) {
            for (int i = 0; i < headers.size(); i++) {
                if (keyColumnHeader.equals(headers.get(i))) {
                    keyColumns.add(i);
                    break;
                }
            }
        }
    }

    /**
     * Parse languages from column headers
     *
     * @param headers list of column headers from first row of data
     */
    private void parseLanguageHeaders(List<Object> headers) {
        for (int i = 0; i < headers.size(); i++) {
            try {
                Language language = Language.parse(headers.get(i).toString());
                languages.add(language);
                languageColumns.put(language, i);
            } catch (NoSuchLanguageException ignore) {
                // this column does not contain translated strings, moving on
            }
        }
    }

    public Map<Language, Properties> asPropertiesMap() {
        Map<Language, Properties> propsMap = new EnumMap<>(Language.class);

        for (Language language : languages) {
            Properties properties = new Properties();
            propsMap.put(language, properties);

            // add a comment to track which sheet we are adding
            properties.setProperty("#sheet_" + schema.getSheetName(), language.getAbbr());
        }

        for (List<String> row : translations) {
            String key = row.get(0);
            for (int i = 1; i < row.size(); i++) {
                // insert a backslash before each linebreak
                String value = row.get(i).replaceAll("\n", "\\\\\n");
                Language language = languages.get(i-1);
                propsMap.get(language).setProperty(key, value);
            }
        }

        return propsMap;
    }

    public Map<Language, Map<String, String>> asJsonMap() {
        Map<Language, Map<String, String>> jsonMap = new EnumMap<>(Language.class);

        for (Language language : languages) {
            Map<String, String> map = new HashMap<>();
            jsonMap.put(language, map);

            // add a comment to track which sheet we are adding
            map.put("#sheet_" + schema.getSheetName(), language.getAbbr());
        }

        for (List<String> row : translations) {
            String key = row.get(0);
            for (int i = 1; i < row.size(); i++) {
                String value = row.get(i);
                Language language = languages.get(i-1);
                jsonMap.get(language).put(key, value);
            }
        }

        return jsonMap;
    }
}
