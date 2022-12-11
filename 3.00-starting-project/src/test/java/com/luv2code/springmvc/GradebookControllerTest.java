package com.luv2code.springmvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;

@TestPropertySource("/application.properties")
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

	@Mock
	private StudentAndGradeService studentCreateServiceMock;
	
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
		jdbc.execute("insert into student(firstname, lastname, email_address)"
				+ "values('Eric', 'Roby', 'eric.roby@luv2code_school.com')");// sql- id is auto, id=1
	}
	
	@Test
	void getStudentsHttpRequest() throws Exception{
		
		CollegeStudent studentOne = new GradebookCollegeStudent("Eric", "Roby", "eric_roby@luv2code_school.com");
		CollegeStudent studentTwo = new GradebookCollegeStudent("Chad", "Darby", "chad_darby@luv2code_school.com");
		
		List<CollegeStudent> collegeStudentList = new ArrayList<>(Arrays.asList(studentOne,studentTwo));
		
		when(studentCreateServiceMock.getGradebook()).thenReturn(collegeStudentList);
		
		assertIterableEquals(collegeStudentList, studentCreateServiceMock.getGradebook());
		
		//web testing
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/")) //get request -> GradebookCOntroller
				.andExpect(status().isOk()).andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView(); // we get the return , for example, the page that return the handler in the controller
		
		ModelAndViewAssert.assertViewName(mav, "index"); //mav and index must to be equals
		
	}
	
	@Test
	void createStudentHttpRequest() throws Exception{
		
		CollegeStudent studentOne = new GradebookCollegeStudent("Eric", "Roby", "eric_roby@luv2code_school.com"); 
		
		List<CollegeStudent> collegeStudentList = new ArrayList<>(Arrays.asList(studentOne));
		
		when(studentCreateServiceMock.getGradebook()).thenReturn(collegeStudentList);
		
		assertIterableEquals(collegeStudentList, studentCreateServiceMock.getGradebook());
		
		MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/")
				.contentType(MediaType.APPLICATION_JSON)
				.param("firstname", request.getParameterValues("firstname")) //we pass the values for the handler, in this case for CollegeStudent
				.param("lastname", request.getParameterValues("lastname"))
				.param("emailAddress", request.getParameterValues("emailAddress")))
				.andExpect(status().isOk()).andReturn();
				
		ModelAndView mav = mvcResult.getModelAndView();
		ModelAndViewAssert.assertViewName(mav, "index");
		
		CollegeStudent verifyStudent = studentDao.findByEmailAddress("chad.darby@luv2code_school.com");
		
		assertNotNull(verifyStudent,"Student should be found");
		
	}
	
	@Test
	public void deleteStudentHttpRequest() throws Exception{
		assertTrue(studentDao.findById(1).isPresent());
		
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/delete/student/{id}",1))
				.andExpect(status().isOk()).andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		ModelAndViewAssert.assertViewName(mav, "index");
		
		assertFalse(studentDao.findById(1).isPresent());//we check if was deleted
	}
	
	@Test
	void deleteStudentHttpRequestErrorPage() throws Exception{
		//We don't have a student with id = 0
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/delete/student/{id}",0))
				.andExpect(status().isOk()).andReturn();
		
		ModelAndView mav = mvcResult.getModelAndView();
		
		ModelAndViewAssert.assertViewName(mav, "error");
		
	}
	
	
	//we delete sample data
	@AfterEach
	void setUpAfterTransaction() {
		jdbc.execute("DELETE FROM student");
	}
	

}
