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

<!--  昨日の最高値、最安値を求める -->
<%
String checkDate = request.getParameter("date");
if(checkDate == null) {
checkDate="20100911";
}
//入力されていたら、その日付で最高値を取得する。
float maxtts = FxCalc.getEurMaxTts(checkDate);
String maxTtsStr = Float.toString(maxtts);

float mintts = FxCalc.getEurMinTts(checkDate);
String minTtsStr = Float.toString(mintts);


float maxUsdtts = FxCalc.getUsdMaxTts(checkDate);
float minUsdtts = FxCalc.getUsdMinTts(checkDate);
String maxUsdTtsStr = Float.toString(maxUsdtts);
String minUsdTtsStr = Float.toString(minUsdtts);
%>
EUR TTS <%= maxTtsStr %> / <%= minTtsStr %><br/>
USD TTS <%= maxUsdTtsStr %> / <%= minUsdTtsStr %><br />

<form action="fxtanuki.jsp" method="post">
<div>購入日<input type="text" name="date" value="<%= checkDate %>" /></div>
    <div><input type="submit" value="確認" /></div>
  </form>


 
  </body>
</html>