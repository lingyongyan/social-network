package com.graphanalysis.algorithm.bridgedetection;

import com.graphanalysis.algorithm.interfaces.AlgorithmInterface;

public interface BridgeDetectionInterface extends AlgorithmInterface {
	public  int exec(Bridge br,String fileName) throws Exception;
}
