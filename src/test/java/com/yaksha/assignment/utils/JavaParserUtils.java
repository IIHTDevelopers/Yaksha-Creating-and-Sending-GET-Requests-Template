package com.yaksha.assignment.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

public class JavaParserUtils {

	/**
	 * Loads the content of a class file from the given file path, parses it using
	 * JavaParser, and checks the class and method structure.
	 *
	 * @param filePath           Full path to the class file (e.g.,
	 *                           "src/main/java/com/yaksha/assignment/controller/AppController.java").
	 * @param classAnnotations   Annotations to check on the class
	 *                           (e.g., @RestController).
	 * @param methodName         The name of the method to inspect.
	 * @param methodAnnotations  Annotations to check on the method
	 *                           (e.g., @GetMapping).
	 * @param paramName          The name of the parameter to check for annotation
	 *                           (e.g., @RequestParam).
	 * @param expectedReturnType The expected return type of the method (e.g.,
	 *                           "String").
	 * @return boolean Returns true if all checks pass, false otherwise.
	 * @throws IOException
	 */
	public static boolean checkControllerStructure(String filePath, String classAnnotations, String methodName,
			String methodAnnotations, String paramName, String expectedReturnType) {
		// Load class file as String from file path
		String classContent;
		try {
			classContent = loadClassContent(filePath);
		} catch (IOException e) {
			System.out.println("Error: Unable to read the class file from path: " + filePath);
			return false;
		}

		// Create a JavaParser instance and parse the class content
		JavaParser javaParser = new JavaParser();
		Optional<CompilationUnit> optionalCompilationUnit = javaParser.parse(classContent).getResult();

		// If parsing fails, return false
		if (optionalCompilationUnit.isEmpty()) {
			System.out.println("Error: Failed to parse the class content.");
			return false;
		}

		CompilationUnit compilationUnit = optionalCompilationUnit.get();

		// Check if the class has the required annotations
		boolean hasClassAnnotation = compilationUnit.getClassByName("AppController").get().getAnnotations().stream()
				.anyMatch(annotation -> annotation.getNameAsString().equals(classAnnotations));

		if (!hasClassAnnotation) {
			System.out.println("Error: The class is missing the @" + classAnnotations + " annotation. Please add it.");
			return false;
		}

		// Check if the method has the required annotations
		MethodDeclaration method = compilationUnit.getClassByName("AppController").get().getMethodsByName(methodName)
				.get(0);

		boolean hasMethodAnnotation = method.getAnnotationByName(methodAnnotations).isPresent();
		if (!hasMethodAnnotation) {
			System.out.println("Error: The method " + methodName + " is missing the @" + methodAnnotations
					+ " annotation. Please add it.");
			return false;
		}

		// Check if the method's parameter has the required annotation
		boolean hasParamAnnotation = method.getParameterByName(paramName).get().getAnnotationByName("RequestParam")
				.isPresent();
		if (!hasParamAnnotation) {
			System.out.println(
					"Error: The parameter " + paramName + " is missing the @RequestParam annotation. Please add it.");
			return false;
		}

		// Check if the return type matches the expected type
		boolean isReturnTypeCorrect = method.getType().asString().equals(expectedReturnType);
		if (!isReturnTypeCorrect) {
			System.out.println("Error: The return type of the method " + methodName + " is not " + expectedReturnType
					+ ". Please correct it.");
			return false;
		}

		// If all checks pass
		return true;
	}

	/**
	 * Load the content of a class from the file path and return it as a String.
	 *
	 * @param filePath Full path to the class file (e.g.,
	 *                 "src/main/java/com/yaksha/assignment/controller/AppController.java").
	 * @return The class content as a String.
	 * @throws IOException If an error occurs while reading the file.
	 */
	private static String loadClassContent(String filePath) throws IOException {
		// Create a File object from the provided file path
		File participantFile = new File(filePath);
		if (!participantFile.exists()) {
			throw new IOException("Class file not found: " + filePath);
		}

		// Read the content of the file
		try (FileInputStream fileInputStream = new FileInputStream(participantFile)) {
			byte[] bytes = fileInputStream.readAllBytes();
			return new String(bytes, StandardCharsets.UTF_8);
		}
	}
}
