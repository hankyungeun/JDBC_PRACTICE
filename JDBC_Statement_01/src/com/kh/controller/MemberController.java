package com.kh.controller;

import java.sql.Date;
import java.util.ArrayList;

import com.kh.model.dao.MemberDao;
import com.kh.model.vo.Member;
import com.kh.view.MemberMenu;

/**
 * Controller : view를 통해서 사용자가 요청한 기능을 처리하는 담당 해당 메소드로 전달된 데이터 가공 처리 후 DADO로
 * 전달하여 호출 DAO로부터 반환받은 결과에 따라서 성공인지 실패인지 판단 후 응답화면 결정(view메소드 호출)
 */
public class MemberController {
	/**
	 * 회원 추가 요청을 처리해주는 메소드
	 * 
	 * 전달받은 데이터들
	 * 
	 * @param usrId ~ hoby : 사용자가 입력했던 정보들이 담겨있는 매개변수
	 */

	public void insertMember(String userId, String userPw, String userName, String gender, int age, String email,
			String phone, String address, String hobby) {
		// view로부터 전달받은 값을 바로 dao쪽으로 전달x
		// 어딘가(Member객체(vo)에 담아서 전달

		// [1] 기본 생성자로 생성하여 각 필드에 setter 메소드를 통해 하나하나 담는 방법
		// [2] 매개변수생성자 로 생성과 동시에 담는방법
		Member m = new Member(userId, userPw, userName, gender, age, email, phone, address, hobby);

		int result = new MemberDao().insertMember(m);

		if (result > 0) { // 회원 추가 성공
			new MemberMenu().displaySuccess("회원추가 성공");
		} else { // 회원 추가 실패
			new MemberMenu().displayFailed("회원추가 실패");
		}

	}

	public void selectAllList() {
		// list객체에 회원 전체 목록을 담기
		ArrayList<Member> list = new MemberDao().selectAllList();

		// view쪽에 결과 전달하여 출력
		// list.size() > 0 : 목록을 출력
		// list.size() == 0 : 데이터가 없습니다.
		if (list.isEmpty()) {
			new MemberMenu().displayNoData(null);
		} else {
			new MemberMenu().displayList(list);

		}
	}
	
	public void searchId(String Id) {
		Member m = new MemberDao().searchMember(Id);

		if (m == null) {
			new MemberMenu().displayFailed("회원이 없습니다.");
		} else {
			new MemberMenu().displayMember(m);
		}
	}
	
	public void searchName(String name) {
		ArrayList<Member> result = null;
		result = new MemberDao().searchName(name);
		
		if (result.isEmpty()) {
			new MemberMenu().displayFailed("회원이 없습니다.");
		} else {
			new MemberMenu().displaySuccess("\n 검색 성공");
			new MemberMenu().displayList(result);
		}
	}
	public Member selectByUserId(String userId) {
		return new MemberDao().searchMember(userId);
	}
	
	public void updateUser(String userId, String userPw, String email, String phone, String address) {
		Member m = new Member();
		
		m.setUserId(userId);
		m.setUserPw(userPw);
		m.setEmail(email);
		m.setPhone(phone);
		m.setAddress(address);
		int result = new MemberDao().updateUser(m);
		if (result > 0) { // 업데이트 성공
			new MemberMenu().displaySuccess("회원 정보 수정 완료");
		} else { // 업데이트 실패
			new MemberMenu().displayFailed("회원 정보 수정 실패");
		}
	}
	
	public void deleteUser(String userId) {
		int result = new MemberDao().deleteMember(userId);
		
		if (result > 0) { // 업데이트 성공
			new MemberMenu().displaySuccess("회원 삭제 완료");
		} else { // 업데이트 실패
			new MemberMenu().displayFailed("회원 삭제 실패");
		}
	}
}
