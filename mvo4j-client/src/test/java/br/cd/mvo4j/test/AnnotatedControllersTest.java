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

import br.com.cd.mvo.CrudService;
import br.com.cd.mvo.client.controller.ContactTypeControllerListener;
import br.com.cd.mvo.client.controller.ContractTypeController;
import br.com.cd.mvo.client.controller.ContractTypeListener;
import br.com.cd.mvo.client.model.ContactType;
import br.com.cd.mvo.client.model.ContractType;
import br.com.cd.mvo.client.service.ContactTypeService;
import br.com.cd.mvo.ioc.ConfigurationException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.ContainerConfig;
import br.com.cd.mvo.ioc.ContainerProvider;
import br.com.cd.mvo.ioc.NoSuchBeanDefinitionException;
import br.com.cd.mvo.web.WebCrudController;
import br.com.cd.mvo.web.ioc.WebContainerConfig;

@ContextConfiguration(locations = { "file:src/test/webapp/WEB-INF/applicationContext.xml" }, loader = MockServletContextWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
public class AnnotatedControllersTest extends AbstractTestNGSpringContextTests {

	@Autowired
	ConfigurableWebApplicationContext wac; // cached

	@Autowired
	MockServletContext servletContext; // cached

	// @Autowired
	// private LocalPropertyContainerConfig applicationConfig;
	private Container container = null;

	@BeforeClass
	public void setUp() {

		servletContext.setContextPath("mvo-test");
		servletContext.setInitParameter("br.com.cd.PROVIDER_CLASS", "br.com.cd.mvo.web.ioc.spring.SpringWebContainerProvider");
		servletContext.setInitParameter("br.com.cd.PACKAGES_TO_SCAN", "br.com.cd.mvo.client");

		WebContainerConfig config = new WebContainerConfig(servletContext);

		try {

			ContainerProvider<ContainerConfig<ServletContext>> provider = config.getProvider();

			container = provider.getContainer(config);
			container.start();

		} catch (ConfigurationException e) {
			e.printStackTrace();
			fail();
		}
	}

	@AfterClass
	public void tearDown() throws Exception {
		container.stop();
	}

	@Test(groups = "client1")
	@Transactional
	@Rollback
	public void testContactTypeControllerListener() {

		ContactTypeControllerListener controller = null;
		try {
			controller = container.getBean("contactTypeBean", ContactTypeControllerListener.class);

		} catch (NoSuchBeanDefinitionException e) {
			fail(e.getMessage(), e);
		}
		assertTrue(WebCrudController.class.isAssignableFrom(controller.getClass()),
				"Instance for ContactTypeControllerListener not is instance of 'WebCrudController'");

		@SuppressWarnings("unchecked")
		WebCrudController<ContactType> webCrudController = (WebCrudController<ContactType>) controller;

		webCrudController.toNewMode();
		ContactType entity = new ContactType("TYPE_TEST");
		webCrudController.save(entity);

		assertNotNull(webCrudController.getService().getRepository().find("type", "TYPE_TEST"));
	}

	@Test(groups = "client1")
	@Transactional
	@Rollback
	public void testContactTypeService() {

		ContactTypeService service = null;
		try {
			service = container.getBean(ContactTypeService.class);

		} catch (NoSuchBeanDefinitionException e) {
			fail(e.getMessage(), e);
		}
		assertTrue(CrudService.class.isAssignableFrom(service.getClass()),
				"Instance for ContactTypeService not is instance of 'CrudService'");

		@SuppressWarnings("unchecked")
		CrudService<ContactType> crudService = (CrudService<ContactType>) service;

		ContactType entity = new ContactType("TYPE_TEST");
		entity.setAcronym("ACRONYM_TEST");
		crudService.getRepository().save(entity);

		assertNotNull(crudService.getRepository().find("type", "TYPE_TEST"));

		ContactType testFind = service.testFindLike("YPE_", "EST");
		assertNotNull(testFind);

		testFind = service.testLocalRepository("_TEST");
		assertNotNull(testFind);

		testFind = service.testLocalRepository("_TEST_12383");
		assertNull(testFind);
	}

	@Test(groups = "client1")
	@Transactional
	@Rollback
	public void testContractTypeController() {

		ContractTypeController controller = null;
		try {
			controller = container.getBean("contractTypeBean", ContractTypeController.class);

		} catch (NoSuchBeanDefinitionException e) {
			fail(e.getMessage(), e);
		}
		assertTrue(WebCrudController.class.isAssignableFrom(controller.getClass()),
				"Instance for ContractTypeController not is instance of 'WebCrudController'");

		@SuppressWarnings("unchecked")
		WebCrudController<ContractType> webController = (WebCrudController<ContractType>) controller;

		ContractType entity = new ContractType();
		entity.setAcronym("ACRONYM_TESTE");
		entity.setType("TYPE_TESTE");
		entity.setPeriodicity(2);
		webController.save(entity);

		webController.toFirstPage();
		assertNotNull(webController.getCurrentEntity());
	}

	@Test(groups = "client1")
	@Transactional
	@Rollback
	public void testContractTypeListener() {

		try {
			@SuppressWarnings("unused")
			ContractTypeListener controller = container.getBean(ContractTypeListener.class);

		} catch (NoSuchBeanDefinitionException e) {
			fail(e.getMessage(), e);
		}
	}
}
