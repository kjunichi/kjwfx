<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.*" %>
<%@ page import="javax.jdo.PersistenceManager" %>
<%@ page import="javax.jdo.Query" %>
<%@ page import="com.cocolog_nifty.kjunichi.kjwfx.ds.FxRate" %>
<%@ page import="com.cocolog_nifty.kjunichi.kjwfx.PMF" %>
<%@ page import="com.cocolog_nifty.kjunichi.kjwfx.FxCalc" %>
<%@ page import="com.cocolog_nifty.kjunichi.kjwfx.MaxMinRate" %>
<%@ page import="com.cocolog_nifty.kjunichi.kjwfx.ds.FxRate" %>

<jsp:useBean id="fxListBean" class="com.cocolog_nifty.kjunichi.kjwfx.bean.FxListBean"
scope="request" />
<html>
  <body>
  <table border="1">
  <tr><th>Day</th><th>Ttb</th><th>Tts</th></tr>
<%
final String DATE_PATTERN = "yyyyMMdd";
final int MAX_POINTS = 500;

PersistenceManager pm = PMF.get().getPersistenceManager();

// 日付
TimeZone.setDefault(TimeZone.getTimeZone("JST"));



String queryStr = "SELECT FROM " + FxRate.class.getName()
				+ " WHERE date < dateParam "
				+ " parameters String dateParam  " + " ORDER BY date";
Query query = pm.newQuery(queryStr);

// 取得するレコード数の設定
query.setRange(0, MAX_POINTS);
@SuppressWarnings("unchecked")
Calendar cal = Calendar.getInstance();
cal.add(Calendar.DATE, -365*6);
Date day = cal.getTime();

SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
List<FxRate> fxrates = (List<FxRate>) query.execute(sdf1.format(day));
DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
List<Key> keys = new ArrayList<Key>();
for (FxRate r : fxrates) {
	%>
<tr><td><%= r.getId() %></td><td><%= r.getDate() %></td></tr>

<%	
  Key key = KeyFactory.createKey("FxRate", r.getId());
  keys.add(key);
  //datastore.delete(key);
}
datastore.delete(keys);
	
%>
</table>
  </body>
</html>