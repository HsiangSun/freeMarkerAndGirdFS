### FreeMarker技术总结

#### 初始数据

```java
map.put("name","hsiang sun");
map.put("list",friendsList);
map.put("stumap",map2);
```

#### 空值处理方式

```html	
${(stuMap[stu1].name)!''} <!--如果为空就返回空子串-->
```

#### 插值表达式

```html	
Hello ${name}
```

#### List遍历

```html
<#list list as stu>
    <tr>
        <td>${stu.name}</td>
        <td>${stu.age}</td>
        <#-- <td>${stu.birthday}</td>-->
		<td>${stu.money}</td>
	</tr>
</#list>
```

#### Map遍历

```html	
<!--Method one-->
<p>The name of  Student 1:<span>${stumap['stu1'].name}</span></p>
<!--Method two-->
<p>The age of  Student 2:<span>${stumap.stu2.age}</span></p>
<!--Method three-->
 <#list  stumap?keys as object>
     ${stumap[object].name}
 </#list>
```

#### 逻辑运算

运算符|表达式
-----|----
\>|gt
\>= | gte
\< | lt
\<= | lte

#### 内建函数

+ 集合大小：${集合名?size}
+ 日期格式化：

含义 | 表达式
-----|-----
显示年月日 | ${today?date}
显示时分秒 | ${today?time}
显示日期+时间|${today?datetime}
自定义格式化时间|${today?string("yyyy年MM月")}

+ 数字转成字串：`${num?c}`  123,145,233 =>123145233
+ JSON转Object

```html
<#assign text="{'back':'ICBC','account':'121464657'}" />
<#assign data=text?eval />
${data.bank} ${data.account}
```

### GridFS

由MongeDB开发，将大于**256KB**的数据分段存入fs.chunks集合中。其本身索引保存在fs.files集合中。

#### 导入依赖

```xml
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-mongodb</artifactId>
    <version>2.0.11.RELEASE</version>
</dependency>
```

#### 存储数据(将文件以二进制形式存储)

```java
File file =new File("C:\\Users\\xy110\\Pictures\\girl00.jpg");
FileInputStream ins = null;
try {
    ins = new FileInputStream(file);
} catch (FileNotFoundException e) {
    e.printStackTrace();
}
ObjectId objectId = gridFsTemplate.store(ins, "girl");
```

#### 获取数据

+ 首先注入`GridFSBucket` 对象

  ```java	
  @Configuration
  public class MongodbConfig {
      private String db="xc_cms";//数据库名实际情况应该从application.yml中获取
      @Bean
      public GridFSBucket createGridFsBucket(MongoClient mongoClient){
          MongoDatabase database = mongoClient.getDatabase(db);
          GridFSBucket gridFSBucket = GridFSBuckets.create(database);
          return gridFSBucket;
      }
  }
  ```
  
+ 获取流并写入数据

  ```java
   //根据ObjectId查询
          GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("5d3acd3fec28a022bc747336")));
          //建立一个下载流
          GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
          //获取流
          GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
          InputStream inputStream = gridFsResource.getInputStream();
          File outFile = new File("C:\\Users\\xy110\\Desktop\\mogo.jpg");
          byte[] buff =new byte[1024];
          int len = 0;
          OutputStream ous = new FileOutputStream(outFile);
          while ( (len= inputStream.read(buff)) != -1 ){
              ous.write(buff,0,len);
          }
          ous.close();
          inputStream.close();
  ```

  
