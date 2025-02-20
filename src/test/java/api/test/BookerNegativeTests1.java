package api.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import api.endpoints.BookerEndpoints;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class BookerNegativeTests1 {
	
	private static final Logger logger = LogManager.getLogger(BookerNegativeTests1.class);
	
	/**
     * Data Provider for Negative Test Cases
     * Each row contains:
     * - Test Case Name
     * - firstname, lastname
     * - totalprice (can be String or Integer)
     * - depositpaid (boolean)
     * - checkin & checkout dates
     */
	
	@BeforeClass
    public void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        logger.debug("Starting API Negative Tests...");
    }
	
	
	@DataProvider(name = "negativeTestCases")
	public Object[][] negativeTestData() {
	    return new Object[][] {
	        { "missingFirstname", "", "Doe", 100, true, "2023-01-01", "2023-01-10" },
	        { "invalidPrice", "John", "Doe", "invalid", true, "2023-01-01", "2023-01-10"},
	        { "invalidCheckinFormat", "John", "Doe", 100, true, "01-01-2023", "2023-01-10"},
	        
	    };
	}

	/**
     * Runs negative test cases based on data provider
     * 
     * @param testCase  - Name of the test case
     * @param firstname - First name of the user
     * @param lastname  - Last name of the user
     * @param totalprice - Total price (can be invalid data type)
     * @param depositpaid - Whether deposit is paid
     * @param checkin - Check-in date
     * @param checkout - Check-out date
     */
	
	@Test(dataProvider = "negativeTestCases", priority = 1)
	public void validatetestNegativeScenarios(String testCase, String firstname, String lastname, Object totalprice, boolean depositpaid, String checkin, String checkout) {
		
		logger.debug("Executing test case: " + testCase);
		
		JSONObject data = new JSONObject();
	    data.put("firstname", firstname);
	    data.put("lastname", lastname);
	    data.put("totalprice", totalprice);
	    data.put("depositpaid", depositpaid);

	    JSONObject bookingDates = new JSONObject();
	    bookingDates.put("checkin", checkin);
	    bookingDates.put("checkout", checkout);
	    data.put("bookingdates", bookingDates);

	    data.put("additionalneeds", "None");

	   // Send request to create a new booking
	   Response response = BookerEndpoints.addItem(data);

	    response.then().log().all();
	    Assert.assertEquals(response.getStatusCode(), 200, "Expected status code is 200");
	    //Assert.assertTrue(response.asString().contains(expectedError), "Expected error message: " + expectedError);
	}
}
