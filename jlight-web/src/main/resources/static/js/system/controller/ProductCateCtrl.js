var productCateApp = angular.module('productCateApp', ['base']);
productCateApp.controller('productCateCtrl', ['$rootScope', '$scope','productCateService',function ($rootScope,$scope,productCateService) {
	$('#multiselect').multiselect({});
	//搜索参数
	$scope.queryFilter = {};
	$scope.role = {	};
	$scope.isShowMap = [{isShow:0, isShowName: "显示"}, {isShow: 1, isShowName: "不显示"}];
	$scope.statusMap = [{status:0, statusName: "启用"}, {status: 1, statusName: "禁用"}];
	//添加用户,1为添加，0为修改
	$scope.updateProductCate = function(sign){
		var selectArray = $("#ProductCate_list tbody input:checked");
		if(!selectArray || (selectArray.length!=1 && sign==0)){
			alertDialog("请选择一个");
			return;
		}
		var titleName = "添加商品分类";
		var CateId = $(selectArray[0]).val();
		if(CateId && sign==0){
			titleName = "修改商品分类";
			productCateService.detail(CateId).then(function(response){
				var isShow = response.data.productCate.isShow;
				var status = response.data.productCate.status;
				$scope.productCate = response.data.productCate;
				$scope.productCate.isShow = isShow.toString();
				$scope.productCate.status = status.toString();
			});
		}else{
			$scope.productCate.isShow = '0';
			$scope.productCate.status = '0';
			productCateService.getProductCate().then(function(response){
				$scope.productCateMap = response.data;
			});
		}
		layer.open({
			type : 1,
			title : titleName,
			maxmin : true,
			shadeClose : true, //点击遮罩关闭层
			area : [ '576px', '468px' ],
			content : $('#Add_productCate_style'),
			btn : [ '保存', '取消' ],
			yes : function(index, layero) {
				if ($("#cateName").val() == "") {
					alertDialog("分类名称不能为空");
					return;
				}
				if ($("#isShow").val() == "") {
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
					var productCate = angular.copy($scope.productCate);
					var roleIds = [];
					$("#multiselect_to option").each(function(i,e){
						var selectVal = $(this).val();
						roleIds.push(selectVal);
					});
					if(!productCate){
						layer.msg('商品分类不能为空！', {
							time : 1000,
							icon : 1
						});
						return;
					}
					var isShowStr = productCate.isShow;
					productCate.isShow = parseInt(isShowStr);
					var statusStr = productCate.status;
					productCate.status = parseInt(statusStr);
					if(!CateId){
						productCateService.addProductCate(productCate).then(function(response){
							layer.alert(response.msg, {
								title : '提示框',
								icon : 1,
							},function(){
								layer.close(index);
								window.location.reload();
							});
						});
					}else{
						productCateService.editProductCate(productCate).then(function(response){
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
	$scope.deleteProductCate = function(){
		var selectArray = $("#ProductCate_list tbody input:checked");
		if(!selectArray || selectArray.length==0){
			alertDialog("请选择商品分类");
			return;
		}
		var cateIds = new Array();
		$.each(selectArray,function(i,e){
			var val = $(this).val();
			cateIds.push(val);
		});
		if(cateIds.lenght==0){
			return;
		}
		layer.confirm('是否删除商品分类？', {
			btn : [ '确定', '取消' ]
		}, function() {
			productCateService.deleteProductCate(cateIds).then(function(resp){
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
			$(".subProductCateChkBox").each(function(i,e){
				$(this).attr("checked",true);
			});
		}else{
			$(".subProductCateChkBox").each(function(i,e){
				$(this).attr("checked",false);
			});
		}
	}
 }]);