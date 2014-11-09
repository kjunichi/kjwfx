package com.cocolog_nifty.kjunichi.kjwfx.ds;

import java.util.Date;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Fxrecord2 {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	@Persistent
	private boolean isCompleted;
	
	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	@Persistent
	private User author;

	@Persistent
	private String currency;

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Persistent
	private Date date;

	@Persistent
	private Float tts;

	@Persistent
	private Float amount;

	@Persistent
	private Date ttsDate;

	public Date getTtsDate() {
		return ttsDate;
	}

	public void setTtsDate(Date ttsDate) {
		this.ttsDate = ttsDate;
	}

	@Persistent
	private Float ttb;

	/**
	 * @param author
	 * @param date
	 * @param tts
	 * @param amount
	 */
	public Fxrecord2(User author, Date date, String currency, Float ttb,
			Float amount) {
		
		this.setAuthor(author);
		this.setDate(date);

		// 購入通貨種類
		this.setCurrency(currency);

		// 取引レート
		this.setTtb(ttb);
		// 取引額(購入した通貨で入力)
		this.setAmount(amount);
		//
		this.setCompleted(false);
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Float getTts() {
		return this.tts;
	}

	public void setTts(Float tts) {
		this.tts = tts;
	}

	public Float getAmount() {
		return this.amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public Float getTtb() {
		return this.ttb;
	}

	public void setTtb(Float ttb) {
		this.ttb = ttb;
	}

}
