package Test1;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.annotation.concurrent.ThreadSafe;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ScrapeFlights {

    // WebDriver instance to control the browser

    private int type;
    private WebDriver driver;

    private String from;
    private String to;


    // WebDriverWait instance to wait for elements to be visible
    private WebDriverWait wait;

    // Lists to store flight data
    private List<String> departingFlightTimes;
    private List<String> arrivingFlightTimes;
    private List<String> flightPrices;
    private List<String> flightCompanies;

    private List<String> flightDepartingLocation;
    private List<String> flightArrivingLocation;

    // Constructor to initialize the WebDriver and WebDriverWait
    public ScrapeFlights(WebDriver driver,int type,String from, String to){
        this.type=type;
        this.driver = driver;
        this.from=from;
        this.to=to;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Initialize the lists
        this.departingFlightTimes = new ArrayList<>();
        this.arrivingFlightTimes = new ArrayList<>();
        this.flightPrices = new ArrayList<>();
        this.flightCompanies = new ArrayList<>();
        this.flightDepartingLocation = new ArrayList<>();
        this.flightArrivingLocation = new ArrayList<>();
    }

    // Method to scrape flight data
    public void scrapeFlights() throws InterruptedException {
        // Get all window handles
        Set<String> allWindowHandles = driver.getWindowHandles();
        List<String> windowHandlesList = new ArrayList<>(allWindowHandles);

        // Switch to the second window if it exists
        if (windowHandlesList.size() > 1) {
            String secondWindowHandle = windowHandlesList.get(1);
            driver.switchTo().window(secondWindowHandle);
        }


        WebElement wE = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body")));

        // Further actions after page has loaded
        // Example: Get page title
//        String pageTitle = driver.getTitle();
//        System.out.println("title is "+pageTitle);

        if(type==1){
            WebElement flightType=wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[aria-label='Quickest']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", flightType);

            flightType.click();
            System.out.println("Parsing quickest flights");
        }
        if(type==2){
            WebElement flightType=wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[aria-label='Best']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", flightType);
            flightType.click();
            System.out.println("Parsing Best flights");
        }
        if(type==3){
            WebElement flightType=wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[aria-label='Cheapest']")));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", flightType);
            flightType.click();
            System.out.println("Parsing Cheapest flights");


        }



        for(int i=0;i<=5;i++){
            WebElement showMoreBtn=wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".show-more-button")));
         showMoreBtn.click();
//            showMoreBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".show-more-button")));


        }




        Thread.sleep(1000);






        Thread.sleep(3000);
        // Wait for the flight data table to be visible
        WebElement flightDataTable = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".c-OCp")));

        // Fetch corresponding departing flight elements
        List<WebElement> departingFlightTimes = flightDataTable.findElements(By.cssSelector("div.vmXl.vmXl-mod-variant-large > span:nth-child(1)"));

        // Fetch corresponding arriving flight elements
        List<WebElement> arrivingFlightTimes = flightDataTable.findElements(By.cssSelector("div.vmXl.vmXl-mod-variant-large > span:nth-child(3)"));

        // Fetch corresponding flight prices elements
        List<WebElement> flightPrices = flightDataTable.findElements(By.cssSelector(".f8F1-price-text"));

        // Fetch corresponding flight company elements
        List<WebElement> flightCompanies = flightDataTable.findElements(By.cssSelector(".J0g6-operator-text"));

        // Lists to store the text values of the elements
        List<String> departingFlightTimesList = new ArrayList<>();
        List<String> arrivingFlightTimesList = new ArrayList<>();
        List<String> flightPricesList = new ArrayList<>();
        List<String> flightCompaniesList = new ArrayList<>();



        // Test data elements for flight times
        List<WebElement> testData = flightDataTable.findElements(By.cssSelector(" .hJSA-list > li.hJSA-item:nth-child(1) "));

        // Print and store departing flight times
//        System.out.println("\n\n-----DEPARTING FLIGHTS TIMINGS------");
        for (WebElement element : testData) {
            WebElement departingEle = element.findElement(By.cssSelector("div.vmXl.vmXl-mod-variant-large > span:nth-child(1) "));
//            System.out.println(departingEle.getText());
            departingFlightTimesList.add(departingEle.getText());
        }



        // Print and store flight companies
//        System.out.println("------FLIGHT TYPES LIST-----");
        for (WebElement element : flightCompanies) {
//            System.out.println(element.getText());
//            flightCompaniesList.add(element.getText().split("\\s", 2)[0]);
            flightCompaniesList.add(element.getText());
        }

        // Print and store arriving flight times
//        System.out.println("\n\n-----ARRIVING FLIGHTS TIMINGS------");
        for (WebElement element : testData) {
            WebElement arrivingEle = element.findElement(By.cssSelector("div.vmXl.vmXl-mod-variant-large > span:nth-child(3) "));
            String ele = arrivingEle.getText().replaceAll("\\+\\d+", "").trim();
//            System.out.println(ele);
            arrivingFlightTimesList.add(ele);
        }

        // Print and store flight prices
//        System.out.println("\n\n-----FLIGHTS PRICINGS------");
        for (WebElement element : flightPrices) {
            String ele = element.getText().replaceAll("[^0-9]", "")+" CAD";
//            System.out.println(ele);
            flightPricesList.add(ele);
        }

        for(int i=0;i<flightPrices.size();i++){
            this.flightDepartingLocation.add(from);
            this.flightArrivingLocation.add(to);
        }

        // Assign the lists to the class variables
        this.departingFlightTimes = departingFlightTimesList;
        this.arrivingFlightTimes = arrivingFlightTimesList;
        this.flightPrices = flightPricesList;
        this.flightCompanies = flightCompaniesList;
    }

    // Getter methods to retrieve the flight data
    public List<String> getArrivingFlightTimes() {
        return arrivingFlightTimes;
    }

    public List<String> getFlightCompanies(){
        return flightCompanies;
    }

    public List<String> getFlightPrices() {
        return flightPrices;
    }

    public List<String> getDepartingFlightTimes() {
        return departingFlightTimes;
    }

    public List<String> getFlightDepartingLocation() {return flightDepartingLocation;}

    public List<String> getFlightArrivingLocation() {return flightArrivingLocation;}
}
