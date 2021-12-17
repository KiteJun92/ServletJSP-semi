package edu.kh.semi.member.controller;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.catalina.connector.Response;
@WebServlet("/member/logout")
public class LogoutServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 로그인 == session에 회원 정보를 추가
		// 로그 아웃 == session에 회원 정보를 제거
		
		
		HttpSession session = req.getSession();
		//session.removeAttribute("loginMember");
		// 현재 session에서 loginMember  정보만 제거
		// --> 부분적 제거 보다 만료 (무효화) 시키는 것이 더 바람직 하다.
		session.invalidate(); // 세션 만료 (무효화)
		
		// redirect 를 이용해서 메인 페이지 재요청
		resp.sendRedirect(req.getContextPath());
	
	}
	
}
