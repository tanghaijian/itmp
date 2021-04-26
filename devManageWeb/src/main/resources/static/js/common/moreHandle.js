
$(function () {
  _select_box_show();
})
//更多操作弹窗
$(document).scroll(function (e) {
//  $('._select_box_menu').hide();
});

//更多操作弹窗
function _select_box_show() {
  $(document).on('click', '._select_box_show', function (e) {
    $('._select_box_menu').hide();
//    var top = $(this).offset().top - $(document).scrollTop();
//    var left = $(this).offset().left - $(document).scrollLeft();
//    $(this).next().css({
//      top: 0,
//      left: 0,
////      top: top + $(this).height() + 'px',
////      left: left  + 'px',
//    });
//    _top = top + $(this).height();
    if ($(this).hasClass('active')) {
      $(this).next().hide();
      $(this).removeClass('active');
    } else {
      $(this).addClass('active');
      $(this).next().show();
    }
  })
  $(document).on('click', function (e) {
	if (!$($(e.target)[0].offsetParent).hasClass('_select_box_menu') || $(e.target).hasClass('_select_box_show')){
      $('._select_box_menu').hide();
      $('._select_box_menu').prev().removeClass('active');
    }
  })
  
  $(document).on('click', '._select_box_show2', function (e) {
    $('._select_box_menu2').hide();
    if ($(this).hasClass('active')) {
        $(this).next().hide();
        $(this).removeClass('active');
      } else {
        $(this).addClass('active');
        $(this).next().show();
      }
  })
  $(document).on('click', function (e) {
	if (!$($(e.target)[0].offsetParent).hasClass('_select_box_menu2') && !$(e.target).hasClass('treeSearchInput') || $(e.target).hasClass('_select_box_show2') && !$(e.target).hasClass('treeSearchInput')){
      $('._select_box_menu2').hide();
      $('._select_box_menu2').prev().removeClass('active');
    }
  })
}