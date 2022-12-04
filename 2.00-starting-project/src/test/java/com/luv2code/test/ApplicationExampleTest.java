package com.luv2code.test; //for spring , you have to set up the same name in the package

//main/java/com.luv2code.component   -> class
//test/java/com.luv2code.component  -> testClass
//If you don't want to do that, then specificity the class in classes

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import com.luv2code.component.MvcTestingExampleApplication;
import com.luv2code.component.models.CollegeStudent;
import com.luv2code.component.models.StudentGrades;

@SpringBootTest(classes = MvcTestingExampleApplication.class) // Here you have to specify
class ApplicationExampleTest {

	private static int count = 0;

	@Value("${info.app.name}") // we access to the application.properties values
	private String appInfo;

	@Value("${info.app.description}")
	private String appDescription;

	@Value("${info.app.version}")
	private String appVersion;

	@Value("${info.school.name}")
	private String schoolName;

	@Autowired
	ApplicationContext context;

	@Autowired
	CollegeStudent student;

	@Autowired
	StudentGrades studentGrades;

	@BeforeEach
	public void beforeEach() {
		count = count + 1;
		List<Double> MathGradeR = new ArrayList<Double>();
		MathGradeR.add(100.0);
		MathGradeR.add(85.0);
		MathGradeR.add(76.50);
		MathGradeR.add(91.75);

		System.out.println("Testing: " + appInfo + "wich is " + appDescription + " Version: " + appVersion
				+ ". Execution of test method " + count);
		student.setFirstname("eric");
		student.setLastname("Roby");
		student.setEmailAddress("eric.roby@luv2code_school.com");
		studentGrades.setMathGradeResults(MathGradeR);
		student.setStudentGrades(studentGrades);
	}

	@DisplayName("Add grade results for student grades")
	@Test
	void addGradeResultsForStudentGrades() {
		// lets compare A with B, if B is equal to A, then true
		assertEquals(353.25,
				studentGrades.addGradeResultsForSingleClass(student.getStudentGrades().getMathGradeResults()));// A =
																												// 353.25
																												// / B
	}

	@DisplayName("Add grade results for student grades not equal")
	@Test
	void addGradeResultsForStudentGradesNotEqual() {
		// lets compare A with B, if B is not equal to A, then true
		assertNotEquals(0,
				studentGrades.addGradeResultsForSingleClass(student.getStudentGrades().getMathGradeResults()));// A = 0
																												// / B
	}

	@DisplayName("Is grade greater")
	@Test
	void isGradeGreaterStudentGrades() {
		assertTrue(studentGrades.isGradeGreater(90, 75), "Failure - should be true"); // we just ask if 90>75
	}

	@DisplayName("Is grade greater false")
	@Test
	void isGradeGreaterStudentGradesAssertFalse() {
		assertFalse(studentGrades.isGradeGreater(89, 92), "Failure - should be false");// we just ask if 89>92
	}

	@DisplayName("Check Null for student grades")
	@Test
	void checkNullForStudentGrades() {
		assertNotNull(studentGrades.checkNull(student.getStudentGrades().getMathGradeResults()),
				"object should not be null");
	}

	@DisplayName("Create student without grade init")
	@Test
	public void createStudentWithoutGradesInit() {
		CollegeStudent studentTwo = context.getBean("collegeStudent", CollegeStudent.class);
		studentTwo.setFirstname("Chad");
		studentTwo.setLastname("Darby");
		studentTwo.setEmailAddress("chad.darby@luv2code_school.con");
		assertNotNull(studentTwo.getFirstname());
		assertNotNull(studentTwo.getLastname());
		assertNotNull(studentTwo.getEmailAddress());
		assertNull(studentGrades.checkNull(studentTwo.getStudentGrades()));
	}

	@DisplayName("Verify students are prototypes")
	@Test
	void verifyStudentArePrototypes() {
		CollegeStudent studentTwo = context.getBean("collegeStudent", CollegeStudent.class);
		assertNotSame(student, studentTwo); // If they were not prototypes, we would have the same student that is in
											// the spring context
	}

	@DisplayName("Find grade Point Average")
	@Test
	void findGradePointAverage() {
		assertAll("Testing all assertEquals",
				() -> assertEquals(353.25,
						studentGrades.addGradeResultsForSingleClass(student.getStudentGrades().getMathGradeResults())),
				() -> assertEquals(88.31,
						studentGrades.findGradePointAverage(student.getStudentGrades().getMathGradeResults()))
				);
	}

}
