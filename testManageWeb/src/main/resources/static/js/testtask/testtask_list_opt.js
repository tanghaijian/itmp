
$(function () {
  _select_box_show();
})
//更多操作弹窗
$(document).scroll(function (e) {
  $('._select_box_menu').hide();
});

//更多操作弹窗
function _select_box_show() {
  $(document).on('click', '._select_box_show', function (e) {
    $('._select_box_menu').hide();
    var top = $(this).offset().top - $(document).scrollTop();
    var left = $(this).offset().left - $(document).scrollLeft();
    $(this).next().css({
      position: 'fixed',
      top: top + $(this).height() + 'px',
      left: left - 15 + 'px',
    });
    _top = top + $(this).height();
    // scroll_flag = true;

    if ($(this).hasClass('active')) {
      $(this).next('._select_box_menu').hide();
      $(this).removeClass('active').children('span').removeClass('fa-angle-down').addClass('fa-angle-up');
      // scroll_flag = false;
    } else {
      $(this).addClass('active').children('span').removeClass('fa-angle-up').addClass('fa-angle-down');
      $(this).next('._select_box_menu').show();
    }
  })
  $(document).on('click', function (e) {
    if (!$(e.target).hasClass('_select_box_show') && !$(e.target).hasClass('_select_box_menu')) {
      $('._select_box_menu').hide();
      $('._select_box_menu').prev().removeClass('active').children('span').removeClass('fa-angle-down').addClass('fa-angle-up');
    }
  })
}