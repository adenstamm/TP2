package tddTests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import database.Database;
import entityClasses.AdminRequest;
import java.sql.SQLException;

/**
 * <p> Title: AdminRequestTDDTest Class </p>
 * <p> Description: TDD Test for HW3, Task 5.1.
 * This test will fail ("Red") until the prototype code is added to 
 * the Database.java class. </p>
 * * @author Arnav Pushkarna
 */
public class AdminRequestTDDTest {

    private Database testDatabase;

    /**
     * Before each test, create a *real* connection to the database.
     * This follows the pattern from the JUnit tutorial.
     */
    @BeforeEach
    public void setUp() {
        testDatabase = new Database();
        try {
            // This connects to the real H2 database file
            testDatabase.connectToDatabase();
        } catch (SQLException e) {
            fail("Failed to connect to the database: " + e.getMessage());
        }
    }

    /**
     * After each test, close the connection.
     */
    @AfterEach
    public void tearDown() {
        testDatabase.closeConnection();
    }

    /**
     * This is the single test for our prototype.
     * It follows Requirement 1: Create a request and save it.
     */
    @Test
    public void testCreateAndRetrieveRequest() {
        System.out.println("--- Running testCreateAndRetrieveRequest ---");

        // 1. Setup (Arrange)
        String creator = "ta_user";
        String desc = "Test Request: Please delete post #42.";
        AdminRequest newRequest = new AdminRequest(creator, desc);

        try {
            // 2. Action (Act)
            // This is the "prototype code" we are testing.
            // This will FAIL until Database.java is updated.
            testDatabase.register(newRequest);

            // 3. Assertion (Assert)
            // Check if the ID was updated in the object
            assertNotEquals(-1, newRequest.getRequestID(), "Database did not update the request ID.");

            // Retrieve the request back from the DB to verify it was saved.
            AdminRequest retrievedRequest = testDatabase.getRequestByID(newRequest.getRequestID());

            // Verify all fields match
            assertNotNull(retrievedRequest, "Request was not found in database.");
            assertEquals(creator, retrievedRequest.getCreatorUsername(), "Creator username does not match.");
            assertEquals(desc, retrievedRequest.getDescription(), "Description does not match.");
            assertEquals("Open", retrievedRequest.getStatus(), "Default status is not 'Open'.");

            System.out.println("--- TDD Test PASSED ---");

        } catch (Exception e) {
            // If any exception happens (e.g., "Method not found"), the test fails.
            e.printStackTrace();
            fail("Test failed with exception: " + e.getMessage());
        }
    }
}