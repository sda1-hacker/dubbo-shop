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
<input type="file" name="img" id="file_upload" multiple="multiple"/>

对应的JS：
// uplodify事件处理，上传图片到后台
            $("#file_upload").uploadifive({
                'uploadScript': 'test/uplodify',      // 提交的地址
                'fileObjName': 'img',   // file输入框的name值,  如果不传递这个值则后台接受的文件为空， 后台通过 @RequestParam("img") MultipartFile file来获取这个文件

	// 上传完毕后的回调函数
	            'onUploadComplete': function (file, data) {   // file 代表上传的文件，    data表示后台返回的json数据  
	                alert('the file: ' + file.name + ',   the data: ' + data)
	            }
	        })

后台代码：
@RequestMapping("uplodify")
    public String uplodify(@RequestParam("img") MultipartFile file){
        System.out.println(file);   // 这样就拿到了图片
        return null;
    }

