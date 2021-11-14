package vn.com.flight.customerManagement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DAttr.Format;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.Select;
import domainapp.basics.util.Tuple;
import vn.com.flight.bookingManagement.Booking;
import vn.com.flight.exceptions.DExCode;
import vn.com.flight.utils.DToolkit;



/**
 * Represents a user. The Account ID is auto-incremented from the current year.
 * 
 * @author G3-SS207
 * @version 2.0
 */
@DClass(schema = "FLIGHT")
public class Account {

	public static final String A_userName = "username";
	public static final String A_password = "password";
	public static final String A_role = "role";
	public static final String A_id = "id";
	public static final String A_passenger = "passenger";
	public static final String A_createdDate = "createdDate";

	// attributes of Account
	@DAttr(name = A_id, id = true, auto = true, length = 6, mutable = false, optional = false, type = Type.String)
	private String id;
	// static variable to keep track of account id
	private static int idCounter = 0;

	@DAttr(name = A_userName, type = Type.String, length = 30, optional = false)
	private String username;

	@DAttr(name = A_password, type = Type.String, length = 30, optional = false)
	private String password;

	@DAttr(name = A_createdDate, type = Type.Date, length = 15, optional = false, format = Format.Date)
	private Date createdDate;


	@DAttr(name = A_role, type = Type.Domain, length = 30, optional = false)
	@DAssoc(ascName = "account-has-role",
	role = "account", 
	ascType = AssocType.One2One, endType = AssocEndType.One,
	associate = @Associate(type = Role.class, cardMin = 1, cardMax = 1))
	private Role role;
	
	@DAttr(name = A_passenger, type = Type.Domain, length = 30, optional = true)
	@DAssoc(ascName = "account-has-customer",
	role = "account", 
	ascType = AssocType.One2One, endType = AssocEndType.One, 
	associate = @Associate(type = Customer.class, cardMin = 1, cardMax = 1))
	private Customer passenger;
	
	@DAttr(name="card",type=Type.Collection,
		      optional=true,serialisable=false,
		      filter=@Select(clazz=Card.class))
		  @DAssoc(ascName="account-has-card",role="account",
		      ascType=AssocType.One2Many,endType=AssocEndType.One,
		      associate=@Associate(type=Card.class,
		      cardMin=1,cardMax=25))  
		  private Collection<Card> card;
	
	
	  @DAttr(name="booking",type=Type.Collection,
		      optional=true,serialisable=false,
		      filter=@Select(clazz=Booking.class))
		  @DAssoc(ascName="account-has-booking",role="account",
		      ascType=AssocType.One2Many,endType=AssocEndType.One,
		      associate=@Associate(type=Booking.class,
		      cardMin=1,cardMax=25))  
		  private Collection<Booking> booking;
	
	  

	// constructor methods
	// for creating in the application

	@DOpt(type = DOpt.Type.ObjectFormConstructor)
	@DOpt(type = DOpt.Type.RequiredConstructor)
	public Account(
			@AttrRef("username") String username, 
			@AttrRef("password") String password, 
			@AttrRef("createdDate") Date createdDate,
			@AttrRef("role") Role role,
			@AttrRef("passenger") Customer passenger) 
	{
		this(null, username, password, createdDate, role, passenger);
	}

	// a shared constructor that is invoked by other constructors
	@DOpt(type = DOpt.Type.DataSourceConstructor)

	public Account( String id,
			 String username, 
			 String password,
			 Date createdDate, 
			 Role role,
			 Customer passenger)
			throws ConstraintViolationException 
	{
		// generate an id
		this.id = nextID(id);

		// assign other values
		this.username = username;
		this.password = password;
		this.role = role;
		this.createdDate = createdDate;
		this.passenger = passenger;
		card = new ArrayList<>();
		booking = new ArrayList<>();
	
	}
	
	  @DOpt(type=DOpt.Type.LinkAdder)
	  //only need to do this for reflexive association: @MemberRef(name="booking")  
	  public boolean addBooking(Booking b) 
	  {
	    if (!this.booking.contains(b)) 
	    {
	      booking.add(b);
	    }
	    
	    // no other attributes changed
	    return false; 
	  }

	  @DOpt(type=DOpt.Type.LinkAdderNew)
	  public boolean addNewBooking(Booking b) 
	  {
	    booking.add(b);	    
	    // no other attributes changed
	    return false; 
	  }
	  
	  @DOpt(type=DOpt.Type.LinkAdder)
	  public boolean addBooking(Collection<Booking> booking)
	  {
	    for (Booking b : booking)
	    {
	      if (!this.booking.contains(b))
	      {
	        this.booking.add(b);
	      }
	    }
	    
	    // no other attributes changed
	    return false; 
	  }

	  @DOpt(type=DOpt.Type.LinkAdderNew)
	  public boolean addNewBooking(Collection<Booking> booking) 
	  {
	    this.booking.addAll(booking);

	    // no other attributes changed
	    return false; 
	  }

	  @DOpt(type=DOpt.Type.LinkRemover)
	  //only need to do this for reflexive association: @MemberRef(name="booking")
	  public boolean removeBooking(Booking b) 
	  {
	    boolean removed = booking.remove(b);   
	    // no other attributes changed
	    return false; 
	  }
	  
	  @DOpt(type=DOpt.Type.Setter)
	  public void setBooking(Collection<Booking> booking)
	  {
	    this.booking = booking;
	    

	  }
	  @DOpt(type=DOpt.Type.Getter)
	  public Collection<Booking> getBooking() 
	  {
	    return booking;
	  }
	  
	  
	  
	  @DOpt(type=DOpt.Type.LinkAdder)
	  //only need to do this for reflexive association: @MemberRef(name="card")  
	  public boolean addCard(Card c) 
	  {
	    if (!this.card.contains(c)) 
	    {
	    	card.add(c);
	    }
	    
	    // no other attributes changed
	    return false; 
	  }

	  @DOpt(type=DOpt.Type.LinkAdderNew)
	  public boolean addNewCard(Card c) 
	  {
		  card.add(c);
	 
	    
	    // no other attributes changed
	    return false; 
	  }
	  
	  @DOpt(type=DOpt.Type.LinkAdder)
	  public boolean addCard(Collection<Card> card) 
	  {
	    for (Card c : card)
	    {
	      if (!this.card.contains(c))
	      {
	        this.card.add(c);
	      }
	    }
	    
	    // no other attributes changed
	    return false; 
	  }

	  @DOpt(type=DOpt.Type.LinkAdderNew)
	  public boolean addNewCard(Collection<Card> card) 
	  {
	    this.card.addAll(card);

	    // no other attributes changed
	    return false; 
	  }

	  @DOpt(type=DOpt.Type.LinkRemover)
	  //only need to do this for reflexive association: @MemberRef(name="card")
	  public boolean removeCard(Card c) 
	  {
	    boolean removed = card.remove(c);
	    
	    
	    // no other attributes changed
	    return false; 
	  }
	  
	  @DOpt(type=DOpt.Type.Setter)
	  public void setCard(Collection<Card> card) 
	  {
	    this.card = card;
	    

	  }
	  @DOpt(type=DOpt.Type.Getter)
	  public Collection<Card> getCard()
	  {
	    return card;
	  }
	  


	

	// setter methods
	public Customer getPassenger() 
	{
		return passenger;
	}

	public void setUsername(String username) 
	{
		this.username = username;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public void setRole(Role role)
	{
		this.role = role;
	}

	public void setPassenger(Customer passenger)
	{
		this.passenger = passenger;
	}

	public void setCreatedDate(Date createdDate) throws ConstraintViolationException 
	{
		// additional validation on createDate
		if (createdDate.before(DToolkit.MIN_DOB))
		{
			throw new ConstraintViolationException(DExCode.INVALID_CREATEDATE, createdDate);
		}

		this.createdDate = createdDate;
	}

	// getter methods
	public String getId()
	{
		return id;
	}

	public String getUsername() 
	{
		return username;
	}

	public String getPassword()
	{
		return password;
	}

	public Role getRole()
	{
		return role;
	}

	public Date getCreatedDate()
	{
		return createdDate;
	}
	public Customer getPassenger(Customer passenger) 
	{
		return passenger;
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
	 * @effects returns <code>Account(id,username,password,createDate, role, passenger)</code>.
	 */
	public String toString(boolean full)
	{
		if (full)
			return "User(" + id + "," + username + "," + password + "," + createdDate + "," + role + "," + passenger +")";
		else
			return "User(" + id + ")";
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
		Account other = (Account) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	// automatically generate the next student id
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
				throw new ConstraintViolationException(ConstraintViolationException.Code.INVALID_VALUE, e,
						new Object[] { id });
			}

			if (num > idCounter)
			{
				idCounter = num;
			}

			return id;
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

			String maxId = (String) maxVal;

			try
			{
				int maxIdNum = Integer.parseInt(maxId.substring(1));

				if (maxIdNum > idCounter) // extra check
					idCounter = maxIdNum;

			} catch (RuntimeException e) {
				throw new ConstraintViolationException(ConstraintViolationException.Code.INVALID_VALUE, e,
						new Object[] { maxId });
			}
		}
	}
}
