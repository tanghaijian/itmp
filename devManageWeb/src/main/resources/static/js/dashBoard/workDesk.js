/**
 * Created by 朱颜辞镜花辞树 on 2018/9/18.
 */
var myChart = echarts.init( document.getElementById("container") );

$(document).ready(function() {
    $("#details").setWorkDesk();//调用下面js中的方法    
    (function (){
		window.addEventListener("resize", function (){  
		    this.myChart.resize();   
		});
	})()
});

jQuery.fn.extend({
	setWorkDesk:function(type){
    	var target = this;
        $.ajax({
            type:"POST",
            url:"/devManage/dashBoard/getUserDesk",
            dataType:"json",
            success:function(data){
            	var taskList=data["taskList"]; 
            	var sjrList=data["sjrList"];
            	var myProject=data["myProject"];
            	var requirementList=data["requirementList"];
            	var taskSystemList=data["taskSystemList"];
            	
            	$("#taskCount").html(taskList.length);
            	var wait=0;	//待实施
            	var develop=0;	//实施中
            	for(var j=0;j<taskList.length;j++){
					if(taskList[j].devTaskStatus == 1){
						wait=wait+1;
					}else if(taskList[j].devTaskStatus == 2){
						develop=develop+1;
					}
            	}
      			$("#waitImp").html(wait);
      			$("#develop").html(develop);
      			//日志信息
      			for(var i=0;i<sjrList.length;i++){
      				if(i==sjrList.length-1){
      					var str='<div class="logDiv lastLog">';
      				}else{
      					var str='<div class="logDiv">';
      				}
      				str+='<div class="logDiv_title fontWeihgt"><span class="orderNum"></span>'+
    	                 '<span class="fontWeight">'+sjrList[i].systemName+"&nbsp;&nbsp;"+getEnvironmentType(sjrList[i].environmentType)
       					+"&nbsp;&nbsp;"+getBuildStatus(sjrList[i].buildStatus)+'</span></div><div class="logDiv_cont"><div class="logDiv_contBorder">'
    	                +timestampToTime(sjrList[i].startDate) +'~'+ timestampToTime(sjrList[i].endDate)+'<br/><br/></div></div></div>';
      				$("#rightBlock3").append( str );
      			}
      			//我的项目
      			var haveInHand=0;	//进行中
      			var complete=0;		//已结束
      			var itOperation=0;	//it运维类
      			var itNew=0;		//it新建类
      			for(var i=0;i<myProject.length;i++){
      				if(myProject[i].projectStatus<=3){
      					haveInHand=haveInHand+1;
      				}else if(myProject[i].projectStatus==4){
      					complete=complete+1;
      				}
      				if(myProject[i].projectType==1){
      					itOperation=itOperation+1;
      				}else if(myProject[i].projectType==2){
      					itNew=itNew+1;
      				}
      			}
      			$("#toWork").html(haveInHand);
      			$("#complete").html(complete);
      			$("#itOperation").html(itOperation);
      			$("#itNew").html(itNew);
      			
      			//我的需求
      			var inImplementation=0;			//实施中
      			var implementationComplete=0;	//已完成
      			var onLine=0;					//已上线
      			var waitRelease=0;				//待发布
      			for(var i=0;i<requirementList.length;i++){
      				if(requirementList[i].requirementStatus=="REQ_DEVELOP"){//实施中
      					inImplementation=inImplementation+1;
      				}else if(requirementList[i].requirementStatus=="REQ_DEVELOPCOM"){//已完成
      					implementationComplete=implementationComplete+1;
      				}else if(requirementList[i].requirementStatus=="REQ_END"){//已上线
      					onLine=onLine+1;
      				}else if(requirementList[i].requirementStatus=="REQ_PREPUBLISH"){//待发布
      					waitRelease=waitRelease+1;
      				}
      			}
      			$("#inImplementation").html(inImplementation);
      			$("#implementationComplete").html(implementationComplete);
      			$("#onLine").html(onLine);
      			$("#waitRelease").html(waitRelease);
      			
      			
      			var data1=[];
      			var data2=[];
      			for(var j=0;j<taskSystemList.length;j++){
      				var obj1 = {};
      				var obj2 = {};
      				var color = {};
      				color.color=getColor(j);
      				obj1.icon='circle';
      				obj1.name = taskSystemList[j].systemName;
      				obj1.value= taskSystemList[j].systemCount;
      				//obj1.itemStyle= color;
      				     				
      				obj2.name = taskSystemList[j].systemName;
      				obj2.value = taskSystemList[j].systemCount;
      				obj2.itemStyle=color;
      				data1.push(obj1);
      				data2.push(obj2);
            	}
      			console.log(data1);
      			console.log(data2);
      			/*echarts环形图*/
      			
      			 
      			var app = {};
      			option = null;
      			app.title = '环形图';
      			option = {
      				    tooltip: {
      				        trigger: 'item',
      				        show:false,
      				        formatter: "{a} <br/>{b}: {c} ({d}%)"
      				    },
      				    legend: {
      				        orient: 'vertical',
      				        top:'middle',
      				        x: '50%',
      				        data:data1,
      				        silent:false,
      				        selectedMode:false,
      				        padding: [
      				        	0,  // 上
	      				        1, 	// 右
	      				        0,  // 下
	      				        1, 	// 左
	      				    ],
	      				    itemGap:0,
	      				    itemWidth:8,
	      				    itemHeight:8,
      				        formatter: function(name) {//这里的name表示图例中的元素名称
      				    	 	for(var i=0;i<data1.length;i++) {//对图例数组进行遍历
	      				    		if (data1[i].name == name) {
	      				    			for(var j=data1[i].name.length;j<13;j++){
	      				    				name=name+" ";
	      				    			}
	      				    			var str1 = name + data1[i].value;
	      				    		}    
      				    		}
          				    	return str1;
      				        }
      				    },
      				    series: [
      				        {
      				            name:'系统分布',
      				            x:'left',
      				            type:'pie',
      				            radius: ['74%', '90%'],
      				            legendHoverLink:false,
      				            avoidLabelOverlap: false,
      				            hoverAnimation:false,
      				            silent:false,
      				            center :['25%','50%'],
      				            label: {
      				                normal: {
      				                	show: true,
      				                	position: 'center',
      				                	color:'#515151',
      				                	fontWeight:'bold',
      				                	fontSize:'20',
      				                	formatter: [
        				                      '{a|'+taskList.length+'}',
        				                      '{b|总任务数}'
        				                ].join('\n'),           				            	
           				            	rich: {
				      				        a: {
				      				        	color:'#515151',
				      				        	fontWeight:'400',
				      				        	fontSize:'32'
				      				        },
				      				        b: {
				      				        	color:'#949494',				      				        	
				      				        	fontSize:'14'
				      				        },
      				        
           				            	}
      				                },
      				                emphasis: {
      				                    show: false,
      				                    
      				                }
      				            },
      				            labelLine: {
      				                normal: {
      				                    show: false
      				                }
      				            },
      				            data:data2
      				        }
      				    ]
      				}; //option
      				if (option && typeof option === "object") {
      				    myChart.setOption(option, true);
      				}
           			      
            }//success
        });//$.ajax
    }//setRoleList:function(type)
});//jQuery.fn.extend

function getEnvironmentType (environmentType) {
	var str=""
    if(environmentType==1){
    	str="DEV环境";
    }else if(environmentType==2){
    	str="SIT环境";
    }else if(environmentType==3){
    	str="UAT环境";
    }else{
    	str="PRD环境";
    }
	return str;
}

function getBuildStatus(buildStatus) {
	var str=""
    if(buildStatus==1){
    	str="构建中 | 代码扫描中 ";
    }else if(buildStatus==2){
    	str="构建成功 | 代码扫描成功 ";
    }else if(buildStatus==3){
    	str="构建失败 | 代码扫描失败 ";
    }
	return str;
}

function timestampToTime(timestamp) {
	var str="";
	if(timestamp!=null){
	    var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
	    var Y = date.getFullYear() + '-';
	    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
	    var D = date.getDate() + '&nbsp;';
	    var h = date.getHours() + ':';
	    var m = date.getMinutes() + ':';
	    var s = date.getSeconds();
	    str=Y+M+D+h+m+s;
	}
    return str;
}

function getColor(j){
	var color="";
	if(j==0){
		color="#49A9EE";
	}else if(j==1){
		color="#98D87D";
	}else if(j==2){
		color="#FFD86E";
	}else if(j==3){
		color="#F3857B";
	}else if(j==4){
		color="#8996E6";
	}else if(j==5){
		color="#FF0000";
	}else if(j==6){
		color="#FF9900";
	}else if(j==7){
		color="#FF00FF";
	}else if(j==8){
		color="#0000FF";
	}else if(j==9){
		color="#00FFFF";
	}else if(j==10){
		color="#CCCCFF";
	}	
	return color;
}