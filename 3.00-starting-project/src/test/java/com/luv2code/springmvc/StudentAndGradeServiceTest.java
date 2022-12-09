package com.luv2code.springmvc;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;

@TestPropertySource("/application.properties")
@SpringBootTest
class StudentAndGradeServiceTest {
	
	@Autowired
	private StudentAndGradeService studentService;
	
	@Autowired
	private StudentDao studentDao;
	
	@Autowired
	private JdbcTemplate jdbc;

	//we create sample data
	@BeforeEach
	public void setUpDatabase() {
		//we can use Dao too
		jdbc.execute("insert into student(firstname, lastname, email_address)" +
				"values('Eric', 'Roby', 'eric.roby@luv2code_school.com')");//sql- id is auto, id=1
	}
	
	
	@Test
	void createStudentService() {
		studentService.createStudent("Chad", "Darby", "chad.darby@luv2code_school.com");
		
		CollegeStudent student = studentDao.findByEmailAddress("chad.darby@luv2code_school.com");
		
		assertEquals("chad.darby@luv2code_school.com", student.getEmailAddress(),"find by email");
		
	}

	@Test
	void isStudentNullCheck() {
		assertTrue(studentService.checkIfStudentIsNull(1));
		assertFalse(studentService.checkIfStudentIsNull(0));
	}
	
	@Test
	void deleteStudentService() {
		Optional<CollegeStudent> deletedCollegeStudent = studentDao.findById(1);
		
		assertTrue(deletedCollegeStudent.isPresent(),"Return true");
		
		studentService.deleteStudent(1);
		
		deletedCollegeStudent = studentDao.findById(1);
		
		assertFalse(deletedCollegeStudent.isPresent(),"Return false");
		
	}
	
	@Sql("/insertData.sql") //execute the sql before the test method, first @BeforeEach and then this
	@Test
	void getGradebookService() {
		Iterable<CollegeStudent> iterableCollegeStudents = studentService.getGradebook();
		
		List<CollegeStudent> collegeStudents = new ArrayList<>();
		
		for(CollegeStudent collegeStudent: iterableCollegeStudents) {
			collegeStudents.add(collegeStudent);
		}
		
		assertEquals(5, collegeStudents.size());
	}
	
	
	
	//we delete sample data
	@AfterEach
	void setUpAfterTransaction() {
		jdbc.execute("DELETE FROM student");
	}
	
}
