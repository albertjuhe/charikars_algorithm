package uoc.language;

/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 *    "Apache Lucene" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    "Apache Lucene", nor may "Apache" appear in their name, without
 *    prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.WordlistLoader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.analysis.LengthFilter;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

/** Filters {@link StandardTokenizer} with {@link StandardFilter}, {@link
 * LowerCaseFilter}, {@link StopFilter} and {@link SpanishStemFilter}. */
/**
 * Analyzer for Spanish using the SNOWBALL stemmer. Supports an external list of stopwords
 * (words that will not be indexed at all) and an external list of exclusions (word that will
 * not be stemmed, but indexed) as in German package from Gerhard Schwarz.
 * A default set of stopwords is used unless an alternative list is specified, the
 * exclusion list is empty by default.
 *
 * @author    Alex Murzaku (alex at lissus.com)
 */
public class SpanishAnalyzer extends Analyzer {

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
    public SpanishAnalyzer(String[] stopWords) {
        stopTable = StopFilter.makeStopSet(stopWords);

    }

    /**
     * Builds an analyzer.
    public SpanishAnalyzer() {
    stopTable = StopFilter.makeStopSet(stopWords.SPANISH_STOP_WORDS);  
    }
     */
    /**
     * Builds an analyzer with the given stop words from file.
     */
    public SpanishAnalyzer(File stopWords) {
        try {
            stopTable = new HashSet(WordlistLoader.getWordSet(stopWords));
        } catch (IOException e) {
            System.out.println("Error d'obertura del fitxer de Stop Words!" + e.getMessage());
        }

    }

    public SpanishAnalyzer(File stopWords, String path) {
        try {
            stopTable = new HashSet(WordlistLoader.getWordSet(stopWords));
            System.out.println("STOP WORDS:" + stopTable.size());
        } catch (IOException e) {
            System.out.println("Error d'obertura del fitxer de Stop Words!" + e.getMessage());
        }
        try {
            this.reader = IndexReader.open("resources/es_dicc");
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

    /** Constructs a {@link StandardTokenizer} filtered by a {@link
     * StandardFilter}, a {@link LowerCaseFilter}, a {@link StopFilter}
     * and a {@link SpanishStemFilter}. */
    public final TokenStream tokenStream(String mode, Reader reader) {
        TokenStream result = new StandardTokenizer(reader);
        result = new LengthFilter(result, 3, 30);
        result = new LowerCaseFilter(result);
        result = new StopFilter(result, stopTable);
        result = new StandardFilter(result);      
        //if (this.reader != null && !mode.equalsIgnoreCase("fingerprint")) {        
        result = new uocSpanishSteemer(result, this.reader);
        //} //Steemer de diccionari        
        return result;
    }   
}


