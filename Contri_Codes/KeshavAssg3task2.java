import java.io.*;
import java.util.*;

// Class representing a Trie nod
class Trrie_Nod {
    Map<Character, Trrie_Nod> Cchldrn = new HashMap<>();
    boolean end_Of_Wrd;

    Trrie_Nod() {
        this.end_Of_Wrd = false;
    }
}

// Class for Trie data structure
class tTTries {
    private Trrie_Nod begin_root;

    tTTries() {
        begin_root = new Trrie_Nod();
    }

    // Insert a word into the Trie
    void tr_insrt(String wwrrd) {
        Trrie_Nod currnt = begin_root;
        for (char v : wwrrd.toCharArray()) {
            currnt = currnt.Cchldrn.computeIfAbsent(v, k -> new Trrie_Nod());
        }
        currnt.end_Of_Wrd = true;
    }

    // Find all words in the Trie that start with the given prfx
    List<String> lookfor_WrdsWithPrfx(String prfx) {
        List<String> final_ans = new ArrayList<>();
        Trrie_Nod currnt = begin_root;
        for (char x : prfx.toCharArray()) {
            if (!currnt.Cchldrn.containsKey(x)) {
                return final_ans;
            }
            currnt = currnt.Cchldrn.get(x);
        }
        look_for_AllWrdsFromNod(currnt, prfx, final_ans);
        return final_ans;
    }

    // Helper function to find all words from a given Trie nod
    private void look_for_AllWrdsFromNod(Trrie_Nod noodde, String pa_prfx, List<String> final_ans) {
        if (noodde.end_Of_Wrd) {
            final_ans.add(pa_prfx);
        }
        for (Map.Entry<Character, Trrie_Nod> inoput : noodde.Cchldrn.entrySet()) {
            look_for_AllWrdsFromNod(inoput.getValue(), pa_prfx + inoput.getKey(), final_ans);
        }
    }
}

public class KeshavAssg3task2 {
    // Method to read words from CSV files and combine them into one list
    public static List<String> get_WrdsFromCSVFiles(String[] F_fiilePths) throws IOException {
        List<String> wrds = new ArrayList<>();
        for (String F_P : F_fiilePths) {
            try (BufferedReader bore = new BufferedReader(new FileReader(F_P))) {
                String lleen;
                while ((lleen = bore.readLine()) != null) {
                    String[] vals = lleen.split(",");
                    for (String z : vals) {
                        String[] individual_Wrds = z.split("\\s+");
                        for (String w : individual_Wrds) {
                            wrds.add(w.toLowerCase().trim());
                        }
                    }
                }
            }
        }
        return wrds;
    }

    public static void main(String[] song) {
        try {
            // Paths to the CSV files
            String[] C_csv_FilePths = {
                    "D:\\Study Material\\UWin\\ACC_COMP8547\\Assg_3\\AirCanada_Data.csv",
                    "D:\\Study Material\\UWin\\ACC_COMP8547\\Assg_3\\Expedia.csv",
                    "D:\\Study Material\\UWin\\ACC_COMP8547\\Assg_3\\flight_data.csv",
                    "D:\\Study Material\\UWin\\ACC_COMP8547\\Assg_3\\flightdata.csv",
                    "D:\\Study Material\\UWin\\ACC_COMP8547\\Assg_3\\Kayak_Data_Asg.csv",
                    "D:\\Study Material\\UWin\\ACC_COMP8547\\Assg_3\\travelocity.csv"
            };

            // Read words from CSV files
            List<String> wrds = get_WrdsFromCSVFiles(C_csv_FilePths);

            // Insert words into the Trie
            tTTries tries = new tTTries();
            for (String wrd : wrds) {
                tries.tr_insrt(wrd);
            }

            // Continuously prompt for prefixes and search
            Scanner scnner = new Scanner(System.in);
            while (true) {
                System.out.print("Prfx of wrd (or type 'exit' to quit): ");
                String prfx = scnner.nextLine().toLowerCase().trim();

                if (prfx.equals("exit")) {
                    break;
                }

                List<String> reslts = tries.lookfor_WrdsWithPrfx(prfx);
                System.out.println("Wrds with '" + prfx + "' are: " + reslts);
            }

        } catch (IOException e) {
            System.err.println("Error reading CSV files: " + e.getMessage());
        }
    }
}
