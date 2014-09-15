/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uoc.dedup.document;


import org.apache.lucene.analysis.*;



import java.util.*;
import java.io.*;

import edu.uoc.myway.language.LanguageDetector;

/**
 *
 * Detect duplicate documents. v1
 * Base classe of the fingerprint calculator
 * 
 */
abstract public class fingerprintDocument {

    protected Map<String, word> tokensTop; //Most important tokens
    protected Map<String, word> m; //Store 1gram tokens, basically store words.
    protected int totalTokens = 0;
    protected int totalnGramTokens = 0;
    protected int totalSentences = 0;
    protected Map<String, word> nGrams; //Store ngrams tokens
    protected static final int TOPTOKENS = 60; //Number of significant tokens.
    protected double idf_tall = 0.80; //This idf value limits the value of which a word is considered significative
    protected Reader reader;
    private String fingerprint;
    protected StringBuffer sb = new StringBuffer();
    //Only accept words between 4 -30 characters size
    protected static final int MAXLENGTHWORDS = 30;
    protected static final int MINLENGHTWORDS = 3;
    protected LanguageDetector languageDetector = null;
    protected String language = "";
    private boolean useStopWords = true;
    private boolean useStemming = true;
    protected Analyzer analyzer = null;

    /*
     * @param isr: Text that we have to analize.
     */
    public fingerprintDocument(Reader isr) {
        this.m = new HashMap<String, word>();
        this.nGrams = new HashMap<String, word>();
        this.reader = isr;
        String texte = this.readerAsString(isr);
        this.language = this.detectLanguage(texte);
        //No se comfer-ho, així no m'agrada
        //System.out.println("Language detector: " + this.language);
        this.reader = new StringReader(texte);
        this.tokensTop = new HashMap<String, word>(); //Top 1 gram

    }
    
    public fingerprintDocument(Reader isr,String language) {
        this.m = new HashMap<String, word>();
        this.nGrams = new HashMap<String, word>();
        this.reader = isr;        
        this.language = language;
        //System.out.println("Language detector: " + this.language);       
        this.tokensTop = new HashMap<String, word>(); //Top 1 gram

    }

    protected String detectLanguage(String text) {
        this.languageDetector = new LanguageDetector();
        return this.languageDetector.detect(text);
    }

    abstract public String calculateFingerprint();

    /*
     * Add a token to the Map, if the token exists he have to increase the frequnce in 1.
     * We store the offset value and the position of the token.
     * @param term: token
     * @param m: Where we store the term analyzed
     */
    protected void add(String term, Map<String, word> m) {
        word w = m.get(term);
        Integer freq = 1;
        int offset = 0;

        if (w != null) {
            freq = w.getFrequencia();
            offset = w.getOffset() + (this.totalTokens - w.getLastPos());
            freq++;
        } else {
            w = new word();
            w.setWord(term);
        }
        ;
        w.setFrequencia(freq);
        w.setOffset(offset);
        w.setLastPos(this.totalTokens);

        m.put(term, w);
    }

    /*
     * Show the valid tokens found in the collection.
     */
    protected void analisisTokens() {
        System.out.println(this.analisis(this.m));
    }

    /*
     * This is only for test information.
     */
    protected String analisis(Map<String, word> hashMap) {
        StringBuffer output = new StringBuffer();

        for (String clave : hashMap.keySet()) {
            Integer valor = hashMap.get(clave).getFrequencia();
            Integer offset = hashMap.get(clave).getOffset();
            double idf = this.idf_norm(valor,this.totalTokens);
            if (idf < this.idf_tall) {
                output.append(clave + " -> " + valor + " (" + offset + ") idf = " + idf + " \n");
            }
        }
        return output.toString();
    }

    /*
     * Calculate idf normalized.
     */
    protected double idf_norm(int valor,int total) {
        double sup = (total + 0.5) / valor;
        double inf = total + 1;

        double idfnorm = Math.log(sup) / Math.log(inf);
        return idfnorm;
    }

    public String toString() {
        StringBuffer output = new StringBuffer();

        for (String clave : m.keySet()) {
            Integer valor = m.get(clave).getFrequencia();
            output.append(clave + " -> " + valor + "\n");
        }
        return output.toString();
    }
    
     public String toTokens() {
        StringBuffer output = new StringBuffer();

        for (String clave : m.keySet()) {
            Integer valor = m.get(clave).getFrequencia();
            output.append(clave + " ");
        }
        return output.toString();
    }

    private String readerAsString(Reader isr) {
        StringBuffer fileData = new StringBuffer(1000);
        BufferedReader reader = new BufferedReader(isr);
        try {
            char[] buf = new char[1024];
            int numRead = 0;
            while ((numRead = reader.read(buf)) != -1) {
                fileData.append(buf, 0, numRead);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("File not found: " + e.getMessage());
        }

        return fileData.toString();
    }

    /*
     * Add term to the top10 terms analyzing main map tokens       
     */
    protected void createTopTerms(Map<String, word> m, Map<String, word> bestTokens,int total) {
        for (String clave : m.keySet()) {
            double valor = idf_norm(m.get(clave).getFrequencia(),total);
            if (valor < this.idf_tall) {
                this.addTop(clave, m.get(clave), bestTokens);
            }
        }
    }

    public Map<String, word> getTokensTop() {
        return tokensTop;
    }

    /*
     * Add term to the top terms.     
     * @param term: Token in string format
     * @param freq: numer of times that the term appears in the text       
     */
    private void addTop(String term, word w, Map<String, word> bestTokens) {
        String terme_min_valor = "";
        int terme_min_freq = w.getFrequencia();

        if (bestTokens.size() < this.TOPTOKENS) {
            bestTokens.put(term, w);
        } else {
            for (String clave : bestTokens.keySet()) {
                Integer valor = bestTokens.get(clave).getFrequencia();
                if (valor > terme_min_freq) {
                    terme_min_freq = valor;
                    terme_min_valor = clave;
                }
            }
            if (!terme_min_valor.equals("")) {
                bestTokens.remove(terme_min_valor);
                bestTokens.put(term, w);
            }
        }
    }

    public int getTotalTokens() {
        return totalTokens;
    }

    public int getDistinctTokens() {
        return this.m.size();
    }

    public int getTotalSentences() {
        return totalSentences;
    }

    public void setTotalSentences(int totalSentences) {
        this.totalSentences = totalSentences;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public boolean useStopWords() {
        return useStopWords;
    }

    public void setUseStopWords(boolean useStopWords) {
        this.useStopWords = useStopWords;
    }

    public boolean useStemming() {
        return useStemming;
    }

    public void setUseStemming(boolean useStemming) {
        this.useStemming = useStemming;
    }

    public Analyzer getAnalyzer() {
        return analyzer;
    }

    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }

    public Map<String, word> getSingles() {
        return m;
    }
}
