package Test1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BestFlightFinder {

    public static void findBestFlight(String departureCity, String arrivalCity, List<String> csvFiles, String airline) {
        String bestFlight = null;
        double minPrice = Double.MAX_VALUE;
        Duration minDuration = Duration.ofDays(1); // initialize with a large duration

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        for (String csvFile : csvFiles) {
            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    if (values.length < 5) {
                        continue; // skip invalid rows
                    }

                    String departure = values[0].trim().toLowerCase();
                    String arrival = values[1].trim().toLowerCase();
                    double price = parsePrice(values[2].trim());
                    String flightDuration = values[3].trim();
                    String flightAirline = values[4].trim().toLowerCase();

                    if (departure.equals(departureCity) && arrival.equals(arrivalCity)) {
                        Duration duration = parseDuration(flightDuration);

                        if (airline == null || airline.equals(flightAirline)) {
                            if ((price < minPrice) || (price == minPrice && duration.compareTo(minDuration) < 0)) {
                                minPrice = price;
                                minDuration = duration;
                                bestFlight = line;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (bestFlight != null) {
            System.out.println("\nBest flight:");
            System.out.println(bestFlight);
        } else {
            System.out.println("\nNo flights found matching the criteria.");
        }
    }

    private static double parsePrice(String priceStr) {
        // Remove any non-numeric characters except for the decimal point
        String cleanedPriceStr = priceStr.replaceAll("[^\\d.]", "");
        return Double.parseDouble(cleanedPriceStr);
    }

    private static Duration parseDuration(String flightDuration) {
        String[] parts = flightDuration.split("h|m");
        int hours = Integer.parseInt(parts[0].trim());
        int minutes = Integer.parseInt(parts[1].trim());
        return Duration.ofHours(hours).plusMinutes(minutes);
    }
}
