package com.kh.model.service;

import java.sql.Connection;
import java.util.ArrayList;

import com.kh.common.JDBCTemplate;
import com.kh.model.dao.MemberDao;
import com.kh.model.vo.Member;

public class MemberService {
	/*
	 * 1) JDBC driver 등록
	 * 2) Connection 객체 생성
	 * 3) Connection 객체 처리
	 */
	
	// 회원 추가
	public int insertMember(Member m) {
		// 1) Connection 객체 생성 (jdbc driver 등록 포함)
		Connection conn = JDBCTemplate.getConnection();
		
		// 2) Member객체 dao 전달하여 데이터 처리에 대한 결과 받기
		int result = new MemberDao().insertMember(conn, m);
		
		// 3) 데이터 추가/수정/삭제(DML) 작업 시 트랜잭션 처리
		if(result > 0) {			// 회원 추가 성공
			JDBCTemplate.commit(conn);
		} else {					// 회원 추가 실패
			JDBCTemplate.rollback(conn);
		}
		JDBCTemplate.close(conn);
		
		return result;
	}
	
	public ArrayList<Member> selectAllList() {
		Connection conn = JDBCTemplate.getConnection();
		ArrayList<Member> list = new MemberDao().selectAllList(conn);
		JDBCTemplate.close(conn);

		return list;
	}
	
	public Member searchMember(String userId) {
		Connection conn = JDBCTemplate.getConnection();
		Member member = new MemberDao().searchMember(conn, userId);
		JDBCTemplate.close(conn);
		return member;
	}
	
	public ArrayList<Member> searchName(String name){
		Connection conn = JDBCTemplate.getConnection();
		ArrayList<Member> list =new MemberDao().searchName(conn, name);
		JDBCTemplate.close(conn);
		return list;
	}
	
	public int updateUser(Member member) {
		Connection conn = JDBCTemplate.getConnection();
		
		int result = new MemberDao().updateUser(conn, member);
		
		if(result > 0) {			// 회원 업데이트 성공
			JDBCTemplate.commit(conn);
		} else {					// 회원 업데이트 실패
			JDBCTemplate.rollback(conn);
		}
		JDBCTemplate.close(conn);
		
		return result;
	}
	
	public int deleteMember(String userId) {
		Connection conn = JDBCTemplate.getConnection();
		
		int result = new MemberDao().deleteMember(conn, userId);
		
		if(result > 0) {			// 회원 업데이트 성공
			JDBCTemplate.commit(conn);
		} else {					// 회원 업데이트 실패
			JDBCTemplate.rollback(conn);
		}
		JDBCTemplate.close(conn);
		
		return result;
	}

}
