package edu.wm.billboardserver.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.el.parser.ParseException;

import edu.wm.billboardserver.HomeController;
import edu.wm.billboardserver.domain.Chart;
import edu.wm.billboardserver.domain.ChartEntry;
import edu.wm.billboardserver.domain.SearchResult;
import edu.wm.billboardserver.domain.SearchResultEntry;

public class PostgresBillboardDAO extends PostgresDAO implements IBillboardDAO {
	
	@Override
	public Chart getSinglesChart(Date date) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		DateFormat df = new SimpleDateFormat("MMddyyyy");
		Connection conn = establishConnection();		
			
		ResultSet r = execQuery(conn, "select * from singles_" + df.format(date) + " order by position;");
		ArrayList<ChartEntry> chartData = new ArrayList<ChartEntry>();

		try {
			
			while (r.next()) {
				chartData.add(new ChartEntry(r.getInt("position"), r.getString("artist"), r.getString("song")));
			}
			
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		try { 
			if (conn != null) 
				conn.close();
		} 
		catch (Exception e2) {};
			
		return new Chart(date, 's', chartData);
			
	}

	@Override
	public Chart getAlbumsChart(Date date) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		DateFormat df = new SimpleDateFormat("MMddyyyy");
		Connection conn = establishConnection();		
			
		ResultSet r = execQuery(conn, "select * from albums_" + df.format(date) + " order by position;");
		ArrayList<ChartEntry> chartData = new ArrayList<ChartEntry>();

		try {
			
			while (r.next()) {
				chartData.add(new ChartEntry(r.getInt("position"), r.getString("artist"), r.getString("album")));
			}
			
		}
		catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		try { 
			if (conn != null) 
				conn.close();
		} 
		catch (Exception e2) {};
			
		return new Chart(date, 'a', chartData);
		
	}

	@Override
	public SearchResult reverseLookupSong(String artist, String song, Calendar startDate, Calendar endDate) {

		Calendar cal = startDate;
		ArrayList<SearchResultEntry> entries = new ArrayList<SearchResultEntry>();
		Connection conn = establishConnection();
		DateFormat df = new SimpleDateFormat("MMddyyyy");
			
		while (!cal.after(endDate)) {
				
			ResultSet r = execQuery(conn, "select * from singles_" + df.format(cal.getTime()) + " where "
					+ "lower(artist)=lower('" + artist + "') and lower(song)=lower('" + song + "');");
				
			try {
			
				if (r.next()) 
					entries.add(new SearchResultEntry(r.getInt("position"), cal.getTime()));
				
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
				
			cal.add(Calendar.DATE, 7);
		}
		
		try { 
			if (conn != null) 
				conn.close();
		} 
		catch (Exception e2) {};
		
		if (entries.size() > 0)
			return new SearchResult(artist, song, 's', entries);
		else
			return null;

	}

	@Override
	public SearchResult reverseLookupAlbum(String artist, String album,	Calendar startDate, Calendar endDate) {

		Calendar cal = startDate;
		ArrayList<SearchResultEntry> entries = new ArrayList<SearchResultEntry>();
		Connection conn = establishConnection();
		
		DateFormat df = new SimpleDateFormat("MMddyyyy");
			
		while (!cal.after(endDate)) {
			
			ResultSet r = execQuery(conn, "select * from albums_" + df.format(cal.getTime()) + " where "
					+ "lower(artist)=lower('" + artist + "') and lower(album)=lower('" + album + "');");
				
			try {
			
				if (r.next()) 
					entries.add(new SearchResultEntry(r.getInt("position"), cal.getTime()));
				
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
				
			cal.add(Calendar.DATE, 7);
		}
		
		try { 
			if (conn != null) 
				conn.close();
		} 
		catch (Exception e2) {};
			
		if (entries.size() > 0)
			return new SearchResult(artist, album, 'a', entries);
		else
			return null;
		
	}

	@Override
	public ArrayList<SearchResult> reverseLookupArtist(String artist, Calendar startDate, Calendar endDate) {
		
		long startTime = System.nanoTime();

		Calendar cal = startDate;
		Calendar albumsStartDate = Calendar.getInstance();
		albumsStartDate.set(1970, 11, 19);
		ArrayList<SearchResult> searchResults = new ArrayList<SearchResult>();
		ArrayList<SearchResultEntry> entries = new ArrayList<SearchResultEntry>();
		Connection conn = establishConnection();
		
		DateFormat df = new SimpleDateFormat("MMddyyyy");
			
		while (!cal.after(endDate)) {
			
			//Song search
				
			ResultSet r = execQuery(conn, "select * from singles_" + df.format(cal.getTime()) + " where "
					+ "lower(artist)=lower('" + artist + "');");
				
			try {
			
				while (r.next())  {
					String songName = r.getString("song");
					int position = r.getInt("position");
					boolean foundDuplicateResult = false;
					SearchResultEntry newEntry = new SearchResultEntry(position, cal.getTime());
					
					System.out.println("Chart result found: " + df.format(cal.getTime()) + " " + songName);
					
					for (int i = 0; i < searchResults.size(); i++) {
						
						SearchResult currentResult = searchResults.get(i);
						if (currentResult.getItemName().equals(songName) && currentResult.getType() == 's')  {
							System.out.println("Song result for " + songName + " was found, adding entry");
							currentResult.entries.add(newEntry);
							foundDuplicateResult = true;
							break;
						}
					}
					
					if (!foundDuplicateResult)  {
						System.out.println("Song result for " + songName + " not found, creating new one");
						SearchResult newResult = new SearchResult(r.getString("artist"), songName, 's', new ArrayList<SearchResultEntry>());
						newResult.entries.add(newEntry);
						searchResults.add(newResult);
					}
				}
				
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			
			//Album search
			
			if (!cal.before(albumsStartDate))  {
			
				//conn = establishConnection();
				r = execQuery(conn, "select * from albums_" + df.format(cal.getTime()) + " where "
						+ "lower(artist)=lower('" + artist + "');");
					
				try {
				
					while (r.next())  {
						
						String albumName = r.getString("album");
						int position = r.getInt("position");
						boolean foundDuplicateResult = false;
						SearchResultEntry newEntry = new SearchResultEntry(position, cal.getTime());
						
						System.out.println("Chart result found: " + df.format(cal.getTime()) + " " + albumName);
						
						for (int i = 0; i < searchResults.size(); i++) {
							
							SearchResult currentResult = searchResults.get(i);
							if (currentResult.getItemName().equals(albumName) && currentResult.getType() == 'a')  {
								System.out.println("Album result for " + albumName + " was found, adding entry");
								currentResult.entries.add(newEntry);
								foundDuplicateResult = true;
								break;
							}
						}
						
						if (!foundDuplicateResult)  {
							System.out.println("Album result for " + albumName + " not found, creating new one");
							SearchResult newResult = new SearchResult(r.getString("artist"), albumName, 'a', new ArrayList<SearchResultEntry>());
							newResult.entries.add(newEntry);
							searchResults.add(newResult);
						}
					}
					
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
			
			}
				
			cal.add(Calendar.DATE, 7);
		}
		
		try { 
			if (conn != null) 
				conn.close();
		} 
		catch (Exception e2) {};
		
		long endTime = System.nanoTime();
		System.out.println("Execution time: " + (endTime - startTime));
		
		if (searchResults.size() > 0)
			return searchResults;
		else
			return null;
		
	}

}
