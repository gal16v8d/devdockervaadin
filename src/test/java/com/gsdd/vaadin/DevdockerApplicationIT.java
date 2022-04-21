package com.gsdd.vaadin;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

class DevdockerApplicationIT {

    private static final String SERVER_URL = "http://127.0.0.1:8097/";
    private static HtmlUnitDriver driver;

    @BeforeEach
    void init() throws Exception {
        driver = new HtmlUnitDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterEach
    void close() throws Exception {
        driver.close();
    }

    @Test
    void enteringReservationPage_newReservationButtonIsEnabled() throws Exception {
        driver.get(SERVER_URL);
        Wait<WebDriver> waitFluent =
                new FluentWait<WebDriver>(driver)
                        .withTimeout(Duration.ofSeconds(20))
                        .pollingEvery(Duration.ofSeconds(1));
        List<WebElement> refList = driver.findElements(By.tagName("a"));
        for (WebElement we : refList) {
            if ("Continue without updating".equals(we.getText())) {
                WebElement anchorToClick =
                        waitFluent.until(ExpectedConditions.elementToBeClickable(we));
                Assertions.assertTrue(
                        (waitFluent.until(ExpectedConditions.elementToBeClickable(we)) != null),
                        "Anchor to click for not supported browser page not present.");
                anchorToClick.click();
            }
        }
    }
}
