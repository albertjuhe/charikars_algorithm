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

import java.util.List;
import java.util.Iterator;

import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntSet;

/** <p>This class stores a language-model. A rough measure of memory-usage for a
 * language-model with the topmost 400 n-grams is about 6 KB.</p>
 *
 * <p>Please note that the methods of this class are <strong>not</strong>
 * synchronized.</p>
 *
 * @author Marco Olivo &lt;me@olivo.net&gt;
 * @version $Id$
 */
public class LanguageModel {
	/** Map that, for a given n-gram, returns the corresponding index. */
	private Int2IntMap indexMap;
	/** List that, for a given index, returns the corresponding n-gram. */
	private IntList reverseIndexMap;
	/** Map that, given an index extracted from <code>indexMap</code>, returns
	 * the n-gram's frequency.
	 */
	private IncrementalInt2IntMap frequencyMap;

	/** The minimum frequency value seen up to now. Used for checking various
	 * methods' arguments at run-time.
	 */
	private int minFreqSeen;
	/** The size of the underlying maps (kept to speed up access). */
	private int currSize;

	/** Constructor.
	 */
	public LanguageModel() {
		indexMap = new Int2IntOpenHashMap();
		reverseIndexMap = new IntArrayList();
		frequencyMap = new IncrementalInt2IntMap();
		indexMap.defaultReturnValue( -1 );
		frequencyMap.defaultReturnValue( -1 );
		minFreqSeen = Integer.MAX_VALUE;
		currSize = 0;
	}

	/** Merges the two given language-models. The language-models can be of different sizes.
	 *
	 * @param lang1 the first language-model to be merged with the current object.
	 * @param lang2 the second language-model to be merged with the current object.
	 * @param useTopMostNgrams the number of top-most n-grams to be used (if
	 *        <code>useTopMostNgrams &gt; 0</code>, otherwise all n-grams will be used).
	 * @return the {@link LanguageModel} resulting from the union of the two given objects.
	 */
	public static LanguageModel merge( LanguageModel lang1, LanguageModel lang2, int useTopMostNgrams ) {
		final int lang1Size = lang1.size();
		final int lang2Size = lang2.size();

		LanguageModel languageModel = new LanguageModel();

		IntSet lang1Keys = lang1.keySet();
		IntSet lang2Keys = lang2.keySet();
		IncrementalInt2IntMap hash = new IncrementalInt2IntMap();

		for ( Iterator it = lang1Keys.iterator(); it.hasNext(); ) {
			ByteArrayList bal = (ByteArrayList)it.next();
			hash.inc( bal.hashCode(), lang1.getFreq( bal ) );
		}
		for ( Iterator it = lang2Keys.iterator(); it.hasNext(); ) {
			ByteArrayList bal = (ByteArrayList)it.next();
			hash.inc( bal.hashCode(), lang2.getFreq( bal ) );
		}

		int[] ngrams = hash.getOrderedKeysByScore();
		int n = ( useTopMostNgrams > 0 ? Math.min( ngrams.length, useTopMostNgrams ) : ngrams.length );
		for ( int k = 0; k < n; k++ ) {
			try {
				languageModel.add( ngrams[k], hash.get( ngrams[k] ) );
			}
			catch ( IllegalArgumentException e ) {
				System.err.println( e );
				System.err.println( "WARNING: resulting language-model will be very likely invalid!" );
				break;
			}
		}

		return languageModel;
	}

	/** Adds a new element (as a pair n-gram, frequency) to this {@link LanguageModel}.
	 * <strong>No reconstruction</strong> of the backing structures takes place, so use this
	 * method to add only new elements having a frequency less than those of the previously
	 * seen elements.
	 *
	 * @param ngram the hash-code of the n-gram to add.
	 * @param freq the frequency of the n-gram appears.
	 * @throws IllegalArgumentException if the usage of this method contrasts with the
	 *		   explanations given above.
	 */
	public void add( int ngram, int freq ) throws IllegalArgumentException {
		if ( freq > minFreqSeen ) {
			throw new IllegalArgumentException( "Given n-gram frequency has a frequency greater than that of the previous element." );
		}
		if ( indexMap.get( ngram ) != -1 ) {
			throw new IllegalArgumentException( "Given n-gram has already been added to this language-model." );
		}
		minFreqSeen = freq;

		final int newIndex = currSize;
		indexMap.put( ngram, newIndex );
		//reverseIndexMap.put( newIndex, ngram );
		reverseIndexMap.add( ngram );
		frequencyMap.set( newIndex, freq );
		currSize++;
	}

	/** Returns the size of this language-model (i.e., the number of n-grams).
	 *
	 * @return the number of n-grams contained in this language-model.
	 */
	public int size() {
		return currSize;
	}

	/** Returns the {@link IntSet} of n-grams used in this language-model.
	 *
	 * @return the set of keys.
	 */
	public IntSet keySet() {
		return (IntSet)indexMap.keySet();
	}

	/** Returns true if the given n-gram is contained in this language-model.
	 *
	 * @param ngram the n-gram to be looked for in this language-model.
	 * @return whether the given n-gram (in hash-code) can be found or not in this language-model.
	 */
	public boolean contains( int ngram ) {
		return indexMap.containsKey( ngram );
	}

	/** Gets the n-gram at position index.
	 *
	 * @param index the position.
	 * @return the hash-code of the n-gram requested, <code>null</code> if none is found.
	 */
	public int getNgram( int index ) {
		return reverseIndexMap.getInt( index );
	}

	/** Gets the position of the given n-gram.
	 *
	 * @param ngram the hashcode of the n-gram whose frequency is to be found.
	 * @return the position requested.
	 */
	public int getPos( int ngram ) {
		return indexMap.get( ngram );
	}

	/** Gets the frequency of the n-gram corresponding to the given position index.
	 *
	 * @param index the position.
	 * @return the frequency requested.
	 */
	public int getFreq( int index ) {
		return frequencyMap.get( index );
	}

	/** Gets the frequency of the given n-gram.
	 *
	 * @param ngram the n-gram whose frequency is to be found.
	 * @return the frequency requested, or <code>-1</code> if the n-gram cannot be found.
	 */
	public int getFreq( ByteArrayList ngram ) {
		return getFreq( getPos( ngram.hashCode() ) );
	}
}
