<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div id="main-content" class="error-page">
  <div class="right-content"></div>
  <div class="center-content">
    <div class="center-content-wrapper">
      <div class="error-message">
        <h1>Error 404: Page not available!</h1>
        <c:choose>
          <c:when test="${not empty errorMsg }">
            <p><strong>${errorMsg}</strong></p>
          </c:when>
          <c:otherwise>
            <p><strong style="color: red;">The page you're looking for does not exist.</strong></p>
          </c:otherwise>
        </c:choose>
      </div>
    </div>
  </div>
</div>