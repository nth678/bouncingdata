<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>


<div id="main-content" class="tagpage-container">
  <div class="center-content">
    <div class="center-content-wrapper">
      <div class="center-content-main">
        <h3>${title }</h3>
        <div class="tag-item-list" id="tag-item-list">
          <c:if test="${not empty anlsList }">
          <c:forEach items="${anlsList }" var="anls">
              <div class="tag-item analysis-list-item" aid="${anls.id }">
                <div class="tag-item-content content">
                  <a class="avatar-link">
                    <img class="avatar no-avatar" src="<c:url value="/resources/images/no-avatar.png" />">
                  </a>
                  <div class="thumbnail">
                    <a id="thumb-${anls.id }" href="<c:url value="/anls/${anls.guid}" />">
                      <c:choose>
                        <c:when test="${not empty anls.thumbnail }">
                          <img class="thumb-img" src="<c:url value="/thumbnails/${anls.thumbnail}.jpg" />" onerror="this.src='<c:url value="/thumbnails/no-image.jpg" />'; this.onerror=null;" />
                        </c:when>
                        <c:otherwise>
                          <img class="thumb-img" src="<c:url value="/thumbnails/no-image.jpg" />" />
                        </c:otherwise>
                      </c:choose>
                    </a>
                  </div>
                  <p class="title">
                    <a href="<c:url value="/anls/${anls.guid}" />"><strong>${anls.name}</strong></a>
                  </p>
                  <p class="info">
                    <span class="author">Author: <a href="#">${anls.user.username }</a></span><br/>
                    <span class="tag-list">Tags:&nbsp; 
                      <c:if test="${not empty anls.tags }">
                        <c:forEach var="tag" items="${anls.tags }">
                          <div class="tag-element-outer">
                            <a class="tag-element" href="<c:url value="/tag/${tag.tag }" />">${tag.tag }</a>
                          </div>
                        </c:forEach>
                      </c:if>
                    </span>
                  </p>
                  <p class="description">
                    <span>${anls.description }</span>
                  </p>
                  
                  <div class="clear"></div>
                  <div class="footer">
                    <c:if test="${anls.score > 0}">
                      <strong class="score score-positive">+${anls.score }</strong>    
                    </c:if>
                    <c:if test="${anls.score == 0}">
                      <strong class="score">0</strong>    
                    </c:if>
                    <c:if test="${anls.score < 0}">
                      <strong class="score score-negative">${anls.score }</strong>    
                    </c:if>
                    &nbsp;<a class="comments-link" href="<c:url value="/anls/${anls.guid}#comments" />"><strong>${anls.commentCount }</strong>&nbsp;comments</a>
                  </div>
                </div>
                <div class="clear"></div>
              </div>
          </c:forEach>   
          </c:if> 
          <c:if test="${empty anlsList }"><span>No analysis associates with this tag.</span></c:if>  
          <!-- div class="tag-items-footer">
            <a href="javascript:void(0);" class="more-item">More</a> &nbsp;&nbsp;
            <img style="display: none;" class="feed-loading" src="<c:url value="/resources/images/loader32.gif" />" />
          </div-->    
        </div>
      </div>
    </div>
  </div>
</div>