<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Freemarker示例</title>
</head>
<body>
<#--这是注释-->
${name}---${message}

<br>
<hr>
<br>
assign设置变量<br>
<#assign str="itcast"/>
${str}<br>

<#assign linkInfo={"mobile":"13400000000","address":"吉山村"}>
mobile = ${linkInfo.mobile}；address = ${linkInfo.address}<br>

<br>
<hr>
<br>
include包含测试：<br>
<#include "header.ftl">


<br>
<hr>
<br>
if条件控制语句：<br>
<#assign bool=true>

<#if bool>
    bool的值为true
<#else>
    bool的值为false
</#if>

<br>
<hr>
<br>
遍历控制语句：<br>

<#list goodsList as goods>
    ${goods_index} --- ${goods.name} --- ${goods.price}<br>
</#list>
<hr>
总共${goodsList?size}条记录。

<br>
<hr>
<br>
eval转换为json对象：<br>
<#assign str2='{"id":123,"name":"itcast"}'>

<#assign jsonObj=str2?eval>
${jsonObj.name}
<br>
<hr>
<br>
日期格式化：<br>
今天日期：${today?date}<br>
今天时间：${today?time}<br>
今天日期时间：${today?datetime}<br>
格式化日期时间：${today?string("yyyy年MM月dd日 HH:mm:ss")}<br>
<br>
<hr>
<br>
数值显示：<br>
${number}---直接字符串化显示数值：${number?c}

<br>
<hr>
<br>
空值处理：<br>
如果是空值的话可以在变量之后添加!表示什么都不显示：${emp!}；如果为空值的时候要显示则可以在!之后添加要显示的内容，如：${emp!"emp的值为空。"}

<br>
???string 前面两个??表示变量是否存在，如果存在则返回true，否则返回false；后面一个?表示函数的调用。<br>

<#assign abc=false>

${abc???string}
<br>

<#if cba??>
    cba存在
<#else>
    cba不存在
</#if>



<br>
<br>
<br>
</body>
</html>