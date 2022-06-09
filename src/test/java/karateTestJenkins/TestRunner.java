package karateTestJenkins;

import com.intuit.karate.junit5.Karate;

public class TestRunner {

	@Karate.Test
	Karate simpleTest() {

		return Karate.run("HelloWorld").relativeTo(getClass());
	}
}
