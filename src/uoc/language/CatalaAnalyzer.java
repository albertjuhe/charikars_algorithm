package uoc.language;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.WordlistLoader;
import org.apache.lucene.analysis.*;
import org.apache.lucene.index.IndexReader;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author   Albert Juhé
 */
public class CatalaAnalyzer extends Analyzer {

    /**
     * Contains the stopwords used with the StopFilter.
     */
    private Set<Object> stopTable = new HashSet<Object>();
    /**
     * Contains words that should be indexed but not stemmed.
     */
    private Set<Object> exclTable = new HashSet<Object>();
    private IndexReader reader = null;

    /** Builds an analyzer with the given stop words. */
    public CatalaAnalyzer(String[] stopWords) {
        stopTable = StopFilter.makeStopSet(stopWords);

    }

    /**
     * Builds an analyzer.
    public CatalaAnalyzer() {
    stopTable = StopFilter.makeStopSet(stopWords.CATALA_STOP_WORDS);
    }
     */
    /**
     * Builds an analyzer with the given stop words from file.
     */
    public CatalaAnalyzer(File stopWords) {
        try {
            stopTable = new HashSet(WordlistLoader.getWordSet(stopWords));
            System.out.println("STOP WORDS:" + stopTable.size());
        } catch (IOException e) {
            System.out.println("Error d'obertura del fitxer de Stop Words!" + e.getMessage());
        }

    }

    public CatalaAnalyzer(File stopWords, String path) {
        try {
            stopTable = new HashSet(WordlistLoader.getWordSet(stopWords));
            System.out.println("STOP WORDS:" + stopTable.size());
        } catch (IOException e) {
            System.out.println("Error d'obertura del fitxer de Stop Words!" + e.getMessage());
        }
        try {
            this.reader = IndexReader.open("resources/ca_dicc");
        } catch (org.apache.lucene.index.CorruptIndexException ce) {
            System.out.println("Error d'index: " + ce);
        } catch (IOException e) {
            System.out.println("Error directori no trobat: " + e);
        }
    }

    /**
     * Builds an exclusionlist from an array of Strings.
     */
    public void setStemExclusionTable(String[] exclusionList) {
        exclTable = StopFilter.makeStopSet(exclusionList);
    }

    /**
     * Builds an exclusionlist from a Hashtable.
     */
    public void setStemExclusionTable(Set exclusionList) {
        exclTable = exclusionList;
    }

    /**
     * Builds an exclusionlist from the words contained in the given file.
     */
    public void setStemExclusionTable(File exclusionList) {
        try {
            exclTable = WordlistLoader.getWordSet(exclusionList);
        } catch (IOException e) {
            System.out.println("Error d'obertura del fitxer de paraules indexades però no s'aplica stemmer!" + e.getMessage());
        }
    }

    public final TokenStream tokenStream(String mode, Reader reader) {
        TokenStream result = new StandardTokenizer(reader);
        result = new LengthFilter(result, 3, 30);
        result = new LowerCaseFilter(result);
        result = new StopFilter(result, stopTable);
        result = new StandardFilter(result);
        result = new CatalaGeneralStemFilter(result);
        //if (this.reader != null && !mode.equalsIgnoreCase("fingerprint")) {
        result = new uocStemmer(result, this.reader);
        //}

        return result;
    }
}


