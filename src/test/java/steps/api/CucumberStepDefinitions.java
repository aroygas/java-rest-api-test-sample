package steps.api;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import utils.ConfigReader;

import java.util.Properties;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * All Cucumber steps are implemented in this class.
 */
public class CucumberStepDefinitions {
    private final String hostAPIBaseURL;
    private final String personalAPIToken;
    private Response response;

    /**
     * Constructor reads property file to set values for private fields
     * @throws Exception - in case property file is missing
     */
    public CucumberStepDefinitions() throws Exception {
        Properties properties = new ConfigReader().getPropValues();
        hostAPIBaseURL = properties.getProperty("hostAPIBaseURL");
        personalAPIToken = properties.getProperty("personalAPIToken");
    }

    /**
     * Create a brand new task with only one field set
     * @param taskName - value for "content" field
     */
    @When("I create a task named {string}")
    public void iCreateATaskNamed(String taskName) {
        response = createANewTaskRequest("{\"content\": \"" + taskName + "\"}");
    }

    /**
     * Create a new task with any parameters set
     * @param body - JSON with tasks parameters
     */
    @When("I create a task with data:")
    public void iCreateATaskWithData(String body) {
        response = createANewTaskRequest(body);
    }

    /**
     * Verify that task with name specified exists
     * @param taskName - a name of a task that should be found in response body
     */
    @Then("I should see created task named {string} in tasks list")
    public void iShouldSeeCreatedTaskNamedInTasksList(String taskName) {
        given().
                auth().oauth2(personalAPIToken).
                get(hostAPIBaseURL+"tasks").
                then().
                assertThat().body(containsString(taskName));
    }

    /**
     * According to Cucumber philosophy checks should be done in separate steps that start with @Then annotation
     * Check that actual code matches
     * @param statusCode - desired status code
     */
    @Then("I should see a {int} status code in response")
    public void iShouldSeeAStatusCodeInResponse(int statusCode) {
        response.then().assertThat().statusCode(statusCode);
    }

    /**
     * Method that actually sends a Create New Task POST request with
     * @param body - parameters of a new task
     * @return response object
     */
    private Response createANewTaskRequest(String body) {
        return given().
                auth().oauth2(personalAPIToken).
                contentType(JSON).
                body(body).
                post(hostAPIBaseURL+"tasks");
    }
}
