package br.com.cd.server.modules.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import br.com.cd.scaleframework.core.dynamic.ServiceBean;
import br.com.cd.scaleframework.orm.Repository;
import br.com.cd.scaleframework.util.ParserUtils;
import br.com.cd.server.model.ParametroSistema;
import br.com.cd.server.modules.service.ParametroSistemaService;

@ServiceBean(name = "parametroSistemaService", targetEntity = ParametroSistema.class)
public class ParametroSistemaServiceImpl implements ParametroSistemaService {

	private Repository<ParametroSistema, Integer> repository;

	public ParametroSistemaServiceImpl(
			Repository<ParametroSistema, Integer> repository) {
		this.repository = repository;
		System.out.println("\ncreating proxy instance for '"
				+ this.getClass().getName() + "', repository: " + repository);

	}

	private Map<String, String> paramsMap;
	private Date lastLoad = new Date();
	private Timer timer = new Timer();
	private Boolean scheduled = false;

	@PostConstruct
	private void loadFromRepository() {
		List<ParametroSistema> params = repository.findList();
		this.lastLoad = new Date();
		for (ParametroSistema param : params) {
			paramsMap.put(param.getChave(), param.getValor());
		}

		synchronized (this.scheduled) {
			if (!this.scheduled) {
				this.scheduled = true;
				this.schedule();
			}
		}
	}

	@PreDestroy
	public void destroy() {
		timer.cancel();
	}

	private synchronized void schedule() {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(this.lastLoad);
		calendar.add(Calendar.HOUR,
				this.getInt(ParametroSistemaService.RELOAD_PARAMS_TIME));

		timer.schedule(
				new TimerTask() {

					@Override
					public void run() {
						ParametroSistemaServiceImpl.this.loadFromRepository();
					}
				},
				calendar.getTime(),
				this.getInt(ParametroSistemaService.RELOAD_MODULES_TIME) * 60 * 100);
	}

	@Override
	public void reload() {
		loadFromRepository();
	}

	@Override
	public String getString(String key, String defValue) {
		if (this.paramsMap.containsKey(key)) {
			return ParserUtils.parseString(this.paramsMap.get(key), defValue);
		}
		return defValue;
	}

	@Override
	public String getString(String key) {
		if (this.paramsMap.containsKey(key)) {
			return ParserUtils.parseString(this.paramsMap.get(key));
		}
		return "";
	}

	@Override
	public Integer getInt(String key, Integer defValue) {
		return ParserUtils.parseInt(this.getString(key), defValue);
	}

	@Override
	public Integer getInt(String key) {
		return this.getInt(key, 0);
	}

	@Override
	public <T> T getObject(Param<T> param, Class<T> returnType) {
		if (this.paramsMap.containsKey(param.KEY)) {
			return ParserUtils.parseObject(returnType,
					this.paramsMap.get(param.KEY), param.DEFAULT);
		}
		return param.DEFAULT;
	}

	@Override
	public Integer getInt(Param<Integer> param) {
		return this.getObject(param, Integer.class);
	}

	@Override
	public String getString(Param<String> param) {
		return this.getObject(param, String.class);
	}
}