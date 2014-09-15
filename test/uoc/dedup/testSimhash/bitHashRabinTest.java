/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uoc.dedup.testSimhash;

import junit.framework.*;
import uoc.dedup.simhash.*;

/**
 *
 * @author ajuhe
 */
public class bitHashRabinTest extends TestCase {
   
    public void testHashRabin() {
        bitHashRabin bHR = new bitHashRabin("detecting");
        bHR.setREFERENCE_VALUE_HASH(1);
        Long RabinHashValue = bHR.getBaseNumber();
        assertTrue((RabinHashValue==1704061));
        String RabinHashbitNumber = bHR.getBitString();
        assertTrue(RabinHashbitNumber.equals("0000000000000000000000000000000000000000000110100000000001111101"));
        
        bHR = new bitHashRabin("near");
        bHR.setREFERENCE_VALUE_HASH(1);
        RabinHashValue = bHR.getBaseNumber();
        assertTrue((RabinHashValue==983063));
        RabinHashbitNumber = bHR.getBitString();
        assertTrue(RabinHashbitNumber.equals("0000000000000000000000000000000000000000000011110000000000010111"));
        
        bHR = new bitHashRabin("duplicates");
        bHR.setREFERENCE_VALUE_HASH(1);
        RabinHashValue = bHR.getBaseNumber();
        assertTrue((RabinHashValue==7929981));
        RabinHashbitNumber = bHR.getBitString();
        assertTrue(RabinHashbitNumber.equals("0000000000000000000000000000000000000000011110010000000001111101"));
        
    }
}
