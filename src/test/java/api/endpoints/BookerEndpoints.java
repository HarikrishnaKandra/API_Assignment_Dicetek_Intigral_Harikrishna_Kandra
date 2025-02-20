package api.endpoints;

import static io.restassured.RestAssured.given;
import org.json.JSONObject;
import io.restassured.response.Response;

/**
 * This class implements CRUD operations (Create & Read) for the Booker API. It
 * interacts with the API endpoints defined in the Routes class.
 */

public class BookerEndpoints {
	// Faker instance to generate random test data
	/**
	 * Sends a POST request to create a new booking.
	 * 
	 * @return Response object containing the API response.
	 */

	public static Response addItem(JSONObject data) {

		// Send POST request with JSON body
		Response response = given().contentType("application/json").body(data.toString())

				.when().post(Routes.ADD_BOOKING);

		return response; // Return API response
	}

	/**
	 * Sends a GET request to retrieve booking details for a given booking ID.
	 *
	 * @param bookingid The ID of the booking to retrieve.
	 * @return Response object containing the booking details.
	 */
	public static Response readItem(int bookingid) {

		// Send GET request using path parameter
		Response response = given().pathParam("bookingid", bookingid)

				.when().get(Routes.GET_BOOKING);

		return response; // Return API response
	}
}
