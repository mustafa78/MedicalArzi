/**
 * 
 */
package com.example.medicalarzi.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.vaadin.server.VaadinServlet;

/**
 * Implement the Singleton design pattern for locating the spring beans.
 * 
 * @author mkanchwa
 *
 */
@Service
public class ServiceLocator {
	
	/** Singleton. */
    private static ServiceLocator me;
    
	private final Map<String, Object> cache;
    
	/**
	 * Initialize the cache in the constructor.
	 */
	private ServiceLocator() {
		cache = new HashMap<String, Object>();
	}
	
	/**
     * Gets an instance of the locator.
     *
     * @return Singleton
     */
    public static ServiceLocator getInstance() {
 
        if (me == null) {
            me = new ServiceLocator();
        }
 
        return me;
    }
    
    /**
	 * Get the object stored under the name in the applicationContext xml. First
	 * look up in the cache, then in the configuration file.
	 *
	 * @param name
	 * @return Object stored
	 */
	public Object lookup(String name) {

		Object object = cache.get(name);
		
		try {
			if (object == null) {
				
				//From most anywhere within your Vaadin app, you can something like this
				WebApplicationContext applicationContext = WebApplicationContextUtils
						.getWebApplicationContext(VaadinServlet.getCurrent()
								.getServletContext());

				object = applicationContext.getBean(name);

				cache.put(name, object);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to fetch the: \"" + name
					+ "\" service from the context: ");
		} 
		return object;
	}
	
	/**
	 * This method lookups the requested patientService in the cache and returns the 
	 * PatientService
	 * 
	 * @return com.example.medicalarzi.service.PatientService
	 * 
	 */
	public PatientService getPatientService() {
		return (PatientService) lookup("service.PatientService");
	}
 

	/**
	 * This method lookups the requested lookupService in the cache and returns the 
	 * LookupService object.
	 * 
	 * @return com.example.medicalarzi.service.LookupService
	 * 
	 */
	public LookupService getLookupService() {
		return (LookupService) lookup("service.LookupService");
	}
	
	/**
	 * This method lookups the requested lookupService in the cache and returns the 
	 * ReviewerService object.
	 * 
	 * @return com.example.medicalarzi.service.ReviewerService
	 */
	public ReviewerService getReviewerService() {
		return (ReviewerService) lookup("service.ReviewerService");
	}
}
