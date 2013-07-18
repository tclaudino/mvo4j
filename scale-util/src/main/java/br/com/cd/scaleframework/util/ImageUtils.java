package br.com.cd.scaleframework.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtils {

	public static byte[] toByteArray(BufferedImage bufferedImage, String ext) {
		byte[] fileBlob = new byte[0];

		if (bufferedImage != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
			try {
				ImageIO.write(bufferedImage, ext, baos);

				baos.flush();
				fileBlob = baos.toByteArray();
				baos.close();
			} catch (Exception ex) {
				System.out.println("\n\ntoByteArray Exception: "
						+ ex.getMessage());
			}
		}
		return fileBlob;
	}

	public static BufferedImage toBufferedImage(String fileName) {
		return ImageUtils.toBufferedImage(new File(fileName));
	}

	public static BufferedImage toBufferedImage(File file) {
		if (file != null) {
			try {
				return ImageIO.read(file);
			} catch (Exception ex) {
				System.out.println("\n\ntoBufferedImage Exception: "
						+ ex.getMessage());
			}
		}
		return new BufferedImage(0, 0, BufferedImage.TYPE_INT_BGR);
	}

	public static BufferedImage toBufferedImage(Image image) throws IOException {
		if (image != null) {
			BufferedImage bufferedImage = new BufferedImage(
					image.getWidth(null), image.getHeight(null),
					BufferedImage.TYPE_INT_BGR);
			Graphics2D g = bufferedImage.createGraphics();
			try {
				g.drawImage(image, image.getWidth(null), image.getHeight(null),
						null);
				return bufferedImage;
			} catch (Exception e) {
				System.out.println("\n\ntoByteArray Exception: "
						+ e.getMessage());
			} finally {
				g.dispose();
			}
		}
		return new BufferedImage(0, 0, BufferedImage.TYPE_INT_BGR);
	}

	public static BufferedImage toRedimension(String fileName, int width,
			int height) {
		// System.out.println("\n\noriginal fileName(" + fileName + "), height("
		// + height + "), width(" + width + ")");
		return ImageUtils.toRedimension(new File(fileName), width, height);
	}

	public static BufferedImage toRedimension(File file, int width, int height) {
		// System.out.println("\n\noriginal file(" + file.getName() +
		// "), height(" + height + "), width(" + width + ")");
		return ImageUtils.toRedimension(ImageUtils.toBufferedImage(file),
				width, height);
	}

	public static BufferedImage toRedimension(Image image, int width, int height) {
		if (image == null) {
			return new BufferedImage(1, 1, BufferedImage.TYPE_INT_BGR);
		}

		// Calculos necessários para manter as propoçoes da imagem
		// ("aspect ratio")
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);
		// System.out.println("\n\noriginal sizes, height(" + imageWidth +
		// "), width(" + imageHeight + ")");

		if ((width == 0 && height == 0)
				|| (width == imageWidth && height == imageHeight)) {
			return new BufferedImage(1, 1, BufferedImage.TYPE_INT_BGR);
		} else if (width == 0 && height > 0) {
			if (height > imageHeight) {
				width = (int) (imageWidth + (imageWidth
						* ((double) (height - imageHeight) / imageHeight * 100) / 100));
			} else {
				width = (int) (imageWidth - (imageWidth
						* ((double) (imageHeight - height) / imageHeight * 100) / 100));
			}
		} else if (height == 0 && width > 0) {
			if (width > imageWidth) {
				height = (int) (imageHeight + (imageHeight
						* ((double) (width - imageWidth) / imageWidth * 100) / 100));
			} else {
				height = (int) (imageHeight - (imageHeight
						* ((double) (imageWidth - width) / imageWidth * 100) / 100));
			}
		}

		// Fim do cálculo
		// System.out.println("\n\nnew sizes, height(" + height +
		// "), imageHeight(" + width + ")");
		BufferedImage scaledInstance = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g = scaledInstance.createGraphics();
		try {
			g.drawImage(image, 0, 0, width, height, null);
			// g.drawImage(image.getScaledInstance(width, height, 10000), 0, 0,
			// null);
		} catch (Exception e) {
			System.out.println("\n\nredimension Exception: " + e.getMessage());
		} finally {
			g.dispose();
		}
		return scaledInstance;
	}
}
