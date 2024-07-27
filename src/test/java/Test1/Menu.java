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
import java.time.Duration;
import java.util.*;

/**
 * This class demonstrates web scraping functionalities using Selenium WebDriver and Jsoup.
 * It scrapes flight information from a travel website, updates vocabulary files, and provides autocomplete suggestions.
 */
public class Menu {

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
        // Print project title
        printProjectTitle();

        // Inform user about the tool
        System.out.println("Welcome to FlightPriceAnalyser!");
        System.out.println("This tool helps you find the best flight deals for August 31st.");

        // Initialize Red-Black BST
        RedBlackBST<String, Integer> redBlackBstTree = new RedBlackBST<>();

        // Prompt user for live scraping or using database
        Scanner scanner = new Scanner(System.in);
        int liveScraping = promptForScrapingOption(scanner);

        Map<String, String> places = new HashMap<>();
        String from = null;
        String to = null;

        if (liveScraping == 1) {
            // Populate Red-Black BST with city and airport codes
            places = populatePlaces();
            populateRedBlackBST(redBlackBstTree, places);

            // Print city and airport codes
            System.out.println("The Cities with their corresponding airport codes");
            for (Map.Entry<String, String> entry : places.entrySet()) {
                String city = entry.getKey();
                String airportCode = entry.getValue();
                System.out.println("City: " + city + ", Airport Code: " + airportCode);
            }

            // Prompt user for departure and arrival cities
            from = promptForCity(scanner, "departure", places, redBlackBstTree);
            to = promptForCity(scanner, "arriving", places, redBlackBstTree);

            WebDriver driver = null;
            try {
                // Initialize WebDriver and perform live scraping
                System.setProperty("webdriver.chrome.driver", "C:\\Users\\macon\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
                driver = new ChromeDriver();
                driver.manage().window().maximize();
                driver.get("https://www.cheapflights.ca/");
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

                WebElement inputField1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[aria-label='Flight origin input']")));
                String pageSource = driver.getPageSource();
                parsePage(pageSource, "vocabulary1.txt");
                parseFile("vocabulary1.txt");

                SearchPage searchPage = new SearchPage(driver, from, to);
                searchPage.searchFlights();

                Set<String> allWindowHandles = driver.getWindowHandles();
                List<String> windowHandlesList = new ArrayList<>(allWindowHandles);
                if (windowHandlesList.size() > 1) {
                    driver.switchTo().window(windowHandlesList.get(1));
                }

                // Prompt user for flight search type
                int type = promptForSearchType(scanner);

                ScrapeFlights scrapeFlights = new ScrapeFlights(driver, type, from, to);
                scrapeFlights.scrapeFlights();
                writeListsToCSV("flight_data.csv", scrapeFlights.getArrivingFlightTimes(), scrapeFlights.getDepartingFlightTimes(), scrapeFlights.getFlightPrices(), scrapeFlights.getFlightCompanies(), scrapeFlights.getFlightDepartingLocation(), scrapeFlights.getFlightArrivingLocation());

                pageSource = driver.getPageSource();
                parsePage(pageSource, "vocabulary2.txt");
                parseFile("vocabulary2.txt");
            } finally {
                if (driver != null) {
                    driver.quit();
                }
            }
        } else {
            // If using database, print city and airport codes
            places = populatePlaces();
            populateRedBlackBST(redBlackBstTree, places);
            System.out.println("The Cities with their corresponding airport codes");
            for (Map.Entry<String, String> entry : places.entrySet()) {
                String city = entry.getKey();
                String airportCode = entry.getValue();
                System.out.println("City: " + city + ", Airport Code: " + airportCode);
            }
        }

        populateRedBlackBSTWithUniqueWords(redBlackBstTree);

        // Perform autocomplete and search functionalities
        performAutocompleteAndSearch(scanner, redBlackBstTree);
    }

    private static void printProjectTitle() {
        System.out.println("*****************************************");
        System.out.println("*                                       *");
        System.out.println("*           FlightPriceAnalyser         *");
        System.out.println("*                                       *");
        System.out.println("*****************************************");
    }

    private static int promptForScrapingOption(Scanner scanner) {
        int option;
        while (true) {
            try {
                System.out.println("Do you want to scrape the data live or use the database?");
                System.out.println("Press 1 for live scraping ");
                System.out.println("Press 2 for using the database ");
                option = scanner.nextInt();
                if (option == 1 || option == 2) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 1 for live scraping or 2 for using the database.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter 1 for live scraping or 2 for using the database.");
                scanner.next();
            }
        }
        return option == 1;
    }

    private static int promptForSearchType(Scanner scanner) {
        int type;
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
                scanner.next();
            }
        }
        return type;
    }

    private static Map<String, String> populatePlaces() {
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
        return places;
    }

    private static void populateRedBlackBST(RedBlackBST<String, Integer> redBlackBstTree, Map<String, String> places) {
        for (Map.Entry<String, String> entry : places.entrySet()) {
            redBlackBstTree.put(entry.getKey(), 1);
        }
    }

    private static String promptForCity(Scanner scanner, String type, Map<String, String> places, RedBlackBST<String, Integer> redBlackBstTree) {
        scanner.nextLine(); // Clear the buffer
        String city;
        while (true) {
            System.out.println("\nPlease Enter Your " + type + " city from the list above:");
            city = scanner.nextLine().trim().toLowerCase();
            if (places.containsKey(city)) {
                break;
            } else {
                System.out.println("Did you mean " + redBlackBstTree.autoComplete(city).keySet() + " ?");
            }
        }
        return city;
    }

    private static void populateRedBlackBSTWithUniqueWords(RedBlackBST<String, Integer> redBlackBstTree) throws IOException {
        for (Map.Entry<String, Integer> entry : uniqueWordsMap1.entrySet()) {
            redBlackBstTree.put(entry.getKey(), entry.getValue());
        }
    }

    private static void performAutocompleteAndSearch(Scanner scanner, RedBlackBST<String, Integer> redBlackBstTree) throws IOException {
        System.out.println("\nEnter the prefix you want to autocomplete:");
        String prefix = scanner.next();

        System.out.println("Autocomplete suggestions:");
        for (String word : redBlackBstTree.autoComplete(prefix).keySet()) {
            System.out.println(word);
        }

        System.out.println("\nSearch for a word:");
        String searchWord = scanner.next();
        System.out.println("Frequency of the word '" + searchWord + "': " + redBlackBstTree.get(searchWord));
        updateSearchFrequency(searchWord);
        writeSearchFrequencyToFile("search_frequencies.txt", searchFrequencyMap);
    }

    private static void parsePage(String pageSource, String fileName) {
        Document document = Jsoup.parse(pageSource);
        String text = document.text();
        String[] words = text.split("\\W+");
        for (String word : words) {
            if (word.length() >= 2) {
                word = word.toLowerCase();
                uniqueWordsMap1.put(word, uniqueWordsMap1.getOrDefault(word, 0) + 1);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (Map.Entry<String, Integer> entry : uniqueWordsMap1.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(": ");
            if (parts.length == 2) {
                String word = parts[0].trim();
                int count = Integer.parseInt(parts[1].trim());
                uniqueWordsMap1.put(word, uniqueWordsMap1.getOrDefault(word, 0) + count);
            }
        }
        reader.close();
    }

    private static void updateSearchFrequency(String searchWord) {
        searchFrequencyMap.put(searchWord, searchFrequencyMap.getOrDefault(searchWord, 0) + 1);
    }

    private static void writeSearchFrequencyToFile(String fileName, Map<String, Integer> searchFrequencyMap) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        for (Map.Entry<String, Integer> entry : searchFrequencyMap.entrySet()) {
            writer.write(entry.getKey() + ": " + entry.getValue());
            writer.newLine();
        }
        writer.close();
    }

    private static void writeListsToCSV(String fileName, List<String> arrivingFlightTimes, List<String> departingFlightTimes, List<String> flightPrices, List<String> flightCompanies, List<String> flightDepartingLocation, List<String> flightArrivingLocation) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write("Departing Flight Time,Arriving Flight Time,Flight Price,Flight Company,Flight Departing Location,Flight Arriving Location");
        writer.newLine();
        for (int i = 0; i < arrivingFlightTimes.size(); i++) {
            writer.write(departingFlightTimes.get(i) + "," + arrivingFlightTimes.get(i) + "," + flightPrices.get(i) + "," + flightCompanies.get(i) + "," + flightDepartingLocation.get(i) + "," + flightArrivingLocation.get(i));
            writer.newLine();
        }
        writer.close();
    }
}
