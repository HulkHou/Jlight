var productApp = angular.module('productApp', ['base']);
productApp.controller('productCtrl', ['$rootScope', '$scope','productService',function ($rootScope,$scope,productService) {
	$('#multiselect').multiselect({});
	//搜索参数
	$scope.queryFilter = {};
	$scope.role = {	};
	$scope.isOnsaleMap = [{isOnsale:1, isOnsaleName: "在售"}, {isOnsale: 0, isOnsaleName: "停售"}];
	$scope.statusMap = [{status:0, statusName: "未删除"}, {status: -1, statusName: "已删除"}];
	//添加商品,1为添加，0为修改
	$scope.updateProduct = function(sign){
		var selectArray = $("#Product_list tbody input:checked");
		if(!selectArray || (selectArray.length!=1 && sign==0)){
			alertDialog("请选择一个");
			return;
		}
		var titleName = "添加商品信息";
		var ProductId = $(selectArray[0]).val();
		if(ProductId && sign==0){
			titleName = "修改商品信息";
			productService.detail(ProductId).then(function(response){
				var isOnsale = response.data.product.isOnsale;
				var status = response.data.product.status;
				$scope.product = response.data.product;
				$scope.product.isOnsale = isOnsale.toString();
				$scope.product.status = status.toString();
			});
		}else{
			$scope.product = {};
			$scope.product.parentId = '-1';
			$scope.product.isOnsale = '1';
			$scope.product.status = '0';
		}
		layer.open({
			type : 1,
			title : titleName,
			maxmin : true,
			shadeClose : true, //点击遮罩关闭层
			area : [ '576px', '468px' ],
			content : $('#Add_product_style'),
			btn : [ '保存', '取消' ],
			yes : function(index, layero) {
				if ($("#productName").val() == "") {
					alertDialog("商品名称不能为空");
					return;
				}
				if ($("#isOnsale").val() == "") {
					alertDialog("是否显示不能为空");
					return;
				}
				if ($("#sortNum").val() == "") {
					alertDialog("排序ID不能为空");
					return;
				}
				if ($("#status").val() == "") {
					alertDialog("状态不能为空");
					return;
				} else {
					var product = angular.copy($scope.product);
					var roleIds = [];
					$("#multiselect_to option").each(function(i,e){
						var selectVal = $(this).val();
						roleIds.push(selectVal);
					});
					if(!product){
						layer.msg('商品名称不能为空！', {
							time : 1000,
							icon : 1
						});
						return;
					}
					var isOnsaleStr = product.isOnsale;
					product.isOnsale = parseInt(isOnsaleStr);
					var statusStr = product.status;
					product.status = parseInt(statusStr);
					if(!ProductId){
						productService.addProduct(product).then(function(response){
							layer.alert(response.msg, {
								title : '提示框',
								icon : 1,
							},function(){
								layer.close(index);
								window.location.reload();
							});
						});
					}else{
						productService.editProduct(product).then(function(response){
							layer.alert('修改成功！', {
								title : '提示框',
								icon : 1,
							},function(){
								window.location.reload();
							});
							layer.close(index);
						},function(response){

						});
					}
				}
			}
		});
	}

	//删除商品分类
	$scope.deleteProduct = function(){
		var selectArray = $("#Product_list tbody input:checked");
		if(!selectArray || selectArray.length==0){
			alertDialog("请选择商品信息");
			return;
		}
		var productIds = new Array();
		$.each(selectArray,function(i,e){
			var val = $(this).val();
			productIds.push(val);
		});
		if(productIds.lenght==0){
			return;
		}
		layer.confirm('是否删除此商品？', {
			btn : [ '确定', '取消' ]
		}, function() {
			productService.deleteProduct(productIds).then(function(resp){
				layer.msg(resp.msg, {
					time : 1000,
					icon : 1
				},function(){
					window.location.reload();
				});
			});
		});
	}

	$scope.selectAll = function($event){
		var target = $event.target
		if($(target).prop("checked")){
			$(".subProductChkBox").each(function(i,e){
				$(this).attr("checked",true);
			});
		}else{
			$(".subProductChkBox").each(function(i,e){
				$(this).attr("checked",false);
			});
		}
	}
}]);