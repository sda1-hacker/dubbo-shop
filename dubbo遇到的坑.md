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



