package Puzzle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Formatter;
import java.util.Random;
import java.util.TooManyListenersException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

/***
 * @author adam Here is definition of playing field logic for moving parts
 *         etc...
 **/
public class NumberTilesField implements Serializable {
	private final static String FILE_PATH = "Game.bin";
	private static File file = new File(FILE_PATH);
	/**
	 * For testing
	 */
	private List<NumberTile> list = new ArrayList<>();
	/**
	 * Gaming field field with objects
	 */
	private NumberTile[][] numberTiles;
	/**
	 * row count = numbers of rows
	 */
	private int rowCount = 0;
	/**
	 * column numbers
	 */
	private int columnCount = 0;
	/**
	 * number of tiles which will have a number
	 */
	private int notEmptyTiles = 0;
	/**
	 * Game state default is PLAYING
	 */
	private GameState gamteState = GameState.PLAYING;

	/**
	 * Constructor will initialized the whole gaming field
	 */
	public NumberTilesField(int rowCount, int columnCount) {
		this.rowCount = rowCount;
		this.columnCount = columnCount;
		this.notEmptyTiles = (rowCount * columnCount) - 1;
		numberTiles = new NumberTile[rowCount][columnCount];
		generateField();

	}

	/**
	 * Array will be shuffled to point if it is solve able
	 */
	private void recursionSolable() {
		if (!checkSolvability()) {
			generateField2();
		} else {
			return;
		}

	}

	/**
	 * Function change the position of tiles and switch them
	 */
	private void changeTile(int lavoPravo, int horeDole) {
		int[] pozicia = huntForPosition(0);
		if (pozicia[0] + horeDole < rowCount && pozicia[0] + horeDole >= 0 && pozicia[1] + lavoPravo < columnCount
				&& pozicia[1] + lavoPravo >= 0) {
			NumberTile pomoc;
			pomoc = numberTiles[pozicia[0]][pozicia[1]];
			numberTiles[pozicia[0]][pozicia[1]] = numberTiles[pozicia[0] + horeDole][pozicia[1] + lavoPravo];
			numberTiles[pozicia[0] + horeDole][pozicia[1] + lavoPravo] = pomoc;
		} else {
			try {
				System.out.println("si mimo pola pockaj 1s a hraj zas");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * Move tile to left
	 */
	public void moveToLeft() {
		changeTile(1, 0);
	}

	/**
	 * Move tile to right
	 */
	public void moveToRight() {
		changeTile(-1, 0);
	}

	/**
	 * Move tile to up
	 */
	public void moveUp() {
		changeTile(0, 1);
	}

	/**
	 * Move tile to down
	 */
	public void moveDown() {
		changeTile(0, -1);
	}

	/**
	 * This method will generate the playing field fill the field with tiles
	 * with unique number
	 */
	private void generateField() {
		Random rn = new Random();
		for (int i = 0; i < notEmptyTiles; i++) {
			int row = rn.nextInt(rowCount);
			int column = rn.nextInt(columnCount);
			generateFiledRecursion(row, column, i + 1);
		}
		int[] position = huntForPosition(null);
		numberTiles[position[0]][position[1]] = new NumberTile(0);
		generateField2();
		recursionSolable();
		Win();
	}

	/**
	 * if you called this metod you can easily win the game in generate metohd
	 * you have to call this method
	 */
	private void Win() {
		int dva = 1;
		for (int row = 0; row < rowCount; row++) {
			for (int column = 0; column < columnCount; column++) {

				if (row == rowCount-1 && column == columnCount-2) {
					numberTiles[row][column] = new NumberTile(0);
				} else {
					numberTiles[row][column] = new NumberTile(dva);
					dva++;
				}

			}
		}
	}

	/**
	 * druha metioda na rozhodenie min
	 */
	private void generateField2() {
		Collections.shuffle(Arrays.asList(numberTiles));
		recursionSolable();
	}

	public void shuffleArray() {
		Collections.shuffle(Arrays.asList(numberTiles));
	}

	/**
	 * give position of object in field
	 */
	public int[] huntForPosition(Object o) {
		int[] position = new int[2];
		for (int row = 0; row < rowCount; row++) {
			for (int column = 0; column < columnCount; column++) {
				if (numberTiles[row][column] == null) {
					position[0] = row;
					position[1] = column;
					return position;
				} else if (numberTiles[row][column] != null) {
					if (numberTiles[row][column].getValue() == 0) {
						position[0] = row;
						position[1] = column;
					}
				}
			}
		}
		return position;
	}

	/**
	 * this function is generating mines in recursive if condition find on the
	 * tile in field is tile with same value start the random again
	 */
	private void generateFiledRecursion(int row, int column, int value) {
		Random rn = new Random();
		if (numberTiles[row][column] instanceof NumberTile) {
			generateFiledRecursion(rn.nextInt(rowCount), rn.nextInt(columnCount), value);
		} else {
			numberTiles[row][column] = new NumberTile(value);
		}

	}

	/**
	 * Check if game is solved if its solved Game.state will by in state solved
	 */
	public void isSolved() {
		int overenie = 1;
		int vysledok = 0;
		for (int row = 0; row < rowCount; row++) {
			for (int column = 0; column < columnCount; column++) {
				if (getNumberTiles(row, column).getValue() == overenie) {
					vysledok++;
				}
				overenie++;
			}
		}

		if (vysledok == notEmptyTiles) {
			gamteState = GameState.SOLVED;
		}
	}

	/**
	 * check if the game can be solved
	 */
	private boolean checkSolvability() {
		for (NumberTile x[] : numberTiles) {
			for (NumberTile y : x) {
				list.add(y);
			}
		}
		String result = inversionType();
		if ((columnCount % 2) != 0 && result == "Even") {
			return true;
		} else if ((result == "Even") && (columnCount % 2) == 0) {
			if (!blankDetector()) {
				return true;
			} else {
				return false;
			}
		} else if (result == "Odd" && (columnCount % 2) == 0) {
			if (blankDetector()) {
				return true;
			} else {
				return false;
			}

		}
		return false;
	}

	/**
	 * If detec if 0 is in array on odd or on even row
	 */
	private boolean blankDetector() {
		int[] position = huntForPosition(0);
		if ((position[0] % 2) == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Giv result if sum of inversion is Odd or Even
	 */
	private String inversionType() {
		int counter = 0;
		int od = 0;
		for (NumberTile numberTile : list) {
			for (int i = od; i < list.size(); i++) {
				if (numberTile.getValue() > list.get(i).getValue() && list.get(i).getValue() != 0) {
					counter++;
				}
			}
			od++;
		}

		if ((counter % 2) == 0) {
			return "Even";
		}
		return "Odd";
	}

	public NumberTile getNumberTiles(int row, int column) {
		return numberTiles[row][column];
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public int getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	public int getNotEmptyTiles() {
		return notEmptyTiles;
	}

	public void setNotEmptyTiles(int notEmptyTiles) {
		this.notEmptyTiles = notEmptyTiles;
	}

	public GameState getGamteState() {
		return gamteState;
	}

	public void setGamteState(GameState gamteState) {
		this.gamteState = gamteState;
	}

	/**
	 * save actual state of game
	 */
	public void saveGame() {
		try (FileOutputStream fout = new FileOutputStream(file);
				ObjectOutputStream oos = new ObjectOutputStream(fout)) {
			oos.writeObject(this);
			System.out.println("Ulozil hru");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Load field if is not there start new game on default playing field
	 */
	public static NumberTilesField load() {
		try (FileInputStream fin = new FileInputStream(file); ObjectInputStream ois = new ObjectInputStream(fin)) {
			NumberTilesField numbertile;
			try {
				numbertile = (NumberTilesField) ois.readObject();
				return numbertile;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return new NumberTilesField(4, 4);
			}
		} catch (Exception ex) {
			return new NumberTilesField(4, 4);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Formatter f = new Formatter(sb);
		for (int row = 0; row < rowCount; row++) {
			for (int column = 0; column < columnCount; column++) {
				f.format("%-3s", numberTiles[row][column].toString());
			}
			f.format("%n");
		}
		return sb.toString();
	}
}
