package top.rymc.phira.protocol.util;

public final class Float16Util {

    private Float16Util() {}

    /**
     * The offset to shift by to obtain the sign bit.
     */
    public static final int SIGN_SHIFT                = 15;
    /**
     * The offset to shift by to obtain the exponent bits.
     */
    public static final int EXPONENT_SHIFT            = 10;
    /**
     * The bitmask to AND a number with to obtain the sign bit.
     */
    public static final int SIGN_MASK                 = 0x8000;
    /**
     * The bitmask to AND a number shifted by {@link #EXPONENT_SHIFT} right, to obtain exponent bits.
     */
    public static final int SHIFTED_EXPONENT_MASK     = 0x1f;
    /**
     * The bitmask to AND a number with to obtain significand bits.
     */
    public static final int SIGNIFICAND_MASK          = 0x3ff;
    /**
     * The offset of the exponent from the actual value.
     */
    public static final int EXPONENT_BIAS             = 15;
    private static final int FP32_SIGN_SHIFT            = 31;
    private static final int FP32_EXPONENT_SHIFT        = 23;
    private static final int FP32_SHIFTED_EXPONENT_MASK = 0xff;
    private static final int FP32_SIGNIFICAND_MASK      = 0x7fffff;
    private static final int FP32_EXPONENT_BIAS         = 127;
    private static final int FP32_QNAN_MASK             = 0x400000;
    private static final int FP32_DENORMAL_MAGIC = 126 << 23;
    private static final float FP32_DENORMAL_FLOAT = Float.intBitsToFloat(FP32_DENORMAL_MAGIC);

    public static float halfToFloat(short h) {
        // https://github.com/caoccao/Javet/blob/3bfd821/src/main/java/com/caoccao/javet/utils/Float16.java#L518-L545
        // Apache License 2.0 | Copyright (C) 2019 The Android Open Source Project

        int bits = h & 0xffff;
        int s = bits & SIGN_MASK;
        int e = (bits >>> EXPONENT_SHIFT) & SHIFTED_EXPONENT_MASK;
        int m = (bits) & SIGNIFICAND_MASK;
        int outE = 0;
        int outM = 0;
        if (e == 0) { // Denormal or 0
            if (m != 0) {
                // Convert denorm fp16 into normalized fp32
                float o = Float.intBitsToFloat(FP32_DENORMAL_MAGIC + m);
                o -= FP32_DENORMAL_FLOAT;
                return s == 0 ? o : -o;
            }
        } else {
            outM = m << 13;
            if (e == 0x1f) { // Infinite or NaN
                outE = 0xff;
                if (outM != 0) { // SNaNs are quieted
                    outM |= FP32_QNAN_MASK;
                }
            } else {
                outE = e - EXPONENT_BIAS + FP32_EXPONENT_BIAS;
            }
        }
        int out = (s << 16) | (outE << FP32_EXPONENT_SHIFT) | outM;
        return Float.intBitsToFloat(out);
    }

    public static short floatToHalf(float f) {
        // https://github.com/caoccao/Javet/blob/3bfd821/src/main/java/com/caoccao/javet/utils/Float16.java#L568-L616
        // Apache License 2.0 | Copyright (C) 2019 The Android Open Source Project

        int bits = Float.floatToRawIntBits(f);
        int s = (bits >>> FP32_SIGN_SHIFT);
        int e = (bits >>> FP32_EXPONENT_SHIFT) & FP32_SHIFTED_EXPONENT_MASK;
        int m = (bits) & FP32_SIGNIFICAND_MASK;
        int outE = 0;
        int outM = 0;
        if (e == 0xff) { // Infinite or NaN
            outE = 0x1f;
            outM = m != 0 ? 0x200 : 0;
        } else {
            e = e - FP32_EXPONENT_BIAS + EXPONENT_BIAS;
            if (e >= 0x1f) { // Overflow
                outE = 0x1f;
            } else if (e <= 0) { // Underflow
                if (e < -10) {
                    // The absolute fp32 value is less than MIN_VALUE, flush to +/-0
                } else {
                    // The fp32 value is a normalized float less than MIN_NORMAL,
                    // we convert to a denorm fp16
                    m = m | 0x800000;
                    int shift = 14 - e;
                    outM = m >> shift;
                    int lowm = m & ((1 << shift) - 1);
                    int hway = 1 << (shift - 1);
                    // if above halfway or exactly halfway and outM is odd
                    if (lowm + (outM & 1) > hway) {
                        // Round to nearest even
                        // Can overflow into exponent bit, which surprisingly is OK.
                        // This increment relies on the +outM in the return statement below
                        outM++;
                    }
                }
            } else {
                outE = e;
                outM = m >> 13;
                // if above halfway or exactly halfway and outM is odd
                if ((m & 0x1fff) + (outM & 0x1) > 0x1000) {
                    // Round to nearest even
                    // Can overflow into exponent bit, which surprisingly is OK.
                    // This increment relies on the +outM in the return statement below
                    outM++;
                }
            }
        }
        // The outM is added here as the +1 increments for outM above can
        // cause an overflow in the exponent bit which is OK.
        return (short) ((s << SIGN_SHIFT) | (outE << EXPONENT_SHIFT) + outM);
    }
}
