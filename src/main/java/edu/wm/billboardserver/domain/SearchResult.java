package edu.wm.billboardserver.domain;

import java.util.ArrayList;

public class SearchResult {
	
	private String itemName;
	private String artist;
	private char type;
	public ArrayList<SearchResultEntry> entries;
	
	public SearchResult(String artist, String itemName, char type,	ArrayList<SearchResultEntry> entries) {
		super();
		this.artist = artist;
		this.itemName = itemName;
		this.type = type;
		this.entries = entries;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public ArrayList<SearchResultEntry> getEntries() {
		return entries;
	}
	
	public String getArtist() {
		return artist;
	}

	public void setEntries(ArrayList<SearchResultEntry> entries) {
		this.entries = entries;
	}

}
