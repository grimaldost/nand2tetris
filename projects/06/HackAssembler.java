import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class HackAssembler {

    static public List<String> assemblyToHack(List<String> code){
        List<String> hackCode = new Vector<String>();

        for (String line : code) {
            // A instruction
            if(line.substring(0,1).equals("@")) {
                hackCode.add(String.format("%16s", Integer.toBinaryString(Integer.parseInt(line.substring(1,line.length()))))
                        .replace(' ', '0'));
            }
            // C instruction
            else {
                String comp = "";
                String dest = "";
                String jump = "";
                String hackInstruction = "111";

                // Split instruction in each field
                if(line.indexOf("=") < 0 && line.indexOf(";") < 0)
                    comp = line;
                else if(line.indexOf("=") < 0) {
                    comp = line.substring(0, line.indexOf(";"));
                    jump = line.substring(line.indexOf(";")+1, line.length());
                }
                else if(line.indexOf(";") < 0) {
                    dest = line.substring(0, line.indexOf("="));
                    comp = line.substring(line.indexOf("=")+1, line.length());
                }
                else {
                    dest = line.substring(0, line.indexOf("="));
                    comp = line.substring(line.indexOf("=")+1, line.indexOf(";"));
                    jump = line.substring(line.indexOf(";")+1, line.length());
                }

                // Comp field
                if(comp.indexOf('M') < 0) {
                    hackInstruction += "0";
                    switch (comp) {
                        case "0":
                            hackInstruction += "101010";
                            break;
                        case "1":
                            hackInstruction += "111111";
                            break;
                        case "-1":
                            hackInstruction += "111010";
                            break;
                        case "D":
                            hackInstruction += "001100";
                            break;
                        case "A":
                            hackInstruction += "110000";
                            break;
                        case "!D":
                            hackInstruction += "001101";
                            break;
                        case "!A":
                            hackInstruction += "110001";
                            break;
                        case "-D":
                            hackInstruction += "001111";
                            break;
                        case "-A":
                            hackInstruction += "110011";
                            break;
                        case "D+1":
                            hackInstruction += "011111";
                            break;
                        case "A+1":
                            hackInstruction += "110111";
                            break;
                        case "D-1":
                            hackInstruction += "001110";
                            break;
                        case "A-1":
                            hackInstruction += "110010";
                            break;
                        case "D+A":
                            hackInstruction += "000010";
                            break;
                        case "D-A":
                            hackInstruction += "010011";
                            break;
                        case "A-D":
                            hackInstruction += "000111";
                            break;
                        case "D&A":
                            hackInstruction += "000000";
                            break;
                        case "D|A":
                            hackInstruction += "010101";
                            break;
                        default:
                            hackInstruction += "000000";
                            break;
                    }
                }
                else {
                    hackInstruction += "1";
                    switch (comp) {
                        case "M":
                            hackInstruction += "110000";
                            break;
                        case "!M":
                            hackInstruction += "110001";
                            break;
                        case "-M":
                            hackInstruction += "110011";
                            break;
                        case "M+1":
                            hackInstruction += "110111";
                            break;
                        case "M-1":
                            hackInstruction += "110010";
                            break;
                        case "D+M":
                            hackInstruction += "000010";
                            break;
                        case "D-M":
                            hackInstruction += "010011";
                            break;
                        case "M-D":
                            hackInstruction += "000111";
                            break;
                        case "D&M":
                            hackInstruction += "000000";
                            break;
                        case "D|M":
                            hackInstruction += "010101";
                            break;
                        default:
                            hackInstruction += "000000";
                            break;
                    }
                }

                // Dest field
                if(dest.indexOf('A') < 0)
                    hackInstruction += "0";
                else
                    hackInstruction += "1";
                if(dest.indexOf('D') < 0)
                    hackInstruction += "0";
                else
                    hackInstruction += "1";
                if(dest.indexOf('M') < 0)
                    hackInstruction += "0";
                else
                    hackInstruction += "1";

                // Jump field
                switch (jump){
                    case "JGT":
                        hackInstruction += "001";
                        break;
                    case "JEQ":
                        hackInstruction += "010";
                        break;
                    case "JGE":
                        hackInstruction += "011";
                        break;
                    case "JLT":
                        hackInstruction += "100";
                        break;
                    case "JNE":
                        hackInstruction += "101";
                        break;
                    case "JLE":
                        hackInstruction += "110";
                        break;
                    case "JMP":
                        hackInstruction += "111";
                        break;
                    default:
                        hackInstruction += "000";
                        break;
                }
                hackCode.add(hackInstruction);
            }
        }
        return hackCode;
    }


    public static void main(String[] args) throws FileNotFoundException {
        // Get code from file and put it in a string
        String fileName = args[0];
        File file = new File(fileName);
        Scanner sc = new Scanner(file);
        sc.useDelimiter("\\Z");
        String[] code = sc.next().split("\\r?\\n");

        // Parse code string to replace symbols
        SymbolTable st = new SymbolTable();
        List<String> parsedCode = st.parseCode(Arrays.asList(code));

        // Translate assembly to binary code
        List<String> hackCode = assemblyToHack(parsedCode);

        // Write hack file
        String hackFileName = fileName.replace(".asm", ".hack");
        PrintWriter writer = new PrintWriter(hackFileName);

        for (String line : hackCode)
            writer.println(line);

        writer.close();
    }
}
