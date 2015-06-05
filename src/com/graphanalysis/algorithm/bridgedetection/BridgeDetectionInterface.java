package com.graphanalysis.algorithm.bridgedetection;

import com.graphanalysis.algorithm.interfaces.AlgorithmInterface;
import com.graphanalysis.graphbase.implement.Graph;

public interface BridgeDetectionInterface extends AlgorithmInterface {
	public  int exec(Bridge br, Graph myGraph) throws Exception;
}
