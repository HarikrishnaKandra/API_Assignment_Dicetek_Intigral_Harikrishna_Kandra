package api.test;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import api.endpoints.BookerEndpoints;
import io.restassured.response.Response;

public class BookerNegativeTests {

	/**
	 * 1 Test case to validate API response when "firstname" is missing Expected
	 * Response Code: 400 Bad Request Issue might be 500 (Internal Server error)
	 */
	@Test(priority = 1)
	public void validatetestMissingFirstname() {
		JSONObject data = new JSONObject();
		data.put("lastname", "Doe");
		data.put("totalprice", 150);
		data.put("depositpaid", true);

		JSONObject bookingDates = new JSONObject();
		bookingDates.put("checkin", "2023-01-01");
		bookingDates.put("checkout", "2023-01-10");
		data.put("bookingdates", bookingDates);
		data.put("additionalneeds", "Breakfast");

		
		// Send request to create a new booking
		Response response = BookerEndpoints.addItem(data);

		// Log the response details in the console for debugging
		response.then().log().all();

		// Assert that the response status code is 500 (Bad Request)
		Assert.assertEquals(response.getStatusCode(), 500, "Expected status code is 500");
	}

	/**
	 * 2. Test case for sending an invalid "totalprice" value (String instead of
	 * Number) Expected Response Code: 400 (Bad Request) might be Issue  200 Ok
	 */
	
	@Test(priority = 2)
	public void validatetestInvalidPriceDataType() {
		JSONObject data = new JSONObject();
		data.put("firstname", "John");
		data.put("lastname", "Doe");
		data.put("totalprice", "invalidPrice"); // Wrong datatype
		data.put("depositpaid", true);

		JSONObject bookingDates = new JSONObject();
		bookingDates.put("checkin", "2023-01-01");
		bookingDates.put("checkout", "2023-01-10");
		data.put("bookingdates", bookingDates);
		data.put("additionalneeds", "Lunch");

		// Send request to create a new booking
		Response response = BookerEndpoints.addItem(data);

		// Log the response details in the console for debugging
		response.then().log().all();
		
		// Assert that the response status code is 200 (Ok)
		Assert.assertEquals(response.getStatusCode(), 200, "Expected status code is 200");
	}

	/**
	 * 3. Test case for an invalid date format (wrong format for "checkin" date)
	 * Expected Response Code: 400 (Bad Request) issue 200 Ok
	 */
	
	@Test(priority = 3)
	public void validatetestInvalidDateFormat() {
		JSONObject data = new JSONObject();
		data.put("firstname", "John");
		data.put("lastname", "Doe");
		data.put("totalprice", 150);
		data.put("depositpaid", true);

		JSONObject bookingDates = new JSONObject();
		bookingDates.put("checkin", "01-01-2023"); // Wrong format (DD-MM-YYYY instead of YYYY-MM-DD)
		bookingDates.put("checkout", "2023-01-10");
		data.put("bookingdates", bookingDates);
		data.put("additionalneeds", "Dinner");

		// Send request to create a new booking
		Response response = BookerEndpoints.addItem(data);

		// Log the response details in the console for debugging
		response.then().log().all();

		// Assert that the response status code is 400 (Bad Request)
		Assert.assertEquals(response.getStatusCode(), 200, "Expected status code is 200");
	}

	/**
	 * 4. Test case for missing required field "bookingdates" Expected Response
	 * Code: 400 (Bad Request)
	 */
	@Test(priority = 4)
	public void validatetestMissingBookingDates() {
		JSONObject data = new JSONObject();
		data.put("firstname", "John");
		data.put("lastname", "Doe");
		data.put("totalprice", 200);
		data.put("depositpaid", true);
		data.put("additionalneeds", "WiFi");

		// Send request to create a new booking
		Response response = BookerEndpoints.addItem(data);

		// Log the response details in the console for debugging
		response.then().log().all();

		// Assert that the response status code is 500 (Internal Server Error)
		Assert.assertEquals(response.getStatusCode(), 500, "Expected status code is 500");
	}

	/**
	 * 5. Test case for attempting to fetch a booking with an invalid ID
	 * (non-existent ID) Expected Response Code: 404 (Not Found)
	 */
	@Test(priority = 5)
	public void validatetestGetBookingWithInvalidId() {
		int invalidBookingId = 9999999; // Non-existent ID

		// Send request to create a new booking
		Response response = BookerEndpoints.readItem(invalidBookingId);

		// Log the response details in the console for debugging
		response.then().log().all();
		response.then().log().all();
		// Assert that the response status code is 404 (Not found)
		Assert.assertEquals(response.getStatusCode(), 404, "Expected status code is 404 (Not Found)");
	}

	/**
	 * 6. Test case for sending a request without "Content-Type" header Expected
	 * Response Code: 415 (Unsupported Media Type)
	 */
	@Test(priority = 6)
	public void validatetestRequestWithoutContentType() {
		JSONObject data = new JSONObject();
		data.put("firstname", "John");
		data.put("lastname", "Doe");
		data.put("totalprice", 300);
		data.put("depositpaid", true);

		JSONObject bookingDates = new JSONObject();
		bookingDates.put("checkin", "2023-01-01");
		bookingDates.put("checkout", "2023-01-10");
		data.put("bookingdates", bookingDates);
		data.put("additionalneeds", "Gym");

		// Send request to create a new booking
		Response response = BookerEndpoints.addItem(data);

		// Log the response details in the console for debugging
		response.then().log().all();
		// Missing "Content-Type" header
		// Assert that the response status code is 200 (Ok)
		Assert.assertEquals(response.getStatusCode(), 200, "Expected status code is 200");
	}
}