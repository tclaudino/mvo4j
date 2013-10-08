package br.com.cd.mvo.ioc.scan;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public abstract class AbstractComponentScanner implements ComponentScanner {

	protected String[] packageToScan;

	protected Collection<BeanMetaDataFactory<?, ?>> metaDataFactories = new HashSet<>();
	{
		{
			metaDataFactories.add(new ServiceMetaDataFactory());
			metaDataFactories.add(new RepositoryMetaDataFactory());
		}
	}

	public AbstractComponentScanner(String... packageToScan) {
		this.packageToScan = packageToScan;
	}

	public AbstractComponentScanner(List<String> packageToScan) {
		this.packageToScan = packageToScan.toArray(new String[packageToScan
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
