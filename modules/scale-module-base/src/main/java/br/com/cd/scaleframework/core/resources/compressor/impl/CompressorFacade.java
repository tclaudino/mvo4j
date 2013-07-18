package br.com.cd.scaleframework.core.resources.compressor.impl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;

import br.com.cd.scaleframework.core.resources.compressor.ResourceCompressor;

public class CompressorFacade implements ResourceCompressor {

	private ResourceCompressor compressor;

	public CompressorFacade(ResourceCompressor compressor) {
		this.compressor = compressor;
	}

	@Override
	public void compress(String outputName, Collection<String> resourcesIn)
			throws URISyntaxException, IOException {

		this.compressor.compress(outputName, resourcesIn);
	}
}
