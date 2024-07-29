package Test1;


import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class KayakScraper {
    public static void main(String[] args) throws InterruptedException {
        // Set the path to your WebDriver executable
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\macon\\Downloads\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");

        // Initialize WebDriver
//        ChromeOptions options = new ChromeOptions();
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        driver.manage().window().maximize();


        // Navigate to KAYAK flight search page
        driver.get("https://www.ca.kayak.com/flights");

        // Specify search details
        String origin = "Toronto";
        String destination = "New York";
        LocalDate departureDate = LocalDate.of(2024, 7, 12);
        LocalDate returnDate = LocalDate.of(2024, 7, 19);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEE M/d", Locale.ENGLISH); // Adjust the pattern as needed

        // Enter origin
        WebElement originBox = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[aria-label='Flight origin input']")));

        originBox.click();
        originBox.sendKeys(origin);
        Thread.sleep(500);
        originBox.sendKeys(Keys.ARROW_DOWN);
        originBox.sendKeys(Keys.RETURN);

        // Enter destination
        WebElement destinationBox = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("input[aria-label='Flight destination input']")));
        destinationBox.click();
        destinationBox.sendKeys(destination);

        Thread.sleep(500);
        destinationBox.sendKeys(Keys.ARROW_DOWN);

        destinationBox.sendKeys(Keys.ENTER);

        WebElement departureDateBox = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div[aria-label='Start date']")));
        departureDateBox.click();


        WebElement searchBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@type='submit']")));
        searchBtn.click();
        Thread.sleep(1000);

        Set<String> windowHandles = driver.getWindowHandles();

        List<String> windowHandlesList = new ArrayList<>(windowHandles);
        String secondWindowHandle = null;
        if (windowHandlesList.size() > 1) {
            secondWindowHandle = windowHandlesList.get(1);
            driver.switchTo().window(secondWindowHandle);
        }

        Thread.sleep(2000);
        WebElement FlightDataTable = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".c-OCp")));
        List<WebElement> flightDurations = FlightDataTable.findElements(By.cssSelector(".vmXl"));
        List<WebElement> flightPrices=FlightDataTable.findElements(By.cssSelector(".f8F1-price-text"));
        List<String> flightPricesList=new ArrayList<>();
        List<String> flightDurationList=new  ArrayList<>();
        System.out.println(flightDurations.size());

        for (WebElement element:flightDurations)
        {
            System.out.println(element.getText());
            flightDurationList.add(element.getText());
        }

        for (WebElement element:flightPrices){
            System.out.println(element.getText());
            flightPricesList.add(element.getText());

        }



        writeListsToCSV("collect_data.csv",flightDurationList,flightPricesList);


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
            // Write header (optional, customize as needed)
            // csvWriter.append("Departing Time,Arriving Time,Price\n");

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


}