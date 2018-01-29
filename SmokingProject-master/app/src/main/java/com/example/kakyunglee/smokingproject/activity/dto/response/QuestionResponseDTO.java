package com.example.kakyunglee.smokingproject.activity.dto.response;

// 향후 추가될 가능성이 있음 > db구조와 맞춤 1022
public class QuestionResponseDTO {
	public int id;
	public String title;
	public int report_category_id;
	public String email;
	public String contents;
	public String image_url;
	public String created_at;


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getReport_category_id() {
		return report_category_id;
	}
	public void setReport_category_id(int report_category_id) {
		this.report_category_id = report_category_id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	@Override
	public String toString() {
		return "QuestionResponseDTO{" +
				"id=" + id +
				", title='" + title + '\'' +
				", report_category_id=" + report_category_id +
				", email='" + email + '\'' +
				", contents='" + contents + '\'' +
				", image_url='" + image_url + '\'' +
				", created_at='" + created_at + '\'' +
				'}';
	}
}
