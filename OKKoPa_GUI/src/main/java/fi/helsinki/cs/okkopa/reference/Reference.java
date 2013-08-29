package fi.helsinki.cs.okkopa.reference;

public class Reference {

    private final ReferenceNumber number;
    private final Integer size;
    private final ReferenceString letters;

    /**
     * Creates a standard size reference code with six digits.
     */
    public Reference() {
        this(6);
    }

    /**
     * Creates a reference code with given number of digits.
     * 
     * @param size
     */
    public Reference(int size) {
        this.size = size;

        this.number = new ReferenceNumber(size);
        this.letters = new ReferenceString(size);
    }

    /**
     *
     * @param number
     * @return checked number
     */
    public boolean checkReferenceNumber(int number) {
        return this.number.checkReferenceNumber(number);
    }

    /**
     *
     * @return reference number
     */
    public int getReferenceNumber() {
        return this.number.getReferenceNumber();
    }

    /**
     *
     * @return reference as String
     */
    public String getReference() {
        return this.letters.getReference();
    }

    /**
     *
     * @param reference
     * @return reference not null or empty
     */
    public boolean checkReference(String reference) {
        return this.letters.checkReference(reference);
    }
}
