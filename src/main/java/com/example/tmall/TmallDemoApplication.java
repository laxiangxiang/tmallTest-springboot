package com.example.tmall;

import com.example.tmall.util.PortUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 因为 jpa 的dao 做了 链接 redis 的，如果放在同一个包下，会彼此影响，出现启动异常。
 */
@SpringBootApplication
@RestController
@EnableCaching
@EnableElasticsearchRepositories(basePackages = "com.example.tmall.elasticsearch")
@EnableJpaRepositories(basePackages = {"com.example.tmall.dao","com.example.tmall.pojo"})
public class TmallDemoApplication {

	static {
        PortUtil.checkPort(6379,"Redis 服务端",true);
        PortUtil.checkPort(9300,"ElasticSearch 服务端",true);
        PortUtil.checkPort(5601,"Kibana 工具", true);
	}
	public static void main(String[] args) {
		SpringApplication.run(TmallDemoApplication.class, args);
	}

	@RequestMapping(value = "/hello",method = RequestMethod.GET)
	public String hello(){
		return "hello spring boot";
	}
}

