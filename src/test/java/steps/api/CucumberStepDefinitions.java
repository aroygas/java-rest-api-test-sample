package steps.api;

import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import utils.ConfigReader;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

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
     * Cucumber "Before" hook that is executed before each suite run.
     */
    @Before
    public void setUp(){
        iCleanUpTasks();
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
     * According to Cucumber philosophy checks should be done in separate steps that start with @Then annotation
     * Check that actual code matches
     * @param statusCode - desired status code
     */
    @Then("I should see a {int} status code in response")
    public void iShouldSeeAStatusCodeInResponse(int statusCode) {
        response.then().assertThat().statusCode(statusCode);
    }

    /**
     * Delete all tasks that might have been created during this test
     */
    @When("I clean up test tasks")
    public void iCleanUpTasks() {
        String[] createdTestTasksNames = {
                "Simple task",
                "Complex task",
                "Due string",
                "Due datetime",
                "01234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123457012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234501234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123450123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234501234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123450123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234",
                "'`@#$%^&*()_+><:\"}{?:",
                "Due date in far future",
                "Due date in far past",
                "Invalid section id",
                "Invalid order",
                "Invalid due_lang"
        };
        cleanUpTasksWithNames(createdTestTasksNames);
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

    /**
     * Get a list of all active tasks
     * @return - Response object
     */
    private Response getAllActiveTasksList () {
        RestAssured.defaultParser = Parser.JSON;
        return given().
                auth().oauth2(personalAPIToken).
                get(hostAPIBaseURL+"tasks").
                then().contentType(JSON).extract().response();
    }

    /**
     * Delete a task with id
     * @param id - id of a task to be deleted
     */
    private void deleteTaskById (long id) {
        given().
                auth().oauth2(personalAPIToken).
                delete(hostAPIBaseURL+"tasks/" + id).
                then().assertThat().statusCode(204);
    }

    /**
     * Method that actually deletes tasks by id when if name matches one from the list
     * @param taskNames - list of task"content" field values to be matched
     */
    private void cleanUpTasksWithNames(String[] taskNames) {
        response = getAllActiveTasksList();
        List<String> jsonResponse = response.jsonPath().getList("$");

        for (int i = 0; i < jsonResponse.size(); i++) {
            String task_name = response.jsonPath().getString("content[" + i + "]");
            String task_id = response.jsonPath().getString("id[" + i + "]");
            if (Arrays.asList(taskNames).contains(task_name)) {
                System.out.println(task_name + " : " + task_id);
                deleteTaskById(Long.parseLong(task_id));
            }
        }
    }
}
