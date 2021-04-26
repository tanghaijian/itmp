/*添加 tpl 触发事件*/
_addEvent(document,'dblclick',function (e) {
    _cancelBubble(e)
    var ev=e||window.event
    var target=ev.target
    if(target.className.indexOf('edit_tpl')!==-1){
        var data=_getObject('globalData',$(target).attr('data-data')) //获取对应data {}
        var list=_getObject('globalData',$(target).attr('data-list')) //获取对应list []
        var id=$(target).attr('data-id') //获取对应tpl id
        var data_tpl=$(target).attr('data-tpl') //'a.b.c'
        var tpl=_getObject('globalData',data_tpl) //获取对应data
        tplEditModal.show({data:data,list:list,tpl:tpl},function (nTpl,hide) {
            $("#loading").css('display', 'block');
            $.ajax({
                url: "/testManage/monthlyReport/updateModule",
                method: "post",
                data: {id:id,content:nTpl},
                dataType: "json",
                success: function (res) {
                    if (res.status === '1') {
                        _setObject('globalData',data_tpl+'.content',nTpl) //修改对象tpl
                        // target.innerHTML=renderTpl(nTpl,data)
                        renderTplEdit(data_tpl,nTpl,data) //可能存在多个
                        hide()
                        layer.alert('修改成功!',{
                            icon: 1,
                        })
                    }else{
                        layer.alert('修改失败!',{
                            icon: 2,
                        })
                    }
                },
                complete:function () {
                    $("#loading").css('display', 'none');
                }
            });
        })
    }
})
/*模板渲染函数*/
function renderTpl(tpl,data) {
    var reg_g = /\{\{(.+?)\}\}/g
    return result = tpl.replace(reg_g, function (a, b, c) {
        return data[b] === undefined ? '{{' + b + '}}' : '<span class="edit_var" data-label="'+data[b].name+'" data-key="'+b+'">'+data[b].value+'</span>'
    })
}

function renderTplEdit(dataTpl,nTpl,data) {
    var attr="[data-tpl='"+dataTpl+"']"
    $(attr).each(function(index,target){
        target.innerHTML=renderTpl(nTpl,data)
    })
}
