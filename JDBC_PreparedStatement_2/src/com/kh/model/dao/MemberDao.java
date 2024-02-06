package com.kh.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.kh.model.vo.Member;

/**
 * DAO(Data Access Object)
 * : DB에 직접적으로 접근해서 사용자의 요청에 맞는 sql문 실행 후 결과 반환(JDBC 작업)
 * 	 결과를 Controller 쪽으로 전달
 */
public class MemberDao {
	/**
	 * JDBC용 객체
	 * 	- Connection : DB의 연결을 위한 객체. DB연결 정보를 가지고 있음
	 *  - [Prepared]Statement : 연결된 DB에 SQL문을 전달하여 실행하고, 결과를 돌려받는 객체
	 *  - ResultSet : SELECT문(SQL)을 실행하고 조회된 결과물들이 담겨있는 객체
	 *  
	 *  *JDBC 과정-> **순서**
	 *  [1] JDBC Driver 등록: 사용할 DBMS가 제공하는 클래스 등록
	 *  [2] Connection 객체 생성 : 연결하려는 DB정보를 입력하여 해당 DB와 연결하면서 객체 생성
	 *  [3] Statement 객체 생성 : Connection 객체를 통해 생성.
	 *  [4] SQL문 전달하여 실행 : Statement 객체를 이용하여 실행
	 *  [5] 결과 받기
	 *  		- DQL(SELECT) : ResultSet(조회된 결과물) 객체로 결과 받기
	 *  		- DML(INSERT, UPDATE, DELETE) : int(처리된 행 수)로 결과 받기
	 *  [6] 결과 처리
	 *  		- DQL 결과 : ResultSet에서 하나하나 뽑아서 vo객체에 차곡차곡 담기 
	 *  		- DML 결과 : 처리된 행 수가 있으면 (성공) commit,
	 *  					처리된 행 수가 없으면 (실패) rollback
	 *  [7] 사용 완료 후 자원 반납(close) -> **생성 역순으로**
	 */
	
	/*
	 * 사용자가 입력한 정보들을 DB에 추가하는 메소드
	 * @param m : 사용자가 입력한 회우너 정보를 담고있는 Member 객체
	 * @return : insert문 실행 후 처리된 행 수
	 */
	
	//1번 메뉴
	public int insertMember(Member m) {
		//INSERT문 실행 => 처리된 행 수 => 트랜잭션 처리
		
		//필요한 변수들 세팅
		Connection conn = null;		// DB연결
		//Statement -> PreparedStatement변경
		Statement stmt = null;		// SQL 실행 및 결과 받기
		int result = 0;				// 처리된 결과
		
		// SQL문 작성. 완성된 형태의 SQL
		// INSERT INTO MEMBER VALUES(SEQ_UNO.NEXTVAL, 'user1', 'pass1' ,'홍길동',
		//				'남',25,'hong@gmail.com', '01012345678','강남','독서',sysdate
		String sql = "INSERT INTO MEMBER VALUES (SEQ_UNO.NEXTVAL, "
				+ "'" + m.getUserId() + "',"
				+ "'" + m.getUserPw() + "',"
				+ "'" + m.getUserName() + "',"
				+ "'" + m.getGender() + "',"
				+ m.getAge() + ", "
				+ "'" + m.getEmail() + "',"
				+ "'" + m.getPhone() + "',"
				+ "'" + m.getAddress() + "',"
				+ "'" + m.getHobby() + "',"
				+ "SYSDATE)";
		try {
			// [1] JDBC DRIVER등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// [2] Connection 객체 생성
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","C##JDBC", "JDBC");

			// [3] Statement 객체 생성
			stmt = conn.createStatement();
			
			// [4], [5] sql문을 실행 및 결과 받기
			result = stmt.executeUpdate(sql);
			
			// [6] DML -> 트랜잭션 처리(결과가 0일때 (실패) ROLLBACK, 0보다 클때(성공) COMMIT)
			if(result > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// [7] 사용한 자원 반납
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * 회원 목록 전체 조회
	 * @return ArrayList<Member> : 회원 목록을 담은 ArrayList 객체를 반환
	 */
	//2번 메뉴
	public ArrayList<Member> selectAllList(){
		// SELECT문여러 행 조회) => ReslutSet 객체 => ArrayList<Member>에 담기
		
		// 필요한 변수 세팅
		ArrayList<Member> list = new ArrayList<Member>();		// 비어있는 상태
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;		// 조회된 결과들이 담길객체
		
		// SQL문 작성
		String sql = "SELECT * FROM MEMBER ORDER BY USERNO";
		try {
			// [1] jdbc 드라이버 등록 
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			// [2] Connection 객체 생성
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","C##JDBC", "JDBC");
		
			// [3] Statement 객체 생성
			pstmt = conn.prepareStatement(sql);
			
			// [4],[5] SQL문 실행하고 결과 받기(DQL(SELECT)->ResultSet타입으로 결과 받기)
			rset = pstmt.executeQuery(sql);
			
			// [6] 결과 처리
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
			
			// 반복문 끝난 시점
			// 조회된 데이터가 없다면 리스트가 비어있을 것이고
			// 조회된 데이터가 없다면 리스트에는 한 개이상의 데이터가 담겨있을 것이다.
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}  finally {
			// [7] 사용한 자원 반납(생성 역순으로)
			try {
				rset.close();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	/**
	 * 사용자가 입력한 회원 id값에 해당하는 회원 정보 조회
	 * @param m
	 * @return Member : 회원정보
	 */
	//3번 메뉴
	public Member searchMember(String id) {
		Connection conn = null;		// DB연결
		// Statement -> PreparedStatement 변경
		/*
			[1] 객체 변경
			[2]객체 생성
			conn.createStatement() -> conn.prepareStatement(SQL)
										con..setString(1,userId)//순번, 전달할 변수
			[3] SQL문 실행
			executeQuery(sql) -> executeQuery()
			[4] close쪽 참조변수 확인
		*/
		PreparedStatement pstmt = null;			// SQL 실행 및 결과 받기
		ResultSet rset = null;
		
		// String sql = "SELECT * FROM MEMBER WHERE USERID = "  + "'" +  id + "'";
		//회원 정보 조회(한개 행) => ResultSet => Member객체
		
		// PreparedStatement 객체 사용 시 작성한 SQL문
		String sql = "SELECT * FROM MEMBER WHere USERID = ?";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","C##JDBC", "JDBC");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			// *Statement 객체 사용 시
			// rest = stmt.executeQuery(sql);
			// * PreparedStatement 객체 사용 시
			
			rset = pstmt.executeQuery();
			
			if(rset.next()) {			// .next():데이터가 있는지 여부 체크
				Member m2 = new Member(
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
				return m2;
			}
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	//4번 메뉴
	public ArrayList<Member> searchName(String name) {
		Connection conn = null;		// DB연결
		//Statement stmt = null;		// SQL 실행 및 결과 받기
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		ArrayList<Member> list = new ArrayList<Member>();
		
//		String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE '%"  + name + "%'";
		String sql = "SELECT * FROM MEMBER WHERE USERNAME LIKE ?";
		
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","C##JDBC", "JDBC");
			// stmt = conn.createStatement();
			//객체 변경 Statement -> PreparedStatement
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
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	// 5번 메뉴
	public int updateUser(Member member) {
		Connection conn = null;		// DB연결
		// Statement stmt = null;		// SQL 실행 및 결과 받기
		PreparedStatement pstmt = null;
		int result = 0;
		
		/**
		 * sql문
		 * update member
		 * set userpw = 'pass2', email = 'xxx', phone = 'xxx', address = 'xxx'
		 * where userid = 'xxx'
		 */
		String sql = "UPDATE MEMBER SET USERPW = ? , EMAIL = ?, PHONE = ?, ADDRESS = ? WHERE USERID = ?";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","C##JDBC", "JDBC");
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getUserPw());
			pstmt.setString(2, member.getEmail());
			pstmt.setString(3, member.getPhone());
			pstmt.setString(4, member.getAddress());
			pstmt.setString(5, member.getUserId());
			result = pstmt.executeUpdate();
			
			if(result > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public int deleteMember(String userId) {
		Connection conn = null;		// DB연결
		PreparedStatement pstmt = null;
		int result = 0;
		
		/**
		 * sql문
		 * delete from member
		 * where userid = 'xxx'
		 */
		String sql = "DELETE FROM MEMBER WHERE USERID = ?";
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","C##JDBC", "JDBC");
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			result = pstmt.executeUpdate();
			

			if(result > 0) {
				conn.commit();
			} else {
				conn.rollback();
			}
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
}
