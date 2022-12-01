package com.luv2code.junitdemo;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;


//here I use a custom displayName, Junit has anothers
//@DisplayNameGeneration(RemplaceCamelCase.class) , //Since I have it specified in the junit-platform.properties , it is not necessary here
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) //priority -> -7,-2,1,2 minor to major
class DemoUtilsTest {
	
	DemoUtils demoUtils;
	
	@BeforeEach
	void setUpBeforeEach() {
		demoUtils = new DemoUtils();//we instantiate a different demoUtils for each testMethod
		System.out.println("@BeforeEach executes before the execution of each test method");
	}
	
	@AfterEach
	void tearDownAfterEach() {
		System.out.println("Running @AfterEach");

	}
	
	@BeforeAll //executed only once, before all test method
	static void setupBeforeEachClass() { //must be static
		System.out.println("@BeforeAll executes only once before all test methods execution in the class");
	}
	
	@AfterAll //executed only once, after all test method
	static void tearDownAfterAll() { //must be static
		System.out.println("@AfterAll executes only once after all test methods execution in the class");
	}
	

	@Test
	//@DisplayName("Equals and not Equals")    //Manual way to name the method
	@Order(-5) //this method will be executed first -> we need @TestMethodOrder too
	void testEqualsAndNotEquals() {//can have any name
		
		assertEquals(6, demoUtils.add(2, 4),"2+4 must be 6");
		assertNotEquals(6, demoUtils.add(1, 9),"1+9 must not be 6");
	}
	
	@Test
	//@DisplayName("Null and not Null")       //Manual way to name the method
	void testNullAndNotNull() {

		String str1= null;
		String str2="luv2code";

		assertNull(demoUtils.checkNull(str1),"object should be null");
		assertNotNull(demoUtils.checkNull(str2),"object should not be null");
	}

	@Test
	@Order(1) //this method will be the second -> we need @TestMethodOrder too
	void testSameAndNotSame() {
		String str= "luv2code";
		
		assertSame(demoUtils.getAcademy(),demoUtils.getAcademyDuplicate(),"Objects should refer to same object"); 
		assertNotSame(str, demoUtils.getAcademy(),"Objects should not refer to the same object"); //str is not referring to demoUtils
	}
	
	@Test
	@Order(2) //this method will be the third -> we need @TestMethodOrder too
	void testTrueFalse() {
		int gradeOne = 10;
		int gradeTwo = 5;
		
		assertTrue(demoUtils.isGreater(gradeOne, gradeTwo),"This should return true");
		assertFalse(demoUtils.isGreater(gradeTwo, gradeOne),"This should return false");
	}
	
	@Test
	void testArrayEquals() {
		String[] stringArray = {"A","B","C"};
		//We test if both arrays have the same values
		assertArrayEquals(stringArray,demoUtils.getFirstThreeLettersOfAlphabet(),"Arrays should be the same");
	}
	
	@Test
	void testIterableEquals() {
		List<String> theList = List.of("luv","2","code");
		//We test if both list have the same values
		assertIterableEquals(theList,demoUtils.getAcademyInList(),"Expected list should be same as actual list");
	}
	
	@Test
	void testLinesMatch() {
		List<String> theList = List.of("luv","2","code");
		//We test if both list of strings have the same values
		assertLinesMatch(theList,demoUtils.getAcademyInList(),"Lines should match");
	}
	
	@Test
	void testThrowsAndDoesNotThrow() {   
		assertThrows(Exception.class,()-> {demoUtils.throwException(-1);}, "Should throw exception"); //we expect and Exception type
		assertDoesNotThrow(()-> {demoUtils.throwException(5);}, "Should not throw exception");
	}
	
	@Test
	void testTimeout() {
		//with Duration, we establish the time that checkTimeout must to last
		assertTimeoutPreemptively(Duration.ofSeconds(3), () -> {demoUtils.checkTimeout();},
				"Method should execute in 3 seconds");
	}
	
	
	
	
	
	
	
}
