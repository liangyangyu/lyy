app.controller("payController", function ($scope, $location, cartService, payService) {

    $scope.getUsername = function () {
        cartService.getUsername().success(function (response) {
            $scope.username = response.username;
        });
    };

    //生成二维码
    $scope.createNative = function () {

        //接收地址栏中的支付日志id
        $scope.outTradeNo = $location.search()["outTradeNo"];

        payService.createNative($scope.outTradeNo).success(function (response) {

            if("SUCCESS"==response.result_code){
                //显示本次支付总金额
                $scope.money = (response.totalFee/100).toFixed(2);

                //如果下单成功则根据返回的支付二维码地址生成二维码
                var qr = new QRious({
                    element:document.getElementById("qrious"),
                    level:"Q",
                    size:250,
                    value:response.code_url
                });


            } else {
                alert("生成二维码失败");
            }

        });

    };
});