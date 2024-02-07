package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class TestRun {
	
	public static void main(String[] args) {
		jdbcOptionTest();
		// Properties
		
		/*
		 * 특징)
		 * 		- Map 계열의 컬렉션 (key-value 세트로 데이터를 저장)
		 * 		- 문자열(String) 타입으로 저장
		 * 		- 값을 저장 : setProperty(key,value)
		 * 		- 저장할 파일 종류 : .properties / .xml 형식으로 저장
		 */
		
		// SQL문 관련 파일 출력
		Properties prop = new Properties();
		
		// SQL문 저장
	/*	String insertQuery ="INSERT INTO MEMBER VALUES(SEQ_UNO.NEXTVAL,?,?,?,?,?,?,?,?,?,SYSDATE";
		prop.setProperty("insertMember", insertQuery);
		prop.setProperty("selectAllList", "SELECT * FROM MEMBER");
		prop.setProperty("searchByUserNameKeyword", "SELECT * FROM MEMBER WHERE USERNAME LIKE ?");
		prop.setProperty("searchByUserId",  "SELECT * FROM MEMBER WHERE USERID = ?" );
		String updateQuery = "UPDATE MEMBER SET USERPW=?, EMAIL = ?, PHONE =?, ADDRESS =? WHERE USERID = ?";
		prop.setProperty("updateMember", updateQuery);
		prop.setProperty("deleteMember", "DELETE FROM MEMBER WHERE USERID = ?");
		// --------------------------------------------------------------------------
		try {
			// 폴더경로/파일명.확장자
			prop.storeToXML(new FileOutputStream("resources/query.xml"),"Member Query");
			System.out.println("SQL문 저장 완료");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		try {
			// xml 형식의 데이터를 입력받을 때는 .loadFromXML()사용
			prop.loadFromXML(new FileInputStream("resources/query.xml"));
			
			System.out.println(prop.getProperty("insertMember"));
			System.out.println(prop.getProperty("searchByUserId"));
			System.out.println(prop.getProperty("deleteMember"));
			System.out.println(prop.getProperty("insertMember"));
			System.out.println(prop.getProperty("searchByUserNameKeyword"));
			System.out.println(prop.getProperty("selectAllList"));
			
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void jdbcOptionTest() {
		//JDBC 관련 설정 파일 출력
		Properties prop = new Properties();
		
		// jdbc driver 정보, url, userid, passwd => 설정 파일에 저장
		prop.setProperty("driver", "oracle.jdbc.driver.OracleDriver");
		prop.setProperty("url", "jdbc:oracle:thin:@localhost:1521:xe");
		prop.setProperty("username", "C##JDBC");
		prop.setProperty("passwd", "JDBC");
		
		try {
			prop.store(new FileOutputStream("resources/driver.properties"), "JDBC option");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//--------------------------------------------------------------------------------		
		//파일 읽어오기(입력)
	
		
		try {
			prop.load(new FileInputStream("resources/driver.properties"));
			
			String driver = prop.getProperty("driver");
			System.out.println(driver);
			System.out.println(prop.getProperty("url"));
			System.out.println(prop.getProperty("username"));
			System.out.println(prop.getProperty("password"));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static void propTest() {
		// 파일을 출력하는 것 => 파일에 데이터를 저장
		Properties prop = new Properties();
		
		prop.setProperty("C", "INSERT");		// Create 	: 데이터 추가
		prop.setProperty("R", "SELECT");		// Read 	: 데이터 조회
		prop.setProperty("U", "UPDATE");		// Update 	: 데이터 수정
		prop.setProperty("D", "DELETE");		// Delete 	: 데이터 삭제
	
		// 파일에 출력
		try {
			// 설정들을 저장하기 위한 파일 : .properties 형식 사용
			prop.store(new FileOutputStream("resources/test.properties"), "properties test");
		
			// SQL문들을 저장하기 위한 파일 : .xml 형식 사용
			prop.storeToXML(new FileOutputStream("resources/textXml.xml"), "properties test");
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
