7.19号


provider【也就是service的实现】的启动类要加上@MapperScan("mapper.class所在包") 


provider【也就是service的实现】要在application.yml中配置数据源

consumer【也就是控制器】也要在application.yml中配置数据源

【只要是需要启动的服务，都需要配置数据源。在mapper中不需要配置对应的数据源。】

在application.yml中数据源配置如下，这里使用的mariadb


spring:

  datasource:
    url: jdbc:mysql://192.168.206.130:3306/shop?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
    username: root
    password: 123456




服务进行了分割，数据在网络上传输，因此需要对entity实现序列化，VO也需要进行序列化





项目结构说明，以及依赖（使用mabatis plus，自动生成代码）：
	
		basic -- 模块公用的东西
		entity -- 导入lombok，mybatis相关依赖
		mapper -- 导入entity，springboot，mybatis，mybatis plus，mysql相关依赖
	
	api  -- service的接口
		导入entity，mybatis plus相关依赖
	
	service -- service接口的实现
		导入entity，springboot，dubbo注解，mapper，service接口相关依赖
	
	web -- controller
		导入entity，service接口，dubbo注解，springboot相关依赖


7.20号
dubbo中将provider和consumer拆分成不同的项目，他们之间通信通过玩过，因此需要进行序列化
mybatis-plus 官方已经明确有说wrapper不支持RPC通信，所以会报错。
		

关于分页：
mybatis + pagehelper

依赖如下：
<!--        分页工具，为了复用，所以放在这里-->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper</artifactId>
            <version>5.1.10</version>
        </dependency>

<!--        pagehelper 的 springboot支持-->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper-spring-boot-autoconfigure</artifactId>
            <version>1.2.13</version>
        </dependency>

thymeleaf模板引擎踩坑 -- 需要在th:href="|xxxx|"  加这个竖线
<a th:href="|goods/page/${item}/${pageInfo.pageSize}|" th:text="${item}"></a>
多个字符串，需要用 |   | 包括起来



7.21号
使用mybatis plus 在设置了主键（id）自增之后，调用insert方法将数据添加到数据库中之后，会将entity的id自动补上【 主键回填 -- 百度mybatis plus主键回填 】，调用entity.getID()就可以获得





7.23号
uplodify实现异步上传文件  --- 单独拆分成一个服务
下载地址：  https://github.com/RonnieSan/uploadify.git
需要：jquery.uploadifive.js	uploadifive.css	uploadifive-cancel.png

页面标签：

```html
<input type="file" name="img" id="file_upload" multiple="multiple"/>    文件选择框
<img src="#" id="img_show" style="width: 100px;height: 100px;display: none"> 异步上传显示
<input type="hidden" name="goodsBase.images" id="images">  图片的url放在这里，方便存储到mysql
```

对应的JS：

```javascript
$("#file_upload").uploadifive({    
    'uploadScript': 'file/uplodify',      // 提交的地址    
    'fileObjName': 'img',   // file输入框的name值,  如果不传递这个值则后台接受的文件为空， 后台通过 @RequestParam("img") MultipartFile file来获取这个文件    
    'onUploadComplete': function (file, data) {  // 图片上传完毕后的回调函数        
        // file 代表上传的文件，    data表示返回的json数据  --        
        // alert('the file: ' + file.name + ',   the data: ' + data)   
        $("#img_show").attr("src", data).show();      // 上传之后直接在页面显示图片  
        $("#images").val(data);    	// 将图片的url放在 input中，便于传递到后台
    }
})
```



后台代码：

```java
@RequestMapping("uplodify")
public String uplodify(@RequestParam("img") MultipartFile file) throws Exception{   		String file_name = file.getOriginalFilename();	// 图片名
	String extName = file_name.substring(file_name.lastIndexOf(".") + 1);  // 后缀名
   	InputStream inputStream = file.getInputStream();    
    StorePath storePath = fastFileStorageClient.uploadFile(inputStream, file.getSize(), extName, null);    
                                                                                 			String full_path = storePath.getFullPath();    // 完整路径
                                                                                 
	String img_url = new StringBuilder(base_url).append(full_path).toString(); //和服务器地址拼接
                                                                                 			System.out.println(img_url);    
                                                                                 			return img_url;
                                                                                
}
```



关于fast DFS需要解决的问题：

1、垃圾文件的处理

2、安全问题的考虑   --  不能让任何人都将文件存储到服务器上。

3、盗链图片--  水印【加大盗取成本】





富文本框：

wangEditor



使用富文本框实现文件批量上传：

```html
<div id="editor">

</div>

<input type="hidden" name="goodsInfo" id="goods_info" ></input>

```



```js
// 富文本编辑器
            let E = window.wangEditor;
            let editor = new E("#editor");

            // 自定义菜单栏配置
            editor.customConfig.menus = [
                'head',  // 标题
                'bold',  // 粗体
                'fontSize',  // 字号
                'fontName',  // 字体
                'justify',  // 对齐方式
                'image',  // 插入图片
                'undo',  // 撤销
                'redo'  // 重复
            ]

            // 配置文件服务器端地址   -- 支持一次选中多张图片
			// 返回数据的格式必须为：{"errno": 0, data:[] }   errno=0表示成功
            editor.customConfig.uploadImgServer = "file/wangEditor_uplodify"
            // 指定上传图片时的name  -->   <input type="file" name="img">
            editor.customConfig.uploadFileName = "img"

            editor.create();

```





获取富文本框中的内容， 然后存储到数据库中：

```js
// 获取富文本框中内容
var editor_content = editor.txt.html();  // 所有内容
var editor_content = editor.txt.text();  // 只获取文本内容
```





7.26号：

商品分类：   id，pid，name			pid=0的为第一级分类

方法一：定义Node类

class Node {

​	id；

​	name；

​	List<Node>

}



方法二：编写递归的sql



方法三：直接获取分类的列表，在前端进行区分		 通常采用这种，减少递归，从而减轻数据库的压力

​		 商品分类：   id，pid，name 

​			向页面返回所有分类，list

​		前台页面这样判断

​		th:each="type_list : ${list}"    //一级分类

​			th:each="son_type_list : ${list}"  th:if="${son_type_list.pid == type_list.id}"	// 二级分类的pid等于一级分类的id

​				th:each="son_son_type_list : ${list}"  th:if="${son_son_type_list.pid == son_type_list.id}"// 三级分类的pid等于二级分类的id





在做开发的时候优先减轻数据库的压力。 







7.27号：

swagger2    构建RESTful API 



关于跨域问题：使用jsonp去解决【json padding   -- json填充】，原理是使用js的 src去访问，这样不会出现跨域   

```html
# 在定义call函数，供下面使用
<script type="text/javascript">
    function: call(data){
    	console.log(data)
	}
</script>
```

   

```html
# 使用script标签的 src去向其他服务器发送一个请求，不会出现跨域的问题
# 向后台传递的参数是callback,   callback是一个函数 ，在上面已经定义过了
<script type="text/javascript" src="http://192.168.206.133:8080/getData?callback=call"></script>
```



```java
# 后台代码
    @GetMapping("list")
    public String list(String callback){

        List<TGoodsType> items = goodsTypeService.list();

		// call(data) 就是上面定义的函数
        return callback + "(" + items.toJson() + ")";
	}
```



ajax使用jsonp

```html
// 在ajax中使用的函数
<script type="text/javascript">
    function: call(data){
    	console.log(data)
	}
</script>
```



```javascript
$(function(){
	$.ajax({
        url: "http://192.168.206.133:8080/getData?callback=call",
        dataType: "jsonp" // 使用jsonp处理跨域
        jsonpCallback: "call" // jsonp使用上面定义的函数，函数名要一致
    })
})
```



```Java
// 后台代码
@GetMapping("list")
public String list(String callback){

    List<TGoodsType> items = goodsTypeService.list();

	// call(data) 就是上面定义的函数
    return callback + "(" + items.toJson() + ")";
}
```



索引失效：

1、select * from t_user where name like '%sd%';

这种会导致索引的失效，

2、select * from t_user where name like 'sd%';

这种不会导致索引失效，（有参照依据）



mysql 5.6之后提供了全文索引，保证了分词（华为手机-->华为、手机）和性能 （like 'sd%'）,使用了全文索引，压力仍然在sql服务器上。



采用成熟的搜索引擎技术来代替数据库【索引库】，solr（Apache）、es（elasticSearch-大数据分析）

应用程序 ----(搜索)---> 索引库【可以做集群，减轻压力】（solr、es）<----(数据同步)---数据库

适用于站内搜索







安装solr：

jdk 1.8、tomcat

wget http://archive.apache.org/dist/lucene/solr/4.10.4/solr-4.10.4.tgz

tar -zxvf solr-4.10.4.tgz 



solr-4.10.4/dist/  包含了一个连通tomcat和solrhome的可运行war包 

solr-core：独立提供搜索服务的单位，在一个solr-home中可以有多个solr-core

程序 ------->tomcat（solr.war）------->solr-home，数据库的数据要导入给某个具体的solr-core



/solr-4.10.4/example/solr/：是一个标准的solr-home，包含了一个solr-core（collection1）



cd /solr-4.10.4/example/

cp -r solr /usr/local/solr-home

cp /solr-4.10.4/dist/solr-4.10.4.war  /usr/local/tomcat/webapps/

启动tomcat

cd /usr/local/tomcat/webapps/

mv solr-4.10.4  solr



# 添加扩展日志

cd /solr/WEB-INF

mkdir classes     //存放配置文件的目录

cp /solr-4.10.4/example/lib/ext/*  /usr/local/tomcat/webapps/solr/WEB-INF/lib/

cp /solr-4.10.4/example/resources/log4j.properties  /usr/local/tomcat/webapps/solr/WEB-INF/classes/



# 加载solr-home

vim /usr/local/tomcat/webapps/solr/WEB-INF/web.xml

<env-entry>  41行

​	<env-entry-name>solr/home</env-entry-name> 

​	<env-entry-value>/usr/local/solr-home/</env-entry-value> 

​	<env-entry-type>java.lang.String</env-entry-type> 

</env-entry>



重启tomcat

访问： http://192.168.206.135:8080/solr/





![1595818690466](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1595818690466.png)

![1595818737227](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1595818737227.png)

这个分词器对中文不敏感。需要安装中文分词器（使用IKAnalyzer）[]

git clone https://gitee.com/wltea/IK-Analyzer-2012FF.git

cp /IK-Analyzer-2012FF/distIKAnalyzer2012FF_u1.jar /usr/local/tomcat/webapps/solr/WEB-INF/lib/

cp /IK-Analyzer-2012FF/dist/IKAnalyzer.cfg.xml stopword.dic /usr/local/tomcat/webapps/solr/WEB-INF/classes/				# xxx.dic表示词典，用户可以通过修改IKAnalyzer.cfg.xml配置自定义词典



# 在solr-core中使用中文分词器

vim /usr/local/solr-home/collection1/conf/schema.xml

在文件末尾添加

<fieldType name="text_ik" class="solr.TextField">	# name可以自定义，分词器名称
                <analyzer class="org.wltea.analyzer.lucene.IKAnalyzer"/>
</fieldType>

重启tomcat



![1595822011181](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\1595822011181.png)



将数据库上的数据同步到solr上：

需要再solr中定义字段，和mylsq进行映射，但是solr->mysql不一定要完全映射，这个是根据需求来分析的。solr只提供搜索需要返回的字段，没有必要完全返回。点击对应的信息才显示详情。



1、自定义域（solr-->mysql的映射）  

vim /usr/local/solr-home/collection1/conf/schema.xml   【添加到文件最后】

// name：自定义字段名，type：指定分词器，indexed：是否可以按照这个字段搜索，stored：存储，这一部分是搜索的时候返回的字段

<field name="goods_name" type="text_ik" indexed="true" stored="true" />

<field name="goods_price" type="int" indexed="true" stored="true" />

<field name="goods_sale_point" type="text_ik" indexed="true" stored="true" />

<field name="goods_img" type="string" indexed="false" stored="true" />



// 目标域		multiValue：符合字段，会按照目标域进行搜索。相当于（or）

<field name="goods_keywords" type="text_ik" indexed="true" stored="true" multiValued="true"/>



// 将自定义域放在目标域中

<copyField source="goods_name" dest="goods_keywords"/>

<copyField source="goods_sale_point" dest="goods_keywords"/>





# springboot整合solr

底层使用的是spring-data-solr





# 数据库的数据全量复制到索引库（solr）



从git或者svn上拉去的项目，文件不是module

参考： https://www.csdn.net/gather_2e/MtjaIgwsOTU4NjctYmxvZwO0O0OO0O0O.html



# solr原理：

​	正向索引：文档-->关键字	

​	反向索引：关键字-->文档列表

​		分词之后为每个词形成一个反向索引。



# 多个solr-core：

​	一个solr-core对应一个搜索服务

​	cd /usr/local/solr-home

​	cp -r collection1 	collection2

​	

​	修改solr-core名称					这个名称在java程序中需要使用到，

​	vim /usr/local/solr-home/collection2/core.properties

​	name 修改为 collection2

​	

​	修改配置文件  -- 自定义域

​	vim /usr/local/solr-home/collection2/conf/schema.xml 



​	删除data

​	

​	之后重启tomcat就可以了



​	添加时指定对应的collection：	（core.properties中定义的collection的名称）

```java
//        solrClient.add("collection2", document);  // 指定添加到collection2
//        solrClient.commit("collection2");     // 指定提交到collection2
```



​	查询时指定对应的collection：	（core.properties中定义的collection的名称）

```java
//        QueryResponse response1 = solrClient.query("collection2",condition);    // 从指定的collection中查询
```





# solr 集群架构（solr-cloud）

​	多台服务器提供一个搜索服务。

​	通过zookeeper管控solr集群，作为集群的入口，也可以对配置文件进行统一的管理。

​	访问结构：			client --->zookeeper（集群）--->solr集群



# 数据同步：增量复制

​	初始化的时候做全量复制

​	之后做增量复制。---》 在新增、修改、删除的时候同时也对solr中的数据进行同步。

​	新增时，直接将添加后的entity传递给solr的添加服务这样会出现id找不到的情况【即使mybatis plus实现了主键自动回填，id也无法直接传递过去，因为是跨jvm的操作】。

1、使用mybatis plus 主键会自动回填，将id传递给solr的添加服务，根据id查询对应的entity，然后进行添加。 2、或者是根据回填的id，重新查找对应entity然后传递给solr服务。





# 商品详情页方案：

​	方案1：动态页面的方式，根据查找的id不同，页面的内容也不同。	id -->mapper -->jsp -->java -->class -->tomcat-->展示商品信息。（与数据库的交互次数比较多）

​	方案2：静态页面的方式，提前生成一系列静态页面。浏览器-->nginx-->静态页面



​	落地方案：

​			1、生成静态页面（使用freemarker）

​			2、将静态页面部署到nginx上

​			3、后续维护，性能