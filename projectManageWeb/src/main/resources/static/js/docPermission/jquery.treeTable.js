/***********
 * @depend 依赖
 * bootstrap.min.css
 * jquery.min.js
 * 
 * @html 容器
 *  <table id="tree_table" class="table table-bordered">
      <thead>
        <tr>
          <td class="center">菜单</td>
          <td class="center">排序</td>
        </tr>
      </thead>
      <tbody></tbody>
    </table>
 * 
 * @params 参数配置
  tree_table.tree_create_table(data,{
    id:'id',
    parentId:'parentId',//父id
    parentIds:'parentIds',//父id集合  例   一级"parentIds": ""  二级"parentIds": "299,"  三级"parentIds": "20,299,"
    level:'level',//级别
    isLeaf:'isLeaf',//是否有子节点
    isSelect:'isSelect',//是否选中
    leaf:'leaf',//是否为子节点
    name:'dirName',//名称
    padding_left:20,//左边距倍率,默认20
    readAuth:'readAuth',//读权限
    writeAuth:'writeAuth',//写权限
  });
 * 
 * @createDate 2019/11/15
 * ************/


var tree_table = (() => {
    function tree_create_table(data, tree_menu_params) {
      if (!data.length) return;
      $('#tree_table tbody').empty();
      data.map(function (val, idx) {//一级菜单
        if (!val[tree_menu_params['parentId']]) {
          tree_table.tree_menu_give('#tree_table tbody', val, val[tree_menu_params['level']], tree_menu_params);
        }
      })
      data.reverse().map(function (val, idx) {//其他菜单
        $('.tree_parent_menu').each(function (index, ele) {
          if (val[tree_menu_params['parentId']] && $(ele).attr('id') == val[tree_menu_params['parentId']]) {
            tree_table.tree_menu_give(ele, val, val[tree_menu_params['level']], tree_menu_params);
          }
        })
      })
      tree_table.tree_tab_handle();
    }
    
    function tree_tab_handle() {
      $('.tree_arrows').bind('click', function (e) {
        if (!$(e.target).hasClass('tree_arrows_bottom') && !$(e.target).hasClass('tree_arrows_right')) return;
        var parent_id = $(this).parent().parent().attr('id');
        if ($(this).hasClass('tree_arrows_right') && $(this).parent().parent().attr('ischild')) {
          $(this).removeClass('tree_arrows_right').addClass('tree_arrows_bottom');
          $(this).parent().parent().siblings().each(function (idx, val) {
            if ($(val).data('id') == parent_id) {
              $(val).removeClass('_hide');
            }
          })
        } else {
          $(this).removeClass('tree_arrows_bottom').addClass('tree_arrows_right');
          $(this).parent().parent().siblings().each(function (idx, val) {
            if ($(val).data('id') == parent_id) {
              $(`.tree_parent_${parent_id}`).addClass('_hide');
              $(`.tree_parent_${parent_id}`).find('.tree_arrows_bottom').removeClass('tree_arrows_bottom').addClass('tree_arrows_right');
            }
          })
        }
      })
    }

    function tree_menu_give(ele, val, level, tree_menu_params) {
      var _class = '';
      if (level == 1) { //一级菜单
        _class = 'tree_parent_menu';
      } else {
        var ids = val[tree_menu_params['parentIds']];
        var ids_str = '';
        ids.split(',').map(function (value) {
          if (value) {
            ids_str += ' tree_parent_' + value
          }
        })
        _class = `${ids_str} _hide tree_parent_menu`;
      }
      var _id = `${val[tree_menu_params['id']]}`;
      var _str = `
        <tr role="row" data-id="${val[tree_menu_params['parentId']]}" id="${_id}"  class="${_class}" level="${val.level}">
          <td class="_flex_c" style="padding-left:${level * tree_menu_params['padding_left']}px;">
            <div class="tree_arrows ${val[tree_menu_params['isLeaf']] ? '' : 'tree_arrows_right'}"></div>
            <div>
              <span class="tree_select_text">${val[tree_menu_params['name']]}</span>
            </div>
          </td>
          <td  style="">
            <input class="readAuth"  data-id="${_id}" type="checkbox" ${val[tree_menu_params['readAuth']] == 1 ? 'checked' : ''} />
            <span class="" id="">读</span>
            <input class="writeAuth" data-id="${_id}" type="checkbox" ${val[tree_menu_params['writeAuth']] == 1 ? 'checked' : ''} />
            <span class="" id="">写</span>
          </td>
        </tr>
      `;
      if (level == 1) {
        $(ele).append(_str);
      } else {
        $(ele).attr('isChild', true).after(_str);
      }
    }
    
    return {
      tree_create_table,
      tree_menu_give,
      tree_tab_handle,
    }
  })();