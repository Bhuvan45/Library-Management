package com.library.model.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "membership_maintenance")
public class MembershipMaintenance 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MEMA_Member_Id")
	private Integer memberId;

	@Column(name = "MEMA_First_Name")
	private String firstName;

	@Column(name = "MEMA_Last_Name")
	private String lastName;
	
	@Transient
	public String getFullName() {
		return firstName + " " + lastName;
	}

	@Column(name = "MEMA_Age")
	private Integer age;

	@Column(name = "MEMA_Gender")
	private String gender;

	@Column(name = "MEMA_Address_Line1")
	private String addressLine1;

	@Column(name = "MEMA_Address_Line2")
	private String addressLine2;

	@Column(name = "MEMA_Mobile_Number")
	private String mobileNumber;

	@Column(name = "MEMA_Email")
	private String email;

	@Column(name = "MEMA_Work_Status")
	private String workStatus;

	@Column(name = "MEMA_Membership_Start_Date")
	private Date membershipStartDate;

	@Column(name = "MEMA_Membership_End_Date")
	private Date membershipEndDate;

	@Column(name = "MEMA_Membership_Status")
	private String membershipStatus;

	@Column(name = "MEMA_Created_Date", updatable = false)
	private Date createdDate;
	
	@Column(name = "MEMA_Updated_Date", insertable = false, updatable = false)
	private Date updatedDate;

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWorkStatus() {
		return workStatus;
	}

	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}

	public String getMembershipStatus() {
		return membershipStatus;
	}

	public void setMembershipStatus(String membershipStatus) {
		this.membershipStatus = membershipStatus;
	}

	public Integer getMemberId() {
		return memberId;
	}

	
	public Date getMembershipStartDate() {
		return membershipStartDate;
	}

	public void setMembershipStartDate(Date membershipStartDate) {
		this.membershipStartDate = membershipStartDate;
	}	

	public Date getMembershipEndDate() {
		return membershipEndDate;
	}

	public void setMembershipEndDate(Date membershipEndDate) {
		this.membershipEndDate = membershipEndDate;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
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

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
}
