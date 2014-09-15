/*
 * 
Copyright [2009] [UOC]
This program is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the Free
Software Foundation; either version 2 of the License, or (at your option)
any later version.
This program is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
for more details, currently published at
http://www.gnu.org/copyleft/gpl.html or in the gpl.txt in the wiki2html of
this distribution.
You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc., 51
Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
Authors: Albert Juhé, Universidad Oberta de Catalunya; 
You may contact the author at [ajuhe@omaonk.com]
And the copyright holder at [jrivera@uoc.es] [Av. Tibidabo 39-43 - 08035 Barcelona]
 * 
 */
package uoc.dedup.document;

import org.apache.lucene.analysis.*;

import org.apache.lucene.analysis.shingle.ShingleMatrixFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import java.util.*;
import java.io.*;

import uoc.language.*;
import uoc.dedup.simhash.*;
import uoc.dedup.hamming.*;

/**
 *  
 * Calculate the fingerprint of a document using
 * Charikar simhash.
 * First Split the text in tokens and generate a fingerprint with this tokens usign HashRbing
 * and Charikar's algorithm.
 */
public class fingerprintCharikar extends fingerprintDocument {

    private simhash simHash;
    //bit vector of the fingerprint
    private int[] vFingerprint;
    //Min token generated
    private int MIMGRAMS = 3;
    //Max token generated
    private int MAXGRAMS = 3;
    public int debug = 0;

    /**
     * 
     * @param isr Text that we have to analyzer to generate the fingerprint
     */
    public fingerprintCharikar(Reader isr) {
        super(isr);
        this.simHash = new simhash();
    }

    /**
     * 
     * @param isr Text that we have to analyzer to generate the fingerprint
     * @param language Text language
     */
    public fingerprintCharikar(Reader isr, String language) {
        super(isr, language);
        this.simHash = new simhash();
    }

    /**
     * Calculate the fingerprint.
     * Splt the text in shingles and with each token generate a hashrabin, with the result
     * we generate a final fingerprint vector.
     * @return fingerprint in a string
     */
    public String calculateFingerprint() {
        totalTokens = 0;
        totalnGramTokens = 0;
        TokenStream tk = null;

        if (this.useStemming()) {
            this.analyzer = analyzerCache.newAnalyzer(this.language);
            tk = this.analyzer.tokenStream("fingerprint", reader);
        } else {
            tk = new StandardTokenizer(reader);
        }
        ShingleMatrixFilter tokens = new ShingleMatrixFilter(tk, 1, this.getMAXGRAMS(), new Character(' '));        

        //Put the tokens in a map and select the most important terms.
        try {
            while (true) {
                Token token = tokens.next();
                if (token == null) {
                    break;
                }
                int numtokens = token.term().split(" ").length;
                if (numtokens == 1) {
                    this.add(token.term(), this.m); //Add a token to the list of frequencies tokens                                        
                    //System.out.println(token.term());
                    totalTokens++;
                } else if (numtokens >= this.MIMGRAMS) {                   
                    //System.out.println(token.term());
                    this.add(token.term(), this.nGrams);
                    totalnGramTokens++; //Count the ngram tokens            
                }
            }
            tokens.close();
            this.createTopTerms(this.m, this.getTokensTop(),this.totalTokens);
            //Calculate the fingerprint vector
            this.calculateVectorFingerprint(this.nGrams,this.totalnGramTokens);
            tk.close();
        } catch (IOException e) {
            System.out.println("Error getTokens: " + e.getMessage());
        }
        vFingerprint = this.simHash.getFingerprint();
        this.fingerprint2String();
        return this.getFingerprint();
    }
    
    private void calculateVectorFingerprint(Map<String, word> document_tokens,int total_tokens) {
        bitHashRabin bHR;
        
        for (String clave : document_tokens.keySet()) {
            Integer frequencia = document_tokens.get(clave).getFrequencia();            
            double idf = this.idf_norm(frequencia,total_tokens);
            // ApplyHashrabin function
            bHR = new bitHashRabin(clave,idf);
            //Add the fingerprint of the term to the final fingerprint vector.
            this.simHash.add(bHR);             
        }        
    }

    /**
     * Converts the fingerprint to a string vector.
     */
    private void fingerprint2String() {
        this.setFingerprint("");

        for (int i = 0; i < this.vFingerprint.length; i++) {
            this.setFingerprint(this.vFingerprint[i] + this.getFingerprint());
        }
    }

    public int getMIMGRAMS() {
        return MIMGRAMS;
    }

    public void setMIMGRAMS(int MIMGRAMS) {
        this.MIMGRAMS = MIMGRAMS;
    }

    public int getMAXGRAMS() {
        return MAXGRAMS;
    }

    public void setMAXGRAMS(int MAXGRAMS) {
        this.MAXGRAMS = MAXGRAMS;
    }
}
