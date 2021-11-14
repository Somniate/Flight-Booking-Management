package vn.com.flight.flightManagement;

import java.util.Calendar;
import java.util.Date;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr.Format;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.util.Tuple;
import vn.com.flight.customer.reports.FlightByCityReport;
import vn.com.flight.customerManagement.City;
import vn.com.flight.exceptions.DExCode;
import vn.com.flight.utils.DToolkit;



@DClass(schema = "FLIGHT")
public class Flight {
	
	  public static final String A_id = "id";
	  public static final String A_DepartureDate = "DepartureDate";
	  public static final String A_ArrivalDate = "ArrivalDate";
	  public static final String A_DepartDestination = "DepartDestination";
	  public static final String A_ArrivalDestination = "ArrivalDestination";
	  public static final String A_Airplane = "Airplane";
	  public static final String A_Airport = "Airport";
	  public static final String A_Airline = "Airline";
	  public static final String A_rptFlightByCity = "rptFlightByCity";
	  
	
	  
	  @DAttr(name=A_id,id=true,auto=true,type=Type.String,length=10,mutable=false,optional=false)
	  private String id;
	//static variable to keep track of flight id
	  private static int idCounter = 0;
	  
	  @DAttr(name = A_DepartureDate, type = Type.Date, length = 15, optional = false, format = Format.Date)
	  private Date DepartureDate;
	  
	  @DAttr(name = A_ArrivalDate, type = Type.Date, length = 15, optional = false, format = Format.Date)
	  private Date ArrivalDate;
	  
	  @DAttr(name = A_DepartDestination, type = Type.Domain, length = 20, optional = false)
	  @DAssoc(ascName="city-has-flight",role="flight",
	      ascType=AssocType.One2Many, endType=AssocEndType.Many,
	  associate=@Associate(type=City.class,cardMin=1,cardMax=25))
	  private City DepartDestination;
	  
	  @DAttr(name = A_ArrivalDestination, type = Type.Domain, length = 20, optional = false)
	  @DAssoc(ascName="city-has-flight",role="flight",
	      ascType=AssocType.One2Many, endType=AssocEndType.Many,
	  associate=@Associate(type=City.class,cardMin=1,cardMax=25))
	  private City ArrivalDestination;
	  
	  @DAttr(name = A_Airplane, type = Type.Domain, length = 20, optional = false)
	  @DAssoc(ascName="flight-has-airplane",role="flight",
	      ascType=AssocType.One2Many, endType=AssocEndType.Many,
	  associate=@Associate(type=Airplane.class,cardMin=1,cardMax=25))
	  private Airplane Airplane;
	  
	  @DAttr(name = A_Airport, type = Type.Domain, length = 20, optional = false)
	  @DAssoc(ascName="flight-has-airport",role="flight",
	      ascType=AssocType.One2Many, endType=AssocEndType.Many,
	  associate=@Associate(type=Airport.class,cardMin=1,cardMax=25))
	  private Airport Airport;
	  
	  @DAttr(name = A_Airline, type = Type.Domain, length = 20, optional = false)
	  @DAssoc(ascName="flight-has-airline",role="flight",
	      ascType=AssocType.One2Many, endType=AssocEndType.Many,
	  associate=@Associate(type=Airline.class,cardMin=1,cardMax=25))
	  private Airline Airline;
	    
	  @DAttr(name=A_rptFlightByCity,type=Type.Domain, serialisable=false, 
		      // IMPORTANT: set virtual=true to exclude this attribute from the object state
		      // (avoiding the view having to load this attribute's value from data source)
		      virtual=true)
	  private FlightByCityReport rptFlightByCity;
	  
		@DOpt(type = DOpt.Type.ObjectFormConstructor)
		@DOpt(type = DOpt.Type.RequiredConstructor)
		public Flight(
				@AttrRef("DepartureDate") Date DepartureDate, 
				@AttrRef("ArrivalDate") Date ArrivalDate, 
				@AttrRef("DepartDestination") City DepartDestination,
				@AttrRef("ArrivalDestination") City ArrivalDestination,
				@AttrRef("Airplane") Airplane Airplane,
				@AttrRef("Airport") Airport Airport,
				@AttrRef("Airline") Airline Airline) 
		{
			this(null,DepartureDate,ArrivalDate, DepartDestination, ArrivalDestination, Airplane, Airport,Airline);
		}

		  @DOpt(type=DOpt.Type.DataSourceConstructor)
		  public Flight(@AttrRef("id")String id,
				  @AttrRef("DepartureDate") Date DepartureDate, 
				  @AttrRef("ArrivalDate") Date ArrivalDate,
				  @AttrRef("DepartDestination") City DepartDestination,
				  @AttrRef("ArrivalDestination") City ArrivalDestination,
				  @AttrRef("Airplane") Airplane Airplane,
				  @AttrRef("Airport") Airport Airport,
				  @AttrRef("Airline") Airline Airline) 
		  throws ConstraintViolationException 
		  {
		    // generate an id
		    this.id = nextID(id);

		    // assign other values
		    this.DepartureDate = DepartureDate;
		    this.ArrivalDate = ArrivalDate;
		    this.DepartDestination = DepartDestination;
		    this.ArrivalDestination = ArrivalDestination;
		    this.Airplane = Airplane;
		    this.Airport = Airport;
		    this.Airline = Airline;  
		  }
		  
			public void setDepartureDate(Date DepartureDate) throws ConstraintViolationException 
			{
				// additional validation on createDate
				if (DepartureDate.before(DToolkit.MIN_DATE)) 
				{
					throw new ConstraintViolationException(DExCode.INVALID_DEPARTUREDATE, DepartureDate);
				}

				this.DepartureDate = DepartureDate;
			}
			
			public void setArrivalDate(Date ArrivalDate) throws ConstraintViolationException 
			{
				// additional validation on createDate
				if (ArrivalDate.before(DepartureDate)) 
				{
					throw new ConstraintViolationException(DExCode.INVALID_ARRIVALDATE, ArrivalDate);
				}

				this.ArrivalDate = ArrivalDate;
			}
			
			  public void setDepartDestination(City DepartDestination) 
			  {
				    this.DepartDestination = DepartDestination;
			  }
			  public void setArrivalDestination(City ArrivalDestination) 
			  {
				    this.ArrivalDestination = ArrivalDestination;
			  }


		public Airline getAirline() 
		{
				return Airline;
		}

		public Airplane getAirplane() 
		{
				return Airplane;
		}


		public Airport getAirport() 
		{
				return Airport;
		}


		public String getId() 
		{
				return id;
		}

		public Date getDepartureDate() 
		{
				return DepartureDate;
		}
		public Date getArrivalDate() 
		{
				return ArrivalDate;
		}

		public City getDepartDestination() 
		{
				return DepartDestination;
		}

		public City getArrivalDestination() 
		{
				return ArrivalDestination;
		}
		

		public FlightByCityReport getRptFlightByCity() 
		{
			return rptFlightByCity;
		}

			// override toString
			  /**
			   * @effects returns <code>this.id</code>
			   */
		@Override
		public String toString() 
		{
			    return toString(true);
		}

			  /**
			   * @effects returns <code>Flight(FlightID,DepartureDate,ArrivalDate,DepartDestination,ArrivalDestination,Airplane,Airport,Airline)</code>.
			   */
	    public String toString(boolean full) 
	    {
			    if (full)
			      return "Flight(" + id + "," 
			    		+ DepartureDate + "," + ArrivalDate  
			    		+ "," + DepartDestination + 
			    		"," + ArrivalDestination+ ","
			    		+ Airplane + "," +Airport + ","  + Airline +"," +
			            ")";
			    else
			      return "Flight(" + id + ")";
			  }

		 @Override
		public int hashCode() 
		{
			    final int prime = 31;
			    int result = 1;
			    result = prime * result + ((id == null) ? 0 : id.hashCode());
			    return result;
		}

		@Override
		public boolean equals(Object obj) 
		{
			    if (this == obj)
			      return true;
			    if (obj == null)
			      return false;
			    if (getClass() != obj.getClass())
			      return false;
			    Flight other = (Flight) obj;
			    if (id == null) {
			      if (other.id != null)
			        return false;
			    } else if (!id.equals(other.id))
			      return false;
			    return true;
		 }
			
		// automatically generate the next flight id
		 private String nextID(String id) throws ConstraintViolationException 
		 {
		    if (id == null) 
		    { 
		    	// generate a new id
		      if (idCounter == 0) 
		      {
		        idCounter = Calendar.getInstance().get(Calendar.YEAR);
		      } else {
		        idCounter++;
		      }
		      return "S" + idCounter;
		    } else {
		      // update id
		      int num;
		      try 
		      {
		        num = Integer.parseInt(id.substring(1));
		      } catch (RuntimeException e) {
		        throw new ConstraintViolationException(
		            ConstraintViolationException.Code.INVALID_VALUE, e, new Object[] { id });
		      }
		      
		      if (num > idCounter) 
		      {
		        idCounter = num;
		      }
		      
		      return id;
		    }
		  }
		  /**
		   * @requires 
		   *  minVal != null /\ maxVal != null
		   * @effects 
		   *  update the auto-generated value of attribute <tt>attrib</tt>, specified for <tt>derivingValue</tt>, using <tt>minVal, maxVal</tt>
		   */
		  @DOpt(type=DOpt.Type.AutoAttributeValueSynchroniser)
		  public static void updateAutoGeneratedValue(
		      DAttr attrib,
		      Tuple derivingValue, 
		      Object minVal, 
		      Object maxVal) throws ConstraintViolationException 
		  {
		    
		    if (minVal != null && maxVal != null)
		    {
		      //TODO: update this for the correct attribute if there are more than one auto attributes of this class 

		      String maxId = (String) maxVal;
		      
		      try 
		      {
		        int maxIdNum = Integer.parseInt(maxId.substring(1));
		        
		        if (maxIdNum > idCounter) // extra check
		          idCounter = maxIdNum;
		        
		      } catch (RuntimeException e) {
		        throw new ConstraintViolationException(
		            ConstraintViolationException.Code.INVALID_VALUE, e, new Object[] {maxId});
		      }
		    }
		  }
	  
	  
	  
	  
}
