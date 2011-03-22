package fasttest;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Id;

import org.fest.reflect.core.Reflection;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.Filter;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.ReplicationMode;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.UnknownProfileException;
import org.hibernate.classic.Session;
import org.hibernate.jdbc.Work;
import org.hibernate.stat.SessionStatistics;
import org.hibernate.type.Type;

public class InMemorySession implements Session {
 
	private static final long serialVersionUID = -6004024204593388638L;
	
	private final InMemorySessionFactory inMemorySessionFactory;

	InMemorySession(InMemorySessionFactory inMemorySessionFactory) {
		this.inMemorySessionFactory = inMemorySessionFactory;
	}

	public EntityMode getEntityMode() {
		return null;
	}

	public org.hibernate.Session getSession(EntityMode entityMode) {
		return null;
	}

	public void flush() throws HibernateException {
	}

	public void setFlushMode(FlushMode flushMode) {
	}

	public FlushMode getFlushMode() {
		return null;
	}

	public void setCacheMode(CacheMode cacheMode) {

	}

	public CacheMode getCacheMode() {
		return null;
	}

	public SessionFactory getSessionFactory() {
		return inMemorySessionFactory;
	}

	public Connection connection() throws HibernateException {
		return null;
	}

	public Connection close() throws HibernateException {
		return null;
	}

	public void cancelQuery() throws HibernateException {
	}

	public boolean isOpen() {
		return false;
	}

	public boolean isConnected() {
		return false;
	}

	public boolean isDirty() throws HibernateException {
		return false;
	}

	public boolean isDefaultReadOnly() {
		return false;
	}

	public void setDefaultReadOnly(boolean readOnly) {
		// TODO Auto-generated method stub

	}

	public Serializable getIdentifier(Object object) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean contains(Object object) {
		// TODO Auto-generated method stub
		return false;
	}

	public void evict(Object object) throws HibernateException {
		// TODO Auto-generated method stub

	}

	public Object load(Class theClass, Serializable id, LockMode lockMode)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object load(Class theClass, Serializable id, LockOptions lockOptions)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object load(String entityName, Serializable id, LockMode lockMode)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object load(String entityName, Serializable id,
			LockOptions lockOptions) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object load(Class theClass, Serializable id)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object load(String entityName, Serializable id)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public void load(Object object, Serializable id) throws HibernateException {
		// TODO Auto-generated method stub

	}

	public void replicate(Object object, ReplicationMode replicationMode)
			throws HibernateException {

	}

	public void replicate(String entityName, Object object,
			ReplicationMode replicationMode) throws HibernateException {

	}

	private static Long incrementor = 0L;
	private HashMap<Serializable, Object> store = new HashMap<Serializable, Object>();

	private InMemoryTransaction transaction;
	
	public Serializable save(Object object) throws HibernateException {
		String idFieldName = getIdFieldName(object);
		
		Long id = incrementor++;
		Reflection.field(idFieldName).ofType(Long.class).in(object).set(id);
		transaction.getStore().put(id, CopyUtils.copy(object));
		
		return id;
	}

	private String getIdFieldName(Object object) {
		String idFieldName = null;
		for (Field field : object.getClass().getDeclaredFields()) {
			Id id = field.getAnnotation(Id.class);
			if (id != null) { 
				idFieldName = field.getName();
				break;
			}
		}
		if (idFieldName == null)
			throw new RuntimeException("Cannot find @Id on any fields in class: " + object.getClass().getName());
		return idFieldName;
	}

	public Serializable save(String entityName, Object object)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public void saveOrUpdate(Object object) throws HibernateException {
		// TODO Auto-generated method stub

	}

	public void saveOrUpdate(String entityName, Object object)
			throws HibernateException {
		// TODO Auto-generated method stub

	}

	public void update(Object object) throws HibernateException {
		// TODO Auto-generated method stub

	}

	public void update(String entityName, Object object)
			throws HibernateException {
		// TODO Auto-generated method stub

	}

	public Object merge(Object object) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object merge(String entityName, Object object)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public void persist(Object object) throws HibernateException {
		// TODO Auto-generated method stub

	}

	public void persist(String entityName, Object object)
			throws HibernateException {
		// TODO Auto-generated method stub

	}

	public void delete(Object object) throws HibernateException {
		// TODO Auto-generated method stub

	}

	public void delete(String entityName, Object object)
			throws HibernateException {
		// TODO Auto-generated method stub

	}

	public void lock(Object object, LockMode lockMode)
			throws HibernateException {
		// TODO Auto-generated method stub

	}

	public void lock(String entityName, Object object, LockMode lockMode)
			throws HibernateException {
		// TODO Auto-generated method stub

	}

	public LockRequest buildLockRequest(LockOptions lockOptions) {
		// TODO Auto-generated method stub
		return null;
	}

	public void refresh(Object object) throws HibernateException {
		// TODO Auto-generated method stub

	}

	public void refresh(Object object, LockMode lockMode)
			throws HibernateException {
		// TODO Auto-generated method stub

	}

	public void refresh(Object object, LockOptions lockOptions)
			throws HibernateException {
	}

	public LockMode getCurrentLockMode(Object object) throws HibernateException {
		return null;
	}

	public Transaction beginTransaction() throws HibernateException {
		return transaction = new InMemoryTransaction(this);
	}

	public InMemoryTransaction getTransaction() {
		return transaction;
	}

	public Criteria createCriteria(Class persistentClass) {
		return new InMemoryCriteria(persistentClass, getTransaction());
	}

	public Criteria createCriteria(Class persistentClass, String alias) {
		return null;
	}

	public Criteria createCriteria(String entityName) {
		return null;
	}

	public Criteria createCriteria(String entityName, String alias) {
		// TODO Auto-generated method stub
		return null;
	}

	public Query createQuery(String queryString) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public SQLQuery createSQLQuery(String queryString)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Query createFilter(Object collection, String queryString)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Query getNamedQuery(String queryName) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public void clear() {
		// TODO Auto-generated method stub

	}

	public Object get(Class clazz, Serializable id) throws HibernateException {
		return transaction.getStore().get(id);
	}

	public Object get(Class clazz, Serializable id, LockMode lockMode)
			throws HibernateException {
		return null;
	}

	public Object get(Class clazz, Serializable id, LockOptions lockOptions)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object get(String entityName, Serializable id)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object get(String entityName, Serializable id, LockMode lockMode)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object get(String entityName, Serializable id,
			LockOptions lockOptions) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getEntityName(Object object) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Filter enableFilter(String filterName) {
		// TODO Auto-generated method stub
		return null;
	}

	public Filter getEnabledFilter(String filterName) {
		// TODO Auto-generated method stub
		return null;
	}

	public void disableFilter(String filterName) {
		// TODO Auto-generated method stub

	}

	public SessionStatistics getStatistics() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isReadOnly(Object entityOrProxy) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setReadOnly(Object entityOrProxy, boolean readOnly) {
		// TODO Auto-generated method stub

	}

	public void doWork(Work work) throws HibernateException {
		// TODO Auto-generated method stub

	}

	public Connection disconnect() throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public void reconnect() throws HibernateException {
		// TODO Auto-generated method stub

	}

	public void reconnect(Connection connection) throws HibernateException {
		// TODO Auto-generated method stub

	}

	public boolean isFetchProfileEnabled(String name)
			throws UnknownProfileException {
		// TODO Auto-generated method stub
		return false;
	}

	public void enableFetchProfile(String name) throws UnknownProfileException {
		// TODO Auto-generated method stub

	}

	public void disableFetchProfile(String name) throws UnknownProfileException {
		// TODO Auto-generated method stub

	}

	public Object saveOrUpdateCopy(Object object) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object saveOrUpdateCopy(Object object, Serializable id)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object saveOrUpdateCopy(String entityName, Object object)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Object saveOrUpdateCopy(String entityName, Object object,
			Serializable id) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public List find(String query) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public List find(String query, Object value, Type type)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public List find(String query, Object[] values, Type[] types)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Iterator iterate(String query) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Iterator iterate(String query, Object value, Type type)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Iterator iterate(String query, Object[] values, Type[] types)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection filter(Object collection, String filter)
			throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection filter(Object collection, String filter, Object value,
			Type type) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection filter(Object collection, String filter, Object[] values,
			Type[] types) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public int delete(String query) throws HibernateException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int delete(String query, Object value, Type type)
			throws HibernateException {
		// TODO Auto-generated method stub
		return 0;
	}

	public int delete(String query, Object[] values, Type[] types)
			throws HibernateException {
		// TODO Auto-generated method stub
		return 0;
	}

	public Query createSQLQuery(String sql, String returnAlias,
			Class returnClass) {
		// TODO Auto-generated method stub
		return null;
	}

	public Query createSQLQuery(String sql, String[] returnAliases,
			Class[] returnClasses) {
		// TODO Auto-generated method stub
		return null;
	}

	public void save(Object object, Serializable id) throws HibernateException {
		// TODO Auto-generated method stub

	}

	public void save(String entityName, Object object, Serializable id)
			throws HibernateException {
		// TODO Auto-generated method stub

	}

	public void update(Object object, Serializable id)
			throws HibernateException {
		// TODO Auto-generated method stub

	}

	public void update(String entityName, Object object, Serializable id)
			throws HibernateException {
		// TODO Auto-generated method stub

	}

	public void setStore(HashMap<Serializable, Object> store) {
		this.store = store;
	}

	public HashMap<Serializable, Object> getStore() {
		return store;
	}

}
