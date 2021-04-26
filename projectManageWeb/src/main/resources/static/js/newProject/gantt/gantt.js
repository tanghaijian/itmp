/********
 * 2019/11/14
 * niexingquan
 * *********/

var ge;
$(function () {
 var canWrite = true; //this is the default for test purposes

 // here starts gantt initialization
 ge = new GanttMaster();
 ge.set100OnClose = true;

 ge.shrinkParent = true;

 ge.init($("#workSpace"));
 loadI18n(); //overwrite with localized ones

 //in order to force compute the best-fitting zoom level
 delete ge.gantt.zoom;

 var project = loadFromLocalStorage();

//  if (!project.canWrite)
//    $(".ganttButtonBar button.requireWrite").attr("disabled", "true");

 ge.loadProject(project);
 ge.checkpoint(); //empty the undo stack
 
//  initializeHistoryManagement(ge.tasks[0].id);
});

function getDemoProject() {
   
}

function loadGanttFromServer(taskId, callback) {

  //this is a simulation: load data from the local storage if you have already played with the demo or a textarea with starting demo data
  // var ret = loadFromLocalStorage();

  //this is the real implementation
  /*
  //var taskId = $("#taskSelector").val();
  var prof = new Profiler("loadServerSide");
  prof.reset();
 
  $.getJSON("ganttAjaxController.jsp", {CM:"LOADPROJECT",taskId:taskId}, function(response) {
    //console.debug(response);
    if (response.ok) {
      prof.stop();
 
      ge.loadProject(response.project);
      ge.checkpoint(); //empty the undo stack
 
      if (typeof(callback)=="function") {
        callback(response);
      }
    } else {
      jsonErrorHandling(response);
    }
  });
  */

  return ret;
}

function saveGanttOnServer() {

  //this is a simulation: save data to the local storage or to the textarea
  saveInLocalStorage();
  /*
  var prj = ge.saveProject();
 
  delete prj.resources;
  delete prj.roles;
 
  var prof = new Profiler("saveServerSide");
  prof.reset();
 
  if (ge.deletedTaskIds.length>0) {
    if (!confirm("TASK_THAT_WILL_BE_REMOVED\n"+ge.deletedTaskIds.length)) {
      return;
    }
  }
 
  $.ajax("ganttAjaxController.jsp", {
    dataType:"json",
    data: {CM:"SVPROJECT",prj:JSON.stringify(prj)},
    type:"POST",
 
    success: function(response) {
      if (response.ok) {
        prof.stop();
        if (response.project) {
          ge.loadProject(response.project); //must reload as "tmp_" ids are now the good ones
        } else {
          ge.reset();
        }
      } else {
        var errMsg="Errors saving project\n";
        if (response.message) {
          errMsg=errMsg+response.message+"\n";
        }
 
        if (response.errorMessages.length) {
          errMsg += response.errorMessages.join("\n");
        }
 
        alert(errMsg);
      }
    }
 
  });
  */
}

function newProject() {
  clearGantt();
}

function clearGantt() {
  ge.reset();
}

//-------------------------------------------  Get project file as JSON (used for migrate project from gantt to Teamwork) ------------------------------------------------------
function getFile() {
  $("#gimBaPrj").val(JSON.stringify(ge.saveProject()));
  $("#gimmeBack").submit();
  $("#gimBaPrj").val("");

  /*  var uriContent = "data:text/html;charset=utf-8," + encodeURIComponent(JSON.stringify(prj));
   neww=window.open(uriContent,"dl");*/
}

function saveInLocalStorage() {
  var prj = ge.saveProject();
  if (localStorage) {
    localStorage.setObject("teamworkGantDemo", prj);
  }
}


function initializeHistoryManagement(taskId) {

  //retrieve from server the list of history points in millisecond that represent the instant when the data has been recorded
  //response: {ok:true, historyPoints: [1498168800000, 1498600800000, 1498687200000, 1501538400000, …]}
  $.getJSON(contextPath + "/applications/teamwork/task/taskAjaxController.jsp", { CM: "GETGANTTHISTPOINTS", OBJID: taskId }, function (response) {

    //if there are history points
    if (response.ok == true && response.historyPoints && response.historyPoints.length > 0) {

      //add show slider button on button bar
      var histBtn = $("<button>").addClass("button textual icon lreq30 lreqLabel").attr("title", "SHOW_HISTORY").append("<span class=\"teamworkIcon\">&#x60;</span>");

      //clicking it
      histBtn.click(function () {
        var el = $(this);
        var ganttButtons = $(".ganttButtonBar .buttons");

        //is it already on?
        if (!ge.element.is(".historyOn")) {
          ge.element.addClass("historyOn");
          ganttButtons.find(".requireCanWrite").hide();

          //load the history points from server again
          showSavingMessage();
          $.getJSON(contextPath + "/applications/teamwork/task/taskAjaxController.jsp", { CM: "GETGANTTHISTPOINTS", OBJID: ge.tasks[0].id }, function (response) {
            jsonResponseHandling(response);
            hideSavingMessage();
            if (response.ok == true) {
              var dh = response.historyPoints;
              if (dh && dh.length > 0) {
                //si crea il div per lo slider
                var sliderDiv = $("<div>").prop("id", "slider").addClass("lreq30 lreqHide").css({ "display": "inline-block", "width": "500px" });
                ganttButtons.append(sliderDiv);

                var minVal = 0;
                var maxVal = dh.length - 1;

                $("#slider").show().mbSlider({
                  rangeColor: '#2f97c6',
                  minVal: minVal,
                  maxVal: maxVal,
                  startAt: maxVal,
                  showVal: false,
                  grid: 1,
                  formatValue: function (val) {
                    return new Date(dh[val]).format();
                  },
                  onSlideLoad: function (obj) {
                    this.onStop(obj);

                  },
                  onStart: function (obj) { },
                  onStop: function (obj) {
                    var val = $(obj).mbgetVal();
                    showSavingMessage();
                    /**
                     * load the data history for that milliseconf from server
                     * response in this format {ok: true, baselines: {...}}
                     *
                     * baselines: {61707: {duration:1,endDate:1550271599998,id:61707,progress:40,startDate:1550185200000,status:"STATUS_WAITING",taskId:"3055"},
                     *            {taskId:{duration:in days,endDate:in millis,id:history record id,progress:in percent,startDate:in millis,status:task status,taskId:"3055"}....}}                     */

                    $.getJSON(contextPath + "/applications/teamwork/task/taskAjaxController.jsp", { CM: "GETGANTTHISTORYAT", OBJID: ge.tasks[0].id, millis: dh[val] }, function (response) {
                      jsonResponseHandling(response);
                      hideSavingMessage();
                      if (response.ok) {
                        ge.baselines = response.baselines;
                        ge.showBaselines = true;
                        ge.baselineMillis = dh[val];
                        ge.redraw();
                      }
                    })

                  },
                  onSlide: function (obj) {
                    clearTimeout(obj.renderHistory);
                    var self = this;
                    obj.renderHistory = setTimeout(function () {
                      self.onStop(obj);
                    }, 200)

                  }
                });
              }
            }
          });


          // closing the history
        } else {
          //remove the slider
          $("#slider").remove();
          ge.element.removeClass("historyOn");
          if (ge.permissions.canWrite)
            ganttButtons.find(".requireCanWrite").show();

          ge.showBaselines = false;
          ge.baselineMillis = undefined;
          ge.redraw();
        }

      });
      $("#saveGanttButton").before(histBtn);
    }
  })
}

function showBaselineInfo(event, element) {
  //alert(element.attr("data-label"));
  $(element).showBalloon(event, $(element).attr("data-label"));
  ge.splitter.secondBox.one("scroll", function () {
    $(element).hideBalloon();
  })
}
