package com.javalab.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * [static 전역변수]
 * JDBC 프로그래밍을 위한 요소들을 모두 멤버변수 즉, 필드 위치로 뽑아올림
 * 	- 본 클래스 어디서라도 사용가능한 전역변수가 됨
 */

public class Database01_field {
	// [멤버변수]
	// 1. oracle 드라이버 이름 문자열 상수
	public static final String DRIVER_NAME = "oracle.jdbc.driver.OracleDriver";
	
	// 2. oracle 데이터베이스 접속 경로(url) 문자열 상수
	public static final String DB_URL = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
	
	// 3. 데이터베이스 접속 객체
	public static Connection con = null;
	
	// 4. query 실행 객체
	public static PreparedStatement pstmt = null;
	
	// 5. select 결과 저장 객체
	public static ResultSet rs = null;
	
	// 6. oracle 계정(id/pwd)
	public static String oracleId =  "tempdb";
	
	// 7. oracle password
	public static String oraclePwd = "1234";
	
	// main 메소드
	public static void main(String[] args) {
	
		try {
			// 1. 드라이버 로딩
			Class.forName(DRIVER_NAME);
			System.out.println("1. 드라이버 로드 성공!");

			// 2. 데이터베이스 커넥션(연결)
			con = DriverManager.getConnection(DB_URL, oracleId, oraclePwd);
			System.out.println("2. 커넥션 객체 생성 성공!");

			// 3. 생성한 statment 객체를 통해서 쿼리하기 위한 SQL 쿼리 문장 만들기(삽입, 수정, 삭제, 조회)
			String sql = "select C.CATEGORY_ID, C. CATEGORY_NAME, P.PRODUCT_ID, P.PRODUCT_NAME, P.PRICE, P.RECEIPT_DATE";
			sql += " from category C, product P";
			sql += " where c.category_id = p.category_id";
			sql += " ORDER by c.category_id ASC, p.product_id DESC";

			// 4. 커넥션 객체를 통해서 데이터베이스에 쿼리(SQL)를 실행해주는
			// prepareStatement 객체 얻음
			pstmt = con.prepareStatement(sql);
			System.out.println("3. stmt 객체 생성 성공 : ");

			// 5. Statment 객체의 executeQuery() 메소드를 통해서 쿼리 실행
			// 데이터 베이스에서 조회된 결과가 ResultSet 객체에 담겨옴
			rs = pstmt.executeQuery(sql);
			System.out.println();

			// 6. rs.next()의 의미 설명
			while (rs.next()) {
				System.out.println(rs.getInt("CATEGORY_ID") + "\t" 
								 + rs.getString("CATEGORY_NAME") + "\t"
								 + rs.getInt("PRODUCT_ID")+ "\t"
								 + rs.getString("PRODUCT_NAME")+ "\t"
								 + rs.getInt("PRICE")+"\t"
								 + rs.getDate("RECEIPT_DATE"));

			}

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 ERR! :" + e.getMessage());
		} catch (SQLException e) {
			System.out.println("SQL ERR : " + e.getMessage());
		} finally {
			try { // 자원 해제(반납) 순서는 작은거에서 큰걸로 가야함
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();

				}
			} catch (SQLException e) {
				System.out.println("자원해제 ERR! : " + e.getMessage());
			}
		}
	}

}