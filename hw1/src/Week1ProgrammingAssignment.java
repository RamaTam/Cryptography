import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Week1ProgrammingAssignment {


    private static ArrayList<String> cipherTexts = new ArrayList<String>(Arrays.asList(
            "315c4eeaa8b5f8aaf9174145bf43e1784b8fa00dc71d885a804e5ee9fa40b16349c146fb778cdf2d3aff021dfff5b403b510d0d0455468aeb98622b137dae857553ccd8883a7bc37520e06e515d22c954eba5025b8cc57ee59418ce7dc6bc41556bdb36bbca3e8774301fbcaa3b83b220809560987815f65286764703de0f3d524400a19b159610b11ef3e",
            "234c02ecbbfbafa3ed18510abd11fa724fcda2018a1a8342cf064bbde548b12b07df44ba7191d9606ef4081ffde5ad46a5069d9f7f543bedb9c861bf29c7e205132eda9382b0bc2c5c4b45f919cf3a9f1cb74151f6d551f4480c82b2cb24cc5b028aa76eb7b4ab24171ab3cdadb8356f",
            "32510ba9a7b2bba9b8005d43a304b5714cc0bb0c8a34884dd91304b8ad40b62b07df44ba6e9d8a2368e51d04e0e7b207b70b9b8261112bacb6c866a232dfe257527dc29398f5f3251a0d47e503c66e935de81230b59b7afb5f41afa8d661cb",
            "32510ba9aab2a8a4fd06414fb517b5605cc0aa0dc91a8908c2064ba8ad5ea06a029056f47a8ad3306ef5021eafe1ac01a81197847a5c68a1b78769a37bc8f4575432c198ccb4ef63590256e305cd3a9544ee4160ead45aef520489e7da7d835402bca670bda8eb775200b8dabbba246b130f040d8ec6447e2c767f3d30ed81ea2e4c1404e1315a1010e7229be6636aaa",
            "3f561ba9adb4b6ebec54424ba317b564418fac0dd35f8c08d31a1fe9e24fe56808c213f17c81d9607cee021dafe1e001b21ade877a5e68bea88d61b93ac5ee0d562e8e9582f5ef375f0a4ae20ed86e935de81230b59b73fb4302cd95d770c65b40aaa065f2a5e33a5a0bb5dcaba43722130f042f8ec85b7c2070",
            "32510bfbacfbb9befd54415da243e1695ecabd58c519cd4bd2061bbde24eb76a19d84aba34d8de287be84d07e7e9a30ee714979c7e1123a8bd9822a33ecaf512472e8e8f8db3f9635c1949e640c621854eba0d79eccf52ff111284b4cc61d11902aebc66f2b2e436434eacc0aba938220b084800c2ca4e693522643573b2c4ce35050b0cf774201f0fe52ac9f26d71b6cf61a711cc229f77ace7aa88a2f19983122b11be87a59c355d25f8e4",
            "32510bfbacfbb9befd54415da243e1695ecabd58c519cd4bd90f1fa6ea5ba47b01c909ba7696cf606ef40c04afe1ac0aa8148dd066592ded9f8774b529c7ea125d298e8883f5e9305f4b44f915cb2bd05af51373fd9b4af511039fa2d96f83414aaaf261bda2e97b170fb5cce2a53e675c154c0d9681596934777e2275b381ce2e40582afe67650b13e72287ff2270abcf73bb028932836fbdecfecee0a3b894473c1bbeb6b4913a536ce4f9b13f1efff71ea313c8661dd9a4ce",
            "315c4eeaa8b5f8bffd11155ea506b56041c6a00c8a08854dd21a4bbde54ce56801d943ba708b8a3574f40c00fff9e00fa1439fd0654327a3bfc860b92f89ee04132ecb9298f5fd2d5e4b45e40ecc3b9d59e9417df7c95bba410e9aa2ca24c5474da2f276baa3ac325918b2daada43d6712150441c2e04f6565517f317da9d3",
            "271946f9bbb2aeadec111841a81abc300ecaa01bd8069d5cc91005e9fe4aad6e04d513e96d99de2569bc5e50eeeca709b50a8a987f4264edb6896fb537d0a716132ddc938fb0f836480e06ed0fcd6e9759f40462f9cf57f4564186a2c1778f1543efa270bda5e933421cbe88a4a52222190f471e9bd15f652b653b7071aec59a2705081ffe72651d08f822c9ed6d76e48b63ab15d0208573a7eef027",
            "466d06ece998b7a2fb1d464fed2ced7641ddaa3cc31c9941cf110abbf409ed39598005b3399ccfafb61d0315fca0a314be138a9f32503bedac8067f03adbf3575c3b8edc9ba7f537530541ab0f9f3cd04ff50d66f1d559ba520e89a2cb2a83",
            "32510ba9babebbbefd001547a810e67149caee11d945cd7fc81a05e9f85aac650e9052ba6a8cd8257bf14d13e6f0a803b54fde9e77472dbff89d71b57bddef121336cb85ccb8f3315f4b52e301d16e9f52f904"
    ));

    private static String result; //= "32510ba9babebbbefd001547a810e67149caee11d945cd7fc81a05e9f85aac650e9052ba6a8cd8257bf14d13e6f0a803b54fde9e77472dbff89d71b57bddef121336cb85ccb8f3315f4b52e301d16e9f52f904";
    private static char[] key;
    private static char[][] possibleLetters;
    private static char[][] resultLetters;
    private static final boolean DEBUG = false;

    public static void main(String[] args) {
        initData();
        calculateXorsGrid();
        printResultGrid(possibleLetters);

        for(int i=0;i<possibleLetters.length;i++)
            System.arraycopy(possibleLetters[i],0,resultLetters[i],0,resultLetters[0].length);
        printResultGrid(resultLetters);

        calculateMostPossibleValues();
        printResultGrid(resultLetters);
        manualDecryption();
    }

    private static void initData(){
        result = cipherTexts.get(cipherTexts.size()-1);
        possibleLetters = new char[cipherTexts.size()][result.length()/2];
        resultLetters = new char[cipherTexts.size()][result.length()/2];
        key = new char[result.length()/2];

        cutLongCipherTexts();
    }

    private static void cutLongCipherTexts() {
        for(int i=0;i<cipherTexts.size();i++){
            String currHexText = cipherTexts.get(i);
            if(currHexText.length() > result.length())
                    cipherTexts.set(i,currHexText.substring(0, result.length()));
        }

    }

    private static void calculateXorsGrid() {
        if(DEBUG)
            System.out.println(result);
        for(int i =0;i<possibleLetters.length;i++) {
            String currText = cipherTexts.get(i);
            String currTextXorResult = CryptoHelpers.xorHex(currText, result);
            String xorText = CryptoHelpers.fromHex(currTextXorResult);

            if(DEBUG)
                System.out.println(currText +"\n"+ xorText);

            for(int j =0;j<possibleLetters[0].length &&j<xorText.length();j++)
                possibleLetters[i][j] = xorText.charAt(j);
        }

    }

    private static final char UNKNOWN_CHAR ='+';
    private static void calculateMostPossibleValues() {
          for(int i =0;i<possibleLetters[0].length;i++) {
              Map< Character, Integer> valuesMap =  createValuesMap(i);
              char resultChar = detectSolution(valuesMap);

              if(resultChar != UNKNOWN_CHAR)
                  tryKeyAt(resultChar, resultLetters.length - 1, i);
          }
    }

    private static Map< Character, Integer> createValuesMap(int col){
        Map< Character, Integer> valuesMap = new HashMap< Character, Integer>();

        for(int j=0;j<possibleLetters.length;j++){
            char currCh = possibleLetters[j][col];
            if(Character.isUpperCase(currCh))
                if (!valuesMap.containsKey(currCh))
                    valuesMap.put(currCh, 1);
                else
                    valuesMap.put(currCh, valuesMap.get(currCh)+1);
        }
        return valuesMap;

    }

    private static void tryKeyAt(char ch, int row, int col) {
        String cipherText =  cipherTexts.get(row);
        printChangesInCell(-1, col);
        key[col] = CryptoHelpers.xorCharAt(cipherText, ch, col);
        printChangesInCell(-1, col);
        for(int i=0;i<possibleLetters.length;i++)  {

            printChangesInCell(i, col);
            resultLetters[i][col] = CryptoHelpers.xorCharAt(cipherTexts.get(i), key[col], col);
            printChangesInCell(i, col);
        }
    }
    //can Be Useful
    private static void resetKeyAt(int charAt){
        key[charAt] = '\u0000';
        for(int i=0;i<possibleLetters.length;i++)
            resultLetters[i][charAt] = possibleLetters[i][charAt];
    }

    private static final int LETTERS_NEEDED_TO_DETECT_SPACE = 6;
    private static char detectSolution(Map<Character, Integer> valuesMap) {
        char maxKey = ' ';
        int maxValue = -1;
        int valuesCounter =0;
        for (Map.Entry<Character, Integer> entry : valuesMap.entrySet())
        {
            if (entry.getValue() > maxValue) {
                maxKey = entry.getKey();
                maxValue = entry.getValue();
            }
            valuesCounter+=entry.getValue();
        }
        return solutionAlgorithm(maxKey, maxValue, valuesCounter);
    }

    private static char solutionAlgorithm(char maxKey, int maxValue, int valuesCounter){
        //Probably can play with this to make more efficient algorithm
        if(valuesCounter >= LETTERS_NEEDED_TO_DETECT_SPACE)
            return ' ';
        else if(maxValue >= 3)
            return Character.toLowerCase(maxKey);
        else if(valuesCounter == maxValue && Character.isUpperCase(maxKey))
            return Character.toLowerCase(maxKey);
        else return UNKNOWN_CHAR;

    }

    private static void manualDecryption() {
       drawGUI();
       //consoleInput(); //not so useful
    }

    private static void drawGUI(){
        JFrame frame = new JFrame("Week1ProgrammingAssignment ");

        frame.setLayout(new BorderLayout());
        JLabel label = new JLabel(
                "You are successfully running a Swing applet!");
        label.setHorizontalAlignment(JLabel.CENTER);

        JTable table = new JTable(new CharTableModel(resultLetters, key));

        table.setSurrendersFocusOnKeystroke(true);
        JScrollPane pane = new JScrollPane(table);
        pane.setPreferredSize(new Dimension(1300, 300));

        frame.add(pane, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public static void changeTable(char ch, int row, int col) {
        printResultString();
        tryKeyAt(ch, row, col);
    }



    private static final char USELES_XOR_CHAR = ' ';
    private static void printResultGrid(char[][] grid) {
        if(DEBUG){
        String res = "Results: ";
        for (char[] text : grid) {
            res += "\n";
            for (char ch : text)
                if(Character.isLetterOrDigit(ch))
                    res += ch  + "|";
                else
                    res += USELES_XOR_CHAR + "|";
        }
        System.out.println(res);
        }
    }

    private static void printChangesInCell(int row, int col) {
        if(DEBUG){
            char resultChar;
            if(row !=-1) resultChar = resultLetters[row][col] ;
            else resultChar = key[col];

            System.out.print("["+resultChar+"]" +" Char: "+(int)resultChar+"row: "+ row +" col: "+ col +"\n");
        }
    }

    private static void printResultString() {
        String result = "";
        for (char ch : resultLetters[resultLetters.length - 1])
            result += ch;

        System.out.println(result);
    }
    /*
    private static void consoleInput(){
        while (true) {

            Scanner scanner = new Scanner(System.in);

            int Row = askForInt(scanner, "Print TextNum: [0-"+(resultLetters[0].length-1)+"]: ");
            if (Row == -1)  break;
            char  Value = askForChar(scanner, "Print Value [a-zA-Z ]: ");
            int TextNum = askForInt (scanner, "Print Row [0-10]:");
            char  Action = askForChar(scanner, "Print Action [ar]: ");
            if(Action =='a')
                tryKeyAt(Value, TextNum, Row);
            else
                resetKeyAt(Row);
            printResultGrid(resultLetters);
            System.out.print(Row + " " + Value + " " + TextNum + "\n");
        }
    }

    private static int askForInt(Scanner scanner, String msg){
        while (true) {
            System.out.println(msg);
            if (scanner.hasNextInt())
                return scanner.nextInt();
        }
    }
    private static char askForChar(Scanner scanner, String msg){
        while (true)          {
            System.out.println(msg);
            if (scanner.hasNext("[a-zA-Z ]"))
                return scanner.next("[a-zA-Z]").charAt(0);
        }
    }
    */
}