package fasttest.matchers;

import java.util.Arrays;
import java.util.List;

import org.fest.reflect.core.Reflection;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;


public class InMatcher extends BaseMatcher<Object> {

	private final String propertyName;
	private final List<Object> values;

	public InMatcher(String propertyName, Object[] values) {
		this.propertyName = propertyName;
		this.values = Arrays.asList(values);
	}

	public boolean matches(Object item) {
		Object obj = Reflection.field(propertyName).ofType(Object.class).in(item).get();
		return values.contains(obj);
	}

	public void describeTo(Description arg0) {
	}


}
