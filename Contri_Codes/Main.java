package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Specify the directory for your ChromeDriver executable
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Umesh Maurya\\IdeaProjects\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");

        // WebDriver initialization
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(50));
        driver.manage().window().fullscreen();

        try {
            // Visit the Expedia flight search page
            driver.get("https://www.travelocity.ca/Flights-Search?flight-type=on&mode=search&trip=oneway&leg1=from%3AToronto%2C%20ON%2C%20Canada%20%28YYZ-Pearson%20Intl.%29%2Cto%3ALondon%2C%20United%20Kingdom%20%28LHR-Heathrow%29%2Cdeparture%3A31%2F08%2F2024TANYT%2CfromType%3AA%2CtoType%3AA&options=cabinclass%3Aeconomy&fromDate=31%2F08%2F2024&d1=2024-8-31&passengers=adults%3A1%2Cinfantinlap%3AN&pwaDialog=clientSideErrorDialog");

            // File access
            Path file = Paths.get("C:/Users/Umesh Maurya/IdeaProjects/Travelocity_flight/travelocity.csv");
            Files.deleteIfExists(file);
            System.out.println("File deleted successfully.");

            try (FileWriter fileWriter = new FileWriter(file.toString(), true)) {
                // Write the CSV header
                fileWriter.append("For June 30\n");
                fileWriter.append("From,To,Airline,Price,Departure Time\n");

                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test-id='offer-listing']")));
                List<WebElement> flights = driver.findElements(By.cssSelector("[data-test-id='offer-listing']"));

                for (WebElement flight : flights) {
                    try {
                        WebElement departureTime = flight.findElement(By.cssSelector("[data-test-id='arrival-time']"));
                        WebElement airline = flight.findElement(By.cssSelector("div[data-test-id='flight-operated']"));
                        WebElement price = flight.findElement(By.cssSelector("span.uitk-lockup-price"));

                        String airlineName = airline.getText();
                        String[] airlineWords = airlineName.split("\\s+");
                        StringBuilder shortenedAirline = new StringBuilder();
                        for (int i = 0; i < Math.min(4, airlineWords.length); i++) {
                            if (i > 0) shortenedAirline.append(" ");
                            shortenedAirline.append(airlineWords[i]);
                        }

                        String formattedPrice = price.getText().replace(",", ""); // Ensure the price is without commas
                        String formattedLine = String.format("Toronto-Pearson,London,%s,%s,%s", shortenedAirline.toString(), formattedPrice, departureTime.getText());

                        System.out.println("Departure Time: " + departureTime.getText());
                        System.out.println("Airline: " + shortenedAirline);
                        System.out.println("Price: " + formattedPrice);

                        fileWriter.write(formattedLine + "\n");
                    } catch (Exception e) {
                        // Skip the flight if any element is not found
                    }
                }

                // Scroll and fetch more flights
                int scrollCount = 0;
                int scrollLimit = 3;
                while (scrollCount < scrollLimit) {
                    ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, document.body.scrollHeight / 2);");
                    Thread.sleep(5000);
                    flights = driver.findElements(By.cssSelector("[data-test-id='offer-listing']"));

                    for (WebElement flight : flights) {
                        try {
                            WebElement departureTime = flight.findElement(By.cssSelector("[data-test-id='arrival-time']"));
                            WebElement airline = flight.findElement(By.cssSelector("div[data-test-id='flight-operated']"));
                            WebElement price = flight.findElement(By.cssSelector("span.uitk-lockup-price"));

                            String airlineName = airline.getText();
                            String[] airlineWords = airlineName.split("\\s+");
                            StringBuilder shortenedAirline = new StringBuilder();
                            for (int i = 0; i < Math.min(4, airlineWords.length); i++) {
                                if (i > 0) shortenedAirline.append(" ");
                                shortenedAirline.append(airlineWords[i]);
                            }

                            String formattedPrice = price.getText().replace(",", ""); // Ensure the price is without commas
                            String formattedLine = String.format("Toronto-Pearson,London,%s,%s,%s", shortenedAirline.toString(), formattedPrice, departureTime.getText());

                            System.out.println("Departure Time: " + departureTime.getText());
                            System.out.println("Airline: " + shortenedAirline);
                            System.out.println("Price: " + formattedPrice);

                            fileWriter.write(formattedLine + "\n");
                        } catch (Exception e) {
                            // Skip the flight if any element is not found
                        }
                    }
                    scrollCount++;
                }

                // Close the file writer
                fileWriter.flush();

            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // Close the WebDriver
            driver.quit();
        }
    }
}