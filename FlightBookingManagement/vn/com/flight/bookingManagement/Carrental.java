package vn.com.flight.bookingManagement;


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
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.util.Tuple;

import vn.com.flight.customerManagement.City;
import vn.com.flight.exceptions.DExCode;
import vn.com.flight.utils.DToolkit;



@DClass(schema = "FLIGHT")
public class Carrental {
	public static final String A_id = "id";
	private static final String A_Location = "Location";
	  private static final String A_Pickup = "Pickup";
	  private static final String A_Dropoff = "Dropoff";
	private static final String A_Cartype = "cartype";

	  
	  
	  @DAttr(name=A_id,id=true,auto=true,type=Type.String,length=10,mutable=false,optional=false)
	  private String id;
	//static variable to keep track of Carrental id
	  private static int idCounter = 0;
	  
	  @DAttr(name = A_Location, type = Type.Domain, length = 20, optional = true)
	  @DAssoc(ascName="city-has-carrental",role="carrental",
	      ascType=AssocType.One2Many, endType=AssocEndType.Many,
	  associate=@Associate(type=City.class,cardMin=1,cardMax=1))
	  private City Location;
	  
	  @DAttr(name = A_Pickup, type = Type.Date, length = 15, optional = false)
	  private Date Pickup;
	  
	  @DAttr(name = A_Dropoff, type = Type.Date, length = 15, optional = false)
	  private Date Dropoff;
	  
	  @DAttr(name = A_Cartype, type = Type.Domain, length = 20, optional = false)
	  @DAssoc(ascName="carrental-has-cartype",role="carrental",
	      ascType=AssocType.One2Many, endType=AssocEndType.Many,
	  associate=@Associate(type=CarType.class,cardMin=1,cardMax=15))
	  private CarType cartype;
	  
	  // constructor methods
	  // for creating in the application
	  @DOpt(type = DOpt.Type.ObjectFormConstructor)
	  @DOpt(type = DOpt.Type.RequiredConstructor)
		public Carrental(
				@AttrRef("Location") City Location, 
				@AttrRef("Pickup") Date Pickup, 
				@AttrRef("Dropoff") Date Dropoff,
				@AttrRef("cartype") CarType cartype
				
				) 
		{
			this(null,Location,Pickup, Dropoff,cartype);
		}
	// a shared constructor that is invoked by other constructors
	  @DOpt(type=DOpt.Type.DataSourceConstructor)
	  public Carrental(@AttrRef("id")String id,
			  @AttrRef("Location") City Location, 
			  @AttrRef("Pickup") Date Pickup,
			  @AttrRef("Dropoff") Date Dropoff,
			  @AttrRef("cartype") CarType cartype
			  ) 
	  throws ConstraintViolationException {
	    // generate an id
	    this.id = nextID(id);

	    // assign other values
	    this.Location = Location;
	    this.Pickup = Pickup;
	    this.Dropoff = Dropoff;
	    this.cartype = cartype;

	  }
	  
		public City getLocation() {
		return Location;
	}
	public void setLocation(City Location) {
		this.Location = Location;
	}
	public Date getPickup() {
		return Pickup;
	}
	public void setPickup(Date Pickup) throws ConstraintViolationException {
		// additional validation on createDate
		if (Pickup.before(DToolkit.MIN_DATE)) {
			throw new ConstraintViolationException(DExCode.INVALID_DATE, Pickup);
		}

		this.Pickup = Pickup;
	}
	public Date getDropoff() {
		return Dropoff;
	}
	public void setDropoff(Date Dropoff) throws ConstraintViolationException {
		// additional validation on createDate
		if (Dropoff.before(Pickup)) {
			throw new ConstraintViolationException(DExCode.INVALID_DATE, Dropoff);
		}

		this.Dropoff = Dropoff;
	}
	

	public String getId() 
	{
		return id;
	}
	
	
	
	
	
	
	

	public CarType getCartype() {
		return cartype;
	}
	public void setCartype(CarType cartype) {
		this.cartype = cartype;
	}
	// override toString
	  /**
	   * @effects returns <code>this.id</code>
	   */
	  @Override
	  public String toString() {
	    return toString(true);
	  }

	  /**
	   * @effects returns <code>Carrental(id,Location,Pickup,Dropoff,cartype)</code>.
	   */
	  public String toString(boolean full) {
	    if (full)
	      return "Carrental(" + id + "," 
	    		+ Location + "," + Pickup  
	    		+ "," + Dropoff +"," + cartype +
	            ")";
	    else
	      return "Carrental(" + id + ")";
	  }

	  @Override
	  public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((id == null) ? 0 : id.hashCode());
	    return result;
	  }

	  @Override
	  public boolean equals(Object obj) {
	    if (this == obj)
	      return true;
	    if (obj == null)
	      return false;
	    if (getClass() != obj.getClass())
	      return false;
	    Carrental other = (Carrental) obj;
	    if (id == null) {
	      if (other.id != null)
	        return false;
	    } else if (!id.equals(other.id))
	      return false;
	    return true;
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
	      Object maxVal) throws ConstraintViolationException {
	    
	    if (minVal != null && maxVal != null) {
	      //TODO: update this for the correct attribute if there are more than one auto attributes of this class 

	      String maxId = (String) maxVal;
	      
	      try {
	        int maxIdNum = Integer.parseInt(maxId.substring(1));
	        
	        if (maxIdNum > idCounter) // extra check
	          idCounter = maxIdNum;
	        
	      } catch (RuntimeException e) {
	        throw new ConstraintViolationException(
	            ConstraintViolationException.Code.INVALID_VALUE, e, new Object[] {maxId});
	      }
	    }
	  }
// automatically generate the next flight id
  private String nextID(String id) throws ConstraintViolationException {
    if (id == null) { // generate a new id
      if (idCounter == 0) {
        idCounter = Calendar.getInstance().get(Calendar.YEAR);
      } else {
        idCounter++;
      }
      return "S" + idCounter;
    } else {
      // update id
      int num;
      try {
        num = Integer.parseInt(id.substring(1));
      } catch (RuntimeException e) {
        throw new ConstraintViolationException(
            ConstraintViolationException.Code.INVALID_VALUE, e, new Object[] { id });
      }
      
      if (num > idCounter) {
        idCounter = num;
      }
      
      return id;
    }
  }
	  
	  
	  
	  
}
