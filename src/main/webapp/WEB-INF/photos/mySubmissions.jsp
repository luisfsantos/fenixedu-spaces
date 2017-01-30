<%--

    Copyright © 2014 Instituto Superior Técnico

    This file is part of FenixEdu Spaces.

    FenixEdu Spaces is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Spaces is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Spaces.  If not, see <http://www.gnu.org/licenses/>.

--%>
<!DOCTYPE html> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<div class="page-header">
  	<h1><spring:message code="title.photo.review"/></h1>
	
	
  	<c:if test="${not empty mySubmissions.pageList}">
  	   	<c:set var="searchPageUrl" value="${searchUrl}${searchId}"/>
  	   	<h3><spring:message code="title.photo.review.list"/></h3>
  		<ul class="pagination">
	  		<li><a href="${searchPageUrl}?p=f">&laquo;</a></li>
  			<c:forEach var="page" begin="${requests.firstLinkedPage}" end="${requests.lastLinkedPage}">
  				<c:set var="pageNumber" value="${page+1}"/>
  				<c:if test="${page == requests.page}">
  					<li class="active"><a href="${searchPageUrl}?p=${pageNumber}">${pageNumber}</a></li>
  				</c:if>
  				<c:if test="${page != requests.page}">
  					<li><a href="${searchPageUrl}?p=${pageNumber}">${pageNumber}</a></li>
  				</c:if>
  			</c:forEach>
	  		<li><a href="${searchPageUrl}?p=l">&raquo;</a></li>
		</ul>
	  	<table class="table">
	  		<thead>
	  			<th><spring:message code="label.space" /></th>
	  			<th><spring:message code="label.photo" /></th>
	  			<th><spring:message code="label.photo.user" /></th>
	  			<th><spring:message code="label.photo.date" /></th>
	  			<th></th>
	  		</thead>
	  		<tbody>
	  			<c:forEach var="photoSubmission" items="${mySubmissions.pageList}">
					<c:set var="photo" value="${photoSubmission.photo}" />
					<c:set var="subject" value="${photoSubmission.submitor}" />
					<c:set var="date" value="${photoSubmission.created}" />
					<c:set var="space" value="${photoSubmission.spacePending}" />
          			<spring:url var="spacePhotoUrl" value="/spaces-view/photo/${photo.externalId}" />
          			<spring:url var="viewUrl" value="/spaces-view/view/${space.externalId}" />
          			<spring:url var="formUrl" value="/spaces/photos/${photoSubmission.externalId}" />
					<tr>
						<td>
							<a href="${viewUrl}"><c:out value="${space.presentationName}"/></a>
						</td>
						<td>
							<img src="${spacePhotoUrl}" class="center-block img-responsive" style="max-height: 450px;"/>
						</td>
						<td>
							<a href="mailto:${subject.email}"><c:out value="${subject.name}"/></a> (<c:out value="${subject.username}"/>)
						</td>
	 					<td><c:out value="${date.toString('dd/MM/yyyy hh:mm')}"/></td>
	 					<td>
		 					<table>
		 						<tr>
		 							<td style="padding:5px;">
		 								<form id="form" role="form" class="accept" action="${formUrl}/accept" method="POST">
		 									<input type="hidden" name="space" value="${space.externalId}">
		 									<input type="hidden" name="page" value="${page}">
											<button type="submit" class="btn btn-xs btn-default"><i class="glyphicon glyphicon-ok"></i>  <spring:message code="label.photo.accept" /></button>
										</form>
									</td>
		 						</tr>
		 						<tr>
		 							<td style="padding:5px;">
		 								<form id="form" role="form" class="reject" action="${formUrl}/reject" method="POST">
		 									<input type="hidden" name="space" value="${space.externalId}">
		 									<input type="hidden" name="page" value="${page}">
		 									<input type="text" class="form-control" name="rejectMessage" required>
											<button type="submit" class="btn btn-xs btn-default"><i class="glyphicon glyphicon-remove-sign"></i> <spring:message code="label.photo.reject" /></button>
										</form>
									</td>
		 						</tr>
		 					</table>
						</td>
					</tr>
				</c:forEach>
			</tbody>
	   	</table>
  	</c:if>
  	
  	<c:if test="${empty mySubmissions.pageList}">
  		<em><spring:message code="space.photo.none"></spring:message></em>
  	</c:if>
</div>

