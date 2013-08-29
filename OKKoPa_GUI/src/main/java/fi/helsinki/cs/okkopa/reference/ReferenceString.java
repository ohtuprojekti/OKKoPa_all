package fi.helsinki.cs.okkopa.reference;

import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;

public class ReferenceString {

    private final int size;
    private final Random randomGenerator;
    private String string;

    /**
     * Creates a new random reference code with given size.
     * 
     * @param size
     */
    public ReferenceString(int size) {
        this.size = size;
        randomGenerator = new Random();
    }

    /**
     * Generates a reference code with mixed letters and numbers where 
     * the first digit is always a check number.
     * 
     * @return reference as String
     */
    public String getReference() {
        string = "" + (1 + randomGenerator.nextInt(9)) + this.getRandomPart();
        return string + this.getCheckPart(string);
    }

    private String getRandomPart() {
        return RandomStringUtils.randomAlphanumeric(size - 2).toLowerCase();
    }

    private String getCheckPart(String string) {
        int sum = 0;
        for (int i = 0; i < string.length(); i++) {
            sum += string.charAt(i);
        }
        if (sum % 36 < 10) {
            return "" + sum % 36;
        } else {
            return Character.toString((char) (87 + sum % 36));
        }
    }

    /**
     * Checks if a reference code is valid.
     * 
     * @param reference
     * @return boolean checked reference
     */
    public boolean checkReference(String reference) {
        if (reference == null || reference.equals("")) {
            return false;
        }

        String subString = reference.substring(0, reference.length() - 1);
        if (reference.equals(subString + this.getCheckPart(subString))) {
            return true;
        } else {
            return false;
        }
    }
}
