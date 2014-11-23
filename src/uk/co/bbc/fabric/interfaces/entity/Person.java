package uk.co.bbc.fabric.interfaces.entity;

import java.io.Serializable;

import javax.persistence.*;


/**
 * The persistent class for the PERSONS database table.
 * 
 */
@Entity
@Table(name="persons")
//@NamedQuery(name="Person.findAll", query="SELECT p FROM Person p")
public class Person implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="address")
	private String address;

	@Column(name="city")
	private String city;

	@Column(name="firstname")
	private String firstname;
	
	@Column(name="lastname")
	private String lastname;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="personid")
	private int personid;

	public Person() {
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getFirstname() {
		return this.firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return this.lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public int getPersonid() {
		return this.personid;
	}

	public void setPersonid(int personid) {
		this.personid = personid;
	}

}