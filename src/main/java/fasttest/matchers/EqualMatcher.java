package fasttest.matchers;

import org.fest.reflect.core.Reflection;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class EqualMatcher extends BaseMatcher<Object> {

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
		Object val = Reflection.field(propertyName).ofType(Object.class).in(item).get();
		return ignoringCase ? value.equals(val.toString().toLowerCase()) :
			                  value.equals(val);
	}

	public void describeTo(Description description) {
		// TODO Auto-generated method stub
		
	}

}
