productCateApp.service('productCateService', ['$http', '$q', 'baseService',
        function($http, $q, baseService) {
            return {
            	addProductCate: function(productCate) {
                    var url = _ctx + '/productCate/add';
                    return baseService.post(url,productCate);
                },
                deleteProductCate: function(json) {
                    var url = _ctx + '/productCate/delete';
                    return baseService.post(url,json);
                },
                detail: function(CateId) {
                    var url = _ctx + '/productCate/detail';
                    return baseService.post(url,CateId);
                },
                editProductCate: function(productCate) {
                    var url = _ctx + '/productCate/edit';
                    return baseService.post(url,productCate);
                }
            }
        }
]);
