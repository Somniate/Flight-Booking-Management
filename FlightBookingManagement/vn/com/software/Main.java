package vn.com.software;

import domainapp.software.SoftwareFactory;
import domainapp.softwareimpl.DomSoftware;
import vn.com.flight.bookingManagement.AdditionalServices;
import vn.com.flight.bookingManagement.Booking;
import vn.com.flight.bookingManagement.CarType;
import vn.com.flight.bookingManagement.Carrental;
import vn.com.flight.bookingManagement.Hotel;
import vn.com.flight.bookingManagement.HotelType;
import vn.com.flight.customer.reports.CustomersByCityJoinReport;
import vn.com.flight.customer.reports.CustomersByNameReport;
import vn.com.flight.customer.reports.FlightByCityReport;
import vn.com.flight.customerManagement.Account;
import vn.com.flight.customerManagement.Card;
import vn.com.flight.customerManagement.City;
import vn.com.flight.customerManagement.Customer;
import vn.com.flight.customerManagement.Role;
import vn.com.flight.flightManagement.Airline;
import vn.com.flight.flightManagement.Airplane;
import vn.com.flight.flightManagement.Airport;
import vn.com.flight.flightManagement.Flight;


		  /**
		   * @overview 
		   *  Create and run a UI-based {@link DomSoftware} for a pre-defined model.  
		   *  
		   * @author dmle
		   */
		  public class Main {
		    
		    // 1. initialise the model
		    static final Class[] model = 
		    {
				     Customer.class, 
				     Account.class,
				     City.class,
				     Flight.class,
				     Hotel.class,
				     HotelType.class,
				     Carrental.class,
				     CarType.class,
				     Booking.class,
				     Airplane.class,
				     Airport.class,
				     Airline.class,
				     Role.class,
				     Card.class,		     
				     AdditionalServices.class,
				     CustomersByNameReport.class,
				     CustomersByCityJoinReport.class,
				     FlightByCityReport.class,
		    };
		    
		    /**
		     * @effects 
		     *  create and run a UI-based {@link DomSoftware} for a pre-defined model. 
		     */
		    public static void main(String[] args)
		    {
		      // 2. create UI software
		      DomSoftware sw = SoftwareFactory.createUIDomSoftware();
		      
		      // 3. run
		      // create in memory configuration
		      System.setProperty("domainapp.setup.SerialiseConfiguration", "false");
		      
		      // 3. run it
		      try {
		        sw.run(model);
		      } catch (Exception e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		      }   
		    }

		  }


