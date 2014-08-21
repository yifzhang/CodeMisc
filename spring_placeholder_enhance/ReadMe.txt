我们在使用spring的时候，如果是多个placeholder的属性文件，我们通常这样配置：
<context:property-placeholder
location="classpath:dao.properties,
          classpath:services.properties,
          classpath:user.properties"
ignore-unresolvable="true"/>

但是，在spring中该实现有一个bug，在这里有详细的说明：https://jira.spring.io/browse/SPR-9989
大意是：如果是多个placeholder，并且使用了@Value的annotation，必须要这多个placeholder属性文件中的第一个文件含有这个属性才行，不然会报错。
这里就是修复了这个问题，并且用spring 的envrionment进行了封装。

