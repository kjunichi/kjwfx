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
<jsp:useBean id="fxRecordBean" class="com.cocolog_nifty.kjunichi.kjwfx.bean.FxRecordBean"
scope="request" />
<html>
  <body>
<form action="/kjwfxsell" method="post">
<div>売却日<input type="text" name="date" value="<jsp:getProperty name="fxRecordBean" property="ttsDateStr"/>" />

</div>
    <div>tts<input type="text" name="tts" /></div>
    <div>売却金額<input type="text" name="amount" value="<jsp:getProperty name="fxRecordBean" property="amount"/>"/></div>
    <div>通貨<input type="text" name="currency" value="<jsp:getProperty name="fxRecordBean" property="currency"/>" /></div>
    <div>購入時のレート <jsp:getProperty name="fxRecordBean" property="ttb"/></div>
    
    <div><input type="submit" value="Sell FX" /></div>
    <div><input type="hidden" name="id" value="<jsp:getProperty name="fxRecordBean" property="id"/>" /></div>
  </form>

  </body>
</html>