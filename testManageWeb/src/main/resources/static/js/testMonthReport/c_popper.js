//表单编辑
function ChartPopper() {
    this.el = ''
    this.value = ''
    this.title = '标题'
    this.success = function (val,hide) {
        console.log('确定'+val);
        hide()
    }
    this.cancel = function (val) {
        console.log('取消'+val);
    }
    this.pos = {top:0,left:0}
    this.modal=null
}
//option={el:'',value:'',title:''}
ChartPopper.prototype.show = function (option) {
    this.el = document.getElementById(option.el)
    if(!this.el) return false
    option.value!==''?this.value =option.value:''
    option.title?this.title =option.title:''
    option.success?this.success =option.success:''
    option.cancel?this.cancel =option.cancel:''
    option.pos?this.pos =option.pos:''
    this.destroyModal()
    this.createdPopper()

}
ChartPopper.prototype.createdPopper = function () {
    var _popper=document.createElement('div')
    var html='<div class="l-popper-arrow "></div><div class="l-popper-title">'+this.title+'</div><div class="l-popper-content"><div class="l-popper-inputWrap"><input class="l-popper-input" value="'+this.value+'"></div><div class="l-popper-btnWrap"><span class="l-popper-submit">确定</span><span class="l-popper-cancel">取消</span></div></div>'
    _popper.id='l-popper'
    _popper.className='l-popper bottom'
    _popper.style.top=this.pos.top+'px'
    _popper.style.left=this.pos.left+'px'
    _popper.innerHTML=html
    this.modal=_popper
    this.el.appendChild(_popper)
    this.bindBtnEvent()
}
ChartPopper.prototype.destroyModal = function () {
    var modal = document.getElementById('l-popper')
    if (modal) modal.parentNode.removeChild(modal) //ie 不支持remove
    this.modal = null
}
ChartPopper.prototype.bindBtnEvent = function () {
    var _this = this
    this.submitBtn=this.modal.getElementsByClassName('l-popper-submit')[0]
    this.cancelBtn=this.modal.getElementsByClassName('l-popper-cancel')[0]
    // var oldSubmit=this.submitBtn.onclick
    // var oldCancel=this.cancelBtn.onclick
    this.submitBtn.onclick=function(){
        var val = _this.saveInput()
        try {
            // if (oldSubmit) oldSubmit()
            _this.success(val, _this.destroyModal.bind(_this))
        } catch (e) {
            console.log(e);
        }
    }
    this.cancelBtn.onclick=function(){
        var val = _this.saveInput()
        _this.destroyModal()
        try {
            // if (oldCancel) oldCancel()
            _this.cancel(val)
        } catch (e) {
            console.log(e);
        }
    }
}
ChartPopper.prototype.saveInput = function () {
    return this.modal.getElementsByClassName('l-popper-input')[0].value
}
ChartPopper.prototype.hide = function () {
    this.destroyModal()
}

var popper = new ChartPopper()