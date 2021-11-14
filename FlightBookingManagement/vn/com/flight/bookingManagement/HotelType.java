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
import vn.com.flight.customerManagement.Gender;

@DClass(schema="FLIGHT")
public class HotelType {
	private static final String A_id = "id";
	private static final String A_HotelLocation = "hotellocation";
	private static final String A_RoomType = "roomtype";
	@DAttr(name=A_id,id=true,auto=true,type=Type.String,length=10,mutable=false,optional=false)
	private String id;
	//static variable to keep track of Hotel type id
	private static int idCounter = 0;
	@DAttr(name = "hotelName", length = 20, mutable = false, optional = false, type = Type.String,cid=true)
	private String hotelName;
	@DAttr(name = "hotelAddress", length = 20, mutable = false, optional = false, type = Type.String)
	private String hotelAddress;
	  @DAttr(name = A_HotelLocation, type = Type.Domain, length = 20, optional = true)
	  @DAssoc(ascName="city-has-hoteltype",role="hoteltype",
	      ascType=AssocType.One2Many, endType=AssocEndType.Many,
	  associate=@Associate(type=City.class,cardMin=1,cardMax=15))
	  private City hotellocation;
	  @DAttr(name = A_RoomType, type = Type.Domain, length = 10, optional = false)
	  private RoomType roomtype;
	  @DAttr(name="hotline",length=20,optional=false,type=Type.Integer)
	  private int hotline;
	  
	  @DOpt(type = DOpt.Type.ObjectFormConstructor)
	  @DOpt(type = DOpt.Type.RequiredConstructor)
		public HotelType(
				@AttrRef("hotelName") String hotelName, 
				@AttrRef("hotelAddress") String hotelAddress, 
				@AttrRef("hotellocation") City hotellocation,
				@AttrRef("roomtype") RoomType roomtype,
				@AttrRef("hotline") Integer hotline
				
				) 
		{
			this(null,hotelName,hotelAddress, hotellocation, roomtype,hotline);
		}
		// a shared constructor that is invoked by other constructors
	  @DOpt(type=DOpt.Type.DataSourceConstructor)
	  public HotelType(@AttrRef("id")String id,
				@AttrRef("hotelName") String hotelName, 
				@AttrRef("hotelAddress") String hotelAddress, 
				@AttrRef("hotellocation") City hotellocation,
				@AttrRef("roomtype") RoomType roomtype,
				@AttrRef("hotline") Integer hotline
			  ) 
	  throws ConstraintViolationException 
	  {
	    // generate an id
	    this.id = nextID(id);

	    // assign other values
	    this.hotelName = hotelName;
	    this.hotelAddress = hotelAddress;
	    this.hotellocation = hotellocation;
	    this.roomtype = roomtype;
	    this.hotline = hotline;    
	  }
	public String getHotelName() 
	{
		return hotelName;
	}
	public void setHotelName(String hotelName) 
	{
		this.hotelName = hotelName;
	}
	public String getHotelAddress() 
	{
		return hotelAddress;
	}
	public void setHotelAddress(String hotelAddress) 
	{
		this.hotelAddress = hotelAddress;
	}
	public City getHotellocation() 
	{
		return hotellocation;
	}
	public void setHotellocation(City hotellocation) 
	{
		this.hotellocation = hotellocation;
	}
	public RoomType getRoomtype() 
	{
		return roomtype;
	}
	public void setRoomtype(RoomType roomtype) 
	{
		this.roomtype = roomtype;
	}
	public int getHotline() 
	{
		return hotline;
	}
	public void setHotline(int hotline) 
	{
		this.hotline = hotline;
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
	   * @effects returns <code>HotelType(id,hotelName,hotelAddress,hotellocation,roomtype,hotline)</code>.
	   */
	  public String toString(boolean full) 
	  {
	    if (full)
	      return "HotelType(" + id + "," 
	    		+ hotelName + "," + hotelAddress  
	    		+ "," + hotellocation + 
	    		"," + roomtype + "," + hotline  +
	            ")";
	    else
	      return "HotelType(" + id + ")";
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
	    HotelType other = (HotelType) obj;
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
	
//automatically generate the next flight id
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
