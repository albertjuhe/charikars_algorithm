/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uoc.dedup.document;

import org.apache.lucene.analysis.*;

import java.security.NoSuchAlgorithmException;
import org.apache.lucene.analysis.shingle.ShingleMatrixFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import java.util.*;
import java.io.*; 

import uoc.language.*;

/**
 *
 * @author ajuhe
 */
public class fingerprintExactDuplicates extends fingerprintDocument {
    
    private Map<String, word> nGramsTop;
    protected static final int MIMGRAMS = 1;
    protected static final int MAXGRAMS = 4;

    public fingerprintExactDuplicates(Reader isr) {
        super(isr);
        this.nGramsTop = new HashMap<String, word>(); //Top2-4 grams
    }

    public String calculateFingerprint() {
        TokenStream tk = new StandardTokenizer(reader);
        tk = new LengthFilter(tk, this.MINLENGHTWORDS, this.MAXLENGTHWORDS);
        tk = new LowerCaseFilter(tk);
        ShingleMatrixFilter tokens = new ShingleMatrixFilter(tk, this.MIMGRAMS, this.MAXGRAMS, new Character(' '));
        this.m = new HashMap<String, word>();
        this.tokensTop = new HashMap<String, word>();
        this.nGrams = new HashMap<String, word>();
        this.nGramsTop = new HashMap<String, word>();
        totalTokens = 0;

        //Put the tokens in a map and select the most important terms.
        try {
            while (true) {
                Token token = tokens.next();
                if (token == null) {
                    break;
                }
                int numtokens = token.term().split(" ").length;
                if (numtokens == 1) {
                    this.add(token.term(),this.m); //Add a token to the list of frequencies tokens                                        
                    totalTokens++;
                } else {
                    this.add(token.term(), this.nGrams);
                    totalnGramTokens++;
                }
            }
            tokens.close();
            this.createTopTerms( this.m,this.getTokensTop(),this.totalTokens);
            this.createTopTerms(this.nGrams, this.getNGramsTop(),this.totalnGramTokens);
        } catch (IOException e) {
            System.out.println("Error getTokens: " + e.getMessage());
        }
        this.generateFingerprint();
        return this.getFingerprint();
    }

    /*
     * Generates a unique id using a hash function (sha1), the
     * id is generated using de terms top frequencies.
     */
    private void generateFingerprint() {
        this.sb = new StringBuffer();
        for (String clave : this.getTokensTop().keySet()) {
            Integer valor = this.getTokensTop().get(clave).getFrequencia();
            Integer offset = this.getTokensTop().get(clave).getOffset();
            this.sb.append(clave);
            this.sb.append(valor);
            this.sb.append(offset);
        }
        sha1 s = new sha1();
        try {
            this.setFingerprint(s.getHash(this.sb.toString()));
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e.getMessage());
        }
    }

    public Map<String, word> getNGramsTop() {
        return nGramsTop;
    }
    
     /*
     * Show the valid tokens found in the collection.
     */
    public void analisisTopTokens() {
        System.out.println(this.analisis(this.getTokensTop()));
    }

    /*
     * Show the valid tokens found in the collection.
     */
    public void analisisTopnGramTokens() {
        System.out.println(this.analisis(this.getNGramsTop()));
    }
}
