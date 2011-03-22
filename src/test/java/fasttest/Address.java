package fasttest;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Address {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private String street;
	
	Address() {}
	
	public Address(String street) {
		this.street = street;
	}
	
	public String getStreet() {
		return street;
	}
	
	public Long getId() {
		return id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Address)) return false;
		Address other = (Address) obj;
		return id == null ? other.id == null : id.equals(other.id);
	}
	
	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : -1;
	}

}
