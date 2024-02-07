package com.kh.model.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import com.kh.common.JDBCTemplate;
import com.kh.model.vo.Member;

// DAO (Data Access Object)
public class MemberDao {
	/*
	 * 기존 : DAO클래스에 사용자가 요청할 때마다 실행해야 하는 SQL문을 자바 소스코드 내에 명시적으로 작성(정적 코딩방식)
	 * -> 문제점 : SQL문을 수정해야 할 때 자바 소스코드를 수정해야 함=> 수정한 내용을 반영할 때 프로그램을 종료 후 재시작 해야 함
	 	-> 해결 : SQL문을 별도로 관리하는 외부파일로 만들어서 실시간으로 기록된 SQL문을 읽어서 실행(동적 코딩 방식)
	 */
	private Properties prop = new Properties();
	
	public MemberDao() {
		//xml읽기
		try {
			prop.loadFromXML(new FileInputStream("resources/query.xml"));
			
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 회원 추가 메소드 (사용자가 입력한 데이터들을 DB에 추가)
	 * @param conn, m : Connection 객체와 사용자가 입력한 데이터(Member)
	 * @return result : 처리된 행 수 (회원 추가 결과)
	 */
	public int insertMember(Connection conn, Member m) {
		// PreparedStatement 객체 생성
		PreparedStatement pstmt = null;
		
		int result = 0;
		// INSERT INTO MEMBER VALUES (SEQ_UNO,?,?,?,?,?,?,?,?,?,SYSDATE)
		String sql = prop.getProperty("insertMember");
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, m.getUserId());
			pstmt.setString(2, m.getUserPw());
			pstmt.setString(3, m.getUserName());
			pstmt.setString(4, m.getGender());
			pstmt.setInt(5, m.getAge());
			pstmt.setString(6, m.getEmail());
			pstmt.setString(7, m.getPhone());
			pstmt.setString(8, m.getAddress());
			pstmt.setString(9, m.getHobby());
			
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		
		return result;
	}
	
	/**
	 * 회원 전체 목록 조회
	 * @param conn : Connection 객체
	 * @return list : 회원 전체 목록을 담은 ArryaList<Member> 타입의 데이터
	 */
	public ArrayList<Member> selectAllList(Connection conn){
		ArrayList<Member> list = new ArrayList<Member>();
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;

		//String sql = "SELECT * FROM MEMBER ORDER BY USERNO";
		String sql = prop.getProperty("selectAllList");
		try {
			pstmt = conn.prepareStatement(sql);
			rset = pstmt.executeQuery(sql);
			
			while(rset.next()) {			// .next():데이터가 있는지 여부 체크
				Member m = new Member();
				m.setUserNo(rset.getInt("USERNO"));
				m.setUserId(rset.getString("USERID"));
				m.setUserPw(rset.getString("USERPW"));
				m.setUserName(rset.getString("USERNAME"));
				m.setGender(rset.getString("GENDER"));
				m.setAge(rset.getInt("AGE"));
				m.setEmail(rset.getString("Email"));
				m.setPhone(rset.getString("PHONE"));
				m.setAddress(rset.getString("ADDRESS"));
				m.setHobby(rset.getString("HOBBY"));
				m.setEnrollDate(rset.getDate("enrolldate"));
				// 현재 참조하고 있는 행에 대한 모든 데이터들을 Member 객체에 담기
				list.add(m);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return list;
	}
	
	/**
	 * 사용자가 
	 * @param conn : Connection객체
	 * @param userId : 검색할 회원 아이디
	 * @return
	 */
	public Member searchMember(Connection conn, String userId) {
		PreparedStatement pstmt = null;			// SQL 실행 및 결과 받기
		ResultSet rset = null;

		String sql = prop.getProperty("searchByUserId");
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			rset = pstmt.executeQuery();
			
			if(rset.next()) {			// .next():데이터가 있는지 여부 체크
				Member member = new Member(
						rset.getInt("USERNO"),
						rset.getString("USERID"),
						rset.getString("USERPW"),
						rset.getString("USERNAME"),
						rset.getString("GENDER"),
						rset.getInt("AGE"),
						rset.getString("Email"),
						rset.getString("PHONE"),
						rset.getString("ADDRESS"),
						rset.getString("HOBBY"),
						rset.getDate("enrolldate")
					);
				return member;
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return null;
	}
	
	/**
	 * 사용자가 입력한 회원 이름에 대한 키워드 검색
	 * @param conn : Connection객체
	 * @param name : 회원 이름에 대한 키워드
	 * @return list : 키워드에 해당되는 회원 목록
	 */
	public ArrayList<Member> searchName(Connection conn, String name) {
		ArrayList<Member> list = new ArrayList<Member>();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String sql = prop.getProperty("searchByUserNameKeyword");
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + name + "%");
			rset = pstmt.executeQuery();
			
			while(rset.next()) {			// .next():데이터가 있는지 여부 체크
				Member member = new Member(
						rset.getInt("USERNO"),
						rset.getString("USERID"),
						rset.getString("USERPW"),
						rset.getString("USERNAME"),
						rset.getString("GENDER"),
						rset.getInt("AGE"),
						rset.getString("Email"),
						rset.getString("PHONE"),
						rset.getString("ADDRESS"),
						rset.getString("HOBBY"),
						rset.getDate("enrolldate")
					);
				list.add(member);
			}
			return list;
		
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(rset);
			JDBCTemplate.close(pstmt);
		}
		return null;
	}
	
	public int updateUser(Connection conn, Member member) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql =  prop.getProperty("updateMember");
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getUserPw());
			pstmt.setString(2, member.getEmail());
			pstmt.setString(3, member.getPhone());
			pstmt.setString(4, member.getAddress());
			pstmt.setString(5, member.getUserId());
			result = pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}
	
	public int deleteMember(Connection conn, String userId) {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "DELETE FROM MEMBER WHERE USERID = ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			result = pstmt.executeUpdate();
		
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			JDBCTemplate.close(pstmt);
		}
		return result;
	}
}