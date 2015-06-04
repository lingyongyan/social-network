///////////////////////////////////////////////////////
//                   Graph页全局变量区                        //
///////////////////////////////////////////////////////
// 当前数据集
var crntDataSet = undefined;


///////////////////////////////////////////////////////
//                           Graph页入口                           //
///////////////////////////////////////////////////////
// Graph.html页面元素加载完毕后，
// 开始进入这里进行加载（图数据集）
// 1.渲染 图区
// 2.渲染 度分布图区
// 3.渲染 邻接链表邻接矩阵区
(function () {
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


// 加载指定数据集
function loadDataSet(theDataSet) {
        // 初始化一些全局变量
        crntDataSet = theDataSet;


        // 分三块加载渲染，
        // 之后分别设置一个就绪变量，图区是必须的；
        // 以便各子功能可以调用

        // 一、读入用户数据集之图JSON文件并渲染
        $.get("../json/graph.json", { whichDataSet: theDataSet, filename: "graph.json" }, paintGraph);
        // 二、读入用户数据集之度分布图JSON文件并渲染
        $.get("../json/degree.json", { whichDataSet: theDataSet, filename: "degree.json" }, paintDegree);
        // 三、读入用户数据集之邻接矩阵邻接链表JSON文件并渲染
        $.get("../json/graph.json", { whichDataSet: theDataSet, filename: "table.json" }, paintTable);

}






