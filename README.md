# itoken-web-admin
管理员服务消费者

# Feign服务消费 + Hystrix熔断
> **yls**   
> *2020/5/5*
### 1.[搭建服务注册中心](https://www.cnblogs.com/yloved/p/12832063.html)
### 2.创建服务提供者

* 新建一个module，导入依赖包
```xml
        <!--服务注册与发现中心 start-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <!--服务注册与发现中心 end-->
```
* 启动类添加注解 `@EnableDiscoveryClient`
* 添加配置文件application.yml,将服务注册到eureka中
```yaml
spring:
  application:
    name: itoken-service-redis
server:
  port: 8502
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
```
* 创建一个controller，以供消费者调用
```java
@RestController
public class DemoController {
    @PostMapping(value = "/put")
    public String put(String value){

        return value+"ok";
    }
}
```
* 启动服务

### 3.创建服务消费者
* 新建module,导入依赖包
```xml
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
```
```xml
<!--熔断器 start   由于feign自带，所有不需要添加
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
        </dependency>
        熔断器 end-->
        <!--服务消费者 feign   start-->
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
        <!--服务消费者  feign  end-->
```

* 启动类添加注解,如下所示
```java
@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class ItokenWebadminApplication {
    public static void main(String[] args) {
        SpringApplication.run(ItokenWebadminApplication.class, args);
    }
}
```

* 添加配置文件application.yml
```yaml
spring:
  application:
    name: itoken-web-admin

#开启熔断器
feign:
  hystrix:
    enabled: true

server:
  port: 8601

eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
```
* 创建service用于消费服务提供者的服务
```java
//itoken-service-redis是服务提供者的服务名称
//fallback = RedisServiceFallBack.class是熔断操作类
@FeignClient(value = "itoken-service-redis",fallback = RedisServiceFallBack.class)
public interface DemoService {
    @PostMapping(value = "/put")
    public String put(@RequestParam(value = "value") String value);
}
```
* 熔断操作类
```java
@Component
public class RedisServiceFallBack implements DemoService {
    @Override
    public String put(String value) {
        return FallBack.badGateWay();//统一管理熔断信息
    }
}
```
```java
/**
 * 统一管理熔断信息
 */
public class FallBack {
    public static String badGateWay(){
        try {
            return JsonUtil.objectToString(ResultUtil.error(502,"内部错误"));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
```
* 创建controller调用service
```java
@RestController
public class UserController {
    @Autowired
    private DemoService demoService;

    @RequestMapping(value = {"/index"})
    public String index(){
        String json = demoService.put("hello ");
        System.out.println(json);
        return json;
    }
}
```

* 浏览器访问http://localhost:8601/index,将会返回 `hello ok`，表示服务消费成功。