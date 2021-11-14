package vn.com.flight.bookingManagement;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.util.Tuple;
import domainapp.basics.util.cache.StateHistory;
import vn.com.flight.customerManagement.Account;
import vn.com.flight.customerManagement.Customer;
import vn.com.flight.flightManagement.Flight;

@DClass(schema = "FLIGHT")
public class Booking {
	

	private static final String A_id = "id";
	private static final String A_flightPrice = "flightPrice";
	private static final String A_hotelPrice = "hotelPrice";
	private static final String A_totalPrice = "totalPrice";
	private static final String A_carrentalPrice = "carrentalPrice";
	
	@DAttr(name=A_id,id=true,auto=true,type=Type.Integer,length=10,mutable=false,optional=false)
	private int id;
	//static variable to keep track of Booking id
	private static int idCounter = 0;


	@DAttr(name = "flight", type = Type.Domain, length = 6, optional = true)
	@DAssoc(ascName = "booking-has-flight", role = "booking", ascType = AssocType.One2Many, endType = AssocEndType.Many, associate = @Associate(type = Flight.class, cardMin = 1, cardMax = 15))
	private Flight flight;

	@DAttr(name = "account", type = Type.Domain, length = 6,optional = true)
	@DAssoc(ascName = "account-has-booking", role = "booking",
	ascType = AssocType.One2Many, endType = AssocEndType.One, associate = @Associate(type = Account.class, cardMin = 1, cardMax = 15))
	private Account account;

	@DAttr(name = "customer", type = Type.Domain, length = 6)
	@DAssoc(ascName = "customer-has-booking", role = "booking", ascType = AssocType.One2Many, endType = AssocEndType.One, associate = @Associate(type = Customer.class, cardMin = 1, cardMax = 15))
	private Customer customer;

	@DAttr(name = "cabin", length = 10, mutable = false, optional = false, type = Type.String)
	private String cabin;

	@DAttr(name = "promocode", length = 10, mutable = false, optional = true, type = Type.String)
	private String promocode;
	
	@DAttr(name = A_flightPrice, type = Type.Double, mutable = false, optional = true,length = 30)
	private Double flightPrice;
	
	@DAttr(name = A_carrentalPrice, type = Type.Double,auto = true, mutable = false, optional = true,serialisable = false,derivedFrom = {
	"additionalservices"})
	private Double carrentalPrice;
	
	@DAttr(name = A_hotelPrice, type = Type.Double,auto = true, mutable = false, optional = true,serialisable = false,
	derivedFrom = {"additionalservices"})
	private Double hotelPrice;
	

		
	@DAttr(name = A_totalPrice, type = Type.Double, auto = true, mutable = false, optional = true,serialisable = false, derivedFrom = {
			A_flightPrice ,A_hotelPrice})
	private Double totalPrice;
	
	@DAttr(name = "additionalservices", type = Type.Collection, 
			 optional = true,serialisable=false,
			filter = @Select(clazz = AdditionalServices.class))
			@DAssoc(ascName = "booking-has-additionalservices",
			role = "booking", ascType = AssocType.One2Many, 
			endType = AssocEndType.Many, 
			associate = @Associate(type = AdditionalServices.class, cardMin = 1, cardMax = 15))
			private Collection<AdditionalServices> additionalservices;
	
	// derived attributes
	private int hotelpricecount;
	private int carrentalpricecount;
	
	private StateHistory<String, Object> stateHist;
	
	  // constructor methods
	  // for creating in the application
	@DOpt(type = DOpt.Type.ObjectFormConstructor)
	@DOpt(type = DOpt.Type.RequiredConstructor)
	public Booking(
			@AttrRef("flight") Flight flight, 
			@AttrRef("account") Account account,
			@AttrRef("customer") Customer customer,
			@AttrRef("cabin") String cabin,
			@AttrRef("promocode") String promocode,
			@AttrRef("flightPrice") Double flightPrice
			) 
	{
		this(null, flight, null, customer, cabin, promocode,flightPrice);
	}
	  // constructor methods
	  // for creating in the application
	  // without account class
	  @DOpt(type=DOpt.Type.DataSourceConstructor)
	  
		public Booking(@AttrRef("id") Integer id,

				@AttrRef("flight") Flight flight,
				@AttrRef("customer") Customer customer,
				@AttrRef("cabin") String cabin, 
				@AttrRef("promocode") String promocode,
				@AttrRef("flightPrice") Double flightPrice

		) throws ConstraintViolationException 
	  {
			// generate an id
			this.id = nextId(id);

			this.flight = flight;
			this.customer = customer;
			this.cabin = cabin;
			this.promocode = promocode;
		    this.flightPrice = flightPrice;
			additionalservices = new ArrayList<>();
			carrentalpricecount = 0;
			hotelpricecount = 0;
			stateHist = new StateHistory<>();
			forupdatehotelprice();
			updatecarrentalPrice();
			forupdatetotalprice();

		}
	// a shared constructor that is invoked by other constructors
	public Booking(@AttrRef("id") Integer id,

			@AttrRef("flight") Flight flight, 
			@AttrRef("account") Account account,
			@AttrRef("customer") Customer customer,
			@AttrRef("cabin") String cabin, 
			@AttrRef("promocode") String promocode,
			@AttrRef("flightPrice") Double flightPrice

	) throws ConstraintViolationException 
	{
		// generate an id
		this.id = nextId(id);

		this.flight = flight;
		this.account = account;
		this.customer = customer;
		this.cabin = cabin;
		this.promocode = promocode;
	    this.flightPrice = flightPrice;
		additionalservices = new ArrayList<>();
		carrentalpricecount = 0;
		hotelpricecount = 0;
		stateHist = new StateHistory<>();
		updatehotelPrice();
		updatecarrentalPrice();
	

	}
	
	
	public Double getHotelPrice() 
	{
		return getHotelPrice(false);
	}

	public Double getHotelPrice(boolean cached) throws IllegalStateException 
	{
		if (cached) {
			Object val = stateHist.get(A_hotelPrice);

			if (val == null)
				throw new IllegalStateException("booking.hotelprice: cached value is null");

			return (Double) val;
		} else {
			if (hotelPrice != null)
				return hotelPrice;
			else
				return 0.0;
		}

	}
	
	public Double getCarrentalPrice() 
	{
		return getCarrentalPrice(false);
	}

	public Double getCarrentalPrice(boolean cached) throws IllegalStateException 
	{
		if (cached) {
			Object val = stateHist.get(A_carrentalPrice);

			if (val == null)
				throw new IllegalStateException("booking.carrentalprice: cached value is null");

			return (Double) val;
		} else {
			if (carrentalPrice != null)
				return carrentalPrice;
			else
				return 0.0;
		}

	}
	
	public Double getTotalPrice() 
	{
		return getTotalPrice(false);
	}

	public Double getTotalPrice(boolean cached) throws IllegalStateException 
	{
		if (cached) {
			Object val = stateHist.get(A_totalPrice);

			if (val == null)
				throw new IllegalStateException("booking.totalprice: cached value is null");

			return (Double) val;
		} else {
			if (totalPrice != null)
				return totalPrice;
			else
				return flightPrice;
		}

	}
	
	
	@DOpt(type = DOpt.Type.DerivedAttributeUpdater)
	@AttrRef(value = A_hotelPrice)
	public void updatehotelPrice() 
	{
		double totalF = 0;
		if(additionalservices != null) 
		{
			for(AdditionalServices a : additionalservices) 
			{
				totalF += a.getHotelPrice();
			}
		}
		stateHist.put(A_hotelPrice, hotelPrice);
		hotelPrice = totalF;

	}
	
	@DOpt(type = DOpt.Type.DerivedAttributeUpdater)
	@AttrRef(value = A_carrentalPrice)
	public void updatecarrentalPrice() 
	{
		double totalF = 0;
		if(additionalservices != null) 
		{
			for(AdditionalServices a : additionalservices) 
			{
				totalF += a.getCarrentalPrice();
			}
		}
		stateHist.put(A_carrentalPrice, carrentalPrice);
		carrentalPrice = totalF;

	}
	
	@DOpt(type = DOpt.Type.DerivedAttributeUpdater)
	@AttrRef(value = A_totalPrice)
	public void updatetotalPrice() 
	{
		double totalPriceD = 0;
		// updates carrental price, hotel price and total price
		if(flightPrice != null)
		{
			 
			 totalPriceD = totalPriceD + flightPrice;
		}
		
		if(carrentalPrice != null) 
		{
			updatecarrentalPrice();
			totalPriceD = totalPriceD + carrentalPrice;
		}
		
		
		if(hotelPrice != null)
		{
			updatehotelPrice();
			totalPriceD = totalPriceD + hotelPrice;
		}
		
		
		stateHist.put(A_totalPrice, totalPrice);
		totalPrice = (double) Math.round(totalPriceD);


	}
	public void forupdatetotalprice() 
	{
		updatetotalPrice();
		if (totalPrice != null) 
		{
			this.totalPrice = getTotalPrice();
		}
	}
	public void forupdatecarrentalprice()
	{
		updatecarrentalPrice();
		if (carrentalPrice != null) 
		{
			this.carrentalPrice = getCarrentalPrice();
		}
	}
	public void forupdatehotelprice() 
	{
		updatehotelPrice();
		if (hotelPrice != null) 
		{
			this.hotelPrice = getHotelPrice();
		}
	}
	

	
	
	
	
	@DOpt(type = DOpt.Type.LinkAdder)
	// only need to do this for reflexive association: @MemberRef(name="additionalServicess")
	public boolean addAdditionalServices(AdditionalServices a)
	{
		if (!this.additionalservices.contains(a)) 
		{
			additionalservices.add(a);
		}
		forupdatehotelprice();
		
		forupdatetotalprice();
		forupdatecarrentalprice();
		
		// no other attributes changed
		return false;
	}

	@DOpt(type = DOpt.Type.LinkAdderNew)
	public boolean addNewAdditionalServices(AdditionalServices a) 
	{
		additionalservices.add(a);
		hotelpricecount++;
		forupdatehotelprice();
		carrentalpricecount++;
		forupdatetotalprice();
		forupdatecarrentalprice();
		// no other attributes changed
		return false;
	}

	@DOpt(type = DOpt.Type.LinkAdder)
	public boolean addAdditionalServices(Collection<AdditionalServices> additionalservices) 
	{
		for (AdditionalServices a : additionalservices) 
		{
			if (!this.additionalservices.contains(a)) 
			{
				this.additionalservices.add(a);
			}
		}
		forupdatehotelprice();
		forupdatecarrentalprice();
		forupdatetotalprice();
		
		
		// no other attributes changed
		return false;
	}

	@DOpt(type = DOpt.Type.LinkAdderNew)
	public boolean addNewAdditionalServices(Collection<AdditionalServices> additionalservices) 
	{
		this.additionalservices.addAll(additionalservices);
		hotelpricecount += additionalservices.size();
		
		forupdatehotelprice();
		carrentalpricecount += additionalservices.size();
		forupdatecarrentalprice();
		forupdatetotalprice();
		

		// no other attributes changed
		return false;
	}

	@DOpt(type = DOpt.Type.LinkRemover)
	// only need to do this for reflexive association: @MemberRef(name="additionalServicess")
	public boolean removeAdditionalServices(AdditionalServices a) 
	{
		boolean removed = additionalservices.remove(a);

		if (removed) 
		{
			hotelpricecount--;
			carrentalpricecount--;
		}
		forupdatehotelprice();
		forupdatecarrentalprice();
		forupdatetotalprice();
		

		// no other attributes changed
		return false;
	}

	@DOpt(type = DOpt.Type.Setter)
	public void setAdditionalservices(Collection<AdditionalServices> additionalservices) 
	{
		this.additionalservices = additionalservices;

		hotelpricecount = additionalservices.size();
		carrentalpricecount = additionalservices.size();
//		forupdatecarrentalprice();
		
	}
	@DOpt(type = DOpt.Type.LinkCountGetter)
	public Integer getHotelpricecount() {
		return hotelpricecount;
	}
	@DOpt(type = DOpt.Type.LinkCountSetter)
	public void setHotelpricecount(int hotelpriceCount) 
	{
		hotelpricecount = hotelpriceCount;
	}
	
	@DOpt(type = DOpt.Type.LinkCountSetter)
	public void setCarrentalpricecount(int carrentalpriceCount) 
	{
		carrentalpricecount = carrentalpriceCount;
	}
	@DOpt(type = DOpt.Type.LinkCountGetter)
	public Integer getCarrentalpricecount() 
	{
		return carrentalpricecount;
	}
	@DOpt(type = DOpt.Type.Getter)
	public Collection<AdditionalServices> getAdditionalservices() 
	{
		return additionalservices;
	}

	
	
	

	public int getId() 
	{
		return id;
	}

	public Double getFlightPrice() 
	{
		return flightPrice;
	}

	public void setFlightPrice(Double flightPrice) 
	{
		this.flightPrice = flightPrice;
	}

	public Flight getFlight() 
	{
		return flight;
	}

	public void setFlight(Flight flight) 
	{
		this.flight = flight;
	}

	public String getCabin() 
	{
		return cabin;
	}

	public void setCabin(String cabin) 
	{
		this.cabin = cabin;
	}

	public String getPromocode()
	{
		return promocode;
	}

	public void setPromocode(String promocode)
	{
		this.promocode = promocode;		
	}

	public Customer getCustomer() 
	{
		return customer;
	}

	public void setCustomer(Customer customer) 
	{
		this.customer = customer;
	}

	public Account getAccount() 
	{
		return account;
	}

	public void setAccount(Account account) 
	{
		this.account = account;
	}




		public static int nextId(Integer currID) 
		{
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
				throws ConstraintViolationException 
		{

			if (minVal != null && maxVal != null) 
			{
				// TODO: update this for the correct attribute if there are more than one auto
				// attributes of this class
				int maxIdVal = (Integer) maxVal;
				if (maxIdVal > idCounter)
					idCounter = maxIdVal;
			}
		}


}
