package xyz.morlotti.virtualbookcase.webapi;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.transaction.Transactional;

@SpringBootApplication
@EnableTransactionManagement
@EnableFeignClients("xyz.morlotti.virtualbookcase.userwebsite")
public class MyApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(MyApplication.class, args);
	}
}
