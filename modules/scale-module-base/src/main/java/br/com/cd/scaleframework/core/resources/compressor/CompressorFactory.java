package br.com.cd.scaleframework.core.resources.compressor;

import br.com.cd.scaleframework.core.resources.compressor.impl.CompressorFacade;
import br.com.cd.scaleframework.core.resources.compressor.impl.CssYUICompressor;
import br.com.cd.scaleframework.core.resources.compressor.impl.JsYUICompressor;

public class CompressorFactory {

	public static enum OutputType {
		CSS, JS
	}

	public static ResourceCompressor getCompressor(OutputType resourceType) {

		return CompressorFactory.getCompressor(resourceType, "");
	}

	public static ResourceCompressor getCompressor(OutputType resourceType,
			String econding) {

		ResourceCompressor compressor;
		switch (resourceType) {
		case CSS:
			compressor = new CssYUICompressor(econding);
			break;
		case JS:
			compressor = new JsYUICompressor(econding);
			break;
		default:
			compressor = new CssYUICompressor(econding);
			break;
		}

		return new CompressorFacade(compressor);
	}
}
