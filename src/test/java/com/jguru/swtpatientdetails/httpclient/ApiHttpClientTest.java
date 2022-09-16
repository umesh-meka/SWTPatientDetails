package com.jguru.swtpatientdetails.httpclient;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jguru.swtpatientdetails.model.Address;
import com.jguru.swtpatientdetails.model.Patient;


@ExtendWith(MockitoExtension.class)
class ApiHttpClientTest {


	@InjectMocks
	private ApiHttpClient apiHttpClient;

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private HttpRequest request;
	
	@Mock
	private HttpResponse<String> response;
	
	@Mock
	private HttpRequest.Builder builder;
	 
	@Mock
	private BodyHandler<String> str;
	
	@Mock
	HttpClient httpClient = HttpClient.newHttpClient();
	 
	@Mock
	HttpRequest httpRequest;
	
	
	
	private List<Patient> getpatients() {
		List<Patient> patients = new ArrayList<Patient>();

		Patient patient = new Patient();
		patient.setID(100L);
		patient.setFirstName("UmeshMeka");
		patient.setLastName("Meka");
		patient.setGender("MALE");
		patient.setDob("1994-09-11");
		patient.setTelNumber(1234567890L);

		List<Address> addressList = new ArrayList<>();
		
		Address currentAddress = new Address();		
		currentAddress.setId(101L);
		currentAddress.setAddressType("CURRENT");
		currentAddress.setStreetName("Attapur");
		currentAddress.setCityName("Hyderabad");
		currentAddress.setStateName("Talangana");
		currentAddress.setPinCode(500048L);
		
		Address permanentAddress = new Address();		
		permanentAddress.setId(101L);
		permanentAddress.setAddressType("PERMANENT");
		permanentAddress.setStreetName("Gorentla");
		permanentAddress.setCityName("Suryapet");
		permanentAddress.setStateName("Talangana");
		permanentAddress.setPinCode(508280L);
		
		addressList.add(currentAddress);
		addressList.add(permanentAddress);
		
		patient.setAddress(addressList);
		
		patients.add(patient);

		return patients;
	}

	@Test
	public void fetchAllPatientsTest() throws IOException, InterruptedException {
		int expectedSize = apiHttpClient.fetchAllPatients().size();
		
		apiHttpClient.savePatient(getpatients().get(0));
		
		List<Patient> patientsList = apiHttpClient.fetchAllPatients();
		assertEquals(expectedSize+1, patientsList.size());
		
		apiHttpClient.deletePatient(getpatients().get(0).toString());
	}
	
	@Test
	public void findPatientByIdTest() throws IOException, InterruptedException{ 
		HttpResponse<String> response = apiHttpClient.savePatient(getpatients().get(0));
		ObjectMapper mapper = new ObjectMapper();
		Patient patientResult = mapper.readValue(response.body(), new TypeReference<Patient>() {});
				 
		Patient patient = apiHttpClient.getPatient(patientResult.getID().toString());
		
		assertEquals("UmeshMeka", patient.getFirstName());
		apiHttpClient.deletePatient(getpatients().get(0).toString());
	}
	
	
	@Test
	public void fetchByNameTest() throws IOException, InterruptedException{
		int expectedSize = apiHttpClient.fetchByName("UmeshMeka").size();
		apiHttpClient.savePatient(getpatients().get(0));
		
		List<Patient> patientsList = apiHttpClient.fetchByName("UmeshMeka");
		assertEquals(expectedSize+1, patientsList.size());
		
		apiHttpClient.deletePatient(getpatients().get(0).toString());
	}
	
	@Test
	public void deletePatientTest() throws IOException, InterruptedException {
		HttpResponse<String> response = apiHttpClient.savePatient(getpatients().get(0));
		ObjectMapper mapper = new ObjectMapper();
		Patient patientResult = mapper.readValue(response.body(), new TypeReference<Patient>() {});
		
		HttpResponse<String> response2 = apiHttpClient.deletePatient(patientResult.getID().toString());

        assertEquals(200, response2.statusCode());
	}
	
	@Test
	public void updatePatientTest() throws IOException, InterruptedException {
		Patient patient = getpatients().get(0);
		patient.setFirstName("Ramesh");
		apiHttpClient.savePatient(patient);
		
		apiHttpClient.updatePatient(getpatients().get(0));
		
		assertEquals("Ramesh", patient.getFirstName());
		apiHttpClient.deletePatient(getpatients().get(0).toString());
	}
	
	@Test
	public void savePatientTest() throws IOException, InterruptedException {
		Patient patient = getpatients().get(0);
		patient.setID(null);
		patient.getAddress().get(0).setId(null);
		patient.getAddress().get(1).setId(null);
		HttpResponse<String> response = apiHttpClient.savePatient(patient);
		
		 ObjectMapper mapper = new ObjectMapper();
		 Patient patientResult = mapper.readValue(response.body(), new TypeReference<Patient>() {});
		 
		 assertEquals("UmeshMeka", patient.getFirstName());
		 apiHttpClient.deletePatient(getpatients().get(0).toString());
	}
}
