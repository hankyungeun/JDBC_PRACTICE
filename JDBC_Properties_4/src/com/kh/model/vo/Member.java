package com.kh.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * VO(Vavlue Object)
 * 한 명의 회원(db 테이블의 한 행 데이터)에 대한 데이터를 기록하기위한 저장용 객체 
 */
@Data
@AllArgsConstructor
@ToString
public class Member {
	private int userNo;
	private String userId;
	private String userPw;
	private String userName;
	private String gender;
	private int age;
	private String email;
	private String phone;
	private String address;
	private String hobby;
	private Date enrollDate;
	
	public Member(String userId, String userPw, String userName, String gender, int age,
					String email, String phone, String address, String hobby) {
		super();
		this.userId = userId;
		this.userPw = userPw;
		this.userName = userName;
		this.gender = gender;
		this.age = age;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.hobby = hobby;
	}

	public Member() {
		// TODO Auto-generated constructor stub
	}
	

}
