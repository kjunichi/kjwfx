package com.cocolog_nifty.kjunichi.kjwfx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
/**
 * MoneyKit ソニーバンクのレートをチェックする
 * @author junichi
 *
 */
public class MoneyKit {
	private static final String MONEYKIT_RATE_URL = "http://moneykit.net/data/rate/NBP64F320.js";

	final static int EUR_INDEX = 1;
	final static int USD_INDEX = 0;
	
	private String[] tts = new String[2];
	private String[] ttb = new String[2];

	public String[] getTts() {
		return this.tts;
	}

	public String getEurTts() {
		return this.tts[EUR_INDEX];
	}
	public String getEurTtb() {
		return this.ttb[EUR_INDEX];
	}
	public String getUsdTts() {
		return this.tts[USD_INDEX];
	}
	public String getUsdTtb() {
		return this.ttb[USD_INDEX];
	}
	public void updateRate() throws IOException {
		URL url = new URL(MONEYKIT_RATE_URL);
		URLConnection ucon = url.openConnection();
		InputStream istream = ucon.getInputStream();
		InputStreamReader isr = new InputStreamReader(istream, "utf-8");
		BufferedReader br = new BufferedReader(isr);

		String line = "";
		int rowNum = 0;
		String buf = "";
		while ((line = br.readLine()) != null) {
			if (rowNum > 0) {
				// レートデータの場合
			} else {
				// buf = line;
				String[] rates = line.split("';");
				for (int i = 0; i < rates.length; i++) {
					buf = buf + i + ":" + rates[i] + "<br>";
				}
				// ';で分離
				// ApplyDate
				String applyDateStr = rates[0].split("='")[1];
				String applyTimeStr = rates[1].split("='")[1];

				// 現在時刻と大きく違いが無いかをチェック

				// USD
				String usdRate = rates[19].split("='")[1];
				this.ttb[USD_INDEX]=usdRate.split(";")[2];
				this.tts[USD_INDEX]=usdRate.split(";")[3];
				
				// EUR
				String eurRate = rates[11].split("='")[1];
				// EUR ttb
				this.ttb[EUR_INDEX] = eurRate.split(";")[2];
				// EUR tts
				this.tts[EUR_INDEX] = eurRate.split(";")[3];		
			}
			rowNum++;
		}
	}
}
