package fasttest;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fest.reflect.core.Reflection;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.criterion.AggregateProjection;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.InExpression;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.NotExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.PropertyProjection;
import org.hibernate.criterion.SimpleExpression;
import org.hibernate.transform.PassThroughResultTransformer;
import org.hibernate.transform.ResultTransformer;

import ch.lambdaj.Lambda;
import fasttest.matchers.EqualMatcher;
import fasttest.matchers.GreaterLessThanMatcher;
import fasttest.matchers.InMatcher;
import fasttest.matchers.LogicalMatcher;
import fasttest.matchers.NotMatcher;

public class InMemoryCriteria implements Criteria {

	private final List<Matcher<Object>> matchers = new ArrayList<Matcher<Object>>();
	private OrderComparator comparator;
	private final InMemoryTransaction transaction;
	private Projection projection;
	private int maxResults = -1;
	private ResultTransformer transformer = PassThroughResultTransformer.INSTANCE;
	
	public InMemoryCriteria(Class<?> persistentClass, InMemoryTransaction transaction) {
		this.transaction = transaction;
		matchers.add(Matchers.instanceOf(persistentClass));
	}
	
	public String getAlias() {
		return null;
	}

	public Criteria setProjection(Projection projection) {
		this.projection = projection;
		return this;
	}

	public Criteria add(Criterion criterion) {
		matchers.add(getMatcherForCriterion(criterion));
		return this;
	}

	private Matcher<Object> getMatcherForCriterion(Criterion criterion) {
		if (criterion instanceof SimpleExpression) {
			SimpleExpression expr = (SimpleExpression) criterion;
			String op = Reflection.field("op").ofType(String.class).in(expr).get();
			Object value = Reflection.field("value").ofType(Object.class).in(expr).get();
			String propertyName = Reflection.field("propertyName").ofType(String.class).in(expr).get();
			boolean ignoringCase = Reflection.field("ignoreCase").ofType(boolean.class).in(expr).get();
			if ("=".equals(op)) {
				return new EqualMatcher(propertyName, value, ignoringCase);
			}
			else if (">".equals(op)) {
				return new GreaterLessThanMatcher(propertyName, value, true);
			}
			else if ("<".equals(op)) {
				return new GreaterLessThanMatcher(propertyName, value, false);
			}
		}
		if (criterion instanceof InExpression) {
			Object[] values = Reflection.field("values").ofType(Object[].class).in(criterion).get();
			String propertyName = Reflection.field("propertyName").ofType(String.class).in(criterion).get();
			return new InMatcher(propertyName, values);
		}
		
		if (criterion instanceof LogicalExpression) {
			Criterion lhs = Reflection.field("lhs").ofType(Criterion.class).in(criterion).get();
			Criterion rhs = Reflection.field("rhs").ofType(Criterion.class).in(criterion).get();
			String op = Reflection.field("op").ofType(String.class).in(criterion).get();
			return new LogicalMatcher(getMatcherForCriterion(lhs), getMatcherForCriterion(rhs), op);
		}
		if (criterion instanceof NotExpression) {
			NotExpression expr = (NotExpression) criterion;
			Criterion inner = Reflection.field("criterion").ofType(Criterion.class).in(expr).get();
			return new NotMatcher(getMatcherForCriterion(inner));
		}
		
		return null;
	}

	public Criteria addOrder(Order order) {
		final boolean ascending = Reflection.field("ascending").ofType(boolean.class).in(order).get();
		final String propertyName = Reflection.field("propertyName").ofType(String.class).in(order).get();
		if (comparator == null) {
			comparator = new OrderComparator(ascending, propertyName);
		} else {
			comparator.addOrder(ascending, propertyName);
		}
		return this;
	}

	public Criteria setFetchMode(String associationPath, FetchMode mode)
			throws HibernateException {
		return this;
	}

	public Criteria setLockMode(LockMode lockMode) {
		return this;
	}

	public Criteria setLockMode(String alias, LockMode lockMode) {
		return this;
	}

	public Criteria createAlias(String associationPath, String alias)
			throws HibernateException {
		return this;
	}

	public Criteria createAlias(String associationPath, String alias,
			int joinType) throws HibernateException {
		return this;
	}

	public Criteria createAlias(String associationPath, String alias,
			int joinType, Criterion withClause) throws HibernateException {
		return this;
	}

	public Criteria createCriteria(String associationPath)
			throws HibernateException {
		return this;
	}

	public Criteria createCriteria(String associationPath, int joinType)
			throws HibernateException {
		return this;
	}

	public Criteria createCriteria(String associationPath, String alias)
			throws HibernateException {
		return this;
	}

	public Criteria createCriteria(String associationPath, String alias,
			int joinType) throws HibernateException {
		return this;
	}

	public Criteria createCriteria(String associationPath, String alias,
			int joinType, Criterion withClause) throws HibernateException {
		return this;
	}

	public Criteria setResultTransformer(ResultTransformer resultTransformer) {
		return this;
	}

	public Criteria setMaxResults(int maxResults) {
		this.maxResults = maxResults;
		return this;
	}

	public Criteria setFirstResult(int firstResult) {
		return this;
	}

	public boolean isReadOnlyInitialized() {
		return false;
	}

	public boolean isReadOnly() {
		return false;
	}

	public Criteria setReadOnly(boolean readOnly) {
		return this;
	}

	public Criteria setFetchSize(int fetchSize) {
		return this;
	}

	public Criteria setTimeout(int timeout) {
		return this;
	}

	public Criteria setCacheable(boolean cacheable) {
		return this;
	}

	public Criteria setCacheRegion(String cacheRegion) {
		return this;
	}

	public Criteria setComment(String comment) {
		return this;
	}

	public Criteria setFlushMode(FlushMode flushMode) {
		return this;
	}

	public Criteria setCacheMode(CacheMode cacheMode) {
		return this;
	}

	public List<Object> list() throws HibernateException {
		List<Object> result = new ArrayList<Object>();
		for (Object candidate : transaction.getStore().values()) {
			boolean add = true;
			for (Matcher<Object> matcher : matchers) {
				add &= matcher.matches(candidate);
			}
			
			if (add) result.add(candidate);
		}
		if (comparator != null) Collections.sort(result, comparator);
		if (projection != null) result = project(result);
		
		return result;
	}

	private List<Object> project(List<Object> result) {
		ProjectionList list = (ProjectionList) this.projection;
		ObjectProjection projection = new ObjectProjection(list.getLength());
		boolean aggregate = false;
		for ( int i=0; i<list.getLength(); i++ ) {
			Projection proj = list.getProjection(i);
			
			if (PropertyProjection.class.isInstance(proj)) {
				PropertyProjection pproj = (PropertyProjection) proj;
				if (pproj.isGrouped()) {
					List<Object> distincts = new ArrayList<Object>();
					for (int y = 0; y < result.size(); y++) {
						Object val = getFieldValue(result.get(y), pproj.getPropertyName());
						if (!distincts.contains(val)) distincts.add(val);
					}
					for (int y = 0; y < distincts.size(); y++) {
						projection.setCell(y,i, distincts.get(y));
					}
				} else {
					for (int y = 0; y < result.size(); y++) {
						Object val = getFieldValue(result.get(y), pproj.getPropertyName());
						projection.setCell(y,i,val);
					}
				}
			}
			
			if (AggregateProjection.class.isInstance(proj)) {
				aggregate = true;
				AggregateProjection aproj = (AggregateProjection) proj;
				String functionName = aproj.getFunctionName();
				if ("sum".equals(functionName)) {
					List<Object> sum = new ArrayList<Object>();
					for (Object candidate : result) {
						Object val = getFieldValue(candidate, aproj.getPropertyName());
						sum.add(val); 
					}
					projection.setCell(0,i, Long.parseLong(Lambda.sum(sum).toString()));
				} else if ("avg".equals(functionName)) {
					long sum = 0;
					for (Object candidate : result) {
						Object val = getFieldValue(candidate, aproj.getPropertyName());
						sum += Long.parseLong(val.toString());
					}
					projection.setCell(0, i, sum / (double) result.size());
				}
			}
		}
		
		
		
		return maxResults != -1 ? projection.transformResult(transformer, aggregate).subList(0, maxResults) : projection.transformResult(transformer, aggregate);
	}

	private Object getFieldValue(Object candidate, String propertyName) {
		try {
			Field field = candidate.getClass().getDeclaredField(propertyName);
			if (Object.class.isAssignableFrom(field.getType())) {
				return Reflection.field(propertyName).ofType(Object.class).in(candidate).get();
			} else {
				if (int.class.equals(field.getType())) return Reflection.field(propertyName).ofType(int.class).in(candidate).get();
				if (byte.class.equals(field.getType())) return Reflection.field(propertyName).ofType(byte.class).in(candidate).get();
				if (short.class.equals(field.getType())) return Reflection.field(propertyName).ofType(short.class).in(candidate).get();
				if (long.class.equals(field.getType())) return Reflection.field(propertyName).ofType(long.class).in(candidate).get();
				if (double.class.equals(field.getType())) return Reflection.field(propertyName).ofType(double.class).in(candidate).get();
				if (float.class.equals(field.getType())) return Reflection.field(propertyName).ofType(float.class).in(candidate).get();
				if (boolean.class.equals(field.getType())) return Reflection.field(propertyName).ofType(boolean.class).in(candidate).get();
				if (char.class.equals(field.getType())) return Reflection.field(propertyName).ofType(char.class).in(candidate).get();
			}
			throw new RuntimeException("Unable to get " + propertyName + " from " + candidate.getClass() + " (" + candidate + ")");
		} catch (NoSuchFieldException e) {
			throw new RuntimeException("Unable to get " + propertyName + " from " + candidate.getClass() + " (" + candidate + ")");
		} 
	}

	public ScrollableResults scroll() throws HibernateException {
		return new InMemoryScrollableResults(list());
	}

	public ScrollableResults scroll(ScrollMode scrollMode)
			throws HibernateException {
		return null;
	}

	public Object uniqueResult() throws HibernateException {
		return list().get(0);
	}

}
