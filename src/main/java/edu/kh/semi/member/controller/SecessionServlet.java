package edu.kh.semi.member.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.kh.semi.member.model.service.MemberService;
import edu.kh.semi.member.model.vo.Member;
@WebServlet("/member/secession")
public class SecessionServlet extends HttpServlet{
	
	
	// GET방식 요청 시 회원 탈퇴 JSP로 요청위임
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/views/member/secession.jsp").forward(req, resp);
	}
	// POST방식 요청 시 회원 탈퇴 수행
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String currentPw =req.getParameter("currentPw");
		HttpSession session = req.getSession();
		int memberNo = ((Member)session.getAttribute("loginMember")).getMemberNo();
		//Map을 이용하여 데이터 전달
		// ** Map을 이용하는 경우 : 기능 한두개 수행하는데 VO를 만들기 아까운 경우
		// - 서로 관련 없는 데이터들의 묶음이 필요한 경우
		
		Map<String, String> map = new HashMap<String , String>();
		// mep에 데이터 담기
		map.put("currentPw", currentPw);
		map.put("memberNo", memberNo + "");
		try {
			int result = new MemberService().secession(map);
			
			String message = null;
			String path = null;
			
			if(result > 0) {
				message = "회원탈퇴 성공";
				path = req.getContextPath(); // 메인페이지
				session.invalidate(); // 로그아웃 session 무효화
				// -> 기존 session 변수에 저장된 세션 객체가 무효화 되었기 때문에
				// 이후 새로운 세션객체를 얻어와야 한다.
			}else {// 실패
				message = "비밀번호가 일치하지 않습니다";
				path = "secession";
			}
			req.getSession().setAttribute("message", message);
			resp.sendRedirect(path);
		}catch(Exception e){
			e.printStackTrace();
			req.setAttribute("errorMessage", "회원 탈퇴 과정에서 문제가 발생했습니다.");
			req.setAttribute("e", e);
			
			req.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(req, resp);
			}
	}
}
