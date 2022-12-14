package com.luv2code.springmvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.GradebookCollegeStudent;
import com.luv2code.springmvc.models.MathGrade;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
class GradebookControllerTest {

	private static MockHttpServletRequest request;

	@Autowired
	private JdbcTemplate jdbc;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private StudentDao studentDao;

	@Mock//with the mock we can establish the return
	private StudentAndGradeService studentCreateServiceMock;
	
	@Autowired
	private StudentAndGradeService studentService;
	
	@Autowired
	private MathGradesDao mathGradesDao;
	

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

	@BeforeAll
	public static void setup() {
		request = new MockHttpServletRequest();
		request.setParameter("firstname", "Chad");
		request.setParameter("lastname", "Darby");
		request.setParameter("emailAddress", "chad.darby@luv2code_school.com");
	}

	@BeforeEach
	void beforeEach() {
		// we can use Dao too
		jdbc.execute(sqlAddStudent);

		jdbc.execute(sqlAddMathGrade);

		jdbc.execute(sqlAddScienceGrade);

		jdbc.execute(sqlAddHistoryGrade);
	}

	@Test
	void getStudentsHttpRequest() throws Exception {

		CollegeStudent studentOne = new GradebookCollegeStudent("Eric", "Roby", "eric_roby@luv2code_school.com");
		CollegeStudent studentTwo = new GradebookCollegeStudent("Chad", "Darby", "chad_darby@luv2code_school.com");

		List<CollegeStudent> collegeStudentList = new ArrayList<>(Arrays.asList(studentOne, studentTwo));

		when(studentCreateServiceMock.getGradebook()).thenReturn(collegeStudentList);

		assertIterableEquals(collegeStudentList, studentCreateServiceMock.getGradebook());

		// web testing
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/")) // get request -> GradebookCOntroller
				.andExpect(status().isOk()).andReturn();

		ModelAndView mav = mvcResult.getModelAndView(); // we get the return , for example, the page that return the
														// handler in the controller

		ModelAndViewAssert.assertViewName(mav, "index"); // mav and index must to be equals

	}

	@Test
	void createStudentHttpRequest() throws Exception {

		CollegeStudent studentOne = new GradebookCollegeStudent("Eric", "Roby", "eric_roby@luv2code_school.com");

		List<CollegeStudent> collegeStudentList = new ArrayList<>(Arrays.asList(studentOne));

		when(studentCreateServiceMock.getGradebook()).thenReturn(collegeStudentList);

		assertIterableEquals(collegeStudentList, studentCreateServiceMock.getGradebook());

		MvcResult mvcResult = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/").contentType(MediaType.APPLICATION_JSON)
						.param("firstname", request.getParameterValues("firstname")) // we pass the values for the
																						// handler, in this case for
																						// CollegeStudent
						.param("lastname", request.getParameterValues("lastname"))
						.param("emailAddress", request.getParameterValues("emailAddress")))
				.andExpect(status().isOk()).andReturn();

		ModelAndView mav = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(mav, "index");

		CollegeStudent verifyStudent = studentDao.findByEmailAddress("chad.darby@luv2code_school.com");

		assertNotNull(verifyStudent, "Student should be found");

	}

	@Test
	public void deleteStudentHttpRequest() throws Exception {
		assertTrue(studentDao.findById(1).isPresent());

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/delete/student/{id}", 1))
				.andExpect(status().isOk()).andReturn();

		ModelAndView mav = mvcResult.getModelAndView();

		ModelAndViewAssert.assertViewName(mav, "index");

		assertFalse(studentDao.findById(1).isPresent());// we check if was deleted
	}

	@Test
	void deleteStudentHttpRequestErrorPage() throws Exception {
		// We don't have a student with id = 0
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/delete/student/{id}", 0))
				.andExpect(status().isOk()).andReturn();

		ModelAndView mav = mvcResult.getModelAndView();

		ModelAndViewAssert.assertViewName(mav, "error");

	}

	@Test//for the case where there is the student
	void studentInformationHttpRequest() throws Exception{
		
		assertTrue(studentDao.findById(1).isPresent());
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}",1))
				.andExpect(status().isOk()).andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		ModelAndViewAssert.assertViewName(mav, "studentInformation");
		
	}
	
	@Test //for the case where not exist the student
	void studentInformationHttpStudentDoesNotExistRequest() throws Exception{
		assertFalse(studentDao.findById(0).isPresent()); //student id=0 not exist
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/studentInformation/{id}",0))
				.andExpect(status().isOk()).andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		ModelAndViewAssert.assertViewName(mav, "error");
	}
	
	@Test//create grade when student exist
	void createValidGradeHttpRequest() throws Exception {
		assertTrue(studentDao.findById(1).isPresent());
		
		GradebookCollegeStudent student = studentService.studentInformation(1);
		
		assertEquals(1, student.getStudentGrades().getMathGradeResults().size());
		
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/grades")
				.contentType(MediaType.APPLICATION_JSON)
				.param("grade","85.00")
				.param("gradeType","math")
				.param("studentId","1")).andExpect(status().isOk()).andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		ModelAndViewAssert.assertViewName(mav, "studentInformation");
		
		student = studentService.studentInformation(1);//I look for the student again to see how many math grades he has
		
		assertEquals(2,student.getStudentGrades().getMathGradeResults().size());//1 was added in the properties y another now, so the student must have 2 math grades
	}
	
	
	@Test//create grade when student doesn't exist
	void createAValidGradeHttpRequestStudentDoesNotExistEmptyResponse() throws Exception  {
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/grades")
				.contentType(MediaType.APPLICATION_JSON)
				.param("grade","85.00")
				.param("gradeType","history")
				.param("studentId","0")) //this student doesn't exist
				.andExpect(status().isOk()).andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		ModelAndViewAssert.assertViewName(mav, "error");
	}
	
	@Test
	void createANonValidGradeHttpRequestGradeTypeDoesNotExistEmptyResponse() throws Exception{
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/grades")
				.contentType(MediaType.APPLICATION_JSON)
				.param("grade","85.00")
				.param("gradeType","literature")//this subject is invalid
				.param("studentId","1")) 
				.andExpect(status().isOk()).andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		ModelAndViewAssert.assertViewName(mav, "error");
	}
	
	@Test
	void deleteAValidGradeHttpRequest() throws Exception{
		Optional<MathGrade> mathGrade = mathGradesDao.findById(1);
		
		assertTrue(mathGrade.isPresent());
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/grades/{id}/{gradeType}",1,"math"))//path var
				.andExpect(status().isOk()).andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		ModelAndViewAssert.assertViewName(mav, "studentInformation");
		
		mathGrade = mathGradesDao.findById(1); 
		
		assertFalse(mathGrade.isPresent());//confirm grade was really deleted
	}
	
	@Test
	void deleteAValidGradeHttpRequestStudentIdDoesNotExistEmptyResponse()throws Exception{
		Optional<MathGrade> mathGrade = mathGradesDao.findById(2);
		
		assertFalse(mathGrade.isPresent());//we check if this grade is really inexistent,to continue the test
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/grades/{id}/{gradeType}",2,"math"))
				.andExpect(status().isOk()).andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		ModelAndViewAssert.assertViewName(mav, "error");
		
	}
	
	@Test
	void deleteANonValidGradeHttpRequest()throws Exception{
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/grades/{id}/{gradeType}",1,"literature")) //literature is invalid
				.andExpect(status().isOk()).andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		ModelAndViewAssert.assertViewName(mav, "error");
	}
	
	// we delete sample data
	@AfterEach
	void setUpAfterTransaction() {
		jdbc.execute(sqlDeleteStudent);
		jdbc.execute(sqlDeleteMathGrade);
		jdbc.execute(sqlDeleteScienceGrade);
		jdbc.execute(sqlDeleteHistoryGrade);
	}

}
