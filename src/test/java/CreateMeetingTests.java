import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class CreateMeetingTests extends BaseTests{

    By continueButton = By.className("btn-primary");
    By titleField = By.cssSelector("#title");
    By timeField = By.cssSelector("button[id*=headlessui-listbox-button]");

    @Test
    public void add_meeting_title_should_show_second_step(){
        driver.get(baseUrl + "/new");
        wait.until(ExpectedConditions.presenceOfElementLocated(titleField)).sendKeys("Review Meeting");
        //wait.until(driver -> driver.findElement(By.id("title"))).sendKeys("Review Meeting");
        driver.findElement(continueButton).click();

        WebElement progressDescription = driver.findElement(By.className("tracking-tight"));
        //Assertions.assertDoesNotThrow(() -> progressDescription, "Progress description is missing");
        Assertions.assertEquals("Etap 2 z 3", progressDescription.getText(),
                "We are not in second step");

    }

    @Test
    public void new_event_should_show_proper_placeholders(){
        driver.get(baseUrl + "/new");
        WebElement titleField = wait.until(driver -> driver.findElement(By.id("title")));
        WebElement locationField = wait.until(driver -> driver.findElement(By.id("location")));
        WebElement descriptionField = wait.until(driver -> driver.findElement(By.id("description")));

        Assertions.assertAll(
                () -> Assertions.assertEquals("Miesięczne spotkanie", titleField.getDomAttribute("placeholder"),
                        "Title placeholder has wrong value"),
                () -> Assertions.assertEquals("Sklep z kawą Joe", locationField.getDomAttribute("placeholder"),
                        "Localization placeholder has wrong value"),
                () -> Assertions.assertEquals("Cześć wszystkim, wybierzcie terminy, które Wam pasują!", descriptionField.getDomAttribute("placeholder"),
                        "Description placeholder has wrong value")
        );


    }

    @Test
    public void input_minimal_information_should_create_survey(){
        //creating_poll_should_show_copy_link_button()
        driver.get(baseUrl + "/new");
        //wait.until(ExpectedConditions.presenceOfElementLocated(titleField)).sendKeys("Review Meeting");
        driver.findElement(titleField).sendKeys("Review Meeting");
        driver.findElement(continueButton).click();

        //Posługiwanie się kolejnością elementów,
        //a to robimy poniżej, nie jest dobrą praktyką.
        //Gdy poznamy selektory CSS i XPatha, będziemy to robić trochę inaczej.
        driver.findElements(By.className("z-10")).get(5).click();
        // tu moja, gorsza wersja
        //wait.until(ExpectedConditions.presenceOfElementLocated(By.className("border-r"))).click();
        driver.findElement(continueButton).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name"))).sendKeys("Tester");
        driver.findElement(By.id("contact")).sendKeys("test@test.com");
        driver.findElement(continueButton).click();

        //Tutaj podobnie: znowu posługujemy się kolejnością, bo inaczej nie umiemy
        WebElement copyLinkButton = wait.until(ExpectedConditions
                .numberOfElementsToBe(continueButton, 3)).get(1);

        Assertions.assertEquals("Kopiuj link", copyLinkButton.getText(),
                "Text of the button is now what expected. ");

        // tutaj moja, gorsza wersja
        /*WebElement surveyField = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("p-4")));
        Assertions.assertTrue(surveyField.isDisplayed(),
                "Survey has not been created properly - there is no \"copy link\" field");*/
    }

    @Test
    public void apply_to_all_dates_should_change_dates_for_all(){
        driver.get(baseUrl + "/new");
        //wait.until(ExpectedConditions.presenceOfElementLocated(titleField)).sendKeys("Review Meeting");
        driver.findElement(titleField).sendKeys("Review Meeting");
        driver.findElement(continueButton).click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//span[text()=10]"))).click();
        driver.findElement(By.xpath(".//span[text()=11]")).click();
        driver.findElement(By.xpath(".//span[text()=15]")).click();

        driver.findElement(By.cssSelector("[data-testid='specify-times-switch']")).click();
        //driver.findElement(By.id("headlessui-switch-:r8:")).click(); // dlaczego nie te id ???

        String startTime = "13:00";
        String endTime = "13:30";

        List<WebElement> timeFields = driver.findElements(timeField);

        timeFields.get(0).click();
        //wait.until(ExpectedConditions.presenceOfElementLocated(By.id("headlessui-listbox-button-:ra:"))).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//span[text()='" + startTime + "']"))).click();
        //driver.findElement(By.id("headlessui-listbox-button-:rc:")).click();
       //wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//span[text()='13:30']"))).click();

        //driver.findElement(By.cssSelector("[id='headlessui-menu-button-:re:']>button")).click();
        driver.findElements(By.cssSelector("[id*=headlessui-menu-button] button:not([role])")).get(0).click();
        //wait.until(ExpectedConditions.presenceOfElementLocated(By.id("headlessui-menu-item-:r73:"))).click();
        driver.findElements(By.cssSelector("[role='menuitem']")).get(0).click();

        List<WebElement> finalTimeFields =
                driver.findElements(timeField);

        /*Assertions.assertEquals(6,driver.findElements(By.xpath(".//span[contains(text(),'13:')]")).size(),
                "Not all dates have updated");*/

        Assertions.assertAll(
                () -> Assertions.assertEquals(startTime, finalTimeFields.get(0).getText(),
                        "Starting time of the first date is not what expected."),
                () -> Assertions.assertEquals(endTime, finalTimeFields.get(1).getText(),
                        "Ending time of the first date is not what expected."),
                () -> Assertions.assertEquals(startTime, finalTimeFields.get(2).getText(),
                        "Starting time of the second date is not what expected."),
                () -> Assertions.assertEquals(endTime, finalTimeFields.get(3).getText(),
                        "Ending time of the second date is not what expected."),
                () -> Assertions.assertEquals(startTime, finalTimeFields.get(4).getText(),
                        "Starting time of the third date is not what expected."),
                () -> Assertions.assertEquals(endTime, finalTimeFields.get(5).getText(),
                        "Ending time of the third date is not what expected.")
        );
    }
}
