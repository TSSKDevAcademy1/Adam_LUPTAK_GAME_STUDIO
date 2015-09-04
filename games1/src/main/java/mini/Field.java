package mini;

import java.util.Formatter;
import java.util.Locale;
import java.util.Random;

import mini.Tile.State;

/**
 * Field represents playing field and game logic.
 */
public class Field {
	/**
	 * Playing field tiles.
	 */
	private final Tile[][] tiles;

	/**
	 * Field row count. Rows are indexed from 0 to (rowCount - 1).
	 */
	private final int rowCount;

	/**
	 * Column count. Columns are indexed from 0 to (columnCount - 1).
	 */
	private final int columnCount;

	/**
	 * Mine count.
	 */
	private final int mineCount;

	/**
	 * Game state.
	 */
	private GameState state = GameState.PLAYING;

	/**
	 * Constructor.
	 *
	 * @param rowCount
	 *            row count
	 * @param columnCount
	 *            column count
	 * @param mineCount
	 *            mine count
	 */
	public Field(int rowCount, int columnCount, int mineCount) {
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.mineCount = mineCount;
		tiles = new Tile[rowCount][columnCount];
		generate();
	}

	/**
	 * Opens tile at specified indeces.
	 *
	 * @param row
	 *            row number
	 * @param column
	 *            column number
	 */
	public void openTile(int row, int column) {
		Tile tile = tiles[row][column];
		if (tile.getState() == Tile.State.OPEN && tile instanceof Clue) {
			Clue clue = (Clue) tile;
			if (clue.getValue() > 0) {

				for (int r = row - 1; r <= row + 1; r++) {
					for (int c = column - 1; c <= column + 1; c++) {
						if (r >= 0 && c >= 0 && c < getColumnCount() && r < getRowCount()) {
							int numberMines = countAdjacentMines(row, column);
							int markedNumber = countAdjacentMarked(row, column);
							if (tiles[r][c].getState() != Tile.State.MARKED && numberMines == markedNumber) {
								tiles[r][c].setState(State.OPEN);
								if (tiles[r][c] instanceof Mine) {
									state = GameState.FAILED;
									return;
								}
							}
						}
					}
				}
			}
			return;
		}

		floodFill(row, column);

		if (tile.getState() == Tile.State.CLOSED) {
			tile.setState(Tile.State.OPEN);
			if (tile instanceof Mine) {
				state = GameState.FAILED;
				return;
			}
			if (isSolved()) {
				state = GameState.SOLVED;
				return;
			}
		}
		if (isSolved()) {
			state = GameState.SOLVED;
			return;
		}
	}

	public void openTile2(int row, int column) {
		Tile tile = tiles[row][column];
		if (tile instanceof Mine) {
			tile.setState(Tile.State.OPEN);
			state = GameState.FAILED;
			return;
		}
		if ((tile instanceof Clue || tile instanceof tileFree) && tile.getState() != Tile.State.OPEN) {
			tile.setState(Tile.State.OPEN);
			if (tile instanceof tileFree) {
				rekuraziaAdjecentMines(row, column);
			}

		}
		// if(tile instanceof tileFree && tile.getState() != Tile.State.OPEN){
		// tile.setState(Tile.State.OPEN);
		// rekuraziaAdjecentMines(row, column);
		// }

		if (isSolved()) {
			state = GameState.SOLVED;
			return;
		}

	}

	private void rekuraziaAdjecentMines(int row, int column) {
		for (int r = row - 1; r <= row + 1; r++) {
			for (int c = column - 1; c <= column + 1; c++) {
				if (r >= 0 && c >= 0 && c < getColumnCount() && r < getRowCount()) {
					openTile2(r, c);
				}
			}
		}
	}

	/**
	 * Marks tile at specified indeces.
	 *
	 * @param row
	 *            row number
	 * @param column
	 *            column number
	 */
	public void markTile(int row, int column) {
		Tile tile = tiles[row][column];

		if (mineCount - getNumberOf(State.MARKED) > 0) {

			if (tile.getState() == State.CLOSED) {
				tile.setState(State.MARKED);
			} else if (tile.getState() == State.MARKED) {
				tile.setState(State.CLOSED);
			}
		} else if (tile.getState() == State.MARKED) {
			tile.setState(State.CLOSED);
		}
	}

	/**
	 * Generates playing field.
	 */
	private void generate() {
		Random random = new Random();// crt shif o automaticky prida kniznicu
		for (int i = 0; i < mineCount; i++) {
			// vygeneruje nahodne suradnice a zistit ci tam je mina ak nieje da
			// ju tam ak je
			// vygeneruje dalsie suradnice
			// int x = (int) (Math.random() * getRowCount());// riadky
			// int y = (int) (Math.random() * getColumnCount());// stlpce
			int r = random.nextInt(rowCount);
			int c = random.nextInt(columnCount);
			rekurzia_mina(r, c);
			// Mine mine = new Mine();
			// tiles[x][y] = mine;
			// Mine mine = new Mine();
			// tiles[1][1] = mine;
		}
		fillClues();
	}

	/**
	 * Returns true if game is solved, false otherwise.
	 *
	 * @return true if game is solved, false otherwise
	 */
	public boolean isSolved() {
		///// poÄ�et vÅ¡etkÃ½ch dlaÅ¾dÃ­c - poÄ�et odokrytÃ½ch dlaÅ¾dÃ­c =
		///// poÄ�et mÃ­n. Z
		if (mineCount == (rowCount * columnCount) - getNumberOf(State.OPEN)) {
			return true;
		} else {
			return false;
		}
	}

	private int getNumberOf(Tile.State state) {
		int number = 0;
		for (int row = 0; row < tiles.length; row++) {
			for (int column = 0; column < tiles.length; column++) {
				if (tiles[row][column].getState() == state) {
					number++;
				}
			}
		}
		return number;
	}

	private int getNumberOfOpenMine(Tile.State state) {
		int number = 0;
		for (int row = 0; row < tiles.length; row++) {
			for (int column = 0; column < tiles.length; column++) {
				if (tiles[row][column].getState() == state) {
					if (tiles[row][column] instanceof Mine)
						number++;
				}
			}
		}
		return number;
	}

	/**
	 * Returns number of adjacent mines for a tile at specified position in the
	 * field.
	 *
	 * @param row
	 *            row number.
	 * @param column
	 *            column number.
	 * @return number of adjacent mines.
	 */
	private int countAdjacentMines(int row, int column) {
		int count = 0;
		for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
			int actRow = row + rowOffset;
			if (actRow >= 0 && actRow < rowCount) {
				for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
					int actColumn = column + columnOffset;
					if (actColumn >= 0 && actColumn < columnCount) {
						if (tiles[actRow][actColumn] instanceof Mine) {
							count++;
						}
					}
				}
			}
		}
		return count;
	}

	private int countAdjacentMarked(int row, int column) {
		int count = 0;
		for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
			int actRow = row + rowOffset;
			if (actRow >= 0 && actRow < rowCount) {
				for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
					int actColumn = column + columnOffset;
					if (actColumn >= 0 && actColumn < columnCount) {
						if (tiles[actRow][actColumn].getState() == Tile.State.MARKED) {
							count++;
						}
					}
				}
			}
		}
		return count;
	}

	private void rekurzia_mina(int row, int columns) {
		Random random = new Random();// crt shif o automaticky prida kniznicu
		if (tiles[row][columns] instanceof Mine) {
			int r = random.nextInt(rowCount);
			int c = random.nextInt(columnCount);
			rekurzia_mina(r, c);
		} else {
			Mine mine = new Mine();
			tiles[row][columns] = mine;
		}
	}

	private void fillClues() {
		int miny = 0;
		for (int row = 0; row < getRowCount(); row++) {
			for (int column = 0; column < getColumnCount(); column++) {
				if (tiles[row][column] instanceof Mine == false) {
					miny = field_adjacent_mines(row, column);
					if (miny > 0) {
						Clue clue = new Clue(miny);
						tiles[row][column] = clue;
						miny = 0;
					} else {
						Clue c = new Clue(0);
						tiles[row][column] = c;
					}
				}
			}
		}
	}

	private int field_adjacent_mines(int row, int column) {
		int miny = 0;
		for (int r = row - 1; r <= row + 1; r++) {
			for (int c = column - 1; c <= column + 1; c++) {
				if (r >= 0 && c >= 0 && c < getColumnCount() && r < getRowCount()) {
					if (tiles[r][c] instanceof Mine) {
						miny++;
					}
				}
			}
		}
		return miny;
	}

	private void floodFill(int row, int column) {
		if (isValid(row, column)) {
			floodFill(row + 1, column);
			floodFill(row - 1, column);
			floodFill(row, column - 1);
			floodFill(row, column + 1);
		} else {
			return;
		}
	}

	private boolean isValid(int row, int column) {
		boolean stav = false;
		if (row >= 0 && row < getRowCount() && column >= 0 && column < getColumnCount()) {
			if (countAdjacentMines(row, column) > 0 && tiles[row][column] instanceof Mine == false) {
				tiles[row][column].setState(State.OPEN);
				return false;
			}

			if (tiles[row][column] instanceof Clue || tiles[row][column] instanceof tileFree) {
				if (tiles[row][column].getState() == State.CLOSED || tiles[row][column].getState() == State.MARKED) {
					tiles[row][column].setState(State.OPEN);
					stav = true;
				}
				if (tiles[row][column] instanceof Mine) {
					stav = false;
				}
			}
		} else {
			stav = false;
		}
		return stav;
	}

	private String cislaX(int count) {
		String vrat = "  ";

		for (int i = 0; i < count; i++) {
			vrat += Integer.toString(i);
			vrat += (i >= 9) ? " " : "  ";
		}
		return vrat + "\n";
	}

	private String vratPismeno(int cislo) {
		String pismeno = "";
		if (cislo > 25) {
			pismeno += (char) ((cislo + 65) - 25);
		} else {
			pismeno += (char) (cislo + 65);
		}
		return pismeno;
	}

	public int getRowCount() {
		return rowCount;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public Tile getTile(int row, int col) {
		return tiles[row][col];
	}

	public void setTiles(Tile tile, int row, int col) {
		tiles[row][col] = tile;
	}

	public int getMineCount() {
		return mineCount;
	}

	public GameState getState() {
		return state;
	}

	public String toString() {
		return toStringFormator();
	}

	public String toStringPrint() {
		int medzery = 5;
		int medzery1 = 5;

		System.out.printf("     ");
		for (int i = 0; i < columnCount; i++) {
			if (i > 9)
				medzery = 6;
			System.out.printf("%5s", i);
		}

		System.out.printf("%n");
		for (int row = 0; row < rowCount; row++) {
			System.out.printf("%5s", tlac(row, rowCount));
			for (int column = 0; column < columnCount; column++) {
				System.out.printf("%5s", tiles[row][column].toString());
			}
			System.out.printf("%n");
		}
		return null;
	}

	public String toStringFormator() {
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb, Locale.US);
		formatter.format("%5s", " ");
		for (int i = 0; i < columnCount; i++) {
			formatter.format("%5s", i);
		}
		formatter.format("%n");
		for (int row = 0; row < rowCount; row++) {
			formatter.format("%5s", tlac(row, rowCount));
			for (int column = 0; column < columnCount; column++) {
				formatter.format("%5s", tiles[row][column].toString());
			}
			formatter.format("%n");
		}
		return sb.toString();
	}

	public int getRemainingMineCount() {

		return (mineCount - getNumberOf(State.MARKED) > 0) ? mineCount - getNumberOf(State.MARKED) : 0;

	}

	static int pocet25(int cislo) {
		int kolko = 1;
		while (cislo > 25) {
			if (cislo % 25 == 0) {
				kolko++;
			}
			cislo--;
		}
		return kolko++;
	}

	private String tlac(int dnu, int od) {
		// TODO Auto-generated method stub
		String pismeno = "";
		int pocet = pocet25(dnu);
		int interval = 25 * pocet;
		// if(dnu > 50) interval = 25 * pocet;
		if (dnu >= interval && dnu != 25) {
			pocet++;
			int p = dnu - interval - 1;
			if (dnu % 25 == 0) {
				p = 25;
				pocet--;
			}
			for (int i = 0; i < pocet; i++) {

				pismeno += (char) (p + 65);
			}
		} else {
			pismeno += (char) (dnu + 65);
		}
		return pismeno;
	}
}
