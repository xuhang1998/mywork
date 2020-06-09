function getMenuTree() {
	var root = {
		id : 0,
		name : "爱好",
		open : true,
	};
	$.ajax({
		type : 'get',
		url : '/users/hobbyAll',
		contentType : "application/json; charset=utf-8",
		async : false,
		success : function(data) {
			var length = data.length;
			var children = [];
			for (var i = 0; i < length; i++) {
				var d = data[i];
				var node = createNode(d);
				children[i] = node;
			}

			root.children = children;
		}
	});

	return root;
}

function initMenuDatas(userId){
	$.ajax({
		type : 'get',
		url : '/users/getHobbyIds?userId=' + userId,
		success : function(data) {
			var length = data.length;
			var ids = [];
			for(var i=0; i<length; i++){
				ids.push(data[i]);
			}

			initMenuCheck(ids);
		}
	});
}

function initMenuCheck(ids) {
	var treeObj = $.fn.zTree.getZTreeObj("hobbyTree");
	var length = ids.length;
	if(length > 0){
		var node = treeObj.getNodeByParam("id", 0, null);
		treeObj.checkNode(node, true, false);
	}

	for(var i=0; i<length; i++){
		var node = treeObj.getNodeByParam("id", ids[i], null);
		treeObj.checkNode(node, true, false);
	}

}
function getCheckedMenuIds(){
	var treeObj = $.fn.zTree.getZTreeObj("hobbyTree");
	var nodes = treeObj.getCheckedNodes(true);
	
	var length = nodes.length;
	var ids = [];
	for(var i=0; i<length; i++){
		var n = nodes[i];
		var id = n['id'];
		ids.push(id);
	}
	
	return ids;
}

function createNode(d) {
	var id = d['id'];
	var pId = d['pid'];
	var name = d['name'];
	var child = d['child'];

	var node = {
		open : true,
		id : id,
		name : name,
		pId : pId,
	};
	if (child != null) {
		var length = child.length;
		if (length > 0) {
			var children = [];
			for (var i = 0; i < length; i++) {
				children[i] = createNode(child[i]);
			}

			node.children = children;
		}

	}
	return node;
}



function getSetting() {
	var setting = {
		check : {
			enable : true,
			chkboxType : {
				"Y" : "ps",
				"N" : "ps"
			}
		},
		async : {
			enable : true,
		},
		data : {
			simpleData : {
				enable : true,
				idKey : "id",
				pIdKey : "pid",
				rootPId : 0
			}
		},
		callback : {
			onCheck : zTreeOnCheck,
			onClick : xx
		}
	};

	return setting;
}

function zTreeOnCheck(event, treeId, treeNode) {
	console.log(treeNode.id + ", " + treeNode.name + "," + treeNode.checked
			+ "," + treeNode.pId);
	console.log(JSON.stringify(treeNode));
	return false;
}
function xx(event,treeId,treeNode){
	console.log(treeNode.name);


}
