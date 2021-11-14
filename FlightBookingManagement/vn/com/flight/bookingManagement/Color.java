package vn.com.flight.bookingManagement;


import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DAttr.Type;

public enum Color 
	{
	YELLOW, RED, GREEN, BLUE, GRAY, CYAN, BLACK, MAGENTA;
		  //Others
		  
		  @DAttr(name="name", type=Type.String, id=true, length=10)
		  public String getName() 
		  {
		    return name();
		  }

	}
