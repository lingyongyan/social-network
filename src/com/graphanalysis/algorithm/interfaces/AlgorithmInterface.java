package com.graphanalysis.algorithm.interfaces;

import com.graphanalysis.algorithm.implement.ExecParameter;
import com.graphanalysis.algorithm.implement.ExecReturn;

/**
 * @author Yan	Lingyong
 *所有图处理类必须实现的接口
 */
public interface AlgorithmInterface {
	public ExecReturn exec(ExecParameter args);
}
