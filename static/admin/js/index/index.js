//定义一些全局变量 开始
var parentId;
var parentName;
var rMenu;
var zTree;
var idTemp;
var zNodes;

//定义一些全局变量 结束
// zTree 的参数配置，深入使用请参考 API 文档（setting 配置详解）
var setting = {
		    /*check: {
				enable: true,
				autoCheckTrigger: true // 设置自动关联勾选时是否触发,如果勾选了父节点，那么自动勾选子节点,如果设置 setting.check.chkboxType = { "Y": "", "N": "" }，将不会有任何自动关联勾选的操作。
			//chkDisabledInherit: true //当父节点设置 chkDisabled = true 时，设置子节点是否自动继承 chkDisabled = true 。
									 //[setting.check.enable = true 时生效]
			                         //1、只使用于初始化节点时，便于批量操作。 对于已存在的节点请利用 setChkDisabled 方法单个节点设置。
									 //默认值: false
		},*/
		
		
		view: {
			dblClickExpand: false,
			showLine: false,
			
		},
		data: {
			simpleData: {
				enable: true,
				idKey : "id",
				pIdKey: "pid"
			}
		},
		callback: {
			onClick: menuOnClick,
		}

  };
  
  // zTree 的数据属性，深入使用请参考 API 文档（zTreeNode 节点数据详解

  


/**
 * 点击菜单，出发add  Panel的事件
 * @param event
 * @param treeId
 * @param treeNode
 */
function menuOnClick(event, treeId, treeNode) {
	//是叶子节点，才打开tab
	zTree.expandNode(treeNode);
	if(treeNode.isParent == false){
		addPanel(treeNode.id,treeNode.name,treeNode.location);
	}
};



/**
 * document Ready
 * 文档加载完成
 */
$(function (){
	var url = basePath + "/renderMenu.htm"; //查询到全部的菜单 
	$.ajax({
		url : url,
		type : "get",
		dataType : "json",
		global : false,
		async : false,
		success : function(data) {
			if(data.length > 0){
				zNodes = data;
			}else{
				zNodes = null;
			}
		}
	});
	
	/**
	 * 加载左侧的树
	 */
	$.fn.zTree.init($("#menuTree"), setting, zNodes);
	 
	
	//实例化menu的onClick事件
	  $("#tabsMenu").menu({
	    onClick:function(item){
	      CloseTab(this,item.name);
	    }
	  });
	  //轮询查看新消息
	  
	  zTree = $.fn.zTree.getZTreeObj("menuTree");
	  rMenu = $("#rMenu");
	  zTree.expandAll(true);//默认展开所有的节点
	  
	  //
	  //var nodes = zTree.getNodes();
	 // nodes.font = { 'color': 'red' }; 
	 // zTree.updateNode(nodes);
	  //
});



//修改密码
function modifyPassword(){
	var tabExists = $('#appendTab').tabs('exists',"修改密码");
	//如果菜单已经打开，那么不需要打开，只需要让其获得焦点就好。为选中状态
	if(!tabExists){
		$('#appendTab').tabs('add',{
			title: "修改密码",
			fit : true,
			//href: basePath + "/" + location,
			content : '<iframe src='+basePath + "/jsp/infromation.jsp" +' allowTransparency="true" style="border:0;width:100%;height:99%;" frameBorder="0"></iframe>',
			closable: true
		});
	}else{//否则：为 选中状态
		$('#appendTab').tabs('select',"修改密码");//如果已经打开，则让当前所点击菜单为选中状态
	}
	
}
//右键的几个关闭事件的实现
function CloseTab(menu,type){
  var curTabTitle = $(menu).data("tabTitle");
  var tabs = $("#appendTab");

  /**
   * 关闭当前
   */
  if(type === "closeCurrent"){
     tabs.tabs("close",curTabTitle); //close是easyUI的一个事件
     return;
  }
  
  var allTabs = tabs.tabs("tabs");
  var closeTabsTitle = [];
  
  $.each(allTabs,function(){
    var opt = $(this).panel("options");
    if(opt.closable && opt.title != curTabTitle && type === "closeOther"){
      closeTabsTitle.push(opt.title);
    }else if(opt.closable && type === "closeAll"){
      closeTabsTitle.push(opt.title);
    }
  });
  
  for(var i = 0;i<closeTabsTitle.length;i++){
	  tabs.tabs("close",closeTabsTitle[i]);
  }
}


//左侧菜单结束


//点击菜单添加tab 开始
var index = 0;
/**
 * 点击菜单，添加panel
 * @param menuId
 * @param menuName
 * @param location
 */
function addPanel(menuId,menuName,location){
	var tabExists = $('#appendTab').tabs('exists',menuName);
	//如果菜单已经打开，那么不需要打开，只需要让其获得焦点就好。为选中状态
	if(!tabExists){
		$('#appendTab').tabs('add',{
			title: menuName,
			fit : true,
			//href: basePath + "/" + location,
			content : '<iframe src='+basePath + "/" + location+' allowTransparency="true" style="border:0;width:100%;height:99%;" frameBorder="0"></iframe>',
			closable: true
		});
	}else{//否则：为 选中状态
		$('#appendTab').tabs('select',menuName);//如果已经打开，则让当前所点击菜单为选中状态
	}
}

function removePanel(){
	var tab = $('#appendTab').tabs('getSelected');
	if (tab){
		var index = $('#appendTab').tabs('getTabIndex', tab);
		$('#appendTab').tabs('close', index);
	}
}
//点击菜单添加tab 结束
function logout(){
	$.messager.confirm('确认对话框', '您想要退出该系统吗？', function(r){
	    if (r){
			window.location.href= basePath +'/PangXie.htm';
	    }
    });
}










