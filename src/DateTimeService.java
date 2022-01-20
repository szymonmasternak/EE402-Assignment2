/* The Date Time Service Class - Written by Derek Molloy for the EE402 Module
 * See: ee402.eeng.dcu.ie
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeService
{
   public Calendar calendar;

   //constructor creates the Calendar object, could use the constructor:
   //   Calendar(TimeZone zone, Locale aLocale) to explicitly specify 
   //	  the time zone and locale
   public DateTimeService()
   {
	 this.calendar = Calendar.getInstance();
   }
   
   //Constructor for Calendar object, provided UTC time in millis
   public DateTimeService(long millis) {
	   this.calendar = Calendar.getInstance();
	   this.calendar.setTimeInMillis(millis);
   }
   
   //Outputs the Time String corresponding to the Calendar Object
   public String getTimeString() {
	   Date date = Calendar.getInstance().getTime();  
	   return new SimpleDateFormat("hh:mm:ss.SSS").format(date);  
   }

   //method returns date/time as a formatted String object
   public String getDateAndTime()
   {
	 Date d = this.calendar.getTime();
     return "The BeagleBone time is: " + d.toString();	
   }
}