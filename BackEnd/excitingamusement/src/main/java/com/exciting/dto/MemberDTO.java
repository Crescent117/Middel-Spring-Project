package com.exciting.dto;

import java.sql.Date;

public class MemberDTO {
	
	String member_id;
	String m_name;
	String m_birth;
	String m_pass;
	String m_email;
	int m_point;
	String m_address;
	Date m_regidate;
	String m_phone;
	String m_gender;
	String m_sha256;
	String m_enc;
	String m_image;
	int m_admin;
	String m_kakaoId;
	
	String m_email1;
	String m_email2;
	
	String m_phone1;
	String m_phone2;
	String m_phone3;
	
	String m_year;
	String m_month;
	String m_day;
	
	

	public MemberDTO() {
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public String getM_name() {
		return m_name;
	}

	public void setM_name(String m_name) {
		this.m_name = m_name;
	}
	
	public String getM_enc() {
		return m_enc;
	}
	
	public void setM_enc(String m_enc) {
		this.m_enc = m_enc;
	}

	public String getM_birth() {
		return m_birth;
	}

	public void setM_birth(String m_birth) {
		this.m_birth = m_birth;
	}

	public String getM_pass() {
		return m_pass;
	}

	public void setM_pass(String m_pass) {
		this.m_pass = m_pass;
	}

	public String getM_email() {
		return m_email;
	}

	public void setM_email(String m_email) {
		this.m_email = m_email;
	}

	public int getM_point() {
		return m_point;
	}

	public void setM_point(int m_point) {
		this.m_point = m_point;
	}

	public String getM_address() {
		return m_address;
	}

	public void setM_address(String m_address) {
		this.m_address = m_address;
	}

	public Date getM_regidate() {
		return m_regidate;
	}

	public void setM_regidate(Date m_regidate) {
		this.m_regidate = m_regidate;
	}

	public String getM_phone() {
		return m_phone;
	}

	public void setM_phone(String m_phone) {
		this.m_phone = m_phone;
	}

	public String getM_gender() {
		return m_gender;
	}

	public void setM_gender(String m_gender) {
		this.m_gender = m_gender;
	}
	
	
	public String getM_sha256() {
		return m_sha256;
	}

	public void setM_sha256(String m_sha256) {
		this.m_sha256 = m_sha256;
	}
	
	public int getM_admin() {
		return m_admin;
	}

	public void setM_admin(int m_admin) {
		this.m_admin = m_admin;
	}
	
	public String getM_image() {
		return m_image;
	}

	public void setM_image(String m_image) {
		this.m_image = m_image;
	}
	

	public String getM_email1() {
		return m_email1;
	}

	public void setM_email1(String m_email1) {
		this.m_email1 = m_email1;
	}

	public String getM_email2() {
		return m_email2;
	}

	public void setM_email2(String m_email2) {
		this.m_email2 = m_email2;
	}

	public String getM_phone1() {
		return m_phone1;
	}

	public void setM_phone1(String m_phone1) {
		this.m_phone1 = m_phone1;
	}

	public String getM_phone2() {
		return m_phone2;
	}

	public void setM_phone2(String m_phone2) {
		this.m_phone2 = m_phone2;
	}

	public String getM_phone3() {
		return m_phone3;
	}

	public void setM_phone3(String m_phone3) {
		this.m_phone3 = m_phone3;
	}

	public String getM_year() {
		return m_year;
	}

	public void setM_year(String m_year) {
		this.m_year = m_year;
	}

	public String getM_month() {
		return m_month;
	}

	public void setM_month(String m_month) {
		this.m_month = m_month;
	}

	public String getM_day() {
		return m_day;
	}

	public void setM_day(String m_day) {
		this.m_day = m_day;
	}

	
	public String getM_kakaoId() {
		return m_kakaoId;
	}

	public void setM_kakaoId(String m_kakaoId) {
		this.m_kakaoId = m_kakaoId;
	}

	@Override
	public String toString() {
		return "MemberDTO [member_id=" + member_id + ", m_name=" + m_name + ", m_birth=" + m_birth + ", m_pass="
				+ m_pass + ", m_email=" + m_email + ", m_point=" + m_point + ", m_address=" + m_address
				+ ", m_regidate=" + m_regidate + ", m_phone=" + m_phone + ", m_gender=" + m_gender + ", m_sha256="
				+ m_sha256 + ", m_enc=" + m_enc + ", m_image=" + m_image + ", m_admin=" + m_admin + ", m_kakaoId="
				+ m_kakaoId + ", m_email1=" + m_email1 + ", m_email2=" + m_email2 + ", m_phone1=" + m_phone1
				+ ", m_phone2=" + m_phone2 + ", m_phone3=" + m_phone3 + ", m_year=" + m_year + ", m_month=" + m_month
				+ ", m_day=" + m_day + "]";
	}
	
	
}
