import java.math.BigInteger;

public class CryptoHelpers {

    public static String xorHex(String s1, String s2) {
        BigInteger i1 = new BigInteger(s1, 16);
        BigInteger i2 = new BigInteger(s2, 16);
        BigInteger res = i1.xor(i2);
        String s3 = res.toString(16);
        return validHex(s3);
    }

    public static String toHex(String txt) {
        byte[] bytes = txt.getBytes();
        BigInteger bigInt = new BigInteger(bytes);
        String hexString = bigInt.toString(16);
        return validHex(hexString);
    }

    public static String fromHex(String s) {

        int len = s.length();
        byte[] data = new byte[len/2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return new String(data);
    }

    private static String validHex(String inputHex){
        if(inputHex.length()%2>0)
            inputHex = "0"+inputHex;
        return  inputHex;
    }

    public static char xorCharAt(String hexStr, char ch, int realIndex /* not hex index*/){
        String xorWith = "0x"+hexStr.substring(realIndex*2,realIndex*2 + 2);

        char hexInt = (char)Integer.decode(xorWith).intValue();
        return (char)(hexInt ^ch);

    }

}
