//var width = parseInt(d3.select("#table").attr("width"));
//var height = parseInt(d3.select("#table").attr("height"));

var svg = d3.select("#graph");


function DFS() {
        $.get("../json/DFS.json", { whichDataSet: crntDataSet, filename: "DFS.json" }, function (dataDFS, status) {
                if (status != "success") {
                        ///////////////////////////////////////////////////////
                        //                  提醒用户没有加载成功                      //
                        ///////////////////////////////////////////////////////
                        return console.log(status);
                }

                var click_id;

                var nodes = svg.selectAll("circle")
                               .on("dblclick", function (d, i) {
                                       click_id = d.id;
                               });

                // 重绘连接线
                var edges_line = svg.selectAll("line");

                for (var i = 0; i < edges_line[0].length; i++) {
                        edges_line[0][i].style.stroke = "#ccc";
                        for (var j = 0; j < dataDFS.length; j++) {
                                if (edges_line[0][i].__data__.source.id == dataDFS[j].source && edges_line[0][i].__data__.target.id == dataDFS[j].target) {
                                        //setInterval(function(){}, 10000);
                                        edges_line[0][i].style.stroke = "red";
                                        //edges_line[0][i].style.stroke = "red";
                                        //edges_line[0][i].innerHTML = "<animate attributeName= \"x1\" attributeType=\"auto\" \
                                        //              begin=\"0s\" dur=\"5s\" from= x1 to= x2 /> ";

                                        /*edges_line[0][i].enter().append("animate")
                                                        .attr("begin", "3s")
                                                                        .attr("dur", "3s");*/
                                }
                        }
                }
        });

}



function BFS() {

        $.get("../json/BFS.json", { whichDataSet: crntDataSet, filename: "BFS.json" }, function (dataBFS, status) {
                if (status != "success") {
                        ///////////////////////////////////////////////////////
                        //                  提醒用户没有加载成功                      //
                        ///////////////////////////////////////////////////////
                        return console.log(status);
                }

                // 重绘连接线
                var edges_line = svg.selectAll("line");

                for (var i = 0; i < edges_line[0].length; i++) {
                        edges_line[0][i].style.stroke = "#ccc";
                        for (var j = 0; j < dataBFS.length; j++) {
                                if (edges_line[0][i].__data__.source.id == dataBFS[j].source && edges_line[0][i].__data__.target.id == dataBFS[j].target) {
                                        edges_line[0][i].style.stroke = "red";
                                }
                        }
                }
        });

}

function Prim() {

        $.get("../json/Prim.json", { whichDataSet: crntDataSet, filename: "Prim.json" }, function (dataPrim, status) {
                if (status != "success") {
                        ///////////////////////////////////////////////////////
                        //                  提醒用户没有加载成功                      //
                        ///////////////////////////////////////////////////////
                        return console.log(status);
                }

                // 重绘连接线
                var edges_line = svg.selectAll("line");

                for (var i = 0; i < edges_line[0].length; i++) {
                        edges_line[0][i].style.stroke = "#ccc";
                        for (var j = 0; j < dataPrim.length; j++) {
                                if (edges_line[0][i].__data__.source.id == dataPrim[j].source && edges_line[0][i].__data__.target.id == dataPrim[j].target) {
                                        edges_line[0][i].style.stroke = "red";
                                }
                        }
                }
        });

}