package com.luv2code.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import com.luv2code.component.MvcTestingExampleApplication;
import com.luv2code.component.dao.ApplicationDao;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;
import com.luv2code.component.service.ApplicationService;

@SpringBootTest(classes= MvcTestingExampleApplication.class)
class MockAnnotationTest {

	@Autowired
	ApplicationContext context;
	
	@Autowired
	CollegeStudent studentOne;
	
	@Autowired
	StudentGrades studentGrades;
	
	//@Mock
	@MockBean //replaces @Mock, and add the mock to the application context
	private ApplicationDao applicationDao;
	
	//@InjectMocks //will only inject dependencies annotates with @Mock or @Spy, in this case,applicationDao 
	@Autowired //if you use @MockBean, we can use @Autowired
	private ApplicationService applicationService; 
	
	@BeforeEach
	public void beforeEach() {
		studentOne.setFirstname("Eric");
		studentOne.setLastname("Roby");
		studentOne.setEmailAddress("eric.roby@luv2code_school.com");
		studentOne.setStudentGrades(studentGrades);
	}
	
	
	@DisplayName("When & Verify")
	@Test
	public void assertEqualsTestAddGrades() {
		//when someone calls addGradeResultsForSingleClass will get 100 as a return
		when(applicationDao.addGradeResultsForSingleClass(studentGrades.getMathGradeResults())).thenReturn(100.00);
		
		//now we call addGradeResultsForSingleClass in the Service, who calls addGradeResultsForSingleClass from the dao, we will get 100 as we establish above
		assertEquals(100, applicationService.addGradeResultsForSingleClass(studentOne.getStudentGrades().getMathGradeResults()));
		
		//We verify that addGradeResultsForSingleClass was called
		verify(applicationDao).addGradeResultsForSingleClass(studentGrades.getMathGradeResults());
		
		//we ask if addGradeResultsForSingleClass was called 3 times
		verify(applicationDao,times(3)).addGradeResultsForSingleClass(studentGrades.getMathGradeResults());
	}
	
	@DisplayName("Find Gpa")
	@Test
	public void assertEqualsTestFIndGpa() {
		when(applicationDao.findGradePointAverage(studentGrades.getMathGradeResults())).thenReturn(88.31);
		
		assertEquals(88.31,applicationService.findGradePointAverage(studentOne.getStudentGrades().getMathGradeResults()));
	}
	
	@DisplayName("Not null")
	@Test
	public void testAssertNotNull() {
		when(applicationDao.checkNull(studentGrades.getMathGradeResults())).thenReturn(true);
		
		assertNotNull(applicationService.findGradePointAverage(studentOne.getStudentGrades().getMathGradeResults()), "Object should not be null");
	}
	
	@DisplayName("Throw runtime error")
	@Test
	public void throwRuntimeError() {
		CollegeStudent nullStudent = (CollegeStudent) context.getBean("collegeStudent");
		
		//when someone call checkNull will get a RuntimeException
		doThrow(new RuntimeException()).when(applicationDao).checkNull(nullStudent);
		
		//We expect a RuntimeException
		assertThrows(RuntimeException.class, ()-> {
			applicationService.checkNull(nullStudent);
		});
		
		//we ask if checkNull was called 1 time
		verify(applicationDao, times(1)).checkNull(nullStudent);
		
	}
	
	@DisplayName("Multiple calls")
	@Test
	public void stubbingConsecutiveCalls() {
		CollegeStudent nullStudent= (CollegeStudent) context.getBean("collegeStudent");
		
		//First time will throw an Exception, second a String
		when(applicationDao.checkNull(nullStudent))
			.thenThrow(new RuntimeException()) //first
			.thenReturn("Don't throw exception second time"); // second
		
		//for first time, we expect a RuntimeException
		assertThrows(RuntimeException.class, () -> applicationService.checkNull(nullStudent));
		
		//for second time, we expect a String
		assertEquals("Don't throw exception second time", applicationService.checkNull(nullStudent));
		
		//Ask if the method was called 2 times
		verify(applicationDao, times(2)).checkNull(nullStudent);
		
	}
	
}
