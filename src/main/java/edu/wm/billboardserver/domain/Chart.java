package edu.wm.billboardserver.domain;

import java.util.ArrayList;
import java.util.Date;

public class Chart {
	
	private Date date;
	private char type;
	private ArrayList<ChartEntry> data;
		
	public Chart(Date date, char type, ArrayList<ChartEntry> data) {
		super();
		this.date = date;
		this.type = type;
		this.data = data;
	}

	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public char getType() {
		return type;
	}
	
	public void setType(char type) {
		this.type = type;
	}
	
	public ArrayList<ChartEntry> getData() {
		return data;
	}
	
	public void setData(ArrayList<ChartEntry> data) {
		this.data = data;
	}
	

}
