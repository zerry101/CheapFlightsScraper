package org.example;

import java.util.*;

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

        // Prompt user for flight preference
        int preference = promptForFlightPreference(scanner);

        // Run the appropriate function based on the user's preference
        switch (preference) {
            case 1:
                findCheapestFlight();
                break;
            case 2:
                findShortestFlight();
                break;
            case 3:
                findBestFlight();
                break;
            case 4:
                displayFiles();
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
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

        siteOptions.put(3, "CheapFlights");



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


        int preference = -1;
        while (preference < 1 || preference > 4) {
            try {
                preference = scanner.nextInt();
                if (preference < 1 || preference > 4) {
                    System.out.println("Invalid input. Please enter a number between 1 and 4.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                scanner.next(); // Clear invalid input
            }
        }
        return preference;
    }

    private static void findCheapestFlight() {
        // Your code to find the cheapest flight here
        System.out.println("Finding the cheapest flight...");
    }

    private static void findShortestFlight() {
        // Your code to find the shortest flight here
        System.out.println("Finding the shortest flight...");
    }

    private static void findBestFlight() {
        // Your code to find the best flight here
        System.out.println("Finding the best flight...");
    }

    private static void displayFiles() {
        // Your code to display files here
        System.out.println("Displaying flights...");
    }
}
