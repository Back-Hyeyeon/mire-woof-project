<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<script type="text/javascript">
	var currUsername = '${pageContext.request.userPrincipal.principal.account.username}';
	var itemno = '${item.itemNo}';
	
	$(document).ready(function() {
		reviewList('1');
		pagination('1');
	});

	function modifyBtn() {
		var reviewNo = event.target.value;
		alert(reviewNo);
		
	}
	
	
	function deleteBtn(){
		var reviewNo = event.target.value;
		var check = confirm("정말로 삭제?");
		if (check) {
			deleteReview(reviewNo);
			reviewList('1');
		}
	}
	
	function pageBtn(){
		 var idx = event.target.value;
		 reviewList(idx);
	   pagination(idx);
	   if(idx!=0){
	   window.location.href = "#firstReview";		   
	   }
	}
	
	function deleteReview(reviewNo){
		var review = {
				reviewNo : reviewNo
		};
		
		$.ajax({
					type : "put",
					url : "/review/deleteReviewAjax",
					data : JSON.stringify(review),
					contentType : "application/json; charset=UTF-8",
					success : 
						function(result) {
				}
		});
	}
	
	function pagination(idx){
		var pageRequest = {
				page : idx,
				itemNo : itemno
		};
		$.ajax({
					type : "put",
					url : "/review/getItemReviewPagination",
					data : JSON.stringify(pageRequest),
					contentType : "application/json; charset=UTF-8",
					success : 
						function(result) {
								let pageList = ""
								pageList += '<ul class="pagination m-auto">';
								if(result[0].prev){
									var startPage = result[0].startPage-1;
									pageList += '<li class="page-item"><button id="pageBtn" value="'+startPage+'"class="page-link" onclick="pageBtn()">Previous</button></li>';
								}							
								for (var idx = result[0].startPage; idx < result[0].endPage+1; idx++) {
									pageList += '<li class="page-item"><button id="pageBtn" value="'+idx+'" class="page-link" onclick="pageBtn()">'+idx+'</button></li>';
								}
								if(result[0].next&&result[0].endPage>0){
									var endpage = result[0].endPage+1;
									pageList += '<li class="page-item"><button id="pageBtn" value="'+endpage+'" class="page-link" onclick="pageBtn()">Next</a></li>';
								}
								pageList += '</ul>';
							$("#pageListSpan").html(pageList);
				}
			});
		}
	
	 function reviewList(idx){
		var pageRequest = {
				page : idx,
				itemNo : itemno
		};
		
		$.ajax({
				type : "put",
				url : "/review/getItemReviewList",
				data : JSON.stringify(pageRequest),
				contentType : "application/json; charset=UTF-8",
				
				success : 
					function(result) {
						let reviewList = ""
						var parsedDate = null;
						var formattedDate = null; 
						for (var i = 0; i < result.length; i++) {
							parsedDate = new Date(result[i].reviewRegDate);
							formattedDate = parsedDate.toLocaleString({ timeZone: 'UTC' });
							reviewList += '<br><form">';
							reviewList += '<input type="hidden" name="reviewNo" value="'+result[i].reviewNo+'"readonly="readonly"> ';
							reviewList += '  <div class="w-100">';
							reviewList += ' <div class="row p-0 m-0">';
							reviewList += '  <span class="col-3">'+result[i].username+'</span>';
							reviewList += '   <span class="col-3">'+formattedDate+'</span>';
							reviewList += '   <span class="col-3">'+result[i].itemName+'</span>';
							reviewList += '  </div>';
							reviewList += '  <div>';
							reviewList += '  <img src="/review/getReviewPic?reviewNo='+result[i].reviewNo+'" style="height:100px">';
							reviewList += ' </div>';
							reviewList += ' <div>';
							reviewList += ' <p class="fs-4">'+result[i].reviewTitle+'</p>';
							reviewList += ' <div class="d-flex">';
							reviewList += ' <textarea class="form-control auto-height-textarea overflow-hidden" readonly="readonly">'+result[i].reviewDesc+'</textarea>';
							reviewList += '<sec:authorize access="hasRole('ROLE_ADMIN')">';
							reviewList += '<button class="btn btn-outline-dark p-0" value="'+result[i].reviewNo+'" onclick="deleteBtn()">삭제</button>';
							reviewList += '</sec:authorize>';
							/* alert("currUsername : "+currUsername+" result[i].userName) : "+result[i].userName); */
							if(currUsername==result[i].username){
								reviewList += '<button class="btn btn-outline-dark p-0" value="'+result[i].reviewNo+'" onclick="deleteBtn()">삭제</button>';
							}
							reviewList += ' </div>';
							reviewList += ' </div>';
							reviewList += '</div>';
							reviewList += '<div align="center" class="border-0">';
							reviewList += '<div class="d-flex">';
							reviewList += '</div></div></form>';
						}
						 $("#reviewListSpan").html(reviewList);  
				}
		});
	} 
	
</script>
<style>
        /* 텍스트 영역에 대한 스타일 */
        .auto-height-textarea {
            resize: none; /* 크기 조절을 막음 (선택사항) */
        }
</style>

		<div class="mt-3 w-75 m-auto">
			<h4 id="firstReview" class="bg-success-subtle">상품평보기</h4>
			<div id="reviewListSpan"></div>
			<div id="pageListSpan" class="d-flex" style="margin-top: 5px"></div>
		</div>
