<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<script>
$(function() {
  $('.result-analysis ul.analysis-list li').each(function() {
    var $link = $('a.anls-item', $(this));
    $link.click(function() {
      com.bouncingdata.Nav.fireAjaxLoad($link.prop('href'), false);
      return false;
    });
  });
  $('.result-dataset ul.dataset-list li').each(function() {
    var $link = $('a.dataset-item', $(this));
    $link.click(function() {
      com.bouncingdata.Nav.fireAjaxLoad($link.prop('href'), false);
      return false;
    });
  });

  $('.result-nav-item').click(function() {
    if ($(this).hasClass('user-selected')) return;
    
    var $currentSelected = $('.search-result-nav .result-nav-item.user-selected');
    if ($currentSelected.length > 0) {
      $currentSelected.removeClass('user-selected');
    }
    $(this).addClass('user-selected');
    
    if ($(this).hasClass('result-nav-all')) {
      $('.search-result').show();
    } else if ($(this).hasClass('result-nav-anls')) {
      $('.search-result.analysis-result').show();
      $('.search-result.scraper-result').hide();
      $('.search-result.dataset-result').hide();
    } else if ($(this).hasClass('result-nav-scraper')) {
      $('.search-result.analysis-result').hide();
      $('.search-result.scraper-result').show();
      $('.search-result.dataset-result').hide();
    } else if ($(this).hasClass('result-nav-dataset')) {
      $('.search-result.analysis-result').hide();
      $('.search-result.scraper-result').hide();
      $('.search-result.dataset-result').show();
    }
  });
  
  com.bouncingdata.Nav.setSelected('search', '${query}');
});
</script>

<style>
	.event-content {
		float: left;
		border: 1px solid #E2E2E2;
		border-radius: 2px;
		padding: 10px;
		width: 510px;
		position: relative;
		margin-bottom: 10px;
	}
	
	.title{
		font-size: 13px;
		line-height: 1.2em;
		margin: 0;
	}
	
	
</style>

<div id="main-content" class="main-content search-page">
  <div class="right-content"></div>
  <div class="center-content">
    <div class="center-content-wrapper search-result-wrapper">
      <div>Search result for <strong>${query}</strong>:</div>
      <div class="search-result-nav">
        <div class="result-nav-item result-nav-all user-selected" href="javascript:void(0);">All</div>
        <div class="result-nav-item result-nav-anls right-item" href="javascript:void(0);">Analysis</div>
        <div class="result-nav-item result-nav-scraper right-item" href="javascript:void(0);">Scraper</div>
        <div class="result-nav-item result-nav-dataset right-item" href="javascript:void(0);">Dataset</div>
      </div>

      <div class="search-result analysis-result">
        <h3>
          <span class="result-title">Analysis</span> &nbsp;&nbsp;
        </h3>
        <ul class="analysis-list search-result-list">
          <c:choose>
            <c:when test="${empty searchResult.analyses }">No analysis matched.</c:when>
            <c:otherwise>
              <c:forEach items="${searchResult.analyses }" var="anls">
                <li>
                  <!-- a class="anls-item" href="<c:url value="/anls/${anls.guid}" />" title="View analysis">${anls.name }</a-->
                  <div class="search-result-item">
                  	<div class="event-content">
	                    <div class="thumbnail">
	                      <a href="<c:url value="/anls/${anls.guid}" />" class="anls-item">
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
	                      <a class="anls-item" href="<c:url value="/anls/${anls.guid}" />" title="View analysis"><strong>${anls.name}</strong></a>
	                      <br style="line-height: 18px;" />
	                      <p style="font-size: 11px; color: #999999; margin-bottom: 0px;">Author: 
	                      	<a class="user-link" href="javascript:void(0);" style="color: #999999;">${anls.user.username }</a>
	                      </p>
	                    </p>
	                    <div class="description">
	                      ${anls.description }
	                    </div>
	                    </div>
                    <div class="clear"></div>
                  </div>
                </li>
              </c:forEach>
            </c:otherwise>
          </c:choose>
        </ul>
      </div>

      <div class="search-result scraper-result">
        <h3>
          <span class="result-title">Scraper</span>
        </h3>
        <ul class="scraper-list search-result-list">
          <c:choose>
            <c:when test="${empty searchResult.scrapers }">No scraper matched.</c:when>
            <c:otherwise>
              <c:forEach items="${searchResult.scrapers }" var="scrp">
                <li>
                  <div class="search-result-item">
	                  <div class="event-content">
	                    <div class="thumbnail">
	                      <a href="javscript:void(0)" class="scraper-item">
	                        <img class="thumb-img" src="<c:url value="/thumbnails/no-image.jpg" />" />
	                      </a>
	                    </div>
	                    <p class="title">
	                      <a class="scraper-item" href="javascript:void(0)" title="View scraper"><strong>${scrp.name}</strong></a>
	                      <br style="line-height: 18px;" />
	                      <p style="font-size: 11px; color: #999999; margin-bottom: 0px;">Author:
	                      	 <a class="user-link" href="" style="color: #999999;">${scrp.user.username }</a>
                      	  </p>
	                    </p>
	                    <p class="description">
	                      <span>${scrp.description }</span>
	                    </p>
                    </div>
                    <div class="clear"></div>
                  </div>
                </li>
              </c:forEach>
            </c:otherwise>
          </c:choose>
        </ul>
      </div>


      <div class="search-result dataset-result">
        <h3>
          <span class="result-title">Dataset</span>
        </h3>
        <ul class="dataset-list search-result-list">
          <c:choose>
            <c:when test="${empty searchResult.datasets }">No dataset matched.</c:when>
            <c:otherwise>
              <c:forEach items="${searchResult.datasets }" var="dts">
                <li>
                  <div class="search-result-item">
	                  <div class="event-content">
	                    <div class="thumbnail">
	                      <a href="javscript:void(0)" class="dataset-item">
	                        <img class="thumb-img" src="<c:url value="/thumbnails/no-image.jpg" />" />
	                      </a>
	                    </div>
	                    <p class="title">
	                      <a class="dataset-item" href="<c:url value="/dataset/view/${dts.guid}" />" title="View dataset"><strong>${dts.name}</strong></a>
	                      <br style="line-height: 18px;" />
	                      <p style="font-size: 11px; color: #999999; margin-bottom: 0px;">Author:
	                        <a class="user-link" href="" style="color: #999999;">${dts.user.username }</a>
	                      </p>
	                    </p>
	                    <p class="description">
	                      <span>${dts.description }</span>
	                    </p>
	                    </div>
                    <div class="clear"></div>
                  </div>
                </li>
              </c:forEach>
            </c:otherwise>
          </c:choose>
        </ul>
      </div>

    </div>
  </div>
</div>