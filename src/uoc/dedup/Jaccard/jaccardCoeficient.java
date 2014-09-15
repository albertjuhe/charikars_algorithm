package uoc.dedup.Jaccard;

/**
 *
 * Given two objects, A and B, each with n binary attributes, the Jaccard 
 * coefficient is a useful measure of the overlap that A and B share with their attributes. 
 * Each attribute of A and B can either be 0 or 1. The total number of each combination of attributes 
 * for both A and B are specified as follows:
 *
 * M11 represents the total number of attributes where A and B both have a value of 1.
 * M01 represents the total number of attributes where the attribute of A is 0 and the attribute of B is 1.
 * M10 represents the total number of attributes where the attribute of A is 1 and the attribute of B is 0.
 * M00 represents the total number of attributes where A and B both have a value of 0.
 * Each attribute must fall into one of these four categories, meaning that
 * M11 + M01 + M10 + M00 = n.
 * The Jaccard similarity coefficient, J, is given as
 * Coeficient = M11 / (M01 + M10 + M11)
 * 
 */
public class jaccardCoeficient {
    
    /**
     * Calculates the Jaccard Coeficient. Source and target have to have the same number of bits.
     * @param source vector of bits
     * @param target vector of bits
     * @return
     */
    public static double Coeficient(int[] source, int[] target) {
        long M11 = 0;
        long M01 = 0;
        long M10 = 0;
        long M00 = 0;
        double coeficient = 0;

        if (source.length != target.length) {
            return coeficient;
        }
        for (int i = 0; i < source.length; i++) {
            if (source[i] == 1 && target[i] == 1) {
                M11++;
            }
            if (source[i] == 0 && target[i] == 1) {
                M01++;
            }
            if (source[i] == 1 && target[i] == 0) {
                M10++;
            }
            if (source[i] == 0 && target[i] == 0) {
                M00++;
            }
        }
        coeficient = (double)M11 / (double)(M01 + M10 + M11);


        return coeficient * 100;
    }
      public static double Coeficient(String source, String target) {
        long M11 = 0;
        long M01 = 0;
        long M10 = 0;
        long M00 = 0;
        double coeficient = 0;

        if (source.length() != target.length()) {
            return coeficient;
        }
        for (int i = 0; i < source.length(); i++) {
            if (source.charAt(i) == '1' && target.charAt(i) == '1') {
                M11++;
            }
            if (source.charAt(i) == '0' && target.charAt(i) == '1') {
                M01++;
            }
            if (source.charAt(i) == '1' && target.charAt(i) == '0') {
                M10++;
            }
            if (source.charAt(i) == '0' && target.charAt(i) == '0') {
                M00++;
            }
        }
        coeficient = (double)M11 / (double)(M01 + M10 + M11);


        return coeficient * 100;
    }
}
