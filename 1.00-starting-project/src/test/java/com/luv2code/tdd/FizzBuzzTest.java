package com.luv2code.tdd;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

//globalNameGenerator in -> junit-platform.properties
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FizzBuzzTest {

	@Test
	@Order(1)
	void testForDivisibleByThree() {
		String expected = "Fizz";

		assertEquals(expected, FizzBuzz.compute(3), "Should return Fizz");
	}

	@Test
	@Order(2)
	void testForDivisibleByFive() {
		String expected = "Buzz";

		assertEquals(expected, FizzBuzz.compute(5), "Should return Buzz");
	}
	
	@Test
	@Order(3)
	void testForDivisibleByThreeAndFive() {
		String expected = "FizzBuzz";

		assertEquals(expected, FizzBuzz.compute(15), "Should return FizzBuzz");
	}
	
	@Test
	@Order(4)
	void testForNotDivisibleByThreeOrFive() {
		String expected = "1";

		assertEquals(expected, FizzBuzz.compute(1), "Should return 1");
	}
	
	@ParameterizedTest(name="value={0} , expected={1}")
	@CsvFileSource(resources="/small-test-data.csv") //JUnit will run the test multiple times and supply the data for the parameters
	@Order(5)
	void testSmallDataFile(int value, String expected) {
		assertEquals(expected, FizzBuzz.compute(value));
	}
	
	@ParameterizedTest(name="value={0} , expected={1}")
	@CsvFileSource(resources="/medium-test-data.csv") //JUnit will run the test multiple times and supply the data for the parameters
	@Order(6)
	void testMediumDataFile(int value, String expected) {
		assertEquals(expected, FizzBuzz.compute(value));
	}
	
	@ParameterizedTest(name="value={0} , expected={1}")
	@CsvFileSource(resources="/large-test-data.csv") //JUnit will run the test multiple times and supply the data for the parameters
	@Order(7)
	void testLargeDataFile(int value, String expected) {
		assertEquals(expected, FizzBuzz.compute(value));
	}
	
	
}
