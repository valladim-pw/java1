package ru.progwards.java1.lessons.project;

public class CompareLine {
	
	private Line srcLine;
	private Line pushLine;
	private AnchorLine anchorLine;
	private String end;
	private String stop;
	private String patch;
	
	public Line getSrcLine() {
		return srcLine;
	}
	public Line getPushLine() {
		return pushLine;
	}
	
	public AnchorLine getAnchorLine() {
		return anchorLine;
	}
	
	public CompareLine(Line line1, Line line2) {
		srcLine = line1;
		pushLine = line2;
	}
	
	public CompareLine(AnchorLine anchor) {
		anchorLine = anchor;
	}
	public void setPushLine(Line pushLine) {
		this.pushLine = pushLine;
	}
	
	public void setEnd(String end){
		this.end = end;
	}
	
	public void setStop(String stop){
		this.stop = stop;
	}
	
	public void setPatch(String patch) {
		this.patch = patch;
	}
	
	public void removePatch(){
		this.patch = null;
	}
	
	public String getEnd(){
		return end;
	}
	
	public String getStop(){
		return stop;
	}
	
	public String getPatch() {
		return patch;
	}
	
	@Override
	public String toString() {
		if(anchorLine != null)
			return anchorLine.toString();
		else
			return srcLine.toString() +	pushLine.toString();
	}
}
