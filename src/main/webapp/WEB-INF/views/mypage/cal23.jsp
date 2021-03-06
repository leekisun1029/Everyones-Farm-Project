<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>



<!-- 합쳐지고 최소화된 최신 CSS -->
<link rel="stylesheet"
   href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
<style type="text/css">
.input-group {
   margin-top: 30px;
}

div {
   text-align: center;
}

.col-lg-1 {
   width: 50%;
   margin-left: 150px;
}

span {
   padding: 0;
}

.input-group {
   width: 400px;
}

label {
   padding-left: 0;
   text-align: left;
}

.btn-warning {
   width: 90px;
   height: 50px;
   margin-top: 30px;
   margin-right: 150px;
}

.media-object {
   margin-left: 70px;
}

.panel-default {
   border: none;
}

.panel-default>.panel-heading {
   background-color: #D1E9CA;
   border: none;
   font-weight: bold;
   font-size: 17px;
}

a {
   text-decoration: none;
   color: black;
}

a:hover {
   text-decoration: none;
}

#sample6_postcode {
   height: 52px;
}

#user_profile {
   display: none;
}

#modify-fileuplod {
   text-align: left;
}
img{
   height : 200px;
   width : 200px;
}
#second_addr{
   height : 80px;
}
.form-inlines{
   border : none;
}
</style>

<script
   src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
   function sample6_execDaumPostcode() {
      new daum.Postcode(
            {
               oncomplete : function(data) {
                  // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                  // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                  // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                  var addr = ''; // 주소 변수
                  var extraAddr = ''; // 참고항목 변수

                  //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                  if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                     addr = data.roadAddress;
                  } else { // 사용자가 지번 주소를 선택했을 경우(J)
                     addr = data.jibunAddress;
                  }

                  // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
                  if (data.userSelectedType === 'R') {
                     // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                     // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                     if (data.bname !== ''
                           && /[동|로|가]$/g.test(data.bname)) {
                        extraAddr += data.bname;
                     }
                     // 건물명이 있고, 공동주택일 경우 추가한다.
                     if (data.buildingName !== ''
                           && data.apartment === 'Y') {
                        extraAddr += (extraAddr !== '' ? ', '
                              + data.buildingName : data.buildingName);
                     }
                     // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                     if (extraAddr !== '') {
                        extraAddr = ' (' + extraAddr + ')';
                     }
                     // 조합된 참고항목을 해당 필드에 넣는다.
                     document.getElementById("first_addr").value = extraAddr;

                  } else {
                     document.getElementById("first_addr").value = '';
                  }

                  // 우편번호와 주소 정보를 해당 필드에 넣는다.
                  document.getElementById('sample6_postcode').value = data.zonecode;
                  document.getElementById("first_addr").value = addr;
                  // 커서를 상세주소 필드로 이동한다.
                  
                  onclose('COMPLETE_CLOSE');

               }
            }).open();

   }

   function onclose() {
      new daum.Postcode({
         onclose : function(state) {
            //state는 우편번호 찾기 화면이 어떻게 닫혔는지에 대한 상태 변수 이며, 상세 설명은 아래 목록에서 확인하실 수 있습니다.
            if (state === 'FORCE_CLOSE') {
               //사용자가 브라우저 닫기 버튼을 통해 팝업창을 닫았을 경우, 실행될 코드를 작성하는 부분입니다.

            } else if (state === 'COMPLETE_CLOSE') {
               document.getElementById("second_addr").focus();
            }
         }
      });
   }
</script>




<!-- 네비바를 fiexd-top으로 설정했을 때 컨텐츠와 겹치는 문제 방지 -->
<body class="pt-5">
   <!-- Page Content -->
   <div class="container">
      <div class="row">
         <div class="col-lg-3">
            <h3 class="my-4 text-left">회원 정보 수정</h3>
            <hr>
            <div class="panel panel-default">
               <div class="panel-heading">
                  <a href="<%=request.getContextPath()%>/mypage/user/modify">회원</a>
               </div>
               <div class="panel-body">
                  <a href="<%=request.getContextPath()%>/mypage/user/modify" style="font-weight: bold;">회원정보
                     수정</a>
               </div>
               <div class="panel-body">
                  <a href="<%=request.getContextPath()%>/mypage/user/deleteId">회원
                     탈퇴</a>
               </div>
               <div class="panel-body">
                  <a href="<%=request.getContextPath()%>/mypage/user/mypageO3List">1대
                     1 문의</a>
               </div>
               <div class="panel-body">
                  <a href="<%=request.getContextPath()%>/mypage/user/myActive">활동
                     신청 현황</a>
               </div>
            </div>

            <div class="panel panel-default">
               <div class="panel-heading">
                  <a href="<%=request.getContextPath()%>/mypage/user/orderList">주문</a>
               </div>
               <div class="panel-body">
                  <a href="<%=request.getContextPath()%>/mypage/user/orderList">구매
                     목록</a>
               </div>
               <div class="panel-body">
                  <a href="<%=request.getContextPath()%>/mypage/user/basket">장바구니</a>
               </div>
            </div>

         </div>
         <div class="col-lg-1">
            
            
            <form action="modifyImg" method="post">
               <div class="media">
                  <div class="preview" id="test">
                  <img alt="" src="">
                  </div>
                  <input type="file" id="upload" name="upload" />


                  <button type="submit" class="btn btn-warning">사진 등록</button>
               </div>
            </form>
            <div class="ModifyUserInfo">

               <form action="<%=request.getContextPath()%>/modify/userInfo" method="post">
                  <!--          아이디 -->
                  <div class="input-group">
                     <label for="inputName" class="col-lg-3 control-label">아이디</label>
                     <div class="input-group input-group-lg">
                        <span class="input-group-addon glyphicon glyphicon-user"
                           id="sizing-addon1"></span> <input type="text"
                           class="form-control" placeholder="아이디" name="userId"
                           aria-describedby="sizing-addon1" value="${userData.userId}" readonly>
                     </div>
                  </div>
                  <!--          비밀번호 -->
                  <div class="input-group">
                     <label for="inputName" class="col-lg-3 control-label">비밀번호</label>
                     <div class="input-group input-group-lg">
                        <span class="input-group-addon glyphicon glyphicon-lock"
                           id="sizing-addon1"></span> <input type="password"
                           class="form-control" placeholder="비밀번호" name="userPw" id="userPw"
                           aria-describedby="sizing-addon1">
                     </div>
                  </div>
                  <div class="input-group">
                     <label for="inputName" class="col-lg-3 control-label">비밀번호</label>
                     <div class="input-group input-group-lg">
                        <span class="input-group-addon glyphicon glyphicon-lock"
                           id="sizing-addon1"></span> <input type="password"
                           class="form-control" placeholder="비밀번호" name="pwcheck" id="pwcheck"
                           aria-describedby="sizing-addon1">
                     </div>
                  </div>
                  <!--          이름 -->
                  <div class="input-group">
                     <label for="inputName" class="col-lg-3 control-label">이름</label>
                     <div class="input-group input-group-lg">
                        <span class="input-group-addon glyphicon glyphicon-user"
                           id="sizing-addon1"></span> <input type="text"
                           class="form-control" placeholder="이름" name="userName"
                           aria-describedby="sizing-addon1" value="${userData.userName}"
                           readonly>
                     </div>
                  </div>

                  <!--          이메일 -->
                  <div class="input-group">
                     <label for="inputName" class="col-lg-3 control-label">E-Mail</label>
                     <div class="input-group input-group-lg">
                        <span class="input-group-addon glyphicon glyphicon-envelope"
                           id="sizing-addon1"></span> <input type="email"
                           class="form-control" placeholder="E-Mail" name="email"id="email"
                           aria-describedby="sizing-addon1" value="${userData.email}">
                     </div>
                  </div>
                  <!--          전화번호 -->
                  <div class="input-group">
                     <label for="inputName" class="col-lg-3 control-label">전화번호</label>
                     <div class="input-group input-group-lg">
                        <span class="input-group-addon glyphicon glyphicon-earphone"
                           id="sizing-addon1"></span> <input type="text"
                           class="form-control" placeholder="전화번호" name="phone"
                           aria-describedby="sizing-addon1" value="${userData.phone}">
                     </div>
                  </div>


                  <!-- 주소 -->
                  <div class="input-group">
                     <label for="inputName" class="col-lg-3 control-label">우편번호</label>
                     <div class=" input-group-lg col-xs-2 input-group">
                        <span class="input-group-addon glyphicon glyphicon-home"
                           id="sizing-addon1"></span> <input type="text" name="zoneCode"
                           id="sample6_postcode"  class="form-control" value="${userData.zoneCode }">
                        <span class="input-group-addon" id="sizing-addon1"><input
                           type="button" onclick="sample6_execDaumPostcode()"value="우편번호"
                           class="half-size back-pink float-right focus form-inlines"></span>
                     </div>
                  </div>
                  <div class="input-group">
                     <label for="inputName" class="col-lg-3 control-label">주소</label>
                     <div class="input-group input-group-lg">
                        <span class="input-group-addon glyphicon glyphicon-home"
                           id="sizing-addon1"></span> <input type="text" id="first_addr"
                           name="firstAddress"
                           class="half-size float-left focus form-control"
                           value="${userData.firstAddress}">
                     </div>
                  </div>
                  <div class="input-group">
                     <label for="inputName" class="col-lg-3 control-label">상세주소</label>
                     <div class="input-group input-group-lg">
                        <span class="input-group-addon glyphicon glyphicon-home"
                           id="sizing-addon1"></span> <input type="text" id="secondAddress"
                           name="secondAddress"
                           class="half-size float-left focus form-control"
                           value="${userData.secondAddress}">
                     </div>
                  </div>



                  <button type="submit" class="btn btn-warning" id="duple">등록</button>

               </form>
            </div>
         </div>
      </div>
   </div>

   <script type="text/javascript">
      function selectUrl(url) {
         var root = '<%=request.getContextPath()%>';

         document.querySelector('#form-data').action = root + url;
      }
   </script>







</body>
   <script>

    var upload = document.querySelector('#upload');
    var preview = document.querySelector('#preview');
    /* fileReader 객체 생성 */
    var reader = new FileReader();

    /* reader 시작시 함수 구현 */
    reader.onload = (function(){
       
       
       this.image = document.createElement('img');
       var vm = this;
          
       return function(e){
          vm.image.src = e.target.result
       }
          
       })();
    
    upload.addEventListener('change',function(e){
    var get_file = e.target.files;
    
   if(get_file){
       reader.readAsDataURL(get_file[0]);
   }    
    
   preview.appendChild(image);
    
   
    
 })
</script>


