/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uoc.dedup.document;

import org.apache.lucene.analysis.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 *
 * @author ajuhe
 */
public class textLoader {
    
    private int totalSentences = 0;
    private StringBuffer text = new StringBuffer();
    private FileReader fr = null;
    
    public textLoader() {}
    

    /**
     * Reads a text file. Return Each sentence.
     *
     * @return sentences
     * @throws IOException 
     */
    public int sentenceDetector(FileReader textfile) {     
        if (textfile == null) {
            throw new NullPointerException("textfile may not be null");
        }
        StringBuffer sentence = new StringBuffer();
        this.fr = textfile;
        TokenStream tokens = new WhitespaceTokenizer(getFr());
        try {
            while (true) {
                Token token = tokens.next();
                if (token == null) {
                    break;
                }
                sentence.append(token.term()+" ");
                this.getText().append(token.term()+" ");
                if (token.term().equals(".") || token.term().endsWith(".")) {
                    this.totalSentences++;
                    sentence = new StringBuffer();
                } 
            }
            tokens.close();
        } catch (IOException e) {
            System.out.println("Error getTokens: " + e.getMessage());
        }
        return getTotalSentences();
    }   
    
    public int getTotalSentences() {
        return totalSentences;
    }

    public StringBuffer getText() {
        return text;
    }

    public FileReader getFr() {
        return fr;
    }
}
