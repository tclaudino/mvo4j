package br.com.cd.mvo.ioc.scan;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

public abstract class AbstractComponentScanner implements ComponentScanner {

	protected String[] packagesToScan;

	protected Collection<BeanMetaDataFactory<?, ?>> metaDataFactories = new TreeSet<>();
	{
		{
			metaDataFactories.add(new ServiceMetaDataFactory());
			metaDataFactories.add(new RepositoryMetaDataFactory());
			metaDataFactories.add(new ControllerListenerMetaDataFactory());
		}
	}

	public AbstractComponentScanner(String packageToScan,
			String... packagesToScan) {
		List<String> asList = Arrays.asList(packagesToScan);
		asList.add(packageToScan);
		this.packagesToScan = asList.toArray(new String[asList.size()]);
	}

	public AbstractComponentScanner(String[] packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	public AbstractComponentScanner(List<String> packageToScan) {
		this.packagesToScan = packageToScan.toArray(new String[packageToScan
				.size()]);
	}

	@Override
	public void addMetaDataFactory(BeanMetaDataFactory<?, ?> metaDataFactory) {
		this.metaDataFactories.add(metaDataFactory);
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
