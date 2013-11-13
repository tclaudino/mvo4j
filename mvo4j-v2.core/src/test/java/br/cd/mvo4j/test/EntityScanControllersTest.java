package br.cd.mvo4j.test;

import static org.testng.Assert.*;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import br.com.cd.mvo.client.controller.ContractTypeListener;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.ContainerConfig;
import br.com.cd.mvo.ioc.ContainerProvider;
import br.com.cd.mvo.web.ioc.WebApplicationConfig;

@ContextConfiguration(locations = { "file:src/test/webapp/WEB-INF/applicationContext.xml" }, loader = MockServletContextWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
public class EntityScanControllersTest extends AbstractTestNGSpringContextTests {

	@Autowired
	ConfigurableWebApplicationContext wac; // cached

	@Autowired
	MockServletContext servletContext; // cached

	// @Autowired
	// private LocalPropertyContainerConfig applicationConfig;
	private Container container = null;

	@BeforeClass(enabled = false)
	public void setUp() {

		servletContext.setContextPath("mvo-test");
		servletContext.setInitParameter("br.com.cd.PROVIDER_CLASS",
				"br.com.cd.mvo.web.ioc.spring.SpringWebContainerProvider");
		servletContext.setInitParameter("br.com.cd.PACKAGES_TO_SCAN",
				"br.com.cd.mvo.client");

		WebApplicationConfig config = new WebApplicationConfig(servletContext);

		try {

			ContainerProvider<ContainerConfig<ServletContext>> provider = config
					.getContainerProvider();

			container = provider.getContainer(config);
			container.start();

		} catch (ConfigurationException e) {
			e.printStackTrace();
			fail();
		}
	}

	@AfterClass(enabled = false)
	public void tearDown() throws Exception {
		container.stop();
	}

	@Test(groups = "client1", enabled = false)
	@Transactional
	@Rollback
	public void testContractTypeListener() {

		try {
			ContractTypeListener controller = container
					.getBean(ContractTypeListener.class);

		} catch (NoSuchBeanDefinitionException e) {
			fail(e.getMessage(), e);
		}
	}
}
