package Test1;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpellChecker {
    public static ArrayList<String> findSimilarWords(String indexTerm, String fileName, int wordCount) {
//        System.out.println("File " + fileName);
//        System.out.println("Search key: " + indexTerm);

        ArrayList<String> similarWordsArray=new ArrayList<>();

        String[] wordDictionary = new String[1000000];
        int[] distArr = new int[1000000];
        String line;
        int count = 0;
        long initialTime=System.nanoTime();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            while ((line = br.readLine()) != null) {
                String[] words = line.split(",");
                for (String word : words) {
                    wordDictionary[count] = word;
                    distArr[count] = Sequences.editDistance(word, indexTerm);
                    count++;
                }
            }
//            System.out.println("Number of words in file: " + count);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Replace bubble sort with merge sort
        mergeSort(distArr, wordDictionary, 0, count - 1);
        long finalTime=System.nanoTime();

        long timeDiff=finalTime-initialTime;
//        System.out.println("it took "+timeDiff/1000000);
        // Print the top 10 similar words
//        System.out.println("Top 10 similar words:");
        for (int i = 0; i < wordCount; i++) {
//            System.out.println(wordDictionary[i]);
            similarWordsArray.add(wordDictionary[i]);
        }

        return similarWordsArray;




    }

    // Merge sort implementation
    public static void mergeSort(int[] distArr, String[] wordDictionary, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(distArr, wordDictionary, left, mid);
            mergeSort(distArr, wordDictionary, mid + 1, right);
            merge(distArr, wordDictionary, left, mid, right);
        }
    }

    public static void merge(int[] distArr, String[] wordDictionary, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        int[] leftDist = new int[n1];
        int[] rightDist = new int[n2];
        String[] leftDict = new String[n1];
        String[] rightDict = new String[n2];

        for (int i = 0; i < n1; ++i) {
            leftDist[i] = distArr[left + i];
            leftDict[i] = wordDictionary[left + i];
        }
        for (int j = 0; j < n2; ++j) {
            rightDist[j] = distArr[mid + 1 + j];
            rightDict[j] = wordDictionary[mid + 1 + j];
        }

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if (leftDist[i] <= rightDist[j]) {
                distArr[k] = leftDist[i];
                wordDictionary[k] = leftDict[i];
                i++;
            } else {
                distArr[k] = rightDist[j];
                wordDictionary[k] = rightDict[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            distArr[k] = leftDist[i];
            wordDictionary[k] = leftDict[i];
            i++;
            k++;
        }

        while (j < n2) {
            distArr[k] = rightDist[j];
            wordDictionary[k] = rightDict[j];
            j++;
            k++;
        }
    }

    public static ArrayList<String> spellCheck(String userInput) {
//        System.out.println("--------EX2----------\r\n");
//        Scanner scanner = new Scanner(System.in);



        return  findSimilarWords(userInput, "D:\\CheapFlightsScraper\\Contri_Codes\\CheapFlights.csv.csv",2);

    }

    public static void main(String[] args) {
//        ex1();
//        spellCheck();
    }
}
