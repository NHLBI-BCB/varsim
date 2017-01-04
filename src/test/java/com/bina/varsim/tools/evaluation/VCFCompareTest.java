package com.bina.varsim.tools.evaluation;

import com.bina.varsim.tools.simulation.VCF2diploid;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static htsjdk.samtools.SAMFileHeader.GroupOrder.reference;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by guoy28 on 10/28/16.
 */
public class VCFCompareTest {
  @Rule
  public TemporaryFolder tmpFolder = new TemporaryFolder();

  public void universalTestMethod(String directory) throws IOException {
    universalTestMethod(directory, new String[0]);
  }
  public void universalTestMethod(String directory, String[] additionalArgs) throws IOException {
    File wd = tmpFolder.newFolder("tmp");
    String truthVcf = new File(directory, "truth.vcf").toString();
    String vcfForCompare = new File(directory, "compare.vcf").toString();
    String expectedFalseNegative = new File(directory, "test_FN.vcf").toString();
    String expectedFalsePositive = new File(directory, "test_FP.vcf").toString();
    String expectedTruePositive = new File(directory, "test_TP.vcf").toString();
    String expectedUnknownFalsePositive = new File(directory, "test_unknown_FP.vcf").toString();
    String expectedUnknownTruePositive = new File(directory, "test_unknown_TP.vcf").toString();

    Path outputFalseNegative = Paths.get(wd.getCanonicalPath(), "test_FN.vcf");
    Path outputFalsePositive = Paths.get(wd.getCanonicalPath(), "test_FP.vcf");
    Path outputTruePositive = Paths.get(wd.getCanonicalPath(), "test_TP.vcf");
    Path outputUnknownFalsePositive = Paths.get(wd.getCanonicalPath(), "test_unknown_FP.vcf");
    Path outputUnknownTruePositive = Paths.get(wd.getCanonicalPath(), "test_unknown_TP.vcf");
    Path outputJson = Paths.get(wd.getCanonicalPath(), "test_report.json");

    //example: java -jar ~/Downloads/varsim_0.6.3/VarSim.jar vcfcompare -true_vcf simu.truth.vcf -prefix try small.lumpy.vcf
    String[] args = new String[]{
            "-true_vcf", truthVcf,
            "-prefix", Paths.get(wd.getCanonicalPath(),"test").toString(),
            vcfForCompare
    };
    VCFcompare.main(ArrayUtils.addAll(args, additionalArgs));
    assertTrue(FileUtils.contentEquals(outputFalseNegative.toFile(), new File(expectedFalseNegative)));
    assertTrue(FileUtils.contentEquals(outputFalsePositive.toFile(), new File(expectedFalsePositive)));
    assertTrue(FileUtils.contentEquals(outputTruePositive.toFile(), new File(expectedTruePositive)));
    assertTrue(FileUtils.contentEquals(outputUnknownFalsePositive.toFile(), new File(expectedUnknownFalsePositive)));
    assertTrue(FileUtils.contentEquals(outputUnknownTruePositive.toFile(), new File(expectedUnknownTruePositive)));
  }

  /**
   * comapre two identical VCFs
   * @throws IOException
   */
  @Test
  public void firstSimpleTest() throws IOException {
    universalTestMethod("src/test/resources/validationTest/identicalTest");
  }

  /**
   * test 2 variants with same mutations but different genotypes
   * -match_geno off
   * @throws IOException
   */
  @Test
  public void genotypeTest1() throws IOException {
    universalTestMethod("src/test/resources/validationTest/genotypeTest1");
  }
  /**
   * test 2 variants with same mutations but different genotypes
   * -match_geno on
   * @throws IOException
   */
  @Test
  public void genotypeTest2() throws IOException {
    universalTestMethod("src/test/resources/validationTest/genotypeTest2", new String[]{"-match_geno"});
  }

  /**
   * comparing two VCFs with two same variants but different reference padding at the beginning
   * @throws IOException
   */
  @Test
  public void differentPaddingTest() throws IOException {
    universalTestMethod("src/test/resources/validationTest/differentPaddingTest");
  }
  /**
   * compare two VCFs with two same left-normalized variants (more work than removing padding is required)
   * a variant in truth VCF will be split and compared against 2 variants in compare.vcf
   * @throws IOException
   */
  @Test
  public void canonicalizationTest1() throws IOException {
    universalTestMethod("src/test/resources/validationTest/canonicalizationTest1");
  }
  /**
   * compare two VCFs with two same left-normalized variants (more work than removing padding is required)
   * a variant in truth VCF will be split and compared against 1 variant in compare.vcf
   * @throws IOException
   */
  @Test
  public void canonicalizationTest2() throws IOException {
    universalTestMethod("src/test/resources/validationTest/canonicalizationTest2");
  }
  /**
   * compare two VCFs with two same left-normalized variants (more work than removing padding is required)
   * 2 variants in truth VCF will be compared against 1 variant in compare.vcf
   * @throws IOException
   */
  @Test
  public void canonicalizationTest3() throws IOException {
    universalTestMethod("src/test/resources/validationTest/canonicalizationTest3");
  }
  /**
   * compare two VCFs with two same balanced, nonreciprocal intrachromosomal translocations
   * (cut-and-paste). Validation will be done by evaluating breakend.
   * @throws IOException
   */
  @Test
  public void breakendEvaluationOnIdenticalBalancedNonreciprocalTranslocation() throws IOException {
    universalTestMethod("src/test/resources/validationTest/breakendTests/breakendEvaluationOnIdenticalBalancedNonreciprocalTranslocation");
  }
  /**
   * compare two VCFs with one insertion and one deletion
   * @throws IOException
   */
  @Test
  public void insertionVsInsertionTest() throws IOException {
    universalTestMethod("src/test/resources/validationTest/insertionVsInsertionTest", new String[]{"-wig", "20", "-over", "0.2"});
  }
  /**
   * compare two VCFs with 2 translocations, same source of duplication, different insertion positions
   * let it fail with a small wiggle setting
   * @throws IOException
   */
  @Test
  public void breakendEvaluationDifferentInsertionsFailWithSmallWiggle() throws IOException {
    universalTestMethod("src/test/resources/validationTest/breakendTests/nonreciprocalWithDifferentInsertionFailWithSmallWiggle", new String[]{"-wig", "1"});
  }
  /**
   * compare two VCFs with 2 translocations, same source of duplication, different insertion positions
   * let it succeed with a large wiggle
   * @throws IOException
   */
  @Test
  public void breakendEvaluationDifferentInsertionsSucceedWithLargeWiggle() throws IOException {
    universalTestMethod("src/test/resources/validationTest/breakendTests/nonreciprocalWithDifferentInsertionSucceedWithLargeWiggle", new String[]{"-wig", "15"});
  }
  /**
   * compare two VCFs with 2 translocations, different sources of duplication, same insertion positions
   * duplications come from two chromosomes, so must fail
   * @throws IOException
   */
  @Test
  public void breakendEvaluationDifferentDuplicationChromosomes() throws IOException {
    universalTestMethod("src/test/resources/validationTest/breakendTests/nonreciprocalWithDifferentDuplicatedSequences");
  }
  /**
   * compare two VCFs with 2 translocations, same sources of duplication, same insertion positions
   * duplications are in different orientations
   * the two translocations (aka interspersed duplications) should not match even if wiggle is very
   * large, because of different orientations in breakend representations.
   * @throws IOException
   */
  @Test
  public void breakendEvaluationDifferentDuplicationOrientations() throws IOException {
    universalTestMethod("src/test/resources/validationTest/breakendTests/nonreciprocalWithDifferentOrientationsAndLargeWiggle", new String[]{"-wig", "100"});
  }
  /**
   * validate one reciprocal balanced translocation with another
   *
   * there are 4 records
   *
   */
  @Test
  public void breakendEvaluationNonReciprocalBalancedWithLargeWiggle() throws IOException {
    universalTestMethod("src/test/resources/validationTest/breakendTests/nonreciprocalBalancedWithLargeWiggle", new String[]{"-wig", "10"});
  }
}
