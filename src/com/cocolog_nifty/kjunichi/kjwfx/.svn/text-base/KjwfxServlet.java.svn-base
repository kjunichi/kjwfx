package com.cocolog_nifty.kjunichi.kjwfx;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cocolog_nifty.kjunichi.kjwfx.bean.FxListBean;
import com.cocolog_nifty.kjunichi.kjwfx.ds.Fxrecord2;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class KjwfxServlet extends HttpServlet {
	private static final String DATE_PATTERN = "yyyyMMdd";
	private static final String MONEYKIT_RATE_URL = "http://moneykit.net/data/rate/NBP64F320.js";

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// ソニーバンクのレートを取得する
		MoneyKit mkit = new MoneyKit();
		mkit.updateRate();
		
		// 解析
		//
		FxListBean fxListBean = new FxListBean();
		fxListBean.setRateInfo(mkit.getEurTts()+","+mkit.getUsdTts());
		req.setAttribute("fxListBean", fxListBean);
		RequestDispatcher rd = req.getRequestDispatcher("/jsp/fxlist.jsp");
		rd.forward(req, resp);

	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException,IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		// 購入日
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
		String ttbStr = req.getParameter("ttb");
		Float ttb = Float.parseFloat(ttbStr);

		// 購入金額(購入した通貨で記入)
		String amountStr = req.getParameter("amount");
		Float amount = Float.parseFloat(amountStr);

		Fxrecord2 fxrecord = new Fxrecord2(user, date, currency, ttb, amount);

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(fxrecord);
		} finally {
			pm.close();
		}

		RequestDispatcher rd = req.getRequestDispatcher("/jsp/fxlist.jsp");
		rd.forward(req, resp);
	}
}
