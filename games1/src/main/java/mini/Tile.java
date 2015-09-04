package mini;

/**
 * Tile of a field.
 */
public abstract class Tile {
    
    /** Tile states. */
    public enum State {
        /** Open tile. */
        OPEN, 
        /** Closed tile. */
        CLOSED,
        /** Marked tile. */
        MARKED
    }
    
    /**
     * */
    
    /** Tile state. */ /** 
     * asas
     * void floodFill( int x, int y ) {
   if ( btn( x, y ) isFillable ) {
       fillBtn(x,y);
       floodFill( x+1, y );
       floodFill( x-1, y );
       floodFill( x, y-1 );
       floodFill( x, y+1 );
   } else {
       return;
   }
}
     * 
     * */
    private State state = State.CLOSED;
        
    /**
     * Returns current state of this tile.
     * @return current state of this tile
     */
    public State getState() {
        return state;
    }

    /**
     * Sets current current state of this tile.
     * @param state current state of this tile
     */
    public void setState(State state) {
        this.state = state;
    }

	@Override
	public String toString() {
		
	
		if(this.state == State.CLOSED)
		{
			return "-";
		}
		else
		{
			return "M";
		}
		
	}
    
    
}
