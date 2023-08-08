import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CommentaryTests extends BaseTests{

    @Test
    public void submit_comment_should_make_comment_visible(){
        String adminMeetingUrl = baseUrl + "/admin/" + createPoll();
        driver.get(adminMeetingUrl);

        //String participantUrl = wait.until(
        // ExpectedConditions.presenceOfElementLocated(By.cssSelector("[value*='" + baseUrl + "/p/']")))
        // .getDomAttribute("value");
        String userMeetingUrl = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.p-4 input")))
                .getDomProperty("value");
        driver.get(userMeetingUrl);

        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#comment")))
                .sendKeys("To jest pierwsza testowa wiadomosc");
        driver.findElement(By.name("authorName")).sendKeys("ledzior");
        driver.findElement(By.cssSelector("form.bg-white.p-3 button")).click();

        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("button.pointer-events-none"),0));

        // moja wersja duzo bardziej toporna - porownuje tresc wiadomosci
        /*WebElement comment = driver.findElement(By.cssSelector(".bg-pattern.space-y-3.border-b.p-3>div:last-of-type .w-fit.whitespace-pre-wrap"));
        Assertions.assertEquals("To jest pierwsza testowa wiadomosc", comment.getText(),
                "The comment not created properly");*/
        Assertions.assertEquals(1,
                driver.findElements(By.cssSelector("[data-testid=comment]")).size(),
                "The number of added comments is not what expected/");

        deletePoll(adminMeetingUrl);
    }
}
