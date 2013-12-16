package edu.wm.billboardserver.domain;

public class ChartEntry {

	private int position;
	private String artist;
	private String itemName;

	public ChartEntry(int position, String artist, String itemName) {
		super();
		this.position = position;
		this.artist = artist;
		this.itemName = itemName;
	}
	
	public int getPosition() {
		return position;
	}
	
	public void setPosition(int position) {
		this.position = position;
	}
	
	public String getArtist() {
		return artist;
	}
	
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	public String getItemName() {
		return itemName;
	}
	
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

}
