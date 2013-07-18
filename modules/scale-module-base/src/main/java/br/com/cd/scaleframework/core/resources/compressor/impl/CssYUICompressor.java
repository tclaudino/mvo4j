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

import br.com.cd.scaleframework.core.resources.compressor.ResourceCompressor;
import br.com.cd.scaleframework.util.StringUtils;

import com.yahoo.platform.yui.compressor.CssCompressor;

public class CssYUICompressor implements ResourceCompressor {

	private String econding = "UTF-8";

	public CssYUICompressor() {
	}

	public CssYUICompressor(String econding) {
		this.econding = !StringUtils.isNullOrEmpty(econding) ? econding
				: this.econding;
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
				econding);
		try {

			for (String path : resourcesIn) {

				Reader in = new InputStreamReader(this.getClass()
						.getResourceAsStream(path), econding);
				CssCompressor compressor = new CssCompressor(in);

				compressor.compress(out, 0);
			}
		} finally {
			out.close();
		}
	}

}
