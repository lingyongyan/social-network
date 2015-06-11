// 当前功能下的状态变量
// 起点索引
var index0 = -1;
// 终点索引
var index1 = -1;
// 动画帧速率
var FPS = 12;
// 循环播放
var loop = true;
// 集合中元素被渲染的计数器
var cnt = 0;
// 当前播放的动画
var playShow = null; 

function initGraphFunction8() {
        index0 = index1 = -1;
        if (playShow) {
                clearInterval(playShow);
                playShow = null;
        }
        resetNodes();
        resetEdges();
        resetWeights();
}


// 重绘边集，条数
function reDrawEdges(subEdges, ne) {
        if (subEdges.length!=0)
                if (!gEdges.empty())
                        gEdges.attr("class", function (theEdge) {
                                for (var edgeIndex = 0; edgeIndex < ne; edgeIndex++) {
                                        if (theEdge.source.index == subEdges[edgeIndex].source && theEdge.target.index == subEdges[edgeIndex].target
                                                || (!gType && (theEdge.source.index == subEdges[edgeIndex].target && theEdge.target.index == subEdges[edgeIndex].source)))
                                                return "edgeHighlight";
                                }
                                return "edge";
                        })
}
// 绘制彩虹桥路径
function reDrawRainbow(subEdges, ne) {
        if (subEdges.length != 0)
                if (!gEdges.empty())
                        gEdges.attr("class", function (theEdge, i) {
                                for (var edgeIndex = 0; edgeIndex < ne; edgeIndex++) {
                                        if (theEdge.source.index == subEdges[edgeIndex].source && theEdge.target.index == subEdges[edgeIndex].target
                                                || (!gType && (theEdge.source.index == subEdges[edgeIndex].target && theEdge.target.index == subEdges[edgeIndex].source)))
                                                return "edgeHighlight" /*+ i % 7*/;
                                }
                                return "edge";
                        })
}
// 重绘结点 只有最大二分图匹配需要
function reDrawNodes(max2match) {
        if (max2match.length!=0)
                if (!gNodes.empty())
                        gNodes.style("fill", function (theNode) {
                                for (var pair in max2match) {
                                        if (theNode.index == max2match[pair].source)
                                                return "#d22";
                                        if (theNode.index == max2match[pair].target)
                                                return "#22d";
                                }
                                return color(theNode.group);
                                //return "#eee";
                        })
}
// 重绘权重 只有网络最大流需要
function reDrawWeights(maxFlow) {
        if (maxFlow)
                if (gEdgeTexts)
                        gEdgeTexts.text(function (theText) {
                                for (var weightIndex in maxFlow) {
                                        if (theText.source.index == maxFlow[weightIndex].source && theText.target.index == maxFlow[weightIndex].target
                                                || (!gType && (theText.source.index == maxFlow[weightIndex].target && theText.target.index == maxFlow[weightIndex].source)))
                                                return maxFlow[weightIndex].weight + " / " + theText.weight;
                                }
                                return theText.weight;
                        });
}
// 实现动画绘制
function play(subEdges, ne) {
        // 动画条件控制
        if (cnt < ne) {
                cnt++;
        }
        else {
                if (loop)
                        cnt = 1;
                else
                        return;
        }
        //-------------------------分界线-----------------------------
        reDrawRainbow(subEdges, cnt);
}

// 响应Random功能
// 选中一个点，在其他点上滑动，形成一条边则高亮该边
function Random() {
        if (!gReady) {
                ///////////////////////////////////////////////////////
                //            提醒用户主图区还未渲染完毕                  //
                ///////////////////////////////////////////////////////         
                warning("图区尚未加载完毕，请等待！");
                return;
        }
        // Random功能初始化
        initGraphFunction8();

        info("请选择一个起点：");
        // 为结点绑定独有的事件响应函数
        gNodes.on("click", function (nodeClicked) {
                //console.log("before");
                if (d3.event.defaultPrevented) return;
                clearTimeout(clickTimeDelay);
                clickTimeDelay = setTimeout(function () {
                        console.log("click");
                        if (index0 >= 0) {
                                if (nodeClicked.index == index0) {//点击了同一个
                                        warning("？点击了同一个？");
                                        return;
                                }
                                clearInterval(playShow);
                                playShow = null;
                                resetEdges();
                        }

                        index0 = nodeClicked.index;
                        $.get("../json/Random.json", { whichDataSet: crntDataSet, filename: "Random.json", id: index0 }, function (randomPaths, status) {
                                if (status != "success") {
                                        ///////////////////////////////////////////////////////
                                        //                  提醒用户没有加载成功                      //
                                        ///////////////////////////////////////////////////////
                                        danger("网络不好，请重新请求！");
                                        return console.log(status);
                                }
                                cnt = 0;
                                FPS = 6;
                                loop = true;
                                playShow = setInterval(function () {
                                        play(randomPaths, randomPaths.length);
                                }, 1000 / FPS); // 这里控制帧数

                                setTimeout(function(){success("您还可以选择其他点：")}, 1000);

                        });

                }, constMaxClickTimeDelay);


        });

}

// 广度优先搜索
function BFS() {
        if (!gReady) {
                ///////////////////////////////////////////////////////
                //            提醒用户主图区还未渲染完毕                  //
                ///////////////////////////////////////////////////////
                warning("图区尚未加载完毕，请等待！");
                return;
        }
        // BFS功能初始化
        initGraphFunction8();

        info("请选择一个起点：");
        // 为结点绑定独有的事件响应函数
        gNodes.on("click", function (nodeClicked) {
                if (d3.event.defaultPrevented) return;
                clearTimeout(clickTimeDelay);
                clickTimeDelay = setTimeout(function () {
                        if (index0 >= 0) {
                                if (nodeClicked.index == index0) {//点击了同一个
                                        warning("？点击了同一个？");
                                        return;
                                }
                                clearInterval(playShow);
                                playShow = null;
                                resetEdges();
                        }

                        index0 = nodeClicked.index;
                        $.get("../json/BFS.json", { whichDataSet: crntDataSet, filename: "BFS.json", id: index0 }, function (bfsPaths, status) {
                                if (status != "success") {
                                        ///////////////////////////////////////////////////////
                                        //                  提醒用户没有加载成功                      //
                                        ///////////////////////////////////////////////////////
                                        danger("网络不好，请重新请求！");
                                        return console.log(status);
                                }

                                cnt = 0;
                                FPS = 6;
                                loop = false;
                                playShow = setInterval(function () {
                                        play(bfsPaths, bfsPaths.length);
                                }, 1000 / FPS); // 这里控制帧数

                                setTimeout(function () { success("您还可以选择其他点：") }, 1000);
                        });

                }, constMaxClickTimeDelay);
                
        });

}
function DFS() {
        if (!gReady) {
                ///////////////////////////////////////////////////////
                //            提醒用户主图区还未渲染完毕                  //
                ///////////////////////////////////////////////////////
                warning("图区尚未加载完毕，请等待！");
                return;
        }
        // DFS功能初始化
        initGraphFunction8();

        $('#tips').text("请选择一个起点：").addClass('green');
        setTimeout(function () {
                $('#tips').text("").removeClass('green');
        }, tipsLastTime);

        // 为结点绑定独有的事件响应函数
        gNodes.on("click", function (nodeClicked) {
                if (d3.event.defaultPrevented) return;
                clearTimeout(clickTimeDelay);
                clickTimeDelay = setTimeout(function () {
                        if (index0 >= 0) {
                                if (index0 == nodeClicked.index) {//点击了同一个
                                        $('#tips').text("？点击了同一个？").addClass('yellow');
                                        setTimeout(function () {
                                                $('#tips').text("").removeClass('yellow');
                                        }, tipsLastTime);
                                        return;
                                }
                                clearInterval(playShow);
                                playShow = null;
                                resetEdges();
                        }

                        index0 = nodeClicked.index;
                        $.get("../json/dfs.json", { whichDataSet: crntDataSet, filename: "DFS.json", id: index0 }, function (dfsPaths, status) {
                                if (status != "success") {
                                        ///////////////////////////////////////////////////////
                                        //                  提醒用户没有加载成功                      //
                                        ///////////////////////////////////////////////////////                                
                                        danger("网络不好，请重新请求！");
                                        return console.log(status);
                                }

                                cnt = 0;
                                FPS = 6;
                                loop = false;
                                playShow = setInterval(function () {
                                        play(dfsPaths, dfsPaths.length);
                                }, 1000 / FPS); // 这里控制帧数

                                setTimeout(function () { success("您还可以选择其他点：") }, 1000);
                        });

                }, constMaxClickTimeDelay);
                
        });

}

// 响应Dijkstra功能
function Dijkstra() {
        if (!gReady) {
                // 提醒用户图还没加载完毕
                warning("图区尚未加载完毕，请等待！");
                return;
        }

        // Dijkstra功能初始化
        initGraphFunction8();

        $('#tips').text("请选择一个起点：").addClass('green');
        setTimeout(function () {
                $('#tips').text("").removeClass('green');
        }, tipsLastTime);

        // 重新为结点绑定点击事件
        gNodes.on("click", function (nodeClicked) {
                if (d3.event.defaultPrevented) return;
                clearTimeout(clickTimeDelay);
                clickTimeDelay = setTimeout(function () {
                        $('#tips').text("滑动到其他点查看最短路径：").addClass('green');
                        setTimeout(function () {
                                $('#tips').text("").removeClass('green');
                        }, tipsLastTime);

                        if (nodeClicked.index == index0) {//点击了同一个
                                $('#tips').text("？点击了同一个？").addClass('yellow');
                                setTimeout(function () {
                                        $('#tips').text("").removeClass('yellow');
                                }, tipsLastTime);
                                return;
                        }

                        index0 = nodeClicked.index;
                        // 向服务器发送包含选择点的Dijkstra请求
                        //$.post("IO.html", { node: 5 }, function () {
                        //后台准备好Dijkstra.json就可以画了
                        //})
                        // 从服务器取回结果
                        $.get("../json/Dijkstra.json", { whichDataSet: crntDataSet, filename: "Dijkstra.json", id: index0 }, function (minPaths, status) {
                                //console.log(status);
                                if (status != "success") {
                                        // 修改状态栏提示没有成功                                
                                        danger("网络不好，请重新请求！");
                                        return;
                                }
                                // 取回结果后，给结点注册OVER和OUT事件函数
                                gNodes.on("mouseover", function (nodeMouseOver) {
                                        d3.select(this).attr("class", "nodeHighlight")
                                                .attr("r", function (nodeData) {
                                                        var radius = Math.log(nodeData.group) + minRadius + 2;
                                                        return radius > maxRadius ? maxRadius : radius;
                                                });
                                        // 滑到自己身上不用响应
                                        if (nodeMouseOver.index == index0)
                                                return;

                                        index1 = nodeMouseOver.index;
                                        var path = minPaths[index1];
                                        // 先把符合条件的边找出来放到一个集合中
                                        if (path)
                                                reDrawEdges(path, path.length);

                                })
                                        // 注册划出函数
                                        .on("mouseout", function (nodeMouseOut) {
                                                d3.select(this).attr("class", "node")
                                                        .attr("r", function (nodeData) {
                                                                var radius = Math.log(nodeData.group) + minRadius;
                                                                return radius > maxRadius ? maxRadius : radius;
                                                        });
                                                // 滑出自己不用响应
                                                if (nodeMouseOut.index == index0)
                                                        return;
                                                //gEdges.style("stroke", "#ccc");
                                                resetEdges();

                                        })
                        });

                }, constMaxClickTimeDelay);
                

        });

}

function Prim() {
        if (!gReady) {
                ///////////////////////////////////////////////////////
                //            提醒用户主图区还未渲染完毕                  //
                ///////////////////////////////////////////////////////
                warning("图区尚未加载完毕，请等待！");
                return;
        }
        // Prim功能初始化
        initGraphFunction8();

        $.get("../json/prim.json", { whichDataSet: crntDataSet, filename: "Prim.json", id: index0 }, function (minTree, status) {
                if (status != "success") {
                        ///////////////////////////////////////////////////////
                        //                  提醒用户没有加载成功                      //
                        ///////////////////////////////////////////////////////
                        danger("网络不好，请重新请求！");
                        return console.log(status);
                }
                cnt = 0;
                FPS = 6;
                loop = false;
                playShow = setInterval(function () {
                        play(minTree, minTree.length);
                }, 1000 / FPS); // 这里控制帧数
        });

}

// 响应Bipartite功能
function Bipartite() {
        if (!gReady) {
                ///////////////////////////////////////////////////////
                //            提醒用户主图区还未渲染完毕                  //
                ///////////////////////////////////////////////////////
                warning("图区尚未加载完毕，请等待！");
                return;
        }
        // 二分图功能初始化
        initGraphFunction8();

        // 为结点绑定独有的事件响应函数
        $.get("../json/P2P.json", { whichDataSet: crntDataSet, filename: "P2P.json", id: index0 }, function (max2match, status) {
                if (status != "success") {
                        ///////////////////////////////////////////////////////
                        //                  提醒用户没有加载成功                      //
                        ///////////////////////////////////////////////////////
                        danger("网络不好，请重新请求！");
                        return console.log(status);
                }
                // 边集高亮
                reDrawEdges(max2match, max2match.length);

                // 画两个结点集
                reDrawNodes(max2match);

        });

}
// 响应Ford-Fulkerson功能
function FordFulkerson() {
        if (!gReady) {
                // 提醒用户图还没加载完毕
                warning("图区尚未加载完毕，请等待！");
                return;
        }

        initGraphFunction8();

        // 提示用户选一个源点
        info("请选择源点-=>：");

        // 重新为结点绑定点击事件
        // 当选中两个不同结点时，
        // 向后台发送请求
        // 成功后回调处理边特效的函数
        gNodes.on("click", function (nodeClicked) {
                if (d3.event.defaultPrevented) return;
                clearTimeout(clickTimeDelay);
                clickTimeDelay = setTimeout(function () {
                        if (index0 < 0) {
                                index0 = nodeClicked.index;
                                resetEdges();
                                resetWeight();
                                // 提示用户选第二个点
                                success("请选择==>汇点：");                                       
                        }
                        else {
                                if (nodeClicked.index == index0) {
                                        // 提示用户选第二个点
                                        warning("？点击了同一个？请选择==>汇点：");
                                        return;
                                }
                                index1 = nodeClicked.index;

                                $.get("../json/FordFulkerson.json", { whichDataSet: crntDataSet, filename: "maxFlow.json", id0: index0, id1: index1 }, function (maxFlow, status) {
                                        if (status != "success") {
                                                ///////////////////////////////////////////////////////
                                                //                  提醒用户没有加载成功                      //
                                                ///////////////////////////////////////////////////////
                                                danger("网络不好，请重新请求！");
                                                return console.log(status);
                                        }
                                        // 取回结果后，修改边上的文字
                                        reDrawEdges(maxFlow, maxFlow.length);
                                        reDrawWeights(maxFlow);
                                        // 提示用户选一个源点
                                        setTimeout(function () { success("您还可以选择其他源点==>：") }, 1000);
                                        index0 = index1 = -1;
                                });
                        }

                }, constMaxClickTimeDelay);
                
        });
}
// 响应Bridge 桥边检测功能
function Bridge() {
        if (!gReady) {
                ///////////////////////////////////////////////////////
                //            提醒用户主图区还未渲染完毕                  //
                ///////////////////////////////////////////////////////
                warning("图区尚未加载完毕，请等待！");
                return;
        }
        // 桥边检测功能初始化
        initGraphFunction8();

        // 为结点绑定独有的事件响应函数
        $.get("../json/Bridge.json", { whichDataSet: crntDataSet, filename: "Bridge.json" }, function (bridges, status) {
                if (status != "success") {
                        ///////////////////////////////////////////////////////
                        //                  提醒用户没有加载成功                      //
                        ///////////////////////////////////////////////////////
                        danger("网络不好，请重新请求！");
                        return console.log(status);
                }
                reDrawRainbow(bridges, bridges.length);
        });

}

function search4() {
        if (!gReady) {
                ///////////////////////////////////////////////////////
                //            提醒用户主图区还未渲染完毕                  //
                ///////////////////////////////////////////////////////
                warning("图区尚未加载完毕，请等待！");
                return;
        }
        // console.log(parseInt( $("#search4").val()));
        var value = $("#search4").val();

        var theNode = gNodes.select(function (nodeData, i) {
                return nodeData.name == value ? this : null;
        })

        if (theNode.empty())
                warning("没有找到您需要的信息！");


        theNode.attr("r", function (nodeData) {
                var radius = Math.log(nodeData.group) + minRadius + 6;
                return radius > maxRadius ? maxRadius : radius;
        }).attr("class", "nodeHighlight");



        setTimeout(function () {
                theNode.attr("r", function (nodeData) {
                        var radius = Math.log(nodeData.group) + minRadius;
                        return radius > maxRadius ? maxRadius : radius;
                }).attr("class", "node");
        }, tipsLastTime);

        $("#search4").val('请输入您想查找的信息');
}