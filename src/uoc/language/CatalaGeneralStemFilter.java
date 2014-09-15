/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uoc.language;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

/**
 *
 * @author $Albert Juhé
 */
public class CatalaGeneralStemFilter extends TokenFilter {

    public CatalaGeneralStemFilter(TokenStream in) {
        super(in);
    }

    /** Returns the next token in the stream, or null at EOS.
     * <p>Removes <tt>`'"</tt> of words.
     */
    public final Token next(Token result) throws java.io.IOException {
        Token t = input.next(result);
       
        if (t == null) {
            return null;
        }
        //System.out.println("Paraula" + t.toString());
        char[] buffer = t.termBuffer();
        final int bufferLength = t.termLength();
        final String type = t.type();
     
        if (bufferLength > 2 && buffer[1] == '\'') {
            t.setTermBuffer(buffer, 2, bufferLength - 2);
        }

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
