package fi.helsinki.cs.okkopa.reference;

import java.util.ArrayList;

public class Warning {

    public static ArrayList<String> warnings = new ArrayList<String>();

    public static String getWarning() {
        String temp = "";

        boolean first = true;
        for (String string : warnings) {
            if (first) {
                first = false;
            } else {
                temp += "<br/>\n";
            }
            temp += " - " + string;
        }
        return temp;
    }
    
    public static void clearWarnings() {
        warnings = new ArrayList<String>();
    }

    public static void setWarning(String warning) {
        warnings.add(warning);
    }
}
