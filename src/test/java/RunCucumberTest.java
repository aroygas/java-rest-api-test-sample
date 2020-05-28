import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * This class is needed to run Cucumber tests with JUnit
*/
@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features",
        glue = "steps.api")
public class RunCucumberTest {
}