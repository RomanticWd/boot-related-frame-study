# mybatis&springboot项目

## JDBC实现查询分析
1. 加载JDBC驱动；
2. 建立并获取数据库连接；
3. 创建 JDBC Statements 对象；
4. 设置SQL语句的传入参数；
5. 执行SQL语句并获得查询结果；
6. 对查询结果进行转换处理并将处理结果返回；
7. 释放相关资源（关闭Connection，关闭Statement，关闭ResultSet）；

## mybatis优化
### 1. 连接获取和释放
使用数据库连接池来解决资源浪费的问题,通过DataSource进行隔离解耦,具体所要使用的datasource由用户自己配置决定。
### 2. SQL统一存取
SQL语句统一集中放到配置文件，涉及一个SQL语句的动态加载问题。
### 3. 传入参数映射和动态SQL
if和else + 嵌入变量（比如使用＃变量名＃）
### 4. 结果映射和结果缓存
告诉SQL处理器两点：第一，需要返回什么类型的对象；第二，需要返回的对象的数据结构怎么跟执行的结果映射
SQL语句和传入参数两部分合起来可以作为数据缓存的key值
### 5. 解决重复SQL语句问题
将重复的SQL片段独立成一个SQL块，然后在各个SQL语句引用重复的SQL块
## mybatis待改进
单表操作可不可以不写SQL语句，通过JavaBean的默认映射器生成对应的SQL语句  可以参照mybatis-plus


