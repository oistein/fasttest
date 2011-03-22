package fasttest.matchers;

import fasttest.InMemoryMatcher;

public class LogicalMatcher implements InMemoryMatcher {

	private final InMemoryMatcher lhs;
	private final InMemoryMatcher rhs;
	private final String op;

	public LogicalMatcher(InMemoryMatcher lhs, InMemoryMatcher rhs, String op) {
		this.lhs = lhs;
		this.rhs = rhs;
		this.op = op;
	}

	public boolean matches(Object item) {
		return "or".equals(op) ? lhs.matches(item) || rhs.matches(item) :
			                     lhs.matches(item) && rhs.matches(item);
	}


}
