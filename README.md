<font size=4.5>

**Seata**

---

- 文章目录

[TOC]

## 1. 什么是Seata?

> [Seata](https://github.com/seata/seata) 是一款开源的分布式事务解决方案，致力于提供高性能和简单易用的分布式事务服务。Seata 将为用户提供了 AT、TCC、SAGA 和 XA
事务模式，为用户打造一站式的分布式解决方案。微服务体系结构具有高性能和易于使用的分布式事务解决方案

## 2. 发展历史

**蚂蚁金服：**
  
- Xts：扩展事务服务。 蚂蚁金服中间件团队从2007年开始开发分布式事务中间件，该中间件广泛应用于蚂蚁金服服务，解决了数据库和服务之间的数据一致性问题。

- Dtx：分布式事务扩展。 自2013年以来，XTS 已经以 DTX 的名字发布在蚂蚁金服云上。

**阿里巴巴：**
  
- TXC：阿里巴巴中间件团队从2014年开始启动这个项目，以解决由于应用程序体系结构从单一向微型服务转变而导致的分布式事务问题
 
- GTS：全球交易服务。 Txc 作为一个新名字 GTS 的阿里云中间件产品于2016年发布
 
- Fescar：我们从2019年开始启动基于 txc / gts 的开源项目 Fescar，以便在未来与社区密切合作

- Seata：简单可扩展的自治事务架构。 蚂蚁金服加入了 Fescar，使它成为一个更加中立和开放的分布式交易社区，而 Fescar 被重命名为 Seata

## 3. 微服务中的分布式事务问题

传统的单体应用的场景——电商购物。 其业务由3个模块构成（库存、订单和账户），这三个模块使用各自的本地数据源。

在业务发生过程中，本地事务将保证数据的一致性。

![](https://gitee.com/FocusProgram/PicGo/raw/master/68747470733a2f2f63646e2e6e6c61726b2e636f6d2f6c61726b2f302f323031382f706e672f31383836322f313534353239363737303234342d34636564663337652d396463362d346663302d613937662d6634323430623964383634302e706e67.png)

微服务体系结构发生了变化。 上面提到的3个模块被设计成在3个不同数据源之上的3个服务(模式: 每个服务的数据库)。 本地事务自然而然地保证了每个服务中的数据一致性。

![](https://gitee.com/FocusProgram/PicGo/raw/master/68747470733a2f2f63646e2e6e6c61726b2e636f6d2f6c61726b2f302f323031382f706e672f31383836322f313534353239363738313233312d34303239646139632d383830332d343361342d616332662d3663386231653265613434382e706e67.png)

## 4. Seata如何解决分布式事务？

### 4.1 Seata解决分布式事务设计原理：

![](https://gitee.com/FocusProgram/PicGo/raw/master/68747470733a2f2f63646e2e6e6c61726b2e636f6d2f6c61726b2f302f323031382f706e672f31383836322f313534353239363739313037342d33626365376263652d303235652d343563332d393338362d3762393531333564616465382e706e67.png)

### 4.2 如何定义分布式事务：

> 分布式事务是由一批分支事务组成的全局事务，通常分支事务就是本地事务。

![](https://gitee.com/FocusProgram/PicGo/raw/master/68747470733a2f2f63646e2e6e6c61726b2e636f6d2f6c61726b2f302f323031382f706e672f31383836322f313534353031353435343937392d61313865313666362d656434312d343466312d396337612d6264383263346435666639392e706e67.png)

### 4.3 Seata有3个基本组件：

- 事务协调器（TC）：维护全局和分支事务的状态，驱动全局提交或回滚。
- 事务管理器（TM）：定义全局事务的范围：开始全局事务，提交或回滚全局事务。
- 资源管理器（RM）：管理分支事务的资源，与TC通信以注册分支事务和报告分支事务的状态，

![](https://gitee.com/FocusProgram/PicGo/raw/master/68747470733a2f2f63646e2e6e6c61726b2e636f6d2f6c61726b2f302f323031382f706e672f31383836322f313534353031333931353238362d34613930663064662d356664612d343165312d393165302d3261613364333331633033352e706e67.png)

### 4.4 Seata管理分布式事务的典型生命周期：

- TM要求TC开始新的全局事务。 TC生成表示全局事务的XID。

- XID通过微服务的调用链传播。

- RM将本地事务注册为XID到TC的相应全局事务的分支。

- TM要求TC提交或回滚XID的相应全局事务。

- TC在XID的相应全局事务下驱动所有分支事务，以完成分支提交或回滚。
 
![](https://gitee.com/FocusProgram/PicGo/raw/master/68747470733a2f2f63646e2e6e6c61726b2e636f6d2f6c61726b2f302f323031382f706e672f31383836322f313534353239363931373838312d32366661626562392d373166612d346633652d386137612d6663333137643333383966342e706e67.png)

## 5. SpringCloud集成Seata

> [github源码参考地址](https://github.com/FocusProgram/springcloud-seata/tree/master/springcloud-jpa-seata)

### 5.1 运行Seata

#### 5.1.1 Seata下载地址 [https://github.com/seata/seata/releases](https://github.com/seata/seata/releases)

#### 5.1.2 Seata配置文件

> seata server所有的配置都在conf文件夹内，该文件夹内有两个文件我们必须要详细介绍下。
>
> seata server默认使用file（文件方式）进行存储事务日志、事务运行信息，我们可以通过-m db脚本参数的形式来指定，目前仅支持file、db这两种方式。

**file.conf**

该文件用于配置存储方式、透传事务信息的NIO等信息，默认对应registry.conf文件内的file方式配置。

**registry.conf**

seata server核心配置文件，可以通过该文件配置服务注册方式、配置读取方式。

注册方式目前支持file 、nacos 、eureka、redis、zk、consul、etcd3、sofa等方式，默认为file，对应读取file.conf内的注册方式信息。

读取配置信息的方式支持file、nacos 、apollo、zk、consul、etcd3等方式，默认为file，对应读取file.conf文件内的配置。

#### 5.1.3 运行Seata

**windows环境下**

```
seata-server.bat -p 8091 -m file
```

**Linux环境下**

```
sh seata-server.sh -p 8091 -m file                       #以前台运行方式运行seata

nohup sh seata-server.sh -p 8091 -h 127.0.0.1 -m file &> seata.log &            #以后台方运行式启动seata
```

> --host -h 指定绑定主机号，默认0.0.0.0
>
> --port -p 指定监听端口号，默认8091
>
> --storeMOde -m 日志存储方式（file、db）,默认file

### 5.2 初始化sql脚本

```
# Account
DROP SCHEMA IF EXISTS db_account;
CREATE SCHEMA db_account;
USE db_account;

CREATE TABLE `account_tbl`
(
    `id`      INT(11) NOT NULL AUTO_INCREMENT,
    `user_id` VARCHAR(255) DEFAULT NULL,
    `money`   INT(11)      DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

INSERT INTO account_tbl (id, user_id, money)
VALUES (1, '1001', 10000);
INSERT INTO account_tbl (id, user_id, money)
VALUES (2, '1002', 10000);

CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

# Order
DROP SCHEMA IF EXISTS db_order;
CREATE SCHEMA db_order;
USE db_order;

CREATE TABLE `order_tbl`
(
    `id`             INT(11) NOT NULL AUTO_INCREMENT,
    `user_id`        VARCHAR(255) DEFAULT NULL,
    `commodity_code` VARCHAR(255) DEFAULT NULL,
    `count`          INT(11)      DEFAULT '0',
    `money`          INT(11)      DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

# Storage
DROP SCHEMA IF EXISTS db_storage;
CREATE SCHEMA db_storage;
USE db_storage;

CREATE TABLE `storage_tbl`
(
    `id`             INT(11) NOT NULL AUTO_INCREMENT,
    `commodity_code` VARCHAR(255) DEFAULT NULL,
    `count`          INT(11)      DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE KEY `commodity_code` (`commodity_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


INSERT INTO storage_tbl (id, commodity_code, count)
VALUES (1, '2001', 1000);

CREATE TABLE `undo_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) NOT NULL,
  `context` varchar(128) NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`,`branch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```

### 5.3 项目结构

- order-servie 订单服务
- business-service 商户服务
- order-service 订单服务
- storage-service 仓储服务

![](https://gitee.com/FocusProgram/PicGo/raw/master/20200311152423.png)

### 5.4 引入maven依赖

```
<dependency>
    <groupId>io.seata</groupId>
    <artifactId>seata-all</artifactId>
    <version>1.1.0</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>5.0.4.RELEASE</version>
</dependency>
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.11</version>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
    <version>2.0.0.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
    <version>2.0.0.RELEASE</version>
</dependency>
```

### 5.5 配置文件

> file.conf 的 service.vgroup_mapping 配置必须和spring.application.name一致
在 org.springframework.cloud:spring-cloud-starter-alibaba-seata的org.springframework.cloud.alibaba.seata.GlobalTransactionAutoConfiguration类中，默认会使用 ${spring.application.name}-fescar-service-group作为服务名注册到 Seata Server上，如果和file.conf中的配置不一致，会提示 no available server to connect错误
>
> 也可以通过配置 spring.cloud.alibaba.seata.tx-service-group修改后缀，但是必须和file.conf中的配置保持一致

![](https://gitee.com/FocusProgram/PicGo/raw/master/20200311154141.png)

![](https://gitee.com/FocusProgram/PicGo/raw/master/20200311154205.png)

**application.properties**

```
spring.application.name=account-service
server.port=8083
spring.datasource.url=jdbc:mysql://localhost:3306/db_account?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.show-sql=true
spring.cloud.alibaba.seata.tx-service-group=my_group
```

**file.conf**

```
service {
  vgroupMapping.my_group = "account-service"
  account-service.grouplist = "127.0.0.1:8091"
  enableDegrade = false
  disableGlobalTransaction = false
}
```

### 5.6 启动项目，显示如下证明启动成功：

![](https://gitee.com/FocusProgram/PicGo/raw/master/20200311154701.png)

![](https://gitee.com/FocusProgram/PicGo/raw/master/20200311154722.png)

![](https://gitee.com/FocusProgram/PicGo/raw/master/20200311154807.png)

![](https://gitee.com/FocusProgram/PicGo/raw/master/20200311154749.png)

### 5.7 测试：

无错误成功提交：

```
curl http://127.0.0.1:8084/purchase/commit
```

> 完成后可以看到数据库中 account_tbl的id为1的money会减少 5，order_tbl中会新增一条记录，storage_tbl的id为1的count字段减少 1

发生异常事务回滚:

```
curl http://127.0.0.1:8084/purchase/rollback
```

> 此时 account-service 会抛出异常，发生回滚，待完成后数据库中的数据没有发生变化，回滚成功

## 6. SpringCloud集成Seata+Nacos

> [github源码参考地址](https://github.com/FocusProgram/springcloud-seata/tree/master/springcloud-nacos-seata)

### 6.1 运行Seata

#### 6.1.1 编辑配置文件conf/registry.conf

> 注：serverAddr不能带‘http://’前缀

```
registry {
  # file 、nacos 、eureka、redis、zk、consul、etcd3、sofa
  type = "nacos"

  nacos {
    serverAddr = "114.55.34.44"
    namespace = ""
    cluster = "default"
  }
}
config {
  # file、nacos 、apollo、zk、consul、etcd3
  type = "nacos"
  nacos {
    serverAddr = "114.55.34.44"
    namespace = ""
    cluster = "default"
  }
}
```

#### 6.1.2 编辑配置文件conf/nacos-config.txt(仅限seata-service-1.1.0版本以下)

service.vgroup_mapping.${your-service-gruop}=default，中间的${your-service-gruop}为自己定义的服务组名称，服务中的application.properties文件里配置服务组名称。

demo中有两个服务，分别是storage-service和order-service，所以配置如下:

```
service.vgroup_mapping.storage-service-group=default
service.vgroup_mapping.order-service-group=default
```

初始化seata的nacos配置：

```
cd conf
sh nacos-config.sh 114.55.34.44  #114.55.34.44为nacos的服务器地址
```

seata-service-1.1.0版本在Nacos手动添加配置:

```
service.vgroupMapping.storage-service-group=default
service.vgroupMapping.order-service-group=default
```

启动seata-service:

```
cd bin
sh seata-server.sh -p 8091 -m file
```

成功启动后显示如下：

![](https://gitee.com/FocusProgram/PicGo/raw/master/20200311205043.png)

![](https://gitee.com/FocusProgram/PicGo/raw/master/20200311205111.png)

#### 6.1.3 初始化sql脚本

```
-- 创建 order库、业务表、undo_log表
create database seata_order;
use seata_order;

DROP TABLE IF EXISTS `order_tbl`;
CREATE TABLE `order_tbl` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) DEFAULT NULL,
  `commodity_code` varchar(255) DEFAULT NULL,
  `count` int(11) DEFAULT 0,
  `money` int(11) DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `undo_log`
(
  `id`            BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `branch_id`     BIGINT(20)   NOT NULL,
  `xid`           VARCHAR(100) NOT NULL,
  `context`       VARCHAR(128) NOT NULL,
  `rollback_info` LONGBLOB     NOT NULL,
  `log_status`    INT(11)      NOT NULL,
  `log_created`   DATETIME     NOT NULL,
  `log_modified`  DATETIME     NOT NULL,
  `ext`           VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


-- 创建 storage库、业务表、undo_log表
create database seata_storage;
use seata_storage;

DROP TABLE IF EXISTS `storage_tbl`;
CREATE TABLE `storage_tbl` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `commodity_code` varchar(255) DEFAULT NULL,
  `count` int(11) DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`commodity_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `undo_log`
(
  `id`            BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `branch_id`     BIGINT(20)   NOT NULL,
  `xid`           VARCHAR(100) NOT NULL,
  `context`       VARCHAR(128) NOT NULL,
  `rollback_info` LONGBLOB     NOT NULL,
  `log_status`    INT(11)      NOT NULL,
  `log_created`   DATETIME     NOT NULL,
  `log_modified`  DATETIME     NOT NULL,
  `ext`           VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;

-- 初始化库存模拟数据
INSERT INTO seata_storage.storage_tbl (id, commodity_code, count) VALUES (1, 'product-1', 9999999);
INSERT INTO seata_storage.storage_tbl (id, commodity_code, count) VALUES (2, 'product-2', 0);
```

#### 6.1.4 引入maven依赖

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>

<!-- nacos -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
    <version>0.2.1.RELEASE</version>
</dependency>

<!-- seata-->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-alibaba-seata</artifactId>
    <version>2.1.0.RELEASE</version>
</dependency>
<dependency>
    <groupId>io.seata</groupId>
    <artifactId>seata-all</artifactId>
    <version>1.1.0</version>
</dependency>

<!-- mysql -->
<dependency>
    <groupId>com.work</groupId>
    <artifactId>base-framework-mysql-support</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

#### 6.1.5 配置文件registy.conf

```
registry {
  # file 、nacos 、eureka、redis、zk、consul、etcd3、sofa
  type = "nacos"

  nacos {
    serverAddr = "114.55.34.44"
    namespace = ""
    cluster = "default"
  }
}
config {
  # file、nacos 、apollo、zk、consul、etcd3
  type = "nacos"
  nacos {
    serverAddr = "114.55.34.44"
    namespace = ""
    cluster = "default"
  }
}
```

#### 6.1.6 配置文件application.properties

**order-service**

```
spring.application.name=order-service
server.port=9091

# Nacos 注册中心地址
spring.cloud.nacos.discovery.server-addr = 114.55.34.44:8848

# seata 服务分组，要与服务端nacos-config.txt中service.vgroup_mapping的后缀对应
spring.cloud.alibaba.seata.tx-service-group=order-service-group
logging.level.io.seata = debug

# 数据源配置
spring.datasource.druid.url=jdbc:mysql://114.55.34.44:3306/seata_order?allowMultiQueries=true
spring.datasource.druid.driverClassName=com.mysql.jdbc.Driver
spring.datasource.druid.username=root
spring.datasource.druid.password=root
```

**storage-service**
```
spring.application.name=storage-service
server.port=9092

# Nacos 注册中心地址
spring.cloud.nacos.discovery.server-addr = 114.55.34.44:8848

# seata 服务分组，要与服务端nacos-config.txt中service.vgroup_mapping的后缀对应
spring.cloud.alibaba.seata.tx-service-group=storage-service-group
logging.level.io.seata = debug

# 数据源配置
spring.datasource.druid.url=jdbc:mysql://114.55.34.44:3306/seata_storage?allowMultiQueries=true
spring.datasource.druid.driverClassName=com.mysql.jdbc.Driver
spring.datasource.druid.username=root
spring.datasource.druid.password=root
```

#### 6.1.7 启动项目

![](https://gitee.com/FocusProgram/PicGo/raw/master/20200312000829.png)

#### 6.1.8 测试

分布式事务成功，模拟正常下单、扣库存

```
curl localhost:9091/order/placeOrder/commit
```

分布式事务失败，模拟下单成功、扣库存失败，最终同时回滚

```
curl localhost:9091/order/placeOrder/rollback
```











