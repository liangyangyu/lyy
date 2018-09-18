app.controller("orderInfoController", function ($scope, cartService, addressService) {

    $scope.getUsername = function () {
        cartService.getUsername().success(function (response) {
            $scope.username = response.username;
        });
    };

    //加载地址列表
    $scope.findAddressList = function () {
        addressService.findAddressList().success(function (response) {

            $scope.addressList = response;

            for (var i = 0; i < response.length; i++) {
                var address = response[i];
                if(address.isDefault=="1"){
                    $scope.address = address;
                    break;
                }
            }

        });

    };

    //选择地址
    $scope.selectAddress = function (address) {
        $scope.address = address;
    };

    //判断当前地址是否是选择了的那个地址
    $scope.isSelectedAddress = function (address) {
        if($scope.address == address){
            return true;
        }

        return false;
    };

    //默认支付方式；微信支付
    $scope.order = {"paymentType":"1"};

    //选择支付方式
    $scope.selectPaymentType = function (type) {
        $scope.order.paymentType = type;
    };

});