package Activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.testng.annotations.Test;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class Activity2_RestAssured
{
	//Base URL
	final static String ROOT_URI = "https://petstore.swagger.io/v2/user";

	@Test(priority=1)
	public void addUserFromFile() throws IOException
	{
		//Import JSON file
		FileInputStream inputJSON = new FileInputStream("src/test/java/Activities/userinfo.json");
		//Read JSON file as String
		byte[] buffer = new byte[10];
		StringBuilder sb = new StringBuilder();
		while (inputJSON.read(buffer) != -1) 
		{
			sb.append(new String(buffer));
			buffer = new byte[10];
		}
		String reqBody = sb.toString();

		Response response = 
				given().contentType(ContentType.JSON) // Set headers
				.body(reqBody)                        //Pass request body from file
				.when().post(ROOT_URI);              //Send POST request

		inputJSON.close();

		//Assertion
		response.then().body("code", equalTo(200));
		response.then().body("message", equalTo("1108"));
	}

	@Test(priority=2)
	public void getUserInfo() 
	{
		//Import JSON file to write to
		File outputJSON = new File("src/test/java/Activities/GETresponse.json");

		Response response = 
				given().contentType(ContentType.JSON) //Set headers
				.pathParam("username", "AmruthaV")    //Pass request body from file
				.when().get(ROOT_URI + "/{username}"); //Send POST request

		String resBody = response.getBody().asPrettyString();

		try {
			// Create JSON file
			outputJSON.createNewFile();
			// Write response body to external file
			FileWriter writer = new FileWriter(outputJSON.getPath());
			writer.write(resBody);
			writer.close();
		} 
		catch (IOException excp) 
		{
			excp.printStackTrace();
		}

		//Assertion
		response.then().body("id", equalTo(1108));
		response.then().body("username", equalTo("AmruthaV"));
		response.then().body("firstName", equalTo("Amrutha"));
		response.then().body("lastName", equalTo("V"));
		response.then().body("email", equalTo("amrutha@mail.com"));
		response.then().body("password", equalTo("password123"));
		response.then().body("phone", equalTo("9812763450"));
	}

	@Test(priority=3)
	public void deleteUser() throws IOException 
	{
		Response response = 
				given().contentType(ContentType.JSON) // Set headers
				.pathParam("username", "AmruthaV") // Add path parameter
				.when().delete(ROOT_URI + "/{username}"); // Send POST request

		//Assertion
		response.then().body("code", equalTo(200));
		response.then().body("message", equalTo("AmruthaV"));
	}
}