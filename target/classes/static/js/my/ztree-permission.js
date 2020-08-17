function getPermissionTree() {
    var root = {
        id : 0,
        name : "权限",
        open : true,
    };
    $.ajax({
        type : 'get',
        url : '/users/permissionAll',
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

function initMenuDatas2(userId){
    $.ajax({
        type : 'get',
        url : '/users/getPermissionIds?userId=' + userId,
        success : function(data) {
            var length = data.length;
            var ids = [];
            for(var i=0; i<length; i++){
                ids.push(data[i]);
            }
            initMenuCheck2(ids);
        }
    });
}

function initMenuCheck2(ids) {
    var treeObj = $.fn.zTree.getZTreeObj("powerTree");
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
function getCheckedMenuIds2(){
    var treeObj = $.fn.zTree.getZTreeObj("powerTree");
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

function initParentMenuSelect(){
    $.ajax({
        type : 'get',
        url : '/permissions/parents',
        async : false,
        success : function(data) {
            var select = $("#parentId");
            select.append("<option value='0'>root</option>");
            for(var i=0; i<data.length; i++){
                var d = data[i];
                var id = d['id'];
                var name = d['name'];

                select.append("<option value='"+ id +"'>" +name+"</option>");
            }
        }
    });
}

function getSetting2() {
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
            onCheck : zTreeOnCheck

        }
    };

    return setting;
}

function zTreeOnCheck(event, treeId, treeNode) {
//	console.log(treeNode.id + ", " + treeNode.name + "," + treeNode.checked
//			+ "," + treeNode.pId);
//	console.log(JSON.stringify(treeNode));

    return false;
}
