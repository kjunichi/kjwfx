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
	 * doGet ���p�m�F��ʂ�\������
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//
		String id = req.getParameter("id");

		// �\�j�[�o���N�̃��[�g���擾����
		MoneyKit mkit = new MoneyKit();
		mkit.updateRate();

		// ID�����Ɉב֎�����擾����
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
		// �ʉ�
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
	 * doPost ���p�������
	 */
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if (user == null) {
			resp.sendRedirect(userService.createLoginURL("/kjwfx"));
		}
		// ���p��
		String dateStr = req.getParameter("date");
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// �ʉ�
		String currency = req.getParameter("currency");

		// tts
		String ttsStr = req.getParameter("tts");
		Float tts = Float.parseFloat(ttsStr);

		// �w�����z(�w�������ʉ݂ŋL��)
		String amountStr = req.getParameter("amount");
		Float amount = Float.parseFloat(amountStr);

		// ID
		String idStr = req.getParameter("id");

		// ID�����Ɉב֎�����擾����
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
			// ���ɍX�V�݂��邢�́A������̉\���B
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
