/**
 * Created by 朱颜辞镜花辞树 on 2018/10/9.
 */
// 时间延时参数
var timer = null;
var key = '';
var isSample = key == 'usaindep';
var currentFileId;
var currentFileScmUrl;
var currentFileCommitFile;
var currentFileCommitNumber;
var currentFileLastCommitNumber;
var currentFileToolId;
var currentFileGitProjectId;
var currentFileBeforeReNameFile;

//左边收起
function hideLeft() {
    if ($(".leftJob").hasClass("shrinkLeft")) {
        var rightLen = $("#commntBody").width() - $("#commentIconDiv").position().left - $("#commentIconDiv").width();
        if (rightLen < 235) {
            if ($("#commentIconDiv").position().left < 235) {
                $("#commentIconDiv").css("left", "0");
            } else {
                $("#commentIconDiv").css("right", rightLen);
                $("#commentIconDiv").css("left", "auto");
            }
        }
        $(".leftJob").removeClass("shrinkLeft");
    } else {
        $(".leftJob").addClass("shrinkLeft");
    }
    handle(500);
}
//右变收起
function hideRight() {
    if ($(".rightComment").hasClass("shrinkRight")) {
        var rightLen = $("#commntBody").width() - $("#commentIconDiv").position().left - $("#commentIconDiv").width();
        if (rightLen < 360) {
            if ($("#commentIconDiv").position().left < 360) {
                $("#commentIconDiv").css("left", "0");
            } else {
                $("#commentIconDiv").css("right", rightLen);
                $("#commentIconDiv").css("left", "auto");
            }
        }
        $(".rightComment").removeClass("shrinkRight");
        //展开后刷新评论区和文件评论总数
        if (currentFileId != null) {
            showFileComments(currentFileId);
            refreshFileCommentCounts(currentFileId);
        }
    } else {
        $(".rightComment").addClass("shrinkRight");
    }
    handle(500);
}

//点击对比的文件事件
function onFileClick(This, id, scmUrl, commitFile, commitNumber, lastCommitNumber) {
    currentFileId = id;
    currentFileScmUrl = scmUrl;
    currentFileCommitFile = commitFile;
    currentFileCommitNumber = commitNumber;
    currentFileLastCommitNumber = lastCommitNumber;
    $(".oneFile").removeClass("choiceBg");
    $(This).addClass("choiceBg");
    $("span[name='codeFileName']").html($(This).find("div.fileName").html());
    if ($('div.codeDiv').is(':visible') && !$('div.mergelyDiv').is(':visible')) {
        //展示代码
        showFileContent();
    } else if (!$('div.codeDiv').is(':visible') && $('div.mergelyDiv').is(':visible')) {
        //展示代码比对
        showFileComparison();
    }
}

//git文件点击
function onGitFileClick(This, id, toolId, gitProjectId, commitFile, beforeReNameFile, commitNumber, lastCommitNumber) {
    currentFileId = id;
    currentFileToolId = toolId;
    currentFileGitProjectId = gitProjectId;
    currentFileCommitFile = commitFile;
    currentFileBeforeReNameFile = beforeReNameFile;
    currentFileCommitNumber = commitNumber;
    currentFileLastCommitNumber = lastCommitNumber;
    $(".oneFile").removeClass("choiceBg");
    $(This).addClass("choiceBg");
    if (beforeReNameFile != "") {
        var beforeReNameFileArray = beforeReNameFile.split("/");
        $("span[name='codeFileName']").html($(This).find("div.fileName").html() + "(原文件名:" + beforeReNameFileArray[beforeReNameFileArray.length - 1] + ")");
    } else {
        $("span[name='codeFileName']").html($(This).find("div.fileName").html());
    }
    if ($('div.codeDiv').is(':visible') && !$('div.mergelyDiv').is(':visible')) {
        //展示代码
        showGitFileContent();
    } else if (!$('div.codeDiv').is(':visible') && $('div.mergelyDiv').is(':visible')) {
        //展示代码比对
        showGitFileComparison();
    }
}
//显示文件内容
function showFileContent() {
    $.ajax({
        type: "POST",
        url: "/devManage/codeReview/getFileContent",
        data: {
            scmUrl: currentFileScmUrl,
            commitFile: currentFileCommitFile,
            commitNumber: currentFileCommitNumber,
            fileContentCharset: $("#codeFileCharset").find("option:selected").val() //编码
        },
        dataType: "json",
        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
        beforeSend: function () {
            editor.setValue("");//清空代码框
        },
        success: function (msg) {
            if (msg.flag) {
                if (msg.data != null) {
                    editor.setValue(msg.data);//给代码框赋值
                }
            } else {
                layer.alert("获取文件内容异常", {
                    icon: 2,
                    title: "提示信息"
                });
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            if (XMLHttpRequest.status != 999) {
                layer.alert("获取文件内容失败", {
                    icon: 2,
                    title: "提示信息"
                });
            }
        }
    });
}

//显示git文件内容
function showGitFileContent() {
    $.ajax({
        type: "POST",
        url: "/devManage/codeReview/getGitFileContent",
        data: {
            toolId: currentFileToolId,
            gitRepositoryId: currentFileGitProjectId,
            commitNumber: currentFileCommitNumber,
            commitFile: currentFileCommitFile,
            fileContentCharset: $("#codeFileCharset").find("option:selected").val()//编码
        },
        dataType: "json",
        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
        beforeSend: function () {
            editor.setValue("");//清空代码框
        },
        success: function (msg) {
            if (msg.flag) {
                if (msg.data != null) {
                    editor.setValue(msg.data);//给代码框赋值
                }
            } else {
                layer.alert("获取文件内容异常", {
                    icon: 2,
                    title: "提示信息"
                });
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            if (XMLHttpRequest.status != 999) {
                layer.alert("获取文件内容失败", {
                    icon: 2,
                    title: "提示信息"
                });
            }
        }
    });
}
//文件对比
function showFileComparison() {
    $.ajax({
        type: "POST",
        url: "/devManage/codeReview/getCompareFileInfo",
        data: {
            scmUrl: currentFileScmUrl,
            commitFile: currentFileCommitFile,
            commitNumber: currentFileCommitNumber,
            lastCommitNumber: currentFileLastCommitNumber,
            fileContentCharset: $("#codeFileCharset").find("option:selected").val()
        },
        dataType: "json",
        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
        beforeSend: function () {
            $('#mergely').mergely('resize');
            $("#mergely").mergely('clear', 'lhs');
            $("#mergely").mergely('clear', 'rhs');
            //清空代码左右比对版本号
            $("div[name='compareFileNewVersion']").html("");
            $("div[name='compareFileOldVersion']").html("");
        },
        success: function (msg) {
            if (msg.flag) {
                if (msg.data.leftFile.content != undefined && msg.data.leftFile.content != null) {
                    //旧版
                    $("#mergely").mergely('lhs', msg.data.leftFile.content);
                }
                if (msg.data.rightFile.content != undefined && msg.data.rightFile.content != null) {
                    //新版
                    $("#mergely").mergely('rhs', msg.data.rightFile.content);
                }
                $("div[name='compareFileOldVersion']").html("版本:" + currentFileLastCommitNumber +
                    " | 作者:" + (msg.data.leftFile.author == undefined ? "未知" : msg.data.leftFile.author) +
                    " | 提交时间:" + ( msg.data.leftFile.commitDate == undefined ? "未知" : msg.data.leftFile.commitDate));
                $("div[name='compareFileNewVersion']").html("版本:" + currentFileCommitNumber +
                    " | 作者:" + (msg.data.rightFile.author == undefined ? "未知" : msg.data.rightFile.author) +
                    " | 提交时间:" + (msg.data.rightFile.commitDate == undefined ? "未知" : msg.data.rightFile.commitDate));
            } else {
                layer.alert("获取比对文件内容异常", {
                    icon: 2,
                    title: "提示信息"
                });
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            if (XMLHttpRequest.status != 999) {
                layer.alert("获取比对文件内容失败", {
                    icon: 2,
                    title: "提示信息"
                });
            }
        }
    });
}

//git文件对比
function showGitFileComparison() {
    $.ajax({
        type: "POST",
        url: "/devManage/codeReview/getCompareGitFileInfo",
        data: {
            toolId: currentFileToolId,
            gitRepositoryId: currentFileGitProjectId,
            commitNumber: currentFileCommitNumber,
            lastCommitNumber: currentFileLastCommitNumber,
            commitFile: currentFileCommitFile,
            beforeRenameFile: currentFileBeforeReNameFile,
            fileContentCharset: $("#codeFileCharset").find("option:selected").val()
        },
        dataType: "json",
        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
        beforeSend: function () {
            $('#mergely').mergely('resize');
            $("#mergely").mergely('clear', 'lhs');
            $("#mergely").mergely('clear', 'rhs');
            //清空代码左右比对版本号
            $("div[name='compareFileNewVersion']").html("");
            $("div[name='compareFileOldVersion']").html("");
        },
        success: function (msg) {
            if (msg.flag) {
                if (msg.data.leftFile.content != undefined && msg.data.leftFile.content != null) {
                    //旧版
                    $("#mergely").mergely('lhs', msg.data.leftFile.content);
                }
                if (msg.data.rightFile.content != undefined && msg.data.rightFile.content != null) {
                    //新版
                    $("#mergely").mergely('rhs', msg.data.rightFile.content);
                }
                var leftFileName;
                var rightFileName;
                var currentFileCommitFileArray = currentFileCommitFile.split("/");
                rightFileName = currentFileCommitFileArray[currentFileCommitFileArray.length - 1];
                if (currentFileBeforeReNameFile != "") {
                    var currentFileBeforeReNameFileArray = currentFileBeforeReNameFile.split("/");
                    leftFileName = currentFileBeforeReNameFileArray[currentFileBeforeReNameFileArray.length - 1];
                } else {
                    leftFileName = rightFileName;
                }
                //拼装上一个版本文件信息
                $("div[name='compareFileOldVersion']").html("文件名:" + leftFileName + " | 版本:" + msg.data.leftFile.revision +
                    " | 作者:" + (msg.data.leftFile.author == undefined ? "未知" : msg.data.leftFile.author) +
                    " | 提交时间:" + ( msg.data.leftFile.commitDate == undefined ? "未知" : msg.data.leftFile.commitDate));
               //拼装当前版本文件信息
                $("div[name='compareFileNewVersion']").html("文件名:" + rightFileName + " | 版本:" + msg.data.rightFile.revision +
                    " | 作者:" + (msg.data.rightFile.author == undefined ? "未知" : msg.data.rightFile.author) +
                    " | 提交时间:" + (msg.data.rightFile.commitDate == undefined ? "未知" : msg.data.rightFile.commitDate));
            } else {
                layer.alert("获取比对文件内容异常", {
                    icon: 2,
                    title: "提示信息"
                });
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            if (XMLHttpRequest.status != 999) {
                layer.alert("获取比对文件内容失败", {
                    icon: 2,
                    title: "提示信息"
                });
            }
        }
    });
}

//评审的意见
function showFileComments(fileId) {
    $.ajax({
        type: "POST",
        url: "/devManage/codeReview/getFileComments",
        data: {
            devTaskScmFileId: fileId,
            scmFileType: fileScmType
        },
        dataType: "json",
        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
        beforeSend: function () {
            $("#rightComment").find("div.contentBody").html("");
        },
        success: function (msg) {
            if (msg.flag) {
                var html = template("commentTemplate", {data: msg.data});
                $("#rightComment").find("div.contentBody").html(html);
            } else {
                layer.alert("获取评论信息异常", {
                    icon: 2,
                    title: "提示信息"
                });
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.alert("获取评论信息失败", {
                icon: 2,
                title: "提示信息"
            });
        }
    });
}

$(document).ready(function () {
    $("#commentIconDiv").Tdrag({
        scope: "#commntBody"
    });
    $("#mergely").mergely('cm', 'lhs').options.readOnly = true;
    $("#mergely").mergely('cm', 'rhs').options.readOnly = true;
    $("div.mergelyDiv").hide();
    if ($("div .fileList").find("div.oneFile").length > 0) {
        $("div .fileList").find("div.oneFile")[0].click();
    } else {
        $("div[name='contTopTitle']").children($("div")).hide();
    }
});

$(document).ajaxStart(function () {
    $("#loading").css('display', 'block')
}).ajaxStop(function () {
    $("#loading").css('display', 'none')
});

//防抖
function handle(sj) {
    clearTimeout(timer);
    timer = setTimeout(function () {
        $('#mergely').mergely('resize');
    }, sj);
}

//开始评审
function startOrEndFileCompare(This) {
    if ($('div.codeDiv').is(':visible') && !$('div.mergelyDiv').is(':visible')) {
        //收起代码，展示比对
        $(This).addClass("showContrast");
        $(This).html("收起比对");
        $("div.mergelyDiv").show();
        $("div.codeDiv").hide();
        if (fileScmType == 1) {
            showFileComparison();
        } else if (fileScmType == 2) {
            showGitFileComparison();
        }
    } else if (!$('div.codeDiv').is(':visible') && $('div.mergelyDiv').is(':visible')) {
        //收起比对，展示代码
        $(This).removeClass("showContrast");
        $(This).html("版本比对");
        $("div.mergelyDiv").hide();
        $("div.codeDiv").show();
        if (fileScmType == 1) {
            showFileContent();
        } else if (fileScmType == 2) {
            showGitFileContent();
        }
    }
}

function changeFileCharset() {
    if ($('div.codeDiv').is(':visible') && !$('div.mergelyDiv').is(':visible')) {
        //展示代码
        if (fileScmType == 1) {
            showFileContent();
        } else if (fileScmType == 2) {
            showGitFileContent();
        }
    } else if (!$('div.codeDiv').is(':visible') && $('div.mergelyDiv').is(':visible')) {
        //展示代码比对
        if (fileScmType == 1) {
            showFileComparison();
        } else if (fileScmType == 2) {
            showGitFileComparison();
        }
    }
}
//更新评审结果
function updateCodeReviewResult(devTaskId, codeReviewStatus) {
    $.ajax({
        type: "POST",
        url: "/devManage/codeReview/updateCodeReviewResult",
        data: {
            id: devTaskId,
            codeReviewStatus: codeReviewStatus
        },
        dataType: "json",
        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
        success: function (msg) {
            if (msg.flag) {
                layer.alert(msg.data, {
                    icon: 1,
                    title: "提示信息"
                }, function () {
                    window.location.href = "/devManageui/codeReviewControl/toCodeReViewManage";
                });
            } else {
                layer.alert(msg.data.message, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.alert("代码评审失败", {
                icon: 2,
                title: "提示信息"
            });
        }
    });
}

//刷新评审评论
function refreshFileCommentCounts(fileId) {
    $.ajax({
        type: "POST",
        url: "/devManage/codeReview/getFileCommentsCountByFileId",
        data: {
            devTaskScmFileId: fileId,
            scmFileType: fileScmType
        },
        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
        success: function (msg) {
            if (msg.flag) {
                $("div[fileId='" + fileId + "']").html(msg.data);
            } else {
                layer.alert("刷新评论总数异常", {
                    icon: 2,
                    title: "提示信息"
                });
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.alert("刷新评论总数失败", {
                icon: 2,
                title: "提示信息"
            });
        }
    });
}

function sendComment() {
    var comment = $.trim($("textarea.form-control.mySuggest").val());
    if (comment.length == 0) {
        layer.alert("评论消息不能为空！", {
            icon: 2,
            title: "提示信息"
        });
        return false;
    }
    if (currentFileId == null) {
        layer.alert("不存在需被评审文件！", {
            icon: 2,
            title: "提示信息"
        });
        return false;
    }
    $.ajax({
        type: "POST",
        url: "/devManage/codeReview/sendCodeReviewComment",
        data: {
            devTaskScmFileId: currentFileId,
            scmFileType: fileScmType,
            reviewComment: comment.replace(new RegExp("\n", "gm"), "\<\/br\>")
        },
        dataType: "json",
        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
        success: function (msg) {
            if (msg.flag) {
                showFileComments(currentFileId);
                refreshFileCommentCounts(currentFileId);
                $("textarea.form-control.mySuggest").val("");
            } else {
                layer.alert(msg.data.message, {
                    icon: 2,
                    title: "提示信息"
                });
            }
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            layer.alert("评审消息发送失败", {
                icon: 2,
                title: "提示信息"
            });
        }
    });
}

var editor = CodeMirror.fromTextArea(document.getElementById("code"), {
    mode: "text/x-java", //实现Java代码高亮
    lineNumbers: true,	//显示行号
    theme: "eclipse",	//设置主题
    styleActiveLine: true, // 当前行背景高亮
    //lineWrapping: true,	//代码折叠
    //foldGutter: true,
    //gutters: ["CodeMirror-linenumbers", "CodeMirror-foldgutter"],
    //matchBrackets: true,	//括号匹配
    //autofocus: true,
    readOnly: true        //只读
});
editor.setSize('100%', 'auto');     //设置代码框的长宽























