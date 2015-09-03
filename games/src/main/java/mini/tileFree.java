package mini;

public class tileFree extends Tile {
	
	@Override
	public String toString() {
		
	  if(this.getState() == State.CLOSED || this.getState() == State.MARKED )
	  {
		 return super.toString();
	  }
		
	  else  
	  {
		return " ";
	  }
	}
	
	
}
