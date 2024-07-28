package Test1;

import org.example.Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FlightSearchUtility {

    public static void flightOPSearch1(String departureCity, String arrivalCity, List<String> siteCSVs, Scanner scanner) {
        System.out.println("\nEnter the flight operator name:");
        scanner.nextLine(); // Clear the buffer
        String operator = scanner.nextLine().trim().toLowerCase();

        // Evaluate sites with the most flights for the specified airline
        String bestSite = null;
        int maxFlights = 0;
        Map<String, Integer> siteFlightCount = new HashMap<>();

        for (String csvFile : siteCSVs) {
            int count = countFlightsForOperator(csvFile, operator);
            siteFlightCount.put(csvFile, count);
            if (count > maxFlights) {
                maxFlights = count;
                bestSite = csvFile;
            }
        }

        // Output the site with the most flights for the specified airline
        if (bestSite != null) {
            System.out.println("\nOut of the chosen sites, the site with the most flights for the airline '" + operator + "' is: " + bestSite);
            System.out.println("Number of flights for this airline on the site: " + maxFlights);
        } else {
            System.out.println("\nNo flights found for the airline '" + operator + "' on the chosen sites.");
            return;
        }

        // Prompt user for flight preference
        int preference = promptForFlightPreference(scanner);

        // Run the appropriate function based on the user's preference and operator
        switch (preference) {
            case 1:
                WebScraper.findCheapestFlight(departureCity, arrivalCity, siteCSVs, operator);
                break;
            case 2:
                WebScraper.findShortestFlight(departureCity, arrivalCity, siteCSVs, operator);
                break;
            case 3:
                WebScraper.findBestFlight(departureCity, arrivalCity, siteCSVs, operator);
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }

    private static int countFlightsForOperator(String csvFile, String operator) {
        int count = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 4) {
                    String flightAirline = values[3].trim().toLowerCase();
                    if (flightAirline.equals(operator)) {
                        count++;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return count;
    }

    private static int promptForFlightPreference(Scanner scanner) {
        System.out.println("\nSelect your flight preference:");
        System.out.println("1. Cheapest flight");
        System.out.println("2. Shortest flight");
        System.out.println("3. Best flight");
        System.out.print("Enter your choice (1, 2, or 3): ");
        return scanner.nextInt();
    }





}
