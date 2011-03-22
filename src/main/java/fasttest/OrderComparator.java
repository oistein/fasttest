package fasttest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.fest.reflect.core.Reflection;

public class OrderComparator implements Comparator<Object> {

	private final boolean ascending;
	private final String propertyName;
	private final List<OrderComparator> others = new ArrayList<OrderComparator>();

	public OrderComparator(boolean ascending, String propertyName) {
		this.ascending = ascending;
		this.propertyName = propertyName;
	}

	@SuppressWarnings("unchecked")
	public int compare(Object o1, Object o2) {
		int compared = 0;
		try {
			Class<?> o1type = o1.getClass().getDeclaredField(propertyName).getType();
			if (o1type.equals(int.class)) {
				compared = Reflection.field(propertyName).ofType(int.class).in(o1).get().compareTo(
						   Reflection.field(propertyName).ofType(int.class).in(o2).get());
			} else if (Comparable.class.isAssignableFrom(o1type)) {
				compared = Reflection.field(propertyName).ofType(Comparable.class).in(o1).get().compareTo(
						   Reflection.field(propertyName).ofType(Comparable.class).in(o2).get());
			}
			
			Iterator<OrderComparator> iterator = others.iterator();
			while (compared == 0 && iterator.hasNext()) {
				compared = iterator.next().compare(o1, o2);
			}
			
		} catch (NoSuchFieldException e) {
			throw new RuntimeException("No field " + propertyName + " in class " + o1.getClass().getName(), e);
		} 
		return ascending ? compared : compared * -1;
	}
	
	public OrderComparator addOrder(boolean ascending, String propertyName) {
		others.add(new OrderComparator(ascending, propertyName));
		return this;
		
	}

}
