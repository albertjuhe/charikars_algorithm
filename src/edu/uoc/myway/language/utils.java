/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uoc.myway.language;

/**
 *
 * @author ajuhe
 */
public class utils {
    public static String getNormalizedLanguage(String language) {        
        
        if (language.equalsIgnoreCase("catalan") || language.equalsIgnoreCase("catala") || language.equalsIgnoreCase("catalan-utf") || language.equalsIgnoreCase("catala-utf")) {
            return "ca";
        }
        if (language.equalsIgnoreCase("spanish") || language.equalsIgnoreCase("spanish-utf") || language.equalsIgnoreCase("castellano") || language.equalsIgnoreCase("castellano-utf")) {
            return "es";
        }
         if (language.equalsIgnoreCase("english") || language.equalsIgnoreCase("english-utf")) {
            return "en";
        }
        
        return language;
    }
}
