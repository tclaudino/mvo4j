package br.com.cd.mvo.orm;

import org.springframework.data.mongodb.core.MongoOperations;

public interface MongoRepository<T> extends SQLRepository<T, MongoOperations> {

}
