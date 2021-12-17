<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%-- 
   JSTL - c  : 변수 선언, if, for 같은 주요기능을 담당
   JSTL - fn : 문자열 관련 기능 담당
          -> JSTL 같은 태그 형식이 아닌 EL 형식으로 작성한다.
 --%>

   <jsp:include page="../common/header.jsp"></jsp:include>
   
   
   <div class="container"  style="min-height:700px">
      
      <div class="row my-5">
      
         <jsp:include page="sideMenu.jsp"></jsp:include>      
         
         <div class="col-sm-8">
         
         
         <%-- memberAddress를 ",," 기준으로 나누고 그것을 배열로 저장하는 변수를 선언 --%>
         <%-- ${fn:split(문자열, 구분자) } : 문자열에서 구분자를 기준으로 나눠 배열로 반환 --%>
         
         <c:set var="addr" value="${fn:split(loginMember.memberAddress, ',,') }"/>


         <!-- memberPhone을 "-" 기준으로 쪼갠 배열을 저장하는 변수 선언 -->
         <c:set var="ph" value="${fn:split(loginMember.memberPhone, '-')}"></c:set>
         
         
            <h3>My Page</h3>
            <hr>
            <div class="bg-white rounded shadow-sm container p-3">
               <form method="POST" action="update" onsubmit="return memberUpdateValidate();" class="form-horizontal" role="form">
                  <!-- 아이디 -->
                  <div class="row mb-3 form-row">
                     <div class="col-md-3">
                        <h6>아이디</h6>
                     </div>
                     <div class="col-md-6">
                        <h5 id="id"> ${sessionScope.loginMember.memberId} </h5>
                     </div>
                  </div>
   
                  <!-- 이름 -->
                  <div class="row mb-3 form-row">
                     <div class="col-md-3">
                        <h6>이름</h6>
                     </div>
                     <div class="col-md-6">
                        <h5 id="name"> ${loginMember.memberName} </h5>
                     </div>
                  </div>
   
                  <!-- 전화번호 -->
                  <div class="row mb-3 form-row">
                     <div class="col-md-3">
                        <label for="phone1">전화번호</label>
                     </div>
                     <!-- 전화번호1 -->
                     <div class="col-md-3">
                        <select class="custom-select" id="phone1" name="phone">
                           <option>010</option>
                           <option>011</option>
                           <option>016</option>
                           <option>017</option>
                           <option>019</option>
                        </select>
                     </div>
                     
   
                     <!-- 전화번호2 -->
                     <div class="col-md-3">
                        <input type="number" class="form-control phone" id="phone2" name="phone" value="${ph[1]}">
                     </div>
   
                     <!-- 전화번호3 -->
                     <div class="col-md-3">
                        <input type="number" class="form-control phone" id="phone3" name="phone" value="${ph[2]}">
                     </div>
                  </div>
   
                  <!-- 이메일 -->
                  <div class="row mb-3 form-row">
                     <div class="col-md-3">
                        <label for="memberEmail">Email</label>
                     </div>
                     <div class="col-md-6">
                        <input type="email" class="form-control" id="email" name="email" value="${loginMember.memberEmail} ">
                     </div>
                  </div>
                  <br>
   
                  <!-- 주소 -->
                  <!-- 오픈소스 도로명 주소 API -->
                  <!-- https://www.poesis.org/postcodify/ -->
                  <div class="row mb-3 form-row">
                     <div class="col-md-3">
                        <label for="postcodify_search_button">우편번호</label>
                     </div>
                     <div class="col-md-3">
                        <input type="text" name="address" class="form-control postcodify_postcode5" value="${addr[0]}">
                     </div>
                     <div class="col-md-3">
                        <button type="button" class="btn btn-primary" id="postcodify_search_button">검색</button>
                     </div>
                  </div>
   
                  <div class="row mb-3 form-row">
                     <div class="col-md-3">
                        <label for="address1">도로명 주소</label>
                     </div>
                     <div class="col-md-9">
                        <input type="text" class="form-control postcodify_address" name="address" id="address1"  value="${addr[1]}">
                     </div>
                  </div>
   
                  <div class="row mb-3 form-row">
                     <div class="col-md-3">
                        <label for="address2">상세주소</label>
                     </div>
                     <div class="col-md-9">
                        <input type="text" class="form-control postcodify_details" name="address" id="address2"  value="${addr[2]}">
                     </div>
                  </div>
   
                  <hr class="mb-4">
                  <button class="btn btn-primary btn-lg btn-block" type="submit">수정</button>
               </form>
            </div>
         </div>
      </div>
   </div>
   <br><br>
   
   <jsp:include page="../common/footer.jsp"></jsp:include>
      
      
   <!-- 오픈소스 도로명 주소 검색 API -->
   <!-- https://www.poesis.org/postcodify/ -->
   <!-- postcodify 라이브러리를 CDN 방식으로 추가. -->
   <script src="//d1p7wdleee1q2z.cloudfront.net/post/search.min.js"></script>
   <script>
      // 검색 단추를 누르면 팝업 레이어가 열리도록 설정한다.
      $(function () {
         $("#postcodify_search_button").postcodifyPopUp();
      });
   </script>
   
   <!-- 내 정보 조회/수정 관련 script -->
   <script>
      // JSP 파일 - html, css, js, jquery, el, jstl
      // JS 파일  - js, jquery      
      $(function(){
         
         const ph0 = "${ph[0]}"; // 문자열
         
         // ******************************************************* 
         // JS 코드에 EL을 작성하는 경우
         // 문자열을 나타내는 ("")로 감싸주지 않으면
         // 문자가 숫자로 인식되거나
         // 올바르지 않은 JS 표기법,
         // 변수에 값을 대입하지 않는 등의 문제가 발생할 수 있다.
         
         // 그러므로 반드시 JS에 EL 사용 시
         // "" 기호를 넣어야한다.
         // ******************************************************* 
         
         // jQuery 향상된 for문으로 option 요소에 접근하기
         $("#phone1 > option").each(function(index,item){
            
            // console.log( item.innerText );
            
            // 현재 접근한 요소에 작성된 문자열과 ph0에 저장된 번호 앞자리가 같으면
            // selected 속성을 추가한다.
            if(ph0 == item.innerText){
               item.setAttribute("selected", true);
            }
            
         });
         
         
         
      });
   </script>
  <script src ="${contextPath}/resources/js/memberUpdate.js"></script>;
   
</body>
</html>