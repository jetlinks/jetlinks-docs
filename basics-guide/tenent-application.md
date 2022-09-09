<a name="E4wza"></a>
## 平台租户管理说明
<a name="PUdd0"></a>
## 场景一
<a name="UDVl4"></a>
### 场景说明

账号性质说明：<br />当前系统有账号A/B/C，<br />A是普通用户账号，未绑定至任何租户下及机构下。<br />B为创建租户时生成的账号，默认为租户管理员。<br />C为租户绑定的普通成员。

<br />账号赋权说明：<br />A账号赋权为用户管理赋权，无其它任何权限，<br />B账号为租户赋权，无其它任何权限<br />C账号为租户赋权，无其它任何权限
<a name="OzeA8"></a>
### 测试结果

<a name="uomtk"></a>
#### A用户展示界面【平台用户】

展示用户赋权内勾选的权限的界面。

<a name="L93j5"></a>
#### B用户展示界面【租户管理员】
<br />系统管理：用户管理、机构管理、租户管理<br />设备管理：产品、产品分类、设备、分组、网关、地理位置、固件升级、设备告警<br />通知管理：通知配置、通知模板<br />规则引擎：规则实例、数据转发<br />可视化：(前端只展示了入口，但是界面无任何内容)<br />视频网关：基本配置、视频设备、分屏展示、国标级联
<a name="LV1ia"></a>
#### <br />C用户展示界面【租户普通成员】
<br />系统设置：用户管理、机构管理<br />设备管理：产品、产品分类、设备、分组、网关、地理位置、固件升级、设备告警<br />通知管理：通知配置、通知模板<br />视频网关：基本配置、视频设备、分屏展示、国标级联

<a name="hv7VU"></a>
#### 租户权限隔离及权限说明

1. 请勿将超级管理员（admin）账号添加到租户中
1. 租户内绑定的成员在**查看租户**的**权限管理**勾选【**用户管理**】权限，则当前租户的所有成员都可以进行用户管理，若想对部分租户成员赋予用户管理的权限，则可以在用户管理中单独对该账号赋权。
1. 租户管理员可以在首页分析内看见自己以及租户下的其他普通租户成员的统计数据，普通成员仅能看见自己的资产数据。
1.  租户内分配资产默认是给所有租户成员分配，可以在资产分配列表内选择绑定租户成员，则仅对该租户成员分配资产。
1. 租户成员解绑，则平台自动会解绑该账号下绑定的一切资产（产品、设备）。
1. 租户主账号被解绑则该账号在用户管理内会被禁用。
1. 对租户成员直接分配设备但未分配产品，则平台⾃动将该设备所属产品绑定⾄租户成员下。对租户成员进行资产解绑时，解绑设备不会解绑对应绑定的产品，解绑产品会默认把当前产品下的所有设备进行同步解绑
1. 在**查看租户**的**权限管理**勾选【**可视化**】权限，界面只展示了入口，二级菜单均做了入口控制屏蔽。
1. 在**查看租户**的**权限管理**勾选【**规则引擎**】权限，则租户管理员可见规则引擎入口并且可以编辑修改，租户普通成员由前端控制屏蔽了入口。
1. 视频网关现目前没有做租户权限数据隔离，不建议开放视频网关给租户。
1. 设备管理地理位置没有做租户权限数据隔离，前端也没有控制屏蔽入口，若在**查看租户**的**权限管理**勾选【**地理位置**】权限，则租户登录可以查看到平台其他租户下的设备数据。
1.  租户成员默认订阅平台全部的设备数据而不是租户成员被分配的资产设备数据。如只订阅该租户成员分配的资产数据，则需要在配置文件中设置参数rule.engine.task-executor.reactor-ql.enable-tenant=true 
1. 开启rule.engine.task-executor.reactor-ql.enable-tenant=true后，使用mqtt订阅设备消息也只能订阅到当前租户绑定的资产设备消息，未绑定的资产上报的数据及平台下发的报文等设备消息无法订阅。
1. 开启allow-all-assets参数并设置device、product资产类型后，该租户下的成员在使用api方式查询设备实例时能够访问到租户成员下全部绑定的资产设备信息。
1. 租户无法看到设备接入菜单，因前端进行入口控制屏蔽。
1. 通知配置中无法对**网络组件**配置（因为租户无法操作设备接入菜单，并且前端进行了屏蔽）。
1. 租户添加第三方账号后，通过API调用进行数据查询，数据会按照租户逻辑进行隔离。
1. 机构可以进行分配资产，但是登录租户成员账号查看，发现没有分配的资产数据，即该场景使用在机构下分配资产并不会以机构做数据隔离。
1. 在**查看租户**的**权限管理**取消勾选【**通知模板**】权限，对租户成员【任意成员，普通或管理员均可】所在的机构赋权【通知模板】权限，则被分配了权限的账号能看见通知模板菜单，未被分配权限则无法看见。
1. 机构下绑定租户展示内容

![image.png](https://cdn.nlark.com/yuque/0/2022/png/21732731/1649831783909-5fc3e2cb-7dd2-4d2c-9ac0-a86549f6ef2a.png#clientId=ue713923d-569b-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=1764&id=udf8e079e&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1764&originWidth=2454&originalType=binary&ratio=1&rotation=0&showTitle=false&size=108916&status=done&style=none&taskId=u79b80a06-494e-47de-995e-6b1ed7d9216&title=&width=2454)<br />![image.png](https://cdn.nlark.com/yuque/0/2022/png/21732731/1649831934527-3c304d21-64e0-4c94-99aa-40be90cfcc5c.png#clientId=ue713923d-569b-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=896&id=uad5fa341&margin=%5Bobject%20Object%5D&name=image.png&originHeight=896&originWidth=1661&originalType=binary&ratio=1&rotation=0&showTitle=false&size=40634&status=done&style=none&taskId=u46cb9afe-093e-45fa-aa30-cf4b18e91e7&title=&width=1661)<br />![image.png](https://cdn.nlark.com/yuque/0/2022/png/21732731/1649831978189-92f48f92-8312-4794-a19f-8a6480035558.png#clientId=ue713923d-569b-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=1204&id=u7f2b9b36&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1204&originWidth=2416&originalType=binary&ratio=1&rotation=0&showTitle=false&size=66584&status=done&style=none&taskId=udcd9d760-0975-4d90-8519-76124b183a7&title=&width=2416)
<a name="kVbv3"></a>
## 场景二
<a name="XsJuN"></a>
### 场景说明

账号性质说明：<br />当前系统有账号A/B/C<br />A为平台超级管理员账号。<br />B为创建租户时生成的账号，默认为租户管理员。<br />C为租户绑定的普通成员。


<br />账号赋权说明：<br />B账号为租户赋权，无其它任何权限<br />C账号为租户赋权，无其它任何权限

<a name="IUimJ"></a>
### 测试结果

<a name="n9x6a"></a>
#### B用户展示界面【租户管理员】
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21732731/1649835690357-7c130ea6-e8ad-4f9e-a741-9146a4a495aa.png#clientId=ue713923d-569b-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=670&id=u93e7a055&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1340&originWidth=1914&originalType=binary&ratio=1&rotation=0&showTitle=false&size=86784&status=done&style=shadow&taskId=u9d52ce7e-4dab-4cd3-aa77-0656f24601a&title=&width=957)
<a name="jFWTG"></a>
#### C用户展示界面【租户普通成员】
![image.png](https://cdn.nlark.com/yuque/0/2022/png/21732731/1649835546581-bcbbe241-632d-4e13-838b-45a52aa506a8.png#clientId=ue713923d-569b-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=481&id=u73c7b3e9&margin=%5Bobject%20Object%5D&name=image.png&originHeight=962&originWidth=1922&originalType=binary&ratio=1&rotation=0&showTitle=false&size=68400&status=done&style=shadow&taskId=u62d926d9-387d-463b-8f46-e7ae25fc94d&title=&width=961)
<a name="BAKRP"></a>
#### 租户权限隔离及权限说明

1. 当用户没有和机构关联时,用户只能管理自己创建和分配给自己的数据。包括超级管理员也只能看见自己创建的数据，租户账号同理。
1. 将B用户绑定至**testcq**机构下,将C用户绑定至广州智慧城市下，两个用户机构树展示一致

![image.png](https://cdn.nlark.com/yuque/0/2022/png/21732731/1649844722419-5bfbc84e-c7f5-4eb9-a729-01a9eb5939cf.png#clientId=ue713923d-569b-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=366&id=u9efbd2cf&margin=%5Bobject%20Object%5D&name=image.png&originHeight=732&originWidth=1662&originalType=binary&ratio=1&rotation=0&showTitle=false&size=34047&status=done&style=shadow&taskId=uffa4c676-5185-4337-be8e-8ac0164a55d&title=&width=831)<br />![image.png](https://cdn.nlark.com/yuque/0/2022/png/21732731/1649844750365-09c85e50-4fa1-43bb-bf5a-14108bc79d12.png#clientId=ue713923d-569b-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=386&id=u8fe74c87&margin=%5Bobject%20Object%5D&name=image.png&originHeight=771&originWidth=1663&originalType=binary&ratio=1&rotation=0&showTitle=false&size=35150&status=done&style=shadow&taskId=u6c12f735-396a-4c9a-a764-18315b83a7b&title=&width=831.5)

3. 使用B账号创建1个产品，并使用A账号将B账户绑在广州智慧城市机构下，并给机构分配了A账号创建的产品，刷新B账号，如下图，可以看到该租户账号可以管理两个产品。一个产品是用户创建的，另一个产品是分配在机构下的。

![image.png](https://cdn.nlark.com/yuque/0/2022/png/21732731/1649845936048-51469c3d-2f31-41ee-99ab-419af97dcbb0.png#clientId=ue713923d-569b-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=444&id=u1f7ebc06&margin=%5Bobject%20Object%5D&name=image.png&originHeight=888&originWidth=1934&originalType=binary&ratio=1&rotation=0&showTitle=false&size=56357&status=done&style=shadow&taskId=u73bb4ddc-fbd6-4f00-8788-4a4555d925c&title=&width=967)

4. C账号不创建产品，使用A账号将C账号绑定至香港机构下，并使用A账号给C所在机构分配一个产品的资产。可以看到只有一个产品，该产品是分配给机构的。

![image.png](https://cdn.nlark.com/yuque/0/2022/png/21732731/1649846957626-333f84ef-31b8-4597-8676-819cf88d5277.png#clientId=ue713923d-569b-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=370&id=u237190fb&margin=%5Bobject%20Object%5D&name=image.png&originHeight=740&originWidth=1480&originalType=binary&ratio=1&rotation=0&showTitle=false&size=50114&status=done&style=shadow&taskId=u52b8465b-9bf6-4785-a19c-09c23c67e6e&title=&width=740)

5. 仅分配设备给机构，则平台会自动将产品也分配给该架构下的用户。
5. 将机构下的产品资产解绑，则绑定给机构的产品下的设备也会被解绑。
5. 将租户管理员B绑定在香港机构下，将租户成员绑定在九龙区机构下，对这两个机构各分配一个产品和设备，则管理员B和普调成员C仅能看见自己创建的和分配给当前机构的资产，不存在上级机构可见下级机构的数据。
5. ![image.png](https://cdn.nlark.com/yuque/0/2022/png/21732731/1649847533888-77350763-9649-40b4-8367-c6a48abf8930.png#clientId=ue713923d-569b-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=861&id=uf1604b10&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1722&originWidth=2477&originalType=binary&ratio=1&rotation=0&showTitle=false&size=155588&status=done&style=shadow&taskId=ud4ee57b9-5008-433a-9263-15d9f57fce6&title=&width=1238.5)
5. 使用模拟器模拟两个租户创建的设备对平台发送报文，未设置rule.engine.task-executor.reactor-ql.enable-tenant=true 的情况下，租户管理员可以在规则引擎内订阅所有用户上报的数据
5. 设置rule.engine.task-executor.reactor-ql.enable-tenant=true 的情况下，租户仅能订阅自己创建的以及分配给租户所在机构下的设备。
5. 新建一个api机构，对该机构绑定api用户，并分配两个产品资产和一个设备资产，使用OpenAPI生成token调用在线接口，查看设备列表只有被分配的设备数据。

![image.png](https://cdn.nlark.com/yuque/0/2022/png/21732731/1649903392173-c0afab3c-32b3-49db-864f-09a76b2084af.png#clientId=u5f51f782-73e5-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=880&id=u2f6d97a2&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1760&originWidth=2490&originalType=binary&ratio=1&rotation=0&showTitle=false&size=121489&status=done&style=shadow&taskId=u966a67b5-94cc-4e1c-bbe9-e76476b830d&title=&width=1245)<br />![image.png](https://cdn.nlark.com/yuque/0/2022/png/21732731/1649903417454-789f4000-f995-4e77-9d6b-53d1d753fda6.png#clientId=u5f51f782-73e5-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=1532&id=u2aaf4ada&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1532&originWidth=2481&originalType=binary&ratio=1&rotation=0&showTitle=false&size=104081&status=done&style=shadow&taskId=u713567e9-37c0-44ff-b743-fc14406e469&title=&width=2481)<br />![image.png](https://cdn.nlark.com/yuque/0/2022/png/21732731/1649903427079-9afe3022-71ea-4590-947f-9792a24e45b2.png#clientId=u5f51f782-73e5-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=1450&id=ubd568e77&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1450&originWidth=2486&originalType=binary&ratio=1&rotation=0&showTitle=false&size=118536&status=done&style=shadow&taskId=u49847af8-c661-417f-b6fd-79348fba702&title=&width=2486)<br />![image.png](https://cdn.nlark.com/yuque/0/2022/png/21732731/1649903473774-232538fc-cf55-443a-97c1-770a032ea432.png#clientId=u5f51f782-73e5-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=1681&id=ua4c67c2d&margin=%5Bobject%20Object%5D&name=image.png&originHeight=1681&originWidth=2359&originalType=binary&ratio=1&rotation=0&showTitle=false&size=253664&status=done&style=shadow&taskId=u70d2638e-0ffa-4fe3-8c0b-b9564afe563&title=&width=2359)

12. 其余视频网关、可视化及设备接入部分是不开放给租户的，同场景一情形一致。
