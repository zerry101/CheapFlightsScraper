package Test1;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
// zishan
/**
 * This class represents the Search Page functionalities for flight search.
 */
public class SearchPage {

    // WebDriver instance to control the browser

    private String from;

    private String to;
    private WebDriver driver;
    private int type;
    // WebDriverWait instance to wait for elements to be visible
    private WebDriverWait wait;

    /**
     * Constructor to initialize the WebDriver and WebDriverWait.
     * @param driver WebDriver instance passed from main class for browser control.
     */
    public SearchPage(WebDriver driver,String from,String to){

        this.to=to;
        this.from=from;
        this.driver = driver;
        // Initialize WebDriverWait with a timeout of 10 seconds
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    /**
     * Method to perform flight search.
     * @throws InterruptedException if any thread has interrupted the current thread.
     */
    public void searchFlights() throws InterruptedException {
        // Wait for the flight origin input field to be visible
        WebElement inputField1 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[aria-label='Flight origin input']")));

        // Enter the flight origin as "DXB" (Dubai), navigate through the suggestions, and select the first one
        inputField1.sendKeys(from);
        inputField1.sendKeys(Keys.ARROW_DOWN); // Navigate down in the suggestions
        Thread.sleep(500); // Pause for a moment to allow suggestion to be selected
        inputField1.sendKeys(Keys.RETURN); // Select the highlighted suggestion by pressing Enter

        // Wait for the flight destination input field to be visible
        WebElement inputField2 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[aria-label='Flight destination input']")));

        // Enter the flight destination as "AMD" (Ahmedabad), navigate through the suggestions, and select the first one
        inputField2.sendKeys(to);
        inputField2.sendKeys(Keys.ARROW_DOWN); // Navigate down in the suggestions
        Thread.sleep(500); // Pause for a moment to allow suggestion to be selected
        inputField2.sendKeys(Keys.RETURN); // Select the highlighted suggestion by pressing Enter

        // Find the search button and click it to initiate the search
        WebElement search = driver.findElement(By.xpath("//button[@type='submit']"));
        search.click();

        // Wait for a second to ensure the new tab has opened

        System.out.println("You have selected:");
        System.out.println("Departure City: " + from );
        System.out.println("Arriving City: " + to );

        Thread.sleep(3000);
    }
}
