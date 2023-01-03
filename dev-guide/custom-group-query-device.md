# 自定义模块如何分组查询产品设备信息

## 应用场景

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
    JetLinks平台未提供分组查询，用户可以使用stream流的方式实现业务数据分组
</div>

## 指导介绍

  <p>1. <a href="/dev-guide/custom-group-query-device.html#自定义实体类主要字段说明">自定义实体类主要字段说明</a></p>
  <p>2. <a href="/dev-guide/custom-group-query-device.html#自定义模块service说明">自定义模块service说明</a></p>
  <p>3. <a href="/dev-guide/custom-group-query-device.html#自定义模块controller说明及方法使用">自定义模块controller说明及方法使用</a></p>




### 自定义实体类主要字段说明

`org.example.mydemo.entity.CustomProductEntity`

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
CustomProductEntity类中包含的字段有：productId(产品ID)、productName(产品名称)、deviceType(产品类型)、devices(泛型为CustomProductEntity的list集合)
</div>

`org.example.mydemo.entity.CustomDevcieEntity`

<div class='explanation primary'>
  <p class='explanation-title-warp'>
    <span class='iconfont icon-bangzhu explanation-icon'></span>
    <span class='explanation-title font-weight'>说明</span>
  </p>
CustomDevcieEntity类中包含的字段有：deviceId(设备ID)、deviceName(设备名称)、productId(产品ID)、productName(产品名称)
</div>




## 自定义模块service说明

`org.example.mydemo.service.CustomProductService`

```java
@Service
public class CustomProductService extends GenericReactiveCrudService<CustomProductEntity,String> {
}
```

`org.example.mydemo.service.CustomDeviceService`

```java
@Service
public class CustomDeviceService extends GenericReactiveCrudService<CustomDevcieEntity,String> {
}
```

## 自定义模块controller说明及方法使用

`org.example.mydemo.web.CustomProDeviceController`

```java
@Getter
@RestController
@AllArgsConstructor
@Authorize(ignore = true)
@RequestMapping("/pro/device")
public class CustomProDeviceController implements ReactiveServiceCrudController {
    private static List<CustomDevcieEntity> list = new ArrayList<>();

    //引入产品service
    private CustomProductService customProductService;
    //引入设备service
    private CustomDeviceService customDeviceService;
    /**
     * 查询产品设备信息，并按照产品类型分组
     *
     * @return
     */
    @GetMapping("/get")
      /**
     * 查询产品设备信息，并按照产品类型分组返回
     *
     * @return
     */
    @GetMapping("/get")
    public Flux<CustomProductEntity> getDeviceGroupByPro() {
        return customProductService
            .createQuery()
            .fetch()
            .collectList()
            .flatMapMany(productList -> {
                List<String> list = productList.stream()
                                               .map(CustomProductEntity::getProductId)
                                               .collect(Collectors.toList());
                return customDeviceService
                    .createQuery()
                    .in("product_id", list)
                    .fetch()
                    .collect(Collectors.toList())
                    .map(deviceList -> deviceList.stream()
                                                     .collect(Collectors.groupingBy(CustomDevcieEntity::getProductId)))
                    .flatMapMany(map-> {
                        productList.forEach(product->product.setDevices(map.get(product.getProductId())));
                        return Flux.fromIterable(productList);
                    });

            });
    }
    @Override
    public ReactiveCrudService<CustomDevcieEntity, String> getService() {
        return customDeviceService;
    }
}
```

