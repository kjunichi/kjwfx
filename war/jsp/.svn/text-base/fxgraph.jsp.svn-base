<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="com.cocolog_nifty.kjunichi.kjwfx.ds.Fxrecord2" %>
<%@ page import="com.cocolog_nifty.kjunichi.kjwfx.PMF" %>
<%@ page import="com.cocolog_nifty.kjunichi.kjwfx.FxCalc" %>
<html>
<head>
<!--Load the AJAX API-->
<script type="text/javascript" src="http://www.google.com/jsapi"></script>
<script type="text/javascript" src="/fxgraph?currency=EUR"></script>
<script type="text/javascript" src="/fxgraph?currency=USD"></script>
<script type="text/javascript" src="/js/viewRecords.js"></script>
<style type="text/css">
div.blocka {
   float: left;
   width: 70%;
}
</style>
<title>Money KitのUSDとEURの最近のレート</title>
</head>
  <body>
<%
TimeZone.setDefault(TimeZone.getTimeZone("JST"));
TimeZone tz = TimeZone.getDefault();
%>  
  <%= tz.getDisplayName() %>
  <a href="fxperformance.jsp">実績</a>
  <div class="blocka">
    <div id="USD_chart_div"></div>
    <div id="EUR_chart_div"></div>
    </div>
    <div class="bockb">
    <div id="FXRECORD">a</div>
    </div>
    <script type="text/javascript">
    viewRecords();
    </script>
    昨日の結果<%= FxCalc.getYesterdayRate() %>
    
    <a href="http://twitter.com/share" class="twitter-share-button" data-text="<%= FxCalc.getYesterdayRate() %>" data-count="horizontal" data-lang="ja" data-via="kjunichi">Tweet</a><script type="text/javascript" src="http://platform.twitter.com/widgets.js"></script>
  </body>
</html>