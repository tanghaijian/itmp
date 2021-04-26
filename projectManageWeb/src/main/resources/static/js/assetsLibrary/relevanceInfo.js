/**
 * Created by 朱颜辞进花辞树 on 2018/9/18.
 * 资产库（需求视角）-右边列表关联信息js
 */
var parameterArr = {};
parameterArr.arr = window.location.href.split("?");
parameterArr.parameterArr = parameterArr.arr[1].split(",");
parameterArr.obj = {};
for (var i = 0; i < parameterArr.parameterArr.length; i++) {
    var obj = parameterArr.parameterArr[i].split("=");
    parameterArr.obj[obj[0]] = obj[1];
}
var mydata = { 
    treeSetting :{   // 树的配置信息
        data: {
            simpleData: {
                enable: true
            },
            key:{
            	name:"showName"
            }
        },
        view: { 
			nameIsHTML: true
		},
        callback: {
            onClick: getDocumentTable
        }
    }
} 
$(document).ready(function () {
    getTree();
    $('a[href="#other"]').on('shown.bs.tab', function (e) {
        if( $(this).attr('flag') == '1' ){
            load_jsmind();
            $(this).attr('flag',"2");
        }
    })
})

//获取关联信息数据
function getTree(){
	$.ajax({
        url:"/projectManage/assetsLibraryRq/getRelationInfo",
        type:"post",
        data:{ 
        	documetId : parameterArr.obj.id
        },
        success:function(data){ 
        	data.reqZnodes.map(function ( item ){
        		item["lev"] = item.level;
        		delete item.level;
                //  .icon_doc,.icon_code,.icon_task
                if( item.type ){
                    if( item.type == "doc" ){//关联的文档信息
                        item.iconSkin = "icon_doc"
                        item.showName = " <strong>【"+ isValueNull(item.docType) +"】</strong> " + isValueNull(item.name);
                    }else if( item.type == "require"){//关联的需求信息
                        item.iconSkin = "icon_task";
                        item.showName = " <strong>【需求变更单】</strong> " + isValueNull(item.code) + " " + isValueNull(item.name);	
                    }else if( item.type == "system"){//关联的系统信息
                        item.iconSkin = "icon_task";
                        item.showName = " <strong>【关联系统】</strong> " + isValueNull(item.code) + " " + isValueNull(item.name);
                    }

                    else if( item.type == "feature"){//关联的开发任务信息
                        item.iconSkin = "icon_task";
                        item.showName = " <strong>【开发任务】</strong> " + isValueNull(item.code) + " " + isValueNull(item.name);
                    }else if( item.type == "devtask"){//关联的开发工作任务信息
                        item.iconSkin = "icon_task";
                        item.showName = " <strong>【开发工作任务】</strong> " + isValueNull(item.code) + " " + isValueNull(item.name);
                    }else if( item.type == "scm"){//关联的提交代码信息
                        item.iconSkin = "icon_code";
                        item.showName = " <strong>【代码】</strong> " + isValueNull(item.name);
                    }
                } 
            })
            $.fn.zTree.init($("#menuTree"),  mydata.treeSetting , data.reqZnodes);
        },
        error:function(data){
        	 console.log( data );
        }
    }) 
    return; 
}
function getDocumentTable() {
    
}

//思维导图（只做图例，没有实际数据）
function load_jsmind(){
    var mind = {
        meta:{
            name:'demo',
            author:'hizzgdev@163.com',
            version:'0.2'
        },
        format:'node_array',
        data:[
            {"id":"root", "isroot":true, "topic":"jsMind"},

            {"id":"sub1", "parentid":"root","direction":"left", "topic":"sub1"},
            {"id":"sub11", "parentid":"sub1", "topic":"sub11"},
            {"id":"sub12", "parentid":"sub1", "topic":"sub12"},
            {"id":"sub13", "parentid":"sub1", "topic":"sub13"},

            {"id":"sub2", "parentid":"root","direction":"right", "topic":"sub2"},
            {"id":"sub21", "parentid":"sub2", "topic":"sub21"},
            {"id":"sub22", "parentid":"sub2", "topic":"sub22"},

            {"id":"sub3", "parentid":"root","direction":"right" , "topic":"sub3"},
        ]
    };
    var options = {
        container:'jsmind_container',
        editable:true,
        theme:'primary'
    }
    var jm = new jsMind(options);
    jm.show(mind);
    // jm.set_readonly(true);
    // var mind_data = jm.get_data();
    // alert(mind_data);
}
