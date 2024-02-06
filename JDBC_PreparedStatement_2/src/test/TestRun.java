package test;

import java.util.Scanner;
import java.sql.*;

public class TestRun {
	
	public static void main(String[] args) {
		
		/*
		 * *JDBC 용 객체
		 * 	- Connection	 	 : DB의 연결 정보를 담고있는 객체
		 *  -[pREPARED]Statement : 연결된 DB에 SQL문을 전달하여 실행하고
		 *  						그 결과를 받아주는 객체 **중요 객체**
		 *  - ResultSet			 : SELECT문 실행 후 조회된 결과들이 담겨있는 객체
		 *  
		 *  * JDBC 과정(** 순서 중요 **)
		 *  1) JDBC Driver 등록	: 해당 DBMS(오라클)
		 *  2) Connection 생성	: 연결하고자 하는 DB정보를 입력해서 해당 DB와 연결하면서 생성
		 *  					  - URL(접속 주소), 사용자이름(ID), 비밀번호(PW)
		 *  3) Statement 생성		: Connection 객체를 이용하여 생성
		 *  					  sql문을 실행하고 결과를 받는 역할
		 *  4) sql문을 DB에 전달하여 실행		: Statement 객체를 이용하여 실행
		 *  5) 결과 받기
		 *  	[1] SELECT문 실행 : ResultSet 객체(조회된 데이터들이 담겨져있음)
		 *  	[2]    DML문 실행 : int (처리된 행 수)		
		 *  6) 결과 처리
		 *  	[1] ResultSet에 담겨져 있는 데이터들을 하나하나 뽑아서 vo객체에 옮겨 담기[+ ArrayList에 차곡차곡 담기]
		 *  	[2] 트랜잭션 처리 (성공적으로 수행했으면 commit, 실패했으면 rollback)
		 *  7) 사용이 완료되면 JDBC용 객체들을 반드시 자원 반납(close) => 생성 역순으로
		 */
//		insertTest();
		// 2. 각자 PC의 DB에 JDBC계정으로 접속하여 TEST테이블의 모든 데이터를 조회(SELECT)해보기
		//			DQL(SELECT) => ResultSet 객체 (조회된 모든 데이터) => 컬럼 값들을 각각 뽑아내기
		
		// 필요한 변수 세팅
		Connection conn = null;
		Statement stmt = null;
		ResultSet rset = null;
		
		//sql문 작성
		String sql ="SELECT * FROM TEST";
		
		try {
			// 1) jdbc driver 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			System.out.println("oracle driver 등록 성공");
			// 2) Connection 객체 생성
//				- URL => jdbc:oracle:thin:@접속IP(호스트명):포트:SID
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "C##JDBC","JDBC");
			
			// 3) Statement 객체 생성
			stmt = conn.createStatement();
			
			// 4, 5) sql문을 실행하고 결과 받기 (결과 : ResultSet)
			rset = stmt.executeQuery(sql);
			
			// 6) 결과를 하나하나 뽑아보기
			//	  데이터가 있는 지 여부 : rset.next() : boolean(데이터가 있으면 true, 없으면 false)
			while(rset.next()) {
				// 데이터를 가지고 올 때, '컬럼명' 또는 '컬럼순번'
				int tno = rset.getInt("TNO");
				String tname = rset.getString("TNAME");
				Date tdate = rset.getDate(3);
				System.out.println(tno + tname + tdate);
			}
		} catch (ClassNotFoundException e) {
			System.out.println("oracle driver 등록에 실패했습니다.");
			e.printStackTrace();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				rset.close();
				stmt.close();
				conn.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//TEST테이블에 INSERT하는 함수
	public static void insertTest() {
		Scanner sc = new Scanner(System.in);
		// 1. 각자 PC(127.0.0.1 | localhost)에 JDBC계정을 연결한 후 TEST테이블에 INSERT 해보기
		//			DML(INSERT) => 처리된 행 수 (int) => 트랜잭션 처리
		
		// 필요한 변수 세팅
		int result = 0;				// 결과(처리된 행 수)를 받아줄 변수
		Connection conn = null;		// DB의 연결정보를 보관할 객체
		Statement stmt = null;		//sql문을 전달하여 실행 후 결과를 받는 객체
		
		//실행할 sql문("완성 형태"로 만들기)
		// * Statement 객체에는 항상 완성 형태의 sql문을 작성해야 함
		// *SQL문 맨 뒤에 세미콜론이 없어야함!
//		String sql = "INSERT INTO TEST VALUES(1,'애기',SYSDATE)";
		System.out.print("번호 입력 : ");
		int tno = sc.nextInt();
		System.out.print("이름 입력 : ");
		String tname = sc.next();
		String input = "INSERT INTO TEST VALUES(" + tno +", '" + tname+"' ," + "SYSDATE)";
		
		try {
			//1) JDBC 드라이버 등록
			Class.forName("oracle.jdbc.driver.OracleDriver");
			// * ClassNotFoundException 발생 시 확인할 부분
			//	[1] ojdbcX.jar 파일 등록(추가)했는지
			//	[2] 추가 했는데도 오류 난다면 오타확인
			System.out.println("OracleDriver 등록 완료");
			
			//2) Connection 객체 생성 : DB에 연결(url, 사용자명, 비밀번호)
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe",
												"C##JDBC","JDBC");
			stmt = conn.createStatement();
			// 4, 5) sql문을 전달하면서 실행 후 결과 받기(결과 : 처리된 행 수)
			result = stmt.executeUpdate(input);
			
			// 실행한 sql문이 DML문(INSERT, UPDATE, DELETE)일 경우 => stmt.executeUpdate(sql) : int
			// 실행한 sql문이 SELECT문일 경우 => stmt.executeUpdate(sql) : ResultSet
			
			// 6) 트린잭션 처리(내가 실행한 sql문이 DML문이므로, 트랜잭션 처리 필요)
			/*
			 * Auto commit : 기본 on(jdbc6 버전 이후 추가됨)
			 * - off 설정 방법
			 * 		* 코드 작성 : conn.setAutoCommit
			 * 		* 실행 시 옵션 : JVM 실행 옵션 추가
			 * 					-Doracle.jdbc.autoCommitSpecCompliant=false
			 * 					=> Run Configuration -> Arguments -> VM arguments 에 추가
			 */
			// conn.setAutoCommit(false)
			if(result > 0) {			// 성공, commit
				conn.commit();
			}else {						// 실패, rollback
				conn.rollback();
			}
			
		} catch (ClassNotFoundException e) {
			System.out.println("OracleDriver를 찾지 못했습니다.");
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			//사용 완료 후 JDBCM 객체 반납(생성 역순으로)
			try {
				stmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(result > 0) {
			System.out.println("성공적으로 데이터가 추가되었습니다.");
		} else {
			System.out.println("데이터 추가에 실패하였습니다.");
		}
	}

}
