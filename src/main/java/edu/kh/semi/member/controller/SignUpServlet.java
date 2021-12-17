package edu.kh.semi.member.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.kh.semi.member.model.service.MemberService;
import edu.kh.semi.member.model.vo.Member;

@WebServlet("/member/signup")
public class SignUpServlet extends HttpServlet{
	
	// 데이터 전송 방식
	// POST : 삽입 			C
	// GET : 조회(검색)  	R
	// PUT : 수정			U
	// DELETE : 삭제		D
	
	
	// GET 방식 요청시 회원가입 화면으로 응답
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String path = "/WEB-INF/views/member/signUp.jsp";//WEB-INF 직접접근 불가 servlet 을통해서 가능
		req.getRequestDispatcher(path).forward(req, resp);
	
	
	}

	//POST 방식 요청 시 회원 정보 삽입 수행
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
		// POST 방식의 경우 문자 인코딩 처리가 필요하지만
		// encodingFilter를 생성했기 때문에 별도 처리 x
	
		
		// 회원 가입 시 입력 받은 파라미터를 모두 변수에 저장
		String memberId = req.getParameter("id");
		String memberPw = req.getParameter("pwd1");
		String memberName = req.getParameter("name");
		String memberEmail = req.getParameter("email");
		String[] phone  = req.getParameterValues("phone");
		String[] address = req.getParameterValues("address");
		
		// 필수 입력인 전화번호를 하나의 문자열로 합치기
		String memberPhone = String.join("-", phone);
		// String.join(구분자 , 배열) : 배열요소를 하나의 문자열로 합치되 요소 사이사이에 구분자를 추가
		
		// 선택적 입력인 주소를 하나의 문자열로 합치기
		// -> 입력 안되면 모든 요소가 빈칸
		String memberAddress = null;
		if(!address[0].equals("")) { // 우편 번호가 없음 == 주소 작성시
			memberAddress = String.join(",", address);
			// 크로스사이트 스크립트 방지 처리
			memberAddress = replaceParameter(memberAddress);
		}
		// 저장된 파라미터를 하나의 Member 객체에 저장
		Member member = new Member(memberId, memberPw, memberName, memberPhone, memberEmail, memberAddress);
		try {
			MemberService service = new MemberService();
			int result = service.signUp(member);
			// 중간 확인
			System.out.println("result : " + result);
			String message = null;
			if(result > 0) { //회원가입 성공시
				message = "회원가입 성공!"; 
				// 메인 페이지로 돌아갈 예정
				
			}
			//redirect를 할 예정이기 때문에
			// req를 사용한 값 전딜이 불가능하다  범위가 더 넓은 session을 사용한다.
			HttpSession session =req.getSession();
			session.setAttribute("message", message);
			
			
			// 메인 페이지로 돌아갈 예정
			// -> 이미 메인페이지를 보여주는 서비스가 구현되어 있음
			// -> 해당 서비스를 요청
			// -> 요청받은 Servlet이 다른 Servlet을 요청 == 재 요청(redirect)
			resp.sendRedirect(req.getContextPath());
			// redirect 시 기존 req ,resp 객체 삭제하고
			// 재요청 시 새롭게 생성
		}catch(Exception e){
			e.printStackTrace();
			//http 상태 코드 = 500 Internal Server Errow
			// => 백엔드에서 발생한 에러
			// =? 로직 수행에 사용되는 값 ,SQL , DB연결 과정 , 코드 오타 등
			// 문제가 발생했을 때 나타난 상태 코드
			String errorMessage = "회원 가입 중 문제 발생";
			req.setAttribute("errorMessage", errorMessage);
			req.setAttribute("e", e);// 예외 관련 정보를 답고있는 객체
		String path = "/WEB-INF/views/common/error.jsp";
		RequestDispatcher dispatcher = req.getRequestDispatcher(path);
		dispatcher.forward(req, resp);
	}
}
	// 크로스 사이트 스크립트 방지 처리 메서드
	private String replaceParameter(String param) {
		
		// 크로스 사이트 스크립트(XSS) 공격이 가능한 이유
		// ->    <, >, &, /  같은 HTML에서 태그, 특수문자에 사용되는 코드가
		//       문자가 아닌 HTML 코드로 해석되서 문제가 발생한다.
		
		// -> HTML 코드로 해석되었을 때 문제가 발생할만한 특수문자를
		//     문자로 인식되도록 변경한다.
		
		String result = param;
		
		if(result != null) {
			// result = "<script>alert("& 1234")</script>";
			
			result = result.replaceAll("&", "&amp;");
			result = result.replaceAll("<", "&lt;");
			result = result.replaceAll(">", "&gt;");
			result = result.replaceAll("\"", "&quot;");
			
		}
		return result;
	}
}