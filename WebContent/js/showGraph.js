////////////////////////////////////////////////////////
//                             主图区                                    //
////////////////////////////////////////////////////////
// 主图区是否就绪
var gReady = false;
var gWidth = undefined; //主图区宽高
var gHeight = undefined;

var gType = undefined; // 有向无向
var gWeight = undefined; // 有权无权

var gNodes = null; // 结点集
var minRadius = 10; //最小结点半径
var maxRadius = 30; // 最大结点圆半径
var color = d3.scale.category20(); // 颜色集

var gEdges = null; // 边集
var gEdgeTexts = null; // 权重集
var minWidth = 1.5; // 最小边宽
var maxWidth = 4.5; // 最大边宽

var force = null; // 主图区的力导向图布局
////////////////////////////////////////////////////////
//                           与交互相关                               //
////////////////////////////////////////////////////////
// 单击和双击之间的延迟
var constMaxClickTimeDelay = 400;//毫秒
var clickTimeDelay = null; // 对应的计时器

var zoomScale = 1; //缩放系数
var zoomTranslate = [0, 0]; // 缩放平移
var constMaxZoomScale = 5; //最大缩放系数

var tipsLastTime = 2500; // 提示存在事件

function tips(msg, tColor) {
        $('#tips').text(msg).addClass(tColor);
        setTimeout(function () {
                $('#tips').text("").removeClass(tColor);
        }, tipsLastTime);
}
function success(msg) { tips(msg, "green"); }
function info(msg) { tips(msg, "blue"); }
function warning(msg) { tips(msg, "yellow"); }
function danger(msg) { tips(msg, "red"); }
// 画图
function paintGraph(graphData, status) {
        if (status != "success") {
                ///////////////////////////////////////////////////////
                //                  提醒用户没有加载成功                      //
                ///////////////////////////////////////////////////////     
                warning("加载用户数据集图数据失败！");
                return console.log(status);
        }

        // 1 选择id为graph的SVG元素
        var svg = d3.select("#graph");

        // 获取graph SVG的宽高
        gWidth = parseInt(svg.attr("width"));
        gHeight = parseInt(svg.attr("height"));

        gType = graphData.type; // 有向无向
        gWeight = graphData.weight; // 有权无权
        // 如果是有向图则需要箭头
        if (gType) {
                var defs = svg.append("defs");
                var arrowMarker = defs.append("marker")
                        .attr("id", "arrow")
                        .attr("markerUnits", "strokeWidth")
                        .attr("markerWidth", "4")
                        .attr("markerHeight", "4")
                        .attr("viewBox", "0 0 4 4")
                        .attr("refX", 16)
                        .attr("refY", 2)
                        .attr("orient", "auto");
                var arrow_path = "M0,0 L4,2 L0,4 L1,2";//"M1,0 L8,4 L1,8 L3,6 L0,6.5 L3,4 L0,2.5 L3,2 L1,0"
                arrowMarker.append("path")
                        .attr("d", arrow_path)
                        .style("fill", "#aaa")
                        .style("opacity", "1.0");
        }



        // 2 定义力学图的布局
        force = d3.layout.force()
                .nodes(graphData.nodes) // 转换结点
                .links(graphData.edges) // 转换边
                .size([gWidth, gHeight]) // 用于设定力学图的作用范围
                .linkDistance(60) // 指定结点连接线的距离80
                .charge(-360) // -800
                //.friction(0.5) // 值为0，粗糙。值为1，无摩擦。
                //.gravity(0.3) // 0无重力 1 最大重力
                //.theta(0.1)
                .on("tick", function () {
                        //限制结点的边界
                        gNodes.forEach(function (nodeData) {
                                nodeData.x = nodeData.x - maxRadius < 0 ? maxRadius : nodeData.x;
                                nodeData.x = nodeData.x + maxRadius > gWidth ? gWidth - maxRadius : nodeData.x;
                                nodeData.y = nodeData.y - maxRadius < 0 ? maxRadius : nodeData.y;
                                nodeData.y = nodeData.y + maxRadius > gHeight ? gHeight - maxRadius : nodeData.y;
                        });
                        //更新连接线的位置
                        gEdges.attr("x1", function (edgeData) { return edgeData.source.x; })
                                .attr("y1", function (edgeData) { return edgeData.source.y; })
                                .attr("x2", function (edgeData) { return edgeData.target.x; })
                                .attr("y2", function (edgeData) { return edgeData.target.y; });

                        gNodes.attr("cx", function (nodeData) { return nodeData.x; })
                                .attr("cy", function (nodeData) { return nodeData.y; });

                        if (gWeight) {
                                //更新连接线上文字的位置
                                gEdgeTexts.attr("x", function (weightData) { return (weightData.source.x + weightData.target.x) / 2; })
                                        .attr("y", function (weightData) { return (weightData.source.y + weightData.target.y) / 2; });
                        }
                })
                .start();

        // 定义交互事件
        var gZoom = d3.behavior.zoom()
                .scaleExtent([0.2, constMaxZoomScale])
                .on("zoom", zoomed);
        // 拖拽响应
        var dragN = force.drag()
                        .origin(function (d) { return d; })
                        .on("dragstart", dragstarted)
                        .on("drag", dragged)
                        .on("dragend", dragended);

        //var fisheye = d3.fisheye.circular()
        //        .radius(120);



        var svg_g = svg.append("g")
                .attr("transform", "translate(0, 0)")
                .call(gZoom)
                .on("dblclick.zoom", null);

        // 放置一个阻止事件冒泡的隐形矩形框
        var rect = svg_g.append("rect")
                .attr("width", gWidth)
                .attr("height", gHeight)
                .style("fill", "none")
                .style("pointer-events", "all");

        // 放结点边的容器，该容器支持 缩放 拖拽
        var container = svg_g.append("g");

        // 根据有向无向绘制连线
        // 3.1 绘制连接线
        gEdges = container.selectAll(".edge")
                .data(graphData.edges)
                .enter()
                .append("line")
                .style("stroke-width", function (edgeData) {
                        var width2 = Math.sqrt(edgeData.weight) + minWidth;
                        return width2 > maxWidth ? maxWidth : width2;
                });
        resetEdges();
        if (gType) {
                gEdges.attr("marker-end", "url(#arrow)");
        }


        // 根据有权无权绘制线的权重
        // 3.2 绘制直线上的文字        
        if (gWeight) {
                gEdgeTexts = container.selectAll(".linetext")
                        .data(graphData.edges)
                        .enter()
                        .append("text")
                        .attr("class", "linetext");
                resetWeights();
        }


        // 4 绘制结点 
        gNodes = container.selectAll(".node")
                .data(graphData.nodes)
                .enter()
                .append("circle")
                .attr("r", function (nodeData) {
                        var radius = Math.sqrt(nodeData.group) + minRadius;
                        return radius > maxRadius ? maxRadius : radius;
                })
                .style("fill", function (nodeData) {
                        return color(nodeData.group);
                })
                .on("dblclick", dblclick)
                .call(dragN);
        // 悬浮鼠标结点信息
        resetNodes();
        gNodes.append("title")
                .text(function (nodeData) { return nodeData.name + " " + nodeData.group; });


        // zoomed 函数，
        // 用于更改需要缩放的元素的属性，
        // d3.event.translate 是平移的坐标值，
        // d3.event.scale 是缩放的值。
        function zoomed() {
                zoomScale = d3.event.scale;
                zoomTranslate = d3.event.translate;
                container.attr("transform", "translate(" + zoomTranslate + ")scale(" + zoomScale + ")");
        }
        function dragstarted(d) {
                d3.event.sourceEvent.stopPropagation();
                //d3.select(this).classed("dragging", true);
                //d3.select(this).classed("fixed", d.fixed = !d.fixed);
        }
        function dragged(d) {
                d3.select(this).attr("cx", d.x = d3.event.x).attr("cy", d.y = d3.event.y);
                //d3.select(this).attr("cx", d.x = newx).attr("cy", d.y = newy);
        }
        function dragended(d) {
                //d3.select(this).classed("dragging", false);
                d3.select(this).classed("fixed", d.fixed = !d.fixed);
        }
        function dblclick(nodeData) {
                clearTimeout(clickTimeDelay);
                console.log("dblclicked");
                //d3.select(this).classed("fixed", nodeData.fixed = false);
                //if (nodeData.group == crntGroup)
                //        return;
                //loadSelection(nodeData.group);
        }
        function loadSelection(nodeGroup) {
                console.log(nodeGroup);
                if (!svg_g.empty())
                        svg_g.remove();

                function loadDataSet(theDataSet, startDepth, endDepth) {
                        // 初始化一些全局变量
                        crntDataSet = theDataSet;

                        // 分三块加载渲染，
                        // 之后分别设置一个就绪变量，图区是必须的；
                        // 以便各子功能可以调用

                        // 一、读入用户数据集之图JSON文件并渲染
                        $.get("../json/graph.json", { whichDataSet: theDataSet, filename: "graph.json", root: startDepth, children: endDepth }, paintGraph);
                        // 三、读入用户数据集之邻接矩阵邻接链表JSON文件并渲染
                        $.get("../json/graph.json", { whichDataSet: theDataSet, filename: "graph.json", root: startDepth, children: endDepth }, paintTable);

                }
                loadDataSet(crntDataSet, nodeGroup, nodeGroup + 3);
        }

        gReady = true;
        success("图数据加载完毕！");

}
// 重置结点
function resetNodes() {
        if (gNodes)
                gNodes.attr("class", "node")
                        .style("fill", function (nodeData) {
                                return color(nodeData.group);
                        })
                        .on("mouseover", function (nodeData) {
                                d3.select(this)//.attr("class", "nodeHighlight")
                                .attr("r", function (nodeData) {
                                        var radius = Math.sqrt(nodeData.group) + minRadius + 2;
                                        return radius > maxRadius ? maxRadius : radius;
                                });
                        })
                        .on("mouseout", function (nodeData) {
                                d3.select(this)//.attr("class", "node")
                                        .attr("r", function (nodeData) {
                                                var radius = Math.sqrt(nodeData.group) + minRadius;
                                                return radius > maxRadius ? maxRadius : radius;
                                        });
                        });
}
// 重置边
function resetEdges() {
        if (gEdges)
                gEdges.attr("class", "edge");
}
//重置权重
function resetWeights() {
        if (gEdgeTexts)
                gEdgeTexts.text(function (theText) { return theText.weight; });
}



