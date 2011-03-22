package fasttest.matchers;

import org.hamcrest.Description;

import fasttest.InMemoryMatcher;
import fasttest.ObjectManipulation;

public class EqualMatcher implements InMemoryMatcher {

	private final String propertyName;
	private final Object value;
	private final boolean ignoringCase;

	public EqualMatcher(String propertyName, Object value,
			boolean ignoringCase) {
				this.propertyName = propertyName;
				this.value = value;
				this.ignoringCase = ignoringCase;
	}

	public boolean matches(Object item) {
		Object val = ObjectManipulation.getFieldValue(item, propertyName);
		return ignoringCase ? value.equals(val.toString().toLowerCase()) :
			                  value.equals(val);
	}

	public void describeTo(Description description) {
		// TODO Auto-generated method stub
		
	}

}
