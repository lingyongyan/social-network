//var echarts = null; // echarts命名空间
var degree2 = null; // 度图分布实例

// 度分布图模板
var dOption = {
        title: {
                text: '度数分布图',
                subtext: '孙家栋',
                x: 'right',
                y:'bottom'
        },
        tooltip: {
                trigger: 'item'
        },
        legend: {
                data: ['度数'],
                x: 'center',
                y: 'top'               
        },
        toolbox: {
                show: true,
                feature: {
                        mark: { show: true },
                        dataZoom: { show: true },
                        dataView: { show: true },
                        //magicType: { show: true, type: ['line'] },
                        restore: { show: true },
                        saveAsImage: { show: true }
                }
        },
        calculable: true,
        dataZoom: {
                show: true,
                realtime: true
        },
        xAxis: [
            {
                    type: 'category',
                    boundaryGap: true,
                    data: [1, 2, 4, 8, 16, 32, 64, 128, 256]
            }
        ],
        yAxis: [
            {
                    type: 'value'
            }
        ],
        series: [
            {
                    name: '出度',
                    type: 'line',
                    data: [256, 128, 64, 32, 16, 8, 4, 2, 1]
            },
            {
                    name: '入度',
                    type: 'line',
                    data: [256+2, 128-2, 64+2, 32-2, 16+2, 8-2, 4+2, 2-2, 1+2]
            }
        ]
};


// 画度分布图
function paintDegree(degreeData, status) {

        if (status != "success") {
                ///////////////////////////////////////////////////////
                //                  提醒用户没有加载成功                      //
                ///////////////////////////////////////////////////////
                return console.log(status);
        }
        // TODO
        // Step:3 conifg ECharts's path, link to echarts.js from current page.
        // Step:3 为模块加载器配置echarts的路径，从当前页面链接到echarts.js，定义所需图表路径
        require.config({
                paths: {
                        echarts: '../js'
                }
        });

        // Step:4 require echarts and use it in the callback.
        // Step:4 动态加载echarts然后在回调函数中开始使用，注意保持按需加载结构定义图表路径
        require(
            [
                'echarts',
                'echarts/chart/line',
            ],
            function (ec) {
                    //echarts = ec;
                    degree2 = ec.init(document.getElementById('degree2'));
                    // 接下来是将后台返回的度分布图数据
                    // 组装成echats支持的格式
                    
                    if (degreeData.length == 3) {
                            dOption.legend.data[0] = "出度";
                            dOption.legend.data[1] = "入度";

                            dOption.series[0].data = degreeData[1];
                            dOption.series[1].data = degreeData[2];
                    }
                    else
                            dOption.series[0].data = degreeData[1];
                    dOption.xAxis[0].data = degreeData[0];
                    degree2.setOption(dOption);
            }//回调函数
        );
        
}
