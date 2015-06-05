package com.graphanalysis.web.com;

public class ParameterObject {
	private int MinCount;
	private int MaxCount;

	public ParameterObject(int MaxCount, int MinCount) {
		this.MaxCount = MaxCount;
		this.MinCount = MinCount;
	}

	public int getMinCount() {
		return MinCount;
	}

	public void setMinCount(int minCount) {
		MinCount = minCount;
	}

	public int getMaxCount() {
		return MaxCount;
	}
	
	public void setMaxCount(int maxCount) {
		MaxCount = maxCount;
	}
}
