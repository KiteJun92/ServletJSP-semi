package edu.kh.semi.board.model.vo;

import java.util.List;

public class Board {
	
	private int boardNo; //글번호
	private String boardTitle; // 글제목
	private String boardContent;
	
	private String createDate;
	private String modifyDate;
	
	private int readCount;
	private int memberNo;
	private int boardStatusCode;
	private int categoryCode;
	
	private String memberName; 
	private String categoryName;
	private String boardStatusName;
	
	// 해당 게시글 이미지 목록을 저장할 필드
	private List<BoardImage> imgList;
	
	
	public Board() {
		
	}
	// getter : EL 출력 시 사용됨
	//

	public int getBoardNo() {
		return boardNo;
	}


	public void setBoardNo(int boardNo) {
		this.boardNo = boardNo;
	}


	public String getBoardTitle() {
		return boardTitle;
	}


	public void setBoardTitle(String boardtitle) {
		this.boardTitle = boardtitle;
	}


	public String getBoardContent() {
		return boardContent;
	}


	public void setBoardContent(String boardContent) {
		this.boardContent = boardContent;
	}


	public String getCreateDate() {
		return createDate;
	}


	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}


	public String getModifyDate() {
		return modifyDate;
	}


	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}


	public int getReadCount() {
		return readCount;
	}


	public void setReadCount(int readCount) {
		this.readCount = readCount;
	}


	public int getMemberNo() {
		return memberNo;
	}


	public void setMemberNo(int memberNo) {
		this.memberNo = memberNo;
	}


	public int getBoardStatusCode() {
		return boardStatusCode;
	}


	public void setBoardStatusCode(int boardStatusCode) {
		this.boardStatusCode = boardStatusCode;
	}


	public int getCategoryCode() {
		return categoryCode;
	}


	public void setCategoryCode(int categoryCode) {
		this.categoryCode = categoryCode;
	}


	public String getMemberName() {
		return memberName;
	}


	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}


	public String getCategoryName() {
		return categoryName;
	}


	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}


	public String getBoardStatusName() {
		return boardStatusName;
	}


	public void setBoardStatusName(String boardStatusName) {
		this.boardStatusName = boardStatusName;
	}

	public List<BoardImage> getImgList() {
		return imgList;
	}

	public void setImgList(List<BoardImage> imgList) {
		this.imgList = imgList;
	}

	@Override
	public String toString() {
		return "Board [boardNo=" + boardNo + ", boardTitle=" + boardTitle + ", boardContent=" + boardContent
				+ ", createDate=" + createDate + ", modifyDate=" + modifyDate + ", readCount=" + readCount
				+ ", memberNo=" + memberNo + ", boardStatusCode=" + boardStatusCode + ", categoryCode=" + categoryCode
				+ ", memberName=" + memberName + ", categoryName=" + categoryName + ", boardStatusName="
				+ boardStatusName + ", imgList=" + imgList + "]";
	}
	
	
	
	
}
