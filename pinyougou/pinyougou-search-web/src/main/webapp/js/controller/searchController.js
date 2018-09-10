app.controller("searchController", function ($scope, searchService) {

    //定义查询对象
    $scope.searchMap = {"keywords":"", "category":"","brand":"", "spec":{}};

    $scope.search = function () {
        searchService.search($scope.searchMap).success(function (response) {
            $scope.resultMap = response;
        });

    };

    //添加过滤查询条件
    $scope.addSearchItem = function (key, value) {
        if("category" == key || key == "brand"){
            $scope.searchMap[key] = value;
        } else {
            //规格
            $scope.searchMap.spec[key] = value;
        }

        $scope.search();
    };

    //移除过滤条件
    $scope.removeSearchItem = function (key) {
        if("category" == key || key == "brand"){
            $scope.searchMap[key] = "";
        } else {
            //规格
            delete $scope.searchMap.spec[key];
        }

        $scope.search();
    };
});