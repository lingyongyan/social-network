var width = parseInt(d3.select("#mysvg").attr("width"));
var height = parseInt(d3.select("#mysvg").attr("height"));
var img_w = 10;
var img_h = 10;
// 0 在指定的位置插入一个SVG元素
var svg = d3.select("#mysvg");

var index0 = 5;
var index1 = 7;
var index2 = 8;

// 1 读入JSON文件
d3.json("../json/graph.json", function (error, root) {
    if (error) {
        return console.log(error);
    }
    //console.log(root);

    // 2 定义力学图的布局
    var force = d3.layout.force()
                    .nodes(root.nodes)
                    .links(root.edges)
                    .size([width, height])
                    .linkDistance(100)
                    .charge(-800)
                    .start();
    // 3.1 绘制连接线
    var edges_line = svg.selectAll("line")
                        .data(root.edges)
                        .enter()
                        .append("line")
                        .style("stroke", function(d, i){
                            if (d.source.id == index1 && d.target.id == index2 || d.source.id == index2&&d.target.id == index1)
                                return "#F88"
                            else
                                return "#ccc";
                        })
                        .style("stroke-width", 2);
    // 3.2 绘制直线上的文字
    var edges_text = svg.selectAll(".linetext")
                        .data(root.edges)
                        .enter()
                        .append("text")
                        .attr("class", "linetext")
                        .text(function (d) {
                            return d.weight;
                        });
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
                        .attr("r", 10)
                        .attr("id", function (d, i) {
                            return d.id;
                        })
                        .style("fill", function (d, i) {
                            return color(i);
                        })
                        .on("mouseover", function (d, i) {
                            //显示结点上的文字
                            nodes_text.style("fill-opacity", function (node) {
                                if (node.name === d.name)
                                    return 1;
                            })
                        })
                        .on("mouseout", function (d, i) {
                            //隐去结点上的文字
                            nodes_text.style("fill-opacity", function (node) {
                                if (node.name === d.name)
                                    return 0;
                            })
                        })
                        .on("dblclick", function (d, i) {
                            d.fixed = false;
                        })
                        .call(force.drag);

    var text_dx = -10;
    var text_dy = 10;
    // 5 绘制结点标签
    var nodes_text = svg.selectAll(".nodetext")
                        .data(root.nodes)
                        .enter()
                        .append("text")
                        .attr("class", "nodetext")
                        .attr("dx", text_dx)
                        .attr("dy", text_dy)
                        .text(function (d) {
                            return d.name;
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

        //更新连接线上文字的位置
        edges_text.attr("x", function (d) { return (d.source.x + d.target.x) / 2; });
        edges_text.attr("y", function (d) { return (d.source.y + d.target.y) / 2; });

        //更新结点和文字
        nodes.attr("cx", function (d) { return d.x; });
        nodes.attr("cy", function (d) { return d.y; });
        nodes_text.attr("x", function (d) { return d.x });
        nodes_text.attr("y", function (d) { return d.y + img_w / 2; });        
    });    
    
});


//console.log(d3.select("#1"));
   

