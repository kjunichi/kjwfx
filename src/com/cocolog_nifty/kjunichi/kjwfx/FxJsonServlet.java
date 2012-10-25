package com.cocolog_nifty.kjunichi.kjwfx;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
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
public class FxJsonServlet extends HttpServlet {
	private static final String DATE_PATTERN = "yyyyMMdd";

	private static final int MAX_POINTS = 50;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 通貨種別を取得する。
		String currency = req.getParameter("currency");
		String startDate = req.getParameter("start_date");
		String endDate = req.getParameter("end_date");
		resp.setContentType("application/json; charset=UTF-8");
		resp.setHeader("Cache-Control", "private");

		if(startDate!=null && startDate.length()>1) {
		}else {

			TimeZone.setDefault(TimeZone.getTimeZone("JST"));

			Calendar cal = Calendar.getInstance();
			
			cal.add(Calendar.DATE, -1);
			Date day = cal.getTime();
			cal.add(Calendar.DATE, -14);
			Date eday = cal.getTime();
			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
			startDate = sdf1.format(eday);
			endDate = sdf1.format(day);
		}
		List<FxDayRate> rate = FxCalc.getMaxMinRateByDay(startDate,
				endDate, currency);
		PrintWriter pwriter = resp.getWriter();
		pwriter.println(req.getParameter("callback") + "({\"rate\":[");

		int n = 1;
		for (FxDayRate g : rate) {
			if (n != 1) {
				pwriter.println(",");
			}
			n++;
			pwriter.println("{");
			pwriter.println("\"day\":" + g.getDate() + ",");
			pwriter.println("\"minttb\":" + g.getMinTtb() + ",");
			pwriter.println("\"maxtts\":" + g.getMaxTts());
			pwriter.println("}");
		}
		pwriter.println("]});");
		pwriter.flush();

	}

}
