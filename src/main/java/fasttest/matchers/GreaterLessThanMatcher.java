package fasttest.matchers;

import java.math.BigDecimal;
import java.math.BigInteger;

import fasttest.InMemoryMatcher;
import fasttest.ObjectManipulation;

public class GreaterLessThanMatcher implements InMemoryMatcher {

	private final String propertyName;
	private final Object value;
	private final boolean greaterThan;

	public GreaterLessThanMatcher(String propertyName, Object value, boolean greaterThan) {
		this.propertyName = propertyName;
		this.value = value;
		this.greaterThan = greaterThan;
	}

	public boolean matches(Object item) {
		int compared = 0;
			Object obj = ObjectManipulation.getFieldValue(item, propertyName);
			if (long.class.isInstance(obj) || Long.class.isInstance(obj)) {
				compared = ((Long)value).compareTo(((Long)obj));
			}
			if (int.class.isInstance(obj) || Integer.class.isInstance(obj)) {
				compared = ((Integer)value).compareTo(((Integer)obj));
			}
			if (double.class.isInstance(obj) || Double.class.isInstance(obj)) {
				compared = ((Double)value).compareTo(((Double)obj));
			}
			if (short.class.isInstance(obj) || Short.class.isInstance(obj)) {
				compared = ((Short)value).compareTo(((Short)obj));
			}
			if (byte.class.isInstance(obj) || Byte.class.isInstance(obj)) {
				compared = ((Byte)value).compareTo(((Byte)obj));
			}
			if (BigDecimal.class.isInstance(obj)) {
				compared = ((BigDecimal)value).compareTo(((BigDecimal)obj));
			}
			if (BigInteger.class.isInstance(obj)) {
				compared = ((BigInteger)value).compareTo(((BigInteger)obj));
			}
		return greaterThan ? compared < 0 : compared > 0;
	}
}
