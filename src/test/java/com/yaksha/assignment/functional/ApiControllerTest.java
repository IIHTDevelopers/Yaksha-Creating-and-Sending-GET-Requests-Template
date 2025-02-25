package com.yaksha.assignment.functional;

import static com.yaksha.assignment.utils.TestUtils.businessTestFile;
import static com.yaksha.assignment.utils.TestUtils.currentTest;
import static com.yaksha.assignment.utils.TestUtils.testReport;
import static com.yaksha.assignment.utils.TestUtils.yakshaAssert;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import com.yaksha.assignment.controller.AppController;
import com.yaksha.assignment.utils.JavaParserUtils;

@WebMvcTest(AppController.class)
public class ApiControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private RestTemplate restTemplate;

	@AfterAll
	public static void afterAll() {
		testReport();
	}

	@Test
	public void testFetchDataFromApi() throws Exception {
		String apiUrl = "https://jsonplaceholder.typicode.com/posts/1";

		// Declare boolean flags to track each assertion result
		boolean statusOk = false;
		boolean containsUserId = false;
		boolean containsId = false;
		boolean containsTitle = false;

		try {
			// Perform the GET request to fetch data from the API
			MvcResult result = mockMvc.perform(get("/fetchData").param("apiUrl", apiUrl)).andExpect(status().isOk()) // Check
																														// if
																														// the
																														// status
																														// is
																														// OK
																														// (200)
					.andReturn(); // Capture the result

			// Check if the HTTP status is OK (200)
			statusOk = result.getResponse().getStatus() == 200;

			// Check if the response contains "userId"
			containsUserId = result.getResponse().getContentAsString().contains("userId");

			// Check if the response contains "id"
			containsId = result.getResponse().getContentAsString().contains("id");

			// Check if the response contains "title"
			containsTitle = result.getResponse().getContentAsString().contains("title");

		} catch (Exception ex) {
			// If any exception occurs, we log it and ensure `yakshaAssert` is called with
			// "false"
			System.out.println("Error occurred: " + ex.getMessage());
		}

		// Combine all the results and pass them to yakshaAssert
		boolean finalResult = statusOk && containsUserId && containsId && containsTitle;

		// Use yakshaAssert to check if all assertions passed
		yakshaAssert(currentTest(), finalResult ? "true" : "false", businessTestFile);
	}

	@Test
	public void testControllerStructure() throws Exception {
		String filePath = "src/main/java/com/yaksha/assignment/controller/AppController.java"; // Update path to your
																								// file
		boolean result = JavaParserUtils.checkControllerStructure(filePath, // Pass the class file path
				"RestController", // Check if @RestController is used on the class
				"fetchDataFromApi", // Check if the method name is correct
				"GetMapping", // Check if @GetMapping is present on the method
				"apiUrl", // Check if the parameter has @RequestParam annotation
				"String" // Ensure the return type is String
		);
		// checkControllerStructure
		yakshaAssert(currentTest(), result, businessTestFile);
	}
}
