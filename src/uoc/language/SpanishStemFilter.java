package uoc.language;   
   
import java.io.IOException;   
   
import org.apache.lucene.analysis.Token;   
import org.apache.lucene.analysis.TokenFilter;   
import org.apache.lucene.analysis.TokenStream;   
   
/**   
  * Spanish stemming algorithm.  
  */   
public final class SpanishStemFilter extends TokenFilter {   
        
     private SpanishStemmer stemmer;   
     private Token token = null;   
        
     public SpanishStemFilter(TokenStream in) {   
         super(in);   
         stemmer = new SpanishStemmer();   
     }   
        
     /** Returns the next input Token, after being stemmed */   
     public final Token next() throws IOException {   
         if ((token = input.next()) == null) {   
             return null;   
         }   
         else {   
             stemmer.setCurrent(token.termText());   
             stemmer.stem();   
             String s = stemmer.getCurrent();   
             if ( !s.equals( token.termText() ) ) {   
                 return new Token( s, token.startOffset(),   
                 token.endOffset(), token.type() );   
             }   
             return token;   
         }   
     }   
        
     /**  
      * Set a alternative/custom Stemmer for this filter.  
      */   
     public void setStemmer(SpanishStemmer stemmer) {   
         if ( stemmer != null ) {   
             this.stemmer = stemmer;   
         }   
     }   
 } 