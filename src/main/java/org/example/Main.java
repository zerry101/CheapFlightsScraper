package org.example;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {

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
                    LiveScraperCode(); // make function for running that part of code
                } else if(option == 2) {
                    UsingDB(scanner); // make function for running that part of code
                } else {
                    System.out.println("Invalid input. Please enter 1 for live scraping or 2 for using the database.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter 1 for live scraping or 2 for using the database.");
                scanner.next();
            }
        }
    }

    private static void UsingDB(Scanner scanner) {
        // Initialize Red-Black BST
        RedBlackBST<String, Integer> redBlackBstTree = new RedBlackBST<>();
        Map<String, String> places = populatePlaces();
        populateRedBlackBST(redBlackBstTree, places);

        // Print city and airport codes
        System.out.println("The Cities to choose from:");
        for (Map.Entry<String, String> entry : places.entrySet()) {
            String city = entry.getKey();
            System.out.println("City: " + city);
        }

        // Prompt user for departure and arrival cities
        String from = promptForCity(scanner, "departure", true, redBlackBstTree, places);
        String to = promptForCity(scanner, "arrival", false, redBlackBstTree, places);

        // Print chosen cities
        System.out.println("Departure city: " + from);
        System.out.println("Arrival city: " + to);

        // Prompt user for site choices
        List<String> sites = promptForSites(scanner);

        // Print chosen sites
        System.out.println("Chosen sites: " + sites);

        // Map site names to CSV file names
        Map<String, String> siteToCSVMap = new HashMap<>();
        siteToCSVMap.put("Expedia", "expedia_flights.csv"); // change locations
        siteToCSVMap.put("Travelocity", "travelocity_flights.csv");
        siteToCSVMap.put("CheapFlights", "cheapflights_flights.csv");
        siteToCSVMap.put("Air Canada", "aircanada_flights.csv");
        siteToCSVMap.put("Kayak", "kayak_flights.csv");
        siteToCSVMap.put("Momondo", "momondo_flights.csv");

        // Get the list of CSV files based on selected sites
        List<String> siteCSVs = new ArrayList<>();
        for (String site : sites) {
            if (siteToCSVMap.containsKey(site)) {
                siteCSVs.add(siteToCSVMap.get(site));
            } else {
                System.out.println("No CSV file found for site: " + site);
            }
        }

        // Prompt user for flight preference
        int preference = promptForFlightPreference(scanner);

        // Run the appropriate function based on the user's preference
        switch (preference) {
            case 1:
                findCheapestFlight(from, to, siteCSVs);
                break;
            case 2:
                findShortestFlight(from, to, siteCSVs);
                break;
            case 3:
                findBestFlight();
                break;
            case 4:
                flightOPSearch();
                break;
            case 5:
                displayFlights();
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }

    private static void flightOPSearch() {
        // Implementation of flightOPSearch function
    }

    private static void displayFlights() {
        // Implementation of displayFlights function
    }

    private static void populateRedBlackBST(RedBlackBST<String, Integer> redBlackBstTree, Map<String, String> places) {
        for (Map.Entry<String, String> entry : places.entrySet()) {
            redBlackBstTree.put(entry.getKey(), 1);
        }
    }

    private static String promptForCity(Scanner scanner, String type, boolean isDeparture, RedBlackBST<String, Integer> redBlackBstTree, Map<String, String> places) {
        scanner.nextLine(); // Clear the buffer
        String city;
        while (true) {
            if (isDeparture) {
                System.out.println("\nPlease enter your " + type + " city (Toronto, Delhi, Dubai):");
            } else {
                System.out.println("\nPlease enter your " + type + " city (London, Paris, New York):");
            }
            city = scanner.nextLine().trim().toLowerCase();

            if (places.containsKey(city)) {
                break;
            } else {
                System.out.println("Did you mean " + redBlackBstTree.autoComplete(city).keySet() + " ?");
            }
        }
        return city;
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

    private static void LiveScraperCode() {
        // Your live scraper code implementation here
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

    private static void findCheapestFlight(String departureCity, String arrivalCity, List<String> siteCSVs) {
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
                    double price = Double.parseDouble(values[2].trim());
                    String operator = values[3];
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

    private static void findShortestFlight(String departureCity, String arrivalCity, List<String> siteCSVs) {
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
                    double price = Double.parseDouble(values[2].trim());
                    String operator = values[3];
                    String depCity = values[4].trim().toLowerCase();
                    String arrCity = values[5].trim().toLowerCase();

                    if (depCity.equals(departureCity.toLowerCase()) && arrCity.equals(arrivalCity.toLowerCase())) {
                        LocalTime departureLocalTime = convertTo24HourFormat(depTime);
                        LocalTime arrivalLocalTime = convertTo24HourFormat(arrTime);

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

    private static LocalTime convertTo24HourFormat(String time) {
        DateTimeFormatter formatter12Hour = DateTimeFormatter.ofPattern("hh:mm a");
        DateTimeFormatter formatter24Hour = DateTimeFormatter.ofPattern("HH:mm");

        LocalTime localTime = LocalTime.parse(time.substring(0, 7).trim(), formatter12Hour);
        return LocalTime.parse(localTime.format(formatter24Hour), formatter24Hour);
    }

    private static Duration calculateFlightDuration(LocalTime departureTime, LocalTime arrivalTime, boolean nextDay) {
        if (nextDay) {
            arrivalTime = arrivalTime.plusHours(24);
        }
        return Duration.between(departureTime, arrivalTime);
    }

    private static void findBestFlight() {
        // Your code to find the best flight here
        System.out.println("Finding the best flight...");
    }

    private static void displayFiles() {
        // Your code to display files here
        System.out.println("Displaying flights...");
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
}
