package mil.nga.efd.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import com.solers.util.dao.ValidationException;

import mil.nga.efd.domain.AllowedHost;

@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan({ "mil.nga.efd.controllers" })
public class TestAllowedHostDAO {
	
	@Autowired
	AllowedHostDAOImpl allowedHostDAO;
	
	@Test
	public void testDAOInjection() {
		assertNotNull(allowedHostDAO);
	}
	
	@Test
	public void testInsertRetrieveRemoveRecord() {
		AllowedHost host = new AllowedHost();
		host.setAlias("host1");
		host.setCommonName("host1.nga.mil");
		assertNotNull(allowedHostDAO);
		// Persist the test record
		allowedHostDAO.persist(host);
		allowedHostDAO.flush();
		AllowedHost retrievedHost = allowedHostDAO.findById(host.getId(), false);
		assertEquals(host.getId(), retrievedHost.getId());
		assertEquals(host.getAlias(), retrievedHost.getAlias());
		assertEquals(host.getCommonName(), retrievedHost.getCommonName());
		// Remove the test record
		allowedHostDAO.remove(retrievedHost);
		allowedHostDAO.flush();
		// Now retrieve the record again
		AllowedHost shouldBeNullHost = allowedHostDAO.findById(retrievedHost.getId(), false);
		assertNull(shouldBeNullHost);
	}
	
	@Test
	public void testInsertRetrieveMultipleRecords() {
		AllowedHost host1 = new AllowedHost();
		host1.setAlias("host1");
		host1.setCommonName("host1.nga.mil");
		AllowedHost host2 = new AllowedHost();
		host2.setAlias("host2");
		host2.setCommonName("host2.nga.mil");
		AllowedHost host3 = new AllowedHost();
		host3.setAlias("host3");
		host3.setCommonName("host3.nga.mil");
		AllowedHost host4 = new AllowedHost();
		host4.setAlias("host4");
		host4.setCommonName("host4.nga.mil");
		assertNotNull(allowedHostDAO);
		// Persist the test records
		allowedHostDAO.persist(host1);
		allowedHostDAO.persist(host2);
		allowedHostDAO.persist(host3);
		allowedHostDAO.persist(host4);
		allowedHostDAO.flush();
		
	}
	
	/** 
	 * Test that no exception is thrown.
	 */
	@Test(expected = Test.None.class)
	public void testValidation() {
		
		AllowedHost host1 = new AllowedHost();
		host1.setAlias("host1");
		host1.setCommonName("host1.nga.mil");
		AllowedHost host2 = new AllowedHost();
		host2.setAlias("host2");
		host2.setCommonName("host2.nga.mil");
		AllowedHost host3 = new AllowedHost();
		host3.setAlias("host3");
		host3.setCommonName("host3.nga.mil");
		AllowedHost host4 = new AllowedHost();
		host4.setAlias("host4");
		host4.setCommonName("host4.nga.mil");
		assertNotNull(allowedHostDAO);
		
		try {
			// Persist the test records
			allowedHostDAO.persist(host1);
			allowedHostDAO.persist(host2);
			allowedHostDAO.persist(host3);
			allowedHostDAO.persist(host4);
			allowedHostDAO.flush();
			// Validate a known good record.
			AllowedHost host5 = new AllowedHost();
			host5.setAlias("host5");
			host5.setCommonName("host5.nga.mil");
			allowedHostDAO.validate(host5);
		}
		finally {
			// Cleanup the records
			allowedHostDAO.remove(host1);
			allowedHostDAO.remove(host2);
			allowedHostDAO.remove(host3);
			allowedHostDAO.remove(host4);
		}
	}
	
	@Test(expected = ValidationException.class)
	public void testEntityValidationFailure() {
		AllowedHost host1 = new AllowedHost();
		host1.setAlias("host1");
		host1.setCommonName("host1.n[]\\;ga.mil");
		assertNotNull(allowedHostDAO);
		allowedHostDAO.persist(host1);
		allowedHostDAO.flush();
	}
	
	@Test(expected = ValidationException.class)
	public void testValidationFailure() {
		AllowedHost host1 = new AllowedHost();
		host1.setAlias("host1");
		host1.setCommonName("host1.nga.mil");
		AllowedHost host2 = new AllowedHost();
		host2.setAlias("host2");
		host2.setCommonName("host2.nga.mil");
		AllowedHost host3 = new AllowedHost();
		host3.setAlias("host3");
		host3.setCommonName("host3.nga.mil");
		AllowedHost host4 = new AllowedHost();
		host4.setAlias("host4");
		host4.setCommonName("host4.nga.mil");
		assertNotNull(allowedHostDAO);
		
		try {
			// Persist the test records
			allowedHostDAO.persist(host1);
			allowedHostDAO.persist(host2);
			allowedHostDAO.persist(host3);
			allowedHostDAO.persist(host4);
			allowedHostDAO.flush();
			allowedHostDAO.validate(host4);	
		}
		finally {
			// Cleanup the records
			allowedHostDAO.remove(host1);
			allowedHostDAO.remove(host2);
			allowedHostDAO.remove(host3);
			allowedHostDAO.remove(host4);
		}
	}
}
