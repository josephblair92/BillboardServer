package edu.wm.billboardserver.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import edu.wm.billboardserver.dao.IBillboardDAO;
import edu.wm.billboardserver.domain.Chart;
import edu.wm.billboardserver.domain.SearchResult;

public class BillboardService {

	@Autowired private IBillboardDAO billboardDAO;
	
	public Chart getChart(char type, Date date) {
		
		Calendar userDate = Calendar.getInstance();
		userDate.setTime(date);
		
		Calendar singlesStartDate = Calendar.getInstance();
		singlesStartDate.set(1940, 6, 20);
		
		Calendar albumsStartDate = Calendar.getInstance();
		albumsStartDate.set(1970, 11, 19);

		Calendar endDate = Calendar.getInstance();
		endDate.set(2013, 11, 15);
		
		//Check if given date is a Saturday
		
		if (userDate.get(Calendar.DAY_OF_WEEK) != 7)
			return null;
	
		//Check if given date is in range
		
		if (type == 'a')  {
			
			if (userDate.before(albumsStartDate) || userDate.after(endDate))
				return null;
			else
				return billboardDAO.getAlbumsChart(date);
			
		}
		else if (type == 's')  {
			
			if (userDate.before(singlesStartDate) || userDate.after(endDate))
				return null;
			else
				return billboardDAO.getSinglesChart(date);
			
		}
		else
			return null;
		
	}

	public ArrayList<SearchResult> reverseLookupArtist(String artist, String strStartDate, String strEndDate) {
		
		if (artist.equals(""))
			return null;

		//Create Calendar objects for date range to search
		
		Calendar defaultStartDate = Calendar.getInstance();
		defaultStartDate.set(1940, 6, 20);

		Calendar defaultEndDate = Calendar.getInstance();
		defaultEndDate.set(2013, 11, 15);
			
		Calendar[] dateRange = verifyUserDateRange(strStartDate, strEndDate, defaultStartDate, defaultEndDate);

		return billboardDAO.reverseLookupArtist(artist, dateRange[0], dateRange[1]);
		
	}

	public SearchResult reverseLookupSong(String artist, String song, String strStartDate, String strEndDate) {
		
		if (artist.equals("") || song.equals(""))
			return null;

		//Create Calendar objects for date range to search
		
		Calendar defaultStartDate = Calendar.getInstance();
		defaultStartDate.set(1940, 6, 20);

		Calendar defaultEndDate = Calendar.getInstance();
		defaultEndDate.set(2013, 11, 15);
			
		Calendar[] dateRange = verifyUserDateRange(strStartDate, strEndDate, defaultStartDate, defaultEndDate);

		return billboardDAO.reverseLookupSong(artist, song, dateRange[0], dateRange[1]);
		
	}
	
	public SearchResult reverseLookupAlbum(String artist, String album, String strStartDate, String strEndDate) {
		
		if (artist.equals("") || album.equals(""))
			return null;

		//Create Calendar objects for date range to search
		
		Calendar defaultStartDate = Calendar.getInstance();
		defaultStartDate.set(1970, 11, 19);

		Calendar defaultEndDate = Calendar.getInstance();
		defaultEndDate.set(2013, 11, 15);
			
		Calendar[] dateRange = verifyUserDateRange(strStartDate, strEndDate, defaultStartDate, defaultEndDate);

		return billboardDAO.reverseLookupAlbum(artist, album, dateRange[0], dateRange[1]);
		
	}
	
	public Calendar[] verifyUserDateRange(String strUserStartDate, String strUserEndDate, Calendar defaultStartDate, Calendar defaultEndDate) {
		
		DateFormat df = new SimpleDateFormat("MMddyyyy");
		Calendar startDate, endDate;
		
		//Try to parse user start date, if this fails, set to default
		
		try {
			
			Calendar userStartDate = Calendar.getInstance();
			userStartDate.setTime(df.parse(strUserStartDate));
			userStartDate = toNearestSaturday(userStartDate);
			
			if (userStartDate.after(defaultStartDate) && userStartDate.before(defaultEndDate))
				startDate = userStartDate;
			else
				startDate = defaultStartDate;
			
		}
		
		catch (java.text.ParseException e) {
			e.printStackTrace();
			startDate = defaultStartDate;
		}		
				
		//Try to parse user end date, if this fails, set to default
		
		try  {
		
			Calendar userEndDate = Calendar.getInstance();
			userEndDate.setTime(df.parse(strUserEndDate));
			userEndDate = toNearestSaturday(userEndDate);
			
			if (userEndDate.after(startDate) && userEndDate.before(defaultEndDate))
				endDate = userEndDate;
			else
				endDate = defaultEndDate;
			
		}
		
		catch (java.text.ParseException e) {
			e.printStackTrace();
			endDate = defaultEndDate;
		}				

		if (endDate.before(startDate))  {
			startDate = defaultStartDate;
			endDate = defaultEndDate;
		}
		
		Calendar[] dateRange = {startDate, endDate};
		return dateRange;
		
	}
	
	public Calendar toNearestSaturday(Calendar cal) {
		
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		int delta;
		
		if (dayOfWeek > 3)
			delta = 7 - dayOfWeek;
		else
			delta = -dayOfWeek;
		
		cal.add(Calendar.DATE, delta);
		return cal;
		
	}

}
