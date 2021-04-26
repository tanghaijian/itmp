/**
 * Description: 缺陷自定义字段操作
 * Author:liushan
 * Date: 2018/12/10 下午 2:21
 */

var field = {

    // 添加自定义字段
    addField:function () {
        $.ajax({
            url: "/testManage/fieldTemplate/findFieldByDefect",
            method: "post",
            success: function (data) {
                if (!data.field) return;
                for (var i = 0; i < data.field.length; i++) {
                    field.appendDataType(data.field[i], 'canEditField', 'new');
                }
            }
        });
    },

    appendDataType:function (thisData, id, status) {
        var obj = $('<div class="def_col_18"></div>');
        switch (thisData.type) {
            case "int":
                obj.attr("dataType", "int");
                var labelName = $('<div class="def_col_8 font_right ">' + thisData.label + '：</div>');
                if (status == "new") {
                    var labelContent = $('<div class="def_col_28"><input maxlength="' + thisData.maxLength + '" fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" type="text" class="form-control" placeholder="请输入" value="' + thisData.defaultValue + '" /></div>');
                } else {
                    var labelContent = $('<div class="def_col_28"><input maxlength="' + thisData.maxLength + '" fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" type="text" class="form-control" placeholder="请输入" value="' + thisData.valueName + '" /></div>');
                }
                labelContent.children(" input ").bind("keyup", function () {
                    this.value = this.value.replace(/\D/gi, "");
                })
                obj.append(labelName, labelContent);
                break;
            case "float":
                obj.attr("dataType", "float")
                var labelName = $('<div class="def_col_8 font_right ">' + thisData.label + '：</div>');
                if (status == "new") {
                    var labelContent = $('<div class="def_col_28"><input fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" type="number" class="input_style" placeholder="请输入" value="' + thisData.defaultValue + '" /></div>');
                } else {
                    var labelContent = $('<div class="def_col_28"><input fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" type="number" class="input_style" placeholder="请输入" value="' + thisData.valueName + '" /></div>');
                }
                obj.append(labelName, labelContent);
                break;
            case "varchar":
                obj.attr("dataType", "varchar")
                var labelName = $('<div class="def_col_8 font_right ">' + thisData.label + '：</div>');
                if (status == "new") {
                    var labelContent = $('<div class="def_col_28"><input  maxlength="' + thisData.maxLength + '"  fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" type="text" class="form-control" placeholder="请输入" value="' + thisData.defaultValue + '" /></div>');
                } else {
                    var labelContent = $('<div class="def_col_28"><input  maxlength="' + thisData.maxLength + '"  fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" type="text" class="form-control" placeholder="请输入" value="' + thisData.valueName + '" /></div>');
                }
                obj.append(labelName, labelContent);
                break;
            case "data":
                obj.attr("dataType", "data")
                var labelName = $('<div class="def_col_8 font_right ">' + thisData.label + '：</div>');
                if (status == "new") {
                    var labelContent = $('<div class="def_col_28"><input fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" id="new' + thisData.fieldName + '" type="text" readonly class="input_style pointStyle" placeholder="请输入" value="' + thisData.defaultValue + '" /></div>');
                } else {
                    var labelContent = $('<div class="def_col_28"><input fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" id="edit' + thisData.fieldName + '" type="text" readonly class="input_style pointStyle" placeholder="请输入" value="' + thisData.valueName + '" /></div>');
                }
                obj.append(labelName, labelContent);
                break;
            case "timestamp":
                obj.attr("dataType", "timestamp")
                var labelName = $('<div class="def_col_8 font_right ">' + thisData.label + '：</div>');
                if (status == "new") {
                    var labelContent = $('<div class="def_col_28"><input fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" id="new' + thisData.fieldName + '" type="text" readonly id="new_' + thisData.fieldName + '" class="input_style pointStyle" placeholder="请输入" value="' + thisData.defaultValue + '" /></div>');
                } else {
                    var labelContent = $('<div class="def_col_28"><input fName="' + thisData.fieldName + '" requireded="' + thisData.required + '" id="edit' + thisData.fieldName + '" type="text" readonly id="new_' + thisData.fieldName + '" class="input_style pointStyle" placeholder="请输入" value="' + thisData.valueName + '" /></div>');
                }
                obj.append(labelName, labelContent);
                break;
            case "enum":
                obj.attr("dataType", "enum")
                var select = $('<select class="selectpicker" requireded="' + thisData.required + '" fName="' + thisData.fieldName + '"></select>')
                var options = JSON.parse(thisData.enums);
                select.append('<option value=""  fName="' + thisData.fieldName + '">请选择</option>');
                for (var i = 0; i < options.length; i++) {
                    if (options[i].enumStatus == 1) {
                        if (status == "edit" && options[i].value == thisData.valueName) {
                            select.append('<option value="' + options[i].value + '" selected >' + options[i].value + '</option>');
                        } else {
                            select.append('<option value="' + options[i].value + '">' + options[i].value + '</option>');
                        }

                    }
                }
                var labelName = $('<div class="def_col_8 font_right ">' + thisData.label + '：</div>');
                var labelContent = $('<div class="def_col_28"></div>');
                labelContent.append(select);
                obj.append(labelName, labelContent);
                break;
            default:
                break;
        }
        $("#" + id).append(obj);
        if (obj.attr("dataType") == "data") {
            laydate.render({
                elem: "#" + obj.find("input")[0].id,
                trigger: 'click',
                done: function (value, date, endDate) {
                    $("#" + obj.find("input")[0].id).next().css("display", "block");
                }
            });
        } else if (obj.attr("dataType") == "timestamp") {
            laydate.render({
                elem: "#" + obj.find("input")[0].id,
                trigger: 'click',
                type: 'datetime',
                format: 'yyyy-MM-dd HH:mm:ss',
                done: function (value, date, endDate) {
                    $("#" + obj.find("input")[0].id).next().css("display", "block");
                }
            });
        }
        $(".selectpicker").selectpicker('refresh');
    },

    getFieldData:function (id) {
        var data = {"field": []};
        for (var i = 0; i < $("#" + id + " > div").length; i++) {
            //int float varchar data timestamp enum
            var obj = {};
            var type = $("#" + id + " > div").eq(i).attr("dataType")
            if (type == "int" || type == "float" || type == "varchar" || type == "data" || type == "timestamp") {
                obj.fieldName = $("#" + id + " > div").eq(i).find("input").attr("fName");
                obj.required = $("#" + id + " > div").eq(i).find("input").attr("requireded");
                obj.valueName = $("#" + id + " > div").eq(i).find("input").val();
                obj.labelName = $("#" + id + " > div").eq(i).children("div").eq(0).text();
            } else if (type == "enum") {
                obj.fieldName = $("#" + id + " > div").eq(i).find("select").attr("fName");
                obj.required = $("#" + id + " > div").eq(i).find("select").attr("requireded");
                obj.valueName = $("#" + id + " > div").eq(i).find("select").val();
                obj.labelName = $("#" + id + " > div").eq(i).children("div").eq(0).text();
            }
            data.field.push(obj);
        }
        return data;
    }
};


