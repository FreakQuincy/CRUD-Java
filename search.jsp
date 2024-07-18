<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../global/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="shortcut icon" type="image/x-icon" href="<c:url value='/img/wexLinx/wexlinx-icon.ico'/>" />
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css">
	<link href="<c:url value='/css/pagination.css'/>" rel="stylesheet" type="text/css">
	
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Manage Ownership</title>
	
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
	<script src="https://code.iconify.design/iconify-icon/1.0.7/iconify-icon.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js" charset="utf-8"></script>
	
	<!-- JS for pagination only -->
	<script src="<c:url value='/js/pagination.js'/>"></script>
	
	<!-- Flatpicker -->
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
	<link rel="stylesheet" type="text/css" href="https://npmcdn.com/flatpickr/dist/themes/material_blue.css">
	<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
	
	<!-- This is required to link to the Dashboard -->
	<link href="<c:url value='/css/dashboard.css'/>" rel="stylesheet" type="text/css">
	<script src="<c:url value="/js/dashboard.js"/>"></script>
	<script src="<c:url value="/js/sweetalert2.min.all.js"/>"></script>
	<style>
		.search-table-list thead th:first-child {
		    padding-left: 20px;
		    width: 200px;
		}
		.search-table-list tbody td:first-child {
			padding-left: 20px;
		}
		.status__td__cotainer .dot__icon {
			font-size: 24px;
		}
		.__active {
		    background: #ECFDF3;
		    color: #037847;
		    display: flex;
		    width: 85px;
		    justify-content: center;
		    align-items: center;
		    gap: 0;
		    border-radius: 13px;
		    padding: 2px 10px 2px 5px;
		    margin: auto;
		}
		.__terminated {
			background: #E0E0E0;
		    color: #4D4D4D;
		    display: flex;
		    width: 105px;
		    justify-content: center;
		    align-items: center;
		    gap: 0;
		    border-radius: 13px;
		    padding: 2px 10px 2px 5px;
		}
		.status__td__cotainer {
			position: relative;
		}
		.status__td__cotainer span {
			position: absolute;
			top: 50%;
			left: 50%;
			transform: translate(-50%,-50%);
			}
	</style>
</head>
<body>

<!-- Header Section -->
<c:import url="/menu.htm?type=system"/>
     
   <form id="form" class="needs-validation" action="searchOwnership.htm" onkeypress="doSubmit(event)">
   	<input type="hidden" id="searchURL" value="searchOwnership.htm"/>
    <section class="main-container">
    	<!-- Start of <b>inner main</b> class where you can put your form designs inside this container class -->
    	<div class="inner-main animate__animated animate__fadeIn">
    		
    		<div class="header-title mb-4">
                <h3 class="page__title">
                    Manage Ownership
                </h3>
                <p class="page__description">
                    The following is a list of all registered Ownership.
                </p>
            </div>
                
            <div class="data-list-container position-relative">
                <jsp:include page="../global/formMessage.jsp"/>
				<div class="row m-auto pt-4 pb-3 d-flex justify-content-between align-items-center flex-md-row flex-column-reverse searchFormPage">
					<div class="col-md-5 px-0 position-relative">
						<input class="form-control search__input__field" type="text" id="searchOption" name="name" value="${name}" placeholder="Search here"/>
						<iconify-icon class="search__icon" icon="ri:search-line"></iconify-icon>
					</div>
					<div class="col-md-7 pe-0 d-flex justify-content-end manage__action__container mb-md-0 mb-3">
						<button type="button" id="refreshPage" class="btn search__action__button tt" onclick="filterSearch()">
							<iconify-icon class="fs-3" icon="material-symbols:refresh"></iconify-icon>
						</button>
						<sec:authorize access="hasRole('OWNERSHIP_ADD')">
							<button type="button" class="btn search__action__button" onclick="javascript: window.location='newOwnership.htm'">
								<iconify-icon class="fs-3 me-2" icon="heroicons-solid:plus"></iconify-icon>
								<span id="addBtn1">Add Ownership</span>
								<span id="addBtn2">Add</span>
							</button>
						</sec:authorize>
					</div>
				</div>
				
				<c:if test="${isResult == 1}">
					<c:if test="${fn:length(ownershipPageHolder.pageList) == 0}">
						<div class="no-record d-flex justify-content-center">
							<img alt="no-record-found" src="<c:url value="/img/no-record.png"/>">
						</div>
					</c:if>
					
					<c:if test="${fn:length(ownershipPageHolder.pageList) > 0}">
						<c:set var="pageHolder" value="${ownershipPageHolder}" scope="request" />
						<div class="table-container position-relative">
							<table class="table search-table-list mb-0">
								<thead>
									<tr>
							            <th scope="col" style="width: 90px">OWNERSHIP</th>
								        <th scope="col" style="width: 200px; text-align: center;">STATUS</th>
							            <th scope="col" style="width: 100px; padding-right: 30px; text-align: right;">ACTION</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="ownership" items="${ownershipPageHolder.pageList}" varStatus="loop">
										<tr>
											<td>${ownership.name}</td>
											<td class="status__td__cotainer">
												<c:if test="${ownership.isActive()}">
									            	<span class="__active">
									            		<iconify-icon icon="entypo:dot-single" class="dot__icon"></iconify-icon>
									            		Active
									            	</span>
									             </c:if>
									             <c:if test="${!ownership.isActive()}">
									             	<span class="__terminated">
									            		<iconify-icon icon="entypo:dot-single" class="dot__icon"></iconify-icon>
									            		Not Active
									            	</span>
									             </c:if>
											</td>
											<td>
												<div class="action__button__container" style="padding-right: 20px;">
													<sec:authorize access="hasRole('OWNERSHIP_EDIT')">
														<a type="button" class="__button __edit tt" data-bs-placement="top" title="Edit" href="editOwnership.htm?id=${ownership.id}">
															<iconify-icon class="action__icon fs-3" icon="iconamoon:edit-bold"></iconify-icon>
														</a>
													</sec:authorize>
													<sec:authorize access="hasRole('OWNERSHIP_DELETE')">
														<a type="button" class="__button __delete tt" data-bs-placement="top" title="Delete" href="deleteOwnership.htm?id=${ownership.id}">
															<iconify-icon class="action__icon fs-3" icon="mdi:trash-can-outline"></iconify-icon>
														</a>
													</sec:authorize>
												</div>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						<jsp:include page="../global/pagination_footer1.jsp"/>
						</div>
					</c:if>
				</c:if>
			</div>
    	</div>
	    	<jsp:include page="../global/footer-section.jsp"/>
    	<!-- End of <b>inner main</b> class where you can put your form designs inside this container class -->
    </section>
</form>
   
<script>
	//TOOLTIP
	const tooltips = document.querySelectorAll('.tt')
	tooltips.forEach(t =>{
		new bootstrap.Tooltip(t)
	});
</script>
</body>
</html>
