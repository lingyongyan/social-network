///////////////////////////////////////////////////////
//                   Graph页全局变量区                        //
///////////////////////////////////////////////////////
// 当前数据集
var crntDataSet = undefined;


///////////////////////////////////////////////////////
//                           Graph页入口                           //
///////////////////////////////////////////////////////
// Graph.html页面元素加载完毕后，
// 开始进入这里进行加载（图数据集）
// 1.渲染 图区
// 2.渲染 度分布图区
// 3.渲染 邻接链表邻接矩阵区
(function () {
        // 首次加载graph页，
        // 先问问后台有哪些数据集{ Array }：
        /*     零：用户没有上传，后台返回 [ 默认测试集名 ]         
        [
                "test"
        ]        
        //      壹：用户有上传，后台返回 [ 用户上传的数据集名 ]+
        [
                "userdata0",
                "userdata1"
        ]
        */
        $.get("../json/dataSets.json", {/*该项请求协议*/ }, function (dataSets, status) {
                if (status != "success") {
                        ///////////////////////////////////////////////////////
                        //                  提醒用户没有加载成功                      //
                        ///////////////////////////////////////////////////////
                        return console.log(status);
                }
                ///////////////////////////////////////////////////////
                //                    使用返回的dataSets                       //
                //             !!更新用户数据集选项卡区域!!                //
                ///////////////////////////////////////////////////////

                // 只用渲染第一个数据集
                crntDataSet = dataSets[0];
                loadDataSet(crntDataSet);
        });

})()

// 加载指定数据集
function loadDataSet(theDataSet) {
        // 初始化一些全局变量
        crntDataSet = theDataSet;


        // 一、读入用户数据集之图JSON文件并渲染
        $.get("../json/graph.json", { whichDataSet: theDataSet, filename: "graph.json" }, paintGraph);
        // 二、读入用户数据集之度分布图JSON文件并渲染
        $.get("../json/degree.json", { whichDataSet: theDataSet, filename: "degree.json" }, paintDegree);
        // 三、读入用户数据集之邻接矩阵邻接链表JSON文件并渲染
        $.get("../json/table.json", { whichDataSet: theDataSet, filename: "table.json" }, paintTable);

        Ready = true;// 已就绪
}
// 画图
function paintGraph(root, status) {
        if (status != "success") {
                ///////////////////////////////////////////////////////
                //                  提醒用户没有加载成功                      //
                ///////////////////////////////////////////////////////
                return console.log(status);
        }
        // zoom 的定义，缩放是由 d3.behavior.zoom() 定义的。
        var zoom = d3.behavior.zoom()
                .scaleExtent([1, 10])
                .on("zoom", zoomed);
        //d3.event.sourceEvent.stopPropagation();

        // 1 选择id为graph的SVG元素
        var svg = d3.select("#graph").call(zoom);
        //var rect = svg.append("rect")
        //.attr("width", gWidth)
        //.attr("height", gHeight)
        //.style("fill", "none")
        //.style("pointer-events", "all");

        // 如果是有向图则需要箭头
        if (root.type) {
                var defs = svg.append("defs");
                var arrowMarker = defs.append("marker")
                        .attr("id", "arrow")
                        .attr("markerUnits", "strokeWidth")
                        .attr("markerWidth", "6")
                        .attr("markerHeight", "6")
                        .attr("viewBox", "0 0 12 12")
                        .attr("refX", "27")
                        .attr("refY", "6")
                        .attr("orient", "auto");
                var arrow_path = "M0,0 L12,6 L0,12 L12,6";
                arrowMarker.append("path")
                                        .attr("d", arrow_path)
                                        .attr("fill", "#ccc");
        }

        var container = svg.append("g");
                //.call(zoom);

        // 获取graph SVG的宽高
        var gWidth = parseInt(svg.attr("width"));
        var gHeight = parseInt(svg.attr("height"));


        // 2 定义力学图的布局
        var force = d3.layout.force()
                .nodes(root.nodes) // 转换结点
                .links(root.edges) // 转换边
                .size([gWidth, gHeight])
                .linkDistance(200)
                .charge(-800)
                .start();
        // 拖拽响应
        var dragN = force.drag()
                .on("dragstart", function (d) {
                        d.fixed = true;    //拖拽开始后设定被拖拽对象为固定
                });

        // 根据有向无向绘制连线
        // 3.1 绘制连接线
        var edges_line = container.selectAll("line")
                .data(root.edges)
                .enter()
                .append("line")
                .attr("class", function (d, i, A) {
                        if (root.type) {
                                //d["marker-end"] = "url('#arrow')";
                                return "line10";
                        }                                
                        else
                                return "line00";
                }).attr("marker-end", "url(#arrow)");

        // 根据有权无权绘制线的权重
        // 3.2 绘制直线上的文字
        var edges_text = container.selectAll(".linetext")
                .data(root.edges)
                .enter()
                .append("text")
                .attr("class", "linetext")
                .text(function (d) {
                        if (root.weight) {
                                return d.weight;
                        }
                        //return "";
                });

        // 4 绘制结点
        var nodeR = 15; // 结点圆半径                
        var color = d3.scale.category20();
        var nodes = container.selectAll("circle")
                .data(root.nodes)
                .enter()
                .append("circle")
                .attr("r", nodeR)
                .style("fill", function (d, i) {
                        return color(i);
                })
                .on("dblclick", function (d, i) {
                        d.fixed = false;
                })
                .call(dragN);
                //.call(force.drag);

        // 5 绘制结点标签
        var text_dx = -7.5;
        var text_dy = 15;

        var nodes_text = container.selectAll(".nodetext")
                            .data(root.nodes)
                            .enter()
                            .append("text")
                            .attr("class", "nodetext")
                            .attr("dx", text_dx)
                            .attr("dy", text_dy)
                            .text(function (d) {
                                    return d.id;
                            });


        force.on("tick", function (d) {
                //if (force.alpha() >= 0.05)
                //        return;
                //限制结点的边界
                root.nodes.forEach(function (d, i) {
                        d.x = d.x - nodeR < 0 ? nodeR : d.x;
                        d.x = d.x + nodeR > gWidth ? gWidth - nodeR : d.x;
                        d.y = d.y - nodeR < 0 ? nodeR : d.y;
                        d.y = d.y + nodeR > gHeight ? gHeight - nodeR : d.y;
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
                nodes_text.attr("y", function (d) { return d.y; });
                //force.stop();
        });
}


// 画度分布图
function paintDegree(root, status) {

        if (status != "success") {
                ///////////////////////////////////////////////////////
                //                  提醒用户没有加载成功                      //
                ///////////////////////////////////////////////////////
                return console.log(status);
        }
        // TODO
}


// 画表
function paintTable(root, status) {
        if (status != "success") {
                ///////////////////////////////////////////////////////
                //                  提醒用户没有加载成功                      //
                ///////////////////////////////////////////////////////
                return console.log(status);
        }
        // TODO

}
// zoomed 函数，
// 用于更改需要缩放的元素的属性，
// d3.event.translate 是平移的坐标值，
// d3.event.scale 是缩放的值。
function zoomed() {
        //d3.event.sourceEvent.stopPropagation();
        container.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
        root.nodes.forEach(function (d, i) {
                d.x = d.x - nodeR < 0 ? nodeR : d.x;
                d.x = d.x + nodeR > gWidth ? gWidth - nodeR : d.x;
                d.y = d.y - nodeR < 0 ? nodeR : d.y;
                d.y = d.y + nodeR > gHeight ? gHeight - nodeR : d.y;
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
        nodes_text.attr("y", function (d) { return d.y; });
}

function dragstarted(d) {
        d3.event.sourceEvent.stopPropagation();
        d3.select(this).classed("dragging", true);
}

function dragged(d) {
        d3.select(this).attr("cx", d.x = d3.event.x).attr("cy", d.y = d3.event.y);
}

function dragended(d) {
        d3.select(this).classed("dragging", false);
}