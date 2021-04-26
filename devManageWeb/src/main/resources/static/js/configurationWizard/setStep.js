function extend(obj1, obj2) {
    for (var attr in obj2) {
        obj1[attr] = obj2[attr];
    }
}
function SetStep(arg) {
    this.body = document.body;
    this.opt = {
        skin: 1,
        show: false,
        content: '.stepCont',
        pageCont: '.pageCont',
        imgWidth: 0,
        stepContainerMar: 0,
        nextBtn: '.nextBtn',
        prevBtn: '.prevBtn',
        skipBtn: '.skipBtn',
        completeBtn: '.completeBtn',
        steps: ['1', '2', '3', '4', '5', '6', '7', '8'],
        descriptionHeader: ['', '', '', '', '', '', '', ''],
        description: ['', '', '', '', '', '', '', ''],
        stepCounts: 8,
        curStep: 1,
        animating: true,
        showBtn: true,
        clickAble: false,
        onLoad: function() {}
    };
    var options = $.extend({}, this.opt, arg);
    switch (options.skin) {
    case 1:
        this.init(arg);
        break;
    case 2:
        $(options.content).addClass("stepY");
        this.initY(arg);
        break;
    case 3:
        $(options.content).addClass("jiantou");
        this.initJanTou(arg);
        break;
    default:
        this.init(arg);
    }
} 
SetStep.prototype.init = function(arg) {
    var _that = this;
    extend(this.opt, arg);
    this.opt.stepCounts = this.opt.steps.length;
    this.content = $(this.opt.content);
    this.pageCont = this.content.find(this.opt.pageCont);
    var w_con = $(this.content).width();
    var w_li = (w_con - this.opt.stepContainerMar * 2) / this.opt.stepCounts / 2;
    var stepContainer = this.content.find('.ystep-container');
    this.stepContainer = stepContainer;
    var stepsHtml = $("<ul class='ystep-container-steps'></ul>");
    var stepDisc = "<li class='ystep-step ystep-step-undone'></li>";
    var stepP = $("<div class='ystep-progress'>" + "<p class='ystep-progress-bar'><span class='ystep-progress-highlight' style='width:0%'></span></p>" + "</div>");
    var stepButtonHtml = $("<div class='step-button'><button type='button' class='btn btn-default prevBtn' id='prevBtn' class='prevBtn'>上一步</button>" + "<button type='button' class='btn btn-primary nextBtn' id='nextBtn'>下一步</button>" + "<button type='button' class='btn btn-default skipBtn' id='skipBtn'>跳     过</button>" + "<button type='button' class='btn btn-primary completeBtn' id='completeBtn'>再次构建</button></div>");
    stepP.css('width', w_li * 2 * (this.opt.stepCounts - 1));
    stepP.css('left', w_li );
    stepP.find('.ystep-progress-bar').css('width', w_li * 2 * (this.opt.stepCounts - 1))
    for (var i = 0; i < this.opt.stepCounts; i++) {
        if (i == 0) {
            var _s = $(stepDisc).html('<div class="description">' + this.opt.description[i] + '</div><div class="stepIconDiv"><span class="stepIcon">' + this.opt.steps[i] + '</span></div><p class="descriptionHeader">' + this.opt.descriptionHeader[i] + '</p>').addClass('')
        } else {
            var _s = $(stepDisc).html('<div class="description">' + this.opt.description[i] + '</div><div class="stepIconDiv"><span class="stepIcon">' + this.opt.steps[i] + '</span></div><p class="descriptionHeader">' + this.opt.descriptionHeader[i] + '</p>')
        }
        stepsHtml.append(_s);
    }
    stepsHtml.find('li').css('width', w_li * 2)
    stepContainer.append(stepsHtml).append(stepP);
    stepContainer.css('left', (w_con - stepP.width() - this.opt.imgWidth - 10 - this.opt.stepContainerMar * 2) / 2)
    stepContainer.css('height', stepsHtml.height() - 25);
    this.content.css('overflow', 'hidden')
    this.setProgress(this.stepContainer, this.opt.curStep, this.opt.stepCounts)
    if (this.opt.showBtn) {
        this.content.append(stepButtonHtml)
        this.prevBtn = this.content.find(this.opt.prevBtn)
        this.nextBtn = this.content.find(this.opt.nextBtn)
        this.skipBtn = this.content.find(this.opt.skipBtn)
        this.completeBtn = this.content.find(this.opt.completeBtn)
        this.prevBtn.on('click', function() {
            if ($(_that).attr('disabled') || _that.opt.animating) {
                return false;
            } else {
                _that.opt.animating = true;
                _that.opt.curStep--;
                _that.setProgress(_that.stepContainer, _that.opt.curStep, _that.opt.stepCounts)
            }
        })
        this.nextBtn.on('click', function() {
            if ($(_that).attr('disabled') || _that.opt.animating) {
                return false;
            } else {
            	var flag=checkStep(_that.stepContainer, _that.opt.curStep, _that.opt.stepCounts);
            	if( flag ){
            		return ;
            	}
                _that.opt.animating = true;
                _that.opt.curStep++;
                _that.setProgress(_that.stepContainer, _that.opt.curStep, _that.opt.stepCounts) 
            }
        })
        this.skipBtn.on('click', function() { 
            if ($(_that).attr('disabled') || _that.opt.animating) {
                return false;
            } else {
            	getNextStepData(  _that.opt.curStep );
                _that.opt.animating = true;
                _that.opt.curStep++;
                _that.setProgress(_that.stepContainer, _that.opt.curStep, _that.opt.stepCounts)
            }
        })
        this.completeBtn.on('click', function() {  
             window.location.reload()
        })
    }
    if (this.opt.clickAble) {
        stepsHtml.find('li').on('click', function() {
            _that.opt.curStep = $(this).index() + 1;
            _that.setProgress(_that.stepContainer, _that.opt.curStep, _that.opt.stepCounts)
        })
    }
    $(window).resize(function() {
        var w_con = $(_that.content).width();
        var w_li = w_con / _that.opt.stepCounts / 2;
        stepP.css('width', w_li * 2 * (_that.opt.stepCounts - 1));
        stepP.find('.ystep-progress-bar').css('width', w_li * 2 * (_that.opt.stepCounts - 1)) 
        stepsHtml.find('li').css('width', w_li * 2) 
        stepContainer.css('left', (w_con - stepP.width() - _that.opt.imgWidth - 10 - _that.opt.stepContainerMar * 2) / 2)
    })
}  
SetStep.prototype.setProgress = function(n, curIndex, stepsLen) {
    var _that = this;
    var $steps = $(n).find("li");
    var $progress = $(n).find(".ystep-progress-highlight");
    if (1 <= curIndex && curIndex <= $steps.length) {
        var scale = "%";
        scale = Math.round((curIndex - 1) * 100 / ($steps.length - 1)) + scale;
        $progress.animate({
            width: scale
        }, {
            speed: 1000,
            done: function() {
                $steps.each(function(j, m) { 
                    var _$m = $(m);
                    var _j = j + 1;
                    if (_j < curIndex) {  
                    	switch (_j) {  
	                		case 2: 
	                			if( $( "#architectureType" ).val() == 1 ){
	                				if( globalData.childArr.length > 0 ){
		                				 _$m.attr("class", "ystep-step-done"); 
			                			 break;	
		                			}else{
		                				_$m.attr("class", "ystep-step-done-2"); 
			                			 break;
		                			}
	                			}else{
	                				 _$m.attr("class", "ystep-step-done"); 
		                			 break;
	                			}
	                			break;
	                		case 3:
	                			if( globalData.dataBaseArr.length > 0 ){
	                				 _$m.attr("class", "ystep-step-done"); 
		                			 break;	
	                			}else{
	                				_$m.attr("class", "ystep-step-done-2"); 
		                			 break;
	                			}   
	                			break;
	                		case 4:
	                			if( globalData.envType.length > 0 ){
	                				 _$m.attr("class", "ystep-step-done"); 
		                			 break;	
	                			}else{
	                				_$m.attr("class", "ystep-step-done-2"); 
		                			 break;
	                			}     
	                			break;
	                		case 5:
	                			if(  globalData.codeConfigStatus ){
	                				 _$m.attr("class", "ystep-step-done"); 
		                			 break;	
	                			}else{
	                				_$m.attr("class", "ystep-step-done-2"); 
		                			 break;
	                			}  
	                			break;
	                		case 6:
	                			if( $( "#createType" ).val() == 1 ){
	                				if(  globalData.autoDeployStatus ){
		                				 _$m.attr("class", "ystep-step-done"); 
			                			 break;	
		                			}else{
		                				_$m.attr("class", "ystep-step-done-2"); 
			                			 break;
		                			}
	                			} else{
	                				_$m.attr("class", "ystep-step-done"); 
		                			 break;	
	                			}
	                			break; 
	                		case 7:
	                			if( $( "#createType" ).val() == 1 ){
	                				if(  globalData.autoTestStatus ){
		                				 _$m.attr("class", "ystep-step-done"); 
			                			 break;	
		                			}else{
		                				_$m.attr("class", "ystep-step-done-2"); 
			                			 break;
		                			}
	                			} else{
	                				_$m.attr("class", "ystep-step-done"); 
		                			 break;	
	                			}
	                			break;
	                		default:
	                			 _$m.attr("class", "ystep-step-done");
	                			break;
                    	}  
                    } else if (_j === curIndex) {
                        _$m.attr("class", "ystep-step-active");
                    } else if (_j > curIndex) {
                        _$m.attr("class", "ystep-step-undone");
                    }
                })
                if (_that.opt.showBtn) {
                	 if (curIndex == 1) {                  
                     	_that.prevBtn.css( "display","none" )
                     	_that.nextBtn.css( "display","inline-block" )
                     	_that.skipBtn.css( "display","none" )
                     	_that.completeBtn.css( "display","none" )
                     	
                         _that.prevBtn.attr('disabled', 'true') 
                         _that.nextBtn.removeAttr('disabled')
                         _that.skipBtn.attr('disabled', 'true')
                         _that.completeBtn.attr('disabled', 'true')
                     } else if (curIndex == stepsLen) { 
                     	_that.prevBtn.css( "display","inline-block" )
                     	_that.nextBtn.css( "display","none" )
                     	_that.skipBtn.css( "display","none" )
                     	_that.completeBtn.css( "display","inline-block" )
                     	
                         _that.prevBtn.removeAttr('disabled')
                         _that.nextBtn.attr('disabled', 'true')
                         _that.skipBtn.attr('disabled', 'true')
                         _that.completeBtn.removeAttr('disabled')
                     }else if (1 < curIndex < stepsLen ) {
                     	_that.prevBtn.css( "display","inline-block" )
                     	_that.nextBtn.css( "display","inline-block" )
                     	_that.skipBtn.css( "display","inline-block" )
                     	_that.completeBtn.css( "display","none" )
                     	
                         _that.prevBtn.removeAttr('disabled')
                         _that.nextBtn.removeAttr('disabled')
                         _that.skipBtn.removeAttr('disabled')	
                         _that.completeBtn.attr('disabled', 'true')
                     }
                }
                _that.checkPage(_that.pageCont, _that.opt.curStep, _that.opt.stepCounts)
                _that.opt.animating = false;
            }
        });
    } else {
        return false;
    }
}
SetStep.prototype.checkPage = function(pageCont, curStep, steps) {
    for (var i = 1; i <= steps; i++) {
        if (i === curStep) {
            pageCont.find('#page' + i).css("display", "block");
        } else {
            pageCont.find('#page' + i).css("display", "none");
        }
    }
}









