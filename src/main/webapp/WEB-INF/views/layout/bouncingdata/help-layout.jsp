<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<script>
  $(function() {
    com.bouncingdata.Main.loadCss(ctx + "/resources/css/bouncingdata/help.css", "help");

	  $('#help-page-nav .help-page-nav-item').each(function() {
	    var $form = $('form', this);
	    var $link = $('a', this);
	    var href = $link.prop('href');
	    $link.bind('click', function(e) {
	      com.bouncingdata.Main.toggleAjaxLoading(true);
	      $('.help-page-nav a.selected-lang').removeClass('selected-lang');
	      $(this).addClass('selected-lang');
	      window.history.pushState({linkId: href, type: 'page'}, $link.text(), href);
	      e.preventDefault();
	    });

	    // load corresponding help page
	    Spring.addDecoration(new Spring.AjaxEventDecoration({
	      elementId: $link.attr('id'),
	      formId: $form.attr('id'),
	      event: "onclick",
	      params: {fragments: "help-content"}
	    }));
	  });

	  com.bouncingdata.Nav.setSelected('page', 'help');
	});
</script>
<div id="main-content" class="help-page">
  <div class="right-content"></div>
  <div class="center-content">
    <div class="center-content-wrapper">
      <div id="help-page-nav" class="help-page-nav">
        <span>Choose your language:&nbsp;&nbsp;</span>
        <div class="help-page-nav-item">
          <form action="<c:url value='/help/python'/>" method="GET" id="help-page-python" style="padding: 0px;margin: 0px;"></form>
          <a class="help-page-link" href="<c:url value='/help/python'/>" id="help-page-python-link">Python</a>
        </div>
        <div class="help-page-nav-item">
          <form action="<c:url value='/help/r'/>" method="GET" id="help-page-r" style="padding: 0px;margin: 0px;"></form>
          <a href="<c:url value='/help/r'/>" id="help-page-r-link" class="help-page-link">R</a>
        </div>
      </div>
      <div class="help-right-menu">

      </div>
      <div class="help-content-wrapper">
        <tiles:insertAttribute name="help-content"></tiles:insertAttribute>
      </div>
    </div>
  </div>
</div>