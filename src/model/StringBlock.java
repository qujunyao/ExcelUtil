package model;

import java.io.Serializable;

/**
 * �ַ�����
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
	 * ��ʼ�±�
	 */
	private Integer start;
	/**
	 * �����±�
	 */
	private Integer end;
	/**
	 * �ı�����
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
