package com.luv2code.springmvc;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.GradebookCollegeStudent;
import com.luv2code.springmvc.models.HistoryGrade;
import com.luv2code.springmvc.models.MathGrade;
import com.luv2code.springmvc.models.ScienceGrade;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;

@TestPropertySource("/application-test.properties")
@SpringBootTest
class StudentAndGradeServiceTest {
	
	@Autowired
	private StudentAndGradeService studentService;
	
	@Autowired
	private StudentDao studentDao;
	
	@Autowired
	private MathGradesDao mathGradeDao;
	
	@Autowired
	private ScienceGradesDao scienceGradeDao;
	
	@Autowired
	private HistoryGradesDao historyGradeDao;
	
	@Autowired
	private JdbcTemplate jdbc;
	
	@Value("${sql.script.create.student}")
	private String sqlAddStudent;
	
	@Value("${sql.script.create.math.grade}")
	private String sqlAddMathGrade;
	
	@Value("${sql.script.create.science.grade}")
	private String sqlAddScienceGrade;
	
	@Value("${sql.script.create.history.grade}")
	private String sqlAddHistoryGrade;
	
	@Value("${sql.script.delete.student}")
	private String sqlDeleteStudent;
	
	@Value("${sql.script.delete.math.grade}")
	private String sqlDeleteMathGrade;
	
	@Value("${sql.script.delete.science.grade}")
	private String sqlDeleteScienceGrade;
	
	@Value("${sql.script.delete.history.grade}")
	private String sqlDeleteHistoryGrade;
	

	//we create sample data
	@BeforeEach
	public void setUpDatabase() {
		//we can use Dao too
		jdbc.execute(sqlAddStudent);//sql- id is auto, id=1
		
		jdbc.execute(sqlAddMathGrade);
		
		jdbc.execute(sqlAddScienceGrade);
		
		jdbc.execute(sqlAddHistoryGrade);
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
		Optional<MathGrade> deletedMathGrade = mathGradeDao.findById(1);
		Optional<HistoryGrade> deletedHistoryGrade = historyGradeDao.findById(1);
		Optional<ScienceGrade> deletedScienceGrade = scienceGradeDao.findById(1);
		
		//we ask if we got or not a record
		assertTrue(deletedCollegeStudent.isPresent(),"Return true");
		assertTrue(deletedMathGrade.isPresent(),"Return true");
		assertTrue(deletedHistoryGrade.isPresent(),"Return true");
		assertTrue(deletedScienceGrade.isPresent(),"Return true");
		
		studentService.deleteStudent(1);//now, we delete all, student and grades
		
		//we try to find the same records again
		deletedCollegeStudent = studentDao.findById(1);
		deletedMathGrade = mathGradeDao.findById(1);
		deletedHistoryGrade = historyGradeDao.findById(1);
		deletedScienceGrade = scienceGradeDao.findById(1);
		
		
		assertFalse(deletedCollegeStudent.isPresent(),"Return false");
		assertFalse(deletedMathGrade.isPresent(),"Return false");
		assertFalse(deletedHistoryGrade.isPresent(),"Return false");
		assertFalse(deletedScienceGrade.isPresent(),"Return false");
		
		
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
	
	
	@Test
	void createGradeService() {
		
		assertTrue(studentService.createGrade(80.50,1,"math"));
		assertTrue(studentService.createGrade(80.50,1,"science"));
		assertTrue(studentService.createGrade(80.50,1,"history"));
		
		Iterable<MathGrade> mathGrades = mathGradeDao.findGradeByStudentId(1);
		Iterable<ScienceGrade> scienceGrades = scienceGradeDao.findGradeByStudentId(1);
		Iterable<HistoryGrade> historyGrades = historyGradeDao.findGradeByStudentId(1);
		
		//we cast iterables list to Collection, to see the size
		assertTrue(((Collection<MathGrade>) mathGrades).size() == 2);
		assertTrue(((Collection<ScienceGrade>) scienceGrades).size() == 2);
		assertTrue(((Collection<HistoryGrade>) historyGrades).size() == 2);
		
	}
	
	@Test
	void createGradeServiceReturnFalse() {
		assertFalse(studentService.createGrade(105, 1, "math")); // grade must to be between 0-100
		assertFalse(studentService.createGrade(-5, 1, "math"));
		assertFalse(studentService.createGrade(80.50, 2, "math")); //we don't have student id=2
		assertFalse(studentService.createGrade(80.50, 1, "Literature")); // literature don't exist
	}
	
	@Test
	void deleteGradeService() {
		//deleteGrade returns student id
		assertEquals(1, studentService.deleteGrade(1,"math"), "Returns student id after delete");
		assertEquals(1, studentService.deleteGrade(1,"science"), "Returns student id after delete");
		assertEquals(1, studentService.deleteGrade(1,"history"), "Returns student id after delete");
	}
	
	void deleteGradeServiceReturnStudentIdOfZero() {
		//first 0 is for student id  , deleteGrade returns student id
		assertEquals(0,studentService.deleteGrade(0, "science"),"No student should have 0 id");
		assertEquals(0,studentService.deleteGrade(1, "literature"),"No student should have a literature class");
	}
	
	@Test
	void studentInformation() {
		GradebookCollegeStudent gradebookCollegeStudent = studentService.studentInformation(1);
		
		assertNotNull(gradebookCollegeStudent);
		assertEquals(1,gradebookCollegeStudent.getId());
		assertEquals("Eric",gradebookCollegeStudent.getFirstname());
		assertEquals("Roby",gradebookCollegeStudent.getLastname());
		assertEquals("eric.roby@luv2code_school.com",gradebookCollegeStudent.getEmailAddress());
		
		assertTrue(gradebookCollegeStudent.getStudentGrades().getMathGradeResults().size() == 1);
		assertTrue(gradebookCollegeStudent.getStudentGrades().getScienceGradeResults().size() == 1);
		assertTrue(gradebookCollegeStudent.getStudentGrades().getHistoryGradeResults().size() == 1);
	}
	
	@Test
	void studentInformationServiceReturnNull() {
		GradebookCollegeStudent gradebookCollegeStudent = studentService.studentInformation(0);// there are not a student with id=0
		
		assertNull(gradebookCollegeStudent);
	}
	
	//we delete sample data
	@AfterEach
	void setUpAfterTransaction() {
		jdbc.execute(sqlDeleteStudent);
		jdbc.execute(sqlDeleteMathGrade);
		jdbc.execute(sqlDeleteScienceGrade);
		jdbc.execute(sqlDeleteHistoryGrade);
	}
	
}
