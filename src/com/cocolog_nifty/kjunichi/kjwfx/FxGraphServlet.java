package com.cocolog_nifty.kjunichi.kjwfx;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cocolog_nifty.kjunichi.kjwfx.ds.FxDayRate;
import com.cocolog_nifty.kjunichi.kjwfx.ds.FxRate;

/**
 * FxGraphServlet
 * 
 * @author junichi
 * 
 */
@SuppressWarnings("serial")
public class FxGraphServlet extends HttpServlet {
	private static final String DATE_PATTERN = "yyyyMMdd";

	private static final int MAX_POINTS = 50;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 通貨種別を取得する。
		String currency = req.getParameter("currency");

		PersistenceManager pm = PMF.get().getPersistenceManager();
		// 指定された通貨の為替レートを取得する。
		String queryStr = "SELECT FROM " + FxRate.class.getName()
				+ " WHERE currency == currencyParam "
				+ " parameters String currencyParam  " + " ORDER BY date desc";
		Query query = pm.newQuery(queryStr);
		// 取得するレコード数の設定
		query.setRange(0, MAX_POINTS);
		@SuppressWarnings("unchecked")
		List<FxRate> fxrates = (List<FxRate>) query.execute(currency);

		// 昨日のTTS,TTBを取得する
		Float yUsdTts = null;
		Float yUsdTtb = null;
		Float yEurTts = null;
		Float yEurTtb = null;

		List<FxDayRate> dayRateList = FxCalc.getYesterdayRateList();
		for (FxDayRate rate : dayRateList) {
			if (rate.getCurrency().equals("USD")) {
				// USD
				yUsdTts = rate.getMaxTts();
				yUsdTtb = rate.getMinTtb();
			}
			if (rate.getCurrency().equals("EUR")) {
				// EUR
				yEurTts = rate.getMaxTts();
				yEurTtb = rate.getMinTtb();
			}
		}

		if (fxrates.isEmpty()) {
			return;
		} else {
			resp.setContentType("text/javascript");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter()
					.println(
							"google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});");

			resp.getWriter().println("google.setOnLoadCallback(drawChart);");
			resp.getWriter().println("function drawChart() {");
			resp.getWriter().println(
					"var data = new google.visualization.DataTable();");

			// グラフの設定を行う。
			resp.getWriter().println("data.addColumn('string', 'Date');");
			resp.getWriter().println(
					"data.addColumn('number', '" + currency + " Tts');");
			resp.getWriter().println(
					"data.addColumn('number', '" + currency + " Ttb');");
			resp.getWriter().println("data.addColumn('number', '昨日のTTS');");
			resp.getWriter().println("data.addColumn('number', '昨日のTTB');");
			resp.getWriter().println("data.addRows(" + MAX_POINTS + ");");

			int pcount = MAX_POINTS - 1;

			// 日本時間で表示する。
			TimeZone.setDefault(TimeZone.getTimeZone("JST"));

			// レートをプロットする。
			for (FxRate r : fxrates) {
				Date jstDate = r.getDate();
				resp.getWriter().println(
						" data.setValue(" + pcount + ",0,'" + jstDate + "');");
				resp.getWriter().println(
						" data.setValue(" + pcount + ",1," + r.getTts() + ");");

				resp.getWriter().println(
						" data.setValue(" + pcount + ",2," + r.getTtb() + ");");

				if (r.getCurrency().equals("USD")) {

					resp.getWriter()
							.println(
									" data.setValue(" + pcount + ",3,"
											+ yUsdTts + ");");
					resp.getWriter()
							.println(
									" data.setValue(" + pcount + ",4,"
											+ yUsdTtb + ");");
				}
				if (r.getCurrency().equals("EUR")) {
					resp.getWriter()
							.println(
									" data.setValue(" + pcount + ",3,"
											+ yEurTts + ");");
					resp.getWriter()
							.println(
									" data.setValue(" + pcount + ",4,"
											+ yEurTtb + ");");
				}
				pcount--;
			}
			resp.getWriter().println(
					"var chart = new google.visualization.LineChart(document.getElementById('"
							+ currency + "_chart_div'));");
			resp.getWriter().println(
					"chart.draw(data, {width: 600, height: 384, title: '"
							+ currency + " Performance'});");
			resp.getWriter().println(" }");
		}

	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		RequestDispatcher rd = req.getRequestDispatcher("/jsp/fxlist.jsp");
		rd.forward(req, resp);
	}
}
