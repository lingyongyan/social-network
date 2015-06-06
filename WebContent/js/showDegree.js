
var degree2 = null; // 度图分布实例

// 度分布图模板
var dOption = {
        title: {
                text: '度分布图',
                subtext: '纯属虚构'
        },
        tooltip: {
                trigger: 'axis'
        },
        legend: {
                data: ['最高气温', '最低气温']
        },
        toolbox: {
                show: true,
                feature: {
                        mark: { show: true },
                        dataView: { show: true, readOnly: false },
                        magicType: { show: true, type: ['line', 'bar'] },
                        restore: { show: true },
                        saveAsImage: { show: true }
                }
        },
        calculable: true,
        xAxis: [
            {
                    type: 'category',
                    boundaryGap: false,
                    data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
            }
        ],
        yAxis: [
            {
                    type: 'value',
                    axisLabel: {
                            formatter: '{value} °C'
                    }
            }
        ],
        series: [
            {
                    name: '最高气温',
                    type: 'line',
                    data: [11, 11, 15, 13, 12, 13, 10],
                    markPoint: {
                            data: [
                                { type: 'max', name: '最大值' },
                                { type: 'min', name: '最小值' }
                            ]
                    },
                    markLine: {
                            data: [
                                { type: 'average', name: '平均值' }
                            ]
                    }
            },
            {
                    name: '最低气温',
                    type: 'line',
                    data: [1, -2, 2, 5, 3, 2, 0],
                    markPoint: {
                            data: [
                                { name: '周最低', value: -2, xAxis: 1, yAxis: -1.5 }
                            ]
                    },
                    markLine: {
                            data: [
                                { type: 'average', name: '平均值' }
                            ]
                    }
            }
        ]
};


        
var dChart;


function eChart(ec) {
        
        //--- 折线图 ---

        
        $.get("../json/degree.json", function (root,status){
                if (status != "success") {
                        // 提示用户没有拿到图
                        return console.log(error);
                }

                for(var i=0;i<root.degrees.length;i++){
                        option2.xAxis[0].data[i] = root.degrees[i].degree;
                        option2.series[0].data[i] = root.numbers[i].number;
                }
                option2.legend.data[0] = root.legend[0].types;
                option2.legend.data[1] = root.legend[1].types;
                degree2.hideLoading();
                degree2.setOption(option2);
            
        });

}
// 画度分布图
function paintDegree(degree, status) {

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
}
