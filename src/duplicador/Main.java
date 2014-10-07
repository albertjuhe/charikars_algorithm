/*
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
 You may contact the author at [ajuhe@fados.produccions.com]
*/

package duplicador;

import java.util.*;
import java.io.*;
import java.net.*;

import uoc.language.*; 
import uoc.dedup.document.*;
import uoc.dedup.xml.*;
import uoc.dedup.hamming.*;
import uoc.dedup.Jaccard.*;

/**
 *
 * @author ajuhe
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Date start = new Date();
        fingerprintCharikar dedup = null;
        int max = 6;
        int min = 3;

        System.out.println("Near-duplicates detector UOC. v2.5");       
        System.out.println("Charika simhash, with hashRabin and Hamming distance.");
        String usage = "Usage: dedup fitxer_1 fitxer_2 max nmax min nmin \n max = max ngram number (default 6) \n min = min ngram number (default 3)";

        if (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0]))) {
            System.out.println(usage);
            System.exit(0);
        }

        if (args.length == 0) {
            System.err.println("Usage: " + usage);
            System.exit(0);
        }

        for (int i = 2; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("max")) {
                i++;
                try {
                    max = Integer.parseInt(args[i]);
                } catch (NumberFormatException e) {
                    System.err.println("Argument must be an integer");
                    System.exit(1);
                }
            }
            if (args[i].equalsIgnoreCase("min")) {
                i++;
                try {
                    min = Integer.parseInt(args[i]);
                } catch (NumberFormatException e) {
                    System.err.println("Argument must be an integer");
                    System.exit(1);
                }
            }

        }
        System.out.println("Token ngrams "+min+"-"+max+".");
        File origen = new File(args[0]);
        if (!origen.exists()) {
            System.out.println("Document '" + origen.getAbsolutePath() + "' does not exist or is not readable, please check the path");
            System.exit(1);
        }
        File desti = new File(args[1]);
        if (!desti.exists()) {
            System.out.println("Document '" + desti.getAbsolutePath() + "' does not exist or is not readable, please check the path");
            System.exit(1);
        }
        /******************************************************************************/
        try {
            Reader in = new InputStreamReader(new FileInputStream(origen), "UTF-8");
            dedup = new fingerprintCharikar(in);
            dedup.setMIMGRAMS(min);
            dedup.setMAXGRAMS(max);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }catch (UnsupportedEncodingException e) {
            System.out.println("File not found: " + e.getMessage());
        }
        String fingerprint_1 = dedup.calculateFingerprint();

        //System.out.println("Finger print de " + args[0] + ": ->" + fingerprint_1);

        Date end = new Date();
        System.out.println(end.getTime() - start.getTime() + " millisegons");

        /******************************************************************************/
        try {
            Reader in = new InputStreamReader(new FileInputStream(desti), "UTF-8");
            dedup = new fingerprintCharikar(in);
            dedup.setMIMGRAMS(min);
            dedup.setMAXGRAMS(max);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        }catch (UnsupportedEncodingException e) {
            System.out.println("File not found: " + e.getMessage());
        }
        String fingerprint_2 = dedup.calculateFingerprint();

        //System.out.println("Finger print de " + args[1] + ": -> " + fingerprint_2);
        int distancia = hammingDistance.hamming(fingerprint_1, fingerprint_2);
        System.out.println("Hamming-distance:" + distancia);
        System.out.println("Jaccard-Coeficient:" + jaccardCoeficient.Coeficient(fingerprint_1, fingerprint_2) + "%");

        if (distancia > 3) {
            System.out.println(
                    "IT'S NOT NEAR-DUPLICATE, ARE DIFFERENT DOCUMENTS.");
        } else {
            System.out.println(
                    "IS NEAR-DUPLICATE, COULD BE THE SAME DOCUMENTS.");
        }
        end = new Date();
        System.out.println(end.getTime() - start.getTime() + " millisegons");
    }
}
