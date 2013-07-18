package br.com.cd.scaleframework.core.resources.compressor.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.Collection;

import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import br.com.cd.scaleframework.core.resources.compressor.ResourceCompressor;

import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

public class JsYUICompressor implements ResourceCompressor {

	private String econding = "UTF-8";

	public JsYUICompressor() {
	}

	public JsYUICompressor(String econding) {
		this.econding = econding;
	}

	@Override
	public void compress(String outputName, Collection<String> resourcesIn)
			throws URISyntaxException, IOException {

		File file = new File(outputName);
		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();

		Writer out = new OutputStreamWriter(new FileOutputStream(file, true),
				this.econding);

		try {
			for (String path : resourcesIn) {

				Reader in = new InputStreamReader(this.getClass()
						.getResourceAsStream(path), this.econding);

				JavaScriptCompressor compressor = new JavaScriptCompressor(in,
						new ErrorReporter() {

							public void warning(String message,
									String sourceName, int line,
									String lineSource, int lineOffset) {
								if (line < 0) {
									System.err
											.println("\n[WARNING] " + message);
								} else {
									System.err.println("\n[WARNING] " + line
											+ ':' + lineOffset + ':' + message);
								}
							}

							public void error(String message,
									String sourceName, int line,
									String lineSource, int lineOffset) {
								if (line < 0) {
									System.err.println("\n[ERROR] " + message);
								} else {
									System.err.println("\n[ERROR] " + line
											+ ':' + lineOffset + ':' + message);
								}
							}

							public EvaluatorException runtimeError(
									String message, String sourceName,
									int line, String lineSource, int lineOffset) {
								error(message, sourceName, line, lineSource,
										lineOffset);
								return new EvaluatorException(message);
							}
						});

				compressor.compress(out, -1, false, true, false, false);
			}
		} finally {
			out.close();
		}
	}
}
