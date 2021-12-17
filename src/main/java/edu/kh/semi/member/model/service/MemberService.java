package edu.kh.semi.member.model.service;

import static edu.kh.semi.common.JDBCTemplate.*;
// -> 해당 클래스 내 static 요소 호출 시 클래스명 생략

import java.sql.Connection;
import java.util.Map;

import edu.kh.semi.member.model.dao.MemberDAO;
import edu.kh.semi.member.model.vo.Member;

// Service : 데이터 가공 
//		   + 트랜잭션 제어(commit/rollback) 
//			-> 이 때 Connection 필요하기 때문에 Service에서 Connection 얻어오기 수행
public class MemberService {

	private MemberDAO dao = new MemberDAO();
	
	// alt + shift + j  : 클래스/메소드 설명 작성 주석
	
	/** 로그인 서비스
	 * @param memberId
	 * @param memberPw
	 * @return loginMember (실패 시 null 반환)
	 * @throws Exception
	 */
	public Member login(String memberId, String memberPw) throws Exception {
		
		// 1) Connection 얻어오기
		Connection conn = getConnection();
						// DBCP에 생성 되어있는 커넥션 객체 하나를 빌려오기.
		
		// 2) DAO 메소드 호출해서 결과 반환 받기
		Member loginMember = dao.login(memberId, memberPw, conn);
		
		// 3) 사용한 Connection 반환하기
		close(conn);  // == conn.close();
		
		// 4) 결과 반환
		return loginMember;
	}
	
	/** 회원가입 
	 * @param member
	 * @return result
	 * @throws Exception
	 */
	public int signUp(Member member) throws Exception{
		// 1)  DBCT에서 Connection 
		Connection conn = getConnection();
		
		int result = dao.signUp(member, conn);
		
		// 트렌잭션 제어
		if(result > 0 ) { commit(conn);}
		else 			  rollback(conn);
		
		// 커넥션 닫기
		close(conn);
		return result;
	}

	/** 아이디 중복확인
	 * @param inputId
	 * @return result 중복 1 아님 0
	 * @throws Exception
	 */
	public int idDupCheck(String inputId)throws Exception{

			Connection conn = getConnection();
			
			int result = dao.idDupCheck(inputId , conn);
			
			close(conn);
			
			
			return result;
	}

	/** 이메일 중복검사
	 * @param inputEmail
	 * @return result (1 중복 , 0 사용가능)
	 * @throws Exception
	 */
	public int EmailDupCheck(String inputEmail)throws Exception{
		
		Connection conn = getConnection();
		int result =  dao.EmailDupCheck(inputEmail , conn);
		
		close(conn);
		
		return result;
	}

	/** 아이디로 회원 정보 검색
	 * @param inputId
	 * @return member
	 * @throws Exception
	 */
	public Member idSearch(String inputId)throws Exception {
		Connection conn = getConnection(); // DBCP에서 커넥션 얻어오기
		
		Member member = dao.idSearch(inputId, conn);
		
		close(conn); // 사용 완료된 커넥션을 DBCP에 반환
		
		return member;
	}

	public int update(Member member)throws Exception{
		Connection conn = getConnection();
		int result = dao.update(member ,conn);
		
		if(result > 0) {commit(conn);}
		else 			rollback(conn);
		
		close(conn);
		return result;
	}

	/** 비밀번호 변경
	 * @param currentPw
	 * @param newPw1
	 * @param memberNo
	 * @return reslut
	 * @throws Exception
	 */
	public int updatePw(String currentPw, String newPw1 ,int memberNo)throws Exception {
		Connection conn = getConnection();
		int result = dao.updatePw(currentPw , newPw1 , memberNo,conn);
		if(result > 0 ) commit(conn);
		else			rollback(conn);
		close(conn);
		return result;
	}

	/** 회원탈퇴
	 * @param map
	 * @return result 1 성공 0 비밀번호 불일치
	 * @throws Exception
	 */
	public int secession(Map<String, String> map)throws Exception{
		// TODO Auto-generated method stub
		Connection conn = getConnection();
		int result = dao.secession(map,conn);
		if(result > 0) commit(conn);
		else		   rollback(conn);
		close(conn);
		return result;
	}
	
	
	
	
	
	
}
