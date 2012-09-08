package com.cocolog_nifty.kjunichi.kjwfx;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.jdo.PersistenceManager;

import com.cocolog_nifty.kjunichi.kjwfx.ds.FxDayRate;
import com.cocolog_nifty.kjunichi.kjwfx.ds.FxRate;
import com.cocolog_nifty.kjunichi.kjwfx.ds.Fxrecord2;
import com.google.appengine.api.users.User;

/**
 * 最安値、最高値等計算するクラス
 * 
 * @author junichi
 * 
 */
public class FxCalc {

	/**
	 * 指定された日付の最高値を返す
	 * 
	 * @param date
	 * @return
	 */
	public static float getMaxTts(Date date) {

		return (float) 0.0;
	}

	public static float getEurMaxTts(String date) {
		List<FxRate> fxrecords = getFxrecords(date, "EUR");

		if (fxrecords.isEmpty()) {
			return (float) -9.0;
		}
		float maxTts = -1;

		for (FxRate g : fxrecords) {
			float tmpTts = g.getTts();
			if (tmpTts > maxTts) {
				maxTts = tmpTts;
			}
		}
		return (float) maxTts;
	}

	public static float getEurMinTts(String date) {
		List<FxRate> fxrecords = getFxrecords(date, "EUR");

		if (fxrecords.isEmpty()) {
			return (float) -9.0;
		}
		float minTts = Float.MAX_VALUE;

		for (FxRate g : fxrecords) {
			float tmpTts = g.getTts();
			if (tmpTts < minTts) {
				minTts = tmpTts;
			}
		}
		return (float) minTts;
	}

	/**
	 * USDのTTSの最高値を取得
	 * 指定された日付のUSDのTTSの最高値を取得する。
	 * 
	 * @param date
	 * @return
	 */
	public static float getUsdMaxTts(String date) {
		List<FxRate> fxrecords = getFxrecords(date, "USD");

		if (fxrecords.isEmpty()) {
			return (float) -9.0;
		}
		float maxTts = -1;

		for (FxRate g : fxrecords) {
			float tmpTts = g.getTts();
			if (tmpTts > maxTts) {
				maxTts = tmpTts;
			}
		}
		return (float) maxTts;
	}

	/**
	 * USDのTTSの最安値を取得
	 * 
	 * @param date
	 * @return
	 */
	public static float getUsdMinTts(String date) {
		List<FxRate> fxrecords = getFxrecords(date, "USD");

		if (fxrecords.isEmpty()) {
			return (float) -9.0;
		}
		float minTts = Float.MAX_VALUE;

		for (FxRate g : fxrecords) {
			float tmpTts = g.getTts();
			if (tmpTts < minTts) {
				minTts = tmpTts;
			}
		}
		return (float) minTts;
	}

	/**
	 * 指定された日付の指定された通貨（USD/EUR）の最高値、最安値を返す。
	 * 
	 * @param date
	 * @param currency
	 * @return
	 */
	public static MaxMinRate getMaxMinRate(Date date, String currency) {

		TimeZone.setDefault(TimeZone.getTimeZone("JST"));
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");

		List<FxRate> fxrecords = getFxrecords(sdf1.format(date), currency);

		// 最高値、最安値を調べる。
		MaxMinRate rate = new MaxMinRate();
		rate.setMaxTts(-1);
		rate.setMinTts(-1);
		rate.setMaxTtb(-1);
		rate.setMinTtb(-1);
		if (fxrecords.isEmpty()) {
			return rate;
		}

		// Min
		float minTts = Float.MAX_VALUE;
		for (FxRate g : fxrecords) {
			float tmpTts = g.getTts();
			if (tmpTts < minTts) {
				minTts = tmpTts;
			}
		}
		rate.setMinTts(minTts);
		float minTtb = Float.MAX_VALUE;
		for (FxRate g : fxrecords) {
			float tmpTtb = g.getTtb();
			if (tmpTtb < minTtb) {
				minTtb = tmpTtb;
			}
		}
		rate.setMinTtb(minTtb);

		// Max
		float maxTts = Float.MIN_VALUE;
		for (FxRate g : fxrecords) {
			float tmpTts = g.getTts();
			if (tmpTts > maxTts) {
				maxTts = tmpTts;
			}
		}
		rate.setMaxTts(maxTts);
		float maxTtb = Float.MIN_VALUE;
		for (FxRate g : fxrecords) {
			float tmpTtb = g.getTtb();
			if (tmpTtb > maxTtb) {
				maxTtb = tmpTtb;
			}
		}
		rate.setMaxTtb(maxTtb);

		return rate;
	}
	/**
	 * 指定された日付の指定された通貨（USD/EUR）の最高値、最安値を返す。
	 * 
	 * @param date
	 * @param currency
	 * @return
	 */
	public static List<FxDayRate> getMaxMinRateByDay(String bDate,String eDate, String currency) {

		TimeZone.setDefault(TimeZone.getTimeZone("JST"));
	
		List<FxDayRate> fxdayrates = getFxDayRate(bDate,eDate, currency);

		return fxdayrates;
	}
	/**
	 * getFxDayRate 日単位のレートを取得する
	 * @param date
	 * @param currency
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static List<FxDayRate> getFxDayRate(String bDate,String eDate, String currency) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String query = "SELECT FROM "
				+ FxDayRate.class.getName()
				+ " WHERE currency == curr && "
				+ " date >= beginDate && date <= endDate "
				+ " parameters String curr,String beginDate,String endDate  ";
		// +" ORDER BY ttb";
		String dispCurr = currency;
		return (List<FxDayRate>) pm.newQuery(query)
				.execute(dispCurr, bDate, eDate);
	}
	
	/**
	 * getFxrecords
	 * @param date
	 * @param currency
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static List<FxRate> getFxrecords(String date, String currency) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String query = "SELECT FROM "
				+ FxRate.class.getName()
				+ " WHERE currency == curr && "
				+ " date >= beginDate && date <= endDate "
				+ " parameters String curr,java.util.Date beginDate,java.util.Date endDate  ";
		// +" ORDER BY ttb";
		// JSTとして日付を設定する
		TimeZone.setDefault(TimeZone.getTimeZone("JST"));
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");
		Date bDate = null;
		try {
			bDate = sdf1.parse(date + "000000");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Date eDate = null;
		try {
			eDate = sdf1.parse(date + "235959");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String dispCurr = currency;
		return (List<FxRate>) pm.newQuery(query)
				.execute(dispCurr, bDate, eDate);
	}

	/**
	 * getFxDone 指定されたユーザの完了した取引記録を返す。
	 * 
	 * @param user
	 * @param currency
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<Fxrecord2> getFxDone(User user, String currency) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String query = "SELECT FROM "
				+ Fxrecord2.class.getName()
				+ " WHERE currency == curr && "
				+ " isCompleted == true && "
				+ " author == auth"
				+ " parameters String curr, com.google.appengine.api.users.User auth";
		return (List<Fxrecord2>) pm.newQuery(query).execute(currency, user);
	}

	private static final String DATE_PATTERN = "yyyyMMdd";

	/**
	 * getYesterdayRate
	 * 昨日のレートを取得する。
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getYesterdayRate() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		TimeZone.setDefault(TimeZone.getTimeZone("JST"));

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		Date yesterday = cal.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
		String yesterdayStr = sdf.format(yesterday);
		String query = "SELECT FROM " + FxDayRate.class.getName()
				+ " WHERE date == dateParam " + "parameters String dateParam";
		List<FxDayRate> list = (List<FxDayRate>) pm.newQuery(query).execute(
				yesterdayStr);
		if (list.isEmpty()) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for (FxDayRate rate : list) {
			sb.append(rate.getCurrency() + "の最安値(TTB)" + rate.getMinTtb()
					+ ",最高値（TTS）は" + rate.getMaxTts());
			sb.append("。");
		}
		return yesterdayStr + "Money Kitの" + sb.toString();
	}

	/**
	 * getYesterDayUsdTts
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<FxDayRate> getYesterdayRateList() {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		TimeZone.setDefault(TimeZone.getTimeZone("JST"));

		// 昨日の日付を取得する。
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		Date yesterday = cal.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
		String yesterdayStr = sdf.format(yesterday);
		String query = "SELECT FROM " + FxDayRate.class.getName()
				+ " WHERE date == dateParam " + "parameters String dateParam";
		List<FxDayRate> list = (List<FxDayRate>) pm.newQuery(query).execute(
				yesterdayStr);
		if (list.isEmpty()) {
			return null;
		}
		
		return list;
	}
}
