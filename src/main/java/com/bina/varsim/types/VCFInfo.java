package com.bina.varsim.types;

import java.rmi.UnexpectedException;
import java.util.*;
import java.util.concurrent.Exchanger;

/**
 * Created by guoy28 on 10/5/16.
 * CLass for VCF INFO field.
 * TODO: this class should be augmented to handle FORMAT field
 * TODO: constructors of this class should honor specifications outlined in VCF header
 */
public class VCFInfo {
    final private Map<String, VCFInfoElement> info2Value;

    /**
     * parse INFO field string
     * store each key value pair in a map
     * @param infoString
     */
    public VCFInfo(final String infoString) throws UnexpectedException {
        this.info2Value = new HashMap<String, VCFInfoElement>();
        String[] infos = infoString.split(";");
        for (int i = 0; i < infos.length; i++) {
            String[] keyAndValue = infos[i].split("=");
            if (keyAndValue.length > 1) {
                this.info2Value.put(keyAndValue[0], new VCFInfoElement(keyAndValue[0], keyAndValue[1]));
            } else {
                //must be boolean or flag
                this.info2Value.put(keyAndValue[0], new VCFInfoElement(keyAndValue[0]));
            }
        }
    }

    /**
     * return value indexed by id, if id is absent
     * return null
     * @param id
     * @return
     */
    public <T> T getValue(String id, Class<T> type) {
        return type.cast(this.info2Value.containsKey(id) ? this.info2Value.get(id).getValue(type) : null);
    }
    private class VCFInfoElement<T> {
        private T value;
        private Class<T> type;

        /**
         * parse comma separated value and store it
         * in proper types
         * @param id
         * @param vcfIdValue
         */
        public VCFInfoElement(final String id, String vcfIdValue) throws UnexpectedException {
            this.type = getType(id);
            String[] valueArray = vcfIdValue.split(",");
            if (type == int[].class) {
                int[] nums = new int[valueArray.length];
                for (int i = 0; i < valueArray.length; i++) {
                    nums[i] = Integer.parseInt(valueArray[i]);
                }
                this.value = type.cast(nums);
            } else if (type == String[].class) {
                    this.value = type.cast(valueArray);
            } else {
                throw new UnexpectedException("ERROR: only Integer and String supported for INFO field (" + id + ").");
            }
        }

        /**
         * store id as a boolean field
         */
        public VCFInfoElement(final String id) {
            this.type = getType(id);
            this.value = type.cast(true);
        }

        /**
         * return appropriate values based on types
         * @return return should be casted before being return
         */
        private <T> T getValue(Class<T> t) {
            return t.cast(value);
        }
    }

    /**
     * return hard-coded type for some INFO IDs (including some reserved IDs in VCF
     * spec)
     * TODO: replace hard-coded infoID-type mapping with VCF header defined mapping
     *
     "##INFO=<ID=SVLEN,Number=.,Type=Integer,Description=\"Length of variant\">\n" +
     "##INFO=<ID=POS2,Number=1,Type=Integer,Description=\"1-based Start position of source sequence\">\n" +
     "##INFO=<ID=END2,Number=1,Type=Integer,Description=\"1-based End position of source sequence\">\n" +
     "##INFO=<ID=SVTYPE,Number=1,Type=String,Description=\"Type of structural variant\">\n" +
     "##INFO=<ID=CHR2,Number=1,Type=String,Description=\"Chromosome of source sequence\">\n" +
     " source sequence deleted (SELFISHNESS); source sequence accepted (CHIVALRY).\">\n"

     * @param infoID
     * @return
     */
    public static Class getType(final String infoID) {
        if (infoID.equals("SVLEN") || infoID.equals("POS2") || infoID.equals("END2") || infoID.equals("END")
                || infoID.equals("DP")) {
            return int[].class;
        } else if (infoID.equals("SVTYPE") || infoID.equals("CHR2") || infoID.equals("TRAID")) {
            return String[].class;
        } else if (infoID.equals("ISINV")) {
            return Boolean.class;
        } else {
            //unrecognized INFO ID, return String for now
            return String[].class;
        }
    }
}
