///////////////////////////////////////////////////////
//                           Graph页入口                           //
///////////////////////////////////////////////////////
// Graph.html页面元素加载完毕后，
// 开始进入这里进行加载（图数据集）
// 1.渲染 图区
// 2.渲染 度分布图区
// 3.渲染 邻接链表邻接矩阵区
(function () {
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
                    echarts = ec;
            });
        // 首次加载graph页，
        // 先问问后台有哪些数据集{ Array }：
        /*     零：用户没有上传，后台返回 [ 默认测试集名 ]         
        [
                "test"
        ]        
        //      壹：用户有上传，后台返回 [ 用户上传的数据集名 ]+
        [
                "userdata0",
                "userdata1"
        ]
        */
        $.get("../json/dataSets.json", {/*该项请求协议*/ }, function (dataSets, status) {
                if (status != "success") {
                        ///////////////////////////////////////////////////////
                        //                  提醒用户没有加载成功                      //
                        ///////////////////////////////////////////////////////
                        danger("加载用户数据集失败！");
                        return console.log(status);
                }
                ///////////////////////////////////////////////////////
                //                    使用返回的dataSets                       //
                //             !!更新用户数据集选项卡区域!!                //
                ///////////////////////////////////////////////////////

                // 只用渲染第一个数据集
                crntDataSet = dataSets[0];

                loadDataSet(crntDataSet);
        });
})()

