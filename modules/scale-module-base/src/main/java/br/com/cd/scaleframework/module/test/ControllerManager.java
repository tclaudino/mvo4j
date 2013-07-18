package br.com.cd.scaleframework.module.test;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class ControllerManager {

	public <T, ID extends Serializable> Controller<T, ID> getComponent(
			Class<T> type, Class<ID> idTYpe) {

		return new ControllerImpl<T, ID>(type, idTYpe);
	}
}
