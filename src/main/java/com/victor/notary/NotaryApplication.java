package com.victor.notary;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.web3j.protocol.Web3j;

import javax.annotation.PostConstruct;

@MapperScan("com.victor.notary.mapper")
@SpringBootApplication
public class NotaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotaryApplication.class, args);
	}
}
