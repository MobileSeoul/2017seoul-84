package com.example.kakyunglee.smokingproject.activity.dto;

import java.io.Serializable;

public class NoticeDTO implements Serializable {
	int no;
	String created_at;
	String title;
	String contents;
	int type;

	String ext_url;
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
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
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public String getExt_url() {
		return ext_url;

	}
	public void setExt_url(String ext_url) {
		this.ext_url=ext_url;
	}
	public int getType() {
		return type;
	}
	@Override
	public String toString() {
		return "NoticeDTO [no=" + no + ", created_at=" + created_at + ", title=" + title + ", contents=" + contents
				+ ", type=" + type + ", ext_url=" + ext_url + "]";
	}
	public void setType(int type) {
		this.type=type;
	}

}

