package org.meri.simpleshirosecuredapplication.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user_personal_data")
public class UserPersonalData {
	
	private String userName;
	private String firstname;
	private String lastname;
	private String about;
	
	
	@Column(name = "user_name")
	@Id
	public String getUserName() {
  	return userName;
  }

	public void setUserName(String userName) {
  	this.userName = userName;
  }

	@Column
	public String getFirstname() {
  	return firstname;
  }
	
	public void setFirstname(String firstname) {
  	this.firstname = firstname;
  }
	
	@Column
	public String getLastname() {
  	return lastname;
  }
	
	public void setLastname(String lastname) {
  	this.lastname = lastname;
  }
	
	@Column
	public String getAbout() {
  	return about;
  }

	public void setAbout(String about) {
  	this.about = about;
  }
	
	
}
