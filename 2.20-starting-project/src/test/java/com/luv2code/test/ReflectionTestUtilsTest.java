package com.luv2code.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

import com.luv2code.component.MvcTestingExampleApplication;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;

@SpringBootTest(classes=MvcTestingExampleApplication.class )
class ReflectionTestUtilsTest {
	
	@Autowired
	ApplicationContext context;
	
	@Autowired
	CollegeStudent studentOne;
	
	@Autowired
	StudentGrades studentGrades;
	

	@BeforeEach
	public void beforeEach() {
		studentOne.setFirstname("Eric");
		studentOne.setLastname("Roby");
		studentOne.setEmailAddress("eric.roby@luv2code_school.com");
		studentOne.setStudentGrades(studentGrades);
		
		//We access the field id directly, without setId method
		ReflectionTestUtils.setField(studentOne, "id", 1);
		
	}
	
	@Test
	public void getPrivateField() {
		//we access the field id directly, without getId method
		assertEquals(1,ReflectionTestUtils.getField(studentOne,"id"));
		
		// What's the use of this, we can only use getters and setters? 
		//well, in case they don't exist, we can still access the field
	}
	
	
	@Test//now we call a private method
	public void invokePrivateMethod() {
		assertEquals("Eric 1",
				ReflectionTestUtils.invokeMethod(studentOne, "getFirstNameAndId"),
				"Fail private method not call");
	}
	
	

}
