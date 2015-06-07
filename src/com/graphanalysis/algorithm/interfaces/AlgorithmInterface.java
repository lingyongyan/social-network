package com.graphanalysis.algorithm.interfaces;

import com.graphanalysis.algorithm.implement.ExecParameter;
import com.graphanalysis.algorithm.implement.ExecReturn;

public interface AlgorithmInterface {
	public ExecReturn exec(ExecParameter args);
}
