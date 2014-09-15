// This file is part of the language categorization Java library.
// Copyright (C) 2005, 2009 Marco Olivo
//
// lc4j is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public License
// as published by the Free Software Foundation; either version 2
// of the License, or (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with this program; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

package net.olivo.lc4j;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.ArrayList;

/** <p>This class implements a generic hash-map that counts the number of identical objects inserted.</p>
 *
 * <p>This is very useful to count how many times a given {@link Object} (the key of the hash-map) is
 * added to the hash-map.</p>
 *
 * @author Marco Olivo &lt;me@olivo.net&gt;
 * @version $Id$
 */
public class IncrementalObject2IntMap {
	/** The backing hash-map. */
	Object2IntMap m;

	/** Constructor.
	 */
	public IncrementalObject2IntMap() {
		m = new Object2IntOpenHashMap();
		m.defaultReturnValue( 0 );
	}

	/** Sets the default return value.
	 *
	 * @param ret the new default return value.
	 * @return the previous default return value.
	 */
	public int defaultReturnValue( int ret ) {
		int prevRet = m.defaultReturnValue();
		m.defaultReturnValue( ret );

		return prevRet;
	}

	/** Gets the default return value.
	 *
	 * @return the default return value.
	 */
	public int defaultReturnValue() {
		return m.defaultReturnValue();
	}

	/** Gets the value corresponding to the given key.
	 *
	 * @param key the key.
	 * @return the number of times is has been added to the map.
	 */
	public int get( Object key ) {
		return m.getInt( key );
	}

	/** Increments by the value of x the number of times the key has been seen.
	 *
	 * @param key the key.
	 * @param x the number of times the count for key is to be incremented.
	 */
	public void inc( Object key, int x ) {
		x += get( key );
		m.put( key, x );
	}

	/** Sets the count value for key to x.
	 *
	 * @param key the key.
	 * @param x the number of times the count for key is to be set.
	 */
	public void set( Object key, int x ) {
		m.put( key, x );
	}

	/** Gets the list of vectors ordered by score.
	 *
	 * @return an array of {@link Object} representing the keys of the map, ordered by score.
	 */
	public Object[] getOrderedKeysByScore() {
		List l = new ArrayList();
		l.addAll( m.keySet() );
		Collections.sort( l, new KeyComparator() );
		return (Object[])l.toArray( new Object[0] );
	}

	/** Gets the map keys.
	 *
	 * @return the {@link Set} of the map keys.
	 */
 	public Set keySet() {
 		return m.keySet();
 	}

	/** Removes the given key.
	 *
	 * @param key the key to remove.
	 */
 	public void remove( Object key ) {
 		m.remove( key );
 	}

	/** Gets the backing {@link Object2IntMap}.
	 *
	 * @return the backing {@link Object2IntMap}.
	 */
	public Object2IntMap asHashMap() {
		return m;
	}

	/** Gets the size of the underlying hash-map.
	 *
	 * @return the size of the underlying hash-map.
	 */
	public int size() {
		return m.size();
	}

	/** The inner class used to compare keys.
	 */
	private class KeyComparator implements Comparator {
		/** Compare the two given {@link Object}s.
		 *
		 * @param o1 first {@link Object}.
		 * @param o2 second {@link Object}.
		 * @return the result of the comparison.
		 */
		public int compare( Object o1, Object o2 ) {
			if ( IncrementalObject2IntMap.this.m.getInt( o2 ) < IncrementalObject2IntMap.this.m.getInt( o1 ) ) return -1;
			else if ( IncrementalObject2IntMap.this.m.getInt( o2 ) > IncrementalObject2IntMap.this.m.getInt( o1 ) ) return 1;
			else return 0;
		}
	}
}

// Local Variables:
// mode: jde
// tab-width: 4
// End:
