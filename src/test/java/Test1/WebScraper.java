package Test1;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class demonstrates web scraping functionalities using Selenium WebDriver and Jsoup.
 * It scrapes flight information from a travel website, updates vocabulary files, and provides autocomplete suggestions.
 */
public class WebScraper {

    // Map to store unique words and their counts extracted from web pages
    private static Map<String, Integer> uniqueWordsMap1 = new HashMap<>();
    private static HashMap<String, Integer> searchFrequencyMap = new HashMap<>();

    /**
     * Main method to initiate web scraping process.
     *
     * @param args command line arguments (not used in this context).
     * @throws InterruptedException if any thread has interrupted the current thread.
     * @throws IOException          if an I/O exception occurs.
     */
    public static void main(String[] args) throws InterruptedException, IOException {

        // Initialize Red-Black BST
        RedBlackBST<String, Integer> redBlackBstTree = new RedBlackBST<>();

        // Set system property for ChromeDriver
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\macon\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");

        // Initialize ChromeDriver
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Navigate to the website
        driver.get("https://www.cheapflights.ca/");

        // Make explicit wait
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Get flight origin input field
        WebElement inputField1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[aria-label='Flight origin input']")));

        // Get the page source
        String pageSource = driver.getPageSource();

        // Parse the page source and update vocabulary file (vocabulary1.txt)
        parsePage(pageSource, "vocabulary1.txt");
        parseFile("vocabulary1.txt");

        // Initialize SearchPage object and perform flight search

        Map<String, String> places = new HashMap<>();
        places.put("toronto", "YYZ");
        places.put("manila", "MNL");
        places.put("new delhi", "DEL");
        places.put("vancouver", "YVR");
        places.put("calgary", "YYC");
        places.put("orlando", "MCO");
        places.put("edmonton", "YEG");
        places.put("montreal", "YUL");
        places.put("london", "LHR");
        places.put("las vegas", "LAS");
        places.put("fort lauderdale", "FLL");
        places.put("halifax", "YHZ");
        places.put("new york", "JFK");
        places.put("los angeles", "LAX");
        places.put("paris", "ORY");
        places.put("hong kong", "HKG");
        places.put("singapore", "SIN");
        places.put("chicago", "ORD");
        places.put("sydney", "SYD");
        places.put("shanghai", "SHA");
        places.put("bangkok", "BKK");
        places.put("istanbul", "IST");
        places.put("rome", "FCO");
        places.put("beijing", "PEK");

        for (Map.Entry<String, String> entry : places.entrySet()) {
            redBlackBstTree.put(entry.getKey(), 1);
        }

        System.out.println("The Cities with their corresponding airport codes");
        for (Map.Entry<String, String> entry : places.entrySet()) {
            String city = entry.getKey();
            String airportCode = entry.getValue();
            System.out.println("City: " + city + ", Airport Code: " + airportCode);
        }

        Scanner scanner = new Scanner(System.in);
        String from, to;

        // Prompt user to enter departure city and validate

        while (true) {
            System.out.println("\nPlease Enter Your departure city from the list above:");
            from = scanner.nextLine().trim().toLowerCase();


            if (places.containsKey(from)) {
                break; // Valid city entered, exit loop
            } else {
                System.out.println("Did you mean " + redBlackBstTree.autoComplete(from).keySet() + " ?");
            }


        }

        // Prompt user to enter arriving city and validate
        while (true) {
            System.out.println("Please Enter Your Arriving city from the list above:");
            to = scanner.nextLine().trim().toLowerCase();

            if (places.containsKey(to)) {
                if (!from.equals(to)) {
                    break; // Valid city entered and different from 'from', exit loop
                } else {
                    System.out.println("Arriving city cannot be the same as departure city. Please enter a different city.");
                }
            } else {
                System.out.println("Did you mean " + redBlackBstTree.autoComplete(to).keySet() + " ?");
            }
        }


        // Now 'from' and 'to' contain valid cities from the HashMap
        System.out.println("You have selected:");
        System.out.println("Departure City: " + from + ", Airport Code: " + places.get(from));
        System.out.println("Arriving City: " + to + ", Airport Code: " + places.get(to));

        int type = 0;
        while (true) {

            try {
                System.out.println("Press 1 to get Quickest Flights ");
                System.out.println("Press 2 to get Best Flights ");
                System.out.println("Press 3 to get Cheapest Flights ");
                type = scanner.nextInt();

                if (type >= 1 && type <= 3) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter a number from 1 to 3.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number from 1 to 3.");
                scanner.next(); // clear the invalid input
            }
        }


        if (type != 0) {
            SearchPage searchPage = new SearchPage(driver, from, to);
            searchPage.searchFlights();
        }


        // Get all window handles
        Set<String> allWindowHandles = driver.getWindowHandles();

        // Switch to the new tab for search results
        List<String> windowHandlesList = new ArrayList<>(allWindowHandles);
        String secondWindowHandle = null;
        if (windowHandlesList.size() > 1) {
            secondWindowHandle = windowHandlesList.get(1);
            driver.switchTo().window(secondWindowHandle);
        }


//        Thread.sleep(1500);


        // Initialize ScrapeFlights object and scrape flight data
        ScrapeFlights scrapeFlights = new ScrapeFlights(driver, type, from, to);
        scrapeFlights.scrapeFlights();

        // Write flight data to CSV file (flight_data.csv)

        writeListsToCSV("flight_data.csv", scrapeFlights.getArrivingFlightTimes(), scrapeFlights.getDepartingFlightTimes(), scrapeFlights.getFlightPrices(), scrapeFlights.getFlightCompanies(), scrapeFlights.getFlightDepartingLocation(), scrapeFlights.getFlightArrivingLocation());

        // Get the updated page source
        pageSource = driver.getPageSource();

        // Parse the updated page source and update vocabulary file (vocabulary2.txt)
        parsePage(pageSource, "vocabulary2.txt");
        parseFile("vocabulary2.txt");


        // Populate Red-Black BST with unique words and their counts
        for (Map.Entry<String, Integer> entry : uniqueWordsMap1.entrySet()) {
            redBlackBstTree.put(entry.getKey(), entry.getValue());
        }

        // Get top suggestions for a given prefix

        String content1 = new String(Files.readAllBytes(Paths.get("D:\\Labs\\Lab 8 - Text Processing\\Text Processing\\textprocessing\\Protein.txt")));
        KMP kmp1 = new KMP(content1);
        System.out.println(content1.length());

        System.out.println(kmp1.search("complex"));


        System.out.println("Enter prefix word to get top suggested words accordingly");

        String prefix = scanner.next();
        System.out.println("-----Top suggested words for prefix " + prefix + "---");
        for (String key : getTopSuggestions(redBlackBstTree.autoComplete(prefix))) {
            System.out.println(key + ":" + redBlackBstTree.get(key));
        }


        System.out.println("Type 'exit' to exit");


        String word = "";
        int exitKeyword = -1;

        while (exitKeyword != 0) {
            System.out.println("Enter word to search:");

            word = scanner.next();
            String content = new String(Files.readAllBytes(Paths.get("D:\\SeleniumLab\\flight_data.csv")));

            System.out.println(content.length());

            KMP kmp = new KMP(content);


            int offset = kmp.search(word);

            searchFrequencyMap.put(word, searchFrequencyMap.getOrDefault(word, 0) + 1);
//            int offset = searchWithFreq(word);
            System.out.println(word + " offset is : " + offset);
            System.out.println("Search Frequencies:");
            for (Map.Entry<String, Integer> entry : searchFrequencyMap.entrySet()) {
                System.out.println("Word: " + entry.getKey() + ", Search Frequency: " + entry.getValue());
            }


            while (true) {
                System.out.println("Type 1 to continue or 0 to exit:");

                try {
                    exitKeyword = scanner.nextInt();
                    if (exitKeyword == 1 || exitKeyword == 0) {
                        break;
                    } else {
                        System.out.println("Please enter 1 to continue or 0 to exit.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter an integer value.");
                    scanner.next(); // Clear the invalid input
                }
            }

            System.out.println("Extracted Prices list from generated CSV File is  ");
            ExtractPrices extractPrices = new ExtractPrices();

            System.out.println(extractPrices.extractPricesFromText("D:\\SeleniumLab\\flight_data.csv"));

        }


    }

    /**
     * Method to write multiple lists to a CSV file.
     *
     * @param fileName the name of the CSV file to write to.
     * @param lists    variable number of lists containing data to write to the CSV.
     */


    public static int searchWithFreq(String word) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get("D:\\SeleniumLab\\vocabulary2.txt")));
        KMP kmp = new KMP(content);


        int offset = kmp.search(word);

        searchFrequencyMap.put(word, searchFrequencyMap.getOrDefault(word, 0) + 1);
        return offset;
    }


    public static void writeListsToCSV(String fileName, List<String>... lists) {
        // Determine the maximum length of the provided lists
        int maxLength = 0;
        for (List<String> list : lists) {
            if (list.size() > maxLength) {
                maxLength = list.size();
            }
        }

        // Write to CSV
        try (FileWriter csvWriter = new FileWriter(fileName)) {
            // Write data
            for (int i = 0; i < maxLength; i++) {
                for (int j = 0; j < lists.length; j++) {
                    if (i < lists[j].size()) {
                        csvWriter.append(lists[j].get(i));
                    } else {
                        csvWriter.append(""); // or any other placeholder
                    }
                    if (j < lists.length - 1) {
                        csvWriter.append(",");
                    }
                }
                csvWriter.append("\n");
            }

            System.out.println("CSV file created successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to parse the whole webpage and count word occurrences.
     *
     * @param pageSource the HTML source of the webpage.
     * @return a map containing words as keys and their counts as values.
     */
    public static Map<String, Integer> parseWholeWebPage(String pageSource) {
        Document document = Jsoup.parse(pageSource);
        String text = document.text();
//        System.out.println(text);

        String[] words = text.split("\\s+");
//        System.out.println("-----Array-------");

        Map<String, Integer> wordCounts = new HashMap<>();
        // Count the occurrences of each word
        for (String word : words) {
            wordCounts.put(word, wordCounts.getOrDefault(word, 0) + 1);
        }

        // Write the word counts to a file
        String filePath = "vocabulary.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return wordCounts;
    }

    /**
     * Method to get top suggestions based on frequency.
     *
     * @param autoCompleteMap a map containing words and their frequencies.
     * @return a list of words sorted by frequency in descending order.
     */
    public static List<String> getTopSuggestions(Map<String, Integer> autoCompleteMap) {
        List<String> suggestions = new ArrayList<>();
        PriorityQueue<Map.Entry<String, Integer>> minHeap = new PriorityQueue<>(Comparator.comparingInt(Map.Entry::getValue));

        // Add all matches to the min-heap
        for (Map.Entry<String, Integer> entry : autoCompleteMap.entrySet()) {
            minHeap.offer(entry);
        }

        // Extract suggestions from min-heap in descending order of frequency
        while (!minHeap.isEmpty()) {
            suggestions.add(0, minHeap.poll().getKey()); // Add to front for descending order
        }

        return suggestions;
    }

    /**
     * Method to parse the page source and write text content to a file.
     *
     * @param pageSource the HTML source of the webpage.
     * @param fileName   the name of the file to write the parsed text content.
     * @throws IOException if an I/O exception occurs.
     */
    public static void parsePage(String pageSource, String fileName) throws IOException {
        Document document = Jsoup.parse(pageSource);
        String text = document.text();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to parse a file and update the unique words map with word counts.
     *
     * @param filePath the path of the file to parse.
     * @throws IOException if an I/O exception occurs.
     */
    public static void parseFile(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");
                for (String word : words) {
                    word = word.toLowerCase();
                    uniqueWordsMap1.put(word, uniqueWordsMap1.getOrDefault(word, 0) + 1);
                }
            }
        }
    }
}
