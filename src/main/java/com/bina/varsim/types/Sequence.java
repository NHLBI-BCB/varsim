package com.bina.varsim.types;

import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Stores a sequence of bases as bytes, does no conversion
 *
 * @author johnmu
 */
public class Sequence {
    public static final byte a = 'a', c = 'c', g = 'g', t = 't', n = 'n',
            A = 'A', C = 'C', G = 'G', T = 'T', N = 'N';
    private String _header = "", _name = "";
    private byte[] _seq = null;
    private Integer numNonNBases = null; // This is computed lazily

    public Sequence(String header, byte[] seq, int len) {
        _header = header;
        if (len < 0)
            len = 0;
        if (seq.length < len)
            len = seq.length;
        _seq = new byte[len];
        System.arraycopy(seq, 0, _seq, 0, len);
        if (header.charAt(0) == '>') {
            _header = header.substring(1);
        }
        StringTokenizer toks = new StringTokenizer(header);
        if (toks.hasMoreTokens()) {
            _name = toks.nextToken();
        }
    }

    /**
     * Returns the complement of a single byte.
     * TODO replace this with look-up table
     */
    public static byte complement(final byte b) {
        switch (b) {
            case a:
                return t;
            case c:
                return g;
            case g:
                return c;
            case t:
                return a;
            case A:
                return T;
            case C:
                return G;
            case G:
                return C;
            case T:
                return A;
            case N:
                return N;
            default:
                return b;
        }
    }

    public static boolean isN(final byte b){
        return b == N || b == n;
    }

    /**
     * This is computed lazily, so the first call to this will be slow
     * @return number of non-N bases in the sequence
     */
    public int getNumNonNBases(){
        int count = 0;
        if(numNonNBases == null){
            for (byte b : _seq) {
                if(!isN(b)) count++;
            }
            numNonNBases = count;
        }
        return numNonNBases;
    }

    /**
     * Reverses and complements the bases in place.
     */
    public static void reverseComplement(final byte[] bases) {
        final int lastIndex = bases.length - 1;

        int i, j;
        for (i = 0, j = lastIndex; i < j; ++i, --j) {
            final byte tmp = complement(bases[i]);
            bases[i] = complement(bases[j]);
            bases[j] = tmp;
        }
        if (bases.length % 2 == 1) {
            bases[i] = complement(bases[i]);
        }
    }

    /**
     * This is usually the chromosome name
     *
     * @return The first token in the header separated by white space.
     */
    public String getName() {
        return _name;
    }

    /**
     * This is the full header with everything even garbage after the chromosome name
     *
     * @return the FASTA file header, ie. after '>'
     */
    public String getHeader() {
        return _header;
    }

    public int length() {
        return _seq.length;
    }

    /**
     * 1-indexed get from the sequence
     *
     * @param p index location to get from (1-indexed)
     * @return byte at that particular location
     */
    public byte byteAt(int p) {
        return _seq[p - 1];
    }

    /**
     * @param beginIndex start of subsequence (inclusive)
     * @param endIndex   end of subsequence (exclusive)
     * @return
     */
    public byte[] subSeq(int beginIndex, int endIndex) {
        return Arrays.copyOfRange(_seq, beginIndex - 1, endIndex - 1);
    }

    /**
     * Get reverse complemented sequence from a range in the sequence
     *
     * @param from start of subsequence (inclusive)
     * @param to   end of subsequence (exclusive)
     * @return subsequence that has been reverse complemented
     */
    public byte[] revComp(int from, int to) {
        byte[] segment = Arrays.copyOfRange(_seq, from - 1, to - 1);
        reverseComplement(segment);
        return segment;
    }
}
