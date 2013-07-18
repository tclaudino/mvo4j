package br.com.cd.scaleframework.core.modules;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.web.context.WebApplicationContext;

import br.com.cd.scaleframework.core.modules.service.ModuleFactoryRegistry;
import br.com.cd.scaleframework.core.modules.service.support.AbstractModuleDiscovery;
import br.com.cd.scaleframework.core.resources.service.ResourcesService;

public abstract class ScheduledModulesLoader extends AbstractModuleDiscovery {

	public ScheduledModulesLoader(ModuleFactoryRegistry registry,
			ResourcesService resourcesService, WebApplicationContext context) {
		super(registry, resourcesService, context);
	}

	private Timer timer = new Timer();

	private Date lastLoad = new Date();

	private Boolean scheduled = false;

	protected abstract int getSchedulePeriod();

	@PostConstruct
	public void init() {
		this.dicoverModules();
		this.lastLoad = new Date();

		synchronized (this.scheduled) {
			if (!this.scheduled) {
				this.scheduled = true;
				this.schedule();
			}
		}
	}

	@PreDestroy
	public void destroy() {
		this.timer.cancel();
	}

	private void schedule() {

		if (this.getSchedulePeriod() <= 0) {
			return;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.lastLoad);
		calendar.add(Calendar.HOUR, this.getSchedulePeriod());

		this.timer.schedule(new TimerTask() {

			@Override
			public void run() {
				ScheduledModulesLoader.this.dicoverModules();
			}
		}, calendar.getTime(), getSchedulePeriod() * 60 * 100);
	}
}
