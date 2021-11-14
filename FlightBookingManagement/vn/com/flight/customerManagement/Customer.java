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
import vn.com.flight.customer.reports.CustomersByCityJoinReport;
import vn.com.flight.customer.reports.CustomersByNameReport;
import vn.com.flight.customer.reports.FlightByCityReport;
import vn.com.flight.exceptions.DExCode;
import vn.com.flight.utils.DToolkit;


/**
 * Represents a customer. The customer ID is auto-incremented from the current
 * year.
 * 
 * @author G3-SS207
 * @version 2.0
 */
@DClass(schema="FLIGHT")
public class Customer {
  public static final String A_name = "name";
  public static final String A_id = "id";
  public static final String A_dob = "dob";
  public static final String A_address = "address";
  public static final String A_email = "email";
  public static final String A_phone = "phone";
  public static final String A_DUser = "Duser";
  public static final String A_booking = "booking";
  private static final String A_gender = "gender";
  public static final String A_rptCustomerByName = "rptCustomerByName";
  public static final String A_rptCustomerByCity = "rptCustomerByCity";



  // attributes of customer
  @DAttr(name = A_id, id = true, type = Type.String, auto = true, length = 6, 
      mutable = false, optional = false) 
  private String id;
  //static variable to keep track of customer id
  private static int idCounter = 0;
 
  @DAttr(name = A_name, type = Type.String, length = 30, optional = false,cid = true)
  private String name;
  
  @DAttr(name = A_gender, type = Type.Domain, length = 10, optional = false)
  private Gender gender;
  
  @DAttr(name = A_dob, type = Type.Date, length = 15, optional = false, format = Format.Date)
  private Date dob;
  
  @DAttr(name = A_phone, type = Type.String, length = 30, optional = false)
  private String phone;
  
  @DAttr(name = A_address, type = Type.Domain, length = 20, optional = true)
  @DAssoc(ascName="customer-has-city",role="customer",
      ascType=AssocType.One2One, endType=AssocEndType.Many,
  associate=@Associate(type=City.class,cardMin=1,cardMax=1))
  private City address;

  @DAttr(name = A_email, type = Type.String, length = 30, optional = false)
  private String email;
  
  @DAttr(name="account",type=Type.Collection,
	      optional=true,serialisable=false,
	      filter=@Select(clazz=Account.class))
	  @DAssoc(ascName="account-has-customer",role="customer",
	      ascType=AssocType.One2One,endType=AssocEndType.One,
	      associate=@Associate(type=Account.class,
	      cardMin=1,cardMax=25))  
	  private Collection<Account> account;
  
  @DAttr(name="booking",type=Type.Collection,
	      optional=true,serialisable=false,
	      filter=@Select(clazz=Booking.class))
	  @DAssoc(ascName="customer-has-booking",role="customer",
	      ascType=AssocType.One2Many,endType=AssocEndType.One,
	      associate=@Associate(type=Booking.class,
	      cardMin=1,cardMax=25))  
	  private Collection<Booking> booking;
  
  @DAttr(name=A_rptCustomerByName,type=Type.Domain, serialisable=false, 
	      // IMPORTANT: set virtual=true to exclude this attribute from the object state
	      // (avoiding the view having to load this attribute's value from data source)
	      virtual=true)
	  private CustomersByNameReport rptCustomerByName;
  
  @DAttr(name=A_rptCustomerByCity,type=Type.Domain, serialisable=false, 
	      // IMPORTANT: set virtual=true to exclude this attribute from the object state
	      // (avoiding the view having to load this attribute's value from data source)
	      virtual=true)
	  private CustomersByCityJoinReport rptCustomerByCity;
  

  // constructor methods
  // for creating in the application
  
  @DOpt(type=DOpt.Type.ObjectFormConstructor)
  @DOpt(type=DOpt.Type.RequiredConstructor)
  public Customer (@AttrRef("name") String name,
	  @AttrRef("gender") Gender gender,
      @AttrRef("dob") Date dob, 
      @AttrRef("phone") String phone,       
      @AttrRef("address") City address,
      @AttrRef("email") String email) 
  {
    this(null, name,gender, dob, phone, address, email);
  }

  // a shared constructor that is invoked by other constructors
  @DOpt(type=DOpt.Type.DataSourceConstructor)
  public Customer(String id, String name,Gender gender, Date dob,  String phone,City address, String email) 
  throws ConstraintViolationException 
  {
    // generate an id
    this.id = nextID(id);

    // assign other values
    this.name = name;
    this.gender = gender;
    this.dob = dob;
    this.phone = phone;
    this.address = address;
    this.email = email;

    account = new ArrayList<>();
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
  //only need to do this for reflexive association: @MemberRef(name="students")
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
  //only need to do this for reflexive association: @MemberRef(name="students")  
  public boolean addAccount(Account u)
  {
    if (!this.account.contains(u))
    {
    	account.add(u);
    }
    
    // no other attributes changed
    return false; 
  }

  @DOpt(type=DOpt.Type.LinkAdderNew)
  public boolean addNewAccount(Account u) 
  {
	  account.add(u);
 
    
    // no other attributes changed
    return false; 
  }
  
  @DOpt(type=DOpt.Type.LinkAdder)
  public boolean addAccount(Collection<Account> account) 
  {
    for (Account u : account)
    {
      if (!this.account.contains(u)) 
      {
        this.account.add(u);
      }
    }
    
    // no other attributes changed
    return false; 
  }

  @DOpt(type=DOpt.Type.LinkAdderNew)
  public boolean addNewAccount(Collection<Account> account) 
  {
    this.account.addAll(account);

    // no other attributes changed
    return false; 
  }

  @DOpt(type=DOpt.Type.LinkRemover)
  //only need to do this for reflexive association: @MemberRef(name="customer")
  public boolean removeAccount(Account u) 
  {
    boolean removed = account.remove(u);
    
    
    // no other attributes changed
    return false; 
  }
  
  @DOpt(type=DOpt.Type.Setter)
  public void setAccount(Collection<Account> account) 
  {
    this.account = account;
    

  }
  @DOpt(type=DOpt.Type.Getter)
  public Collection<Account> getAccount()
  {
    return account;
  }
  
  

  // setter methods
  public void setName(String name)
  {
    this.name = name;
  }
  
  public void setGender(Gender gender)
  {
	this.gender = gender;
  }
  public Gender getGender()
  {
	return gender;
  }


public void setDob(Date dob) throws ConstraintViolationException
  {
	    // additional validation on dob
	    if (dob.before(DToolkit.MIN_DOB))
	    {
	      throw new ConstraintViolationException(DExCode.INVALID_DOB, dob);
	    }
	    
	    this.dob = dob;
  }

  public void setAddress(City address) 
  {
    this.address = address;
  }

  // v2.7.3
  public void setNewAddress(City address) 
  {
    // change this invocation if need to perform other tasks (e.g. updating value of a derived attribtes)
    setAddress(address);
  }
  
  public void setEmail(String email) 
  {
	 
    this.email = email;
	  
  }
  
  public void setPhone(String phone) 
  {
	  this.phone = phone;
  }
  


  // getter methods
  public String getId() 
  {
    return id;
  }

  public String getName() 
  {
    return name;
  }

  public Date getDob() 
  {
    return dob;
  }

  public City getAddress() 
  {
    return address;
  }
  
  



  public String getEmail()  
  {
	  return email;  
  }

public String getPhone() 
  {
	  return phone;
  }





public CustomersByNameReport getRptCustomerByName() 
  {
	return rptCustomerByName;
  }


public CustomersByCityJoinReport getRptCustomerByCity() 
  {
	return rptCustomerByCity;
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
   * @effects returns <code>User(id,name,dob,address,email)</code>.
   */
  public String toString(boolean full)
  {
    if (full)
      return "Customer(" + id + "," + name + ","+ gender + ","+ dob + "," + phone + ","
          + address + "," + email + ")";
    else
      return "Customer(" + id + ")";
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
    Customer other = (Customer) obj;
    if (id == null) 
    {
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
      try {
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
