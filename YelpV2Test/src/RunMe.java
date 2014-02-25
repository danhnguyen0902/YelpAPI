import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.yelp.v2.Business;
import com.yelp.v2.YelpSearchResult;

public class RunMe {

	public void start() {
		// Define your keys, tokens and secrets.  These are available from the Yelp website.  
		String CONSUMER_KEY = "5aybpAtlE94h5DcH5gIM0A";
		String CONSUMER_SECRET = "-SMdO9uN6JhOOumG25GiOO5AkyE";
		String TOKEN = "VdI-5RcDHupx9CZT6wWcHekqOuzUr9b_";
		String TOKEN_SECRET = "wxMcQoBnXh_mZOy5P3c9qpZJwMM";
		
		// Some example values to pass into the Yelp search service.  
		String lat = "37.2250";
		String lng = "-80.4250";
		String category = "restaurants";
		
		// Execute a signed call to the Yelp service.  
		OAuthService service = new ServiceBuilder().provider(YelpV2API.class).apiKey(CONSUMER_KEY).apiSecret(CONSUMER_SECRET).build();
		Token accessToken = new Token(TOKEN, TOKEN_SECRET);
		OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
		request.addQuerystringParameter("ll", lat + "," + lng);
		request.addQuerystringParameter("category", category);
		service.signRequest(accessToken, request);
		Response response = request.send();
		String rawData = response.getBody();
		 
		// Sample of how to turn that text into Java objects.  
		try {
			YelpSearchResult places = new Gson().fromJson(rawData, YelpSearchResult.class);
			
			System.out.println("Your search found " + places.getTotal() + " results.");
			System.out.println("Yelp returned " + places.getBusinesses().size() + " businesses in this request.");
			System.out.println();
			
			for(Business biz : places.getBusinesses()) {
				System.out.println(biz.getName());
				for(String address : biz.getLocation().getAddress()) {					
					System.out.println("  " + address);
				}
				System.out.print("  " + biz.getLocation().getCity());
				System.out.println(biz.getUrl());
				System.out.println();
			}
			
			
		} catch(Exception e) {
			System.out.println("Error, could not parse returned data!");
			System.out.println(rawData);			
		}
			
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new RunMe().start();
	}

}
