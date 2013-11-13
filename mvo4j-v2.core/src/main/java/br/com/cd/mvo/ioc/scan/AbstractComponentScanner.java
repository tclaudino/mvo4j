package br.com.cd.mvo.ioc.scan;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

public abstract class AbstractComponentScanner implements ComponentScanner {

	protected String[] packagesToScan;

	protected Collection<BeanMetaDataFactory<?, ?>> metaDataFactories = new LinkedHashSet<>();

	public AbstractComponentScanner(String packageToScan, String... packagesToScan) {
		List<String> asList = Arrays.asList(packagesToScan);
		asList.add(packageToScan);
		this.packagesToScan = asList.toArray(new String[asList.size()]);
	}

	@Override
	public void addMetaDataFactories(BeanMetaDataFactory<?, ?> metaDataFactory) {
		this.metaDataFactories.add(metaDataFactory);
	}

	public AbstractComponentScanner(String[] packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	public AbstractComponentScanner(List<String> packageToScan) {
		this.packagesToScan = packageToScan.toArray(new String[packageToScan.size()]);
	}

	@Override
	public int compareTo(ComponentScanner o) {
		if (this.getOrder() < o.getOrder()) {
			return -1;
		}
		if (this.getOrder() > o.getOrder()) {
			return 1;
		}
		return 0;
	}
}
