// 当前功能下的状态变量
// 图是否就绪
graphReady = false;
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
// 自调函数
(function () {        

        var width = parseInt(d3.select("#graph").attr("width"));
        var height = parseInt(d3.select("#graph").attr("height"));
        var img_w = 10;
        var img_h = 10;
        // 0 选择id为graph的SVG元素
        var svg = d3.select("#graph");

        // 1 读入JSON文件
        d3.json("../json/graph.json", function (error, root) {
                if (error) {
                        return console.log(error);
                }

                // 2 定义力学图的布局
                var force = d3.layout.force()
                        .nodes(root.nodes)
                        .links(root.edges)
                        .size([width, height])
                        .linkDistance(200)
                        .charge(-800)
                        .start();

                // 根据有向无向绘制连线
                // 3.1 绘制连接线
                var edges_line = svg.selectAll("line")
                        .data(root.edges)
                        .enter()
                        .append("line")
                        .style("stroke", "#ccc")
                        .style("stroke-width", 2);

                // 根据有权无权绘制线的权重
                // 3.2 绘制直线上的文字
                var edges_text;
                if (root.weight) {
                        edges_text = svg.selectAll(".linetext")
                                .data(root.edges)
                                .enter()
                                .append("text")
                                .attr("class", "linetext")
                                .text(function (d) {
                                        return d.weight;
                                });
                }

                var drag = force.drag()
                        .on("dragstart", function (d, i) {
                                d.fixed = true;    //拖拽开始后设定被拖拽对象为固定
                        });

                // 4 绘制结点
                var color = d3.scale.category20();
                var nodes = svg.selectAll("circle")
                        .data(root.nodes)
                        .enter()
                        .append("circle")
                        .attr("r", 15)
                        .style("fill", function (d, i) {
                                return color(i);
                        })
                        .on("dblclick", function (d, i) {
                                d.fixed = false;
                        })
                        .call(force.drag);

                var text_dx = -7.5;
                var text_dy = 15;
                // 5 绘制结点标签
                var nodes_text = svg.selectAll(".nodetext")
                                    .data(root.nodes)
                                    .enter()
                                    .append("text")
                                    .attr("class", "nodetext")
                                    .attr("dx", text_dx)
                                    .attr("dy", text_dy)
                                    .text(function (d) {
                                            return d.id;
                                    });


                force.on("tick", function (d, i) {
                        //限制结点的边界
                        root.nodes.forEach(function (d, i) {
                                d.x = d.x - img_w / 2 < 0 ? img_w / 2 : d.x;
                                d.x = d.x + img_w / 2 > width ? width - img_w / 2 : d.x;
                                d.y = d.y - img_h / 2 < 0 ? img_h / 2 : d.y;
                                d.y = d.y + img_h / 2 + text_dy > height ? height - img_h / 2 - text_dy : d.y;
                        });

                        //更新连接线的位置
                        edges_line.attr("x1", function (d) { return d.source.x; });
                        edges_line.attr("y1", function (d) { return d.source.y; });
                        edges_line.attr("x2", function (d) { return d.target.x; });
                        edges_line.attr("y2", function (d) { return d.target.y; });
                        if (root.weight) {
                                //更新连接线上文字的位置
                                edges_text.attr("x", function (d) { return (d.source.x + d.target.x) / 2; });
                                edges_text.attr("y", function (d) { return (d.source.y + d.target.y) / 2; });
                        }

                        //更新结点和文字
                        nodes.attr("cx", function (d) { return d.x; });
                        nodes.attr("cy", function (d) { return d.y; });
                        nodes_text.attr("x", function (d) { return d.x });
                        nodes_text.attr("y", function (d) { return d.y + img_w / 2; });
                });
                
                graphReady = true;// 图已就绪
        });


})()
                        //.on("mouseover", function (d, i) {
                        //        //显示结点上的文字
                        //        nodes_text.style("fill-opacity", function (node) {
                        //                if (node.name === d.name)
                        //                        return 1;
                        //        })
                        //})
                        //.on("mouseout", function (d, i) {
                        //        //隐去结点上的文字
                        //        nodes_text.style("fill-opacity", function (node) {
                        //                if (node.name === d.name)
                        //                        return 0;
                        //        })
                        //})
                        //.on("click", function (d, i) {
                        //        index0 = d.id;
                        //})
// 响应Random功能
// 选中一个点，在其他点上滑动，形成一条边则高亮该边
function Random()
{
        if (!graphReady)
                return;
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
// 响应BFS功能
function BFS() {        
        if (!graphReady)
                return;
        index0 = -1; // 清除脏数据
        var svg = d3.select("#graph");
        // 重新为结点绑定点击事件
        var nodes = svg.selectAll("circle")
                .on("click", function (d, i) {
                        index0 = d.id;
                        var edges = svg.selectAll("line")
                                .style("stroke", function (d, i) {
                                        if (d.source.id == index0 || d.target.id == index0)
                                                return "#F88";
                                        else
                                                return "#ccc";
                                })
                                .style("stroke-width", 2);
                })
}
// 响应DFS功能
function DFS() {
        if (!graphReady)
                return;
        var svg = d3.select("#graph");
        svg.selectAll("line")
                .style("stroke", function (d, i) {
                        if (d.source.id == index1 && d.target.id == index0 || d.source.id == index0 && d.target.id == index1)
                                return "#F88"
                        else
                                return "#ccc";
                })
                .style("stroke-width", 2);
}

// 响应Dijkstra功能
function Dijkstra() {
}
// 响应Prim功能
function Prim() {
}
// 响应Bipartite功能
function Bipartite() {
}
// 响应Ford-Fulkerson功能
function FordFulkerson() {
        if (!graphReady)
                return;
        index0 = -1; // 初始化选中的起点
        index1 = -1; // 初始化选中的终点
        var svg = d3.select("#graph");
        // 重新为结点绑定点击事件
        var nodes = svg.selectAll("circle")
                .on("click", function (d, i) {
                        if (index0 < 0) { // 选中第一个结点
                                index0 = d.id;
                                return;
                        }
                        if (d.id == index0) // 说明选的和第一个一样
                                return;
                        if (index1 < 0) { // 选中了一个和第一个不一样的点
                                index1 = d.id;
                                var edges = svg.selectAll("line")
                                .style("stroke", function (d, i) {
                                        if (d.source.id == index1 && d.target.id == index0 || d.source.id == index0 && d.target.id == index1)
                                                return "#F88";
                                        else
                                                return "#ccc";
                                })
                                .style("stroke-width", 2);

                        }

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




