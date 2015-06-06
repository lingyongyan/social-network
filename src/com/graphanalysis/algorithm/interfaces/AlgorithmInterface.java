package com.graphanalysis.algorithm.interfaces;

import com.graphAnalysis.algorithm.implement.ExecParameter;
import com.graphAnalysis.algorithm.implement.ExecReturn;

public interface AlgorithmInterface {
	public ExecReturn exec(ExecParameter args);
}
