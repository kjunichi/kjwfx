<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.*" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="com.cocolog_nifty.kjunichi.kjwfx.ds.FxRate" %>
<%@ page import="com.cocolog_nifty.kjunichi.kjwfx.PMF" %>
<%@ page import="com.cocolog_nifty.kjunichi.kjwfx.FxCalc" %>
<%@ page import="com.cocolog_nifty.kjunichi.kjwfx.MaxMinRate" %>
<jsp:useBean id="fxListBean" class="com.cocolog_nifty.kjunichi.kjwfx.bean.FxListBean"
scope="request" />
<html>
  <body>
  <table border="1">
  <tr><th>Day</th><th>Ttb</th><th>Tts</th></tr>
<%
// ð“ú‚Ì“ú•t
TimeZone.setDefault(TimeZone.getTimeZone("JST"));

Calendar cal = Calendar.getInstance();
for(int n = 1; n < 14; n++) {;
	cal.add(Calendar.DATE, -1);
	Date day = cal.getTime();
	MaxMinRate rate = FxCalc.getMaxMinRate(day,"EUR"); 
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		
%>
<tr><td><%= sdf1.format(day) %></td><td><%= rate.getMinTtb() %></td><td><%= rate.getMaxTts() %></td></tr>
<%	
}	
%>
</table>
  </body>
</html>