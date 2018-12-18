package model;

import java.io.Serializable;

/**
 * 字符块类
 * 
 * @author Administrator
 * 
 */
public class StringBlock implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4265001324559009084L;
	/**
	 * 开始下标
	 */
	private Integer start;
	/**
	 * 结束下标
	 */
	private Integer end;
	/**
	 * 文本内容
	 */
	private String content;

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "StringBlock [start=" + start + ", end=" + end + ", content=" + content + "]";
	}

}
