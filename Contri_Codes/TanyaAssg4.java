package org.example;

import java.io.File; // Tanya Maurya 110155825 MAC Adv Computing Concepts
import java.io.FileReader;
import java.io.IOException; // Tanya Maurya 110155825 MAC Adv Computing Concepts
import java.io.BufferedReader;
import java.util.regex.Matcher; // Tanya Maurya 110155825 MAC Adv Computing Concepts
import java.util.regex.Pattern;

public class TanyaAssg4 {
    public static void main(String[] songs) {
        String fit_xt_location = "C:/Users/Umesh Maurya/Downloads/Advanced Computing Concepts - 792024 - 1206 AM/Lab 9 - Languages/output"; // Tanya Maurya 110155825 MAC Adv Computing Concepts
        String No_pat_tern = "\\d{3}-\\d{3}-\\d{4}|\\(\\d{3}\\) \\d{3}-\\d{4}|\\d{3}\\.\\d{3}\\.\\d{4}|\\d{3} \\d{3} \\d{4}";
        Fi_und_proc(fit_xt_location, No_pat_tern); // Tanya Maurya 110155825 MAC Adv Computing Concepts
    }

    private static void Fi_und_proc(String fold_loca, String No_pat_tern) {
        File l_o_c = new File(fold_loca); // Tanya Maurya 110155825 MAC Adv Computing Concepts
        File[] t_x_tFi = l_o_c.listFiles();

        if (t_x_tFi != null) {
            for (File fl : t_x_tFi) { // Tanya Maurya 110155825 MAC Adv Computing Concepts
                if (fl.isFile() && fl.getName().endsWith(".txt")) {
                    extractPatternsFromFile(fl, No_pat_tern);
                }
            }
        } else {
            System.out.println("No files found: " + fold_loca); // Tanya Maurya 110155825 MAC Adv Computing Concepts
        }
    }

    private static void extractPatternsFromFile(File file, String phnPattern) {
        try (BufferedReader bfr = new BufferedReader(new FileReader(file))) {
            String ln; // Tanya Maurya 110155825 MAC Adv Computing Concepts
            while ((ln = bfr.readLine()) != null) {
                matchPattern(ln, phnPattern, file.getName());
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage()); // Tanya Maurya 110155825 MAC Adv Computing Concepts
        }
    }

    private static void matchPattern(String ln, String pattern, String fileName) {
        Pattern p = Pattern.compile(pattern); // Tanya Maurya 110155825 MAC Adv Computing Concepts
        Matcher matcher = p.matcher(ln);
        while (matcher.find()) {
            System.out.println("Found phone number: " + matcher.group() + " at " + matcher.start() + " in file " + fileName); // Tanya Maurya 110155825 MAC Adv Computing Concepts
        }
    }
}
