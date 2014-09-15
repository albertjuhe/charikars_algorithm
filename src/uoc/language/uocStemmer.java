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
import org.apache.lucene.document.Field;

import java.io.*;

/**
 *
 * @author ajuhe
 * This stemmer try to find the lema using two dictionary collections
 */
public class uocStemmer extends TokenFilter {

    IndexReader reader;
    IndexSearcher searcher;

    public uocStemmer(TokenStream in, IndexReader reader) {
        super(in);
        this.reader = reader;
    }

     public final Token next(Token result) throws java.io.IOException {


        Token t = input.next(result);
        if (t == null) {
            return null;
        }
        
        t = removeAccents(t);        
        t = searchRoot(t);
        t = removeAccents(t); 
        
        return t;
    }

    /*
     * 
     * It's possible to return more than one
     */
    protected Token searchRoot(Token t) {
        TermDocs tDocs;
        String lema;

        try {
            tDocs = this.reader.termDocs(new Term("word", t.term()));
            if (tDocs.next()) {
                int id_doc = tDocs.doc();
                Field[] lemas = this.reader.document(id_doc).getFields("root");
                lema = lemas[lemas.length - 1].stringValue();
                t.setTermBuffer(lema.toCharArray(), 0, lema.length());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return t;
    }

    public Token removeAccents(Token t) {
        //Treiem els accents
        char[] buffer = t.termBuffer();
        final int bufferLength = t.termLength();
        int upto = 0;

        for (int i = 0; i < bufferLength; i++) {
            char c = buffer[i];
            buffer[upto++] = modifica(c);
        }
        t.setTermLength(upto);
        return t;
    }

    private char modifica(char caracter) {
        if (caracter == 'à' || caracter == 'á') {
            return 'a';
        }
        if (caracter == 'è' || caracter == 'é') {
            return 'e';
        }
        if (caracter == 'ì' || caracter == 'í') {
            return 'i';
        }
        if (caracter == 'ò' || caracter == 'ó') {
            return 'o';
        }
        if (caracter == 'ù' || caracter == 'ú' || caracter == 'ü') {
            return 'u';
        }

        return caracter;
    }
}
