package mission;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/java",
        glue = {"mission"},
        plugin = {
                "pretty",
                "html:test-output/cucumber-reports.html",
                "json:test-output/CucumberTestReport.json",
                "rerun:test-output/rerun.txt"
        }
)
public class RunnerTest extends AbstractTestNGCucumberTests {
        // Modern Cucumber 7 - no additional code needed!
}
