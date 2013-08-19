<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<style>
.toggler {
	width: 100%;
	height: 200px;
	position: relative;
}

#button {
	padding: .5em 1em;
	text-decoration: none;
}

#effect {
	width: 90%;
	height: 135px;
	padding: 0.4em;
	margin-top: 15px;
	margin-left: 50px;
	float: left;
}
	
#effect h3 {
	margin: 0;
	padding: 0.4em;
	text-align: center;
}

.lnkA{
	color: #12c !important;
	text-decoration: underline !important;
}

.ui-effects-transfer {
	border: 2px dotted gray;
}

.divImg{
	width: 80px; 
	float: left; 
	margin-top: 5px;
}

.imgAuthor{
	width: 80px; 
	height: 100px; 
	vertical-align: text-top;
}

.divFeed{
	float: left; 
	padding-left: 10px; 
	width: 85%; 
	margin-top: 10px;
}

.divMore{
	float: left; 
	padding-right: 10px; 
	width: 85%; 
	padding-top: 45px; 
	text-align: right;
}

</style>

<script>
	com.bouncingdata.ActivityStream.init();
</script>

<div id="main-content" class="homepage-container">
	<div class="right-content">
		<div class="right-content-section most-popular-section">
			<h4 class="right-section-title">Most Popular</h4>
			<div class="most-popular-content right-section-tabs-wrapper">
				<div id="most-popular-content-tabs"
					class="most-popular-content-tabs ui-tabs">
					<ul>
						<li><a href="#most-popular-analysis">Analysis</a></li>
						<li><a href="#most-popular-dataset">Dataset</a></li>
					</ul>
					<div class="side-list-panel ui-tabs-hide"
						id="most-popular-analysis">
						<c:forEach items="${topAnalyses }" var="anls">
							<div class="side-item-panel">
								<a class="small-avatar-link"> <img class="avatar no-avatar"
									src="<c:url value="/resources/images/no-avatar.png" />">
								</a>
								<div class="small-thumbnail">
									<a href="<c:url value="/anls/${anls.guid}" />"> <c:choose>
											<c:when test="${not empty anls.thumbnail }">
												<img class="thumb-img"
													src="<c:url value="/thumbnails/${anls.thumbnail}.jpg" />"
													onerror="this.src='<c:url value="/thumbnails/no-image.jpg" />'; this.onerror=null;" />
											</c:when>
											<c:otherwise>
												<img class="thumb-img"
													src="<c:url value="/thumbnails/no-image.jpg" />" />
											</c:otherwise>
										</c:choose>
									</a>
								</div>
								<p class="side-item-title">
									<a href="<c:url value="/anls/${anls.guid}" />"><strong>${anls.name}</strong></a>
								</p>
								<p class="side-item-author">
									<span>by <a href="#">${anls.user.username }</a></span>
								</p>
								<div class="clear"></div>
							</div>
						</c:forEach>
					</div>
					<div class="side-list-panel ui-tabs-hide" id="most-popular-dataset">
						<c:forEach items="${topDatasets }" var="dts">
							<div class="side-item-panel">
								<a class="small-avatar-link"> <img class="avatar no-avatar"
									src="<c:url value="/resources/images/no-avatar.png" />">
								</a>
								<div class="small-thumbnail">
									<a href="<c:url value="/dataset/view/${dts.guid}" />"> <img
										class="thumb-img"
										src="<c:url value="/thumbnails/no-image.jpg" />" />
									</a>
								</div>
								<p class="side-item-title">
									<a href="<c:url value="/dataset/view/${dts.guid}" />"><strong>${dts.name}</strong></a>
								</p>
								<p class="side-item-author">
									<span>by <a href="#">${dts.user.username }</a></span>
								</p>
								<div class="clear"></div>
							</div>
						</c:forEach>
					</div>
				</div>
			</div>
		</div>
		<div class="right-content-section recommendation-section">
			<h4 class="right-section-title">Staff's Pick</h4>
			<div class="staff-pick-content right-section-tabs-wrapper">
				<div id="staff-pick-content-tabs"
					class="staff-pick-content-tabs ui-tabs">
					<ul>
						<li><a href="#staff-pick-analysis">Analysis</a></li>
						<li><a href="#staff-pick-dataset">Dataset</a></li>
					</ul>
					<div class="side-list-panel ui-tabs-hide" id="staff-pick-analysis">
						<c:forEach items="${topAnalyses }" var="anls">
							<div class="side-item-panel">
								<a class="small-avatar-link"> <img class="avatar no-avatar"
									src="<c:url value="/resources/images/no-avatar.png" />">
								</a>
								<div class="small-thumbnail">
									<a href="<c:url value="/anls/${anls.guid}" />"> <c:choose>
											<c:when test="${not empty anls.thumbnail }">
												<img class="thumb-img"
													src="<c:url value="/thumbnails/${anls.thumbnail}.jpg" />"
													onerror="this.src='<c:url value="/thumbnails/no-image.jpg" />'; this.onerror=null;" />
											</c:when>
											<c:otherwise>
												<img class="thumb-img"
													src="<c:url value="/thumbnails/no-image.jpg" />" />
											</c:otherwise>
										</c:choose>
									</a>
								</div>
								<p class="side-item-title">
									<a href="<c:url value="/anls/${anls.guid}" />"><strong>${anls.name}</strong></a>
								</p>
								<p class="side-item-author">
									<span>by <a href="#">${anls.user.username }</a></span>
								</p>
								<div class="clear"></div>
							</div>
						</c:forEach>
					</div>
					<div class="side-list-panel ui-tabs-hide" id="staff-pick-dataset">
						<c:forEach items="${topDatasets }" var="dts">
							<div class="side-item-panel">
								<a class="small-avatar-link"> <img class="avatar no-avatar"
									src="<c:url value="/resources/images/no-avatar.png" />">
								</a>
								<div class="small-thumbnail">
									<a href="<c:url value="/dataset/view/${dts.guid}" />"> <img
										class="thumb-img"
										src="<c:url value="/thumbnails/no-image.jpg" />" />
									</a>
								</div>
								<p class="side-item-title">
									<a href="<c:url value="/dataset/view/${dts.guid}" />"><strong>${dts.name}</strong></a>
								</p>
								<p class="side-item-author">
									<span>by <a href="#">${dts.user.username }</a></span>
								</p>
								<div class="clear"></div>
							</div>
						</c:forEach>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="center-content">
		<div class="center-content-wrapper">
			<div class="stream-container center-content-main">
				<div class="toggler">
						
						<!-- img 1 -->
						<div id="effect" class="ui-widget-content ui-corner-all">
							<h3 class="ui-widget-header ui-corner-all">1.Demo</h3>
							<div class="divImg">
								<img  src="<c:url value="/thumbnails/author.jpg" />" class="imgAuthor">
							</div>
							<div>
								<div class="divFeed">
									<u><b><i>Most feeds</i></b></u>: 
									<a class="lnkA" href="#">Etiam libero neque</a>. 
									<a class="lnkA" href="#">luctus a semper at</a>. 
									<a class="lnkA" href="#">lorem</a>.
									<a class="lnkA" href="#">Sed pede</a>. 
									<a class="lnkA" href="#">Nulla lorem metus</a>. 
									<a class="lnkA" href="#">luctus sed</a>. 
									<a class="lnkA" href="#">hendrerit vitae</a>. 
									<a class="lnkA" href="#">mi</a>.
								</div>
								<div class="divMore">
									<a href="#" class="lnkA">...More</a>
								</div>
							</div>
						</div>
						
						<!-- img 2 -->
						<div id="effect" class="ui-widget-content ui-corner-all">
							<h3 class="ui-widget-header ui-corner-all">2.khiem</h3>
							<div class="divImg">
								<img  src="<c:url value="/thumbnails/author.jpg" />" class="imgAuthor">
							</div>
							<div>
								<div class="divFeed">
									<u><b><i>Most feeds</i></b></u>: 
									<a class="lnkA" href="#">Etiam libero neque</a>. 
									<a class="lnkA" href="#">luctus a semper at</a>. 
									<a class="lnkA" href="#">lorem</a>.
									<a class="lnkA" href="#">Sed pede</a>. 
									<a class="lnkA" href="#">Nulla lorem metus</a>. 
									<a class="lnkA" href="#">luctus sed</a>. 
									<a class="lnkA" href="#">hendrerit vitae</a>. 
									<a class="lnkA" href="#">mi</a>.
								</div>
								<div class="divMore">
									<a href="#" class="lnkA">...More</a>
								</div>
							</div>
						</div>

						<!-- img 3 -->
						<div id="effect" class="ui-widget-content ui-corner-all">
							<h3 class="ui-widget-header ui-corner-all">3.minh</h3>
							<div class="divImg">
								<img  src="<c:url value="/thumbnails/author.jpg" />" class="imgAuthor">
							</div>
							<div>
								<div class="divFeed">
									<u><b><i>Most feeds</i></b></u>:  
									<a class="lnkA" href="#">Etiam libero neque</a>. 
									<a class="lnkA" href="#">luctus a semper at</a>. 
									<a class="lnkA" href="#">lorem</a>.
									<a class="lnkA" href="#">Sed pede</a>. 
									<a class="lnkA" href="#">Nulla lorem metus</a>. 
									<a class="lnkA" href="#">luctus sed</a>. 
									<a class="lnkA" href="#">hendrerit vitae</a>. 
									<a class="lnkA" href="#">mi</a>.
								</div>
								<div class="divMore">
									<a href="#" class="lnkA">...More</a>
								</div>
							</div>
						</div>
						
						<!-- img 4 -->
						<div id="effect" class="ui-widget-content ui-corner-all">
							<h3 class="ui-widget-header ui-corner-all">4.nth678</h3>
							<div class="divImg">
								<img  src="<c:url value="/thumbnails/author.jpg" />" class="imgAuthor">
							</div>
							<div>
								<div class="divFeed">
									<u><b><i>Most feeds</i></b></u>: 
									<a class="lnkA" href="#">Etiam libero neque</a>. 
									<a class="lnkA" href="#">luctus a semper at</a>. 
									<a class="lnkA" href="#">lorem</a>.
									<a class="lnkA" href="#">Sed pede</a>. 
									<a class="lnkA" href="#">Nulla lorem metus</a>. 
									<a class="lnkA" href="#">luctus sed</a>. 
									<a class="lnkA" href="#">hendrerit vitae</a>. 
									<a class="lnkA" href="#">mi</a>.
								</div>
								<div class="divMore">
									<a href="#" class="lnkA">...More</a>
								</div>
							</div>
						</div>
						
						<!-- img 5 -->
						<div id="effect" class="ui-widget-content ui-corner-all">
							<h3 class="ui-widget-header ui-corner-all">5.lapnd</h3>
							<div class="divImg">
								<img  src="<c:url value="/thumbnails/author.jpg" />" class="imgAuthor">
							</div>
							<div>
								<div class="divFeed">
									<u><b><i>Most feeds</i></b></u>: 
									<a class="lnkA" href="#">Etiam libero neque</a>. 
									<a class="lnkA" href="#">luctus a semper at</a>. 
									<a class="lnkA" href="#">lorem</a>.
									<a class="lnkA" href="#">Sed pede</a>. 
									<a class="lnkA" href="#">Nulla lorem metus</a>. 
									<a class="lnkA" href="#">luctus sed</a>. 
									<a class="lnkA" href="#">hendrerit vitae</a>. 
									<a class="lnkA" href="#">mi</a>.
								</div>
								<div class="divMore">
									<a href="#" class="lnkA">...More</a>
								</div>
							</div>
						</div>
						
						<!-- img 6 -->
						<div id="effect" class="ui-widget-content ui-corner-all">
							<h3 class="ui-widget-header ui-corner-all">6.vinhpq</h3>
							<div class="divImg">
								<img  src="<c:url value="/thumbnails/author.jpg" />" class="imgAuthor">
							</div>
							<div>
								<div class="divFeed">
									<u><b><i>Most feeds</i></b></u>: 
									<a class="lnkA" href="#">Etiam libero neque</a>. 
									<a class="lnkA" href="#">luctus a semper at</a>. 
									<a class="lnkA" href="#">lorem</a>.
									<a class="lnkA" href="#">Sed pede</a>. 
									<a class="lnkA" href="#">Nulla lorem metus</a>. 
									<a class="lnkA" href="#">luctus sed</a>. 
									<a class="lnkA" href="#">hendrerit vitae</a>. 
									<a class="lnkA" href="#">mi</a>.
								</div>
								<div class="divMore">
									<a href="#" class="lnkA">...More</a>
								</div>
							</div>
						</div>
						
				</div>
			</div>
		</div>
	</div>