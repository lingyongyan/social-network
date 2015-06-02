// 当前功能下的状态变量
// 图是否就绪
Ready = false;
// 起点索引
var index0 = -1;
// 终点索引
var index1 = -1;
// 设置Cookie
function setCookie(cname, cvalue) {
        document.cookie = cname + "=" + cvalue + ";";
}
// 获取Cookie
function getCookie(cname) {
        var name = cname + "=";
        var ca = document.cookie.split(';');
        for (var i = 0; i < ca.length; i++) {
                var c = ca[i].trim();
                if (c.indexOf(name) == 0) return c.substring(name.length, c.length);
        }
        return "";
}



// 响应Random功能
// 选中一个点，在其他点上滑动，形成一条边则高亮该边
function Random() {
        if (!Ready) {
                // 提醒用户图还没加载完毕
                return;
        }
        index0 = -1; // 清除脏数据
        index1 = -1;
        var svg = d3.select("#graph");
        // 重新为结点绑定点击事件
        var nodes = svg.selectAll("circle")
                .on("click", function (d, i) {
                        index0 = d.id;
                })
                .on("mouseover", function (d, i) {
                        if (index0 < 0)
                                return;
                        index1 = d.id;
                        var edges = svg.selectAll("line")
                                .style("stroke", function (d, i) {
                                        if (d.source.id == index1 && d.target.id == index0 || d.source.id == index0 && d.target.id == index1)
                                                return "#F88";
                                        else
                                                return "#ccc";
                                })
                                .style("stroke-width", 2);

                })

}

// 响应Dijkstra功能
function Dijkstra() {
        if (!Ready) {
                // 提醒用户图还没加载完毕
                return;
        }

        index0 = -1; // 初始化起点
        var svg = d3.select("#graph");
        // 重新为结点绑定点击事件
        var nodes = svg.selectAll("circle");
        var edges = svg.selectAll("line");
        nodes.on("click", function (n0d, n0i) {
                index0 = n0d.id;

                // 向服务器发送包含选择点的BFS请求
                //$.post("IO.html", { node: 5 }, function () {
                //后台准备好BFS.json就可以画了
                //})
                // 从服务器取回结果
                $.get("../json/Dijkstra.json", { file: "Dijkstra.json", id: 0 }, function (data, status) {
                        console.log(status);
                        if (status != "success") {
                                // 修改状态栏提示没有成功
                                alert("请重新点击！");
                                return;
                        }
                        // 取回结果后，给结点注册OVER事件函数
                        nodes.on("mouseover", function (n1d, n1i) {
                                if (n1d.id == index0)
                                        return;
                                index1 = n1d.id;
                                var path = data[index1];
                                // 先把符合条件的边找出来放到一个集合中                                
                                edges.style("stroke", function (ed, ei) {
                                        for (var e in path) {
                                                if (ed.source.index == path[e].source && ed.target.index == path[e].target)
                                                        return "#F88";
                                        }
                                        return "#ccc"
                                });

                        })
                                // 注册划出函数
                                .on("mouseout", function () {
                                        edges.style("stroke", "#ccc")
                                })
                });

        });
}
// 响应Prim功能
function Prim() {
}
// 响应Bipartite功能
function Bipartite() {
}
// 响应Ford-Fulkerson功能
function FordFulkerson() {
        if (!Ready) {
                // 提醒用户图还没加载完毕
                return;
        }
        index0 = undefined; // 初始化选中的起点
        index1 = undefined; // 初始化选中的终点
        var svg = d3.select("#graph");
        // 重新为结点绑定点击事件
        var nodes = svg.selectAll("circle");
        var edges = svg.selectAll("line");
        var edges_text = svg.selectAll(".linetext");
        // 给结点绑定点击事件
        // 当选中两个不同结点时，
        // 向后台发送请求
        // 成功后回调处理边特效的函数
        nodes.on("mouseover", function () { })
                .on("mouseout", function () { })
                .on("click", function (d, i) {
                        // 选中第一个结点
                        // 提示用户选第二个点
                        if (!index0) {
                                index0 = d.id;
                                return;
                        }
                        // 说明选的和第一个一样
                        // 提示用户选第二个点
                        if (d.id == index0) {
                                return;
                        }
                        if (!index1) {
                                // 选中了一个和第一个不一样的点                       
                                // 向服务器发送包含选择点的BFS请求
                                //$.post("IO.html", { node: 5 }, function () {
                                //后台准备好BFS.json就可以画了
                                //})
                                index1 = d.id;
                                $.get("../json/FordFulkerson.json", function (data, status) {
                                        // 取回结果后，修改边上的文字
                                        var maxFlows = data["maxFlows"];
                                        edges_text.text(function (td) {
                                                for (var e in maxFlows) {
                                                        if (td.source.index == maxFlows[e].source && td.target.index == maxFlows[e].target)
                                                                return maxFlows[e].weight;
                                                }
                                                return td.weight;
                                        });
                                        edges.style("stroke", function (ed, ei) {
                                                for (var e in maxFlows) {
                                                        if (ed.source.index == maxFlows[e].source && ed.target.index == maxFlows[e].target)
                                                                return "#F88";
                                                }
                                                return "#ccc"
                                        });
                                });
                        }
                        // 选中了第三个有效点，将它设为起点，并清除第二个选点
                        index0 = d.id;
                        index1 = undefined;
                        // 提示用户选第二个点
                })
}
// 响应Bridge功能
function Bridge() {
}
// 响应出度功能
function OutDegree() {

}
// 响应入度功能
function InDegree() {

}

// 响应邻接矩阵功能
function Matrix() {

}
// 响应邻接链表功能
function List() {

}




