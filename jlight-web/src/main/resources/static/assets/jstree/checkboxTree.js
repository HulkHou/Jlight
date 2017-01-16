/* 
**带checkbox的树形控件使用说明 
**@url：此url应该返回一个json串，用于生成树 
**@id: 将树渲染到页面的某个div上，此div的id 
**@checkId:需要默认勾选的数节点id；1.checkId="all"，表示勾选所有节点 2.checkId=[1,2]表示勾选id为1,2的节点 
**节点的id号由url传入json串中的id决定 
*/  
loadedTreefag = false;
function showCheckboxTree(url,id,checkId){
	if(loadedTreefag){
		initTree(id,checkId);
	}
    var menuTree = jQuery("#"+id).bind("loaded.jstree",function(e,data){  
       loadedTreefag = true;
       initTree(id,checkId);
    }).jstree({  
        "core" : {  
        "data":{  
            "url":url,  
            "dataType":"json",  
            "cache":false  
        },  
        "attr":{  
            "class":"jstree-checked"  
        }  
      },  
      "types" : {  
        "default" : {  
          "valid_children" : ["default","file"]  
        },  
        "file" : {  
          "icon" : "glyphicon glyphicon-file",  
          "valid_children" : []  
        }  
      },  
      "checkbox" : {  
          "keep_selected_style" : false,  
          "real_checkboxes" : true  
      },  
      "plugins" : [  
        "contextmenu", "dnd", "search",  
        "types", "wholerow","checkbox"  
      ],  
      "contextmenu":{  
        "items":{  
            "create":null,  
            "rename":null,  
            "remove":null,  
            "ccp":null  
        }  
      }  
    });  
}  
  
function getCheckboxTreeSelNode(treeid){  
  var ids = Array();  
  jQuery("#"+treeid).find("li").each(function(){  
      var liid = jQuery(this).attr("id");  
      if(jQuery("#"+liid+">a").hasClass("jstree-clicked") || jQuery("#"+liid+">a>i").hasClass("jstree-undetermined")){  
          ids.push(liid);  
      }  
  });  
  return ids;  
}  

function initTree(id,checkId){
	 jQuery("#"+id).jstree("open_all");   
     jQuery("#"+id).find("li").each(function(){
     	jQuery("#"+id).jstree("uncheck_node",jQuery(this));  
         if(checkId == 'all'){  
             jQuery("#"+id).jstree("check_node",jQuery(this));  
         }else if(checkId instanceof Array){  
             for(var i=0;i<checkId.length;i++){  
                 if(jQuery(this).attr("id") == checkId[i]){
                   jQuery("#"+id).jstree("check_node",jQuery(this));  
                 }  
             }  
         }  
     });  
}

