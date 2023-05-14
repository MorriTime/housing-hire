# housing—hire

## 项目简介

基于SpringCloud Alibaba为核心房屋租赁社区，采用简易的RBAC模型结合Oauth2实现授权。社区功能包括：房源搜索、房源发布、论坛发表、用户管理等。

## 使用技术

- SpringCloud Alibaba
- Spring GateWay
- Oauth2
- Nacos
- OpenFeign
- Canal

### 数据库

- MySQL
- Redis
- ElasticSearch

## 项目结构

| 主项目 | 第一层        | 第二层              | 项目说明                                   |
| ------ | ------------- | ------------------- | ------------------------------------------ |
| hire   | hire-common   | common-base         | 基础模块，包括认证实体、统一异常、统一返回 |
|  |  | common-elastic | ElasticSearch依赖，ElasticSearch配置 |
|        |               | common-mybatis-plus | MySQL依赖，MyBatis-plus相关配置，分页实体       |
|        |               | common-redis        | Redis依赖以及相关配置，Redis工具类            |
|        |               | common-web          | OpenFeign相关配置，一些枚举类和全局异常 |
|        | hire-security | hire-gateway        | 服务网关，端口9010                         |
|        |               | hire-oauth2   | 服务授权，端口9020                         |
|        | hire-service  | hire-admin-10020    | 管理员相关服务，端口10020                 |
|        |               | hire-base-10000     | 基础服务，端口10000                      |
|        |               | hire-super-10010    | 超级管理员服务，端口10010                |
|        |               | hire-user-10030     | 用户服务，端口10030                      |

## 版本

- Spring Cloud Alibaba：2.2.9.RELEASE
- Spring Cloud：Hoxton.SR12
- Spring Boot：2.3.12.RELEASE

## 相关内容

### Nacos配置

网关配置文件注册在 Naco s中，服务加载的时候从 Nacos 上获取。同时通用的配置，如 MySQL、Redis 等都保存在 Nacos 上

~~~yaml
spring:
  cloud:
    gateway:
      routes: #配置路由路径
        - id: hire-base-10000
          uri: lb://hire-base-10000
          predicates:
            - Path=/api/base/**
          filters:
            - StripPrefix=1
            - name: MyRequestRateLimiter
              args:
                # 令牌桶每秒填充平均速率
                redis-rate-limiter.replenishRate: 1
                # 令牌桶的上限
                redis-rate-limiter.burstCapacity: 100
                # 根据路径来限流
                key-resolver: "#{@pathKeyResolver}"
            - name: MyRequestRateLimiter
              args:
                # 令牌桶每秒填充平均速率
                redis-rate-limiter.replenishRate: 1
                # 令牌桶的上限
                redis-rate-limiter.burstCapacity: 10
                # 根据ip来限流
                key-resolver: "#{@remoteAddrKeyResolver}"

        - id: hire-super-10010
          uri: lb://hire-super-10010
          predicates:
            - Path=/api/super/**
          filters:
            - StripPrefix=1
        - id: hire-admin-10020
          uri: lb://hire-admin-10020
          predicates:
            - Path=/api/admin/**
          filters:
            - StripPrefix=1
        - id: hire-user-10030
          uri: lb://hire-user-10030
          predicates:
            - Path=/api/user/**
          filters:
            - StripPrefix=1
            - name: MyRequestRateLimiter
              args:
                # 令牌桶每秒填充平均速率
                redis-rate-limiter.replenishRate: 1
                # 令牌桶的上限
                redis-rate-limiter.burstCapacity: 100
                # 根据路径来限流
                key-resolver: "#{@pathKeyResolver}"
            - name: MyRequestRateLimiter
              args:
                # 令牌桶每秒填充平均速率
                redis-rate-limiter.replenishRate: 1
                # 令牌桶的上限
                redis-rate-limiter.burstCapacity: 10
                # 根据ip来限流
                key-resolver: "#{@remoteAddrKeyResolver}"

        - id: hire-oauth-server
          uri: lb://hire-oauth-server
          predicates:
            - Path=/auth/**
          filters:
            - StripPrefix=1
      discovery:
        locator:
          enabled: true #开启从注册中心动态创建路由的功能
          lower-case-service-id: true #使用小写服务名，默认是大写

# 自定义白名单
secure:
  ignore:
    urls: #配置白名单路径
      - "/auth/oauth/token"
      - "/api/base/*"
~~~

### 限流返回自定义

自定义 MyRequestRateLimiterGateWayFilterFactory 类，继承 RequestRateLimiterGatewayFilterFactory类，重写 apply 方法。

~~~java
@Override
    public GatewayFilter apply(Config config) {
        KeyResolver resolver = this.getOrDefault(config.getKeyResolver(), this.defaultKeyResolver);
        RateLimiter<Object> limiter = this.getOrDefault(config.getRateLimiter(), this.defaultRateLimiter);
        boolean denyEmpty = getOrDefault(config.getDenyEmptyKey(), this.denyEmptyKey);
        HttpStatusHolder emptyKeyStatus = HttpStatusHolder
                .parse((String) this.getOrDefault(config.getEmptyKeyStatus(), this.getEmptyKeyStatusCode()));

        return (exchange, chain) -> resolver.resolve(exchange).defaultIfEmpty("____EMPTY_KEY__")
                .flatMap(key -> {
                    if ("____EMPTY_KEY__".equals(key)) {
                        if (denyEmpty) {
                            setResponseStatus(exchange, emptyKeyStatus);
                            return exchange.getResponse().setComplete();
                        }
                        return chain.filter(exchange);
                    }
                    // 获取到 routeId
                    String routeId = config.getRouteId();
                    if (routeId == null) {
                        Route route = exchange
                                .getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
                        routeId = route.getId();
                    }
                    log.debug("MultiRequestRateLimiterGatewayFilterFactory::routeId={}", routeId);

                    return limiter.isAllowed(routeId, key).flatMap(response -> {

                        for (Map.Entry<String, String> header : response.getHeaders()
                                .entrySet()) {
                            exchange.getResponse().getHeaders().add(header.getKey(),
                                    header.getValue());
                        }

                        if (response.isAllowed()) {
                            return chain.filter(exchange);
                        } else {
                            // 自定义返回信息
                            ServerHttpResponse httpResponse = exchange.getResponse();
                            httpResponse.setStatusCode(HttpStatus.OK);
                            httpResponse.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                            String body= JSON.toJSONString(ResponseData.Error("操作过于频繁，稍后再试~"));
                            DataBuffer buffer =  httpResponse.bufferFactory().wrap(body.getBytes(Charset.forName("UTF-8")));
                            return httpResponse.writeWith(Mono.just(buffer));
                        }
                    });
                });
    }
~~~

