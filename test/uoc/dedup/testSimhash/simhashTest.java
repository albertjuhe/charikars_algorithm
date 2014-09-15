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
public class simhashTest extends TestCase {

    public void testSimhash() {
        simhash simHash = new simhash();
        bitHashRabin bHR = new bitHashRabin("detecting");
        simHash.add(bHR);
        String simHashVector = simHash.vectorToString().replaceAll(" ","");
        assertTrue(simHashVector.equals("-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-1-111-11-1-1-1-1-1-1-1-1-1-111111-11"));        
        
        bHR = new bitHashRabin("near");
        simHash.add(bHR); 
        simHashVector = simHash.vectorToString().replaceAll(" ","");
        assertTrue(simHashVector.equals("-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-2-202020-2-2-2-2-2-2-2-2-20020202"));        
        
        bHR = new bitHashRabin("duplicates");
        simHash.add(bHR); 
        simHashVector = simHash.vectorToString().replaceAll(" ","");
        assertTrue(simHashVector.equals("-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-3-1-113-111-3-3-3-3-3-3-3-3-311313-13"));               
        assertTrue(simHash.getStringFingerprint().replaceAll(" ","").equals("0000000000000000000000000000000000000000000110110000000001111101"));
    }
    
}
