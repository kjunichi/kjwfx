package com.cocolog_nifty.kjunichi.kjwfx;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cocolog_nifty.kjunichi.kjwfx.ds.FxDayRate;

/**
 * FxUpdateRateRecordServlet 動かした前日の最安値、最高値を記録する。
 * 
 * @author junichi
 * 
 */
@SuppressWarnings("serial")
public class FxUpdateRateRecordServlet extends HttpServlet {
	private static final String DATE_PATTERN = "yyyyMMdd";

	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Logger logger = Logger.getLogger("FxUpdateRateRecordServlet");

		// 前日の日付を算出する。
		TimeZone.setDefault(TimeZone.getTimeZone("JST"));

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		Date yesterday = cal.getTime();

		// 前日のレートを取得する。
		MaxMinRate rateUsd;
		rateUsd = FxCalc.getMaxMinRate(yesterday, "USD");
		MaxMinRate rateEur;
		rateEur = FxCalc.getMaxMinRate(yesterday, "EUR");
		// 取得した最高値、最安値を記録する。
		TimeZone.setDefault(TimeZone.getTimeZone("JST"));
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");

		PersistenceManager pm = PMF.get().getPersistenceManager();
		resp.getWriter().print("text/plain\n\n");
		try {
			FxDayRate fxDayRate = new FxDayRate(sdf1.format(yesterday), "USD",
					new Float(rateUsd.getMinTtb()), new Float(rateUsd
							.getMaxTtb()), new Float(rateUsd.getMinTts()),
					new Float(rateUsd.getMaxTts()));
			pm.makePersistent(fxDayRate);

			fxDayRate = new FxDayRate(sdf1.format(yesterday), "EUR", new Float(
					rateEur.getMinTtb()), new Float(rateEur.getMaxTtb()),
					new Float(rateEur.getMinTts()), new Float(rateEur
							.getMaxTts()));
			pm.makePersistent(fxDayRate);
		}catch(Exception e){
			resp.getWriter().print("e = " + e.getMessage() + "\n");
		}
		finally {
			pm.close();
		}

		pm = PMF.get().getPersistenceManager();
		String query = "SELECT FROM " + FxDayRate.class.getName();
		// +
		// " parameters String curr,java.util.Date beginDate,java.util.Date endDate  ";
		List<FxDayRate> drate = (List<FxDayRate>) pm.newQuery(query).execute();
		
		
		for (FxDayRate r : drate) {
			resp.getWriter().print("date = " + r.getDate() + "\n");
		}
		resp.getWriter().print("yesterday = " + yesterday + "\n");
		resp.getWriter().print("MaxTts = " + rateUsd.getMaxTts() + "\n");
		resp.getWriter().print("MaxTtb = " + rateUsd.getMaxTtb() + "\n");
		resp.getWriter().print("MinTts = " + rateUsd.getMinTts() + "\n");
		resp.getWriter().print("MinTtb = " + rateUsd.getMinTtb() + "\n");
		resp.getWriter().print("MaxEURTts = " + rateEur.getMaxTts() + "\n");
		resp.getWriter().print("MaxEURTtb = " + rateEur.getMaxTtb() + "\n");
		resp.getWriter().print("MinEURTts = " + rateEur.getMinTts() + "\n");
		resp.getWriter().print("MinEURTtb = " + rateEur.getMinTtb() + "\n");

	}
}
