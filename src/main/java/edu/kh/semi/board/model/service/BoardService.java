package edu.kh.semi.board.model.service;
import static edu.kh.semi.common.JDBCTemplate.*;

import java.sql.Connection;
import java.util.List;

import edu.kh.semi.board.model.dao.BoardDAO;
import edu.kh.semi.board.model.vo.Board;
import edu.kh.semi.board.model.vo.BoardImage;
import edu.kh.semi.board.model.vo.Category;
import edu.kh.semi.board.model.vo.Pagination;
import edu.kh.semi.common.XSS;
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
	 * @return boardList;
	 * @throws Exception
	 */
	public List<Board> selectBoardList(Pagination pagination)throws Exception {
		Connection conn = getConnection();
		List<Board> boardList = dao.selectBoardList(pagination , conn);
		
		// 조회된 게시글 목록에서 번호를 얻어와
		// 해당 게시글 번호의 모든 이미지를 조회
		// 게시글 객체에 모든 이미지 정보를 추가
		for(Board temp : boardList) {
			
			List<BoardImage> imgList = dao.selectBoardImageList(temp.getBoardNo(), conn);
			
			// temp(조회된 게시글 정보를 담은 Board VO)에 imgList 세팅
			temp.setImgList(imgList);
			
		}
		
		close(conn);
		return boardList;
	}
	
	
	

	/**게시글 상세 조회
	 * @param boardNo
	 * @param memberNo 
	 * @return board(없으면 null)
	 * @throws Exception
	 */
	public Board selectBoard(int boardNo, int memberNo)throws Exception {
		
		Connection conn = getConnection();
		
		// 게시글 조회
		Board board = dao.selectBoard(boardNo,conn);
		

		// 게시글에 해당되는 이미지 정보를 조회
		List<BoardImage> imgList = dao.selectBoardImageList(boardNo, conn);
		
		// 조회된 이미지 목록을 board에 추가
		board.setImgList(imgList);
		
		
		// 조회된 게시글이 있고 , 해당 게시글의 작성자와 로그인된 회원이 같지 않으면
		// 조회수 증가
		if(board != null && board.getMemberNo() != memberNo) {
			
			int result = dao.increaseReadCount(boardNo,conn);
			if(result > 0 ) {
				commit(conn);
				// 먼저 조회되었던 게시글 정보에서 조회수를 1 증가
				board.setReadCount(board.getReadCount()+1);
			}
			else			rollback(conn);
			
		}
		close(conn);
		
		return board;
	}

	/** 카테고리 조회
	 * @return category
	 * @throws Exception
	 */
	public List<Category> selectCategory()throws Exception {
		Connection conn = getConnection();
		List<Category> category = dao.selectCategory(conn);
		close(conn);
		
		return category;
	}

	
	/** 게시글 삽입 + 이미지 정보 삽입
	 * @param board
	 * @param imgList
	 * @return result (1 성공, 0 실패)
	 * @throws Exception
	 */
	public int insertBoard(Board board, List<BoardImage> imgList) throws Exception{
		
		Connection conn = getConnection();
		
		// 1. 다음 게시글 번호 얻어오기
		// - 게시글 -> 이미지 정보 DB 삽입 과정 중에
		//   다른 DB INSERT 요청이 끼어들게 되어
		//   시퀀스 번호가 불일치하는 현상이 발생할 수 있다
		// -> 번호표를 미리 뽑아 놓듯이 다음 시퀀스 번호를 미리 얻어둠.
		int boardNo = dao.nextBoardNo(conn);
		
		// 조회된 다음 글 번호를 board에 세팅(DB 삽입 시 사용)
		board.setBoardNo(boardNo);
		
		// 2. 게시글 삽입 - SEQ_BOARD_NO.NEXTVAL (100)
		
		// 2-1) XSS 방지 처리
		board.setBoardTitle(XSS.replaceParameter( board.getBoardTitle() ));
		board.setBoardContent(XSS.replaceParameter( board.getBoardContent() ));
		
		// 2-2) 개행 문자 -> <br> 태그로 변경
		// -> 정규 표현식 이용
		String content = board.getBoardContent().replaceAll("(\r\n|\r|\n|\n\r)", "<br>");
		board.setBoardContent(content);
		
		int result = dao.insertBoard(board, conn);
		
		
		if(result > 0) {
			
			// 3. (이미지가 있을 경우) 이미지 삽입 - SEQ_BOARD_NO.CURRVAL
			for(BoardImage img : imgList) {
				// + BoardImage에 어떤 게시글의 이미지인지 알려주는 게시글 번호 추가
				img.setBoardNo(boardNo);
				
				result = dao.insertBoardImage(img,conn);
				
				if(result == 0) {
				
					rollback(conn);
					// Service의 메서드는 하나의 트랜잭션을 공유한다.
					// -> 이미지 정보 삽입 전에 삽입된 게시글 정보도 트랜잭션에 담겨있다.
					break;
					
				}
				
				// Board, BoardImage 모두 삽입 성공된 경우
				if(result > 0) {
					commit(conn);
					result = boardNo;
					// 성공하면 result가 0보다 크므로 거기에 boardNo를 담아서 다용도로 사용한다.
					// boardNo도 무조건 0보다 큰 값이 나오므로 사용
				}
				else rollback(conn);
				
				// 글 작성 성공 -> 현재 작성한 글 상세 조회
				// -> 상세 조회 하려면 boardNo가 필요하다
				// -> 삽입 시 사용되던 boardNo가 존재
				// -> boardNo를 controller로 반환하여
				// 상세 조회 기능을 재요청 -> 상세 조회로 이동된다.
				
			}

		}else {
			rollback(conn);
		}
		
		
		return result;
	}


	/** 게시글 수정 화면 전환
	 * @param boardNo
	 * @return board
	 * @throws Exception
	 */
	public Board updateView(int boardNo) throws Exception{
		
		Connection conn = getConnection();
		
		Board board = dao.selectBoard(boardNo, conn);
					// 상세 조회 때 만들어둔 게시글 조회
		
		// 게시글에 해당되는 이미지 정보를 조회
		List<BoardImage> imgList = dao.selectBoardImageList(boardNo, conn);
		
		// 조회된 이미지 목록을 board에 추가
		board.setImgList(imgList);
		
		// + 현재 DB에는 줄 바꿈을 <br> 형태로 저장하고 있음.
		// -> <br>을 \r\n 형태로 돌려놓아야함
		
		board.setBoardContent(board.getBoardContent().replaceAll("<br>", "\r\n"));
		
		close(conn);
		
		
		return board;
	}


	/** 게시글 수정
	 * @param board
	 * @param imgList
	 * @return result
	 * @throws Exception
	 */
	public int updateBoard(Board board, List<BoardImage> imgList) throws Exception{
		
		Connection conn = getConnection();
		
		// 1. XSS 방지 처리하기, 개행 문자 변경 처리하기
		String boardTitle = XSS.replaceParameter(board.getBoardTitle());
		String boardContent = XSS.replaceParameter(board.getBoardContent());
		
		boardContent = boardContent.replaceAll("(\r\n|\r|\n|\n\r)", "<br>");
		
		board.setBoardTitle(boardTitle);
		board.setBoardContent(boardContent);
		
		
		// 2. 게시글 부분 수정
		int result = dao.updateBoard(board, conn);
		
		if(result > 0) { // 게시글 수정이 이루어진 경우
			
			// 3. 이미지 부분 수정하기
			// -> 반복문을 이용하여 새로 업로드 된 이미지 정보를 
			//    기존 BOARD_IMG 테이블에 저장된 행에 UPDATE 진행

			// -> 만약 기존 데이터가 없으면 INSERT
			
			for(BoardImage img : imgList) {
				
				result = dao.updateBoardImage(img, conn);
				// 기존 데이터를 수정 성공 			-> result == 1
				// 기존 데이터가 없어서 수정 실패 	-> result == 0 
				
				if(result == 0) {
					
					result = dao.insertBoardImage(img, conn);
					
				}
				
			}	// end for
			
			if(result > 0) commit(conn);
			else rollback(conn);
			
			
		}else {		// 게시글 수정에 실패한 경우
			rollback(conn);
		}
		
		close(conn);
		
		return result;
	}

	
	
}
