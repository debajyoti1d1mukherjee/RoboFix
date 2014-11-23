package uk.co.bbc.fabric.interfaces.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import uk.co.bbc.fabric.interfaces.dao.hibernate.OnAirDerbyDaoImpl;
import uk.co.bbc.fabric.interfaces.dao.hibernate.OnAirEntityDao;
import uk.co.bbc.fabric.interfaces.derby.entity.Episode;
import uk.co.bbc.fabric.interfaces.derby.entity.Publicationevent;
import uk.co.bbc.fabric.interfaces.model.Report;

@Controller
//@RequestMapping("/report")
public class GenerateReportController {

	@RequestMapping(value = "/report",  method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		return "index";
	}
	
	@RequestMapping(value = "/pvreport",  method = RequestMethod.GET)
	public String openPvReport(ModelMap model) {
		return "generatepvreport";
	}
	
	private OnAirDerbyDaoImpl derbyEntityDao;
	
	public OnAirDerbyDaoImpl getDerbyImpl() {
		return derbyEntityDao;
	}
	@Autowired
	@Qualifier("derby")
	public void setDerbyImpl(OnAirDerbyDaoImpl derbyImpl) {
		this.derbyEntityDao = derbyImpl;
	}
	
	
	private OnAirEntityDao airEntityDao;
	public OnAirEntityDao getAirEntityDao() {
		return airEntityDao;
	}
	@Autowired
	public void setAirEntityDao(OnAirEntityDao airEntityDao) {
		this.airEntityDao = airEntityDao;
	}
	
	@RequestMapping(value = "/generateReport", method = RequestMethod.GET)
    public ModelAndView generateReport(@ModelAttribute("report")
                          Report report, BindingResult result, HttpServletRequest request) {
         
		System.out.println("From Date:" + report.getFromDate() + 
              "To Date:" + report.getToDate() +request.getParameter("fromDate"));
    
		final int pageSize = 10;
		
		
		if (report.getFromDate() != null && report.getToDate() != null) {
			Date toDate= new Date(report.getToDate());
			toDate.setHours(24);
			Date fromDate= new Date(report.getFromDate());
			List<Episode> commissioningList = new ArrayList<Episode>();
			commissioningList.addAll(derbyEntityDao.getAllEpisodes(fromDate,toDate));
			commissioningList.addAll(airEntityDao.getAllFailedEpisodes(fromDate, toDate));
			commissioningList.addAll(airEntityDao.getAllFailedSeries(fromDate,toDate));

			//List<Publicationevent> publicationEventList = new ArrayList<Publicationevent>();
			//publicationEventList.addAll(derbyEntityDao.getAllPublicationEvents(fromDate, toDate));
			//publicationEventList.addAll(airEntityDao.getAllFailedPublicationEvents(fromDate, toDate));

			PagedListHolder<Episode> productList = new PagedListHolder(commissioningList);
			productList.setPageSize(pageSize);
			request.getSession( ).setAttribute("SearchProductsController_productList", productList);
			
			List<Episode> pagedList = productList.getPageList();
			// return back to index.jsp
			ModelAndView model = new ModelAndView("index");
			model.addObject("pageText", "Showing Results 1 to 10 of "+ commissioningList.size());
			model.addObject("commisionLists", pagedList);
			//model.addObject("pvLists", publicationEventList);
			return model;
		} else {
			String page = request.getParameter("page");  
			PagedListHolder<Episode> productList = (PagedListHolder) request.getSession( ).getAttribute("SearchProductsController_productList");
			if ("next".equals(page)) {
				productList.nextPage( );
			}
			else if ("previous".equals(page)) {
				productList.previousPage( );
			}
			
			int lastResultNo=0;
			if(productList.getPageSize()*(productList.getPage()+1)>productList.getSource().size()){
				lastResultNo = productList.getSource().size();
			} else {
				lastResultNo = productList.getPageSize()*(productList.getPage()+1);
			}
			
			List<Episode> pagedList = productList.getPageList();
			ModelAndView model = new ModelAndView("index");
			model.addObject("pageText", "Showing Results "+(productList.getPageSize()*productList.getPage()+1) +" to "+lastResultNo+" of "+ productList.getSource().size());
			model.addObject("commisionLists", pagedList);
			return model;
		}
	}
	
	@RequestMapping(value = "/generatePVReport", method = RequestMethod.GET)
    public ModelAndView generatePVReport(@ModelAttribute("report")
                          Report report, BindingResult result, HttpServletRequest request) {
         
		System.out.println("From Date:" + report.getFromDate() + 
              "To Date:" + report.getToDate() +request.getParameter("fromDate"));
    
		final int pageSize = 10;
		
		
		if (report.getFromDate() != null && report.getToDate() != null) {
			Date toDate= new Date(report.getToDate());
			toDate.setHours(24);
			Date fromDate= new Date(report.getFromDate());
			//List<Episode> commissioningList = new ArrayList<Episode>();
			//commissioningList.addAll(derbyEntityDao.getAllEpisodes(fromDate,toDate));
			//commissioningList.addAll(airEntityDao.getAllFailedEpisodes(fromDate, toDate));
			//commissioningList.addAll(airEntityDao.getAllFailedSeries(fromDate,toDate));

			List<Publicationevent> publicationEventList = new ArrayList<Publicationevent>();
			publicationEventList.addAll(derbyEntityDao.getAllPublicationEvents(fromDate, toDate));
			publicationEventList.addAll(airEntityDao.getAllFailedPublicationEvents(fromDate, toDate));

			PagedListHolder<Episode> pvPagedList = new PagedListHolder(publicationEventList);
			pvPagedList.setPageSize(pageSize);
			request.getSession( ).setAttribute("SearchProductsController_productList", pvPagedList);
			
			List<Episode> pagedList = pvPagedList.getPageList();
			// return back to index.jsp
			ModelAndView model = new ModelAndView("generatepvreport");
			model.addObject("pageText", "Showing Results 1 to 10 of "+ publicationEventList.size());
			//model.addObject("commisionLists", pagedList);
			model.addObject("pvLists", pagedList);
			return model;
		} else {
			String page = request.getParameter("page");  
			PagedListHolder<Episode> productList = (PagedListHolder) request.getSession( ).getAttribute("SearchProductsController_productList");
			if ("next".equals(page)) {
				productList.nextPage( );
			}
			else if ("previous".equals(page)) {
				productList.previousPage( );
			}
			
			int lastResultNo=0;
			if(productList.getPageSize()*(productList.getPage()+1)>productList.getSource().size()){
				lastResultNo = productList.getSource().size();
			} else {
				lastResultNo = productList.getPageSize()*(productList.getPage()+1);
			}
			
			List<Episode> pagedList = productList.getPageList();
			ModelAndView model = new ModelAndView("generatepvreport");
			model.addObject("pageText", "Showing Results "+(productList.getPageSize()*productList.getPage()+1) +" to "+lastResultNo+" of "+ productList.getSource().size());
			model.addObject("pvLists", pagedList);
			return model;
		}
	}
	@ModelAttribute("report")
	public Report createModel() {
	    return new Report();
	}
	
}
