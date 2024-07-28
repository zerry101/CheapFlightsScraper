package Test1;


import org.example.Main;
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
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;


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
        Map<String, String> places = new HashMap<>();
        Map<String, Integer> searchFrequency = new HashMap<>();


        System.setProperty("webdriver.chrome.driver", "C:\\Users\\macon\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");


        System.out.println("*****************************************");
        System.out.println("*                                       *");
        System.out.println("*           FlightPriceAnalyser         *");
        System.out.println("*                                       *");
        System.out.println("*****************************************");
        // Inform user about the tool
        System.out.println("Welcome to FlightPriceAnalyser!");
        System.out.println("This tool helps you find the best flight deals for August 31st.");
        int option;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("Do you want to scrape the data live or use the database?");
                System.out.println("Press 1 for live scraping ");
                System.out.println("Press 2 for using the database ");
                option = scanner.nextInt();
                if (option == 1) {
//                    LiveScraperCode();
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
                    }// make function for running that part of code


                    Scanner scanner2 = new Scanner(System.in);
                    String from, to;

                    // Prompt user to enter departure city and validate

                    while (true) {
                        System.out.println("\nPlease Enter Your departure city from the list above:");
                        from = scanner2.nextLine().trim().toLowerCase();


                        if (places.containsKey(from)) {
                            break; // Valid city entered, exit loop
                        } else {
                            System.out.println("Did you mean " + redBlackBstTree.autoComplete(from).keySet() + " ?");
                        }


                    }

                    while (true) {
                        System.out.println("Please Enter Your Arriving city from the list above:");
                        to = scanner2.nextLine().trim().toLowerCase();

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
                    scrapeFlights.
                            scrapeFlights();

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


                } else if(option == 2) {

//USING DB (DATASET)




                    SpellChecker spellChecker = new SpellChecker();
                    String spellCheckCSVFilePath="D:\\CheapFlightsScraper\\flight_data.csv";
//                    spellChecker.loadVocabularyFromCSV(spellCheckCSVFilePath);


                    for (Map.Entry<String, String> entry :deparurePlaces().entrySet()) {
                        redBlackBstTree.put(entry.getKey(), 1);
                    }



                    Scanner scanner2 = new Scanner(System.in);
                    String from, to;

                    // Prompt user to enter departure city and validate

                    System.out.println("The departure Cities with their corresponding airport codes");
                    for (Map.Entry<String, String> entry : deparurePlaces().entrySet()) {
                        String city = entry.getKey();
                        String airportCode = entry.getValue();
                        System.out.println("City: " + city + ", Airport Code: " + airportCode);
                    }// make function for running that part of code


                    while (true) {
                        System.out.println("\nPlease Enter Your departure city from the list above:");
                        from = scanner2.nextLine().trim().toLowerCase();
                        SearchFrequencyTracker.processSearchQuery(from,searchFrequency);


                        if (deparurePlaces().containsKey(from)) {
                            break; // Valid city entered, exit loop
                        } else {
                            if(!redBlackBstTree.autoComplete(from).keySet().isEmpty()){
                                System.out.println("Did you mean " + redBlackBstTree.autoComplete(from).keySet() + " ?");
                            }
                            else{
                                ArrayList similarWordsArray=SpellChecker.spellCheck(from,null);

                                System.out.println("Did you mean "+similarWordsArray.get(1)+"?");
                            }                        }


                    }

                    for (Map.Entry<String, String> entry :arrivingPlaces().entrySet()) {
                        redBlackBstTree.put(entry.getKey(), 1);
                    }

                    System.out.println("The arrival Cities with their corresponding airport codes");
                    for (Map.Entry<String, String> entry : arrivingPlaces().entrySet()) {
                        String city = entry.getKey();
                        String airportCode = entry.getValue();
                        System.out.println("City: " + city + ", Airport Code: " + airportCode);
                    }// make function for running that part of code


                    while (true) {
                        System.out.println("Please Enter Your Arriving city from the list above");

                        to = scanner2.nextLine().trim().toLowerCase();

                        SearchFrequencyTracker.processSearchQuery(to,searchFrequency);

                        if (arrivingPlaces().containsKey(to)) {
                            if (!from.equals(to)) {
                                break; // Valid city entered and different from 'from', exit loop
                            } else {
                                System.out.println("Arriving city cannot be the same as departure city. Please enter a different city.");
                            }
                        } else {
                            if(!redBlackBstTree.autoComplete(to).keySet().isEmpty()){
                                System.out.println("Did you mean " + redBlackBstTree.autoComplete(to).keySet() + " ?");
                            }
                            else{
                                ArrayList similarWordsArray=SpellChecker.spellCheck(to,null);

                                System.out.println("Did you mean "+similarWordsArray.get(1)+"?");
                            }
//
//                                List<String> suggestions = spellChecker.suggestAlternatives(to, 2); // Suggest alternatives //Priyal Thakkar

                                //                          }





                        }
                    }

                    System.out.println("You have selected:");
                    System.out.println("Departure City: " + from + ", Airport Code: " + deparurePlaces().get(from));
                    System.out.println("Arriving City: " + to + ", Airport Code: " + arrivingPlaces().get(to));


                    List<String> sites = promptForSites(scanner);

                    Map<String, String> siteToCSVMap = new HashMap<>();
                    siteToCSVMap.put("Expedia", "D:\\CheapFlightsScraper\\Contri_Codes\\Expedia (2).csv"); // change locations
                    siteToCSVMap.put("Travelocity", "D:\\CheapFlightsScraper\\Contri_Codes\\NEW_Travelocity (1).csv");
                    siteToCSVMap.put("CheapFlights", "D:\\CheapFlightsScraper\\Contri_Codes\\CheapFlights.csv.csv");
                    siteToCSVMap.put("Air Canada", "D:\\CheapFlightsScraper\\Contri_Codes\\AirCanada_Data (1).csv");
                    siteToCSVMap.put("Kayak", "D:\\CheapFlightsScraper\\Contri_Codes\\Updated_Converted_Kayak_Dataset (1).csv");
                    siteToCSVMap.put("Momondo", "D:\\CheapFlightsScraper\\Contri_Codes\\Updated_Momondo_Flightdata (1).csv");


                    // Get the list of CSV files based on selected sites
                    List<String> siteCSVs = new ArrayList<>();
                    for (String site : sites) {
                        if (siteToCSVMap.containsKey(site)) {
                            siteCSVs.add(siteToCSVMap.get(site));
                        } else {
                            System.out.println("No CSV file found for site: " + site);
                        }
                    }


                    int preference = promptForFlightPreference(scanner);

                    // Run the appropriate function based on the user's preference
                    switch (preference) {
                        case 1:
                            findCheapestFlight(from, to, siteCSVs,"");
                            break;
                        case 2:
                            findShortestFlight(from, to, siteCSVs,"");
                            break;
                        case 3:
                            BestFlightFinder.findBestFlight(from, to, siteCSVs, null);
                            break;
                        case 4:
                                flightOPSearch(from,to,siteCSVs,scanner);
                            break;
//                        case 5:
//                            displayFlights();
//                            break;

                        default:
                            System.out.println("Invalid choice.");
                            break;
                    }


                    SearchFrequencyTracker.displayTopSearches(searchFrequency,10);


//                    UsingDB(scanner);
                    // make function for running that part of code
                } else {
                    System.out.println("Invalid input. Please enter 1 for live scraping or 2 for using the database.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter 1 for live scraping or 2 for using the database.");
                scanner.next();
            }
        }



        // Initialize ChromeDriver




        // Prompt user to enter arriving city and validate



        // Now 'from' and 'to' contain valid cities from the HashMap


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

    private static Map<String, String> populatePlaces() {
        Map<String, String> places = new HashMap<>();
        // Include all specified cities in the Red-Black BST
        places.put("toronto", "YYZ");
        places.put("delhi", "DEL");
        places.put("dubai", "DXB");
        places.put("london", "LHR");
        places.put("paris", "ORY");
        places.put("new york", "JFK");
        return places;
    }

    private static Map<String, String> deparurePlaces() {
        Map<String, String> deparurePlaces = new HashMap<>();
        // Include all specified cities in the Red-Black BST
        deparurePlaces.put("toronto", "YYZ");
        deparurePlaces.put("delhi", "DEL");
        deparurePlaces.put("dubai", "DXB");

        return deparurePlaces;
    }

    private static Map<String, String> arrivingPlaces() {
        Map<String, String> arrivingPlaces = new HashMap<>();
        // Include all specified cities in the Red-Black BST
        arrivingPlaces.put("london", "LHR");
        arrivingPlaces.put("paris", "ORY");
        arrivingPlaces.put("new york", "JFK");
        return arrivingPlaces;
    }



    private static List<String> promptForSites(Scanner scanner) {
        List<String> selectedSites = new ArrayList<>();
        Map<Integer, String> siteOptions = new HashMap<>();
        siteOptions.put(1, "Expedia");
        siteOptions.put(2, "Travelocity");
        siteOptions.put(3, "CheapFlights");
        siteOptions.put(4, "Air Canada");
        siteOptions.put(5, "Kayak");
        siteOptions.put(6, "Momondo");

        System.out.println("\nPlease choose the sites you want to scrape data from (enter numbers separated by commas):");
        for (Map.Entry<Integer, String> entry : siteOptions.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue());
        }

        scanner.nextLine(); // Clear the buffer
        String input = scanner.nextLine();
        String[] choices = input.split(",");

        for (String choice : choices) {
            try {
                int option = Integer.parseInt(choice.trim());
                if (siteOptions.containsKey(option)) {
                    selectedSites.add(siteOptions.get(option));
                } else {
                    System.out.println("Invalid choice: " + choice);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + choice);
            }
        }

        return selectedSites;
    }


    private static double parsePrice(String priceString) {
        // Remove any non-numeric characters except the dot (.)
        return Double.parseDouble(priceString.replaceAll("[^\\d.]", ""));
    }

    private static void findCheapestFlight(String departureCity, String arrivalCity, List<String> siteCSVs,String operator) {
        System.out.println("Finding the cheapest flight...");
        Flight cheapestFlight = null;

        for (String csvFile : siteCSVs) {
            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("Departure Time")) {
                        continue;
                    }

                    String[] values = line.split(",");

                    String depTime = values[0];
                    String arrTime = values[1];
                    double price = parsePrice(values[2].trim());

                    String flightOperator="";
                    flightOperator = SpellChecker.spellCheck(operator,csvFile).toString();

                    operator=flightOperator;



                    String depCity = values[4].trim().toLowerCase();
                    String arrCity = values[5].trim().toLowerCase();

                    if (depCity.equals(departureCity.toLowerCase()) && arrCity.equals(arrivalCity.toLowerCase())) {
                        Flight currentFlight = new Flight(depTime, arrTime, depCity, arrCity, price, operator);

                        if (cheapestFlight == null || currentFlight.getPrice() < cheapestFlight.getPrice()) {
                            cheapestFlight = currentFlight;
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading file: " + csvFile);
                e.printStackTrace();
            }
        }

        if (cheapestFlight != null) {
            System.out.println("Cheapest Flight Details:");
            System.out.println("Departure Time: " + cheapestFlight.getDepartureTime());
            System.out.println("Arrival Time: " + cheapestFlight.getArrivalTime());
            System.out.println("Departure City: " + cheapestFlight.getDepartureCity());
            System.out.println("Arrival City: " + cheapestFlight.getArrivalCity());
            System.out.println("Price: $" + cheapestFlight.getPrice());
            System.out.println("Flight Operator: " + cheapestFlight.getOperator());
        } else {
            System.out.println("No flights found for the given cities.");
        }
    }

    private static void findShortestFlight(String departureCity, String arrivalCity, List<String> siteCSVs,String operator) {
        System.out.println("Finding the shortest flight...");
        Flight shortestFlight = null;
        Duration shortestDuration = null;

        for (String csvFile : siteCSVs) {
            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("Departure Time")) {
                        continue;
                    }

                    String[] values = line.split(",");

                    String depTime = values[0];
                    String arrTime = values[1];
                    double price = parsePrice(values[2].trim());

                    String flightOperator="";
                    flightOperator = SpellChecker.spellCheck(operator,csvFile).toString();
                    operator=flightOperator;

                    String depCity = values[4].trim().toLowerCase();
                    String arrCity = values[5].trim().toLowerCase();

                    if (depCity.equals(departureCity.toLowerCase()) && arrCity.equals(arrivalCity.toLowerCase())) {
//                        LocalTime departureLocalTime = LocalTime.parse(convertTo24HourFormat(depTime.trim()));
//                        LocalTime arrivalLocalTime = LocalTime.parse(convertTo24HourFormat(arrTime.trim()));
                        String departureLocalTime = convertTo24HourFormat(depTime.trim());
                        String  arrivalLocalTime = convertTo24HourFormat(arrTime.trim());

                        Duration duration = calculateFlightDuration(departureLocalTime, arrivalLocalTime, arrTime.contains("+1"));

                        Flight currentFlight = new Flight(depTime, arrTime, depCity, arrCity, price, operator);

                        if (shortestDuration == null || duration.compareTo(shortestDuration) < 0) {
                            shortestFlight = currentFlight;
                            shortestDuration = duration;
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("Error reading file: " + csvFile);
                e.printStackTrace();
            }
        }

        if (shortestFlight != null) {
            System.out.println("Shortest Flight Details:");
            System.out.println("Departure Time: " + shortestFlight.getDepartureTime());
            System.out.println("Arrival Time: " + shortestFlight.getArrivalTime());
            System.out.println("Departure City: " + shortestFlight.getDepartureCity());
            System.out.println("Arrival City: " + shortestFlight.getArrivalCity());
            System.out.println("Price: $" + shortestFlight.getPrice());
            System.out.println("Flight Operator: " + shortestFlight.getOperator());
            System.out.println("Duration: " + shortestDuration.toHours() + " hours and " + shortestDuration.toMinutesPart() + " minutes");
        } else {
            System.out.println("No flights found for the given cities.");
        }
    }


    private static class Flight {
        private final String departureTime;
        private final String arrivalTime;
        private final String departureCity;
        private final String arrivalCity;
        private final double price;
        private final String operator;

        public Flight(String departureTime, String arrivalTime, String departureCity, String arrivalCity, double price, String operator) {
            this.departureTime = departureTime;
            this.arrivalTime = arrivalTime;
            this.departureCity = departureCity;
            this.arrivalCity = arrivalCity;
            this.price = price;
            this.operator = operator;
        }

        public String getDepartureTime() {
            return departureTime;
        }

        public String getArrivalTime() {
            return arrivalTime;
        }

        public String getDepartureCity() {
            return departureCity;
        }

        public String getArrivalCity() {
            return arrivalCity;
        }

        public double getPrice() {
            return price;
        }

        public String getOperator() {
            return operator;
        }
    }


    private static int promptForFlightPreference(Scanner scanner) {
        System.out.println("\nPlease choose your flight preference:");
        System.out.println("1. Cheapest flight");
        System.out.println("2. Shortest flight");
        System.out.println("3. Best flight");
        System.out.println("4. Search for FLight Operator");
        System.out.println("5. Display FLights");

        int preference = -1;
        while (preference < 1 || preference > 5) {
            try {
                preference = scanner.nextInt();
                if (preference < 1 || preference > 5) {
                    System.out.println("Invalid input. Please enter a number between 1 and 5.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                scanner.next(); // Clear invalid input
            }
        }
        return preference;
    }

    private static String convertTo24HourFormat(String time) {
        // Normalize the string by trimming and replacing multiple spaces with a single space
        String normalizedTime = time.trim().replaceAll("\\s+", " ");
//        System.out.println("Normalized time string: '" + normalizedTime + "'");

        // Split time and AM/PM part
        String[] parts = normalizedTime.split(" ");
        String timePart = parts[0];
        String amPmPart = parts[1];


        // Split hour and minute
        String[] timeParts = timePart.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        // Convert hour based on AM/PM
        if (amPmPart.equalsIgnoreCase("PM") && hour != 12) {
            hour += 12;
        } else if (amPmPart.equalsIgnoreCase("AM") && hour == 12) {
            hour = 0;
        }



        // Format to 24-hour time string
        return String.format("%02d:%02d", hour, minute);
    }


    private static Duration calculateFlightDuration(String departureTime, String arrivalTime, boolean nextDay) {
        LocalTime depTime = LocalTime.parse(departureTime);
        LocalTime arrTime = LocalTime.parse(arrivalTime);

        // If the arrival time is on the next day, adjust the arrival time by adding 24 hours
        if (nextDay) {
            arrTime = arrTime.plusHours(24);
        }

        return Duration.between(arrTime,depTime).abs();
    }

    private static void findBestFlight(String departureCity, String arrivalCity, List<String> csvFiles, String airline) {
        BestFlightFinder.findBestFlight(departureCity, arrivalCity, csvFiles, airline);
    }



    private static void flightOPSearch(String departureCity, String arrivalCity, List<String> siteCSVs, Scanner scanner) {
        System.out.println("\nEnter the flight operator name:");
        scanner.nextLine(); // Clear the buffer
        String operator = scanner.nextLine().trim().toLowerCase();

        // Prompt user for flight preference
        int preference = promptForFlightPreference(scanner);

        // Run the appropriate function based on the user's preference and operator
        switch (preference) {
            case 1:
                findCheapestFlight(departureCity, arrivalCity, siteCSVs, operator);
                break;
            case 2:
                findShortestFlight(departureCity, arrivalCity, siteCSVs, operator);
                break;
            case 3:
                findBestFlight(departureCity, arrivalCity, siteCSVs, operator);
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }

    private static void displayFlights() {
        // Initialize a list to store all the flights
        List<String> allFlights = new ArrayList<>();

        // Specify the CSV files containing flight data
        List<String> csvFiles = Arrays.asList("expedia_flights.csv", "travelocity_flights.csv",
                "cheapflights_flights.csv", "aircanada_flights.csv",
                "kayak_flights.csv", "momondo_flights.csv");

        // Read and add flights from each CSV file
        for (String csvFile : csvFiles) {
            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    if (values.length < 5) {
                        continue; // skip invalid rows
                    }
                    allFlights.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Display all flights
        if (!allFlights.isEmpty()) {
            System.out.println("\nAll available flights:");
            for (String flight : allFlights) {
                System.out.println(flight);
            }
        } else {
            System.out.println("\nNo flights available.");
        }
    }

}

