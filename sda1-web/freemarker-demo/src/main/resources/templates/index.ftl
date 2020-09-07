<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>

你好,${name}
<!-- 以下是三种在freemarker中显示时间的方式 -->
${data?date}
${data?time}
${data?datetime}

<!-- 以下是list -->
<#list personList as item>
    <p>${item}</p>
</#list>

<!-- 以下是判断 -->
<#if (condition1 > 30)>
    <p> 条件1--if </p>

    <#elseif (condition1 > 40)>
        <p> 条件2--elseif </p>

    <#else >
        <p> else </p>

</#if>

<!-- 空值的判断 -->
${msg!""}
${msg!"msg没有定义，这个是默认值"}
</body>
</html>