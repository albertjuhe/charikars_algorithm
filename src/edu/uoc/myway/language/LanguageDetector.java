package edu.uoc.myway.language;

import it.unimi.dsi.fastutil.bytes.ByteArrayList;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.olivo.lc4j.LanguageCategorization;

public class LanguageDetector {

    public LanguageCategorization lc;
    private String languagesModelsDirPath = "resources\\models";

    public LanguageDetector() {
        lc = new LanguageCategorization();
        try {
            lc.loadLanguages(languagesModelsDirPath);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage() + "- No s'ha trobat el directori: " + languagesModelsDirPath);
        } catch (IOException e) {
            System.out.println(e.getMessage() + " - Error de lectura del directori: " + languagesModelsDirPath);
        }
    }

    public LanguageDetector(String languagesModelsDirPath) {
        lc = new LanguageCategorization();
        this.languagesModelsDirPath = languagesModelsDirPath;

        try {
            lc.loadLanguages(languagesModelsDirPath);
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage() + "- No s'ha trobat el directori: " + languagesModelsDirPath);
        } catch (IOException e) {
            System.out.println(e.getMessage() + " - Error de lectura del directori: " + languagesModelsDirPath);
        }
    }

    public String getModelsDir() {
        return lc.getLanguageModelsDir();
    }

    @SuppressWarnings("unchecked")
    public String detect(String xmlResult) {
        List languageList = new ArrayList();
        byte[] b = xmlResult.getBytes();
        ByteArrayList bList = new ByteArrayList(b);
        languageList = lc.findLanguage(bList);
        String language = (String) languageList.get(0);
        language = language.replaceFirst(".lm", "");
        String code = utils.getNormalizedLanguage(language);
        return code;
    }

    public String getLanguagesModelsDirPath() {
        return languagesModelsDirPath;
    }

    public void setLanguagesModelsDirPath(String languagesModelsDirPath) {
        this.languagesModelsDirPath = languagesModelsDirPath;
    }
}
