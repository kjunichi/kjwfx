package com.cocolog_nifty.kjunichi.kjwfx;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cocolog_nifty.kjunichi.kjwfx.ds.FxRate;
import com.cocolog_nifty.kjunichi.kjwfx.ds.Fxrecord2;

@SuppressWarnings("serial")
public class FxCheckServlet extends HttpServlet {
	private static final String DATE_PATTERN = "yyyyMMdd";

	@SuppressWarnings("unchecked")
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// ソニーバンクのレートを取得する
		MoneyKit mkit = new MoneyKit();
		mkit.updateRate();

		// 解析
		// 現在の売値を取得する。
		float eurCurrentTts = Float.parseFloat(mkit.getEurTts());
		float eurCurrentTtb = Float.parseFloat(mkit.getEurTtb());
		float usdCurrentTts = Float.parseFloat(mkit.getUsdTts());
		float usdCurrentTtb = Float.parseFloat(mkit.getUsdTtb());
		PersistenceManager pm = PMF.get().getPersistenceManager();
		checkEur(pm, eurCurrentTts, eurCurrentTtb);
		checkUsd(pm, usdCurrentTts, usdCurrentTtb);

		// 取得した為替レートを記録する。
		try {
			FxRate fxrate = new FxRate(new Date(), "EUR", new Float(
					mkit.getEurTtb()), new Float(mkit.getEurTts()));
			pm.makePersistent(fxrate);

			fxrate = new FxRate(new Date(), "USD", new Float(mkit.getUsdTtb()),
					new Float(mkit.getUsdTts()));
			pm.makePersistent(fxrate);
		} finally {
			pm.close();
		}

		resp.getWriter().print("text/plain\n\n");
		resp.getWriter().print("eurTts = " + mkit.getEurTts());
		resp.getWriter().print("eurTtb = " + mkit.getEurTtb());
		// resp.getWriter().print("isMail = " + isMail);
		// resp.getWriter().print("mailErrorMsg = " + mailErrorMsg);
	}

	/**
	 * checkEurSell EURのレートが買い時の場合、アラートメールを送信する。
	 * 
	 * @param eurCurrnetTts
	 */
	@SuppressWarnings("unchecked")
	void checkEurSell(PersistenceManager pm, float eurCurrnetTts) {

		String queryStr = "SELECT FROM " + Fxrecord2.class.getName()
				+ " WHERE " + "isCompleted == isCompletedParam" + " && "
				+ "currency == currencyParam " + " ORDER BY ttb asc ";
		Query query = pm.newQuery(queryStr);
		query.declareParameters("Boolean isCompletedParam,String currencyParam");

		List<Fxrecord2> fxrecords = (List<Fxrecord2>) pm.newQuery(query)
				.execute(false, "EUR");
		boolean isMail = false;

		if (fxrecords.isEmpty()) {

		} else {
			Logger logger = Logger.getLogger(this.getClass().getName());
			for (Fxrecord2 g : fxrecords) {
				if (isMail) {
					break;
				}

				// 買ったときのレートを取得する。
				Float eurTtb = g.getTtb();
				if (eurTtb == null) {
					continue;
				}

				// 購入時の日本円を求める。（単位は円）
				float beforeEur = g.getAmount() * eurTtb;
				// 今回売却した場合の金額を求める。(単位は円)
				float afterEur = g.getAmount() * eurCurrnetTts;

				if (afterEur > beforeEur) {
					logger.warning("beforeEur = " + beforeEur);
					logger.warning("afterEur = " + afterEur);
				}

				if (afterEur - beforeEur > 200) {
					// 売値が買値より一定金額上だったら、アラートメールを送信する。
					isMail = true;

					// アラートメールを送信する。
					Mail.sendSellAlert(g.getAuthor().getEmail(),
							eurTtb.toString(),
							(new Float(eurCurrnetTts)).toString(), "EUR");
				}
			}
		}
	}

	/**
	 * checkEurBuy EURのレートが売りの場合、アラートメールを送信する。
	 * 
	 * @param pm
	 * @param eurCurrentTtb
	 */
	@SuppressWarnings("unchecked")
	void checkEurBuy(PersistenceManager pm, float eurCurrentTtb) {

		String queryStr = "SELECT FROM " + Fxrecord2.class.getName()
				+ " WHERE isCompleted == isCompletedParam " + " && "
				+ " currency == currencyParam " + " ORDER BY ttb";
		Query query2 = pm.newQuery(queryStr);
		query2.declareParameters("Boolean isCompletedParam,String currencyParam");
		query2.setRange(0, 1);
		List<Fxrecord2> fxrecords = (List<Fxrecord2>) query2.execute(false,
				"EUR");
		if (fxrecords.isEmpty()) {

		} else {
			Boolean isMail = false;
			for (Fxrecord2 g : fxrecords) {
				if (isMail) {
					// 既にメールを送信していたら終了する。
					break;
				}
				// 買値を取得する。
				Float eurTtb = g.getTtb();
				if (eurTtb == null) {
					continue;
				}
				Logger logger = Logger.getLogger(this.getClass().getName());
				logger.warning("eurTtb " + eurTtb);
				logger.warning("eurCurrnetTtb "
						+ (new Float(eurCurrentTtb)).toString());

				// if (eurTtb - eurTts > 20.5) {
				if (eurTtb > eurCurrentTtb + 1.5) {
					// 買値が前回の買値より一定金額以下だったら、アラートメールを送信する。
					isMail = true;

					// アラートメールを送信する。
					Mail.sendBuyAlert(g.getAuthor().getEmail(),
							eurTtb.toString(),
							(new Float(eurCurrentTtb)).toString(), "EUR");
				}
			}
		}
	}

	/**
	 * checkEur
	 * 
	 * @param pm
	 * @param eurCurrnetTts
	 * @param eurCurrentTtb
	 */
	void checkEur(PersistenceManager pm, float eurCurrnetTts,
			float eurCurrentTtb) {
		checkEurSell(pm, eurCurrnetTts);
		checkEurBuy(pm, eurCurrentTtb);
	}

	/**
	 * checkUsdSell
	 * 
	 * @param pm
	 * @param usdCurrnetTts
	 */
	@SuppressWarnings("unchecked")
	void checkUsdSell(PersistenceManager pm, float usdCurrnetTts) {

		String queryStr = "SELECT FROM " + Fxrecord2.class.getName()
				+ " WHERE " + "isCompleted == isCompletedParam" + " && "
				+ "currency == currencyParam " + " ORDER BY ttb asc ";
		Query query = pm.newQuery(queryStr);
		query.declareParameters("Boolean isCompletedParam,String currencyParam");

		List<Fxrecord2> fxrecords = (List<Fxrecord2>) pm.newQuery(query)
				.execute(false, "USD");
		boolean isMail = false;

		if (fxrecords.isEmpty()) {

		} else {

			for (Fxrecord2 g : fxrecords) {
				if (isMail) {
					break;
				}

				// 買ったときの値段を取得する。
				Float usdTtb = g.getTtb();
				if (usdTtb == null) {
					continue;
				}

				if (usdTtb + 2.5 < usdCurrnetTts) {
					// 売値が買値より一定金額上だったら、アラートメールを送信する。
					isMail = true;

					// アラートメールを送信する。
					Mail.sendSellAlert(g.getAuthor().getEmail(),
							usdTtb.toString(),
							(new Float(usdCurrnetTts)).toString(), "USD");
				}
			}
		}
	}

	/**
	 * checkEurBuy USDのレートが売りの場合、アラートメールを送信する。
	 * 
	 * @param pm
	 * @param usdCurrentTtb
	 */
	@SuppressWarnings("unchecked")
	void checkUsdBuy(PersistenceManager pm, float usdCurrentTtb) {

		String queryStr = "SELECT FROM " + Fxrecord2.class.getName()
				+ " WHERE isCompleted == isCompletedParam " + " && "
				+ " currency == currencyParam " + " ORDER BY ttb";
		Query query2 = pm.newQuery(queryStr);
		query2.declareParameters("Boolean isCompletedParam,String currencyParam");
		query2.setRange(0, 1);
		List<Fxrecord2> fxrecords = (List<Fxrecord2>) query2.execute(false,
				"USD");
		if (fxrecords.isEmpty()) {

		} else {
			Boolean isMail = false;
			for (Fxrecord2 g : fxrecords) {
				if (isMail) {
					// 既にメールを送信していたら終了する。
					break;
				}
				// 買値を取得する。
				Float usdTtb = g.getTtb();
				if (usdTtb == null) {
					continue;
				}
				Logger logger = Logger.getLogger(this.getClass().getName());
				logger.warning("usdTtb " + usdTtb);
				logger.warning("usdCurrnetTtb "
						+ (new Float(usdCurrentTtb)).toString());

				// if (eurTtb - eurTts > 20.5) {
				if (usdTtb > usdCurrentTtb + 1.5) {
					// 買値が前回の買値より一定金額以下だったら、アラートメールを送信する。
					isMail = true;

					// アラートメールを送信する。
					Mail.sendBuyAlert(g.getAuthor().getEmail(),
							usdTtb.toString(),
							(new Float(usdCurrentTtb)).toString(), "USD");
				}
			}
		}
	}

	/**
	 * checkUsd
	 * 
	 * @param pm
	 * @param usdCurrnetTts
	 * @param usdCurrentTtb
	 */
	void checkUsd(PersistenceManager pm, float usdCurrnetTts,
			float usdCurrentTtb) {
		checkUsdSell(pm, usdCurrnetTts);
		checkUsdBuy(pm, usdCurrentTtb);
	}
}
