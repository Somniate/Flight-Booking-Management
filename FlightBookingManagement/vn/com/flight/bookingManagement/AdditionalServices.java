package vn.com.flight.bookingManagement;

import java.util.Calendar;

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
import vn.com.flight.exceptions.DExCode;



@DClass(schema="FLIGHT")
public class AdditionalServices{
	  private static final String A_TotalPrice = "TotalPrice";
	  private static final String A_CarrentalPrice = "CarrentalPrice";
	
	  private static final String A_id = "id";
		@DAttr(name=A_id,id=true,auto=true,type=Type.Integer,length=10,mutable=false,optional=false)
	  private int id;
	 //static variable to keep track of AdditionalServices id
	  private static int idCounter = 0;
	  @DAttr(name = "hotel", type = Type.Domain, length = 20, optional = true)
	  @DAssoc(ascName="services-has-hotel",role="services",
	      ascType=AssocType.One2Many, endType=AssocEndType.Many,
	  associate=@Associate(type=Hotel.class,cardMin=1,cardMax=15))
	  private Hotel hotel;
	  
	  @DAttr(name = "carrental", type = Type.Domain, length = 20, optional = true)
	  @DAssoc(ascName="services-has-carrental",role="carrental",
	      ascType=AssocType.One2Many, endType=AssocEndType.Many,
	  associate=@Associate(type=Carrental.class,cardMin=1,cardMax=15))
	  private Carrental carrental;
	  
	  @DAttr(name="booking",type=Type.Domain,optional=true)
	  @DAssoc(ascName="booking-has-additionalservices",role="booking",
	  ascType=AssocType.One2Many,endType=AssocEndType.Many,
	  associate=@Associate(type=Booking.class,cardMin=1,cardMax=15))
	  private Booking booking;
	  
	   
	  
	  @DAttr(name = A_CarrentalPrice, type = Type.Double, length = 15, optional = true,cid = true)
	  private Double CarrentalPrice;
	  
	  @DAttr(name = "HotelPrice", type = Type.Double, length = 15, optional = true,cid = true)
	  private Double HotelPrice;

	  // constructor methods
	  // for creating in the application
	  @DOpt(type=DOpt.Type.ObjectFormConstructor)
	  @DOpt(type=DOpt.Type.RequiredConstructor)
	  public AdditionalServices(@AttrRef("hotel") Hotel hotel,
			  @AttrRef("carrental") Carrental carrental,
			  @AttrRef("booking") Booking booking,
			  @AttrRef("CarrentalPrice") Double CarrentalPrice,
			  @AttrRef("HotelPrice") Double HotelPrice
			  ) 
	  {
	    this(null, hotel, carrental,booking,CarrentalPrice,HotelPrice);
	  }
	//constructor methods for creating in the application
	//without booking class

	  @DOpt(type=DOpt.Type.DataSourceConstructor)
	  public AdditionalServices(
			  @AttrRef("id") Integer id,
			  @AttrRef("hotel") Hotel hotel,
			  @AttrRef("carrental") Carrental carrental,			
			  @AttrRef("CarrentalPrice") Double CarrentalPrice,
			  @AttrRef("HotelPrice") Double HotelPrice
			  
			  ) 
	  {
	    this(id, hotel, carrental,null,CarrentalPrice,HotelPrice);
	  }
		//constructor methods for creating in the application
		//without carrental class
	  @DOpt(type=DOpt.Type.DataSourceConstructor)
	  public AdditionalServices(
			  @AttrRef("id") Integer id,
			  @AttrRef("hotel") Hotel hotel,			
			  @AttrRef("CarrentalPrice") Double CarrentalPrice,
			  @AttrRef("HotelPrice") Double HotelPrice
			  
			  ) 
	  {
	    this(id, hotel, null,null,CarrentalPrice,HotelPrice);
	  }
	  
	// a shared constructor that is invoked by other constructors
	  public AdditionalServices(@AttrRef("id") Integer id,
			  @AttrRef("hotel") Hotel hotel, 
			  @AttrRef("carrental") Carrental carrental,
			  @AttrRef("booking") Booking booking,
			  @AttrRef("CarrentalPrice") Double CarrentalPrice, 
			  @AttrRef("HotelPrice") Double HotelPrice) 
			  throws ConstraintViolationException {
		    // generate an id
		    this.id = nextId(id);

		    // assign other values
		    this.hotel = hotel;
		    this.carrental = carrental;
		    this.booking = booking;
		    this.CarrentalPrice = CarrentalPrice;
		    this.HotelPrice = HotelPrice;
		
		    
		 
//		    stateHist = new StateHistory<>();
//		    updateTotalPrice();
		    
		  }
	  
	  
	public void setCarrentalPrice(Double CarrentalPrice) {
			
			if (CarrentalPrice < 0) {
				throw new ConstraintViolationException(DExCode.INVALID_CARRENTALPRICE, CarrentalPrice);
			}
		    
		    setCarrentalPrice(CarrentalPrice, false);
		  }
		  
	public void setCarrentalPrice(Double CarrentalPrice, boolean updateTotalPrice) {
		    this.CarrentalPrice = CarrentalPrice;
		    
//		    updateTotalPrice(); 
		  }
	


			  
			  
		public Double getCarrentalPrice() {
				return CarrentalPrice;
			}

	public Double getHotelPrice() {
		 return HotelPrice;
	}


	public void setHotelPrice(Double hotelPrice) {
		HotelPrice = hotelPrice;
	}


		public Hotel getHotel() {
		return hotel;
	}
	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}
	public Carrental getCarrental() 
	{
		return carrental;
	}
	
	public void setCarrental(Carrental carrental) 
	{
		this.carrental = carrental;
	}
		

	public int getId() {
		return id;
	}


	public Booking getBooking() 
	{
		return booking;
	}
			  public void setBooking(Booking booking) {
			    this.booking = booking;
			  }
			  @DOpt(type=DOpt.Type.LinkAdderNew)
			  public void setNewBooking(Booking booking) {
			    this.booking = booking;
			    // do other updates here (if needed)
			  }
	  
				public static int nextId(Integer currID) {
					if (currID == null) {
						idCounter++;
						return idCounter;
					} else {
						int num = currID.intValue();
						if (num > idCounter)
							idCounter = num;

						return currID;
					}
				}
				/**
				 * @requires minVal != null /\ maxVal != null
				 * @effects update the auto-generated value of attribute <tt>attrib</tt>,
				 *          specified for <tt>derivingValue</tt>, using <tt>minVal, maxVal</tt>
				 */
				@DOpt(type = DOpt.Type.AutoAttributeValueSynchroniser)
				public static void updateAutoGeneratedValue(DAttr attrib, Tuple derivingValue, Object minVal, Object maxVal)
						throws ConstraintViolationException {

					if (minVal != null && maxVal != null) {
						// TODO: update this for the correct attribute if there are more than one auto
						// attributes of this class
						int maxIdVal = (Integer) maxVal;
						if (maxIdVal > idCounter)
							idCounter = maxIdVal;
					}
				}

	  
}
