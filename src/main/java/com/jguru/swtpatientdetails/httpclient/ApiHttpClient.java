/**
 * 
 */
package com.jguru.swtpatientdetails.httpclient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jguru.swtpatientdetails.model.Patient;

/**
 * This class will handle the below CURD operations:
 * <ul>
 * <li> Patient data saving
 * <li> Patient data updating
 * <li> Patient data deleting by ID
 * <li> Finding Patient by ID
 * <li> Finding Patient by patient name
 * <li> Finding all Patients list in database
 * </ul>
 *
 * @author      Umesh M
 * @version     1.0
 * @since       1.0
 */

public class ApiHttpClient {

	public static List<Patient> fetchAllPatients() throws IOException, InterruptedException {		
		String findAllEndpoint = "http://localhost:8080/patient/findAll";

		var request = HttpRequest.newBuilder()
				.uri(URI.create(findAllEndpoint))
				.header("Content-Type", "application/json")
				.GET()
				.build();

		var client = HttpClient.newHttpClient();

		var response = client.send(request, HttpResponse.BodyHandlers.ofString());

		System.out.println(response.statusCode());
		System.out.println(response.body());

		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(response.body(), new TypeReference<List<Patient>>() {});
	}

	public static Patient getPatient(String patientId) throws IOException, InterruptedException {
		String findByIdEndpoint = "http://localhost:8080/patient/findById/"+patientId;

		var request = HttpRequest.newBuilder()
				.uri(URI.create(findByIdEndpoint))
				.header("Content-Type", "application/json")
				.GET()
				.build();

		var client = HttpClient.newHttpClient();

		var response = client.send(request, HttpResponse.BodyHandlers.ofString());

		System.out.println(response.statusCode());
		System.out.println(response.body());

		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(response.body(), new TypeReference<Patient>() {});
	}
	
	public static List<Patient> fetchByName(String patientName) throws IOException, InterruptedException {		
		String findByNameEndpoint = "http://localhost:8080/patient/findByName/"+patientName;

		var request = HttpRequest.newBuilder()
				.uri(URI.create(findByNameEndpoint))
				.header("Content-Type", "application/json")
				.GET()
				.build();

		var client = HttpClient.newHttpClient();

		var response = client.send(request, HttpResponse.BodyHandlers.ofString());

		System.out.println(response.statusCode());
		System.out.println(response.body());

		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(response.body(), new TypeReference<List<Patient>>() {});
	}

	public static HttpResponse<String> deletePatient(String patientId) throws IOException, InterruptedException {
		String deleteEndpoint = "http://localhost:8080/patient/delete/"+patientId;

		var request = HttpRequest.newBuilder()
				.uri(URI.create(deleteEndpoint))
				.header("Content-Type", "application/json")
				.DELETE()
				.build();

		var client = HttpClient.newHttpClient();

		var response = client.send(request, HttpResponse.BodyHandlers.ofString());

		System.out.println(response.statusCode());
		System.out.println(response.body());
		
		return response;
	}

	public static HttpResponse<String> savePatient(Patient patient) throws IOException, InterruptedException {		
		var objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(patient); 
        
        String saveEndpoint = "http://localhost:8080/patient/save";
        
        var request = HttpRequest.newBuilder()
                .uri(URI.create(saveEndpoint))
				.header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        var client = HttpClient.newHttpClient();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        System.out.println(response.statusCode());
		System.out.println(response.body());

		return response;
		
//        ObjectMapper mapper = new ObjectMapper();
//		return mapper.readValue(response.body(), new TypeReference<Patient>() {});
	}
	
	public static HttpResponse<String> updatePatient(Patient patient) throws IOException, InterruptedException {		
		var objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(patient);
        
        String updateEndpoint = "http://localhost:8080/patient/update/"+patient.getID();
        
        System.out.println("kajskajsahdjahkjshakjshk: "+ requestBody);
        var request = HttpRequest.newBuilder()
                .uri(URI.create(updateEndpoint))
				.header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        var client = HttpClient.newHttpClient();

        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        System.out.println(response.statusCode());
		System.out.println(response.body());
		
		return response;

//        ObjectMapper mapper = new ObjectMapper();
//		return mapper.readValue(response.body(), new TypeReference<Patient>() {});
	}


}
