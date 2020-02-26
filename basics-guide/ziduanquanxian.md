### 字段权限赋权
**操作步骤**
1. 在系统左侧导航栏，单击系统设置>权
限管理。  
![选择权限导航菜单](images/system/permission-choose.png)  
2. 在权限管理页，选择需要进行字段权限控制的权限，本教程以设备型号为例。点击`编辑`按钮进入编辑页。  
![进入权限编辑页](images/system/permission-update.png)  
 i. 在`支持的数据权限控制方式`下拉列表处，选择`字段权限`；  
 ![选择字段权限](images/system/column-setting.png)   
 ii. 在i的基础上`操作配置`中`支持数据权限`下拉列表将会出现`字段权限`的选项，选择即可；  
 ![选择字段权限](images/system/column-choose.png)  
 iii. 在`操作配置`中，对应`标识`选择了`字段权限`将在对应操作时进行字段权限控制。  
 如在`标识`query行的`支持数据权限`，选择了字段权限，则在型号管理功能界面查询时将进行字段权限控制。  
 iv. 在该页底部点击`保存`按钮即可保存配置。  
3. 在系统左侧导航栏，单击系统设置>用户管理。  
![进入用户管理](images/system/choose-permission.png)  
4. 用户管理页面选择需要分配权限的用户，点击`用户赋权`按钮进入用户赋权页面。  
![进入用户赋权](images/system/into-permission-setting.png)  
  
5. 进入用户赋权页面，定位到设备型号，该行将多出一个`字段权限`按钮，点击进入字段权限控制配置  
![进入字段权限编辑](images/system/into-field-access.png)  
  
6. 在字段权限编辑页面将展示出权限编辑中`数据视图`中的所有字段，并且在每个字段后列出权限编辑中选择了字段权限控制的操作，以供用户选择。  
  
![字段权限编辑](images/system/update-field-access.png)  
i. 在该页面中，勾选字段列后的操作选项，选中表示该字段允许进行此操作。  
例如：`名称` 勾选了`保存`和`删除`，则在设备型号管理中查询时不会返回`名称`的数据。  
![选择字段](images/system/choose-field.png)  
ii. 字段权限配置完成后点击需`保存`按钮完成编辑。  
![保存字段权限配置](images/system/save-field-access.png)  
7. 点击赋权页下方`保存`按钮完成用户赋权操作。  
![用户赋权保存](images/system/permission-setting-save.png)  
字段赋权演示  
![用户字段赋权演示](images/system/field-access-play.gif) 
