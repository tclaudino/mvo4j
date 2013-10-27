package br.cd.mvo4j.test;

import javax.servlet.ServletContext;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ConfigurableWebApplicationContext;

import br.com.cd.mvo.client.controller.ContactTypeControllerListener;
import br.com.cd.mvo.client.controller.ContractTypeController;
import br.com.cd.mvo.client.controller.ContractTypeListener;
import br.com.cd.mvo.client.model.ContactType;
import br.com.cd.mvo.client.model.ContractType;
import br.com.cd.mvo.client.service.ContactTypeService;
import br.com.cd.mvo.core.ConfigurationException;
import br.com.cd.mvo.core.CrudService;
import br.com.cd.mvo.core.NoSuchBeanDefinitionException;
import br.com.cd.mvo.ioc.Container;
import br.com.cd.mvo.ioc.ContainerConfig;
import br.com.cd.mvo.ioc.ContainerProvider;
import br.com.cd.mvo.web.WebCrudController;
import br.com.cd.mvo.web.ioc.WebApplicationConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/test/webapp/WEB-INF/applicationContext.xml" }, loader = MockServletContextWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
public class ControllerTest {

	@Autowired
	ConfigurableWebApplicationContext wac; // cached

	@Autowired
	MockServletContext servletContext; // cached

	// @Autowired
	// private LocalPropertyContainerConfig applicationConfig;
	private Container container = null;

	@Before
	public void setUp() {

		if (container != null)
			return;

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
			Assert.fail();
		}
	}

	@After
	public void tearDown() throws Exception {
		container.stop();
	}

	@Test
	@Transactional
	@Rollback
	public void testContactTypeControllerListener() {

		ContactTypeControllerListener controller = null;
		try {
			controller = container.getBean(ContactTypeControllerListener.class);

		} catch (NoSuchBeanDefinitionException e) {
			e.printStackTrace();
			Assert.fail();
		}
		Assert.assertTrue(
				"Instance for ContactTypeControllerListener not is instance of 'WebCrudController'",
				WebCrudController.class.isAssignableFrom(controller.getClass()));

		WebCrudController<ContactType> webCrudController = (WebCrudController<ContactType>) controller;

		webCrudController.toNewMode();
		ContactType entity = new ContactType("TYPE_TEST");
		webCrudController.save(entity);

		Assert.assertNotNull(webCrudController.getService().getRepository()
				.find("type", "TYPE_TEST"));
	}

	@Test
	@Transactional
	@Rollback
	public void testContactTypeService() {

		ContactTypeService service = null;
		try {
			service = container.getBean(ContactTypeService.class);

		} catch (NoSuchBeanDefinitionException e) {
			e.printStackTrace();
			Assert.fail();
		}
		Assert.assertTrue(
				"Instance for ContactTypeService not is instance of 'CrudService'",
				CrudService.class.isAssignableFrom(service.getClass()));

		CrudService<ContactType> crudService = (CrudService<ContactType>) service;

		ContactType entity = new ContactType("TYPE_TEST");
		crudService.getRepository().save(entity);

		Assert.assertNotNull(crudService.getRepository().find("type",
				"TYPE_TEST"));

		ContactType testFind = service.testFindLike(1, "TESTE");
		Assert.assertNotNull(testFind);

		testFind = service.testLocalRepository(1);
		Assert.assertNotNull(testFind);
	}

	@Test
	@Transactional
	@Rollback
	public void testContractTypeController() {

		ContractTypeController controller = null;
		try {
			controller = container.getBean(ContractTypeController.class);

		} catch (NoSuchBeanDefinitionException e) {
			e.printStackTrace();
			Assert.fail();
		}
		Assert.assertTrue(
				"Instance for ContractTypeController not is instance of 'WebCrudController'",
				WebCrudController.class.isAssignableFrom(controller.getClass()));

		WebCrudController<ContractType> webController = (WebCrudController<ContractType>) controller;

		ContractType entity = new ContractType();
		entity.setAcronym("ACRONYM_TESTE");
		entity.setType("TYPE_TESTE");
		entity.setPeriodicity(2);
		webController.save(entity);

		webController.toPreviousPage();
		Assert.assertNotNull(webController.getCurrentEntity());
	}

	@Test
	@Transactional
	@Rollback
	public void testContractTypeListener() {

		try {
			ContractTypeListener controller = container
					.getBean(ContractTypeListener.class);

		} catch (NoSuchBeanDefinitionException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
}
