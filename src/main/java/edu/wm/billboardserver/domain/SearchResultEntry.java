package edu.wm.billboardserver.domain;

import java.util.Date;

public class SearchResultEntry {
	
	private int position;
	private Date date;
	
	public SearchResultEntry(int position, Date date) {
		super();
		this.position = position;
		this.date = date;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
