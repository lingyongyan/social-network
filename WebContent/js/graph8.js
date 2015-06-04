// 当前功能下的状态变量
// 起点索引
var index0 = -1;
// 终点索引
var index1 = -1;

function resetG(resetNodes, resetEdges, resetWeight) {
        index0 = -1;
        index1 = -1;
        if (resetNodes) {
                gNodes.attr("r", function (d, i) {
                        return Math.log(d.group) + 10;
                })
                        .style("fill", function (d, i) {
                                return color(d.group);
                        })
                        .on("mouseover", null)
                        .on("mouseout", null)
                        .on("click", null);
        }
        if (resetEdges) {
                gEdges.style("stroke", "#ccc")
                        .style("stroke-width", function (d) {
                                return Math.log(d.weight)+2;
                        });
        }
        if (resetWeight) {
                if (gWeight) {
                        gEdgeTexts.text(function (d) { return d.weight; });
                }
        }


}



// 响应Random功能
// 选中一个点，在其他点上滑动，形成一条边则高亮该边
function Random() {
        if (!gReady) {
                ///////////////////////////////////////////////////////
                //            提醒用户主图区还未渲染完毕                  //
                ///////////////////////////////////////////////////////
                return;
        }
        resetG(true, true, true);
        // BFS功能初始化

        // 为结点绑定独有的事件响应函数
        gNodes.on("click", function (d, i) {
                        if (d3.event.defaultPrevented) return;
                        index0 = d.index;
                        $.get("../json/RanDom.json", { whichDataSet: crntDataSet, filename: "RanDom.json", id: index0 }, reDraw);
                });

        // 重新绑定动画函数

}
function reDraw(data, status) {
        if (status != "success") {
                ///////////////////////////////////////////////////////
                //                  提醒用户没有加载成功                      //
                ///////////////////////////////////////////////////////
                return console.log(status);
        }
        gEdges.style("stroke", function (d, i) {
                for (var p in data) {
                        if (d.source.index == data[p].source && d.target.index == data[p].target)
                                return "#d22";
                }
                return "#ccc";
        })
}

function BFS() {
        if (!gReady) {
                ///////////////////////////////////////////////////////
                //            提醒用户主图区还未渲染完毕                  //
                ///////////////////////////////////////////////////////
                return;
        }
        resetG(true, true, true);
        // BFS功能初始化

        // 为结点绑定独有的事件响应函数
        gNodes/*.on("mouseover", null)
                .on("mouseout", null)*/
                .on("click", function (d, i) {
                        if (d3.event.defaultPrevented) return;
                        index0 = d.index;
                        $.get("../json/BFS.json", { whichDataSet: crntDataSet, filename: "BFS.json", id: index0 }, reDraw);
                });

        // 重新绑定动画函数
}
function DFS() {
        if (!gReady) {
                ///////////////////////////////////////////////////////
                //            提醒用户主图区还未渲染完毕                  //
                ///////////////////////////////////////////////////////
                return;
        }
        // DFS功能初始化
        resetG(true, true, true);

        // 为结点绑定独有的事件响应函数
        gNodes/*.on("mouseover", null)
                .on("mouseout", null)*/
                .on("click", function (d, i) {
                        if (d3.event.defaultPrevented) return;
                        index0 = d.index;
                        $.get("../json/dfs.json", { whichDataSet: crntDataSet, filename: "DFS.json", id: index0 }, reDraw);
                });

}

// 响应Dijkstra功能
function Dijkstra() {
        if (!gReady) {
                // 提醒用户图还没加载完毕
                return;
        }

        resetG(true, true, true);

        // 重新为结点绑定点击事件
        gNodes/*.on("mouseover", null)
                .on("mouseout", null);
        gNodes*/.on("click", function (n0d, n0i) {
                if (d3.event.defaultPrevented) return;
                index0 = n0d.index;

                // 向服务器发送包含选择点的Dijkstra请求
                //$.post("IO.html", { node: 5 }, function () {
                //后台准备好Dijkstra.json就可以画了
                //})
                // 从服务器取回结果
                $.get("../json/Dijkstra.json", { whichDataSet: crntDataSet, filename: "Dijkstra.json", id: index0 }, function (data, status) {
                        //console.log(status);
                        if (status != "success") {
                                // 修改状态栏提示没有成功
                                alert("请重新选择！");
                                return;
                        }
                        // 取回结果后，给结点注册OVER和OUT事件函数
                        gNodes.on("mouseover", function (n1d, n1i) {
                                // 滑到自己身上不用响应
                                if (n1d.index == index0)
                                        return;
                                index1 = n1d.index;
                                var path = data[index1];
                                // 先把符合条件的边找出来放到一个集合中                                
                                gEdges.style("stroke", function (ed, ei) {
                                        for (var e in path) {
                                                if (ed.source.index == path[e].source && ed.target.index == path[e].target)
                                                        return "#d22";
                                        }
                                        return "#ccc"
                                });

                        })
                                // 注册划出函数
                                .on("mouseout", function () {
                                        gEdges.style("stroke", "#ccc")
                                })
                });

        });

}

function Prim() {
        if (!gReady) {
                ///////////////////////////////////////////////////////
                //            提醒用户主图区还未渲染完毕                  //
                ///////////////////////////////////////////////////////
                return;
        }
        // DFS功能初始化
        resetG(true, true, true);

        // 为结点绑定独有的事件响应函数
        gNodes/*.on("mouseover", null)
                .on("mouseout", null)*/
                .on("click", function (d, i) {
                        if (d3.event.defaultPrevented) return;
                        index0 = d.index;
                        $.get("../json/prim.json", { whichDataSet: crntDataSet, filename: "Prim.json", id: index0 }, reDraw);
                });
}
function reDrawP2P(data, status) {
        if (status != "success") {
                ///////////////////////////////////////////////////////
                //                  提醒用户没有加载成功                      //
                ///////////////////////////////////////////////////////
                return console.log(status);
        }
        // 边集高亮
        gEdges.style("stroke", "#ccc")
                .style("stroke-width", function (d) {
                        return Math.log(d.weight) + 2;
                });

        // 画两个结点集

}
// 响应Bipartite功能
function Bipartite() {
        if (!gReady) {
                ///////////////////////////////////////////////////////
                //            提醒用户主图区还未渲染完毕                  //
                ///////////////////////////////////////////////////////
                return;
        }
        // 二分图功能初始化
        resetG(true, true, true);

        // 为结点绑定独有的事件响应函数
        $.get("../json/p2p.json", { whichDataSet: crntDataSet, filename: "p2p.json", id: index0 }, reDrawP2P);


}
// 响应Ford-Fulkerson功能
function FordFulkerson() {
        if (!gReady) {
                // 提醒用户图还没加载完毕
                return;
        }
        resetG(true, true, true);
        // 重新为结点绑定点击事件
        // 给结点绑定点击事件
        // 当选中两个不同结点时，
        // 向后台发送请求
        // 成功后回调处理边特效的函数
        gNodes/*.on("mouseover",null)
                .on("mouseout", null)*/
                .on("click", function (d, i) {
                        if (d3.event.defaultPrevented) return;
                        // 选中第一个结点                        
                        if (index0<0) {
                                index0 = d.index;
                                // 提示用户选第二个点
                                return;
                        }
                        // 和前一个点一样          
                        if (d.index == index0) {
                                // 提示用户选第二个点
                                return;
                        }
                        if (index1 < 0) {
                                index1 = d.index;                               
                                // 选中了一个和第一个不一样的点                       
                                // 向服务器发送包含选择点的最大流请求
                                //$.post("IO.html", { node: 5 }, function () {
                                //后台准备好最大流.json就可以画了
                                //})
                                $.get("../json/FordFulkerson.json", function (data, status) {
                                        // 取回结果后，修改边上的文字
                                        gEdgeTexts.text(function (td) {
                                                for (var e in data) {
                                                        if (td.source.index == data[e].source && td.target.index == data[e].target)
                                                                return data[e].weight + "/" + td.weight;
                                                }
                                                return td.weight;
                                        });
                                        gEdges.style("stroke", function (ed, ei) {
                                                for (var e in data) {
                                                        if (ed.source.index == data[e].source && ed.target.index == data[e].target)
                                                                return "#F88";
                                                }
                                                return "#ccc"
                                        });
                                        index0 = -1;
                                        index1 = -1;

                                });
                        }
                        // 选中了第三个有效点，将它设为起点，并清除第二个选点
                        //index0 = d.index;
                        //index1 = -1;
                        // 提示用户选第二个点
                })
}
// 响应Bridge功能
function Bridge() {
        if (!gReady) {
                ///////////////////////////////////////////////////////
                //            提醒用户主图区还未渲染完毕                  //
                ///////////////////////////////////////////////////////
                return;
        }
        resetG(true, true, true);
        // 桥边检测功能初始化

        // 为结点绑定独有的事件响应函数
        $.get("../json/Bridge.json", { whichDataSet: crntDataSet, filename: "Bridge.json", id: index0 }, reDraw);
        // 重新绑定动画函数

}