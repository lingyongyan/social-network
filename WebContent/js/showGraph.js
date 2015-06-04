// 主图区是否就绪
var gReady = false;

var gType = undefined; // 有向无向
var gWeight = undefined; // 有权无权

var force = undefined; // 主图区的力导向图布局
var gEdges = undefined; // 边集
var gEdgeTexts = undefined; // 权重集
var gNodes = undefined; // 结点集
var color = d3.scale.category20(); // 颜色集
var constMaxRadius = 25; // 最大结点圆半径
var gWidth = undefined;
var gHeight = undefined;
// 画图
function paintGraph(graphData, status) {
        if (status != "success") {
                ///////////////////////////////////////////////////////
                //                  提醒用户没有加载成功                      //
                ///////////////////////////////////////////////////////
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
                        .attr("markerWidth", "6")
                        .attr("markerHeight", "6")
                        .attr("viewBox", "0 0 12 12")
                        .attr("refX", 20)
                        .attr("refY", 6)
                        .attr("orient", "auto");
                var arrow_path = "M0,0 L12,6 L0,12 L8,6 L0,0";
                arrowMarker.append("path")
                                        .attr("d", arrow_path)
                                        .style("fill", "#333");
        }



        // 2 定义力学图的布局
        force = d3.layout.force()
                .nodes(graphData.nodes) // 转换结点
                .links(graphData.edges) // 转换边
                .size([gWidth, gHeight])
                .linkDistance(40)
                .charge(-400)
                .on("tick", function () {
                        tickBase();
                })
                .start();

        // 定义交互事件
        var gZoom = d3.behavior.zoom()
                .scaleExtent([1, 10])
                .on("zoom", zoomed);
        // 拖拽响应
        var dragN = force.drag()
                        .origin(function (d) { return d; })
                        .on("dragstart", dragstarted)
                        .on("drag", dragged)
                        .on("dragend", dragended);



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
                .style("stroke", "#ccc")
                .style("stroke-width", function (d) {
                        return Math.log(d.weight)+2;
                })
                .attr("class", "edge");
        if (gType){
                gEdges.attr("marker-end", "url(#arrow)");
        }                


        // 根据有权无权绘制线的权重
        // 3.2 绘制直线上的文字        
        if (gWeight) {
                gEdgeTexts = container.selectAll(".linetext")
                        .data(graphData.edges)
                        .enter()
                        .append("text")
                        .attr("class", "linetext")
                        .text(function (d) { return d.weight; });
        }


        // 4 绘制结点        
        gNodes = container.selectAll(".node")
                .data(graphData.nodes)
                .enter()
                .append("circle")
                .attr("class", "node")
                .attr("r", function (d, i) {
                        return Math.log(d.group)+10;
                })
                .style("fill", function (d, i) {
                        return color(d.group);
                })
                .on("dblclick", dblclick)
                .call(dragN);
        // 悬浮鼠标结点信息
        gNodes.append("title")
                .text(function (d) { return d.name+ " "+ d.group; });


        // zoomed 函数，
        // 用于更改需要缩放的元素的属性，
        // d3.event.translate 是平移的坐标值，
        // d3.event.scale 是缩放的值。
        function zoomed() {
                container.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
        }

        function dragstarted(d) {
                //console.log("dragstarted");
                d3.event.sourceEvent.stopPropagation();
                //d3.select(this).classed("dragging", true);
                d3.select(this).classed("fixed", d.fixed = true);
        }

        function dragged(d) {
                //console.log("dragged");
                d3.select(this).attr("cx", d.x = d3.event.x).attr("cy", d.y = d3.event.y);
        }

        function dragended(d) {
                //console.log("dragended");
                //d3.select(this).classed("dragging", false);
        }

       
        function dblclick(d) {
                //d3.event.sourceEvent.stopPropagation();
                d3.select(this).classed("fixed", d.fixed = false);
        }
        

        gReady = true;
}

function tickBase() {
        //限制结点的边界
        gNodes.forEach(function (d, i) {
                d.x = d.x - constMaxRadius < 0 ? constMaxRadius : d.x;
                d.x = d.x + constMaxRadius > gWidth ? gWidth - constMaxRadius : d.x;
                d.y = d.y - constMaxRadius < 0 ? constMaxRadius : d.y;
                d.y = d.y + constMaxRadius > gHeight ? gHeight - constMaxRadius : d.y;
        });
        //更新连接线的位置
        gEdges.attr("x1", function (d) { return d.source.x; })
                .attr("y1", function (d) { return d.source.y; })
                .attr("x2", function (d) { return d.target.x; })
                .attr("y2", function (d) { return d.target.y; });

        gNodes.attr("cx", function (d) { return d.x; })
                .attr("cy", function (d) { return d.y; });

        if (gWeight) {
                //更新连接线上文字的位置
                gEdgeTexts.attr("x", function (d) { return (d.source.x + d.target.x) / 2; })
                        .attr("y", function (d) { return (d.source.y + d.target.y) / 2; });
        }
}