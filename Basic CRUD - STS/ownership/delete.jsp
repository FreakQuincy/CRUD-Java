<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../global/taglib.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<link rel="shortcut icon" type="image/x-icon" href="<c:url value='/img/wexLinx/wexlinx-icon.ico'/>" />
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css">
	
	<meta charset="utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Delete Ownership</title>
	
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
	<script src="https://code.iconify.design/iconify-icon/1.0.7/iconify-icon.min.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.5.1/jquery.min.js" charset="utf-8"></script>
	
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
	</style>
</head>
<body>

	<!-- Header Section -->
	<c:import url="/menu.htm?type=system"/>
      
    <section class="main-container">
	    <!-- Start of <b>inner main</b> class where you can put your form designs inside this container class -->
	    <div class="inner-main animate__animated animate__fadeIn">
	    
	    	<div class="header-title mb-5">
                <h3 class="page__title">
                    Delete Ownership
                </h3>
                <p class="page__description">
                    Please review the details below before you proceed deleting the Ownership in the system.
                </p>
            </div>
	    	<form id="form" method="POST" action="deleteOwnership.htm">
				<input type="hidden" name="id" value="${ownership.id}">
                <jsp:include page="../global/formMessage.jsp"/>
                
                <!-- Ownership Detail -->
                <div class="form-container mb-5">
                    <h5>
                        Ownership Status
                    </h5>
                    <div class="row m-auto gx-lg-5 gx-md-5 py-5 px-3 px-xl-5">
                         <div class="col-md-6 col-lg-4 col-xxl-3 mb-5">
                            <label for="ownership" class="form-label fw-semibold">Status</label>
                            <c:if test="${ownership.isActive()}">
                             <p class="ps-3">Active</p>
                            </c:if>
                            <c:if test="${!ownership.isActive()}">
                             <p class="ps-3">Not Active</p>
                            </c:if>   
                        </div>
                    </div>
                </div>
                
                <div class="form-container mb-5">
                    <h5>
                        Ownership Detail
                    </h5>
                    <div class="row m-auto gx-lg-5 gx-md-5 py-5 px-3 px-xl-5">
                        <div class="col-md-6 col-lg-4 col-xxl-3 mb-5">
                            <label for="ownership" class="form-label fw-semibold">Ownership Name</label>
                            <p class="ps-3">${ownership.name}</p>
                        </div>
                    </div>
                </div>
                <!-- Ownership Detail -->
                
                <!-- Action buttons -->
                <div class="button-action mt-5">
                    <div class="d-flex justify-content-between justify-content-md-center">
	                    <div class="me-md-5">
	                    	<input type="button" class="btn btn-back" value=" Cancel  " onclick="javascript: window.location='searchOwnership.htm?cancel=1'">
                        </div>
                       	<div class="ms-md-5">
                       		<c:if test="${ownership.id > 0}">
                           		<input type="submit" class="btn btn-danger" value=" Delete " name="confirmDelete">
                       		</c:if>
                        </div>
                    </div>
                </div>
            </form>
	    
	        <jsp:include page="../global/footer-section.jsp"/>
	    </div>
	    <!-- End of <b>inner main</b> class where you can put your form designs inside this container class -->
	</section>
<script src="<c:url value="/js/action-button.js"/>"></script>
</body>
</html>