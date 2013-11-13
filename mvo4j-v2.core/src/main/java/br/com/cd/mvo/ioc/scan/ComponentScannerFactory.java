package br.com.cd.mvo.ioc.scan;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;

public class ComponentScannerFactory {

	public static final String BEAN_NAME = ComponentScannerFactory.class.getName();

	private String[] packagesToScan;
	private Collection<ComponentScanner> scanners = new TreeSet<>();

	public ComponentScannerFactory(String... packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	public String[] getPackagesToScan() {
		return packagesToScan;
	}

	public void setPackagesToScan(Collection<String> packagesToScan) {
		this.setPackagesToScan(packagesToScan.toArray(new String[packagesToScan.size()]));
	}

	public void setPackagesToScan(String... packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	public void addComponentScanner(ComponentScanner scanner) {
		this.scanners.add(scanner);
	}

	public Collection<ComponentScanner> getComponentScanners() {
		return scanners;
	}

	public void setComponentScanner(ComponentScanner... scanners) {
		this.setComponentScanners(Arrays.asList(scanners));
	}

	public void setComponentScanners(List<ComponentScanner> scannerList) {
		this.scanners.clear();
		this.scanners.addAll(scannerList);
	}

}