package com.luv2code.CustomDisplayNameGenerator;

import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayNameGenerator.Standard;

public class RemplaceCamelCase extends Standard{

	public String generateDisplayNameForClass(Class<?> testClass) {
		return this.replaceCapitals(super.generateDisplayNameForClass(testClass));
	}

	public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
		return this.replaceCapitals(super.generateDisplayNameForNestedClass(nestedClass));
	}

	public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
		return this.replaceCapitals(testMethod.getName());
	}

	private String replaceCapitals(String name) {
		name = name.replaceAll("([A-Z])", " $1");
		name = name.replaceAll("([0-9]+)", " $1");
		return name;
	}
}
