package fasttest.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class NotMatcher extends BaseMatcher<Object> {

	private final Matcher<Object> matcher;

	public NotMatcher(Matcher<Object> matcher) {
		this.matcher = matcher;
	}

	public boolean matches(Object object) {
		return !matcher.matches(object);
	}

	public void describeTo(Description arg0) {
		
	}

}
