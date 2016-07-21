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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:url var="baseUrl" value="/static/fenix-spaces"/>
 ${portal.bennuPortal()}
<script type="text/javascript" src="${baseUrl}/js/sprintf.min.js">
</script>

<div class="page-header">
  <h1><fmt:message key="title.space.photo.submit"/><small><spring:message code="title.space.photo.submit"/></small></h1>
</div>

<spring:url var="formActionUrl" value="${action}"/>
<form:form modelAttribute="photoSubmission" role="form" method="post" action="${formActionUrl}" enctype="multipart/form-data">
  
  <div class="form-group">
    <form:label for="spacePhotoFileInput" path="submissionMultipartFile"><spring:message code="label.spaces.photo"/></form:label>
    <form:input type="file" accept="image/*" class="form-control" id="spacePhotoFileInput" path="submissionMultipartFile"/>
  </div>
  
  <button type="submit" class="btn btn-default"><spring:message code="label.submit"/></button>
</form:form>
