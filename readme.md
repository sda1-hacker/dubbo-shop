api  -- 服务层接口

basic -- 公共部分，例如entity，mapper等

service -- 服务层具体实现

web -- 控制器



product-controller 和 product-service 使用了rabbitmq将添加product改成了异步的方式。