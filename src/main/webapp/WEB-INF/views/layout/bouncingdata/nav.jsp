<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="main-nav-container">
  <div class="main-nav-content">
    <ul class="main-nav-links">
      <%-- <li><a class="main-nav-item" href="<c:url value="/stream" />">Timeline</a></li> --%>
      <li><a class="main-nav-item ${(pageId=='' || pageId=='a')?'nav-selected':''}" href="<c:url value="/stream/a/all/recent" />">All</a></li>
      <li><a class="main-nav-item ${pageId=='streambyself'?'nav-selected':''}" href="<c:url value="/stream/streambyself/all/recent" />">Created By Me</a></li>
      <li><a class="main-nav-item ${pageId=='staffpicks'?'nav-selected':''}" href="<c:url value="/stream/staffpicks/all/recent" />">Staff's Picks</a></li>
      <li><a class="main-nav-item ${pageId=='popularAuthors'?'nav-selected':''}" href="<c:url value="/stream/popularAuthors/all/recent" />">Popular Authors</a></li>
      <li>
        <a class="main-nav-item ${pageId=='tags'?'nav-selected':''}" href="<c:url value="/tags" />">Tags</a>
        <div class="tags-container">
          <ul class="tag-links">
            <li><a class="sub-nav-item ${pageId=='economics'?'nav-selected':''}" href="<c:url value="/tag/economics" />">Economics</a></li>
            <li><a class="sub-nav-item ${pageId=='finance'?'nav-selected':''}" href="<c:url value="/tag/finance" />">Finance</a></li>
            <li><a class="sub-nav-item ${pageId=='health'?'nav-selected':''}" href="<c:url value="/tag/health" />">Health</a></li>
            <li><a class="sub-nav-item ${pageId=='education'?'nav-selected':''}" href="<c:url value="/tag/education" />">Education</a></li>
            <li><a class="sub-nav-item ${pageId=='us'?'nav-selected':''}" href="<c:url value="/tag/us" />">US</a></li>
            <li><a class="sub-nav-item ${pageId=='football'?'nav-selected':''}" href="<c:url value="/tag/football" />">Football</a></li>
            <li><a class="sub-nav-item ${pageId=='sports'?'nav-selected':''}" href="<c:url value="/tag/sports" />">Sports</a></li>
            <li><a class="sub-nav-item ${pageId=='amz'?'nav-selected':''}" href="<c:url value="/tag/amz" />">AMZ</a></li>
            <li><a class="sub-nav-item ${pageId=='worldbank'?'nav-selected':''}" href="<c:url value="/tag/worldbank" />">World Bank</a></li>
            <li><a class="sub-nav-item ${pageId=='oced'?'nav-selected':''}" href="<c:url value="/tag/oced" />">OCED</a></li>
            <li><a class="sub-nav-item ${pageId=='payroll'?'nav-selected':''}" href="<c:url value="/tag/payroll" />">Payroll</a></li>
            <li><a class="sub-nav-item ${pageId=='more'?'nav-selected':''}" href="<c:url value="/tag/more" />">More..</a></li>
          </ul>
        </div>
      </li>
    </ul>
  </div>
</div>
<div style="display:none;">
<div class="nav-item nav-page" id="nav-stream">
  <form action="<c:url value='/stream'/>" method="GET" id="nav-stream-form" style="padding: 0px;margin: 0px;"></form>
  <a href="<c:url value='/stream'/>" class="nav-item-link" id="nav-stream-link" ref="stream">
    <div class="nav-item-icon"></div>
    <div class="nav-item-text">Home</div>
  </a>
</div>
<!--div class="nav-item" id="nav-profile">
  <form action="<c:url value='/profile'/>" method="GET" id="nav-profile-form" style="padding: 0px;margin: 0px;"></form>
  <a href="<c:url value='/profile'/>" class="nav-item-link" id="nav-profile-link" ref="profile">
    <div class="nav-item-icon"></div>
    <div class="nav-item-text">Profile</div>
  </a>
</div-->
<div class="nav-item" id="nav-create">
  <a href="javascript:void(0)" class="nav-item-link" id="nav-create-link" ref="create">
    <div class="nav-item-icon"></div>
    <div class="nav-item-text">Create</div>
  </a>
</div>
<div class="nav-item nav-page" id="nav-connect">
  <form action="<c:url value='/connect'/>" method="GET" id="nav-connect-form" style="padding: 0px;margin: 0px;"></form>
  <a href="<c:url value='/connect'/>" class="nav-item-link" id="nav-connect-link" ref="connect">
    <div class="nav-item-icon"></div>
    <div class="nav-item-text">Connect</div>
  </a>
</div>
<div class="nav-item nav-page" id="nav-help">
  <form action="<c:url value='/help/python'/>" method="GET" id="nav-help-form" style="padding: 0px;margin: 0px;"></form>
  <a href="<c:url value='/help/python'/>" class="nav-item-link" id="nav-help-link" ref="help">
    <div class="nav-item-icon"></div>
    <div class="nav-item-text">API Help</div>
  </a>
</div>
<!-- div class="nav-item" id="nav-search">
  <form action="<c:url value='/main/search'/>" method="GET" id="nav-search-form" style="padding: 0px;margin: 0px;"></form>
  <a href="<c:url value='/main/search'/>" class="nav-item-link" id="nav-search-link">
    <div class="nav-item-icon"></div>
    <div class="nav-item-text">Search</div>
  </a>
</div-->
<div class="nav-hidden">
  <div class="nav-hidden-menu nav-create-popup" id="nav-create-popup">
    <a href="javascript:void(0)" class="nav-hidden-item nav-create-viz">Visualization</a>
    <a href="javascript:void(0)" class="nav-hidden-item nav-create-scraper">Scraper</a>
    <div class="nav-hidden-menu-rule"></div>
    <a href="javascript:void(0)" class="nav-hidden-item nav-upload-data">Upload dataset</a>
  </div>
</div>
</div>