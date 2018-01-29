package com.example.kakyunglee.smokingproject.activity.dto;

import java.math.BigDecimal;

public class ReportDetailDTO {
	int no;
	int report_category_id;
	BigDecimal latitude;
	BigDecimal longtitude;
	String email;
	String image_url;
	String contents;
	int report_id;
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}

	public int getReport_category_id() {
		return report_category_id;
	}
	public void setReport_category_id(int report_category_id) {
		this.report_category_id = report_category_id;
	}
	public BigDecimal getLatitude() {
		return latitude;
	}
	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}
	public BigDecimal getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(BigDecimal longtitude) {
		this.longtitude = longtitude;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public int getReport_id() {
		return report_id;
	}
	public void setReport_id(int report_id) {
		this.report_id = report_id;
	}

	
}
