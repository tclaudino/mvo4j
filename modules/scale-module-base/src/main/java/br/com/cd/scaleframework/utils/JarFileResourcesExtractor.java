package br.com.cd.scaleframework.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.springframework.util.AntPathMatcher;

public class JarFileResourcesExtractor {

	private JarFile jarFile;
	private File destinationFolder;
	private String resourcePathPattern;
	private AntPathMatcher pathMatcher = new AntPathMatcher();

	public JarFileResourcesExtractor(JarFile jarFile, File destinationFolder,
			String resourcePathPattern) {
		this.jarFile = jarFile;
		this.destinationFolder = destinationFolder;
		this.resourcePathPattern = resourcePathPattern;
		this.pathMatcher.setPathSeparator(System.getProperty("file.separator"));
	}

	public void extractFiles() throws IOException {
		String fileSeparator = System.getProperty("file.separator");
		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			if (pathMatcher.match(resourcePathPattern, entry.getName())) {
				String fileName = entry.getName().replaceFirst(
						".*\\" + fileSeparator, "");

				InputStream inputStream = jarFile.getInputStream(entry);
				File materializedFile = new File(destinationFolder, fileName);

				FileOutputStream outputStream = new FileOutputStream(
						materializedFile);

				copyAndClose(inputStream, outputStream);
			}
		}
	}

	public static int IO_BUFFER_SIZE = 8192;

	private static void copyAndClose(InputStream in, OutputStream out)
			throws IOException {
		try {
			byte[] b = new byte[IO_BUFFER_SIZE];
			int read;
			while ((read = in.read(b)) != -1) {
				out.write(b, 0, read);
			}
		} finally {
			in.close();
			out.close();
		}
	}
}
