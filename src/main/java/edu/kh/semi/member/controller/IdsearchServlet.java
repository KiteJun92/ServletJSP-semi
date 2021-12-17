package edu.kh.semi.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.google.gson.Gson;

import edu.kh.semi.member.model.service.MemberService;
import edu.kh.semi.member.model.vo.Member;
@WebServlet("/member/idSearch")
public class IdsearchServlet extends HttpServlet {
		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			String inputId = req.getParameter("inputId");
			try {
				// DB에서 아이디가 일치하는 회원 정보를 조회하는 Service 호출 후 결과 반환받기
				 Member member =new MemberService().idSearch(inputId); // 객체 생성 후 바로 메소드를 수행
				
				// resp.getWriter().print(member);
				// System.out.println(member);
				// -? member 객체가 아닌 member.toString() 문자열이 출력됨
				 // -> 문자열은 자바스크립트에서 객체가 아니기 때문에 객체로 취급할 수 없다.ㅈ
				 String temp = "";
				 //temp += "memberId : '" + member.getMemberId() + "',";
				 
				 // JSON 형태를 만드는 방법 2 : json.simple.jar
				 // JSONObject : 데이터 -> JSON , JSON -> 데이터 바꿀 수 있게 하는 객체
				 //				Map 형식 (K : V) 형태로 데이터를 바꿈
				 
					/*
					 * JSONObject jsonObj = null; if(member != null) { // DB에서 조회된 결과가 있을 때만
					 * JSONObject 생성
					 * 
					 * jsonObj = new JSONObject(); jsonObj.put("memberId" , member.getMemberId());
					 * jsonObj.put("memberName" , member.getMemberName()); jsonObj.put("memberPhone"
					 * , member.getMemberPhone()); jsonObj.put("memberEmail" ,
					 * member.getMemberEmail()); jsonObj.put("memberAddress" ,
					 * member.getMemberAddress()); System.out.println(jsonObj); }
					 * resp.getWriter().print(jsonObj);
					 */
				 // JSON 형태를 만드는 방법 3 : GSON 사용
				 new Gson().toJson(member, resp.getWriter());
				 
			}catch(Exception e) {
				e.printStackTrace();
				resp.setStatus(500);
				resp.getWriter().print(e.getMessage());
			}
			
		}
	
}
