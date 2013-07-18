package br.com.cd.scaleframework.core.resources.compressor;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;

public interface ResourceCompressor {

	void compress(String outputName, Collection<String> resourcesIn)
			throws URISyntaxException, IOException;
}
