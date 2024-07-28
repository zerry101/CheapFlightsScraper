package Test1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

public class BestFlightFinder {

    public static void findBestFlight(String departureCity, String arrivalCity, List<String> csvFiles, String airline) {
        String bestFlight = null;
        double minPrice = Double.MAX_VALUE;
        Duration minDuration = Duration.ofDays(1); // initialize with a large duration

        for (String csvFile : csvFiles) {
            try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    if (values.length < 6) {
                        continue; // skip invalid rows
                    }
                    String departureTimeStr = values[0].trim();
                    String arrivalTimeStr = values[1].trim();
                    double price = parsePrice(values[2].trim());
                    String flightAirline = values[3].trim().toLowerCase();
                    String departure = values[4].trim().toLowerCase();
                    String arrival = values[5].trim().toLowerCase();

                    try {
                        int departureTime = convertTo24HourFormat(departureTimeStr);
                        int arrivalTime = convertTo24HourFormat(arrivalTimeStr);
                        Duration duration = calculateDuration(departureTime, arrivalTime);

                        if (departure.equals(departureCity.toLowerCase()) && arrival.equals(arrivalCity.toLowerCase())) {
                            if (airline == null || airline.equals(flightAirline)) {
                                if ((price < minPrice) || (price == minPrice && duration.compareTo(minDuration) < 0)) {
                                    minPrice = price;
                                    minDuration = duration;
                                    bestFlight = line;
                                }
                            }
                        }
                    } catch (Exception e) {
                        // Continue to next line if there's an error parsing times
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (bestFlight != null) {
            System.out.println("\nBest flight:");
            System.out.println(bestFlight);
            System.out.println("Duration of flight: " + minDuration.toMinutes() + " minutes");
        } else {
            System.out.println("\nNo flights found matching the criteria.");
        }
    }

    private static double parsePrice(String priceStr) {
        // Remove any non-numeric characters except for the decimal point
        String cleanedPriceStr = priceStr.replaceAll("[^\\d.]", "");
        return Double.parseDouble(cleanedPriceStr);
    }

    private static int convertTo24HourFormat(String timeStr) {
        String[] parts = timeStr.split(" ");
        String timePart = parts[0];
        String periodPart = parts[1];

        String[] timeParts = timePart.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        if (periodPart.equalsIgnoreCase("PM") && hour != 12) {
            hour += 12;
        } else if (periodPart.equalsIgnoreCase("AM") && hour == 12) {
            hour = 0;
        }

        return hour * 60 + minute;
    }

    private static Duration calculateDuration(int departureTime, int arrivalTime) {
        if (arrivalTime < departureTime) {
            // Arrival time is on the next day
            arrivalTime += 24 * 60;
        }
        return Duration.ofMinutes(arrivalTime - departureTime);
    }
}
