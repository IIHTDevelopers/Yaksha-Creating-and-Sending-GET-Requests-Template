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

		boolean statusOk = false;
		boolean containsUserId = false;
		boolean containsId = false;
		boolean containsTitle = false;

		try {
			MvcResult result = mockMvc.perform(get("/fetchData").param("apiUrl", apiUrl))
					.andReturn(); // Capture the result

			int responseStatus = result.getResponse().getStatus();

			// Check if the HTTP status is OK (200)
			if (responseStatus == 200) {
				statusOk = true;
				String responseBody = result.getResponse().getContentAsString();
				containsUserId = responseBody.contains("userId");
				containsId = responseBody.contains("id");
				containsTitle = responseBody.contains("title");
			} else {
				System.out.println("API request failed with status: " + responseStatus);
			}

		} catch (Exception ex) {
			System.out.println("Error occurred: " + ex.getMessage());
			yakshaAssert(currentTest(), "false", businessTestFile); // Ensure test fails if exception occurs
			return; // Exit early to prevent further checks
		}

		boolean finalResult = statusOk && containsUserId && containsId && containsTitle;

		// Assert test case result while handling failure scenario
		if (!finalResult) {
			System.out.println("Test case failed due to API response issues.");
		}

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
