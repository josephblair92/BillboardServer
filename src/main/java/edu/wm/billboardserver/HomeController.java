package edu.wm.billboardserver;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.wm.billboardserver.domain.Chart;
import edu.wm.billboardserver.domain.ChartEntry;
import edu.wm.billboardserver.domain.SearchResult;
import edu.wm.billboardserver.service.BillboardService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@Autowired private BillboardService billboardService;
	
	public static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	@RequestMapping(value = "/reverse/artist", method = RequestMethod.GET)
	public @ResponseBody ArrayList<SearchResult> reverseLookupArtist(@RequestParam("artist") String artist, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
		return billboardService.reverseLookupArtist(artist, startDate, endDate);
	}
	
	@RequestMapping(value = "/reverse/song", method = RequestMethod.GET)
	public @ResponseBody SearchResult reverseLookupSong(@RequestParam("artist") String artist, @RequestParam("song") String song, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
		return billboardService.reverseLookupSong(artist, song, startDate, endDate);
	}
	
	@RequestMapping(value = "/reverse/album", method = RequestMethod.GET)
	public @ResponseBody SearchResult reverseLookupAlbum(@RequestParam("artist") String artist, @RequestParam("album") String album, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {
		return billboardService.reverseLookupAlbum(artist, album, startDate, endDate);
	}
	
	@RequestMapping(value = "/chart", method = RequestMethod.GET)
	public @ResponseBody Chart getChart(@RequestParam("date") String date, @RequestParam("type") String type) {
		DateFormat df = new SimpleDateFormat("MMddyyyy");
		try {
			return billboardService.getChart(type.charAt(0), df.parse(date));
		} 
		catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	

	
}
