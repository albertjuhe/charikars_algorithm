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
package uoc.language;

import java.io.*;
import java.util.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

/**
 *
 * @author ajuhe
 */
public class analyzerCache {
    private static Map cache = new HashMap();

    /**
     * Flush all cached from memory, emptying the cache.
     */
    public static synchronized void flushAll() {
        cache.clear();
    }

    /**
     * Flush a specific cached analyzer from memory.
     *
     * @param xsltFileName the file name of the stylesheet to remove.
     */
    public static synchronized void flush(String language) {
        cache.remove(language);
    }

    /**
     * Obtain a new Analyzer instance for the specified language.
     * A new entry will be added to the cache if this is the first request
     * for the specified file name.
     *
     * @param language language.
     * @return Analyzer.
     */
    public static synchronized Analyzer newAnalyzer(String language) {
         Analyzer entry = (Analyzer) cache.get(language);
         if (entry != null) {
          //System.out.println(language + " CACHED.");  
         } 
        if (entry == null) {          
            if (language.equals(tipo_idioma.ANGLES)) {
                System.out.println("Loading English Steemer.");
                entry = new StandardAnalyzer();
            } else if (language.equals(tipo_idioma.CASTELLA)) {
                System.out.println("Loading Spanish Steemer.");
                entry = new SpanishAnalyzer(new File("resources/stop-spa.txt"), ".");
            } else if (language.equals(tipo_idioma.CATALA)) {
                System.out.println("Loading catalan Steemer.");
                entry = new CatalaAnalyzer(new File("resources/stop-cat.txt"), ".");
            }
            else entry = new StandardAnalyzer();
            cache.put(language, entry);
            //System.out.println(language + " CREATED.");  
        }
         
        return entry;
    }

    // prevent instantiation of this class
    private analyzerCache() {
    }

    
}
