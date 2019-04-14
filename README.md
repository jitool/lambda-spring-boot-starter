

#### 介绍
基于springboot加lambda表达式的函数式编程风格，将类抽象为一个容器，lambda函数作为行为属性，其他属性作为基础属性。

通过@Lambda(name = "test",version = 1)反射加载类并自动注入lambda函数到容器。

通过@LambdaInject(name="test")将函数引入到依赖的类中。

通过@Lambda(name = "函数名",version = 版本号)注解可以注入指定版本的函数，对开闭原则有良好的支持。

通过@LambdaInject(name="test",version =版本号)指定函数版本引入类中，若不指定version属性则使用最新版本的函数

示例：注入函数
(s://github.com/jitool/lambda-spring-boot-starter/blob/master/images/Dome1.png")<br>
