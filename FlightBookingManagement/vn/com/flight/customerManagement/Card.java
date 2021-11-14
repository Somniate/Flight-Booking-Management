package vn.com.flight.customerManagement;

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
import vn.com.flight.exceptions.DExCode;
import vn.com.flight.utils.DToolkit;



@DClass(schema = "FLIGHT")
public class Card {

	  private static final String A_account = "account";
	  @DAttr(name="id",id=true,auto=true,length=3,mutable=false,optional=false,type=Type.Integer)
	  private int id;
	// static variable to keep track of card id
	  private static int idCounter = 0;
	  @DAttr(name="cardnumber",type=Type.String,length=50,optional=false)
	  private String cardnumber;
	  @DAttr(name = "validuntil", type = Type.Date, length = 15, optional = false, format = Format.Date)
	  private Date validuntil;
	  @DAttr(name="cardverificationcode",type=Type.String,length=50,optional=false)
	  private String cardverificationcode;
	  @DAttr(name="nameoncard",type=Type.String,length=15,optional=false,cid=true)
	  private String nameoncard;
	  
	  @DAttr(name = A_account, type = Type.Domain, length = 20, optional = true)
	  @DAssoc(ascName="account-has-card",role="card",
	      ascType=AssocType.One2Many, endType=AssocEndType.One,
	  associate=@Associate(type=Account.class,cardMin=1,cardMax=25,determinant=true))
	  private Account account;
	  
	  @DOpt(type=DOpt.Type.ObjectFormConstructor)
	  @DOpt(type=DOpt.Type.RequiredConstructor)
	  public Card(@AttrRef("cardnumber") String cardnumber, 
			  @AttrRef("validuntil") Date validuntil,
			  @AttrRef("cardverificationcode") String cardverificationcode,
			  @AttrRef("nameoncard") String nameoncard,
			  @AttrRef("account") Account account
			  ) 
	  {
	    this(null, cardnumber, validuntil,cardverificationcode,nameoncard,account);
	  }
	  @DOpt(type = DOpt.Type.DataSourceConstructor)
	  public Card(@AttrRef("id") Integer id,@AttrRef("cardnumber") String cardnumber, 
			  @AttrRef("validuntil") Date validuntil, 
			  @AttrRef("cardverificationcode") String cardverificationcode,
			  @AttrRef("nameoncard") String nameoncard) 
	  {
		  this(null, cardnumber, validuntil,cardverificationcode,nameoncard,null);
		   
	  }
	  
	  public Card(Integer id, String cardnumber, Date validuntil, String cardverificationcode,String nameoncard,Account account) 
	  {
		    this.id = nextId(id);
		    this.cardnumber = cardnumber;
		    this.validuntil = validuntil;
		    this.cardverificationcode = cardverificationcode;
		    this.nameoncard = nameoncard;
		    this.account = account;
		   
	  }
		public Account getAccount() 
	{
		return account;
	}

	public void setAccount(Account account) 
	{
		this.account = account;
	}

		public String getCardnumber()
	{
		return cardnumber;
	}
	public void setCardnumber(String cardnumber) 
	{
		
		this.cardnumber = cardnumber;
	}
	public Date getValiduntil() 
	{
		return validuntil;
	}
	public void setValiduntil(Date validuntil) throws ConstraintViolationException 
	{
		// additional validation on createDate
		if (validuntil.before(DToolkit.MIN_DATE)) 
		{
			throw new ConstraintViolationException(DExCode.INVALID_DATE, validuntil);
		}

		this.validuntil = validuntil;
	}
	public String getCardverificationcode() 
	{
		return cardverificationcode;
	}
	public void setCardverificationcode(String cardverificationcode) 
	{
		this.cardverificationcode = cardverificationcode;
	}
	public String getNameoncard() 
	{
		return nameoncard;
	}
	public void setNameoncard(String nameoncard) 
	{
		this.nameoncard = nameoncard;
	}
	public int getId() 
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
	   * @effects returns <code>Card(id,cardnumber,validuntil,cardverificationcode,nameoncard)</code>.
	   */
	  public String toString(boolean full) 
	  {
	    if (full)
	      return "Card(" + id + "," + cardnumber + "," + validuntil + "," + cardverificationcode + ","
	          + nameoncard + ")";
	    else
	      return "Card(" + id + ")";
	  }

	  
		private static int nextId(Integer currID) 
		{
		    if (currID == null) 
		    {
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
		      int maxIdVal = (Integer) maxVal;
		      if (maxIdVal > idCounter)  
		        idCounter = maxIdVal;
		    }
		  }
		  
}
