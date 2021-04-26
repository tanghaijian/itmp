$(document).ready(function () {
    $.ajaxSetup({
        complete: function (XMLHttpRequest, textStatus) {
            if (XMLHttpRequest.status == 999) {
                window.parent.location.href = XMLHttpRequest.getResponseHeader('logoutUrl');
            }
        }
    });
})

// 添加验证提示
function add_Validator(that) {
     $(that).siblings('i').removeClass('glyphicon-ok').addClass('glyphicon-remove').parent().parent().removeClass('has-success').addClass('has-error');
	 $(that).parent().parent().find('small').show();
}

/**
 *@author liushan
 *@Description 文件大小控制
 *@Param
 *@return
 **/
var commonFileOpt = {
    fileSizeTotal: 1048576000, // 文件公共大小
    fileSizeOne: 10485760, // 单个文件大小
    fileSizeOneMsg:"超过10MB",
    fileSizeTotalMsg:"文件太大,请删选！！！"
};

/**
 *@author liushan
 *@Description 文件图片地址
 *@Date 2019/9/26
 *@Param
 *@return
 **/
var filePicAddress = {
    _icon_word: "/systemui/static/images/file/word.png",
    _icon_excel: "/systemui/static/images/file/excel.png",
    _icon_text: "/systemui/static/images/file/text.png",
    _icon_pdf: "/systemui/static/images/file/pdf.png",
    _icon_ppt: "/systemui/static/images/file/ppt.png",
    _icon_PNG: "/systemui/static/images/file/PNG.png",
    _icon_JPG: "/systemui/static/images/file/JPG.png",
    _icon_BMP: "/systemui/static/images/file/BMP.png",
    _icon_WENJIAN: "/systemui/static/images/file/wenjian.png"
};

/**
 *@author liushan
 *@Description 提取文件图片显示
 *@Date 2019/9/26
 *@Param
 *@return
 **/
function filePicClassPath(fileType) {
    var classType = "";
    switch (fileType) {
        case "doc":
        case "docx":
            classType = '<img src="' + filePicAddress._icon_word + '" />';
            break;
        case "xls":
        case "xlsx":
            classType = '<img src="' + filePicAddress._icon_excel + '" />';
            break;
        case "txt":
            classType = '<img src="' + filePicAddress._icon_text + '" />';
            break;
        case "pdf":
            classType = '<img src="' + filePicAddress._icon_pdf + '" />';
            break;
        case "ppt":
        case "PPT":
            classType = '<img src="' + filePicAddress._icon_ppt + '" />';
            break;
        case "JPG":
        case "jpg":
        case "gif":
        case "bmp":
        case "BMP":
        case "PNG":
        case "png":
            classType = '<img src="' + filePicAddress._icon_JPG + '" />';
            break;
        default:
            classType = '<img src="' + filePicAddress._icon_WENJIAN + '" />';
            break;
    }
    return classType;
}

/**
 *@author liushan
 *@Description 公共表格控制
 *@Param
 *@return
 **/
var commonTableOpt = {
    rowList: [10, 20, 50, 100] // 页面显示分页
};


function showSreachShow(This) {
    if ($(This).text() == '收起表格搜索') {
        $(This).text('展开表格搜索')
        $(".ui-search-toolbar").css('display', 'none');
    } else {
        $(This).text('收起表格搜索');
        $(".ui-search-toolbar").css('display', 'table-row');
    }
}
function stopDefaultEvent(e) {
    if (e && e.stopPropagation) {
        e.stopPropagation();
    } else if (window.event) {
        window.event.cancelBubble = true;
    }
}
/**
 * 选自己
 * add by ztt
 * */
function selectMyself(that) {
    $($(that).parent().parent().children('input[type^="hidden"]')).val(uid);
    $($(that).parent().parent().children('input[type^="text"]')).val(username).attr('username',username).change();
}

//转义  元素的innerHTML内容即为转义后的字符
function htmlEncodeJQ(str) {
    return $('<span/>').text(str).html();
}
//解析 
function htmlDecodeJQ(str) {
    return $('<span/>').html(str).text();
}
/**
 * 判断元素是否为空值
 */
function _is_null(ele,ele2){
	if(!ele2) return ele.val() ? true : false;
	return ele.val() ? true : false || ele2.val() ? true : false;
}

/**
 * 解决数据出现null，页面出现null
 */
function isValueNull(value) {
    return value != null && value != undefined ? htmlDecodeJQ(value) : '';
}

// jqgrid 选中表格变样式
function tableMouseover(tableId) {
    $(tableId).delegate("tr", "mouseover", function () {
        $(this).addClass("tr-mouseover");

    });

    $(tableId).delegate("tr", "mouseout", function () {
        $(this).removeClass("tr-mouseover");
    });
}

// 搜索展开和收起
function downOrUpButton() {
    $(".downBtn").click(function () {
        if ($(this).children("span").hasClass("fa-caret-up")) {
            $(this).children("span").removeClass("fa-caret-up");
            $(this).children("span").addClass("fa-sort-desc");
            $(this).prev().slideUp(200);
        } else {
            $(this).children("span").removeClass("fa-sort-desc");
            $(this).children("span").addClass("fa-caret-up");
            $(this).prev().slideDown(200);
        }
    });
}

/**
 *@author liushan
 *@Description 请求失败提示信息
 *@Date 2019/8/28
 *@Param
 *@return
 **/
function errorFunMsg(XMLHttpRequest, textStatus,errorThrown) {
    $("#loading").css('display', 'none');
    if (XMLHttpRequest.status != 999) {
        layer.alert("系统内部错误，请联系管理员 ！！！", {
            icon: 2,
            title: "提示信息"
        });
    }
}

// 所有的Input标签，在输入值后出现清空的按钮
function buttonClear() {

    $('input[type="text"]').parent().css("position", "relative");
    $('input[type="text"]').parent().append("<span onclick='clearContent(this)' class='btn_clear'></span>");
    $('input[type="text"]').bind("input propertychange change", function () {
        if ($(this).val() != "") {
            $(this).parent().children(".btn_clear").css("display", "block");
        } else {
            $(this).parent().children(".btn_clear").css("display", "none");
        }
    });
}

// 清空表格内容
function clearContent(that) {
    $(that).parent().children('input').val("").change();
    $(that).parent().children(".btn_clear").css("display", "none");
    // $(that).siblings('i').removeClass('glyphicon-ok').addClass('glyphicon-remove').parent().parent().removeClass('has-success').addClass('has-error');
	// $(that).parent().parent().find('small').show();
}

function banEnterSearch() {
    $('form input[type^=text]').bind('keypress', function (event) {
        if (event.keyCode == "13") {
            return false;
        }
    });
    $('form input[type^=number]').bind('keypress', function (event) {
        if (event.keyCode == "13") {
            return false;
        }
    });
} 

// 过滤HTML标签
function getText(msg) {
    var msg = msg.replace(/<\/?[^>]*>/g, ''); //去除HTML Tag
    msg = msg.replace(/[|]*\n/, '') //去除行尾空格
    msg = msg.replace(/&nbsp;/ig, ''); //nbsp
    return msg;
}

/**
*@author liushan
*@Description 打开新页面展示图片
*@Date 2019/8/22
*@Param 
*@return 
**/
function openNewWindowCheckImg() {
    var objs = document.getElementsByTagName("img");
    for(var i=0;i<objs.length;i++) {
        objs[i].onclick=function() {
            var newwin = window.open();
            var myimg = newwin.document.createElement("img");
            var tilte = newwin.document.createElement("title");
            myimg.src = this.src;
            tilte.text = "图片展示";
            myimg.style.cursor = "zoom-in";
            var width = myimg.style.width;
            myimg.onclick = function(){
                if(myimg.style.width == width){
                    myimg.style.width =  $(window).width()*1.1;
                    myimg.style.cursor = "zoom-out"
                } else {
                    myimg.style.width = width;
                    myimg.style.cursor = "zoom-in";
                }
            };
            newwin.document.head.appendChild(tilte);
            newwin.document.body.appendChild(myimg);
        };
        objs[i].style.cursor = "pointer";
    }
}


/**
 *@author liushan
 *@Description 创建一个临时表单提交
 *@Date 2019/7/3
 *@Param
 *@return
 **/
function createNewForm(url, paramData,urlType) {
    var form = document.createElement("form");
    form.action = url;
    form.target = "_self";
    form.method = urlType == undefined?"post":urlType;
    form.style.display = "none";
    for (var x in paramData) {
        var opt = document.createElement("input");
        opt.name = x;
        opt.type = "hidden";
        opt.value = paramData[x];
        form.appendChild(opt);
    }
    document.body.appendChild(form);

    form.submit();
    form.remove();
}

function toPageAndValue(titTag, id, curl) {
    if ($(".iframeGroup #iframe" + id).length == 1) {
        $(".iframeGroup iframe").css("display", "none");
        $(".contentNav .navTitle").removeClass("nav_active");
        $(".contentNav .navTitle[name='navTag_" + id + "']").addClass("nav_active");
        $(".iframeGroup iframe[id='iframe" + id + "']").remove();

        $(".iframeGroup").append("<iframe src='" + curl + "' width='100%' scrolling='yes' frameborder='no' id='iframe" + id + "'></iframe>");
    } else {
        if ($(".contentNav .navTitle").length <= 8) {
            $(".iframeGroup iframe").css("display", "none");
            $(".contentNav .navTitle").removeClass("nav_active");
            $(".contentNav").append("<div name='navTag_" + id + "' val='" + id + "' onclick='turnIframe(" + id + ",this)' class='navTitle nav_active'><span class='nav_tit '>" + titTag + "</span><span class='titIcon' onclick='closeIframe(" + id + ",this,event)'></span></div>");
            $(".contentNav div[name='navTag_" + id + "']").mousedown(function (event) {
                rightClick(id, event);
            });
            $(".iframeGroup").append("<iframe src='" + curl + "' height='100%' width='100%' frameborder='no' id='iframe" + id + "'></iframe>");
        } else {
            alert("请先尝试关闭一些页面，再打开新页面!");
        }
    }
}

//日期转换
function datFmt(date,fmt) { // author: meizz
    var o = {
        "M+": date.getMonth() + 1, // 月份
        "d+": date.getDate(), // 日
        "h+": date.getHours(), // 小时
        "m+": date.getMinutes(), // 分
        "s+": date.getSeconds(), // 秒
        "q+": Math.floor((date.getMonth() + 3) / 3), // 季度
        "S": date.getMilliseconds() // 毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

// 弹框操作
var modal= {
    selectRows:[],
    // bootstrapTable复选框操作
    checkSelectRows:function (id){
        $(id).on('uncheck.bs.table check.bs.table check-all.bs.table uncheck-all.bs.table',function(e,rows){
            var singleSelect = $(id).bootstrapTable('getOptions').singleSelect;
            var datas = $.isArray(rows) ? rows : [rows];        // 点击时获取选中的行或取消选中的行
            if(singleSelect == true ){// 单选
                modal.selectRows = [];
                modal.selectRows.push(datas[0]);
            } else { // 多选
                examine(e.type,datas);                              // 保存到全局 Array() 里
            }
        });
    },

    examine:function (type,datas){
        if(type.indexOf('uncheck')==-1){
            $.each(datas,function(i,v){
                // 添加时，判断一行或多行的 id 是否已经在数组里 不存则添加　
                modal.selectRows.indexOf(v.id.toString()) == -1 ? modal.selectRows.push(v) : -1;
            });
        } else {
            $.each(datas,function(i,v){
                modal.selectRows.splice(modal.selectRows.indexOf(v),1);    //删除取消选中行
            });
        }
    }
};

//节流
function throttle_setInter(s,fn){
  let timeOut = (s,fn)=>{
      setTimeout(()=>{
        fn();
        timeOut(s,fn);
      },s)
  }
  timeOut(s,fn);
}