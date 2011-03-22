package fasttest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

import fasttest.matchers.EqualMatcher;
import fasttest.matchers.GreaterLessThanMatcher;
import fasttest.matchers.InMatcher;
import fasttest.matchers.LogicalMatcher;
import fasttest.matchers.NotMatcher;

public class InMemoryCriteria implements Criteria {

	private final List<InMemoryMatcher> matchers = new ArrayList<InMemoryMatcher>();
	private OrderComparator comparator;
	private final InMemoryTransaction transaction;
	private Projection projection;
	private int maxResults = -1;
	private ResultTransformer transformer = PassThroughResultTransformer.INSTANCE;
	
	public InMemoryCriteria(Class<?> persistentClass, InMemoryTransaction transaction) {
		this.transaction = transaction;
		matchers.add(new InstanceOfMatcher(persistentClass));
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

	private InMemoryMatcher getMatcherForCriterion(Criterion criterion) {
		if (criterion instanceof SimpleExpression) {
			SimpleExpression expr = (SimpleExpression) criterion;
			String op = (String) ObjectManipulation.getFieldValue(expr, "op");
			Object value = ObjectManipulation.getFieldValue(expr, "value");
			String propertyName = (String) ObjectManipulation.getFieldValue(expr, "propertyName");
			boolean ignoringCase = (Boolean) ObjectManipulation.getFieldValue(expr, "ignoreCase"); 
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
			Object[] values = (Object[]) ObjectManipulation.getFieldValue(criterion, "values");
			String propertyName = (String) ObjectManipulation.getFieldValue(criterion, "propertyName");
			return new InMatcher(propertyName, values);
		}
		
		if (criterion instanceof LogicalExpression) {
			Criterion lhs = (Criterion) ObjectManipulation.getFieldValue(criterion, "lhs");
			Criterion rhs = (Criterion) ObjectManipulation.getFieldValue(criterion, "rhs");
			String op = (String) ObjectManipulation.getFieldValue(criterion, "op");
			return new LogicalMatcher(getMatcherForCriterion(lhs), getMatcherForCriterion(rhs), op);
		}
		if (criterion instanceof NotExpression) {
			Criterion inner = (Criterion) ObjectManipulation.getFieldValue(criterion, "criterion");
			return new NotMatcher(getMatcherForCriterion(inner));
		}
		
		return null;
	}

	public Criteria addOrder(Order order) {
		final boolean ascending = (Boolean) ObjectManipulation.getFieldValue(order, "ascending");
		final String propertyName = (String) ObjectManipulation.getFieldValue(order, "propertyName");
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
			for (InMemoryMatcher matcher : matchers) {
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
						Object val = ObjectManipulation.getFieldValue(result.get(y), pproj.getPropertyName());
						if (!distincts.contains(val)) distincts.add(val);
					}
					for (int y = 0; y < distincts.size(); y++) {
						projection.setCell(y,i, distincts.get(y));
					}
				} else {
					for (int y = 0; y < result.size(); y++) {
						Object val = ObjectManipulation.getFieldValue(result.get(y), pproj.getPropertyName());
						projection.setCell(y,i,val);
					}
				}
			}
			
			if (AggregateProjection.class.isInstance(proj)) {
				aggregate = true;
				AggregateProjection aproj = (AggregateProjection) proj;
				String functionName = aproj.getFunctionName();
				if ("sum".equals(functionName)) {
					Long sum = 0L;
					for (Object candidate : result) {
						Object val = ObjectManipulation.getFieldValue(candidate, aproj.getPropertyName());
						sum += (Integer) val;
					}
					projection.setCell(0,i, sum);
				} else if ("avg".equals(functionName)) {
					long sum = 0;
					for (Object candidate : result) {
						Object val = ObjectManipulation.getFieldValue(candidate, aproj.getPropertyName());
						sum += Long.parseLong(val.toString());
					}
					projection.setCell(0, i, sum / (double) result.size());
				}
			}
		}
		
		
		
		return maxResults != -1 ? projection.transformResult(transformer, aggregate).subList(0, maxResults) : projection.transformResult(transformer, aggregate);
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
