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

package uoc.dedup.testHamming;

import uoc.dedup.hamming.*;
import junit.framework.*;
/**
 *
 * @author ajuhe
 */
public class hammingDistanceTest  extends TestCase {
    
    public void testHammingDistanceString() {
        String source ="00001101001011101001111";
        String target ="01010111001011001001101";
        int distance = hammingDistance.hamming(source, target);
        assertEquals(distance, 6);               
    }
    
    public void testHammingDistanceInt() {
        int source[] = {0,0,0,0,1,1,0,1,0,0,1,0,1,1,1,0,1,0,0,1,1,1,1};
        int target[] = {0,1,0,1,0,1,1,1,0,0,1,0,1,1,0,0,1,0,0,1,1,0,1};
        int distance = hammingDistance.hamming(source, target);
        assertEquals(distance, 6);       
        
    }
}
