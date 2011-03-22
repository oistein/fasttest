package fasttest;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Person {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	private String name;
	private Integer age;

	Person() {}
	
	@ManyToOne
	private Address address;
	
	public Person(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Person)) return false;
		Person other = (Person) obj;
		return id != null ? id.equals(other.id) : null;
	}
	
	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : -1;
	}

	public Person setAge(int age) {
		this.age = age;
		return this;
	}
	
	@Override
	public String toString() {
		return "Person<name:" + name + ",age:" + age + ">";
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Address getAddress() {
		return address;
	}

	public String getName() {
		return name;
	}

}
