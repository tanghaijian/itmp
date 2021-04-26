package cn.pioneeruniverse.common.bean;

/**
 * excel合并单元格信息
 * 
 * @author fanwentao
 *
 */
public class MergedRegionResult {
	private boolean merged;   //是否为合并单元格
	
	private int startRow;    //单元格开始行
	
	private int endRow;		//单元格结束行
	
	private int startCol;	//单元格开始列
	
	private int endCol;		//单元格结束列
	
	private String value;  //单元格值
	
	public MergedRegionResult() {}

	public MergedRegionResult(boolean merged, int startRow, int endRow, int startCol, int endCol, String value) {
		super();
		this.merged = merged;
		this.startRow = startRow;
		this.endRow = endRow;
		this.startCol = startCol;
		this.endCol = endCol;
		this.value = value;
	}

	public boolean isMerged() {
		return merged;
	}

	public void setMerged(boolean merged) {
		this.merged = merged;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	public int getStartCol() {
		return startCol;
	}

	public void setStartCol(int startCol) {
		this.startCol = startCol;
	}

	public int getEndCol() {
		return endCol;
	}

	public void setEndCol(int endCol) {
		this.endCol = endCol;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	

}
