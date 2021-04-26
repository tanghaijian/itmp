//变量编辑
function EditPopper(option) {
    this.el = option.el
    this.target=''
    this.value = ''
    this.nValue = ''
    this.title = option.title||'标题'
    this.success = option.success||function (target,val,hide) {
        console.log('submit:'+val);
        hide()
    }
    this.fail = option.fail||function (val) {
        console.log('fail:'+val);
    }
    this.modal = null
    this.pos={
        top:0,
        left:0,
        width:0,
        height:0
    }
    this.init()
}

EditPopper.prototype.init = function () {
    var that = this
    _addEvent(document, 'dblclick', function (e) {
        var ev = e || window.event
        var target = ev.target
        _cancelBubble()
        if (target.className.indexOf(that.el) !== -1) {
            that.target=target
            that.value=target.innerHTML
            that.title='修改-'+_attribute.get(target,'data-label')
            that.pos=_getDomRect(target)
            that.show()
        }
    })
}
EditPopper.prototype.show = function () {
    this.destroyModal()
    this.modal=document.createElement('div')
    this.modal.id='l-popper'
    this.modal.className='l-popper bottom'
    this.modal.style.top=this.pos.top+this.pos.height+10+'px'
    this.modal.style.left=this.pos.left+this.pos.width/2+'px'
    this.modal.innerHTML= '<div class="l-popper-arrow ">' + '</div>' +
        '<div class="l-popper-title">' + this.title + '</div>' +
        '<div class="l-popper-content">' +
        '<div class="l-popper-inputWrap">' +
        '<input class="l-popper-input" value="' + this.value + '">' +
        '</div>' +
        '<div class="l-popper-btnWrap">' +
        '<span class="l-popper-submit">确定</span>' +
        '<span class="l-popper-cancel">取消</span>' +
        '</div>' +
        '</div>'
    document.body.appendChild(this.modal)
    this.bindBtnEvent()
}
EditPopper.prototype.destroyModal = function () {
    var modal = document.getElementById('l-popper')
    if (modal) modal.parentNode.removeChild(modal) //ie 不支持remove
    this.modal = null
}
EditPopper.prototype.destroyModalTrue = function () {
    this.target.innerHTML=this.nValue
    var modal = document.getElementById('l-popper')
    if (modal) modal.parentNode.removeChild(modal) //ie 不支持remove
    this.modal = null
}
EditPopper.prototype.bindBtnEvent = function () {
    var _this = this
    this.submit = this.modal.getElementsByClassName('l-popper-submit')[0]
    this.cancel = this.modal.getElementsByClassName('l-popper-cancel')[0]
    _addEvent(this.submit, 'click', function () {
        _this.nValue=_this.saveInput()
        try {
            _this.success(_this.target,_this.nValue,_this.destroyModalTrue.bind(_this))
        } catch (e) {
            console.log(e);
        }
    })
    _addEvent(this.cancel, 'click', function () {
        _this.destroyModal()
        try {
            _this.fail('取消')
        } catch (e) {
            console.log(e);
        }
    })
}
EditPopper.prototype.saveInput = function () {
    return this.modal.getElementsByClassName('l-popper-input')[0].value
}
EditPopper.prototype.hide = function () {
    this.destroyModal()
}
