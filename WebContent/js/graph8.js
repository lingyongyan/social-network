// 当前功能下的状态变量
// 起点索引
var index0 = -1;
// 终点索引
var index1 = -1;

// 重置结点
function resetNodes() {
        if (gNodes)
                gNodes.style("fill", function (d, i) {
                        return color(d.group);
                })
                        .on("mouseover", null)
                        .on("mouseout", null)
                        .on("click", null);
}
// 重置边
function resetEdges() {
        if (gEdges)
                gEdges.style("stroke", "#ccc");
}
//重置权重
function resetWeight() {
        if (gEdgeTexts)
                gEdgeTexts.text(function (d) { return d.weight; });
}

// 重绘边集，条数
function reDrawEdges(subEdges, ne) {
        if (subEdges)
                if (gEdges)
                        gEdges.style("stroke", function (d, i) {
                                for (var edgeIndex = 0; edgeIndex < ne; edgeIndex++) {
                                        if (d.source.index == subEdges[edgeIndex].source && d.target.index == subEdges[edgeIndex].target)
                                                return "#d22";
                                }
                                return "#ccc";
                        })
}
// 重绘结点 只有最大二分图匹配需要
function reDrawNodes(max2match) {
        if (max2match)
                if (gNodes)
                        gNodes.style("fill", function (d, i) {
                                for (var pair in max2match) {
                                        if (d.index == max2match[pair].source)
                                                return "#d22";
                                        if (d.index == max2match[pair].target)
                                                return "#22d";
                                }
                                return color(d.group);
                                //return "#eee";
                        })
}
// 重绘权重 只有网络最大流需要
function reDrawWeights(maxFlow) {
        if (maxFlow)
                if (gEdgeTexts)
                        gEdgeTexts.text(function (textData) {
                                for (var weightIndex in maxFlow) {
                                        if (textData.source.index == maxFlow[weightIndex].source && textData.target.index == maxFlow[weightIndex].target)
                                                return maxFlow[weightIndex].weight + "/" + textData.weight;
                                }
                                return textData.weight;
                        });
}
var FPS = 12; // 帧速率
var loop = true; // 循环播放
var cnt = 0; // 子集渲染比率
var playshow = null; // 当前播放的动画

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
        reDrawEdges(subEdges, cnt);
}

// 响应Random功能
// 选中一个点，在其他点上滑动，形成一条边则高亮该边
function Random() {
        if (!gReady) {
                ///////////////////////////////////////////////////////
                //            提醒用户主图区还未渲染完毕                  //
                ///////////////////////////////////////////////////////
                return;
        }
        // Random功能初始化
        index0 = -1;
        if (playshow) {
                clearInterval(playshow);
                playshow = null;
        }

        resetNodes();
        resetEdges();
        resetWeight();

        // 为结点绑定独有的事件响应函数
        gNodes.on("click", function (d, i) {
                if (d3.event.defaultPrevented) return;
                if (index0 >= 0) {
                        clearInterval(playshow);
                        playshow = null;
                        resetEdges();
                }

                index0 = d.index;
                $.get("../json/Random.json", { whichDataSet: crntDataSet, filename: "Random.json", id: index0 }, function (randomPaths, status) {
                        if (status != "success") {
                                ///////////////////////////////////////////////////////
                                //                  提醒用户没有加载成功                      //
                                ///////////////////////////////////////////////////////
                                return console.log(status);
                        }
                        cnt = 0;
                        FPS = 6;
                        loop = false;
                        playshow = setInterval(function () {
                                play(randomPaths, randomPaths.length);
                        }, 1000 / FPS); // 这里控制帧数
                });
        });

        // 重新绑定动画函数

}

// 广度优先搜索
function BFS() {
        if (!gReady) {
                ///////////////////////////////////////////////////////
                //            提醒用户主图区还未渲染完毕                  //
                ///////////////////////////////////////////////////////
                return;
        }
        // BFS功能初始化
        index0 = -1;
        if (playshow) {
                clearInterval(playshow);
                playshow = null;
        }

        resetNodes();
        resetEdges();
        resetWeight();

        // 为结点绑定独有的事件响应函数
        gNodes.on("click", function (d, i) {
                if (d3.event.defaultPrevented) return;
                if (index0 >= 0) {
                        clearInterval(playshow);
                        playshow = null;
                        resetEdges();
                }
                index0 = d.index;
                $.get("../json/BFS.json", { whichDataSet: crntDataSet, filename: "BFS.json", id: index0 }, function (bfsPaths, status) {
                        if (status != "success") {
                                ///////////////////////////////////////////////////////
                                //                  提醒用户没有加载成功                      //
                                ///////////////////////////////////////////////////////
                                return console.log(status);
                        }

                        cnt = 0;
                        FPS = 6;
                        loop = false;
                        playshow = setInterval(function () {
                                play(bfsPaths, bfsPaths.length);
                        }, 1000 / FPS); // 这里控制帧数

                });
        });

        // 重新绑定动画函数
}
function DFS() {
        if (!gReady) {
                ///////////////////////////////////////////////////////
                //            提醒用户主图区还未渲染完毕                  //
                ///////////////////////////////////////////////////////
                return;
        }
        // DFS功能初始化
        index0 = -1;
        if (playshow) {
                clearInterval(playshow);
                playshow = null;
        }

        resetNodes();
        resetEdges();
        resetWeight();

        // 为结点绑定独有的事件响应函数
        gNodes.on("click", function (d, i) {
                if (d3.event.defaultPrevented) return;
                if (index0 >= 0) {
                        clearInterval(playshow);
                        playshow = null;
                        resetEdges();
                }
                index0 = d.index;
                $.get("../json/dfs.json", { whichDataSet: crntDataSet, filename: "DFS.json", id: index0 }, function (dfsPaths, status) {
                        if (status != "success") {
                                ///////////////////////////////////////////////////////
                                //                  提醒用户没有加载成功                      //
                                ///////////////////////////////////////////////////////
                                return console.log(status);
                        }

                        cnt = 0;
                        FPS = 6;
                        loop = false;
                        playshow = setInterval(function () {
                                play(dfsPaths, dfsPaths.length);
                        }, 1000 / FPS); // 这里控制帧数
                });
        });

}

// 响应Dijkstra功能
function Dijkstra() {
        if (!gReady) {
                // 提醒用户图还没加载完毕
                return;
        }

        // Dijkstra功能初始化
        index0 = index1 = -1;
        if (playshow) {
                clearInterval(playshow);
                playshow = null;
        }
        resetNodes();
        resetEdges();
        resetWeight();

        // 重新为结点绑定点击事件
        gNodes.on("click", function (nodeClicked) {
                if (d3.event.defaultPrevented) return;

                if (index0 == nodeClicked.index)//点击了同一个
                        return;

                index0 = nodeClicked.index;
                resetEdges();
                // 向服务器发送包含选择点的Dijkstra请求
                //$.post("IO.html", { node: 5 }, function () {
                //后台准备好Dijkstra.json就可以画了
                //})
                // 从服务器取回结果
                $.get("../json/Dijkstra.json", { whichDataSet: crntDataSet, filename: "Dijkstra.json", id: index0 }, function (minPaths, status) {
                        //console.log(status);
                        if (status != "success") {
                                // 修改状态栏提示没有成功
                                alert("请重新选择！");
                                return;
                        }
                        // 取回结果后，给结点注册OVER和OUT事件函数
                        gNodes.on("mouseover", function (nodeMouseOver) {
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
                                        // 滑出自己不用响应
                                        if (nodeMouseOut.index == index0)
                                                return;
                                        gEdges.style("stroke", "#ccc")
                                })
                });

        });

}

function Prim() {
        if (!gReady) {
                ///////////////////////////////////////////////////////
                //            提醒用户主图区还未渲染完毕                  //
                ///////////////////////////////////////////////////////
                return;
        }
        // Prim功能初始化
        if (playshow) {
                clearInterval(playshow);
                playshow = null;
        }
        resetNodes();
        resetEdges();
        resetWeight();


        $.get("../json/prim.json", { whichDataSet: crntDataSet, filename: "Prim.json", id: index0 }, function (minTree, status) {
                if (status != "success") {
                        ///////////////////////////////////////////////////////
                        //                  提醒用户没有加载成功                      //
                        ///////////////////////////////////////////////////////
                        return console.log(status);
                }
                cnt = 0;
                FPS = 6;
                loop = false;
                playshow = setInterval(function () {
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
                return;
        }
        // 二分图功能初始化
        if (playshow) {
                clearInterval(playshow);
                playshow = null;
        }
        resetNodes();
        resetEdges();
        resetWeight();

        // 为结点绑定独有的事件响应函数
        $.get("../json/P2P.json", { whichDataSet: crntDataSet, filename: "P2P.json", id: index0 }, function (max2match, status) {
                if (status != "success") {
                        ///////////////////////////////////////////////////////
                        //                  提醒用户没有加载成功                      //
                        ///////////////////////////////////////////////////////
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
                return;
        }
        index0 = index1 = -1;
        if (playshow) {
                clearInterval(playshow);
                playshow = null;
        }
        resetNodes();
        resetEdges();
        resetWeight();

        // 重新为结点绑定点击事件
        // 当选中两个不同结点时，
        // 向后台发送请求
        // 成功后回调处理边特效的函数
        gNodes.on("click", function (d, i) {
                if (d3.event.defaultPrevented) return;
                // 选中第一个结点                        
                if (index0 < 0) {
                        resetEdges();
                        index0 = d.index;
                        // 提示用户选第二个点
                        return;
                }
                // 和前一个点一样          
                if (d.index == index0) {
                        // 提示用户选第二个点
                        return;
                }
                if (index1 < 0) {
                        index1 = d.index;
                        // 选中了一个和第一个不一样的点                       
                        // 向服务器发送包含选择点的最大流请求
                        //后台准备好最大流.json就可以画了
                        //})
                        $.get("../json/FordFulkerson.json", function (maxFlow, status) {
                                if (status != "success") {
                                        ///////////////////////////////////////////////////////
                                        //                  提醒用户没有加载成功                      //
                                        ///////////////////////////////////////////////////////
                                        return console.log(status);
                                }
                                // 取回结果后，修改边上的文字
                                reDrawEdges(maxFlow, maxFlow.length);
                                // 画完之后，可以把选择点清空，等待下一对儿幸运儿
                                index0 = index1 = -1;
                        });
                }
        })
}
// 响应Bridge 桥边检测功能
function Bridge() {
        if (!gReady) {
                ///////////////////////////////////////////////////////
                //            提醒用户主图区还未渲染完毕                  //
                ///////////////////////////////////////////////////////
                return;
        }
        // 桥边检测功能初始化
        if (playshow) {
                clearInterval(playshow);
                playshow = null;
        }
        resetNodes();
        resetEdges();
        resetWeight();

        // 为结点绑定独有的事件响应函数
        $.get("../json/Bridge.json", { whichDataSet: crntDataSet, filename: "Bridge.json" }, function (bridges, status) {
                if (status != "success") {
                        ///////////////////////////////////////////////////////
                        //                  提醒用户没有加载成功                      //
                        ///////////////////////////////////////////////////////
                        return console.log(status);
                }
                reDrawEdges(bridges, bridges.length);
        });

}

function search() {
        alert("搜索内容" + document.getElementById("search4").innerHTML);
}