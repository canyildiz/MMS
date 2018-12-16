package mms.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Scanner;

public class Console {

    public static Scanner scanner = null;

    public Console() {
        scanner = new Scanner(System.in);
    }

    public void println(String text) {
        System.out.println(text);
    }

    public void print(String text) {
        System.out.print(text);
    }

    public int waitInt(String text, int[] validEntries, String invalidMsg) {
        if (invalidMsg.isEmpty()) {
            invalidMsg = "\n!Invalid entry, try again";
        }

        System.out.print(text);
        while (scanner.hasNext()) {
            if (scanner.hasNextInt()) {
                int ret = scanner.nextInt();
                for (int i = 0; i < validEntries.length; i++) {
                    if (validEntries[i] == ret) {
                        return ret;
                    }
                }
                System.out.println(invalidMsg);
            } else {
                System.out.println(invalidMsg);
            }
        }
        return -1;
    }

    public int waitInt(String text, String invalidMsg) {
        if (invalidMsg.isEmpty()) {
            invalidMsg = "\n!Invalid entry, try again";
        }

        System.out.print(text);
        while (scanner.hasNext()) {
            if (scanner.hasNextInt()) {
                int ret = scanner.nextInt();
                return ret;
            } else {
                System.out.println(invalidMsg);
            }
        }
        return -1;
    }

    public String waitString(String text, String invalidMsg) {
        if (invalidMsg.isEmpty()) {
            invalidMsg = "\n!Invalid entry, try again";
        }
        System.out.print(text);
        while (scanner.hasNext()) {
            String ret = scanner.next();
            if (ret.isEmpty()) {
                System.out.println(invalidMsg);
            } else {
                return ret;
            }
        }
        return "";
    }

    public String waitStringOrEmpty(String text, String emptyInput, String currentValue) {
        System.out.print(text);

        if (currentValue != null) {
            System.out.println("Current Value: " + currentValue);
        }

        String ret = scanner.next();
        if (ret.equals(emptyInput)) {
            return currentValue;
        }
        return ret;
    }

    public Date waitDate(String text, String format, String invalidMsg, String emptyInput, Date currentValue) {
        if (invalidMsg.isEmpty()) {
            invalidMsg = "\n!Invalid date entry, try again in " + format + " format";
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        System.out.print(text);
        if (currentValue != null) {
            System.out.println("Current Value: " + currentValue);
        }
        while (scanner.hasNext()) {
            String ret = scanner.next();

            if (ret.equals(emptyInput)) {
                return currentValue;
            }

            try {
                Date d = dateFormat.parse(ret);
                return d;
            } catch (ParseException e) {
                System.out.println(invalidMsg);
            }
        }
        return Date.from(Instant.MIN);
    }
}
