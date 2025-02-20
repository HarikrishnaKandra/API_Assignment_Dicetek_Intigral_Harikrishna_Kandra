package api.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.BookerEndpoints;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

/**
 * This class contains API test cases for the Restful Booker application.
 * 
 * It validates: 1. Creating a new booking (POST request) 2. Retrieving an
 * existing booking (GET request)
 * 
 * Test execution is managed using TestNG framework.
 */

public class BookerPositiveTests {

	// Variable to store the extracted booking ID for further validation
	private int bookingId;
	private Faker faker;
    private JSONObject TestData;
    private JsonPath jsonPath;
    private Response response;

	private static final Logger logger = LogManager.getLogger(BookerPositiveTests.class);

	/**
	 * Setup method to initialize any required configurations before tests.
	 */
	@BeforeClass
	public void setUp() {
		
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		logger.debug("********** Test Execution Started **********");
		
		faker = new Faker(); // Initialize Faker for generating random data
		
		// Create booking dates JSON Object
		JSONObject bookingDates = new JSONObject();
		bookingDates.put("checkin", "2022-01-01");
		bookingDates.put("checkout", "2024-01-01");

		// Create main booking JSON Object
		TestData = new JSONObject();
		TestData.put("firstname", faker.name().firstName());
		TestData.put("lastname", faker.name().lastName());
		TestData.put("totalprice", faker.number().randomDouble(2, 50, 500)); // Use double value, not string
		TestData.put("depositpaid", faker.bool().bool());
		TestData.put("bookingdates", bookingDates); // Nested JSON Object
		TestData.put("additionalneeds", faker.food().dish().replaceAll("[^a-zA-Z0-9 ]", "")); // Cleaning special characters
	}

	/**
	 * Test case to create a new booking.
	 * 
	 * - Sends a POST request to create a new booking - Extracts the booking ID from
	 * the response - Validates the status code and ensures a valid booking ID is
	 * generated
	 */

	@Test(priority = 1)
	public void testCreateBooking() {
		logger.debug("Executing Test: testCreateBooking");
		logger.info("*********** Creating Booking Item *************");
	    response = BookerEndpoints.addItem(TestData);
	    response.then().log().all();

	    jsonPath = response.jsonPath();
	    bookingId = jsonPath.getInt("bookingid");
	    Assert.assertTrue(bookingId > 0, "Booking ID should be greater than 0");
	
	    logger.debug("Booking created successfully. Booking ID: {}", bookingId);
	}
	
	 /**
     * Validate status code of booking creation.
     */
	
	@Test(priority = 2, dependsOnMethods = "testCreateBooking")
	public void validateAddItemStatusCode() {
	
		Assert.assertEquals(response.getStatusCode(), 200, "Booking creation failed!");
	}
	
	 /**
     * Validate response time is within acceptable limit.
     */
	
	@Test(priority = 3, dependsOnMethods = "testCreateBooking")
	public void validateAddItemResponseTime() {
		
		 long responseTime = response.getTime();
	        Assert.assertTrue(responseTime < 4000, "Response time is too high!");
	}
	
	 /**
     * Validate Content-Type header.
     */
	
	@Test(priority = 4, dependsOnMethods = "testCreateBooking")
	public void validateContentTypeHeader() {
		
		String contentType = response.getHeader("Content-Type");
        Assert.assertEquals(contentType, "application/json; charset=utf-8", "Incorrect Content-Type!");
	}
	
	 /**
     * Validate response body fields.
     */
	
	@Test(priority = 5, dependsOnMethods = "testCreateBooking")
	public void validateResponseBodyFields() {
		Assert.assertNotNull(jsonPath.getString("booking.firstname"), "Firstname is missing!");
        Assert.assertNotNull(jsonPath.getString("booking.lastname"), "Lastname is missing!");
        Assert.assertTrue(jsonPath.getInt("booking.totalprice") > 0, "Total price must be positive!");
        Assert.assertNotNull(jsonPath.getBoolean("booking.depositpaid"), "DepositPaid field is missing!");
        Assert.assertNotNull(jsonPath.getString("booking.bookingdates.checkin"), "Check-in date is missing!");
        Assert.assertNotNull(jsonPath.getString("booking.bookingdates.checkout"), "Check-out date is missing!");
        Assert.assertNotNull(jsonPath.getString("booking.additionalneeds"), "Additional needs field is missing!");
	}
	
	/**
     * Validate date format in the response.
     */
	
	@Test(priority = 6, dependsOnMethods = "testCreateBooking")
	public void validateBookingDatesFormat() {
	    String checkin = jsonPath.getString("booking.bookingdates.checkin");
        String checkout = jsonPath.getString("booking.bookingdates.checkout");
        Assert.assertTrue(checkin.matches("\\d{4}-\\d{2}-\\d{2}"), "Check-in date format is incorrect!");
        Assert.assertTrue(checkout.matches("\\d{4}-\\d{2}-\\d{2}"), "Check-out date format is incorrect!");
	}
	
	/**
	 * Test case to retrieve an existing booking using the extracted booking ID.
	 * 
	 * - Sends a GET request with the booking ID - Validates the response status
	 * code - Ensures booking details are retrieved successfully
	 * 
	 * This test depends on `testCreateBooking` to execute first.
	 */

	@Test(priority = 7, dependsOnMethods = "testCreateBooking")
	public void testGetBooking() {
		
		logger.debug("==== Starting test: Get Booking Details ====");
        
		// Send request to fetch booking details using extracted booking ID
		 response = BookerEndpoints.readItem(bookingId);

		// Log the response details for debugging
		response.then().log().all();
		jsonPath = response.jsonPath();
		// Assert that the response status code is 200 (OK)
		Assert.assertEquals(response.getStatusCode(), 200, "Failed to fetch booking details!");
		
		logger.debug("Booking details retrieved successfully for Booking ID: {}", bookingId);
	}
	
	/**
     * Validate status code for Get Booking API.
     */
	
	@Test(priority = 8, dependsOnMethods = "testGetBooking")
	public void validateReadItemStatusCode() {
	   
	    Assert.assertEquals(response.getStatusCode(), 200, "Unexpected status code!");
	}
	
	/**
     * Validate response time for Get Booking API.
     */
	
	@Test(priority = 9, dependsOnMethods = "testGetBooking")
	public void validateReadItemResponseTime() {
	    
	    Assert.assertTrue(response.getTime() < 3000, "Response time is too high!");
	}
	
	/**
     * Validate Content-Type header for Get Booking API.
     */
	
	@Test(priority = 10, dependsOnMethods = "testGetBooking")
	public void validatereadContentType() {
	    
	    Assert.assertEquals(response.getHeader("Content-Type"), "application/json; charset=utf-8", "Incorrect Content-Type!");
	}
	
	 /**
     * Validate response body fields for Get Booking API.
     */
	
	@Test(priority = 11, dependsOnMethods = "testGetBooking")
	public void validatereadItesmBookingFields() {
	    Assert.assertNotNull(jsonPath.getString("firstname"), "Firstname is missing!");
	    Assert.assertNotNull(jsonPath.getString("lastname"), "Lastname is missing!");
	    Assert.assertTrue(jsonPath.getInt("totalprice") > 0, "Total price must be positive!");
	    Assert.assertNotNull(jsonPath.getBoolean("depositpaid"), "DepositPaid field is missing!");
	    Assert.assertNotNull(jsonPath.getString("additionalneeds"), "Additional needs field is missing!");
	}
	
	/**
     * Validate date format for Get Booking API.
     */
	
	@Test(priority = 12, dependsOnMethods = "testGetBooking")
	public void validatereadItemDateFormat() {
	    String checkin = jsonPath.getString("bookingdates.checkin");
	    String checkout = jsonPath.getString("bookingdates.checkout");

	    Assert.assertTrue(checkin.matches("\\d{4}-\\d{2}-\\d{2}"), "Check-in date format is incorrect!");
	    Assert.assertTrue(checkout.matches("\\d{4}-\\d{2}-\\d{2}"), "Check-out date format is incorrect!");
	}
	
}
