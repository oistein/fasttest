package fasttest.matchers;

import fasttest.InMemoryMatcher;

public class NotMatcher implements InMemoryMatcher {

	private final InMemoryMatcher matcher;

	public NotMatcher(InMemoryMatcher matcher) {
		this.matcher = matcher;
	}

	public boolean matches(Object object) {
		return !matcher.matches(object);
	}


}
