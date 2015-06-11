///////////////////////////////////////////////////////
//                   Graph页全局变量区                        //
///////////////////////////////////////////////////////
// 当前数据集
var crntDataSet = null;
// 当前组
var crntGroup = undefined;

// 加载指定数据集
//function loadDataSet(theDataSet) {
//        // 初始化一些全局变量
//        crntDataSet = theDataSet;

//        // 分三块加载渲染，
//        // 之后分别设置一个就绪变量，图区是必须的；
//        // 以便各子功能可以调用

//        // 一、读入用户数据集之图JSON文件并渲染
//        $.get("../json/graph.json", { whichDataSet: theDataSet, filename: "graph.json"}, paintGraph);
//        // 二、读入用户数据集之度分布图JSON文件并渲染
//        $.get("../json/degree.json", { whichDataSet: theDataSet, filename: "degree.json"}, paintDegree);
//        // 三、读入用户数据集之邻接矩阵邻接链表JSON文件并渲染
//        $.get("../json/graph.json", { whichDataSet: theDataSet, filename: "graph.json"}, paintTable);

//}

// 加载指定数据集
function loadDataSet(theDataSet) {
        // 初始化一些全局变量
        crntDataSet = theDataSet;

        // 分三块加载渲染，
        // 之后分别设置一个就绪变量，图区是必须的；
        // 以便各子功能可以调用

        // 一、读入用户数据集之图JSON文件并渲染
        $.get("../json/graph.json", { whichDataSet: theDataSet, filename: "graph.json"}, paintGraph);
        // 二、读入用户数据集之度分布图JSON文件并渲染
        $.get("../json/degree.json", { whichDataSet: theDataSet, filename: "degree.json" }, paintDegree);
        // 三、读入用户数据集之邻接矩阵邻接链表JSON文件并渲染
        $.get("../json/graph.json", { whichDataSet: theDataSet, filename: "graph.json" }, paintTable);

}