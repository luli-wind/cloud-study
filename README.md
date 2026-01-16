

> 基于[尚硅谷Spring Cloud教程](https://www.bilibili.com/video/BV1UJc2ezEFU/)的完整实践 | 含Nacos/OpenFeign/Sentinel/Gateway/Seata

---

## 📚 核心模块总结
### 一、分布式基础与架构演进
- **核心概念**：单体→集群→分布式→微服务架构
- **项目搭建**：微服务项目创建与组件拆分（[代码目录](/services)）

### 二、Nacos 服务治理
#### 1. 注册中心
- **服务注册**：`@EnableDiscoveryClient` + 配置命名空间
- **服务发现**：负载均衡原理（Ribbon）  
- **高频面试题**：
  java
  // 经典负载均衡实现
  @Bean
  @LoadBalanced // 开启客户端负载均衡
  public RestTemplate restTemplate() {
      return new RestTemplate();
  }


#### 2. 配置中心
- **动态刷新**：`@RefreshScope` 注解
- **多环境隔离**：namespace 区分开发/测试/生产环境
- **监听机制**：配置变更实时推送

### 三、OpenFeign 声明式调用
- **远程调用**：
  java
  @FeignClient(name = "user-service", fallback = UserFallback.class)
  public interface UserApi {
      @GetMapping("/users/{id}")
      User getUser(@PathVariable Long id);
  }

- **进阶配置**：
  - 超时控制（默认1s）
  - 重试机制（规避网络抖动）
  - 拦截器（添加认证头）
- **熔断降级**：Fallback 兜底策略

### 四、Sentinel 流量治理
#### 1. 核心规则
| 规则类型       | 应用场景                          | 配置示例                     |
|----------------|-----------------------------------|------------------------------|
| **流控规则**   | QPS/线程数控制                   | 阈值类型+流控模式+流控效果    |
| **熔断规则**   | 慢调用/异常比例熔断              | 熔断策略+恢复时间窗           |
| **热点规则**   | 参数级限流（如高频用户ID拦截）   | 参数索引+限流阈值             |

#### 2. 异常处理
- **统一降级**：`@SentinelResource` 指定 blockHandler/fallback
- **Feign整合**：Sentinel + OpenFeign 熔断联动

### 五、Gateway 网关
#### 路由配置模板
yaml
spring:
  cloud:
    gateway:
      routes:
        ▪ id: order_route

          uri: lb://service-order
          predicates:
            ▪ Path=/api/orders/

          filters:
            ▪ RewritePath=/api/orders/(?<segment>.*), /$\{segment}

            ▪ AddRequestHeader=X-Request-Color, blue


#### 高阶能力
- **动态路由**：Nacos 配置中心热更新路由
- **自定义过滤器**：实现 GlobalFilter 接口
- **跨域处理**：全局 CORS 配置

### 六、Seata 分布式事务
#### 核心流程
1. **事务模式**：AT/TCC/SAGA/XA
2. **执行原理**：
   - TM 开启全局事务 → RM 注册分支 → TC 协调提交/回滚
3. **整合实战**：
   java
   @GlobalTransactional // 开启全局事务
   public void createOrder(Order order) {
       orderDao.save(order);
       stockFeignClient.deduct(order.getProductId()); 
   }


---

## 🚀 项目快速启动
bash
启动Nacos（需提前安装）

sh nacos/bin/startup.sh -m standalone

启动服务模块

mvn spring-boot:run -pl service-order
mvn spring-boot:run -pl service-product

启动网关

mvn spring-boot:run -pl gateway


---

## 💡 学习心得
1. **服务解耦**：通过注册中心实现服务自治，降低耦合度
2. **弹性设计**：熔断+降级+限流构建韧性系统
3. **事务一致性**：Seata 二阶提交解决分布式事务难题
4. **网关价值**：统一入口管理 + 安全防护 + 流量调度

