package cn.itcast.springboot;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//如果是springboot项目则必须要有引导类；类上面必须要添加SpringBootApplication
//是一个组合注解；默认扫描本类及其子包的那些注解
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        //SpringApplication.run(Application.class, args);
        SpringApplication springApplication = new SpringApplication(Application.class);
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run(args);
    }
}
