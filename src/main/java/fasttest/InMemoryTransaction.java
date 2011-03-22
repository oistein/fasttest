package fasttest;

import java.io.Serializable;
import java.util.HashMap;

import javax.transaction.Synchronization;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;

public class InMemoryTransaction implements Transaction {

	private final HashMap<Serializable, Object> original;
	private final HashMap<Serializable, Object> copy;
	
	private boolean rollback;
	private boolean commit;
	private boolean active = true;
	private InMemorySession session;

	@SuppressWarnings("unchecked")
	public InMemoryTransaction(InMemorySession session) {
		this.original = session.getStore();
		this.session = session;
		this.copy = (HashMap<Serializable, Object>) ObjectManipulation.copy(original);
		begin();
	}


	public void begin() throws HibernateException {
		active = true;
	}

	public void commit() throws HibernateException {
		commit = true;
		active = false;
		session.setStore(copy);
	}

	public void rollback() throws HibernateException {
		rollback = true;
		active = false;
	}

	public boolean wasRolledBack() throws HibernateException {
		return rollback;
	}

	public boolean wasCommitted() throws HibernateException {
		return commit;
	}

	public boolean isActive() throws HibernateException {
		return active;
	}

	public void registerSynchronization(Synchronization synchronization)
			throws HibernateException {
	}

	public void setTimeout(int seconds) {
	}

	public HashMap<Serializable, Object> getStore() {
		return rollback ? original : copy;
	}

}
