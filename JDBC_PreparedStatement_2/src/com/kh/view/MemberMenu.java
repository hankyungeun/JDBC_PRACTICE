package com.kh.view;

import java.util.ArrayList;
import java.util.Scanner;

import com.kh.controller.MemberController;
import com.kh.model.vo.Member;

/**
 * View : 사용자가 보게 될 시각적인 요소(화면) 출력 및 입력
 */
public class MemberMenu {
	private Scanner sc = new Scanner(System.in);
	private MemberController mc = new MemberController();
	
	public void mainMenu() {
		while(true) {		// 사용자가 종료하기 전까지 메뉴를 계속 출력
			System.out.println("\n==회원 관리 프로그램");
			System.out.println("1. 회원 추가");
			System.out.println("2. 회원 전체 조회");
			System.out.println("3. 회원 아이디 검색");
			System.out.println("4. 회원 이름으로 키워드 검색");
			System.out.println("5. 회원 정보 변경");
			System.out.println("6. 회원 탈퇴");
			System.out.println("0. 프로그램 종료");
			
			System.out.print(">> 메뉴 선택 : ");
			int menu = sc.nextInt();
			
			sc.nextLine();
			switch(menu) {
			case 1: inputMember(); break;
			case 2: mc.selectAllList(); break;
			case 3: searchUserId(); break;
			case 4: searchByName(); break;
			case 5: updateUser(); break;
			case 6: deleteUser(); break;
			case 0: return;
			default : System.out.println("잘못 선택했습니다. 다시 선택해주세요.");
			}
		}
	}
	/**
	 * 회원 추가 창(서브화면) 추가하고자 하는 회원의 정보를 입력받아 회원 추가를 요청하는 창
	 */
	public void inputMember() {
		System.out.print("아이디 : ");
		String userId= sc.nextLine();
		
		System.out.print("비밀번호 : ");
		String userPw = sc.nextLine();
		
		System.out.print("이름 : ");
		String userName = sc.nextLine();
		
		System.out.print("성별(남/여) : ");
		String gender = sc.nextLine();
		
		System.out.print("나이 : ");
		int age = sc.nextInt();
		sc.nextLine();
		
		System.out.print("이메일 : ");
		String email = sc.nextLine();
		
		System.out.print("전화번호 (-빼고 입력) : ");
		String phone = sc.nextLine();
		
		System.out.print("주소 : ");
		String address = sc.nextLine();
		
		System.out.print("취미 (,로 구분하여 어러개 작성 가능) : ");
		String hobby = sc.nextLine();
		
		mc.insertMember(userId, userPw, userName, gender, age, email, phone, address, hobby);
	}
	
	public void searchUserId() {
		String userId= inputUserId();
		mc.searchId(userId);
		
	}
	public String inputUserId() {
			System.out.print("아이디 입력 : ");
			return sc.nextLine();
	}
	
	public void searchByName() {
		System.out.print("회원 이름 입력 : ");
		String name = sc.nextLine();
		mc.searchName(name);
	}
	
	public void updateUser() {
		System.out.println("\n==회원 정보 수정==");
		String userId = inputUserId();
		
		Member m =mc.selectByUserId(userId);
		if(m==null) {
			displayNoData("회원 정보 없음");
		} else {
			System.out.print("변경할 비밀번호 입력 : ");
			String newPasswd = sc.nextLine();
			System.out.print("변경할 이메일 입력 : ");
			String newEmail = sc.nextLine();
			System.out.print("변경할 연락처 입력 : ");
			String newPhone = sc.nextLine();
			System.out.print("변경할 주소 입력 : ");
			String newAddr = sc.nextLine();
			mc.updateUser(userId, newPasswd, newEmail, newPhone, newAddr);
			
		}
	}
	
	public void deleteUser() {
		String userId = inputUserId();
		
		Member m =mc.selectByUserId(userId);
		if(m==null) {
			displayNoData("회원 정보 없음");
		} else {
			mc.deleteUser(userId);
		}
	}
	
	//------------------------응답화면--------------------------------
	/**
	 * 요청 처리 후 성공했을 경우 사용자가 보게될 화면
	 * @param message : 객체 별 성공 메세지
	 */
	public void displaySuccess(String message) {
		System.out.println("\n 요청 성공 : " + message);
	}
	
	/**
	 * 요청 처리 후 실패했을 경우 사용자가 보게될 화면
	 * @param message : 객체 별 실패 메시지
	 */
	public void displayFailed(String message) {
		System.out.println("\n 요청 실패 : " + message);
	}
	
	/**
	 * 조회 요청 후 조회 결과가 없을 때 사용자가 보게될 화면
	 * @param message : 객체 별 조회 결과에 대한 메시지
	 */
	public void displayNoData(String message) {
		System.out.println("\n 결과 없음 : message");
	}
	
	/**
	 * 조회 요청 후 조회 결과가 있을 때 사용자가 보게될 화면
	 * @param list : 조회된 결과 목록이 담겨진 리스트 객체
	 */
	public void displayList(ArrayList<Member> list) {
		System.out.println("\n 조회된 결과는 다음과 같습니다.");
		for(Member m : list) {
			System.out.println(m);
		}
	}
	
	public void displayMember(Member member) {
		System.out.println(member);
	}
	
	
}
