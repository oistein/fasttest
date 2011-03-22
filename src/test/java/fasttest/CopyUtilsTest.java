package fasttest;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

public class CopyUtilsTest {

	
	@Test
	public void testCopySimple() throws Exception {
		Address address = new Address("foo");
		Address addressCopy = (Address) CopyUtils.copy(address);
		
		assertThat(addressCopy.getStreet()).isEqualTo("foo");
		assertThat(addressCopy).isEqualTo(address);
		assertThat(addressCopy).isNotSameAs(address); 
	}
}
