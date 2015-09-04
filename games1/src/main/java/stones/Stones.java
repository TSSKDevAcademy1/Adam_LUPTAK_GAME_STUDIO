package stones;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Puzzle.ImagePreparator;
import Puzzle.NumberTile;
import Puzzle.NumberTilesField;

/**
 * Servlet implementation class Stones
 */
@WebServlet("/hello3")
public class Stones extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String A_HREF_HTTP_LOCALHOST_8080_GAMES_HELLO2_X = "<a href=\"http://localhost:8080/games1/hello3?x=";
	private static final String FIELD_SESSION = "fieldStones";
	

	/**
	 * @see HttpServlet#HttpServlet()
	 */


	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		NumberTilesField field = (NumberTilesField) request.getSession().getAttribute(FIELD_SESSION);

		if (field == null) {
			field = new NumberTilesField(4, 4);
			ImagePreparator.cutImage(4, 4, "D:/workspace/games1/src/main/webapp/resources/gfx/puz.jpg", "D:/workspace/games1/src/main/webapp/resources/gfx/stoneGameImages/puz");
			request.getSession().setAttribute(FIELD_SESSION, field);
		}
		String mark = "";

		PrintWriter out = response.getWriter();
		boolean custom = request.getParameter("custom") != null;

		response.setContentType("text/html");
		out.print("<!DOCTYPE html>");
		out.print("<html>");
		out.print("<head>");
		out.print(
				"<!-- Latest compiled and minified CSS --><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css\"><!-- Optional theme --><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css\"><!-- Latest compiled and minified JavaScript --><script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js\"></script>");
		out.print("<title>Title of the document</title>");
		out.print("</head>");
		out.print("<body>");
		String game = request.getParameter("newGame");
		// System.out.println(request.getParameter("x"));
		// System.out.println(request.getParameter("y"));
		try {
			mark = request.getParameter("mark");
			int row = Integer.parseInt(request.getParameter("x"));
			int column = Integer.parseInt(request.getParameter("y"));

			if (row >= 0 && column >= 0 && !"1".equals(mark)) {
				afterClickMove(row, column, field);
				field.isSolved();
			}
			if (row >= 0 && column >= 0 && "1".equals(mark)) {

			}
		} catch (Exception e) {
			System.out.println("nejde");
		}
		String color = "green";

		if (field.getGamteState() == Puzzle.GameState.SOLVED) {

			request.getSession().setAttribute(FIELD_SESSION, null);
			color = "red";
		}

		if ("2".equals(game)) {
			request.getSession().setAttribute(FIELD_SESSION, null);
			field = new NumberTilesField(4, 4);

		}
		String rows = request.getParameter("rows");
		System.out.println(rows);

		out.print("<div style=\"text-align: center  \">");
		out.print(
				"<div style=\"background-color: darkturquoise; margin-top: 0; padding-top: 1em;padding-bottom: 1em;margin-bottom: 10px;\">");
		out.print("<h1>15 Puzzle Game</h1>");
		out.print("</div>");

		out.printf("<h2 style=\"color: %s\">GameState: %s</h2>", color, field.getGamteState());

		for (int i = 0; i < field.getRowCount(); i++) {
			for (int j = 0; j < field.getColumnCount(); j++) {
				// out.print("<img src=\"resources/gfx/closed.png \">");

				out.print(A_HREF_HTTP_LOCALHOST_8080_GAMES_HELLO2_X + i + "&y=" + j + "\">");
				String imageType = imageGet(field.getNumberTiles(i, j));
				System.out.println(imageType);
				out.print("<img src=\"resources/gfx/stoneGameImages/" + imageType
						+ "\" alt=\"Go to W3Schools!\" width=\"80\" height=\"80\" style=\"cursor: default;vertical-align: text-bottom\" >");

				out.print("</a>");

			}

			out.print("</br>");
		}
		out.print("</div>");
		System.out.println(mark);
		System.out.print(field.toString());
		out.print("<div class=\"row, text-center\" style=\"margin-top: 1em;\"; te>");
		out.print("<div class=\"col-sm-12\" style= \"text-align: center;\">");
		out.print("<form action=\"hello3\" method=\"get\">");
		out.print(
				"<button type=\"submit\" class=\"btn btn-success\" name=\"newGame\" value=\"2\" style=\"margin-left: 1px;margin-bottom: 1em;\" >New Game</button>");
		out.print("</form>");
		out.print("	<footer>");
		out.print("<p>Made by ©Adam Luptak, ");
		out.print(
				"Contact information: <a href=\"mailto:adamluptakosice@gmail.com\">adamluptakosice@gmail.com</a>.<br><a href=\"http://localhost:8080/games1/games.html\">MainPage</a></p>");
		out.print("</footer>");
		out.print("</div>");

		out.print("</div>");

		out.print("</div>");

		out.print("</body>");
		out.print("</html>");
	}

	private String imageGet(NumberTile tile) {

		return "puz" + Integer.toString(tile.getValue()) + ".png";
		
	}

	public void afterClickMove(int buttonRow, int buttonCloumn, NumberTilesField playingField) {

		int row = buttonRow;
		int column = buttonCloumn;
		int[] position = playingField.huntForPosition(0);
		int counter = 0;
		for (int r = position[0] - 1; r <= position[0] + 1; r++) {
			for (int c = position[1] - 1; c <= position[1] + 1; c++) {

				if ((r >= 0 && c >= 0 && c < playingField.getColumnCount() && r < playingField.getRowCount())) {
					if (playingField.getNumberTiles(row, column).getValue() == playingField.getNumberTiles(r, c)
							.getValue()) {

						if (position[0] - 1 == r && position[1] == c) {
							playingField.moveDown();
						} else if (position[0] == r && position[1] + 1 == c) {
							playingField.moveToLeft();
						} else if (position[0] == r && position[1] - 1 == c) {
							playingField.moveToRight();
						} else if (position[0] + 1 == r && position[1] == c) {
							playingField.moveUp();
						}

					}
				}
			}
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
}
