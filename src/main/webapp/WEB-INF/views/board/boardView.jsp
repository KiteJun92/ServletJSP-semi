<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<title>게시판</title>
<link rel="stylesheet" href="${contextPath}/resources/css/board-style.css">

	<jsp:include page="../common/header.jsp"></jsp:include>
	
	<div class="container my-5">

		<div>
			<div id="board-area">

				<!-- Category -->
				<h6 class="mt-4">카테고리 : [ ${board.categoryName} ]</h6>
				
				<!-- Title -->
				<h3 class="mt-4">
					${board.boardTitle}
					<c:if test="${board.boardStatusName =='블라인드'}">
						<strong style=" color: red; font-size:11px">
							관리자에 의해 블라인드 처리된 게시글입니다.
						</strong>
					</c:if>
				</h3>
	
				<!-- Writer -->
				<p class="lead">
					${board.memberName}
				</p>
			
				<hr>

				<!-- Date -->
				<p>
					<span class="board-dateArea">
					
						작성일 : ${board.createDate}
						
						<c:if test="${!empty board.modifyDate}">
							<br>마지막 수정일 : ${board.modifyDate}
						</c:if>
						
					</span>
					
					<%-- 
					1. 자신의 글은 조회해도 조회수 증가 X 
					2. 한 사람이 연속적으로 조회하는 경우 
					   최초 1회 또는 일정 시간마다 1회씩 증가
					--%>
			 		<span class="float-right">조회수 ${board.readCount} </span>
				</p>
				<hr>


					<!-- 이미지 출력 -->
					<div class="form-inline mb-2">
						<label class="input-group-addon mr-3 insert-label">썸네일</label>
						<div class="boardImg thubnail">
							<!-- <img src=""> -->
						</div>
					</div>
	
					<div class="form-inline mb-2">
						<label class="input-group-addon mr-3 insert-label">업로드<br>이미지</label>
						<div class="mr-2 boardImg">
							<!-- <img src=""> -->
						</div>
	
						<div class="mr-2 boardImg">
							<!-- <img src=""> -->
						</div>
	
						<div class="mr-2 boardImg">
							<!-- <img src=""> -->
						</div>
					</div>
				

				<!-- Content -->
				<div id="board-content">
					${board.boardContent}
				</div>
				
	
				<hr>
				 
				
				<div>
					<c:if test="${loginMember.memberNo == board.memberNo}">
						<%-- 로그인된 회원과 해당 글 작성자가 같은 경우에만 수정, 삭제 버튼 노출--%>
						<button id="deleteBtn" class="btn btn-primary float-right mr-2">삭제</button> 
						<button id="updateBtn" class="btn btn-primary float-right mr-2" onclick="updateForm();">수정</button> 
					</c:if>
					
					<a href="list?cp=${param.cp}" class="btn btn-primary float-right mr-2">목록으로</a>
				</div>
				
				
				<%-- 댓글 영역 include 예정 --%>
			</div>



		</div>
	</div>
	<jsp:include page="../common/footer.jsp"></jsp:include>
	
	
</body>
</html>