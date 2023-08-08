import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class BaseTests {
    protected WebDriver driver;
    protected WebDriverWait wait;
    protected final String baseUrl = "http://localhost:3000";

    protected String createPoll() {
        HttpClient client = HttpClient.newHttpClient();
        String urlId = null;
        String body = """
                {
                  "0": {
                    "json": {
                      "title": "Monthly coffee!",
                      "location": "Kitchen",
                      "description": "",
                      "user": {
                        "name": "Emilia",
                        "email": "test@test.com"
                      },
                      "timeZone": "Europe/Madrid",
                      "options": [
                        {
                          "startDate": "2023-07-11T13:00:00",
                          "endDate": "2023-07-11T13:30:00"
                        }
                      ]
                    }
                  }
                }
                """;
        try {
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .uri(URI.create(baseUrl + "/api/trpc/polls.create?batch=1"))
                    .header("accept", "application/json")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONArray jsonArray = new JSONArray(response.body());
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            urlId = jsonObject.getJSONObject("result")
                    .getJSONObject("data")
                    .getJSONObject("json")
                    .getString("urlId");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return urlId;
    }

    protected void deletePoll(String urlId) {
        HttpClient client = HttpClient.newHttpClient();
        String body = "{\n" +
                "  \"0\": {\n" +
                "    \"json\": {\n" +
                "      \"urlId\": \"" + urlId + "\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
        try {
            HttpRequest request = HttpRequest
                    .newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .uri(URI.create(baseUrl + "/api/trpc/polls.delete?batch=1"))
                    .header("accept", "application/json")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.http.factory", "jdk-http-client");

        /*ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        driver = new ChromeDriver(options);*/
        driver = new ChromeDriver();

        wait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    @AfterEach
    public void quitDriver() {
        driver.quit();
    }
}
