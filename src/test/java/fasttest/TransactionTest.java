package fasttest;

import static org.fest.assertions.Assertions.assertThat;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TransactionTest extends DataTest {
	
	@Parameters
	public static Collection<Object[]> data() {
	   Object[][] data = new Object[][] { { createSessionFactory() }, { createInMemorySessionFactory() } };
	   return Arrays.asList(data);
	}

	private Session session;
	
	public TransactionTest(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Before public void startSession() {
		session = sessionFactory.getCurrentSession();
	}
	
	@Test
	public void testCommit() throws Exception {
		Transaction transaction = session.beginTransaction();
		
		Serializable save = session.save(new Person(""));
		assertThat(session.get(Person.class, save)).isNotNull();
		
		transaction.commit();
		session = sessionFactory.getCurrentSession();
		transaction = session.beginTransaction();
		
		assertThat(session.get(Person.class, save)).isNotNull();
		
	}
	
	@Test
	public void testRollback() throws Exception {
		Transaction transaction = session.beginTransaction();
		
		Serializable save = session.save(new Person(""));
		assertThat(session.get(Person.class, save)).isNotNull();
		
		transaction.rollback();
		session = sessionFactory.getCurrentSession();
		transaction = session.beginTransaction();
		
		assertThat(session.get(Person.class, save)).isNull();
	}
	
}
