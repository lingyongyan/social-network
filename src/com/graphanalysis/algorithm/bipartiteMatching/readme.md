Call Method:
		GraphInterface g = new Graph(GraphReader.readFromFile(
				"match.txt", 3));
		AlgorithmInterface algorithm = null;
		algorithm = new BipartiteMatching(g);
		((BipartiteMatchingInterface)algorithm).exec();