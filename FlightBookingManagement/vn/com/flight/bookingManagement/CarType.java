package vn.com.flight.bookingManagement;

import java.util.Calendar;
import java.util.Date;

import domainapp.basics.exceptions.ConstraintViolationException;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr.Format;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.util.Tuple;

public class CarType {
	
		private static final String A_id = "id";
		private static final String A_Carrental = "carrental";
	  	@DAttr(name=A_id,id=true,auto=true,type=Type.String,length=10,mutable=false,optional=false)
	  	private String id;
	  //static variable to keep track of CarType id
	  	private static int idCounter = 0;
	  
		@DAttr(name = "carName", length = 20, mutable = false, optional = false, type = Type.String,cid=true)
		private String carName;
		
		@DAttr(name = "carBrand", length = 20, mutable = false, optional = false, type = Type.String)
		private String carBrand;
		
		@DAttr(name = "color", type = Type.Domain, length = 10, optional = false)
		private Color color;
		  
		@DAttr(name="capacity",type=Type.Integer,length=20,optional=false)
		private int capacity; 
		
		@DAttr(name = "carmodelYear", type = Type.Date, length = 15, optional = false, format = Format.Date)
		private Date carmodelYear;
		
		@DAttr(name = A_Carrental, type = Type.Domain, length = 20, optional = true)
		@DAssoc(ascName="carrental-has-cartype",role="cartype",
		      ascType=AssocType.One2Many, endType=AssocEndType.Many,
		  associate=@Associate(type=Carrental.class,cardMin=1,cardMax=15))
		  private Carrental carrental;
		  
		  
		  // constructor methods
		  // for creating in the application
		  @DOpt(type = DOpt.Type.ObjectFormConstructor)
		  @DOpt(type = DOpt.Type.RequiredConstructor)
			public CarType(
					@AttrRef("carName") String carName, 
					@AttrRef("carBrand") String carBrand, 
					@AttrRef("color") Color color,
					@AttrRef("capacity") Integer capacity,
					@AttrRef("carmodelYear") Date carmodelYear,
					@AttrRef("carrental") Carrental carrental
					
					) 
			{
				this(null,carName,carBrand, color,capacity,carmodelYear,carrental);
			}
		// a shared constructor that is invoked by other constructors
		  @DOpt(type=DOpt.Type.DataSourceConstructor)
		  public CarType(@AttrRef("id")String id,
				  @AttrRef("carName") String carName, 
					@AttrRef("carBrand") String carBrand, 
					@AttrRef("color") Color color,
					@AttrRef("capacity") Integer capacity,
					@AttrRef("carmodelYear") Date carmodelYear,
					@AttrRef("carrental") Carrental carrental
				  ) 
		  throws ConstraintViolationException 
		  {
		    // generate an id
		    this.id = nextID(id);

		    // assign other values
		    this.carName = carName;
		    this.carBrand = carBrand;
		    this.color = color;
		    this.capacity = capacity;
		    this.carmodelYear = carmodelYear;
		    this.carrental = carrental;


		  }
		  
		 
		public Carrental getCarrental() 
		{
			return carrental;
		}
		public void setCarrental(Carrental carrental) 
		{
			this.carrental = carrental;
		}
		public String getCarName() 
		{
			return carName;
		}
		public void setCarName(String carName) 
		{
			this.carName = carName;
		}
		public String getCarBrand() 
		{
			return carBrand;
		}
		public void setCarBrand(String carBrand) 
		{
			this.carBrand = carBrand;
		}
		public int getCapacity() 
		{
			return capacity;
		}
		public void setCapacity(int capacity) 
		{
			this.capacity = capacity;
		}
		public Date getCarmodelYear() 
		{
			return carmodelYear;
		}
		public void setCarmodelYear(Date carmodelYear) 
		{
			this.carmodelYear = carmodelYear;
		}
		
		public Color getColor() 
		{
			return color;
		}
		public void setColor(Color color)
		{
			this.color = color;
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
		   * @effects returns <code>CarType(id,carName,carBrand,color,capacity,carmodelYear)</code>.
		   */
		  public String toString(boolean full) 
		  {
		    if (full)
		      return "CarType(" + id + "," + carName + ","+ carBrand + ","+ color + "," + capacity + ","
		          + carmodelYear + ")";
		    else
		      return "CarType(" + id + ")";
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
		    CarType other = (CarType) obj;
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
		
		
		
}
