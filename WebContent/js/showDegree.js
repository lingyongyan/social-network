/* 这是前端需要的度分布图数据JSON格式
无向图，数组中有两个元素
[
        [1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131070],

        [256, 128, 64, 32, 16, 8, 4, 2, 1, 1, 2, 4, 8, 16, 32, 64, 128, 256]
]

有向图，数组中有三个元素，

[
        [1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131070],

        [256, 128, 64, 32, 16, 8, 4, 2, 1, 1, 2, 4, 8, 16, 32, 64, 128, 256],

        [254, 126, 66, 30, 18, 6, 6, 5, 2, 256, 128, 64, 32, 16, 8, 4, 2, 1]
]
第一个元素表示横轴，是度数，
第二个元素是对应的结点个数，对于有向图是出度结点个数

第三个元素对于有向图是入度结点个数
元素的长度是一样的，一一对应

*/
var echarts = null;
var degree2 = null; // 度图分布实例

// 度分布图模板
var dOption = {
        title: {
                text: '度数分布图',
                subtext: '孙家栋',
                x: 'right',
                y: 'bottom'
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
                        //mark: { show: true },
                        dataZoom: { show: true },
                        dataView: { show: true },
                        //magicType: { show: true, type: ['line'] },
                        //restore: { show: true },
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
                    name: '度数',
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
        
                    
        degree2 = echarts.init(document.getElementById('degree2'));
        // 接下来是将后台返回的度分布图数据
        // 组装成echats支持的格式

        if (degreeData.length == 3) {//有向图
                dOption.legend.data[0] = "出度";
                dOption.legend.data.push("入度");

                dOption.series[0].data = degreeData[1];
                dOption.series[0].name = '出度';
                dOption.series[1].data = degreeData[2];
        }
        else {//无向图
                dOption.series[0].data = degreeData[1];
                dOption.series.pop(1);
        }

        dOption.xAxis[0].data = degreeData[0];
        degree2.setOption(dOption);


}
