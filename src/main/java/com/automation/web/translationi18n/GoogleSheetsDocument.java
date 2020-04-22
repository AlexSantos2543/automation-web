package com.automation.web.translationi18n;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import lombok.Getter;
import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;

@Log
public class GoogleSheetsDocument {
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "google_sheets_tokens";
    private static final String CREDENTIALS_FILE_PATH = "/google_sheets_credentials.json";

    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);

    @Getter
    private final List<TranslationsSheet> sheets = new ArrayList<>();
    private final Set<Language> allLanguages = new HashSet<>();

    // TODO: extract a connector to allow unit testing; write unit tests
    public GoogleSheetsDocument(String applicationName, String spreadsheetId,
            List<TranslationSheetSchema> schemas) throws IOException, GeneralSecurityException {

        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        Sheets service = new Sheets.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport))
                .setApplicationName(applicationName)
                .build();
        for (TranslationSheetSchema schema : schemas) {
            ValueRange response = service.spreadsheets().values()
                    .get(spreadsheetId, schema.getRange())
                    .execute();
            List<List<Object>> values = response.getValues();

            if (StringUtils.isEmpty(values)) {
                log.info("No data found in sheet " + schema.getRange());
            } else {
                TranslationsSheet sheet = new TranslationsSheet(schema, values);
                sheets.add(sheet);
                allLanguages.addAll(sheet.getLanguages());
            }
        }
    }

    /**
     * Creates an authorized Credential object.
     *
     * @param httpTransport The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the google_sheets_credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport httpTransport) throws IOException {
        // Load client secrets.
        InputStream in = GoogleSheetsDocument.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public Map<Language, File> getLanguageFiles(String filenameFormat) {
        Map<Language, File> files = new EnumMap<>(Language.class);

        for (Language language : allLanguages) {
            files.put(language, new File(String.format(filenameFormat, language.getAbbr())));
        }
        return files;
    }

    public void writeToJsonFiles(String filenameFormat) throws IOException {
        Map<Language, Map<String, String>> allTranslations = new EnumMap<>(Language.class);

        // collect all strings for all sheets and all languages into a single map
        for (TranslationsSheet sheet : sheets) {
            Map<Language, Map<String, String>> jsonMap = sheet.asJsonMap();
            for (Language language : allLanguages) {
                allTranslations.putIfAbsent(language, new HashMap<>());
                allTranslations.get(language).putAll(jsonMap.get(language));
            }
        }

        // write parts of the map as different json files
        Map<Language, File> languageFiles = getLanguageFiles(filenameFormat);
        for (Language language : allLanguages) {
            File file = languageFiles.get(language);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileUtils.write(file, gson.toJson(allTranslations.get(language)), StandardCharsets.UTF_8);
        }
    }

    public void writeToPropertiesFiles(String filenameFormat) throws IOException {
        Map<Language, File> languageFiles = getLanguageFiles(filenameFormat);

        // clear old translations
        for (File file : languageFiles.values()) {
            FileUtils.deleteQuietly(file);
        }

        // append properties from each sheet to the language files
        for (TranslationsSheet sheet : sheets) {
            Map<Language, Properties> allTranslations = sheet.asPropertiesMap();

            for (Language language : allLanguages) {
                File file = languageFiles.get(language);

                FileOutputStream out = new FileOutputStream(file, true);
                allTranslations.get(language).store(out, null);
            }
        }
    }
}
