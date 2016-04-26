package org.pdftools.utils;

/**
 * Enum to keep numbers as constants and to avoid magic number issues in sonar
 * @author asingh7!
 *
 */
public enum NumberEnum {
  
    ZERO("0"), ONE("1"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"), TEN("10"), ELEVEN("11"), TEWELVE("12"), THIRTEEN("13"), 
    FOURTEEN("14"), FIFTEEN("15"), SIXTEEN("16"), SEVENTEEN("17"), EIGHTEEN("18"), NINETEEN("19"), TWENTY("20"), THIRTY("30"), 
    HUNDRED("100");
    
    private String value;
     
    /**
     * Instantiates a new number enum.
     *
     * @param val
     */
    private NumberEnum(String val) {
        value = val;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public int getValue() {
        return Integer.valueOf(value);
    }
    
    /**
     * Gets the float value.
     *
     * @return the float value
     */
    public float getFloatValue() {
        return Float.valueOf(value);
    }
    
    /**
     * Gets the long value.
     *
     * @return the long value
     */
    public long getLongValue() {
        return Long.valueOf(value);
    }
}


