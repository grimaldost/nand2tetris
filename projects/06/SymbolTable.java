import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class SymbolTable {
    private Hashtable<String, Integer> symbolTable;
    private int lineCount;
    private int varSymboladd;

    private static boolean isNumeric(String strNum) {
        return strNum.matches("-?\\d+(\\.\\d+)?");
    }

    public SymbolTable() {
        // Construct a initial symbol table for predefined symbols
        symbolTable = new Hashtable<String, Integer>();
        symbolTable.put("SP", 0);
        symbolTable.put("LCL", 1);
        symbolTable.put("ARG", 2);
        symbolTable.put("THIS", 3);
        symbolTable.put("THAT", 4);
        symbolTable.put("R0", 0);
        symbolTable.put("R1", 1);
        symbolTable.put("R2", 2);
        symbolTable.put("R3", 3);
        symbolTable.put("R4", 4);
        symbolTable.put("R5", 5);
        symbolTable.put("R6", 6);
        symbolTable.put("R7", 7);
        symbolTable.put("R8", 8);
        symbolTable.put("R9", 9);
        symbolTable.put("R10", 10);
        symbolTable.put("R11", 11);
        symbolTable.put("R12", 12);
        symbolTable.put("R13", 13);
        symbolTable.put("R14", 14);
        symbolTable.put("R15", 15);
        symbolTable.put("SCREEN", 16384);
        symbolTable.put("KBD", 24576);

        lineCount = 0;
        varSymboladd = 16; // variable symbols are mapped starting at RAM address 16 (0x0010)
    }

    public List<String> parseCode(List<String> code) {
        List<String> parsedCode = new Vector<String>();

        // First iteration: build symbol table
        for (String line : code) {
            // Remove whitespaces and comments
            line = line.replaceAll("\\s+","");
            if(line.indexOf("//") >= 0)
                line = line.substring(0, line.indexOf("//"));

            if(line.length() == 0 ) continue; // ignore conditions

            if(line.substring(0,1).equals("("))
                symbolTable.put(line.substring(1, line.length() - 1), lineCount);
            else
                lineCount++;
        }

        // Second iteration: replace symbols and build parsedCode
        for (String line : code) {
            // Remove whitespaces and comments
            line = line.replaceAll("\\s+","");
            if(line.indexOf("//") >= 0)
                line = line.substring(0, line.indexOf("//"));

            if(line .length() == 0 || line.substring(0,2).equals("//") ) continue; // ignore conditions
            if(line.substring(0,1).equals("(")) continue; // also ignore pseudocode

            if(line.substring(0,1).equals("@")) {
                if(isNumeric(line.substring(1, line.length())))
                    parsedCode.add(line);
                else {
                    if(!symbolTable.containsKey(line.substring(1, line.length()))) {
                        symbolTable.put(line.substring(1, line.length()), varSymboladd++);
                    }
                    String tmpstr = "@" + symbolTable.get(line.substring(1, line.length())).toString();
                    parsedCode.add(tmpstr);
                }
            }
            else {
                parsedCode.add(line);
            }

        }

        return parsedCode;
    }

}
