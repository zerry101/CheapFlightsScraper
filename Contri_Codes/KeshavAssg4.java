package htmlparser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeshavAssg4 {
    public static void main(String[] sonsgs) {
        String location = "D:/Study Material/UWin/ACC_COMP8547/Lab 9 - Languages/ouutpuut"; // Used the Lab 9 text
                                                                                            // files for searching
                                                                                            // for URLs for this
                                                                                            // assignment.
        String U_R_L_reg_ex = "(https?://[\\w.-]+(/[\\w.-]*)*)"; // regular expression utilized to find URLs
        File location_2 = new File(location);
        File[] Sample_t_x_t_Fi = location_2.listFiles();

        if (Sample_t_x_t_Fi != null) {
            for (File f_i : Sample_t_x_t_Fi) {
                if (f_i.isFile() && f_i.getName().endsWith(".txt")) { // fetching txt files from the folder
                    Fi_in_proc(f_i, U_R_L_reg_ex);
                }
            }
        } else {
            System.out.println("Txt file not found at: " + location);
        }
    }

    private static void Fi_in_proc(File b, String u_r_l_Reg_ex) { // function to start reading the file & initiating
                                                                  // finding URLs
        try (BufferedReader bro = new BufferedReader(new FileReader(b))) {
            String sen_tence;
            Pattern norm = Pattern.compile(u_r_l_Reg_ex);
            while ((sen_tence = bro.readLine()) != null) {
                U_R_L_finder(sen_tence, norm, b.getName());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void U_R_L_finder(String sen_tence, Pattern no_rm, String ttxt_naame) { // function to find URLs from
                                                                                           // the text received
        Matcher sima_aunty = no_rm.matcher(sen_tence);
        while (sima_aunty.find()) {
            String ans = sima_aunty.group();
            System.out.println("Found URL: " + ans + " at position: " + sima_aunty.start() + " in file: " + ttxt_naame);
        }
    }
}
