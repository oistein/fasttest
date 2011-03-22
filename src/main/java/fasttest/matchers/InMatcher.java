package fasttest.matchers;

import java.util.Arrays;
import java.util.List;

import fasttest.InMemoryMatcher;
import fasttest.ObjectManipulation;


public class InMatcher implements InMemoryMatcher {

	private final String propertyName;
	private final List<Object> values;

	public InMatcher(String propertyName, Object[] values) {
		this.propertyName = propertyName;
		this.values = Arrays.asList(values);
	}

	public boolean matches(Object item) {
		Object obj = ObjectManipulation.getFieldValue(item, propertyName);
		return values.contains(obj);
	}
}
