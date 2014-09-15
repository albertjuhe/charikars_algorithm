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

import java.io.*;
import java.util.*;

import uoc.dedup.hamming.*;

/**
 *
 * @author ajuhe
 */
public class fingerprintCache {

    static int MAXNUMBER_ITEMS = 100;
   
    private static Map cache = new HashMap();
 
    /**
     * Flush all cached stylesheets from memory, emptying the cache.
     */
    public static synchronized void flushAll() {
        cache.clear();
    }

    /**
     * Flush a specific cached stylesheet from memory.
     *
     * @param xsltFileName the file name of the stylesheet to remove.
     */
    public static synchronized void flush(String fingerprint) {
        cache.remove(fingerprint);
    }

  /**
   * 
   * @param fingerprint
   * @param codi
   * @return
   */
    public static synchronized String newFingerprint(String fingerprint, String codi, int hamming_distance) {
        String codiFP = (String) cache.get(fingerprint);
        boolean trobat = false;

        if (codiFP == null) {
            Set setFP = cache.entrySet();
            Iterator iter = setFP.iterator();
            while (iter.hasNext() && trobat == false) {
                Map.Entry entry = (Map.Entry) iter.next();
                int distancia = hammingDistance.hamming(fingerprint, (String)entry.getKey());
                if (distancia<=hamming_distance) {
                    trobat = true;
                    codiFP = (String)entry.getValue();
                    System.out.println(codi + " igual a " + codiFP + " Hamming = " + distancia);
                }
            }            
        } else trobat=true;
        
        if (trobat==false) cache.put(fingerprint, codi);

        return codiFP;
    }

    // prevent instantiation of this class
    private fingerprintCache() {
    }
    /**
     * This class represents a value in the cache Map.
     */
}
