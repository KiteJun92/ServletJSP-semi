package edu.kh.semi.member.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.kh.semi.member.model.service.MemberService;
@WebServlet("/member/idDupCheck")
public class IdDupCheckServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String inputId = req.getParameter("inputId");
		// 아이디가 입력 될 때 마다 비동기로 서버에 값이 넣어오는지 확인
		//System.out.println(inputId);
		try {
			MemberService service = new MemberService();
			int result = service.idDupCheck(inputId);
			// AJAX는 화면의 부분적인 갱신을 하는데 사용하기 때문에
			// 응답화면 전체를 다시 만드는 코드를 작성하지 않음
			// forward , redirect  구문 x
			
			PrintWriter out = resp.getWriter();
			
			out.print(result);	//아이디 중복 조회 결과를 클라이언트에게 출력
								// 현재 비동기 통신 중
								// 화면 재구성이 아닌 ajax 로 보낸다.
			
		}catch(Exception e){
			e.printStackTrace();
			// 강제로 에러 상태를 인식 시키고
			// 에러 메세지를 전달하기
			// HTTP 응답 상태 코드
			// 200 : 요청/ 응답 성공
			// 400 : 잘못된 요청
			// 403 : 서버가 접근을 거부
			// 404 : 요청 주소를 찾을 수 없음
			// 405 : 지정된 method(데이터 전달 방식)을 사용할 수 없음
			// 500 : 서버 내부 에러
			resp.setStatus(500);
			resp.getWriter().print(e.getMessage());
		}
	}
}
