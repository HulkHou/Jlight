productApp.service('productService', ['$http', '$q', 'baseService',
    function($http, $q, baseService) {
        return {
            addProduct: function(product) {
                var url = _ctx + '/product/add';
                return baseService.post(url,product);
            },
            deleteProduct: function(json) {
                var url = _ctx + '/product/delete';
                return baseService.post(url,json);
            },
            detail: function(ProductId) {
                var url = _ctx + '/product/detail';
                return baseService.post(url,ProductId);
            },
            editProduct: function(product) {
                var url = _ctx + '/product/edit';
                return baseService.post(url,product);
            }
        }
    }
]);
