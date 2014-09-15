/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uoc.dedup.document;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * Funtion that generates a SHA1 code from a string.
 */
public class sha1 {

    private MessageDigest md;
    private byte[] buffer, digest;
    private String hash = "";

    public String getHash(String message) throws NoSuchAlgorithmException {
        buffer = message.getBytes();
        md = MessageDigest.getInstance("SHA1");
        md.update(buffer);
        digest = md.digest();

        for(byte aux : digest) {
            int b = aux & 0xff;
            if (Integer.toHexString(b).length() == 1) hash += "0";
            hash += Integer.toHexString(b);
        }

        return hash;
    }

}
