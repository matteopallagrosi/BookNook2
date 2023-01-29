package it.ispw.booknook.logic.boundary.secondary_view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class InOut {

    private InOut() {}

    //num indica il numero di opzioni
    public static char multiChoice(char []choices, int num) {
        //genera la stringa delle possibilità
        char[] possib = new char[(choices.length * 2)];
        int j = 0;
        for(int i = 0; i < num; i++) {
            possib[j] = choices[i];
            possib[j+1] = '/';
            j = j + 2;
        }

        String possibilities = String.valueOf(possib);

        //elimino l'ultimo "/"
        StringBuilder poss = new StringBuilder(possibilities);
        poss.deleteCharAt(j-1);

        int c = 0;
        //continua a chiedere finchè non riceve un input corretto
        try {
            InputStreamReader in = new InputStreamReader(System.in);
            while (true) {
                //mostra la domanda con le possibilità
                if ((char)c != '\n')
                    System.out.print("Select an option [" + poss + "]: ");

                c = in.read();

                for (int i = 0; i < num; i++) {
                    if ((char)c == choices[i])
                        return (char)c;
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return (char)c;
    }

    public static int multiIntChoice(int num) {
        int c = 0;
        Scanner in = new Scanner( System.in );
        while (true) {
            //mostra la domanda con le possibilità
            System.out.print("Select an option: ");

            c = in.nextInt();

            //se l'input è corretto lo ritorna
            if (c>0 && c<=num)
                return c;
        }
    }

    public static void printLine(String stringToPrint) {
        System.out.println(stringToPrint);
    }

    public static void print(String stringToPrint) {
        System.out.print(stringToPrint);
    }

    public static String readLine() {
        String input = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            input = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }

    //verifica se data in input da utente è nel corretto formato
    public static boolean isValidFormat(String format, String value, Locale locale) {
        LocalDateTime ldt = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format, locale);

        try {
            ldt = LocalDateTime.parse(value, formatter);
            String result = ldt.format(formatter);
            return result.equals(value);
        } catch (DateTimeParseException e) {
            try {
                LocalDate ld = LocalDate.parse(value, formatter);
                String result = ld.format(formatter);
                return result.equals(value);
            } catch (DateTimeParseException exp) {
                try {
                    LocalTime lt = LocalTime.parse(value, formatter);
                    String result = lt.format(formatter);
                    return result.equals(value);
                } catch (DateTimeParseException e2) {
                    // Debugging purposes
                    //e2.printStackTrace();
                }
            }
        }

        return false;
    }

    public static String[] splitStringFixedLen(String data, int interval) {
        List<String> dataPiece = new ArrayList<String>();

        int addedOffset;
        for (int offset = 0; offset < data.length(); offset += addedOffset) {
            String subData = data.substring(offset,
                    Math.min(data.length(), (offset + interval)));
            addedOffset = subData.lastIndexOf('\n');
            if (addedOffset >= 0) {
                subData = subData.substring(0, addedOffset);
                ++addedOffset;
            } else {
                addedOffset = interval;
            }
            dataPiece.add(subData);
        }

        String[] result = new String[dataPiece.size()];
        dataPiece.toArray(result);
        return result;
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }
}
