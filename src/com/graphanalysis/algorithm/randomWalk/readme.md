Call Method:
		GraphInterface g = new RandomWalkGraph(GraphReader.readFromFile(
				"tinyG.txt", 3));
		AlgorithmInterface algorithm = null;
		algorithm = new RandomWalk(g);
		((RandomWalkInterface)algorithm).exec(1000);//传入参数为行走步骤数 