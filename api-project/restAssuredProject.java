package IBM.RestAssured_Project;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class restAssuredProject 
{
	//Request specification
	RequestSpecification requestSpec;	
	//Response specification
	ResponseSpecification responseSpec;
	//Global variables
	String sshKey;
	int sshKeyId;
	
	@BeforeClass
	public void beforeClass()
	{
		//Create request specification
		requestSpec = new RequestSpecBuilder()
				//Set content type
				.setContentType(ContentType.JSON)
				.addHeader("Authorization", "token xxxxxx")
				//Set base URL
				.setBaseUri("https://api.github.com")
				//Build request specification
				.build();		
		
		sshKey= "ssh-rsa xxxxxx";
	}
	@Test (priority= 1)
	public void addSSHKeys()
	{
		String reqBody = "{\"title\": \"TestKey\", \"key\": \"" + sshKey + "\" }";
		Response response = given().spec(requestSpec)     //Use requestSpec
				            .body(reqBody)                //Send request body
			               	.when().post("/user/keys");   //Send POST request
		
		String resBody = response.getBody().asPrettyString();
		System.out.println(resBody);
		sshKeyId = response.then().extract().path("id");
		
		//Assertion
		response.then().statusCode(201);		
	}
	
	@Test (priority= 2)
	public void getSSHKeys()
	{
		Response response = given().spec(requestSpec)            //Use requestSpec
				.when().get("/user/keys");                       //Send GET Request
	
		String resBody= response.getBody().asPrettyString();
		System.out.println(resBody);
		
		//Assertion
		response.then().statusCode(200);
	}
	
	@Test(priority= 3)
	public void deleteSSHKeys()
	{
		Response response = given().spec(requestSpec)                                   //Use requestSpec
				.pathParam("keyId", sshKeyId).when().delete("/user/keys/{keyId}");      //Send GET Request
		
		String resBody= response.getBody().asPrettyString();
		System.out.println(resBody);
		
		//Assertion
		response.then().statusCode(204);
	}
}
