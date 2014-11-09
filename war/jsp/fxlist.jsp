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
<jsp:useBean id="fxListBean" class="com.cocolog_nifty.kjunichi.kjwfx.bean.FxListBean"
scope="request" />
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
</head>
  <body>
  <a href="/jsp/fxgraph.jsp">グラフ</a>
<jsp:getProperty name="fxListBean" property="rateInfo"/>
<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
%>
<p>Hello, <%= user.getNickname() %>! (You can
<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">sign out</a>.)</p>
<%
    } else {
%>
<p>Hello!
<a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Sign in</a>
to include your name with greetings you post.</p>
<%
    }
%>
<form action="/kjwfx" method="post">
<div>購入日<input type="text" name="date" /></div>
<div>通貨<input type="text" name="currency" /></div>
    <div>ttb<input type="text" name="ttb" /></div>
    <div>購入金額<input type="text" name="amount" /></div>
    <div><input type="submit" value="Post FX" /></div>
  </form>
<%
    PersistenceManager pm = PMF.get().getPersistenceManager();
    String query = "SELECT FROM " + Fxrecord2.class.getName() 
     +" WHERE isCompleted == isCompletedParam "
     +" parameters Boolean isCompletedParam  "
     +" ORDER BY ttb";
    // +" ORDER BY ttb";
    List<Fxrecord2> fxrecords = (List<Fxrecord2>) pm.newQuery(query).execute(false);
    if (fxrecords.isEmpty()) {
} else {
SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
%>
<table border="1">
<%
 for (Fxrecord2 g : fxrecords) {
 
 %>
 <tr><td><%= sdf1.format(g.getDate())%></td><td><%= g.getCurrency()%></td><td><%= g.getTtb()%></td><td><%= g.getAmount()%>
 </td><td><%= g.isCompleted() %></td><td>
 <form method="GET" action="/kjwfxsell">
  <input type="hidden" name="id" value="<%= g.getId() %>">
 <input type="submit" value="Sell"></form></td></tr>
 <%
 }
}
pm.close();
%>
   
    </table>
  </body>
</html>