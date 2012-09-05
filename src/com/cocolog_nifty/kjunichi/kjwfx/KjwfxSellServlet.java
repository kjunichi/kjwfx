package com.cocolog_nifty.kjunichi.kjwfx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.cocolog_nifty.kjunichi.kjwfx.bean.FxListBean;
import com.cocolog_nifty.kjunichi.kjwfx.bean.FxRecordBean;
import com.cocolog_nifty.kjunichi.kjwfx.ds.Fxrecord2;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class KjwfxSellServlet extends HttpServlet {
	private static final String DATE_PATTERN = "yyyyMMdd";
	private static final String MONEYKIT_RATE_URL = "http://moneykit.net/data/rate/NBP64F320.js";

	/**
	 * doGet 売却確認画面を表示する
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//
		String id = req.getParameter("id");

		// ソニーバンクのレートを取得する
		MoneyKit mkit = new MoneyKit();
		mkit.updateRate();

		// IDを元に為替取引を取得する
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String queryStr = "SELECT FROM " + Fxrecord2.class.getName()
				+ " WHERE id == idParam ";

		Query query = pm.newQuery(queryStr);
		query.declareParameters("Long idParam");
		List<Fxrecord2> fxrecords = (List<Fxrecord2>) query
				.execute(new Long(id));
		Fxrecord2 bean = null;
		if (fxrecords.isEmpty()) {

		} else {
			for (Fxrecord2 g : fxrecords) {
				bean = g;
			}
		}
		FxRecordBean frb = new FxRecordBean();
		// ID
		frb.setId(bean.getId());
		// 通貨
		frb.setCurrency(bean.getCurrency());

		frb.setAmount(bean.getAmount());
		frb.setTtb(bean.getTtb());

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
		frb.setTtsDateStr(sdf1.format(new Date()));

		//
		req.setAttribute("fxRecordBean", frb);
		RequestDispatcher rd = req.getRequestDispatcher("/jsp/fxDetail.jsp");
		rd.forward(req, resp);

	}

	/**
	 * doPost 売却完了画面
	 */
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user == null) {
			resp.sendRedirect(userService.createLoginURL("/kjwfx"));
		}
		// 売却日
		String dateStr = req.getParameter("date");
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 通貨
		String currency = req.getParameter("currency");

		// tts
		String ttsStr = req.getParameter("tts");
		Float tts = Float.parseFloat(ttsStr);

		// 購入金額(購入した通貨で記入)
		String amountStr = req.getParameter("amount");
		Float amount = Float.parseFloat(amountStr);

		// ID
		String idStr = req.getParameter("id");

		// IDを元に為替取引を取得する
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String queryStr = "SELECT FROM " + Fxrecord2.class.getName()
				+ " WHERE id == idParam && isCompleted == isCompletedParam ";
		// + " && author == authorParam ";
		Query query = pm.newQuery(queryStr);
		query.declareParameters("Long idParam,Boolean isCompletedParam");
		List<Fxrecord2> fxrecords = (List<Fxrecord2>) query.execute(new Long(
				idStr), false);
		Fxrecord2 bean = null;
		if (fxrecords.isEmpty()) {
			// 既に更新みあるいは、改ざんの可能性。
		} else {
			for (Fxrecord2 g : fxrecords) {
				if (g.getAuthor().equals(user)) {
					bean = g;
					bean.setTts(tts);
					bean.setTtsDate(date);
					bean.setCompleted(true);

					pm.makePersistent(bean);
				}
			}

			pm.close();

		}

		resp.sendRedirect("/kjwfx");
	}
}
