/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uoc.dedup.document;

/**
 *
 * @author ajuhe
 */
public class word {

    private String word = "";
    private int frequencia = 0;
    private int offset = 0;
    private int lastPos = 0;
    
    public word(String word,int frequencia,int offset) {
        this.word = word;
        this.frequencia = frequencia;
        this.offset = offset;
    }
    
     public word() {        
    }
    
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(int frequencia) {
        this.frequencia = frequencia;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLastPos() {
        return lastPos;
    }

    public void setLastPos(int lastPos) {
        this.lastPos = lastPos;
    }
}
