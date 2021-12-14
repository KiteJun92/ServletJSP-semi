package edu.kh.semi.board.model.service;
import static edu.kh.semi.common.JDBCTemplate.*;

import java.sql.Connection;
import java.util.List;

import edu.kh.semi.board.model.dao.BoardDAO;
import edu.kh.semi.board.model.vo.Board;
import edu.kh.semi.board.model.vo.Pagination;
public class BoardService {
	private BoardDAO dao = new BoardDAO();

	/** 페이징 처리용 객체 생성
	 * @param cp
	 * @return pagination
	 * @throws Exception
	 */
	public Pagination getPagination(int cp)throws Exception {
		// 전체 게시글 수 필요 -> Connection 얻어오기 DBCP 에서~
		Connection conn = getConnection();
		// 전체 게시글 조회 DAO 호출
		int listCount =  dao.getListCount(conn);
		
			
		close(conn);
		
		
		
		return new Pagination(listCount,cp);
	}
	/** 게시글 목록 조회
	 * @param pagination
	 * @return boardList
	 * @throws Exception
	 */

	/** 게시글 목록 조회
	 * @param pagination
	 * @return boardList;
	 * @throws Exception
	 */
	public List<Board> selectBoardList(Pagination pagination)throws Exception {
		Connection conn = getConnection();
		List<Board> boardList = dao.selectBoardList(pagination , conn);
		
		close(conn);
		return boardList;
	}

	
	
}
