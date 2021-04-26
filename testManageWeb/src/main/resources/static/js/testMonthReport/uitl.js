/*
* 1.dom
* 2.方法
* */

/*1.dom*/

//插入节点到指定元素前边
function _insertBefore(newElement, targetElement) {
    targetElement.parentElement.insertBefore(newElement, targetElement);
}

/*2.dom*/

//插入节点到指定元素后边
function _insertAfter(newElement, targetElement) {
    var parent = targetElement.parentNode;
    if (parent.lastChild === targetElement) {
        parent.appendChild(newElement);
    } else {
        parent.insertBefore(newElement, targetElement.nextSibling);
    }
}

/*2
* 移除指定节点
* */
function _removeDom(target) {
    target.parentNode.removeChild(target);
}

// 目标元素是否是指定元素子元素,包括自己
function _isParent(target, parent) {
    if (target === parent) {
        return true
    } else if (target.parentNode) {
        return _isParent(target.parentNode, parent)
    } else {
        return false
    }
}

//判断是不是子元素 不包含自己
function _isSubElement(target, parent) {
    if (target.parentNode) {
        if (target.parentNode === parent) {
            return true
        } else {
            return _isSubElement(target.parentNode, parent)
        }
    } else {
        return false
    }
}

// 获取元素宽高以及位置信息
function _getDomRect(dom) {
    var xy = dom.getBoundingClientRect();
    var top = xy.top - document.documentElement.clientTop + document.documentElement.scrollTop,//document.documentElement.clientTop 在IE67中始终为2，其他高级点的浏览器为0
        bottom = xy.bottom,
        left = xy.left - document.documentElement.clientLeft + document.documentElement.scrollLeft,//document.documentElement.clientLeft 在IE67中始终为2，其他高级点的浏览器为0
        right = xy.right,
        width = xy.width || right - left, //IE67不存在width 使用right - left获得
        height = xy.height || bottom - top;

    return {
        top: top,
        right: right,
        bottom: bottom,
        left: left,
        width: width,
        height: height
    }
}

/*2.方法*/

//类型判断
function _typeOf(obj) {
    var map = {
        '[object Boolean]': 'boolean',
        '[object Number]': 'number',
        '[object String]': 'string',
        '[object Function]': 'function',
        '[object Array]': 'array',
        '[object Date]': 'date',
        '[object RegExp]': 'regExp',
        '[object Undefined]': 'undefined',
        '[object Null]': 'null',
        '[object Object]': 'object'
    };
    return map[Object.prototype.toString.call(obj)];
}

//阻止默认事件
function _preventDefault(event) {
    var ev = event || window.event;
    if (typeof ev.preventDefault != 'undefined') {//w3c
        ev.preventDefault();
    } else {
        ev.returnValue = false;//ie
    }
}

//阻止冒泡
function _stopBubble(e) {
    //如果提供了事件对象，则是一个非IE浏览器
    if (e && e.stopPropagation)
        //因此它支持W3C的stopPropagation()方法
        e.stopPropagation();
    else
        //否则，我们需要使用IE的方式来取消事件冒泡
        window.event.cancelBubble = true;
}

/*3.数据*/

//深度合并对象
function _deepObjectMerge(FirstOBJ, SecondOBJ) { // 深度合并对象
    for (var key in SecondOBJ) {
        FirstOBJ[key] = FirstOBJ[key] && ["[object Array]", "[object Object]"].indexOf(Object.prototype.toString.call(FirstOBJ[key])) !== -1 ?
            _deepObjectMerge(FirstOBJ[key], SecondOBJ[key]) : FirstOBJ[key] = SecondOBJ[key];
    }
    return FirstOBJ;
}

//深拷贝
function _deepClone(obj) {
    //判断拷贝的要进行深拷贝的是数组还是对象，是数组的话进行数组拷贝，对象的话进行对象拷贝
    var objClone = Array.isArray(obj) ? [] : {};
    //进行深拷贝的不能为空，并且是对象或者是
    if (obj && typeof obj === "object") {
        for (key in obj) {
            if (obj.hasOwnProperty(key)) {
                if (obj[key] && typeof obj[key] === "object") {
                    objClone[key] = _deepClone(obj[key]);
                } else {
                    objClone[key] = obj[key];
                }
            }
        }
    }
    return objClone;
}


//监听事件添加
function _addEvent(ele, event, fn) {
    //通过判断调用的方式兼容IE678
    //判断浏览器是否支持该方法，如果支持那么调用，如果不支持换其他方法
    if (ele.addEventListener) {
        //直接调用
        ele.addEventListener(event, fn);
    } else if (ele.attachEvent) {
        ele.attachEvent("on" + event, fn);
    } else {
        //在addEventListener和attachEvent都不存在的情况下，用此代码
        var oldEvent = ele["on" + event];
        ele["on" + event] = function () {
            if (oldEvent) oldEvent()
            fn()
        };
    }
}
//监听事件移除
function _removeEvent(ele, event, fn) {
    //通过判断调用的方式兼容IE678
    //判断浏览器是否支持该方法，如果支持那么调用，如果不支持换其他方法
    if (ele.removeEventListener)
        ele.removeEventListener(event, fn, false);
    else if (ele.deattachEvent) {               /*IE*/
        ele.deattachEvent('on' + event, fn);
    } else {
        ele["on" + event] = null;
        /*直接赋给事件*/
    }
}

//事件绑定移除
var _EventUtil = { /*检测绑定事件*/
    addHandler:
        function (element, type, handler) {
            if (element.addEventListener) {
                element.addEventListener(type, handler, false);
            } else if (element.attachEvent) {
                element.attachEvent('on' + type, handler);
            } else {
                element["on" + type] = handler /*直接赋给事件*/
            }
        }

    ,
    /*通过removeHandler*/
    removeHandler: function (element, type, handler) {   /*Chrome*/
        if (element.removeEventListener)
            element.removeEventListener(type, handler, false);
        else if (element.deattachEvent) {               /*IE*/
            element.deattachEvent('on' + type, handler);
        } else {
            element["on" + type] = null;
            /*直接赋给事件*/
        }
    }
}

//number 转字符串
function _formatFloat(value, n) {
    var f = Math.round(value * Math.pow(10, n)) / Math.pow(10, n);
    var s = f.toString();
    var rs = s.indexOf('.');
    if (rs < 0) {
        s += '.';
    }
    for (var i = s.length - s.indexOf('.'); i <= n; i++) {
        s += "0";
    }
    return s;
}

//科学计数法转换
function _scientificToNumber(num) {
    var str = num.toString();
    var reg = /^(\d+)(e)([-]?\d+)$/;
    var arr, len,
        zero = '';

    /*6e7或6e+7 都会自动转换数值*/
    if (!reg.test(str)) {
        return num;
    } else {
        /*6e-7 需要手动转换*/
        arr = reg.exec(str);
        len = Math.abs(arr[3]) - 1;
        for (var i = 0; i < len; i++) {
            zero += '0';
        }
        return '0.' + zero + arr[1];
    }
}

//数组移除包含元素
function _arrRemove(arr, value) {
    while (arr.indexOf(value) !== -1) {
        arr.splice(arr.indexOf(value), 1);
    }
}

//不改变属性的 attribute 的设置获取
var _attribute = {
    set: function (dom, attr, value) {
        dom.setAttribute(attr, JSON.stringify(value))
    },
    get: function (dom, attr) {
        try {
            return JSON.parse(dom.getAttribute(attr))
        } catch (e) {
            return dom.getAttribute(attr)
        }
    }
}

//防抖 操作完成后 delay 毫秒后执行一次
function _debounce(fun, delay, id) {
    if (id === undefined) {
        id = 'id'; //如果函数不指定id 则给默认id为值
    }
    var _this = this
    return function () {
        var _args = arguments
        //每次事件被触发，都会清除当前的timer，然后重写设置超时调用
        if (fun[id]) clearTimeout(fun[id])
        fun[id] = setTimeout(function () {
            fun.apply(_this, _args)
        }, delay ? delay : 300)
    }
}

//节流 操作时每隔 delay 毫秒执行一次
function _throttle(fun, delay) {
    return function (args) {
        var that = this;
        var _args = arguments;
        fun.now = +new Date();
        if (fun.last && fun.now < fun.last + delay) {
            if (fun.id) clearTimeout(fun.id)
            fun.id = setTimeout(function () {
                fun.last = fun.now;
                // fun.apply(that, _args); 会造成delay 后的一次执行
            }, delay)
        } else {
            fun.last = fun.now;
            fun.apply(that, _args);
        }
    }
}

//插入 id 为 styleId 样式为 cssText 的style 设置元素样式
function _changeStyleCssText(styleId, cssText) {
    var style = document.getElementById(styleId)
    if (!style) {
        style = document.createElement('style')
        style.id = styleId
        document.head.appendChild(style)
    }
    if (style.styleSheet) { //IE
        var func = function () {
            try { //防止IE中stylesheet数量超过限制而发生错误
                style.styleSheet.cssText = cssText;
            } catch (e) {
                console.log(e);
            }
        }
        //如果当前styleSheet还不能用，则放到异步中则行
        if (style.styleSheet.disabled) {
            setTimeout(func, 10);
        } else {
            func();
        }
    } else { //w3c
        style.innerHTML = cssText;
    }
}

/*阻止冒泡事件*/
function _cancelBubble(e) {
    window.event ? window.event.cancelBubble = true : e.stopPropagation();
}

function _generator(pre, end) {
    var num = +new Date()
    return (pre ? pre : '') + num + (end ? end : '')
}

/*获取url指定参数*/
function _getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]);
    return null;
}

/*获取url全部参数*/
function _getQueryObject(name) {
    var all = window.location.search.substr(1)
    var _allArr = all.split('&'), obj = {}
    for (var i = 0; i < _allArr.length; i++) {
        var _item = _allArr[i].split('=')
        obj[_item[0]] = _item[1]
    }
    return obj
}

/*获取鼠标点击位置*/
function _getMousePos(event) {
    var e = event || window.event;
    return {'x': e.clientX, 'y': e.clientY}
}

/*

* objectStr 这个是字符串格式 ’obj‘
*path 属性的路径 'a.b.c'
* val 要改成的值   新值
* */

function _setObject(objectStr, path, val) {
    eval(objectStr +'["'+path.replace(/\./g,'"]["')+'"]='+JSON.stringify(val));

}
/*

* objectStr 这个是字符串格式 ’obj‘
*path 属性的路径 'a.b.c'
* val 要改成的值   新值
* */

function _getObject(objectStr, path) {
    return  eval(objectStr +'["'+path.replace(/\./g,'"]["')+'"]');
}


/*正则获取匹配的数值 {{var}} */
function _getMatchedString(str) {
    var reg = /\{\{(.*?)\}\}/
    var reg_g = /\{\{(.*?)\}\}/g
    var result = str.match(reg_g)||[]
    var list = []
    for (var i = 0; i < result.length; i++) {
        var item = result[i]
        var res = item.match(reg)[1]
        list.push(res)
    }
    return list
}

/*正则替换匹配的数值 {{var}} */
function _replaceMatchedString(str, rp) {
    var reg_g = /\{\{(.+?)\}\}/g
    return result = str.replace(reg_g, function (a, b, c) {
        return rp
    })

}

/*获取焦点在输入框内文本的位置 oInput 要获取的对象*/
function _getCursorPosition(oInput){
    if(!oInput) return -1
    var cursorPosition=-1;
    if(oInput.selectionStart||oInput.selectionStart===0){//非IE浏览器
        cursorPosition= oInput.selectionStart;
    }else{//IE
        var range = document.selection.createRange();
        range.moveStart("character",-oInput.value.length);
        cursorPosition=range.text.length;
    }
    return cursorPosition
}