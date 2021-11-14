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
import domainapp.basics.model.meta.DAttr.Format;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.util.Tuple;
import vn.com.flight.customerManagement.City;
import vn.com.flight.exceptions.DExCode;
import vn.com.flight.utils.DToolkit;


@DClass(schema = "FLIGHT")
public class Hotel {
	public static final String A_id = "id";
	private static final String A_Destination = "Destination";
	  private static final String A_checkin = "checkin";
	  private static final String A_checkout = "checkout";
	private static final String A_HotelType = "hotelType";

	  
	  @DAttr(name=A_id,id=true,auto=true,type=Type.String,length=10,mutable=false,optional=false)
	  private String id;
	//static variable to keep track of Hotel id
	  private static int idCounter = 0;
	  
	  @DAttr(name = A_Destination, type = Type.Domain, length = 20, optional = true)
	  @DAssoc(ascName="city-has-hotel",role="hotel",
	      ascType=AssocType.One2Many, endType=AssocEndType.Many,
	  associate=@Associate(type=City.class,cardMin=1,cardMax=1))
	  private City Destination;
	  
	  @DAttr(name = A_checkin, type = Type.Date, length = 15, optional = false, format = Format.Date)
	  private Date checkin;
	  
	  @DAttr(name = A_checkout, type = Type.Date, length = 15, optional = false, format = Format.Date)
	  private Date checkout;
	  
	  @DAttr(name="Customernum",length=10,optional=false,type=Type.Integer)
	  private int Customernum;
	  
	  @DAttr(name="room",length=10,optional=false,type=Type.Integer)
	  private int room;
	  @DAttr(name = A_HotelType, type = Type.Domain, length = 20, optional = false)
		  @DAssoc(ascName="hotel-has-hoteltype",role="hotel",
		      ascType=AssocType.One2Many, endType=AssocEndType.Many,
		  associate=@Associate(type=HotelType.class,cardMin=1,cardMax=15))
		  private HotelType hotelType;

	  
	  
	
	  // constructor methods
	  // for creating in the application
	  
	  @DOpt(type = DOpt.Type.ObjectFormConstructor)
	  @DOpt(type = DOpt.Type.RequiredConstructor)
		public Hotel(
				@AttrRef("Destination") City Destination, 
				@AttrRef("checkin") Date checkin, 
				@AttrRef("checkout") Date checkout,
				@AttrRef("Customernum") Integer Customernum,
				@AttrRef("room") Integer room,
				@AttrRef("hotelType") HotelType hotelType
				
				) 
		{
			this(null,Destination,checkin, checkout, Customernum,room,hotelType);
		}
	  
	// a shared constructor that is invoked by other constructors
	  @DOpt(type=DOpt.Type.DataSourceConstructor)
	  public Hotel(@AttrRef("id")String id,
			  @AttrRef("Destination") City Destination, 
			  @AttrRef("checkin") Date checkin,
			  @AttrRef("checkout") Date checkout,
			  @AttrRef("Customernum") Integer Customernum,
			  @AttrRef("room") Integer room,
			  @AttrRef("hotelType") HotelType hotelType
			  ) 
	  throws ConstraintViolationException 
	  {
	    // generate an id
	    this.id = nextID(id);

	    // assign other values
	    this.Destination = Destination;
	    this.checkin = checkin;
	    this.checkout = checkout;
	    this.Customernum = Customernum;
	    this.room = room;
	    this.hotelType = hotelType;
	    
	    
	  }
	  
	  
	  
	public City getDestination() 
	{
		return Destination;
	}
	public void setDestination(City destination) 
	{
		Destination = destination;
	}
	public Date getCheckin() 
	{
		return checkin;
	}
	public void setCheckin(Date checkin) throws ConstraintViolationException 
	{
		// additional validation on createDate
		if (checkin.before(DToolkit.MIN_DATE)) 
		{
			throw new ConstraintViolationException(DExCode.INVALID_DATE, checkin);
		}

		this.checkin = checkin;
	}
	public Date getCheckout() 
	{
		return checkout;
	}
	public void setCheckout(Date checkout) throws ConstraintViolationException 
	{
		// additional validation on createDate
		if (checkout.before(checkin)) 
		{
			throw new ConstraintViolationException(DExCode.INVALID_DATE, checkout);
		}

		this.checkout = checkout;
	}
	public int getCustomernum() 
	{
		return Customernum;
	}
	public void setCustomernum(int customernum) 
	{
		Customernum = customernum;
	}
	public int getRoom() 
	{
		return room;
	}
	public void setRoom(int room) 
	{
		this.room = room;
	}
	
	public HotelType getHotelType() 
	{
		return hotelType;
	}

	public void setHotelType(HotelType hotelType) 
	{
		this.hotelType = hotelType;
	}

	public String getId() 
	{
		return id;
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
	   * @effects returns <code>Hotel(id,Destination,checkin,checkout,Customernum,room,cabin)</code>.
	   */
	  public String toString(boolean full) 
	  {
	    if (full)
	      return "Hotel(" + id + "," 
	    		+ Destination + "," + checkin  
	    		+ "," + checkout + 
	    		"," + Customernum + "," + room  +
	            ")";
	    else
	      return "Hotel(" + id + ")";
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
	    Hotel other = (Hotel) obj;
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
	
// automatically generate the next flight id
  private String nextID(String id) throws ConstraintViolationException 
  {
    if (id == null) 
    { // generate a new id
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
	  
	  
	  
	  
}
