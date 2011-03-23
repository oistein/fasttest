package fasttest;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
@SuppressWarnings("rawtypes")
public class InsertTest extends DataTest {
	
	private Session session;
	private Transaction transaction;
	
	@Parameters
	public static Collection<Object[]> data() {
	   Object[][] data = new Object[][] { { createSessionFactory() }, { createInMemorySessionFactory() } };
	   return Arrays.asList(data);
	}
	
	public InsertTest(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Before public void startTransaction() {
		session = sessionFactory.getCurrentSession();
		transaction = session.beginTransaction();
	}
	
	@After public void rollbackTransaction() {
		transaction.rollback();
	}

	@Test
	public void testSave() throws Exception {
		Person person = new Person("");
		Person ola = new Person("ola");
		assertThat(session.save(person)).isNotNull();
		assertThat(session.save(ola)).isNotNull();
		session.flush();
		session.clear();

		Person ret = (Person) session.get(Person.class, person.getId());
		assertThat(ret).isNotNull();
		assertThat(ret).isEqualTo(person);
		assertThat(ret).isNotSameAs(person);
		
		ret = (Person) session.get(Person.class, ola.getId());
		assertThat(ret).isNotNull();
		assertThat(ret).isEqualTo(ola);
		assertThat(ret).isNotEqualTo(person);
		assertThat(ret).isNotSameAs(ola);
	}
	
	@Test
	public void testSaveChildCascade() throws Exception {
		Person ola = new Person("ola");
		Address address = new Address("Some street 142");
		ola.setAddress(address);
		assertThat(session.save(ola)).isNotNull();
		session.save(address);
		session.flush();
		session.clear();
		
		Person ret = (Person) session.get(Person.class, ola.getId());
		assertThat(ret).isNotNull();
		assertThat(ret.getAddress()).isNotNull();
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testFindEq() throws Exception {
		Person ¿istein = new Person("¯istein"); Person ola = new Person("ola");
		session.save(¿istein); session.save(ola);
		session.flush();
		session.clear();
		
		Criteria criteria = session.createCriteria(Person.class);
		criteria.add(Restrictions.eq("name", "¯istein"));
		List<Person> list = criteria.list();
		
		assertThat(list).contains(¿istein).excludes(ola);
		
		criteria = session.createCriteria(Person.class);
		criteria.add(Restrictions.not(Restrictions.eq("name", "¯istein")));
		list = criteria.list();
		
		assertThat(list).excludes(¿istein).contains(ola);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testFindGt() throws Exception {
		Person ¿istein = new Person("¿istein").setAge(18); Person ola = new Person("ola").setAge(20);
		session.save(¿istein); session.save(ola);
		session.flush();
		session.clear();
		
		
		Criteria criteria = session.createCriteria(Person.class);
		criteria.add(Restrictions.gt("age", 19));
		List<Person> list = criteria.list();
		
		assertThat(list).contains(ola).excludes(¿istein);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testFindLt() throws Exception {
		Person ¿istein = new Person("¿istein").setAge(18); Person ola = new Person("ola").setAge(20);
		session.save(¿istein); session.save(ola);
		session.flush();
		session.clear();
		
		
		Criteria criteria = session.createCriteria(Person.class);
		criteria.add(Restrictions.lt("age", 19));
		List<Person> list = criteria.list();
		
		assertThat(list).contains(¿istein).excludes(ola);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testFindOr() throws Exception {
		Person ¿istein = new Person("¿istein").setAge(18); Person ola = new Person("ola").setAge(20);
		Person ola2 = new Person("ola2").setAge(18);
		session.save(¿istein); session.save(ola); session.save(ola2);
		session.flush();
		session.clear();
		
		
		Criteria criteria = session.createCriteria(Person.class);
		criteria.add(Restrictions.or(Restrictions.gt("age", 19), Restrictions.eq("name", "¿istein")));
		List<Person> list = criteria.list();
		
		assertThat(list).contains(¿istein, ola).excludes(ola2);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testFindIn() throws Exception {
		Person ¿istein = new Person("¿istein").setAge(18); Person ola = new Person("ola").setAge(20);
		Person ¿istein2 = new Person("¿istein2").setAge(90);;
		session.save(¿istein); session.save(ola); session.save(¿istein2);
		session.flush();
		session.clear();
		
		
		Criteria criteria = session.createCriteria(Person.class);
		criteria.add(Restrictions.in("age", Arrays.asList(18, 20)));
		List<Person> list = criteria.list();
		
		assertThat(list).contains(¿istein, ola).excludes(¿istein2);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testRestriction() throws Exception {
		Person ¿istein = new Person("¿istein").setAge(18); Person ¿istein3 = new Person("ola").setAge(20);
		Person ¿istein2 = new Person("¿istein").setAge(18);;
		session.save(¿istein); session.save(¿istein3); session.save(¿istein2);
		session.flush();
		session.clear();
		
		HashMap<Object, Object> map = new HashMap<Object, Object>();
		map.put("name", "¿istein");
		map.put("age", 18);
		
		Criteria criteria = session.createCriteria(Person.class);
		criteria.add(Restrictions.allEq(map));
		List<Person> list = criteria.list();
		
		assertThat(list).contains(¿istein, ¿istein2).excludes(¿istein3);
	}
	
	@Test
	public void testFindOrder() throws Exception {
		Person ¿istein = new Person("¿istein").setAge(18); Person ola = new Person("ola").setAge(20);
		
		session.save(¿istein); session.save(ola); 
		
		Criteria criteria = session.createCriteria(Person.class);
		assertThat(criteria.addOrder(Order.desc("age")).list()).containsExactly(ola, ¿istein);
		
		Person ¿istein2 = new Person("¿istein").setAge(40);
		session.save(¿istein2);
		session.flush();
		session.clear();
		
		criteria = session.createCriteria(Person.class);
		criteria.addOrder(Order.asc("name")).addOrder(Order.desc("age"));
 		assertThat(criteria.list()).containsExactly(ola, ¿istein2, ¿istein);
	}
	
	
	@Test
	public void testSimpleProjection() throws Exception {
		
		Person ¿istein = new Person("¿istein");
		Person ola = new Person("ola");
		session.save(¿istein); session.save(ola);
		
		Criteria criteria = session.createCriteria(Person.class)
		.setProjection(Projections.projectionList()
				.add(Projections.property("name")));
		List list = criteria.list();
		assertThat(list).contains(¿istein.getName(), ola.getName());
	}
	
	@Test
	public void testProjection() throws Exception {
		
		Person ¿istein = new Person("¿istein");
		¿istein.setAge(20);
		Person ola = new Person("ola");
		session.save(¿istein); session.save(ola);
		
		Criteria criteria = session.createCriteria(Person.class)
		.setProjection(Projections.projectionList()
				.add(Projections.property("name"))
				.add(Projections.property("age")))
				.addOrder(Order.desc("name"));
		
		List list = criteria.list();
		Object [] res = (Object[]) list.get(0);
		assertThat(res).contains(¿istein.getName(), 20);    
		res = (Object[]) list.get(1);
		assertThat(res).contains(ola.getName(), null);
	}
	
	@Test 
	public void testProjectionAggregate() throws Exception {
		
		Person ¿istein = new Person("¿istein");
		¿istein.setAge(20);
		Person ola = new Person("ola");
		ola.setAge(40);
		session.save(¿istein); session.save(ola);
		
		Criteria criteria = session.createCriteria(Person.class)
		.setProjection(Projections.projectionList()
				.add(Projections.avg("age"))
				.add(Projections.sum("age")));
		List list = criteria.list();
		assertThat(list.size()).isEqualTo(1);
		assertThat(list.get(0)).isEqualTo(new Object[]{30.0, 60L});
	}
	
	@Test 
	public void testProjectionGroup() throws Exception {
		
		Person ¿istein = new Person("¿istein");
		Person ola = new Person("ola");
		Person ola2 = new Person("ola"); 
		session.save(¿istein); session.save(ola); session.save(ola2);
		
		Criteria criteria = session.createCriteria(Person.class)
		.setProjection(Projections.projectionList()
				.add(Projections.groupProperty("name")));
		List list = criteria.list();
		assertThat(list.size()).isEqualTo(2);
		assertThat(list).contains("ola", "¿istein");
	}
}
