package fasttest.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class LogicalMatcher extends BaseMatcher<Object> {

	private final Matcher<Object> lhs;
	private final Matcher<Object> rhs;
	private final String op;

	public LogicalMatcher(Matcher<Object> lhs, Matcher<Object> rhs, String op) {
		this.lhs = lhs;
		this.rhs = rhs;
		this.op = op;
	}

	public boolean matches(Object item) {
		return "or".equals(op) ? lhs.matches(item) || rhs.matches(item) :
			                     lhs.matches(item) && rhs.matches(item);
	}

	public void describeTo(Description arg0) {
	}

}
