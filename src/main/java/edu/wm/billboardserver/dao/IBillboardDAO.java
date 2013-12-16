package edu.wm.billboardserver.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import edu.wm.billboardserver.domain.Chart;
import edu.wm.billboardserver.domain.SearchResult;

public interface IBillboardDAO {
	
	Chart getSinglesChart(Date date);
	Chart getAlbumsChart(Date date);
	ArrayList<SearchResult> reverseLookupArtist(String artist, Calendar startDate, Calendar endDate);
	SearchResult reverseLookupSong(String artist, String song, Calendar startDate, Calendar endDate);
	SearchResult reverseLookupAlbum(String artist, String album, Calendar startDate, Calendar endDate);

}
