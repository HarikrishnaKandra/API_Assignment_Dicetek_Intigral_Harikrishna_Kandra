package api.endpoints;

/**
 * This class contains the API endpoint URLs used for the application. It helps
 * in centralizing and managing API routes effectively.
 */

public final class Routes {
	
	// Base URL
	public static final String BASE_URL = "https://restful-booker.herokuapp.com/";

	// Booker Module Endpoints
	public static String ADD_BOOKING = BASE_URL + "booking";
	public static String GET_BOOKING = BASE_URL + "booking/{bookingid}";

	// Add more endpoints as needed for other modules...

}
