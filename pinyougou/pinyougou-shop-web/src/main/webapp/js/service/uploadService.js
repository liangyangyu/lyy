app.service("uploadService",function ($http) {

    this.uploadFile = function () {
        //创建表单数据；只能用于html5
        var formData = new FormData();
        //表单数据中添加file表单项；名称为file，值为在页面中选择的那个文件
        formData.append("file", file.files[0]);
        return $http({
            url:"../upload.do",
            method:"post",
            data:formData,
            headers:{"Content-Type": undefined},
            transformRequest: angular.identity
        });
    };
});