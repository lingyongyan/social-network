// 主图区是否就绪
var gReady = false;

var gType = undefined; // 有向无向
var gWeight = undefined; // 有权无权

var force = null; // 主图区的力导向图布局
var gEdges = null; // 边集
var gEdgeTexts = null; // 权重集
var gNodes = null; // 结点集

var color = d3.scale.category20(); // 颜色集
var constMaxRadius = 25; // 最大结点圆半径

var gWidth = undefined; //主图区宽高
var gHeight = undefined;

var zoomScale = 1; //缩放系数
var zoomTranslate = [0, 0]; // 缩放平移
var constMaxZoomScale = 3;
// 画图
function paintGraph(graphData, status) {
        if (status != "success") {
                ///////////////////////////////////////////////////////
                //                  提醒用户没有加载成功                      //
                ///////////////////////////////////////////////////////                
                $('#tips').text("加载用户数据集图数据失败！").addClass('red');
                setTimeout(function () {
                        $('#tips').text("").removeClass('red');
                }, 3000);
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
                                        .style("fill", "#999");
        }



        // 2 定义力学图的布局
        force = d3.layout.force()
                .nodes(graphData.nodes) // 转换结点
                .links(graphData.edges) // 转换边
                .size([gWidth, gHeight]) // 用于设定力学图的作用范围
                .linkDistance(80) // 指定结点连接线的距离
                .charge(-800)
                .friction(0.5) // 值为0，粗糙。值为1，无摩擦。
                .gravity(0.3) // 0无重力 1 最大重力
                //.theta(0.1)
                .on("tick", tickBase)
                .start();

        // 定义交互事件
        var gZoom = d3.behavior.zoom()
                .scaleExtent([1, constMaxZoomScale])
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
                //console.log(d3.event.translate);
                zoomScale = d3.event.scale;
                zoomTranslate = d3.event.translate;
                //zoomTranslate[0] = d3.event.translate[0] < -gWidth * zoomScale * 0.1 ? -gWidth * zoomScale * 0.1 : d3.event.translate[0];
                //zoomTranslate[0] = d3.event.translate[0] > gWidth * zoomScale * 0.1 ? gWidth * zoomScale * 0.1 : d3.event.translate[0];
                //zoomTranslate[1] = d3.event.translate[1] < -gHeight * zoomScale * 0.1 ? -gHeight * zoomScale * 0.1 : d3.event.translate[1];
                //zoomTranslate[1] = d3.event.translate[1] > gHeight * zoomScale * 0.1 ? gHeight * zoomScale * 0.1 : d3.event.translate[1];
                container.attr("transform", "translate(" + zoomTranslate + ")scale(" + zoomScale + ")");
        }

        function dragstarted(d) {
                //console.log("dragstarted");
                d3.event.sourceEvent.stopPropagation();
                //d3.select(this).classed("dragging", true);
                //d3.select(this).classed("fixed", d.fixed = true);
        }
        //var newWidth = gWidth / zoomScale, newHeight = gHeight / zoomScale;
        //var newx = undefined, newy = undefined;

        function dragged(d) {
                //console.log("dragged");
                //newWidth = gWidth / zoomScale; newHeight =gHeight / zoomScale; // 
                //newx = d3.event.x; newy = d3.event.y;
                //newx = newx < constMaxRadius ? constMaxRadius : newx;
                //newx = newx > newWidth - constMaxRadius ? newWidth - constMaxRadius : newx;
                //newx = newy < constMaxRadius ? constMaxRadius : newy;
                //newy = newy > newHeight - constMaxRadius ? newHeight - constMaxRadius : newy;

                
                //console.log(d3.event.x);
                //console.log(d3.event.y);
                d3.select(this).attr("cx", d.x = d3.event.x).attr("cy", d.y = d3.event.y);
                //d3.select(this).attr("cx", d.x = newx).attr("cy", d.y = newy);
        }

        function dragended(d) {

                //console.log("dragended");
                //d3.select(this).classed("dragging", false);
                //newWidth = gWidth / zoomScale; newHeight = gHeight / zoomScale; // 

                //newx = newx < constMaxRadius ? constMaxRadius : newx;
                //newx = newx > newWidth - constMaxRadius ? newWidth - constMaxRadius : newx;
                //newy = newy < constMaxRadius ? constMaxRadius : newy;
                //newy = newy > newHeight - constMaxRadius ? newHeight - constMaxRadius : newy;
                //d3.select(this).attr("cx", d.x = newx).attr("cy", d.y = newy);
                d3.select(this).classed("fixed", d.fixed = true);
        }

       
        function dblclick(d) {
                //d3.event.sourceEvent.stopPropagation();
                d3.select(this).classed("fixed", d.fixed = false);
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

        gReady = true;
        $('#tips').text("图数据加载完毕！").addClass('green');
        setTimeout(function () {
                $('#tips').text("").removeClass('green');
        }, 3000);
}

