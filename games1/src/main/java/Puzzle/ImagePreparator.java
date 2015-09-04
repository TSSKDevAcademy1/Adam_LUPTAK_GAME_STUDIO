package Puzzle;

import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImagePreparator {

	private static BufferedImage convertCMYK2RGB(BufferedImage image) throws IOException {
		// log.info("Converting a CYMK image to RGB");
		// Create a new RGB image
		BufferedImage rgbImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		// then do a funky color convert
		ColorConvertOp op = new ColorConvertOp(null);
		op.filter(image, rgbImage);
		return rgbImage;
	}

	public static void cutImage(int rows, int cols, String mainImageName, String imagesFirstLetters) {
		try {

			// Provide number of rows and column
			int row = rows;
			int col = cols;

			BufferedImage originalImgage = convertCMYK2RGB(ImageIO.read(new File(mainImageName)));

			// total width and total height of an image
			int tWidth = originalImgage.getWidth();
			int tHeight = originalImgage.getHeight();
			// int tWidth = 240;
			// int tHeight = 240;

			System.out.println("Image Dimension: " + tWidth + "x" + tHeight);

			// width and height of each piece
			int eWidth = tWidth / col;
			int eHeight = tHeight / row;

			int x = 0;
			int y = 0;
			int sum = 1;
			for (int i = 0; i < row; i++) {
				y = 0;
				for (int j = 0; j < col; j++) {
					try {

						BufferedImage SubImgage = originalImgage.getSubimage(y, x, eWidth, eHeight);
						if (i == row - 1 && j == col - 1) {
							File outputfile = new File( imagesFirstLetters +"0.png");
							ImageIO.write(SubImgage, "jpg", outputfile);
						} else {
							File outputfile = new File( imagesFirstLetters + sum + ".png");
							ImageIO.write(SubImgage, "jpg", outputfile);
						}

						sum++;
						y += eWidth;
						System.out.println(y);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println(x);
				x += eHeight;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
