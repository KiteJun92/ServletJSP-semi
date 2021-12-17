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

@WebServlet("/member/updatePw")
public class UdatePwServlet extends HttpServlet {
	
	// GET 방식 요청 시 비밀번호 변경 JSP로 요청 위임
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.getRequestDispatcher("/WEB-INF/views/member/updatePw.jsp").forward(req, resp);
			
	}
	// POST 방식 요청 시 DB 비밀번호 변경
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String currentPw = req.getParameter("currentPw");
		String newPw1 = req.getParameter("newPw1");

		System.out.println("currentPw" + currentPw);
		System.out.println("newPw1" + newPw1);
		
		HttpSession session = req.getSession();
		
		Member loginMember = (Member)session.getAttribute("loginMember");
		
		int memberNo = loginMember.getMemberNo();
		// int memberNo = ((Member)req.getSession().getAttribute("loginMember")).getMemberNo();
		try {
			int result = new MemberService().updatePw(currentPw,newPw1,memberNo);
			String message = null;
			String path = null;
			
			
			if(result > 0) {
				/*
				 * session.setAttribute("message", "비밀번호가 수정 되었습니다");
				
				 */
				message = "비밀번호가 변경되었습니다";
				path = "myPage";
			}else {
				/* session.setAttribute("message", "비밀번호 수정실패"); */
				message = "현재 비밀번호가 일치하지 않습니다.";
				path = "updatePw";
			}
			req.getSession().setAttribute("message", message);
			resp.sendRedirect(path);
		}catch(Exception e) {
			String message = "비밀번호 수정중 오류입니다.";
			req.setAttribute("message", message);
			req.setAttribute("e", e);
			req.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(req, resp);
			
			
		}
	}
}
