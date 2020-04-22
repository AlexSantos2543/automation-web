package com.automation.web.config;

import com.automation.web.translationi18n.GoogleSheetsDocument;
import com.automation.web.translationi18n.TranslationSheetSchema;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TranslationsMain {
    public static void main(String... args) throws IOException, GeneralSecurityException {

        List<TranslationSheetSchema> sheets = Arrays.asList(
                new TranslationSheetSchema("Translation", Collections.singletonList("key"))
        );

        GoogleSheetsDocument doc = new GoogleSheetsDocument(
                "BCD Automation Translations",
                "1LiKL8YgwcdNdI2BiwpDS680YOWLMkC-fwtG00YxlAf4",
                sheets);
        doc.writeToJsonFiles("src/main/resources/i18n/translations-%s.json");
    }
}