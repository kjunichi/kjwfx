<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="java.util.List" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="com.cocolog_nifty.kjunichi.kjwfx.Fxrecord" %>
<%@ page import="com.cocolog_nifty.kjunichi.kjwfx.Fxrecord2" %>
<%@ page import="com.cocolog_nifty.kjunichi.kjwfx.PMF" %>
<jsp:useBean id="fxListBean" class="com.cocolog_nifty.kjunichi.kjwfx.bean.FxListBean"
scope="request" />
<html>
  <body>
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
    String query = "SELECT FROM " + Fxrecord.class.getName() +" ORDER BY ttb";
    List<Fxrecord> fxrecords = (List<Fxrecord>) pm.newQuery(query).execute();
    if (fxrecords.isEmpty()) {

%>
<%
} else {
 for (Fxrecord g : fxrecords) {
 Fxrecord2 rec = new Fxrecord2(
  g.getAuthor(),g.getDate(), g.getCurrency(),g.getTtb(),
			g.getAmount());
			try {
			pm.makePersistent(rec);
		} finally {
			//pm.close();
		}
 
 if (g.getTts() == null) {
 %>
 <p><b><%= g.getDate()%></b><%= g.getCurrency()%> <%= g.getTtb()%> <%= g.getAmount()%></p>
 <%
 			
 }
}
}
pm.close();
%>
    <%
    //http://moneykit.net/visitor/sb_rate/index.html
    %>
  </body>
</html>