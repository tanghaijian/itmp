function MenuRightPopper(option) {
    this.data={
        target:null,
        data:'', //转换变量
        list:[], //变量数组
        name:'', //模板名称
        id:null, //模板id
        num:0, //模板顺序
        index:null //模板下标
    }
    this.success = option.success ? option.success : function (data, hide) {
        console.log(data);
        hide()
    }
    this.cancel = option.cancel ? option.cancel : function (data, hide) {
        console.log(data);
        hide()
    }
    this.delete = option.delete ? option.delete : function (data, hide) {
        console.log(data);
        hide()
    }
    this.bindTrigger()
}

MenuRightPopper.prototype.bindTrigger = function () {
    var _this = this
    _addEvent(document,'contextmenu',function (e) {
        var ev = e || window.event
        var className=ev.target.className
        var type=-1;_this.data.index=null;_this.data.id=null;_this.data.target=null;_this.data.num=0
        if(className.indexOf('module_content')!==-1){
            type=0 ;
            _this.data.target=ev.target
        }
        if(className.indexOf('edit_tpl')!==-1) {
            type=1 ;
            _this.data.target=ev.target.parentNode
            _this.data.index=$(ev.target).attr('data-index')
            _this.data.id=$(ev.target).attr('data-id')
            _this.data.num=$(ev.target).attr('data-num')
        }
        if(className.indexOf('edit_var')!==-1) {
            type=2;
            _this.data.target=ev.target.parentNode.parentNode
            _this.data.index=$(ev.target.parentNode).attr('data-index')
            _this.data.id=$(ev.target.parentNode).attr('data-id')
            _this.data.num=$(ev.target.parentNode).attr('data-num')
        }
        if(type!==-1){
            e.preventDefault();
            _cancelBubble()
            _this.data.name=$(_this.data.target).attr('data-tpl')
            _this.data.data=$(_this.data.target).attr('data-data')
            _this.data.list=$(_this.data.target).attr('data-list')
            var pos = _getMousePos(e)
            _this.hide()
            _this.show(pos,type)
        }else{
            _this.hide()
        }
    })
}
MenuRightPopper.prototype.bindBtnClick = function () {
    var _this = this
    _addEvent(this.rightModal, 'click', function (e) {
        var ev = e || window.event
        _cancelBubble(e)
        if (ev.target.className.indexOf('operation-popper-item') !== -1) {
            var type = $(ev.target).attr('data-type')
            try {
                switch (type) {
                    case 'add':
                        _this.success( _this.data, _this.hide.bind(_this))
                        break;
                    case 'delete':
                        _this.delete( _this.data, _this.hide.bind(_this))
                        break;
                    case 'cancel':
                        _this.cancel( _this.data, _this.hide.bind(_this))
                        break;
                    default:
                        _this.cancel( _this.data, _this.hide.bind(_this))
                }

            } catch (e) {
                console.log(e);
            }
        }
    })
}
MenuRightPopper.prototype.show = function (pos,type) {
    this.rightModal = document.getElementById('right-menu-popper')
    if (!this.rightModal) {
        this.rightModal = document.createElement('div')
        this.rightModal.id = 'right-menu-popper'
        this.rightModal.style.display = 'none'
        document.body.appendChild(this.rightModal)
        this.bindBtnClick()
        this.dBindHide()
    }
    this.showPos(pos,type)
}
MenuRightPopper.prototype.showPos = function (pos,type) {
    if(type===0){
        this.rightModal.innerHTML = '<div data-type="add" class="operation-popper-item">新增</div><div data-type="cancel" class="operation-popper-item">取消</div>'
    }else{
        this.rightModal.innerHTML = '<div data-type="add" class="operation-popper-item">新增</div><div data-type="delete" class="operation-popper-item">删除</div><div data-type="cancel" class="operation-popper-item">取消</div>'
    }
    this.rightModal.style.top = pos.y + 'px'
    this.rightModal.style.left = pos.x + 'px'
    this.rightModal.style.display = 'block'
}
MenuRightPopper.prototype.hide = function () {
    var modal = document.getElementById('right-menu-popper')
    if (modal) {
        this.rightModal.style.display = 'none'
    }
}
MenuRightPopper.prototype.dBindHide = function () {
    var _this = this
    _addEvent(document, 'click', function (e) {
        var ev = e || window.event
        if (!_isParent(ev.target, document.getElementById('right-menu-popper'))) {
            _this.hide()
        }
    })
    _addEvent(document, 'oncontextmenu', function (e) {
        var ev = e || window.event
        if (!_isParent(ev.target, document.getElementById('right-menu-popper'))) {
            _this.hide()
        }
    })

}

