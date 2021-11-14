package vn.com.flight.customer.reports;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import domainapp.basics.core.dodm.dsm.DSMBasic;
import domainapp.basics.core.dodm.qrm.QRM;
import domainapp.basics.exceptions.DataSourceException;
import domainapp.basics.exceptions.NotPossibleException;
import domainapp.basics.model.Oid;
import domainapp.basics.model.meta.AttrRef;
import domainapp.basics.model.meta.DAssoc;
import domainapp.basics.model.meta.DAssoc.AssocEndType;
import domainapp.basics.model.meta.DAssoc.AssocType;
import domainapp.basics.model.meta.DAssoc.Associate;
import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DAttr.Type;
import domainapp.basics.model.meta.DClass;
import domainapp.basics.model.meta.DOpt;
import domainapp.basics.model.meta.MetaConstants;
import domainapp.basics.model.meta.Select;
import domainapp.basics.model.query.Expression.Op;
import domainapp.basics.model.query.Query;
import domainapp.basics.model.query.QueryToolKit;
import domainapp.basics.modules.report.model.meta.Output;
import vn.com.flight.customerManagement.Customer;


/**
 * @overview 
 * 	Represent the reports about students by name.
 * 
 * @author dmle
 *
 * @version 5.0
 */
@DClass(schema="FLIGHT",serialisable=false)
public class CustomersByNameReport {
  @DAttr(name = "id", id = true, auto = true, type = Type.Integer, length = 5, optional = false, mutable = false)
  private int id;
  private static int idCounter = 0;

  /**input: student name */
  @DAttr(name = "name", type = Type.String, length = 30, optional = false)
  private String name;
  
//  Ex 6.13: Add Student.A_enrolments
  /**output: students whose names match {@link #name} */
  @DAttr(name="students",type=Type.Collection,optional=false, mutable=false,
      serialisable=false,filter=@Select(clazz=Customer.class, 
      attributes={Customer.A_id, Customer.A_name, Customer.A_dob, Customer.A_address, 
    		  Customer.A_email, Customer.A_phone})
      ,derivedFrom={"name"}
      )
  @DAssoc(ascName="customer-by-name-report-has-customer",role="report",
      ascType=AssocType.One2Many,endType=AssocEndType.One,
    associate=@Associate(type=Customer.class,cardMin=0,cardMax=MetaConstants.CARD_MORE
    ))
  @Output
  private Collection<Customer> customer;

  /**output: number of students found (if any), derived from {@link #customer} */
  @DAttr(name = "numCustomers", type = Type.Integer, length = 20, auto=true, mutable=false)
  @Output
  private int numCustomers;
  
  /**
   * @effects 
   *  initialise this with <tt>name</tt> and use {@link QRM} to retrieve from data source 
   *  all {@link Customer} whose names match <tt>name</tt>.
   *  initialise {@link #customer} with the result if any.
   *  
   *  <p>throws NotPossibleException if failed to generate data source query; 
   *  DataSourceException if fails to read from the data source
   * 
   */
  @DOpt(type=DOpt.Type.ObjectFormConstructor)
  @DOpt(type=DOpt.Type.RequiredConstructor)
  public CustomersByNameReport(@AttrRef("name") String name) throws NotPossibleException, DataSourceException {
    this.id=++idCounter;
    
    this.name = name;
    
    doReportQuery();
  }
  
  /**
   * @effects return name
   */
  public String getName() {
    return name;
  }

  /**
   * @effects <pre>
   *  set this.name = name
   *  if name is changed
   *    invoke {@link #doReportQuery()} to update the output attribute value
   *    throws NotPossibleException if failed to generate data source query; 
   *    DataSourceException if fails to read from the data source.
   *  </pre>
   */
  public void setName(String name) throws NotPossibleException, DataSourceException 
  {
//    boolean doReportQuery = (name != null && !name.equals(this.name));
    
    this.name = name;
    
    // DONOT invoke this here if there are > 1 input attributes!
    doReportQuery();
  }

  /**
   * This method is invoked when the report input has be set by the user. 
   * 
   * @effects <pre>
   *   formulate the object query
   *   execute the query to retrieve from the data source the domain objects that satisfy it 
   *   update the output attributes accordingly.
   *  
   *  <p>throws NotPossibleException if failed to generate data source query; 
   *  DataSourceException if fails to read from the data source. </pre>
   */
  @DOpt(type=DOpt.Type.DerivedAttributeUpdater)
  @AttrRef(value="students")
  public void doReportQuery() throws NotPossibleException, DataSourceException 
  {
    // the query manager instance
    
    QRM qrm = QRM.getInstance();
    
    // create a query to look up Student from the data source
    // and then populate the output attribute (students) with the result
    DSMBasic dsm = qrm.getDsm();
    
    
//    6.1: new Object[] {name + "%"});
//    6.2: new Object[] {name + "%", "%" + name});
    
    //TODO: to conserve memory cache the query and only change the query parameter value(s)
    Query q = QueryToolKit.createSearchQuery(dsm, Customer.class, 
        new String[] {Customer.A_name}, 
        new Op[] {Op.MATCH}, 
        new Object[] {name + "%"});
    
    Query q2 = QueryToolKit.createSearchQuery(dsm, Customer.class, 
            new String[] {Customer.A_name}, 
            new Op[] {Op.MATCH}, 
            new Object[] { "%" + name});
    
    Map<Oid, Customer> result = qrm.getDom().retrieveObjects(Customer.class, q);
    if (result != null) 
    {
	      // update the main output data 
	      customer = result.values();
	      
	      // update other output (if any)
	      numCustomers = customer.size();
	    } else {
	      // no data found: reset output
	      resetOutput();
	    }
	  }
 
      
 

  /**
   * @effects 
   *  reset all output attributes to their initial values
   */
  private void resetOutput() 
  {
    customer = null;
    numCustomers = 0;
  }

  /**
   * A link-adder method for {@link #customer}, required for the object form to function.
   * However, this method is empty because students have already be recorded in the attribute {@link #customer}.
   */
  @DOpt(type=DOpt.Type.LinkAdder)
  public boolean addStudent(Collection<Customer> customer) 
  {
    // do nothing
	  
    return false;
  }
  
  /**
   * @effects return customer
   */
  public Collection<Customer> getCustomer() 
  {
    return customer;
  }
  
  /**
   * @effects return numStudents
   */
  public int getNumCustomers() 
  {
    return numCustomers;
  }

  /**
   * @effects return id
   */
  public int getId()
  {
    return id;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  /**
   * @effects 
   * 
   * @version 
   */
  @Override
  public int hashCode() 
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    return result;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  /**
   * @effects 
   * 
   * @version 
   */
  @Override
  public boolean equals(Object obj) 
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CustomersByNameReport other = (CustomersByNameReport) obj;
    if (id != other.id)
      return false;
    return true;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  /**
   * @effects 
   * 
   * @version 
   */
  @Override
  public String toString() 
  {
    return "CustomerByNameReport (" + id + ", " + name + ")";
  }

}