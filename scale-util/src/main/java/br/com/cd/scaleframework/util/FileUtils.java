package br.com.cd.scaleframework.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.FileChannel;

public class FileUtils {

	public static boolean checkIfExist(File file) {
		return file != null && file.exists();
	}

	public static boolean checkNotIfExist(File file) {
		return file == null || !file.exists();
	}

	public static String getExtension(File file) {
		if (file != null) {
			return FileUtils.getExtension(file.getName());
		}
		return "";
	}

	public static String getExtension(String fileName) {
		if (fileName != null) {
			int extDot = fileName != null ? fileName.lastIndexOf('.') : -1;
			String extension = extDot > -1 ? fileName.substring(extDot + 1)
					: "";
			return extension;
		}
		return "";
	}

	public static File getFile(byte[] fileBytes, String fileName) {
		System.out.println("fileName: " + fileName);
		File file = new File(fileName);

		try {
			if (fileBytes != null) {
				// create the output stream.
				FileOutputStream fos = new FileOutputStream(file);

				// send bytes to file
				for (int i = 0; i < fileBytes.length; i++) {
					fos.write(fileBytes[i]);
				}
				fos.close();
			}
		} catch (IOException e) {
			System.out.println("Exception in getFileFromBytes\nMessage: "
					+ e.getMessage());
		}
		return file;
	}

	public static byte[] getBytes(File file) {
		byte[] fileBlob = new byte[0];
		try {
			if (file != null) {

				java.io.FileInputStream fis = new java.io.FileInputStream(file);
				int fileLength = (int) file.length() + 1;
				fileBlob = new byte[fileLength];
				fis.read(fileBlob);
				fis.close();
			}
		} catch (IOException e) {
			System.out.println("getFileByte Exception\nMessage: "
					+ e.getMessage());
		}
		return fileBlob;
	}

	public static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] subDiretorio = dir.list();
			for (int i = 0; i < subDiretorio.length; i++) {
				boolean deletado = deleteDir(new File(dir, subDiretorio[i]));
				if (!deletado) {
					return false;
				}
			}
		}

		// ? O diretorio agora está vazio, então removemos ele:
		return dir.delete();
	}

	// Copia todos os arquivos sob o srcDir para o dstDir.
	// Se dstDir não existir, ele será criado.
	public void copyDirectory(File srcDir, File dstDir) throws IOException {
		if (srcDir.isDirectory()) {
			if (!dstDir.exists()) {
				dstDir.mkdir();
			}

			String[] subDiretorios = srcDir.list();
			for (int i = 0; i < subDiretorios.length; i++) {
				copyDirectory(new File(srcDir, subDiretorios[i]), new File(
						dstDir, subDiretorios[i]));
			}
		} else {
			// Copiando o arquivo
			// Cria um FileChannel para o arquivo de origem:
			FileChannel srcChannel = new FileInputStream(srcDir).getChannel();

			// Cria um FileChannel para o arquivo de destino:
			FileChannel dstChannel = new FileOutputStream(dstDir).getChannel();

			// Copia o conteúdo:
			dstChannel.transferFrom(srcChannel, 0, srcChannel.size());

			// Fecha os FileChannels
			srcChannel.close();
			dstChannel.close();
		}
	}

	public static String getString(String fileName)
			throws FileNotFoundException, IOException {

		StringBuilder build = new StringBuilder();
		FileReader reader = new FileReader(fileName);

		BufferedReader bRead = new BufferedReader(reader);
		while (bRead.ready()) {
			build.append(bRead.readLine());
		}
		System.out.println("Line: " + build.toString());
		return build.toString();
	}

	public static void save(File file, boolean force) throws IOException {
		if (force || FileUtils.checkIfExist(file)) {
			write(file, "");
		}
	}

	public static void save(String name, String str) throws IOException {
		File file = checkFile(new File(name), name);
		if (FileUtils.checkIfExist(file)) {
			write(file, str);
		}
	}

	public static void update(File file, String str) throws IOException {
		if (FileUtils.checkNotIfExist(file)) {
			write(file, str, true);
		}
	}

	public static void update(String name, String str) throws IOException {
		File file = new File(name);
		if (FileUtils.checkNotIfExist(file)) {
			write(file, str, true);
		}
	}

	private static void write(File file, String str) throws IOException {
		write(file, str, false);
	}

	private static void write(File file, String str, boolean append)
			throws IOException {
		FileWriter writer = new FileWriter(file, append);
		PrintWriter out = new PrintWriter(new BufferedWriter(writer));
		out.println(str);
		out.close();
		writer.close();
	}

	public static File checkFile(File file, String name) {

		if (file.isDirectory() || (!file.isDirectory() && !file.isFile())) {
			if (!name.contains(".")) {
				name += ".txt";
			}
			file = new File(file, name);
		}
		return file;
	}

	public static String readFromURL(String url) throws MalformedURLException,
			IOException {
		StringBuffer buffer = new StringBuffer();
		BufferedReader br;
		br = getBufferedReader(url);
		String linha;
		while ((linha = br.readLine()) != null) {
			buffer.append(linha);
		}
		br.close();
		System.out.println(buffer.toString());
		return buffer.toString();
	}

	public static URL getURL(String url) throws MalformedURLException {
		return new URL(url);
	}

	public static InputStream getInputStream(String url)
			throws MalformedURLException, IOException {
		return getURL(url).openStream();
	}

	public static InputStreamReader getInputStreamReader(String url)
			throws MalformedURLException, IOException {
		return new InputStreamReader(getInputStream(url));
	}

	public static BufferedReader getBufferedReader(String url)
			throws MalformedURLException, IOException {
		return new BufferedReader(getInputStreamReader(url));
	}
}
