package com.automation.web.translationi18n;

import java.util.List;
import lombok.Getter;

public class TranslationSheetSchema {
    @Getter
    private final String sheetName;
    @Getter
    private final String range;
    @Getter
    private final List<String> keyColumnHeaders;

    public TranslationSheetSchema(String sheetName, List<String> keyColumnHeaders) {
        this(sheetName, sheetName, keyColumnHeaders);
    }

    public TranslationSheetSchema(String sheetName, String range, List<String> keyColumnHeaders) {
        this.sheetName = sheetName;
        this.range = range;
        this.keyColumnHeaders = keyColumnHeaders;
    }
}
