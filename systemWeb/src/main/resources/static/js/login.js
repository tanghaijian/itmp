function loginExt() {
    var userAccount = $("#userAccount").val();
    if (userAccount == "") {
        $(".txtlayer").show();
        $("#errspan").html("请输入用户");
        $("#userAccount").focus();
        return false;
    }
    var password = $("#password").val();
    if (password == "") {
        $(".txtlayer").show();
        $("#errspan").html("请输入密码");
        $("#password").focus();
        return false;
    }
    //ajax请求
    $.ajax({
        type: "POST",
        url: "/systemui/login",
        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
        dataType: "json",
        data: {
            userAccount: userAccount,
            password: password
        },
        success: function (msg) {
            if (msg.flag) {
                window.location.href = "/systemui/index";
            } else {
                alert(msg.data.message);
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            if (XMLHttpRequest.status != 999) {
                layer.alert("服务器异常，请联系管理员", {
                    icon: 2,
                    title: "提示信息"
                });
            }
        }
    });
}
$(document).keydown(function (event) {
    if (event.keyCode == 13) {
        loginExt();
    }
});

$(document).ready(function () {
    if ($(".iframeGroup", parent.document).length != 0) {
        window.parent.location.href = "/systemui/logout";
    }
})

