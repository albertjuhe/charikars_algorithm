/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uoc.language;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

import org.apache.lucene.search.*;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.*;

import java.io.*;

/**
 *
 * @author ajuhe
 */
public class uocSpanishSteemer extends uocStemmer {

    private SpanishStemmer stemmer;

    public uocSpanishSteemer(TokenStream in, IndexReader reader) {
        super(in, reader);
        stemmer = new SpanishStemmer();
    }

    protected Token searchRoot(Token t) {
        TermDocs tDocs;
        try {
            tDocs = this.reader.termDocs(new Term("word", t.term()));
            if (tDocs.next()) {
                int id_doc = tDocs.doc();
                String lema = this.reader.document(id_doc).get("root");
                t.setTermBuffer(lema.toCharArray(), 0, lema.length());
            } else {
                stemmer.setCurrent(t.termText());
                stemmer.stem();
                String s = stemmer.getCurrent();
                if (!s.equals(t.term())) {
                    t.setTermBuffer(s.toCharArray(), 0, s.length());
                    return t;
                }
               
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return t;
    }
}
