package mini;

import java.io.IOException;
import java.io.PrintWriter;

import javax.jws.WebService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mini.Field;
import mini.GameState;
import mini.Tile;
import mini.Tile.State;
import mini.Clue;

/**
 * Servlet implementation class Miny
 */
@WebServlet("/hello")
public class Miny extends HttpServlet {
	private static final String A_HREF_HTTP_LOCALHOST_8080_GAMES_HELLO_X = "<a href=\"http://localhost:8080/games1/hello?x=";
	private static final String FIELD_SESSION = "fieldMiny";
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Field field;

		field = (Field) request.getSession().getAttribute(FIELD_SESSION);

		String tester = request.getParameter("test");
		if (field == null) {
			field = new Field(10, 10, 9);
			System.out.println("hra je nova");
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
			int x = Integer.parseInt(request.getParameter("x"));
			int y = Integer.parseInt(request.getParameter("y"));

			if (x >= 0 && y >= 0 && !"1".equals(mark)) {
				field.openTile(x, y);
			}
			if (x >= 0 && y >= 0 && "1".equals(mark)) {
				field.markTile(x, y);
			}
		} catch (Exception e) {
			System.out.println("nejde");
		}
		if (field.getState() == GameState.SOLVED) {
			request.getSession().setAttribute(FIELD_SESSION, null);

		} else if (field.getState() == GameState.FAILED) {
			request.getSession().setAttribute(FIELD_SESSION, null);
		}
		if ("2".equals(game)) {
			request.getSession().setAttribute(FIELD_SESSION, null);
			// field = new Field(10, 10, 9);

		}
		String rows = request.getParameter("rows");
		System.out.println(rows);
		try {
			if (!"".equals(rows) || rows != null) {
				field = new Field(Integer.parseInt(request.getParameter("rows")),
						Integer.parseInt(request.getParameter("columns")),
						Integer.parseInt(request.getParameter("mines")));
				request.getSession().setAttribute(FIELD_SESSION, field);
			}
		} catch (Exception e) {

		}

		out.print("<div style=\"text-align: center  \">");
		out.print(
				"<div style=\"background-color: darkturquoise; margin-top: 0; padding-top: 1em;padding-bottom: 1em;margin-bottom: 10px;\">");
		out.print("<h1>MineSweeper</h1>");
		out.print("</div>");
		String color = "green";
		switch (field.getState()) {
		case PLAYING:
			color = "green";
			break;
		case FAILED:
			color = "red";
			break;
		case SOLVED:
			color = "blue";
			break;
		}
		out.printf("<h2 style=\"color: %s\">GameState: %s</h2>", color, field.getState());
		if ("1".equals(mark)) {
			out.print("<h3 style=\"color: red\">Mines: " + field.getRemainingMineCount() + " MARK ON</h3>");
		} else {
			out.print("<h3>Mines: " + field.getRemainingMineCount() + "</h3>");
		}

		for (int i = 0; i < field.getRowCount(); i++) {
			for (int j = 0; j < field.getColumnCount(); j++) {
				// out.print("<img src=\"resources/gfx/closed.png \">");

				if ("1".equals(mark)) {
					if (field.getState() == GameState.PLAYING) {
						out.print(A_HREF_HTTP_LOCALHOST_8080_GAMES_HELLO_X + i + "&y=" + j + "&mark=" + mark
								+ "\">");
					}
				} else {
					if (field.getState() == GameState.PLAYING) {
						out.print(A_HREF_HTTP_LOCALHOST_8080_GAMES_HELLO_X + i + "&y=" + j + "\">");
					}
				}
				if (field.getTile(i, j).getState() == Tile.State.OPEN
						|| field.getTile(i, j).getState() == Tile.State.MARKED) {

					String adresa = imageGet(field.getTile(i, j));

					out.print("<img src=\"resources/gfx/" + adresa
							+ "\" alt=\"Go to W3Schools!\" width=\"20\" height=\"20\" style=\"cursor: default;vertical-align: text-bottom\" >");

				} else {
					out.print(
							"<img src=\"resources/gfx/closed.png\" width=\"20\" height=\"20\" style=\"cursor: default;vertical-align: text-bottom\" >");
				}
				if (field.getState() == GameState.PLAYING) {
					out.print("</a>");
				}
			}
			out.print("</br>");
		}
		out.print("</div>");
		System.out.println(mark);

		out.print("<div class=\"row, text-center\" style=\"margin-top: 1em;\"; te>");
		out.print("<div class=\"col-sm-12\" style= \"text-align: center;\">");
		out.print("<form action=\"hello\" method=\"get\">");
		out.print(
				"<button type=\"submit\" class=\"btn btn-success\" name=\"newGame\" value=\"2\" style=\"margin-left: 1px;margin-bottom: 1em;\" >New Game</button>");
		out.print(
				"<button type=\"submit\" class=\"btn btn-warning\" name=\"custom\" style=\"margin-left: 11px;margin-bottom: 1em;\">CustomGame</button>");
		if ("1".equals(mark)) {
			out.print(
					"<button type=\"submit\" class=\"btn btn-danger\" name=\"mark\" value=\"0\" style=\"margin-left: 11px;margin-bottom: 1em;\">Mark</button>");
		} else {
			out.print(
					"<button type=\"submit\" class=\"btn btn-primary\" name=\"mark\" value=\"1\" style=\"margin-left: 11px;margin-bottom: 1em;\">Mark</button>");
		}

		out.print("</form>");
		out.print("</div>");
		out.print("</div>");
		if (custom) {
			out.print("<form action=\"hello\" method=\"get\">");
			out.print("<div class=\"row, text-center\" style=\"margin-top: 1em;\"; te>");
			out.print("<div class=\"col-sm-12\" style= \"text-align: center;\">");
			out.print(
					"Rows count &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp <input type=\"text\" name=\"rows\" maxlength=\"4\" style=\"margin-left: 20px;\"  size=\"4\"><br><br>");
			out.print(
					"Columns count &nbsp &nbsp <input type=\"text\" name=\"columns\" maxlength=\"4\" style=\"margin-left: 11px;\" size=\"4\"><br><br>");
			out.print(
					"Mines count <input type=\"text\" name=\"mines\" maxlength=\"4\" size=\"4\" style=\"margin-left: 45px;\"><br><br>");
			out.print("<input type=\"submit\" class=\"btn btn-success\" value=\"Submit\">");
			out.print("<button type=\"submit\" class=\"btn btn-danger\" style=\"margin-left: 11px;\">Cancel</button>");

			out.print("</form>");

			out.print("</div>");
			out.print("</div>");
		}

		out.print("</div>");
		out.print("<footer class=\"text-center\">");
		out.print("<p>Made by ©Adam Luptak, ");
		out.print(
				"Contact information: <a href=\"mailto:adamluptakosice@gmail.com\">adamluptakosice@gmail.com</a>.<br><a href=\"http://localhost:8080/games1/games.html\">MainPage</a></p>");
		out.print("</footer>");
		out.print("</body>");
		out.print("</html>");
	}

	private String imageGet(Tile tile) {
		String adress = null;

		if (tile.getState() == State.MARKED) {
			adress = "marked.png";
			return adress;
		}

		if (tile instanceof Clue) {

			Clue clue = (Clue) tile;

			adress = "open" + Integer.toString(clue.getValue()) + ".png";

			return adress;
		} else if (tile instanceof Mine) {
			return "mine.png";
		}

		return null;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
