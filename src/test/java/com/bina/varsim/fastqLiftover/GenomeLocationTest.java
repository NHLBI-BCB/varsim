package com.bina.varsim.fastqLiftover;

import com.bina.varsim.fastqLiftover.types.GenomeLocation;
import com.bina.varsim.fastqLiftover.types.MapBlock;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for GenomeLocation
 */
public class GenomeLocationTest
        extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public GenomeLocationTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(GenomeLocationTest.class);
    }

    public void testGenomeLocationSimple() {
        final String locationString = "1-100-INV-";
        final GenomeLocation genomeLocation = new GenomeLocation(locationString);

        assertEquals("1", genomeLocation.chromosome);
        assertEquals(100, genomeLocation.location);
        assertEquals(MapBlock.BlockType.INV, genomeLocation.feature);
        assertEquals(1, genomeLocation.direction);
    }
}
