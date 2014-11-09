<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="com.cocolog_nifty.kjunichi.kjwfx.FxCalc" %>
<%@ page import="com.cocolog_nifty.kjunichi.kjwfx.ds.Fxrecord2" %>
<%@ page import="com.cocolog_nifty.kjunichi.kjwfx.PMF" %>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
<title>成績</title>
</head>
<body>
<%
UserService userService = UserServiceFactory.getUserService();
User user = userService.getCurrentUser();

// EURの成績の取得		
List<Fxrecord2> list = FxCalc.getFxDone(user,"EUR");
%>
<%= list.size() %>
<table border="1">
<tr><th>購入日</th><th>売却日</th><th>金額</th><th>TTB</th><th>TTS</th><th>利益(円)</th></tr>
<%
int total = 0;
for (Fxrecord2 g : list) {
%>
<tr><td><%= g.getDate() %></td><td><%= g.getTtsDate() %></td><td><%= g.getAmount() %></td><td><%= g.getTtb() %></td><td><%= g.getTts() %></td>
<%
int result = 0;
if (g.getAmount() != null && g.getTtb() != null && g.getTts() != null  ) {
Float srcAmount = g.getAmount() * g.getTtb();
Float distAmount = g.getAmount() * g.getTts();
result = distAmount.intValue() - Math.round(srcAmount.floatValue());
total = total + result; 
}
%>
<td><%= result %></td></tr>
<%
}
%>
</table>
<p>EUR成果（円）：<%= total %></p>

<%
list = FxCalc.getFxDone(user,"USD");
%>
<%= list.size() %>
<table border="1">
<tr><th>購入日</th><th>売却日</th><th>取引額</th><th>TTB</th><th>TTS</th><th>利益(円)</th></tr>
<%
total = 0;
for (Fxrecord2 g : list) {
%>
<tr><td><%= g.getDate() %></td><td><%= g.getTtsDate() %></td><td><%= g.getAmount() %></td><td><%= g.getTtb() %></td><td><%= g.getTts() %></td>
<%
int result = 0;
if (g.getAmount() != null && g.getTtb() != null && g.getTts() != null  ) {
 Float srcAmount = (g.getAmount() * g.getTtb());
 Float distAmount = (g.getAmount() * g.getTts());
result = distAmount.intValue() - Math.round(srcAmount.floatValue());
total = total + result; 
}
%>
<td><%= result %></td></tr>
<%
}
%>
</table>
<p>USD成果(円)：<%= total %></p>


<a href="fxgraph.jsp">fxgraph</a>
</body>
</html>