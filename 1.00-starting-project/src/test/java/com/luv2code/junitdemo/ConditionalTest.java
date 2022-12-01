package com.luv2code.junitdemo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;

class ConditionalTest {
	
	@Test
	@Disabled("Don't run unitil JIRA #123 is resolved")  //now this method won't be executed
	void basicTest() {
		
	}
	
	@Test
	@EnabledOnOs(OS.WINDOWS)  //If your system is Windows this test will be executed, if not, then won't be executed
	void testForWindowsOnly() {
		
	}
	
	@Test
	@EnabledOnOs(OS.MAC)  //If your system is Mac this test will be executed, if not, then won't be executed
	void testForMacOnly() {
		
	}
	
	@Test
	@EnabledOnOs({OS.MAC, OS.WINDOWS})  //If your system is Mac or Windows this test will be executed, if not, then won't be executed
	void testForMacAndWindowsOnly() {
		
	}
	
	@Test
	@EnabledOnOs(OS.LINUX)  //If your system is Linux this test will be executed, if not, then won't be executed
	void testForLinuxOnly() {
		
	}
	
	@Test
	@EnabledOnJre(JRE.JAVA_17) //If you have java17 in your project then this test will be executed, only with java17
	void testOnlyForJava17() {
		
	}
	
	@Test
	@EnabledOnJre(JRE.JAVA_15) //If you have java15 in your project then this test will be executed, only with java17
	void testOnlyForJava15() {
		
	}
	
	@Test
	@EnabledOnJre(JRE.JAVA_8) //If you have java8 in your project then this test will be executed, only with java17 
	void testOnlyForJava8() {
		
	}
	
	@Test
	@EnabledForJreRange(min=JRE.JAVA_8 , max= JRE.JAVA_10) //Only if you have java between 8 and 10 in your project, this test works
	void testOnlyForJavaRange() {
		
	}
	
	@Test
	@EnabledForJreRange(min=JRE.JAVA_11) //Only if you have java 11 and higher in your project, this test works
	void testOnlyForJavaRangeMin() {
		
	}
	
	
	@Test
	@EnabledIfEnvironmentVariable(named="ENV" ,matches = "DEV")
	void testOnlyForDevEnvironment() {
		
	}
	
	@Test
	@EnabledIfSystemProperty(named="SYS_PROP" ,matches = "CI_CD_DEPLOY")
	void testOnlyForSystemProperty() {
		
	}
	
	
	
}
