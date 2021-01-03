import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Main {
    static int signal = 0;
    static int count = 0, countLine = 0;
    static String token;
    static String line;
    static String str = "";
    static String[] strings;
    static ArrayList<String> reservedWords = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        File file = new File("src/sample.txt");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        while (bufferedReader.ready()) {
            str = str + bufferedReader.readLine() + "\n";
        }
        fileReader.close();
        bufferedReader.close();
        File file1 = new File("src/reservedWords.txt");
        fileReader = new FileReader(file1);
        bufferedReader = new BufferedReader(fileReader);
        while (bufferedReader.ready()) {
            String line = bufferedReader.readLine().trim();
            reservedWords.add(line);
        }
        System.out.println(reservedWords);
        strings = str.split("\n");
        line = getLine();
        token = getToken();
        System.out.println(line);
        body();
        if (token != "$")
            System.out.println("err missing #");
        else
            System.out.println("success");
    }

    static void body() {
        lib_decl();
        if (line.equals("main()")) {
            line = getLine();
            declarations();
            block();
        }

    }

    static void lib_decl() {
        while (token.contains("#")) {
            token = getToken();
            if (token.equals("include")) {
                token = getToken();
                if (token.equals("<")) {
                    token = getToken();
                    name();
                }
                if (token.equals(">")) {
                    token = getToken();
                    if (token.equals(";")) {
                        line = getLine();
                        token = getToken();
                    }
                }
            }
        }
    }

    static void declarations() {
        token = getToken();
        const_decl();
        var_decl();


    }

    static void const_decl() {
        while (token.equals("const")) {
            token = getToken();
            data_type();
            name();
            if (token.equals("=")) {
                token = getToken();
                value();
                token = getToken();
                if (token.equals(";")) {
                    line = getLine();
                    token = getToken();
                }
            }
        }


    }

    static void var_decl() {
        while (token.equals("var")) {
            token = getToken();

            data_type();
            if (signal == 1) {
                return;
            }
            if (line.contains(","))
                name_list();
            else {
                name();
            }
            line = getLine();
            token = getToken();

        }

    }

    static void name_list() {

        name();
        while (token.equals(",")) {
            token = getToken();
            name();

        }

    }


    static void data_type() {
        if (token.equals("float") || token.equals("int")) {
            token = getToken();

        } else {
            signal = 1;
            System.out.println("not valid data type");
            return;
        }
    }

    static void name() {
        if (!reservedWords.contains(token)) {
            token = getToken();
            return;
        } else {
            System.out.println("err, use reserved word as a reference");
        }
    }

    static void block() {
        if (token.equals("{")) {
            stmt_list();
            if (!token.equals("}")) {
                System.out.println("err missing } at " + (--countLine));
            }
        } else {
            System.out.println("missing { at line " + countLine);
        }


    }

    static void stmt_list() {
        token = getToken();
        statement();
        while (token.equals(";")) {
            statement();
        }
    }

    static void statement() {
        if (token.equals("input") || token.equals("output")) {
            inout_stmt();
        }
        if (token.equals("if")) {
            if_stmt();
        }
        if (line.contains("=")) {
            ass_stmt();
        }

    }

    static void ass_stmt() {
        name();
        if (token.equals("=")) {
            exp();
        }
    }

    static void exp() {
        term();
        while (token.equals("+")) {
            term();
        }

    }

    static void term() {
        factor();
        while (token.equals("*")){
            factor();
        }
    }

    static void factor() {
        if (token.equals("(")) {
            exp();
            if (token.equals(")")) {
                return;
            }

        }


    }

    static void value() {
        try {
            double d = Double.parseDouble(token);
        } catch (NumberFormatException nfe) {
            Integer integer = Integer.parseInt(token);
        }

    }

    static void add_sign() {

    }

    static void mul_sign() {

    }

    static void inout_stmt() {
        if (token.equals("input") || token.equals("output")) {
            token = getToken();
            token = token + getToken();
            if (token.equals(">>")) {
                token = getToken();
                name();
                if (token.equals(";")) {
                    line = getLine();
                    token = getToken();
                }
            }
        }
    }

    static void if_stmt() {
        token = getToken();
        if (token.equals("(")) {
            bool_exp();
            if (token.equals(")")) {
                line = getLine();
                token = getToken();
                statement();
                if (token.equals("else")) {
                    else_part();
                }
            }
            if (token.equals("endif")) {
                line = getLine();
                token = getToken();
            } else {
                System.out.println("err missed endif at line " + countLine);
            }
        }

    }

    static void else_part() {

    }

    static void while_stmt() {

    }

    static void bool_exp() {
        token = getToken();
        name_value();
        String temp = token;
        String nextToken = getToken();
        if (nextToken.equals("=")) {
            token = temp + nextToken;
        } else {
            --count;
        }
        relational_oper();
        name_value();
        token = getToken();
    }

    static void name_value() {
        try {
            value();
        } catch (Exception e) {
            name();
        }
    }

    static void relational_oper() {
        if (token.equals("==") || token.equals("<") || token.equals(">") || token.equals("<=") || token.equals("!=")) {
            token = getToken();
            return;
        }
    }


    private static String getLine() {
        count = 0;
        String str = strings[countLine];
        countLine++;
        return str;
    }

//    private static String getToken() {
//        String str = "";
//        int i;
//        for (i = count; i < line.length(); i++) {
//            if (line.charAt(i) == ' ') {
//                count++;
//                return str.trim();
//            }
//
//            str = str + line.charAt(i);
//            count = i;
//            if (reservedWords.contains(str)) {
//                break;
//            }
//
//        }
//        count = ++i;
//        if (count < line.length() - 1) {
//            if (line.charAt(count) == ' ') {
//                count++;
//            }
//        }
//        return str;
//
//    }

    private static String getToken() {
        String str = "";
        int i;
        for (i = count; i < line.length(); i++) {
            if (line.charAt(i) == ' ') {
                count = ++i;
                return str.trim();
            }
            if (reservedWords.contains(str)) {
                ++count;
                break;
            } else {
                str = str + line.charAt(i);
                count = i;
                if (i < line.length() - 1 && reservedWords.contains((line.charAt(i + 1)) + "")) {

                    ++count;
                    break;

                }

            }

            if (count > 0 && reservedWords.contains(line.charAt(i) + "")) {
                str = line.charAt(i) + "";
                count = ++i;
                break;
            }


        }

        return str;

    }
}


