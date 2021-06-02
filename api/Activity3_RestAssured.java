package Activities;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class Activity3_RestAssured 
{
	RequestSpecification requestSpec;
	ResponseSpecification responseSpec;

	@BeforeClass
	public void setUp() 
	{
		requestSpec = new RequestSpecBuilder()
				//Set content type
				.setContentType(ContentType.JSON)
				//Set base URL
				.setBaseUri("https://petstore.swagger.io/v2/pet")
				//Build request specification
				.build();

		responseSpec = new ResponseSpecBuilder()		
				.expectStatusCode(200)
				.expectContentType("application/json")
				.expectBody("status", equalTo("alive"))
				.build();
	}

	@DataProvider
	public Object[][] petInfoProvider()
	{
		//Setting parameters to pass to test case
		Object[][] testData = new Object[][]
	   { 
			{ 77932, "Riley", "alive" }, 
			{ 77933, "Hansel", "alive" }
		};
				return testData;
	}

	@Test(priority=1)
	public void addPets() 
	{
		String reqBody = "{\"id\": 77932, \"name\": \"Riley\", \"status\": \"alive\"}";
		Response response = given().spec(requestSpec) //Use requestSpec
				.body(reqBody)                        //Send request body
				.when().post();                       //Send POST request

		reqBody = "{\"id\": 77933, \"name\": \"Hansel\", \"status\": \"alive\"}";
		response = given().spec(requestSpec)        //Use requestSpec
				.body(reqBody)                      //Send request body
				.when().post();                    //Send POST request

		//Assertions
		response.then().spec(responseSpec);   
	}

	@Test(dataProvider = "petInfoProvider", priority=2)
	public void getPets(int id, String name, String status) 
	{
		Response response = given().spec(requestSpec) 
				.pathParam("petId", id) 
				.when().get("/{petId}"); 

		//Print response
		System.out.println(response.asPrettyString());
		//Assertions
		response.then()
		.spec(responseSpec) 
		.body("name", equalTo(name));
	}

	// Test case using a DataProvider
	@Test(dataProvider = "petInfoProvider", priority=3)
	public void deletePets(int id, String name, String status) 
	{
		Response response = given().spec(requestSpec)
				.pathParam("petId", id) 
				.when().delete("/{petId}"); 
		
		//Assertions
		response.then().body("code", equalTo(200));
	}

}