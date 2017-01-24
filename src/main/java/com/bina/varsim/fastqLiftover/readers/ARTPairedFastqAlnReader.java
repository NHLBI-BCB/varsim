package com.bina.varsim.fastqLiftover.readers;

import com.bina.varsim.fastqLiftover.types.SimulatedRead;
import com.bina.varsim.fastqLiftover.types.SimulatedReadPair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class ARTPairedFastqAlnReader implements PairedFastqReader {
    private ARTFastqAlnReader r1;
    private ARTFastqAlnReader r2;

    public ARTPairedFastqAlnReader(final LineNumberReader aln1, final LineNumberReader fastq1, final LineNumberReader aln2, final LineNumberReader fastq2, boolean forceFiveBaseEncoding) throws IOException {
        r1 = new ARTFastqAlnReader(aln1, fastq1, forceFiveBaseEncoding);
        r2 = new ARTFastqAlnReader(aln2, fastq2, forceFiveBaseEncoding);
    }

    public SimulatedReadPair getNextReadPair() throws IOException {
        final SimulatedRead read1 = r1.getNextRead();
        final SimulatedRead read2 = r2.getNextRead();

        if (read1 == null || read2 == null) {
            return null;
        }
        return new SimulatedReadPair(read1, read2);
    }
}
