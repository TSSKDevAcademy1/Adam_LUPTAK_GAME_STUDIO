package mini;

import mini.Tile.State;

/**
 * Clue tile.
 */
public class Clue  extends Tile {
    /** Value of the clue. */
    private final int value;
    
    public int getValue() {
		return value;
	}

	/**
     * Constructor.
     * @param value  value of the clue
     */
    public Clue(int value) {
        this.value = value;
    }

	@Override
	public String toString() {
		
		if(this.getState() == State.CLOSED || this.getState() == State.MARKED)
		  {
			 return super.toString();
		  }
			
		  else
		  {
			return value + "";
		  }
	}
    
   
    
}
