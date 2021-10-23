

public class Habit {

	//declare the variables
	private int userID;
	private String habbitName;
    private String habbitReason;
    private Date startDate;
	private ArrayList<String> weekdayPlan;
	
 


    
	
	//the following are all constructors
	Habit(int userID) {
		this.userID = userID;
		
	}
	
	Habit(int userID, String habbitName){
		this.userID = userID;
		this.habbitName = habbitName;
		
		}

	Habit(int userID, String habbitName, String habbitReason){
		this.userID = userID;
		this.habbitName = habbitName;
		this.habbitReason = habbitReason;
		}
 
	Habit(int userID, String habbitName, String habbitReason, Date startDate){
		this.userID = userID;
		this.habbitName = habbitName;
		this.habbitReason = habbitReason;
		this.startDate = startDate;
		
		}
	
	//check the length of habbit reasons
	public Boolean habbitReasonCheck(String habbitReason){
		return ( (habbitReason.trim().length() > 0) && (habbitReason.trim().length() <= 30) );
	}
	
	// check the length of habbit title
	public Boolean habbitNameCheck(String habbitName){
		
		        return ( (habbitName.trim().length() > 0) && (habbitName.trim().length() <= 20) );

		
	}
	
	
	public int getUserID(){
		
		return this.userID;
	}
	
	public void setUserID(int uid){
		//implement constraints
		//if passes constraints
		uid = this.userID;
		
	}
	
	
	
	public String getHabbitName(){
		
		return this.habbitName;
	}
	
 public void setHabitName(String habbitName) throws IllegalArgumentException {

        if (habbitNameCheck(habbitName)) {
            this.habbitName = habbitName;
        } else {
            String showError = "Error: Name of the Habbit cannot exceed 20 characters";
            throw new IllegalArgumentException(showError);
        }
    }
	
	public String getHabbitReason(){
		
		return this.habbitReason;
		
	}

    public void setHabbitReeason(String habbitReason){
		//implement constraints
		        if (habbitReasonCheck(habbitReason)) {
            this.habbitReason = habbitReason;
        } else {
            String showError = "Error: Reason behind the habbit cannot exceed 30 characters";
            throw new IllegalArgumentException(showError);
        }
		
		
	}
		
    public Date getStartDate(){
		return this.date;
		
	}
	
	public void setStartDate(Date startDate){
		this.startDate = startDate;
		
		
	}
	
	public ArrayList<String> getWeekdayPlan{
		
		return this.getWeekdayPlan;
		
	}
	
	public void setWeekdayPlan(ArrayList<String> weekdayPlan) {
			this.weekdayPlan = weekdayPlan;
	
	}
}

}