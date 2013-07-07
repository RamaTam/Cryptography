public class Week1ProblemSet1 {
    private static final String text1 = "attack at dawn";
    private static final String text2 = "attack at dusk";
    private static final String cipherText = "6c73d5240a948c86981bc294814d";

    public static void main(String[] args) {
        String hex1   = CryptoHelpers.toHex(text1);
        String hex2   = CryptoHelpers.toHex(text2);
        String key    = CryptoHelpers.xorHex(cipherText, hex1);
        String result = CryptoHelpers.xorHex(hex2, key);


        System.out.println("HEX for \""+text1+"\"        : " + hex1);
        System.out.println("HEX for \""+text2+"\"        : " + hex2);
        System.out.println("Key                             : " + key);
        System.out.println("Result hex for \""+text1+"\" : " + result +" <= This is result.");
        System.out.println("Result hex for \""+text2+"\" : " + cipherText);
    }

    /**
     * HEX for "attack at dawn"        : 61747461636b206174206461776e
     * HEX for "attack at dusk"        : 61747461636b206174206475736b
     * Key                             : d07a14569fface7ec3ba6f5f623
     * Result hex for "attack at dawn" : 6c73d5240a948c86981bc2808548
     * Result hex for "attack at dusk" : 6c73d5240a948c86981bc294814d
     */

}
