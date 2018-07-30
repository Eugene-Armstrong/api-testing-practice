package exercises;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static java.lang.Integer.parseInt;
import static org.hamcrest.Matchers.equalTo;


public class RestAssuredExercises2Test {

	private static RequestSpecification requestSpec;

	@BeforeAll
	public static void createRequestSpecification() {
		requestSpec = new RequestSpecBuilder().
			setBaseUri("http://localhost").
			setPort(9876).
			setBasePath("/api/f1").
			build();
	}
	
	/*******************************************************
	 * Use junit-jupiter-params for @ParameterizedTest that
	 * specifies in which country
	 * a specific circuit can be found (specify that Monza 
	 * is in Italy, for example) 
	 ******************************************************/
	static Stream<Arguments> countyDataProvider() {
		return Stream.of(
				Arguments.of("monza", "Italy"),
				Arguments.of("sepang", "Malaysia")
		);
	}
	@ParameterizedTest
	@MethodSource("countyDataProvider")
	public void checkCircuitInWhichCountry(String circuitId, String country) {
		given().
				spec(requestSpec).
				pathParam("circuit", circuitId).
				when().
				get("circuits/{circuit}.json").
				then().
				assertThat().
				body("MRData.CircuitTable.Circuits[0].Location.country", equalTo(country));
	}

//	@Test
//	public void checkASpecificCircuitCanBeFound(){
//		given().
//				spec(requestSpec).
//				when().
//				then();
//	}

	/*******************************************************
	 * Use junit-jupiter-params for @ParameterizedTest that specifies for all races
	 * (adding the first four suffices) in 2015 how many  
	 * pit stops Max Verstappen made
	 * (race 1 = 1 pitstop, 2 = 3, 3 = 2, 4 = 2)
	 ******************************************************/
	static Stream<Arguments> numberDataProvider() {
		return Stream.of(
				Arguments.of("max_verstappen", "1")
		);
	}
	@ParameterizedTest
	@MethodSource("numberDataProvider")
	public void checkNumberOfPitStops(String driver, String number){
		given().
				spec(requestSpec).
				pathParam("driver", driver).
				when().
				get("2015/2/drivers/{driver}/pitstops.json").
				then().
				assertThat().
				body("MRData.RaceTable.Races.PitStops.size()", equalTo(parseInt(number)));
	}

	/*******************************************************
	 * Request data for a specific circuit (for Monza this 
	 * is /circuits/monza.json)
	 * and check the country this circuit can be found in
	 ******************************************************/
	@Test
	public void checkCountryForCircuit() {
		given().
				spec(requestSpec).
				when().
				get("/circuits/monza.json").
				then().
				assertThat().
				body("MRData.CircuitTable.Circuits[0].circuitId", equalTo("monza"));
	}
	
	/*******************************************************
	 * Request the pitstop data for the first four races in
	 * 2015 for Max Verstappen (for race 1 this is
	 * /2015/1/drivers/max_verstappen/pitstops.json)
	 * and verify the number of pit stops made
	 ******************************************************/
	@Test
	public void checkNumberOfPitstopsForMaxVerstappenIn2015() {
		given().
				spec(requestSpec).
				when().
				then();
	}
}