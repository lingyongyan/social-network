// 画图
function paintGraph(root, status) {
        if (status != "success") {
                ///////////////////////////////////////////////////////
                //                  提醒用户没有加载成功                      //
                ///////////////////////////////////////////////////////
                return console.log(status);
        }

        // 1 选择id为graph的SVG元素
        var svg = d3.select("#graph");
        // 获取graph SVG的宽高
        var gWidth = parseInt(svg.attr("width"));
        var gHeight = parseInt(svg.attr("height"));

        var rootType = root.type; // 有向无向
        var rootWeight = root.weight; // 有权无权
        // 如果是有向图则需要箭头
        if (rootType) {
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
                var arrow_path = "M0,0 L12,6 L0,12 L11,6 L0,0";
                arrowMarker.append("path")
                                        .attr("d", arrow_path)
                                        .style("fill", "#ccc");
        }



        // 2 定义力学图的布局
        var force = d3.layout.force()

                .size([gWidth, gHeight])
                .linkDistance(40)
                .charge(-400);
        //.start();

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

        var rootEdges = container.selectAll(".edge")
                .data(root.edges)
                .enter()
                .append("line")
                .style("stroke", "#ccc")
                .style("stroke-width", function (d) {
                        return Math.sqrt(d.weight);
                })
                .attr("class", "edge")
                .attr("marker-end", "url(#arrow)");


        // 根据有权无权绘制线的权重
        // 3.2 绘制直线上的文字        
        var rootETexts = container.selectAll(".linetext")
                .data(root.edges)
                .enter()
                .append("text")
                .attr("class", "linetext")
                .text(function (d) {
                        if (rootWeight)
                                return d.weight;
                        else
                                return "";
                });

        // 4 绘制结点
        var nodeR = 15; // 结点圆半径                
        var color = d3.scale.category20();

        var rootNodes = container.selectAll(".node")
                .data(root.nodes)
                .enter()
                .append("circle")
                .attr("class", "node")
                .attr("r", function (d, i) {
                        return d.group;
                })//nodeR
                .style("fill", function (d, i) {
                        return color(d.group);
                })
                .on("dblclick", dblclick)
                .call(dragN);

        // 5 绘制结点标签
        var text_dx = -7.5;
        var text_dy = 15;

        var rootNTexts = container.selectAll(".nodetext")
                            .data(root.nodes)
                            .enter()
                            .append("text")
                            .attr("class", "nodetext")
                            .attr("dx", text_dx)
                            .attr("dy", text_dy)
                            .text(function (d) {
                                    return d.name;
                            });

        // zoomed 函数，
        // 用于更改需要缩放的元素的属性，
        // d3.event.translate 是平移的坐标值，
        // d3.event.scale 是缩放的值。
        function zoomed() {
                //d3.event.sourceEvent.stopPropagation();
                container.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
                // 更新动画
        }

        function dragstarted(d) {
                d3.event.sourceEvent.stopPropagation();
                //d3.select(this).classed("dragging", true);
                d3.select(this).classed("fixed", d.fixed = true);
                //ticktick();
        }

        function dragged(d) {
                d3.select(this).attr("cx", d.x = d3.event.x).attr("cy", d.y = d3.event.y);
        }

        function dragended(d) {
                //d3.select(this).classed("dragging", false);
        }

        force.on("tick", function () {
                //限制结点的边界
                rootNodes.forEach(function (d, i) {
                        d.x = d.x - nodeR < 0 ? nodeR : d.x;
                        d.x = d.x + nodeR > gWidth ? gWidth - nodeR : d.x;
                        d.y = d.y - nodeR < 0 ? nodeR : d.y;
                        d.y = d.y + nodeR > gHeight ? gHeight - nodeR : d.y;
                });
                //更新连接线的位置
                rootEdges.attr("x1", function (d) { return d.source.x; })
                        .attr("y1", function (d) { return d.source.y; })
                        .attr("x2", function (d) { return d.target.x; })
                        .attr("y2", function (d) { return d.target.y; });

                rootNodes.attr("cx", function (d) { return d.x; })
                        .attr("cy", function (d) { return d.y; });

                if (rootWeight) {
                        //更新连接线上文字的位置
                        rootETexts.attr("x", function (d) { return (d.source.x + d.target.x) / 2; })
                                .attr("y", function (d) { return (d.source.y + d.target.y) / 2; });
                }

                //更新结点的文字
                rootNTexts.attr("x", function (d) { return d.x })
                        .attr("y", function (d) { return d.y });

        });
        function dblclick(d) {
                //d3.event.sourceEvent.stopPropagation();
                d3.select(this).classed("fixed", d.fixed = false);
        }
        // force.on("tick", ticktick);

        force.nodes(root.nodes) // 转换结点
                .links(root.edges) // 转换边
                .start();
}
