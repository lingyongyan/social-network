
var tipsLastTime = 2500; // 提示存在事件

//

var control = null; // 全局控制变量

var gWidth = undefined; //主图区宽高
var gHeight = undefined;

var gType = undefined; // 有向无向
var gWeight = undefined; // 有权无权

var oNodes = null; // 结点集DOM对象
var minRadius = 10; //最小结点半径
var maxRadius = 30; // 最大结点圆半径

var oEdges = null; // 边集DOM对象
var oEdgeTexts = null; // 权重集DOM对象
var minWidth = 1.5; // 最小边宽
var maxWidth = 4.5; // 最大边宽
var gReady = undefined;

////////////////////////////////////////////////////////
//                        树状目录浏览特效                        //
////////////////////////////////////////////////////////
function callInit(graphData, status) {
        if (status != "success") {
                ///////////////////////////////////////////////////////                
                //                  提醒用户没有加载成功                      //                
                ///////////////////////////////////////////////////////                     
                warning("加载用户数据集图数据失败！");
                return console.log(status);
        }
        initialize(graphData).then(function (control) {
                doTheDIR(control);
        });
}
// initialize函数
// 配置全局control
function initialize(graphData) {
        var initPromise = $.Deferred();
        // 一个数据集实例，包括原始数据，正在处理的数据以及控制面板        
        control = {};
        control.data = graphData;
        control.divName = "#graph";
        var d3options = {
                "radius": 2.5,
                "fontSize": 9,
                "labelFontSize": 9,
                "gravity": 0.1,
                "height": 800,
                "nodeFocusColor": "#000",
                "nodeFocusRadius": 25,
                "nodeFocus": true,
                "linkDistance": 150,
                "charge": -220,
                "nodeResize": "group",//"count"
                "nodeLabel": "label",
                "linkName": "tag",
                "constMaxZoomScale": 5
        }
        control.options = $.extend({
                stackHeight: 12,
                width: $(control.divName).outerWidth(),
                height: $(control.divName).outerHeight(), //"height": 800,
                radius: 5,//"radius": 2.5,
                gap: 1.5,
                fontSize: 14,//"fontSize": 9,
                labelFontSize: 8, //"labelFontSize": 9,
                labelOffset: "5",
                linkName: null,//"linkName": "tag"
                markerWidth: 0,
                markerHeight: 0,
                styleColumn: null,
                styles: null,
                nodeResize: "",//"nodeResize": "group",
                nodeFocus: true,//"nodeFocus": true,
                nodeFocusRadius: 25,//"nodeFocusRadius": 25,
                nodeFocusColor: "#000", //"nodeFocusColor": "black",
                nodeLabel: null,//"nodeLabel": "label",
                // 力导向图配置参数              
                linkDistance: 80,//"linkDistance": 150,
                charge: -120,//"charge": -220,
                gravity: .2 //gravity: .05,     "gravity": 0.1,
        }, d3options);

        var options = control.options;
        options.gap = options.gap * options.radius;
        control.width = options.width;
        control.height = options.height;

        control.dNodes = control.data.nodes;
        control.dEdges = control.data.edges;

        control.color = d3.scale.category20();
        control.clickHack = 500; // 单击双击延迟
        control.clickTimer = null;
        organizeData(control);
        


        control.svg = d3.select(control.divName);

        // 1 选择id为graph的SVG元素
        // 获取graph SVG的宽高
        gWidth = parseInt(control.svg.attr("width"));
        gHeight = parseInt(control.svg.attr("height"));

        gType = control.data.type; // 有向无向
        gWeight = control.data.weight; // 有权无权

        // 配置力导向图
        control.force = d3.layout.force()
                .size([gWidth, gHeight])
                .linkDistance(control.options.linkDistance)
                .charge(control.options.charge)
                //.friction(control.options.friction)
                .gravity(control.options.gravity);
        // 如果是有向图则需要箭头
        if (gType) {
                var defs = control.svg.append("defs");
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
        // 定义交互事件
        control.zoom = d3.behavior.zoom()
                .scaleExtent([1, control.options.constMaxZoomScale])//control.constMaxZoomScale
                .on("zoom", zoomed);
        control.svg_g = control.svg.append("g")
                .attr("transform", "translate(0, 0)")
                .call(control.zoom)
                .on("dblclick.zoom", null);
        // 放置一个阻止事件冒泡的隐形矩形框
        control.rect = control.svg_g.append("rect")
                .attr("width", gWidth)
                .attr("height", gHeight)
                .style("fill", "none")
                .style("pointer-events", "all");

        // 放结点边的容器，该容器支持 缩放 拖拽
        control.container = control.svg_g.append("g");
        
        // 拖拽响应
        control.drag = control.force.drag()
                        .origin(function (d) { return d; })
                        .on("dragstart", dragstarted)
                        .on("drag", dragged)
                        .on("dragend", dragended);

        initPromise.resolve(control);
        return initPromise.promise();
        // zoomed 函数，
        // 用于更改需要缩放的元素的属性，
        // d3.event.translate 是平移的坐标值，
        // d3.event.scale 是缩放的值。
        function zoomed() {
                console.log(d3.event);
                console.log(d3.event);
                control.container.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
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
}
// organizeData函数，
// 组织control的数据，
// 主要是做重映射
function organizeData(control) {
        for (var i = 0; i < control.dNodes.length; i++) {
                var node = control.dNodes[i];
                node.unique = i;
                node.isCurrentlyFocused = false;
        }
        for (var i = 0; i < control.dEdges.length; i++) {
                var edge = control.dEdges[i];
                edge.unique = i;
                edge.source = control.dNodes[edge.source];
                edge.target = control.dNodes[edge.target];
        }
        return control;
}
// doTheDIR函数，做目录展示
// 双击进入目录下一层，双击上一级再回去
function doTheDIR(control) {
        gReady = false;
        var container = control.container;
        var force = control.force;
        force.nodes(control.dNodes)
                .links(control.dEdges);
                
        // 边
        // Update the edges    
        oEdges = container.selectAll("line")
                .data(control.dEdges, function (d) { return d.unique; });
        // Enter any new edges   
        oEdges.enter().append("line")//
                .attr("class", "edge")
                .style("stroke-width", function (dEdge) {
                        var width2 = Math.sqrt(dEdge.weight) + minWidth;
                        return width2 > maxWidth ? maxWidth : width2;
                });
        // Exit any old edges. 
        oEdges.exit().remove();
        if (gType) {
                oEdges.attr("marker-end", "url(#arrow)");
        }



        // 根据有权无权绘制线的权重
        // 3.2 绘制直线上的文字        
        if (gWeight) {
                oEdgeTexts = container.selectAll(".linetext")
                        .data(control.dEdges, function (d) { return d.unique; });

                oEdgeTexts.enter()
                        .append("text")
                        .attr("class", "linetext");
                resetWeights();
                oEdgeTexts.exit().remove();
        }

        // Update the nodes     
        oNodes = container.selectAll("circle")
                .data(control.dNodes, function (d) { return d.unique; });
             
        // Enter any new nodes.    
        var oNodesEnter = oNodes.enter()
                .append("circle")
                .attr("class", "node")
                .style("fill", function (d) {
                        return getColor(d);
                })
                .attr("r", function (d) {
                        return getRadius(d);
                })                
                .on("dblclick", dblclick)
                .on("click", function (d) {
                        clearTimeout(control.clickTimer);
                        control.clickTimer = setTimeout(function () {
                                console.log("click!");
                        }, control.clickHack);
                })
                .call(control.drag);


        if (control.options.nodeLabel) {
                // text is done once for shadow as well as for text  
                oNodesEnter.append("svg:text")
                        .attr("x", control.options.labelOffset)
                        .attr("dy", ".35em")    //  不太            
                        .attr("class", "text")  //  一样             
                        .style("font-size", control.options.labelFontSize + "px")
                        .text(function (d) {
                                return d.name;
                        });
        }
        // Exit any old nodes.    
        oNodes.exit().remove();
        console.log(oNodes);
        console.log(oEdges);
        

        control.oEdges = d3.selectAll(".edge");
        control.oNodes = d3.selectAll(".node");


        force.on("tick", tick)
        .start();
        gReady = true;
        function tick() {
                //限制结点的边界
                /*control.*/oNodes.forEach(function (nodeData) {
                        nodeData.x = nodeData.x - maxRadius < 0 ? maxRadius : nodeData.x;
                        nodeData.x = nodeData.x + maxRadius > gWidth ? gWidth - maxRadius : nodeData.x;
                        nodeData.y = nodeData.y - maxRadius < 0 ? maxRadius : nodeData.y;
                        nodeData.y = nodeData.y + maxRadius > gHeight ? gHeight - maxRadius : nodeData.y;
                });
                //更新连接线的位置
                /*control.*/oEdges.attr("x1", function (edgeData) { return edgeData.source.x; })
                        .attr("y1", function (edgeData) { return edgeData.source.y; })
                        .attr("x2", function (edgeData) { return edgeData.target.x; })
                        .attr("y2", function (edgeData) { return edgeData.target.y; });

                /*control.*/oNodes.attr("cx", function (nodeData) { return nodeData.x; })
                        .attr("cy", function (nodeData) { return nodeData.y; });

                if (gWeight) {
                        //更新连接线上文字的位置
                        /*control.*/oEdgeTexts.attr("x", function (weightData) { return (weightData.source.x + weightData.target.x) / 2; })
                                .attr("y", function (weightData) { return (weightData.source.y + weightData.target.y) / 2; });
                }
        }

        function dblclick(d) {
                clearTimeout(control.clickTimer);
                //console.log("dblclicked");
                //d3.select(this).classed("fixed", nodeData.fixed = false);
                if (control.options.nodeFocus) {
                        d.isCurrentlyFocused = !d.isCurrentlyFocused;
                        doTheDIR(makeFilteredData(control));
                }
        }

        // 根据是否聚焦获得半径       
        function getRadius(d) {
                var r = control.options.radius * (control.options.nodeResize ? Math.sqrt(d[control.options.nodeResize]) / Math.PI + 5 : 1);
                return control.options.nodeFocus && d.isCurrentlyFocused ? control.options.nodeFocusRadius : r;
        }
        // 根据分组获得颜色    
        function getColor(d) {
                return control.options.nodeFocus && d.isCurrentlyFocused ? control.options.nodeFocusColor : control.color(d.group);
        }
}
// 生成过滤后的数据
function makeFilteredData(control, selectedNode) {
        // we'll keep only the data where filterned nodes are the source or target      
        var newNodes = [];
        var newEdges = [];
        for (var i = 0; i < control.data.edges.length ; i++) {
                var edge = control.data.edges[i];
                if (edge.target.isCurrentlyFocused || edge.source.isCurrentlyFocused) {
                        newEdges.push(edge);
                        addNodeIfNotThere(edge.source, newNodes);
                        addNodeIfNotThere(edge.target, newNodes);
                }
        }
        // if none are selected reinstate the whole dataset      
        if (newNodes.length > 0) {
                control.dEdges = newEdges;
                control.dNodes = newNodes;
        } else {
                control.dNodes = control.data.nodes;
                control.dEdges = control.data.edges;
        }
        console.log(control.dNodes);
        console.log(control.dEdges);
        return control;
        function addNodeIfNotThere(node, nodes) {
                for (var i = 0; i < nodes.length; i++) {
                        if (nodes[i].unique == node.unique)
                                return i;
                }
                return nodes.push(node) - 1;
        }
}

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
// 重置结点
function resetNodes() {
        if (!oNodes.empty())
                oNodes.attr("class", "node")
                        .style("fill", function (nodeData) {
                                return control.color(nodeData.group);
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
        if (!oEdges.empty())
                oEdges.attr("class", "edge");
}
//重置权重
function resetWeights() {
        if (!oEdgeTexts.empty())
                oEdgeTexts.text(function (theText) { return theText.weight; });
}

