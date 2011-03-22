package fasttest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.hibernate.HibernateException;
import org.hibernate.ScrollableResults;
import org.hibernate.type.Type;

public class InMemoryScrollableResults implements ScrollableResults {

	private final List<Object> list;
	private int current;
	private int size;

	public InMemoryScrollableResults(List<Object> list) {
		this.list = new LinkedList<Object>(list);
		this.size = list.size();
		this.current = 0;
	}

	public boolean next() throws HibernateException {
		current++;
		return size < current;
	}

	public boolean previous() throws HibernateException {
		current--;
		return current >= 0;
	}

	public boolean scroll(int i) throws HibernateException {
		return false;
	}

	public boolean last() throws HibernateException {
		return false;
	}

	public boolean first() throws HibernateException {
		return false;
	}

	public void beforeFirst() throws HibernateException {

	}

	public void afterLast() throws HibernateException {

	}

	public boolean isFirst() throws HibernateException {
		return current == 0;
	}

	public boolean isLast() throws HibernateException {
		return current == size - 1;
	}

	public void close() throws HibernateException {
	}

	public Object[] get() throws HibernateException {
		return null;
	}

	public Object get(int i) throws HibernateException {
		return null;
	}

	public Type getType(int i) {
		return null;
	}

	public Integer getInteger(int col) throws HibernateException {
		return null;
	}

	public Long getLong(int col) throws HibernateException {
		return null;
	}

	public Float getFloat(int col) throws HibernateException {
		return null;
	}

	public Boolean getBoolean(int col) throws HibernateException {
		return null;
	}

	public Double getDouble(int col) throws HibernateException {
		return null;
	}

	public Short getShort(int col) throws HibernateException {
		return null;
	}

	public Byte getByte(int col) throws HibernateException {
		return null;
	}

	public Character getCharacter(int col) throws HibernateException {
		return null;
	}

	public byte[] getBinary(int col) throws HibernateException {
		return null;
	}

	public String getText(int col) throws HibernateException {
		return null;
	}

	public Blob getBlob(int col) throws HibernateException {
		return null;
	}

	public Clob getClob(int col) throws HibernateException {
		return null;
	}

	public String getString(int col) throws HibernateException {
		return null;
	}

	public BigDecimal getBigDecimal(int col) throws HibernateException {
		return null;
	}

	public BigInteger getBigInteger(int col) throws HibernateException {
		return null;
	}

	public Date getDate(int col) throws HibernateException {
		return null;
	}

	public Locale getLocale(int col) throws HibernateException {
		return null;
	}

	public Calendar getCalendar(int col) throws HibernateException {
		return null;
	}

	public TimeZone getTimeZone(int col) throws HibernateException {
		return null;
	}

	public int getRowNumber() throws HibernateException {
		return 0;
	}

	public boolean setRowNumber(int rowNumber) throws HibernateException {
		return false;
	}

}
