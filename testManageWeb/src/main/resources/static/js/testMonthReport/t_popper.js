//tpl 编辑
var tplEditModal = {
    inputIndex:-1,
    data: {},
    notMatch: [],
    callback: function (tpl, hide) {
        hide()
    },
    init: function () {
        var html = '\n' +
            '<div id="tplEditModal" class="modal fade" tabindex="-1" role="dialog">\n' +
            '    <div class="modal-dialog" role="document">\n' +
            '        <div class="modal-content">\n' +
            '            <div class="modal-header">\n' +
            '                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>\n' +
            '                </button>\n' +
            '                <h4 class="modal-title">模板修改</h4>\n' +
            '            </div>\n' +
            '            <div class="modal-body">\n' +
            '                <div>\n' +
            '                    <form class="form-horizontal">\n' +
            '                        <div class="form-group" style="display: flex;justify-content: center;margin-bottom:5px;">\n' +
            '                            <label class="col-sm-2 control-label">变量：</label>\n' +
            '                            <div id="tplEditModal_selects" class="col-sm-10">\n' +
                '                            <select class="form-control" style="display: inline-block;width: auto;margin-bottom:10px;" id="tplEditModal_select_1" onchange="tplEditModal.selectOnclick(this)"></select>\n' +
                '                            <select class="form-control" style="display: inline-block;width: auto;margin-bottom:10px;" id="tplEditModal_select_2" onchange="tplEditModal.onSelectChange(this)"></select>\n' +
            '                            </div>\n' +
            '                        </div>\n' +
            '                        <div class="form-group">\n' +
            '                            <label class="col-sm-2 control-label">选中值：</label>\n' +
            '                            <div class="col-sm-10">\n' +
            '                                <div id="tplEditModal_selected" style="padding: 6px 0;"></div>\n' +
            '                            </div>\n' +
            '                        </div>\n' +
            '                        <div class="form-group">\n' +
            '                            <label class="col-sm-2 control-label">模板：</label>\n' +
            '                            <div class="col-sm-10">\n' +
            '                                <textarea id="tplEditModal_tpl" onblur="tplEditModal.getNowIndex(this)" class="form-control" style="width: 100%" rows="3"></textarea>\n' +
            '                                <div style="margin-top: 10px"><span onclick="tplEditModal.tplPreview()" class="btn btn-default">预览</span></div>\n' +
            '                            </div>\n' +
            '                        </div>\n' +
            '                        <div class="form-group">\n' +
            '                            <label class="col-sm-2 control-label">预览：</label>\n' +
            '                            <div class="col-sm-10">\n' +
            '                                <div id="tplEditModal_review" style="padding: 6px 0;"></div>\n' +
            '                            </div>\n' +
            '                        </div>\n' +
            '                    </form>\n' +
            '                </div>\n' +
            '            </div>\n' +
            '            <div class="modal-footer">\n' +
            '                <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>\n' +
            '                <button onclick="tplEditModal.submit()" type="button" class="btn btn-primary">提交</button>\n' +
            '            </div>\n' +
            '        </div><!-- /.modal-content -->\n' +
            '    </div><!-- /.modal-dialog -->\n' +
            '</div><!-- /.modal -->'
        $('body').append(html)
    },
    show: function (data, cb, type) { //data list:变量数组 html：预览 tpl：模板
        cb ? this.callback = cb : ''
        this.data = data
        this.initTemplate(data)
        // this.initSelects(data.list) //初始化select
        this.renderSelect(data.list) //初始化select
        $("#tplEditModal").modal("show");
    },
    hide: function () {
        $("#tplEditModal").modal("hide");
    },
    renderSelect: function (list) {
        list && list.length > 0 ? '' : list = []
        var options = '<option value="" selected disabled>请选择</option>'
        for (var i = 0; i < list.length; i++) {
            options += '<option  value="' + list[i].code + '-'+i+'">' + list[i].name + '</option>'
        }
        $('#tplEditModal_select_1').html(options)
        this.selectOnclick()
    },
    selectOnclick: function (_this) {
        var val = _this?_this.value:'value-x';			//value-index 获取第一个下拉框中option的值
        var index=val.split('-')[1]
        var list=this.data.list&&this.data.list[index]?this.data.list[index].list:[]

        var options = '<option value="" selected disabled>请选择</option>'
        for (var i = 0; i < list.length; i++) {
            options += '<option data-name="'+list[i].name+'" value="' + list[i].code + '">' + list[i].name + '</option>'
        }
        document.getElementById("tplEditModal_select_2").innerHTML=options
    },
    initTemplate: function (data) {
        var tpl = data.tpl.content, html = renderTpl(tpl, data.data)
        var selectVarTpl =getNewTplSelectedVarHtml(tpl,data.data)
        $('#tplEditModal_selected').html(selectVarTpl)
        $('#tplEditModal_tpl').val(tpl)
        $('#tplEditModal_review').html(html)
    },
    onSelectChange: function (_this) {
        var _var = $(_this).val();
        if(!_var) return false
        this.changeTplHtml(_var)
        this.changeSelectedHtml()
    },
    changeTplHtml: function (_var) {
        var _oldHtml = $('#tplEditModal_tpl').val()
        var _newHtml=_oldHtml + '{{' + _var + '}}'
        if(this.inputIndex>=0) {
            _newHtml=_oldHtml.slice(0,this.inputIndex)+'{{' + _var + '}}'+_oldHtml.slice(this.inputIndex)
            this.inputIndex+=_var.length+4
        }
        $('#tplEditModal_tpl').val(_newHtml)
    },
    changeSelectedHtml: function () {
        var tpl = $('#tplEditModal_tpl').val()
        var selectVarTpl =getNewTplSelectedVarHtml(tpl,this.data.data)
        $('#tplEditModal_selected').html(selectVarTpl)
    },
    tplPreview: function () {
        var tpl = $('#tplEditModal_tpl').val()
        var tplHtml = replaceMatchedString(tpl, this.data.data)
        this.notMatch = getMatchedString(tplHtml)
        $('#tplEditModal_review').html(tplHtml)
    },
    submit: function () {
        this.tplPreview()
        if (this.notMatch.length > 0) {
            console.log(this.notMatch)
            layer.alert('变量'+this.notMatch.splice(',')+'不存在',{
                icon: 0,
            })
        } else {
            var tpl = $('#tplEditModal_tpl').val(),tplHtml=$('#tplEditModal_review').html()
            try {
                this.callback(tpl, this.hide,tplHtml)
            } catch (e) {
                console.log(e);
            }
        }
    },
    getNowIndex:function(_this){
        this.inputIndex=_getCursorPosition(_this)
    }
}

function getMatchedString(str) {
    var reg = /\{\{(.*?)\}\}/
    var reg_g = /\{\{(.*?)\}\}/g
    var result = str.match(reg_g) || []
    var list = []
    for (var i = 0; i < result.length; i++) {
        var item = result[i]
        var res = item.match(reg)[1]
        list.push(res)
    }
    return list
}

function replaceMatchedString(str, obj) {
    var reg_g = /\{\{(.+?)\}\}/g
    return result = str.replace(reg_g, function (a, b, c) {
        return obj[b] === undefined ? '{{' + b + '}}' : obj[b].value
    })

}
function getNewTplSelectedVarHtml(tpl,data) {
    var _mArr = getMatchedString(tpl), _uniArr = []
    for (var i = 1, len = _mArr.length; i < len + 1; i++) {
        var _var = _mArr[len - i],_var_name=_var+'-'+data[_var].name
        if (_uniArr.indexOf(_var_name) === -1) {
            _uniArr.unshift(_var_name)
        }
    }
    return _uniArr.join(',')
}
