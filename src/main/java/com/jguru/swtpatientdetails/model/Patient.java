package com.jguru.swtpatientdetails.model;

import java.util.List;

/*
 * This model will be used to store patient details
 * It will provide support for storing more than one address using OneToMany relation with Address model
 *
 * @author      Umesh M
 * @version     1.0
 * @since       1.0
 */

public class Patient {
	private Long ID;
    private String firstName;
    private String lastName;
    private String dob;
    private String gender;
    private List<Address> address;
    private Long telNumber;
    
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public List<Address> getAddress() {
		return address;
	}
	public void setAddress(List<Address> address) {
		this.address = address;
	}
	public Long getTelNumber() {
		return telNumber;
	}
	public void setTelNumber(Long telNumber) {
		this.telNumber = telNumber;
	}
    
    
}
