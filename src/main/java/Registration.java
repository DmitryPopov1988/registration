import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.sound.sampled.LineUnavailableException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Registration {

    public static void main(String[] args) {
        registerMe();

    }

    private static void registerMe() {


        try {

            ChromeDriver driver = initWebDriver();


            parseSite(driver);
        } catch (Exception e) {

            registerMe();
        }

    }


    private static ChromeDriver initWebDriver() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-gpu");

        WebDriverManager.chromedriver().forceDownload()
                .setup();

        ChromeDriver driver = new ChromeDriver(chromeOptions);

        driver.manage()
                .window()
                .maximize();

        driver.get("https://www.bezkolejki.eu/luwlodz");
        driver.manage()
                .timeouts()
                .implicitlyWait(Duration.ofSeconds(5));

        return driver;
    }

    private static void parseSite(ChromeDriver driver) throws LineUnavailableException, InterruptedException {

        try {

            Thread.sleep(2000L);



            WebElement kartaPolakaButton = driver.findElement(
                    By.xpath("//*[text()='Pobyt - Złożenie wniosku - pobyt stały dla posiadacza Karty Polaka']"));
            kartaPolakaButton.click();

            WebElement dalejButton = driver.findElement(By
                    .xpath("//button[@class='btn footer-btn btn-secondary btn-lg btn-block' and contains(.,'Dalej')]"));

            WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(20));

            new Actions(driver).moveToElement(webDriverWait.until(ExpectedConditions.elementToBeClickable(dalejButton)))
                    .click()
                    .build()
                    .perform();

            dalejButton.sendKeys(Keys.RETURN);

            Thread.sleep(3000L);

            if (hasNoFreeSlots(driver)) {

                driver.navigate()
                        .refresh();

                parseSite(driver);

            } else {
                System.out.println("Free slots were " + LocalDateTime.now());
                notifyMe();
            }

        } catch (Exception e) {

            parseSite(driver);

        }


    }

    private static boolean hasNoFreeSlots(ChromeDriver driver) {
        return driver.getPageSource()
                .contains("Brak dostępnych");
    }

    private static void notifyMe() throws LineUnavailableException, InterruptedException {

        SoundUtils.tone(5000, 500);
        Thread.sleep(5000L);
        notifyMe();
    }

}