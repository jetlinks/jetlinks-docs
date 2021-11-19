
JetLinks 使用[hsweb-easyorm](https://github.com/hs-web/hsweb-easy-orm)实现响应式的ORM. 

## DAO

`easyorm`封装了`r2dbc`实现了动态DDL,DSL动态条件等便捷操作.实现一个增删改查只需要2步.

创建实体类,使用jpa注解描述映射关系.

```java

@Data
@Table(name="s_test")
public class TestEntity extends GenericEntity<String> {

    @Column
    private String name;

    @Column
    @EnumCodec  //使用枚举进行编解码
    @ColumnType(javaType = String.class)//指定数据库数据类型
    @DefaultValue("disabled")//默认值
    private TestEnum state;

    @Column
    @JsonCodec //json编解码
    @ColumnType(jdbcType = JDBCType.CLOB, javaType = String.class)
    private Map<String,Object> configuration;
}

```

在需要使用crud的地方直接注入`ReactiveRepository<TestEntity,String>`即可,如:

```java

@Autowired
ReactiveRepository<TestEntity,String> testRepository;

```

::: tip
启动类上需要注解: `@EnableEasyormRepository("实体类所在包,如: org.jetlinks.community.**.entity")`.
:::

### 自定义通用查询条件



## Service

### CRUD Service
hsweb提供了`GenericReactiveCrudService`,使用`ReactiveRepository`实现通用增删该查.例如:

```java

public class TestService extends GenericReactiveCrudService<TestEntity,String>{
}

```

### 支持缓存的Service

`GenericReactiveCacheSupportCrudService`

```java
@Service
public class TestService extends GenericReactiveCacheSupportCrudService<TestEntity, String> {

    @Override
    public String getCacheName() {
        return "test-entity-cache";
    }
}
```

### 支持树结构实体的Service

`GenericReactiveTreeSupportCrudService`,提供了对树结构数据对一些通用处理,如`List`与`树结构`互转.

```java
@Service
public class TestService extends GenericReactiveTreeSupportCrudService<TestEntity, String> {

}
```

::: tip
树结构实体需要实现: `TreeSortSupportEntity`接口,或者继承`GenericTreeSortSupportEntity`
:::

## Web

hsweb和jetlinks都使用`注解式`来声明web映射,方式与`spring-mvc`类似.

### Web Crud

增删改查与`Service`类似,实现接口:`ReactiveCrudController`(基于`ReactiveRepository`),或者`ReactiveServiceCrudController`基于(`ReactiveCrudService`)即可.树结构实体还可以实现:`ReactiveTreeServiceQueryController`接口.

具体的接口内容请查看对应源代码.

## 动态查询

平台的Controller和Service均支持动态查询,查询参数说明[请看这里](/interface-guide/query-param.md).

## 权限控制

hsweb提供来一套统一的权限控制API,方便进行细粒度的权限控制.

### 注解式

常用注解:

1. `@Resource`: 用于定义资源,通常注解在类上.
2. `@ResourceAction`: 定义对资源对操作,通常注解在方法上.
3. `@QueryAction`: 继承自`@ResourceAction(id = "query", name = "查询")`
4. `@SaveAction`: 继承自`@ResourceAction(id = "save", name = "保存")`
5. `@DeleteAction`: 继承自`@ResourceAction(id = "delete", name = "删除")`
6. `@Authorze`: 声明权限控制,注解在类或者方法上,可通过注解属性配置控制权限方式.
7. `@EnableAopAuthorize`: 注解在启动类上,开启权限控制.

::: tip
`@ResourceAction`通常使用注解继承的方式来使用,可以更好的管理操作定义.详细使用方法可参照:`@QueryAction`
:::


### 编程式

在有的场景需要编程方式获取当前用户权限信息,例如:

```java
@GetMapping("/user/detaul/current")
public Mono<UserDetail> getCurrentUserDetail(){
    return Authentication
            .currentReactive()
            .switchIfEmpty(Mono.error(UnAuthorizedException::new))//如果没有用户信息则抛出异常
            .map(autz->service.getUserDetail(autz.getUser().getId()))
}
```

### 权限维度

hsweb抽象来一个权限维度的概念,例如 `用户`,`角色`,`公司`是维度类型,那么`具体`的一个`用户`,`角色`或者`人员`就是维度信息.
在`Authentication`中,没有角色这些固有概念,只有维度,维度是可拓展的.不同的系统,可能维度不同,但是权限控制使用同一套API.

权限控制:

```java
    //注解式,推荐使用注解继承方式来自定义注解.
    @RequiresRoles("admin")//继承自: @Dimension(type="role",id="admin")
    public Mono<UserDetail> getData(String id){
       //...
    }

    //编程式
    //用户是否在id为admin中role维度中.相当于判断用户是否是admin角色.
    autz.hasDimension(DefaultDimensionType.role,"admin").
```

### 自定义维度

实现接口:`DimensionProvider`,然后注入到`spring`即可.例如:

```java
@Component
public class CompanyDimensionProvider implements DimensionProvider {

    @Autowired
    private CompanyService service;

    @Override
    public Flux<DimensionType> getAllType() {
        //建议使用枚举来实现DimensionType
        return Flux.just(CustomDimensionType.company);
    }

    @Override
    public Flux<Dimension> getDimensionByUserId(String userId) {
        //根据userId获取维度
        return service
                .findByUserId(userId)
                .map(CompanyEntity::toDimension);
    }

    @Override
    public Mono<? extends Dimension> getDimensionById(DimensionType type, String id) {
        //根据维度id获取维度
        return service.findById(id)
                      .map(CompanyEntity::toDimension);
    }

    @Override
    public Flux<String> getUserIdByDimensionId(String dimensionId) {
        //根据维度ID获取所有用户ID
        return service.findUserBind(dimensionId)
                .map(CompanyUserBindEntity::getUserId);
    }
}
```

