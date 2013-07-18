package br.com.cd.scaleframework.core.resources.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.XmlWebApplicationContext;

import br.com.cd.scaleframework.core.resources.DynamicResourceType;
import br.com.cd.scaleframework.core.resources.ResourceType;
import br.com.cd.scaleframework.core.resources.Resources;
import br.com.cd.scaleframework.core.resources.Resources.ResourceInfo;
import br.com.cd.scaleframework.core.resources.StaticResourceType;
import br.com.cd.scaleframework.core.resources.compressor.CompressorFactory;
import br.com.cd.scaleframework.core.resources.compressor.ResourceCompressor;
import br.com.cd.scaleframework.core.resources.service.ResourcesService;
import br.com.cd.scaleframework.util.ParserUtils;
import br.com.cd.scaleframework.util.StringUtils;

@Component
public class DefaultResourcesServiceImpl implements ResourcesService,
		ApplicationListener<ContextRefreshedEvent> {

	Logger logger = LoggerFactory.getLogger(DefaultResourcesServiceImpl.class);

	private Map<ResourceType, Resources> resourcesMap = new LinkedHashMap<ResourceType, Resources>();

	private String ENABLE_COMPRESSION = "enableResourcesCompression";
	private String RESOURCES_FOLDER = "resourcesFolder";

	private String resourcesFolder = System.getProperty("java.io.tmpdir");

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		boolean enableCompression = ParserUtils.parseBoolean(
				((XmlWebApplicationContext) event.getApplicationContext())
						.getServletContext().getInitParameter(
								ENABLE_COMPRESSION), false);

		ServletContext servletContext = ((XmlWebApplicationContext) event
				.getApplicationContext()).getServletContext();

		String folder = ParserUtils
				.parseString(
						servletContext.getRealPath("/")
								+ System.getProperty("file.separator")
								+ "resources"
								+ System.getProperty("file.separator"),
						resourcesFolder);

		folder = ParserUtils.parseString(
				servletContext.getInitParameter(RESOURCES_FOLDER), folder);

		if (enableCompression) {
			this.compress(folder);
			this.doCopyResources(folder, StaticResourceType.CSS,
					StaticResourceType.JS);
		} else {
			this.doCopyResources(folder);
		}
	}

	@Override
	public void compress(String resourcesFolder) {

		this.resourcesFolder = !StringUtils.isNullOrEmpty(resourcesFolder) ? resourcesFolder
				: this.resourcesFolder;

		new File(this.resourcesFolder).mkdirs();

		this.compressCssFiles();
		this.compressJSFiles();
	}

	@Override
	public void copyResources(String resourcesFolder) {
		this.copyResources(resourcesFolder, false);
	}

	@Override
	public void copyResources(String resourcesFolder, boolean compress) {

		if (compress) {
			this.compress(resourcesFolder);
			this.doCopyResources(resourcesFolder, StaticResourceType.CSS,
					StaticResourceType.JS);
		} else {
			this.doCopyResources(resourcesFolder);
		}
	}

	private void doCopyResources(String resourcesFolder,
			ResourceType... excludedResourceTypes) {

		this.resourcesFolder = !StringUtils.isNullOrEmpty(resourcesFolder) ? resourcesFolder
				: this.resourcesFolder;

		new File(this.resourcesFolder).mkdirs();

		List<ResourceType> resourceTypes = new LinkedList<ResourceType>(
				Arrays.asList(excludedResourceTypes));
		resourceTypes.add(StaticResourceType.NONE);

		for (ResourceType resourceType : this.resourcesMap.keySet()) {

			if (!resourceTypes.contains(resourceType)) {
				this.doCopyResources(resourceType);
			}
		}
	}

	private void doCopyResources(ResourceType resourceType) {

		Map<String, ResourceInfo> map = this.getResources(resourceType)
				.getPaths();
		for (Map.Entry<String, ResourceInfo> entry : map.entrySet()) {

			String dir = entry.getKey().replaceAll(
					"[a-zA-Z0-9_&$#-]*\\" + resourceType.getExtension(), "");

			File fileDest = new File(this.resourcesFolder
					+ System.getProperty("file.separator") + dir);
			fileDest.mkdirs();

			fileDest = new File(fileDest, entry.getKey().replace(dir, ""));
			if (fileDest.exists()) {
				fileDest.delete();
			}

			try {
				fileDest.createNewFile();

				FileUtils.copyInputStreamToFile(
						this.getClass().getResourceAsStream(
								entry.getValue().getInternalPath()), fileDest);

				this.resourcesMap
						.get(resourceType)
						.getPaths()
						.put(entry.getKey(),
								new ResourceInfo(entry.getValue()
										.getInternalPath(), "file:"
										+ fileDest.getPath(), entry.getValue()
										.getUri()));
			} catch (IOException e) {
				logger.error("can't copy %d to directory '%d'",
						entry.getValue(), this.resourcesFolder);
			}
		}
	}

	@Override
	public ResourcesService addResources(ResourceType resourceType,
			String internalPath, String filePath, String uri) {

		Resources resources = this.resourcesMap.get(resourceType);
		if (resources == null) {
			resources = new Resources(resourceType);
			this.resourcesMap.put(resourceType, resources);
		}
		resources.addPath(internalPath, filePath, uri);
		return this;
	}

	@Override
	public Resources getResources(ResourceType resourceType) {

		Resources resources = this.resourcesMap.get(resourceType);
		return resources != null ? resources : new Resources(
				StaticResourceType.NONE);
	}

	private void compressCssFiles() {

		ResourceCompressor compressor = CompressorFactory
				.getCompressor(CompressorFactory.OutputType.CSS);
		try {

			this.doCompress(compressor, StaticResourceType.CSS);

		} catch (Exception e) {
			logger.error("can't compress CSS files", e);
		}
	}

	private void compressJSFiles() {

		ResourceCompressor compressor = CompressorFactory
				.getCompressor(CompressorFactory.OutputType.JS);

		try {

			this.doCompress(compressor, StaticResourceType.JS);

		} catch (Exception e) {
			logger.error("can't compress JS files", e);
		}
	}

	private Map<ResourceType, String> compresseds = new LinkedHashMap<ResourceType, String>();

	private void doCompress(ResourceCompressor compressor,
			StaticResourceType resourceType) throws Exception {

		String outputName = resourcesFolder + generateFileName(resourceType);

		Map<String, ResourceInfo> rs = this.getResources(resourceType)
				.getPaths();

		Collection<String> paths = new LinkedList<String>();
		for (ResourceInfo resourceInfo : rs.values()) {
			paths.add(resourceInfo.getInternalPath());
		}
		try {
			compressor.compress(outputName, paths);
			compresseds.put(resourceType, outputName);
		} catch (Exception e) {
			throw e;
		}
	}

	private String generateFileName(StaticResourceType resourceType) {
		return new SimpleDateFormat("yyyyMMdd-HHmm").format(new Date())
				+ resourceType.getExtension();
	}

	@Override
	public String getResource(String resourceName) {

		ResourceType resourceType = this.extractResourceType(resourceName);

		String compressedName = compresseds.get(resourceType);
		if (compressedName == null) {
			Resources resources = this.resourcesMap.get(resourceType);
			if (resources != null) {
				ResourceInfo resourceInfo = resources.getPaths().get(
						resourceName);
				if (resourceInfo != null) {
					compressedName = ParserUtils.parseString(
							resourceInfo.getFilePath(), "");
				}
			}
		}
		return compressedName;
	}

	private ResourceType extractResourceType(String resourceName) {

		String ext = resourceName.substring(resourceName.indexOf("."));

		return DynamicResourceType.getResourceType(ext);
	}
}
