/**
 * Created by 64866 on 2017/6/20.
 */

// window.onload = function () {
//     "use strict";
//     getDataFromDB();
// };
// var getDataFromDB = function () {
//     $.ajax({
//         url:"",
//         type:"post",
//         dataType:"json",
//         error:function () {
//             alert("Error loading");
//         },
//         success:function (data) {
//             console.log(data);
//             radar(data);
//         }
//     });
// };
var data = [{"label":"语文","value":"85"},{"label":"数学","value":"10"},{"label":"应用","value":"40"},{"label":"语文","value":"60"},{"label":"高数","value":"70"}];
radar(data);
function radar(data) {
    "use strict";
    if(data.length===null || data.length === 0){
        return;
    }
    var radarData = {};
    radarData.labels = [];
    radarData.datasets = [];
    var tempData = {};
    tempData.data = [];
    tempData.fillColor = "rgba(220,220,220,0.2)";
    tempData.strokeColor =  "rgba(220,220,220,1)";
    tempData.pointColor = "rgba(220,220,220,1)";
    tempData.pointStrokeColor = "#fff";
    tempData.pointHighlightFill = "#fff";
    tempData.pointHighlightStroke = "rgba(220,220,220,1)";
    for(var i = 0; i < data.length;i++){
      radarData.labels.push(data[i].label);
     tempData.data.push(data[i].value);
    }
    radarData.datasets.push(tempData);
    console.log(radarData);
    window.myRadar = new Chart(document.getElementById("canvas").getContext("2d")).Radar(radarData, {
        responsive: true
    });
}


