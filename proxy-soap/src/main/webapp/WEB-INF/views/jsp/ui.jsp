<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %><%@ page session="false" %><!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8" />
    <title>Proxy Soap - UI</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="description" content="" />
    <meta name="author" content="" />

    <!-- Le styles -->
    <link href="<%=request.getContextPath()%>/res/css/bootstrap.css" rel="stylesheet" />
    <style>
      body {
        padding-top: 60px; /* 60px to make the container go all the way to the bottom of the topbar */
      }
    </style>
    <!--<link href="<%=request.getContextPath()%>/res/css/bootstrap-responsive.css" rel="stylesheet" />-->
    <link href="<%=request.getContextPath()%>/res/css/bootstrap-glyphicons.css" rel="stylesheet" />

    <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="<%=request.getContextPath()%>/res/js/html5shiv.js"></script>
    <![endif]-->
  </head>

  <body>

    <div class="navbar navbar-inverse navbar-fixed-top bs-docs-nav">
        <div class="container">
          <a class="navbar-brand" href="#">Proxy SOAP</a>
          <div class="nav-collapse collapse bs-navbar-collapse">
            <ul class="nav navbar-nav">
              <li class="active"><a href="#">Logs</a></li>
              <li><a href="#about">Configuration</a></li>
            </ul>
          </div><!--/.nav-collapse -->
        </div>
    </div>
    <div class="bs-masthead">
    <div class="container">
        <%@include file="/WEB-INF/views/jsp/logs.jsp" %>
    </div> <!-- /container -->
    </div>
    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="<%=request.getContextPath()%>/res/js/jquery.js"></script>
    <script src="<%=request.getContextPath()%>/res/js/bootstrap.js"></script>
    <script>
        $(document).ready(function(){
            // popover demo
            $("a[data-toggle=popover]")
              .popover()
              .click(function(e) {
                e.preventDefault()
              })
        });</script>

  </body>
</html>