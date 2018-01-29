package com.example.kakyunglee.smokingproject.activity.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NoticeListDTO implements Serializable{
	private static final long serialVersionUID=1L;
	public List<NoticeDTO> noticeLists=new ArrayList<NoticeDTO>();
	public void getName() {
		for(NoticeDTO notice : noticeLists) {
			System.out.println(notice.getContents());
		}
	}

}
