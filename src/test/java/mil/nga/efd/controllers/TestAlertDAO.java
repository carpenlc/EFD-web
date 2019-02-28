package mil.nga.efd.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import mil.nga.efd.domain.Alert;
import mil.nga.efd.domain.Alert.AlertType;

/**
 * JUnit tests for the <code>AlertDAOImpl</code> class.  Includes tests for 
 * the superclass <code>GenericDAOImpl</code> class.
 * 
 * @author L. Craig Carpenter
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan({ "mil.nga.efd.controllers" })
public class TestAlertDAO {

	@Autowired
	AlertDAOImpl alertDAO;
	
	@Test
	public void testDAOInjection() {
		assertNotNull(alertDAO);
	}
	
	@Test
	public void testInsertRetrieveRemoveRecord() {
		Alert alert = new Alert();
		alert.setTimestamp(new Date());
		alert.setMessage("This is a test message.");
		alert.setUnread(true);
		alert.setType(AlertType.USER);
		assertNotNull(alertDAO);
		// Persist the test record
		alertDAO.persist(alert);
		alertDAO.flush();
		Alert retrievedAlert = alertDAO.findById(alert.getId(), false);
		assertEquals(alert.getId(), retrievedAlert.getId());
		assertEquals(alert.getMessage(), retrievedAlert.getMessage());
		assertEquals(alert.getType(), retrievedAlert.getType());
		assertEquals(alert.isUnread(), retrievedAlert.isUnread());
		// Remove the test record
		alertDAO.remove(retrievedAlert);
		alertDAO.flush();
		// Now retrieve the record again
		Alert shouldBeNullAlert = alertDAO.findById(retrievedAlert.getId(), false);
		assertNull(shouldBeNullAlert);
	}
	
	@Test
	public void testInsertRemoveMultipleRecords() {
		Alert alert1 = new Alert();
		alert1.setTimestamp(new Date());
		alert1.setMessage("This is a test message.");
		alert1.setUnread(true);
		alert1.setType(AlertType.USER);
		Alert alert2 = new Alert();
		alert2.setTimestamp(new Date());
		alert2.setMessage("This is another test message.");
		alert2.setUnread(true);
		alert2.setType(AlertType.ADMIN);
		Alert alert3 = new Alert();
		alert3.setTimestamp(new Date());
		alert3.setMessage("This is a third test message.");
		alert3.setUnread(true);
		alert3.setType(AlertType.ADMIN);
		assertNotNull(alertDAO);
		alertDAO.persist(alert1);
		alertDAO.persist(alert2);
		alertDAO.persist(alert3);
		alertDAO.flush();
		List<Alert> alerts = alertDAO.findAll();
		assertEquals(alerts.size(), 3);
		List<Long> idsToDelete = new ArrayList<Long>(3);
		idsToDelete.add(alert1.getId());
		idsToDelete.add(alert2.getId());
		idsToDelete.add(alert3.getId());
		alertDAO.removeById(idsToDelete);
		alertDAO.flush();
		List<Alert> shouldBeEmpty = alertDAO.findAll();
		assertEquals(shouldBeEmpty.size(), 0);
	}

	
}
