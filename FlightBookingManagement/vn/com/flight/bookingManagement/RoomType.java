package vn.com.flight.bookingManagement;

import domainapp.basics.model.meta.DAttr;
import domainapp.basics.model.meta.DAttr.Type;

public enum RoomType {
	ONE_BED, TWO_BED,THREE_BEDRM, PENTHOUSE;
	
	  @DAttr(name="name", type=Type.String, id=true, length=10)
	  public String getName() 
	  {
	    return name();
	  }
}
