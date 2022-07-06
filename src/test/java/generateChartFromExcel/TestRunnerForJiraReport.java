package generateChartFromExcel;

import com.intuit.karate.junit5.Karate;

public class TestRunnerForJiraReport {
	
	@Karate.Test
	Karate simpleTest() {
	return Karate.run("fetchJira.feature").relativeTo(getClass());
	
	}

}