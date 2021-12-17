package edu.kh.semi.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.kh.semi.member.model.service.MemberService;
import edu.kh.semi.member.model.vo.Member;
@WebServlet("/member/update")
public class MemberUpdateServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 문자인코딩 처리 -> 필요 없음 -> 왜 ? 필터 처리 되어있음
		
		// 파라미터 얻어오기
		
		String memberEmail = req.getParameter("email");
		String[] phone = req.getParameterValues("phone");
		String memberPhone = String.join("-",phone);
		
		String[] address = req.getParameterValues("address");
		String memberAddress = null;
		if(!address[0].equals("")) { // 우편번호가 비어있지 않은 경우
			memberAddress = String.join(",,", address);
			Member member = new Member();
			member.setMemberEmail(memberEmail);
			member.setMemberPhone(memberPhone);
			member.setMemberAddress(memberAddress);
			// 어떤 회원 정보를 수정할지 구분하기 위한 "회원번호"를 session에 있는 loginmember에서 얻어오기
			// 1) session 객체 얻어오기 
				HttpSession session = req.getSession();
			// 2) loginMember 얻어오기
				Member loginMember = (Member)session.getAttribute("loginMember");
			// 3) 회원 번호 얻어오기
				member.setMemberNo(loginMember.getMemberNo());
				
			try {
				int result = new MemberService().update(member);
				if(result > 0) {
					session.setAttribute("message", "회원 정보가 수정 되었습니다");
					
					// loginMember 변수에는 session에 있는 "loginMember" 객체의 주소가 담겨있음 (얕은복사)
					// -> 복사본을 수정해도 원본이 수정된다
					// -> 이를 이용해서 세션 정보를 최신화 !
					
					loginMember.setMemberEmail(memberEmail);
					loginMember.setMemberPhone(memberPhone);
					loginMember.setMemberAddress(memberAddress);
					
				}else {// 실패
					session.setAttribute("message", "회원 정보 수정 실패");
					
				}
				
				resp.sendRedirect("myPage");
			}catch(Exception e){
				e.printStackTrace();
				String message = "회원정보를 잘못 입력하셨습니다.";
				req.setAttribute("message", message);
				req.setAttribute("e", e);
				req.getRequestDispatcher("/WEB-INF/views/common/error.jsp").forward(req, resp);
			}
		}
	}
}
