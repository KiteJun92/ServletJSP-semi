package edu.kh.semi.board.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import edu.kh.semi.board.model.service.ReplyService;
import edu.kh.semi.board.model.vo.Reply;


// *** Controller ***
// 요청에 따라 알맞은 서비스를 호출하고
// 요청 처리 결과를 보여줄 view를 선택해 응답을 제어한다.

@WebServlet("/reply/*")
public class ReplyController extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		//데이터 전달 방식 저장용 변수
		String method = req.getMethod();
		
		// 요청 주소 뒷 부분을 잘라내어 구분 방법 만들기
		
		String uri = req.getRequestURI();
		
		String contextPath = req.getContextPath();
		
		String command = uri.substring( (contextPath + "/reply/").length());
		// -> 요청 주소에서 /semi/board/ 의 길이만큼 잘라낸 후
		// 	  나머지 문자열을 command 변수에 저장
		
		
		ReplyService service = new ReplyService();
		
		
		try {
			
			
			// 댓글 조회 기능
			if(command.equals("select")) {
				
				// 파라미터 얻어오기
				int boardNo = Integer.parseInt(req.getParameter("boardNo"));
				
				List<Reply> rList = service.selectReplyList(boardNo);
				
				// ajax 비동기 통신 시
				// 연결된 스트림을 이용하여 값(JSON 형태)만을 내보낸다.
				
				// JSONSimple, GSON 둘 중 하나 사용
				new Gson().toJson(rList, resp.getWriter());
				
			}
			
			
			
			// 댓글 생성(삽입) 기능
			else if(command.equals("insert")) {
				
				// 파라미터를 얻어와 Reply VO에 담아서 Service 호출 후 결과 반환 받기
				
				int memberNo = Integer.parseInt(req.getParameter("memberNo"));
				int boardNo = Integer.parseInt(req.getParameter("boardNo"));
				String replyContent = req.getParameter("replyContent");
				
				
				Reply reply = new Reply();
				
				reply.setMemberNo(memberNo);
				reply.setBoardNo(boardNo);
				reply.setReplyContent(replyContent);
				
				int result = service.insertReply(reply);
				
				resp.getWriter().print(result);
				
			}
			
			
			
			// 댓글 수정 기능
			else if(command.equals("update")) {
				
				int replyNo = Integer.parseInt(req.getParameter("replyNo"));
				String replyContent = req.getParameter("replyContent");
				
				resp.getWriter().print( service.updateReply(replyNo, replyContent) );				
				
			}
			
			
			
			else if(command.equals("delete")) {
				
				int replyNo = Integer.parseInt(req.getParameter("replyNo"));
				
				resp.getWriter().print( service.deleteReply(replyNo) );
				
			}
			
			
			
		}catch(Exception e) {
			e.printStackTrace();
			
			// ajax error 속성 활용을 위해 500에러를 응답으로 전달
			resp.setStatus(500);
			resp.getWriter().print(e.getMessage());
			
		}
		
	}
	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req,resp);
	}
	
}
