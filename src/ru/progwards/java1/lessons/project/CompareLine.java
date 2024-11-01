package ru.progwards.java1.lessons.project;

public class CompareLine {
	
	private Line srcLine;
	private Line pushLine;
	AnchorLine anchorLine;
	private String patch;
	private String end;
	
	public CompareLine(Line line1, Line line2) {
		srcLine = line1;
		pushLine = line2;
	}
	
	public CompareLine(AnchorLine anchor) {
		anchorLine = anchor;
	}
	
	public Line getSrcLine() {
		return srcLine;
	}
	
	public Line getPushLine() {
		return pushLine;
	}
	
	public AnchorLine getAnchorLine() {
		return anchorLine;
	}
	
	public boolean hasAnchorLine() {
		if(anchorLine != null)
			return true;
		return false;
	}
	
	public void setPatch(String patch) {
		this.patch = patch;
	}
	
	public boolean hasPatch() {
		if(patch != null)
			return true;
		return false;
	}
	
	public void removePatch() {
		patch = null;
	}
	
	public void setEnd(String endValue){
		end = endValue;
	}
	
	public boolean hasEnd() {
		if(end != null)
			return true;
		return false;
	}

	@Override
	public String toString() {
		if(anchorLine != null)
			return anchorLine.toString();
		else
			return srcLine.toString() +	pushLine.toString();
	}
}
