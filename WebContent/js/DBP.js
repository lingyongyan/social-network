//var width = parseInt(d3.select("#table").attr("width"));
//var height = parseInt(d3.select("#table").attr("height"));

var svg = d3.select("#graph");


function DFS(){
	
   d3.json("../json/dfs.json", function (error, root) {
    if (error) {
        return console.log(error);
    }
    console.log(root);
	
	var click_id;
	
	var nodes = svg.selectAll("circle")
	               .on("dblclick", function (d, i) {
                        click_id = d.id;
                    });

    // 重绘连接线
    var edges_line = svg.selectAll("line");
    	
    for(var i = 0; i < edges_line[0].length; i++){
	    edges_line[0][i].style.stroke = "#ccc";
		for(var j = 0; j < root.length; j++){
			if(edges_line[0][i].__data__.source.id == root[j].source && edges_line[0][i].__data__.target.id == root[j].target){
				//setInterval(function(){}, 10000);
				edges_line[0][i].style.stroke = "red";
				//edges_line[0][i].style.stroke = "red";
				//edges_line[0][i].innerHTML = "<animate attributeName= \"x1\" attributeType=\"auto\" \
				                     //              begin=\"0s\" dur=\"5s\" from= x1 to= x2 /> ";
				
				/*edges_line[0][i].enter().append("animate")
				                .attr("begin", "3s")
								.attr("dur", "3s");*/
			}
		}
	}
});
	
}



function BFS(){
	
	d3.json("../json/bfs.json", function (error, root) {
    if (error) {
        return console.log(error);
    }
    console.log(root);

    // 重绘连接线
    var edges_line = svg.selectAll("line");
    	
    for(var i = 0; i < edges_line[0].length; i++){
		edges_line[0][i].style.stroke = "#ccc";
		for(var j = 0; j < root.length; j++){
			if(edges_line[0][i].__data__.source.id == root[j].source && edges_line[0][i].__data__.target.id == root[j].target){
				edges_line[0][i].style.stroke = "red";
			}
		}
	}
});
	
}

function Prim(){
	
	d3.json("../json/prim.json", function (error, root) {
    if (error) {
        return console.log(error);
    }
    console.log(root);

    // 重绘连接线
    var edges_line = svg.selectAll("line");
    	
    for(var i = 0; i < edges_line[0].length; i++){
		edges_line[0][i].style.stroke = "#ccc";
		for(var j = 0; j < root.length; j++){
			if(edges_line[0][i].__data__.source.id == root[j].source && edges_line[0][i].__data__.target.id == root[j].target){
				edges_line[0][i].style.stroke = "red";
			}
		}
	}
});
	
}