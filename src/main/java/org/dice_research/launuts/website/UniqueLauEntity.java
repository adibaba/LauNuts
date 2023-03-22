package org.dice_research.launuts.website;

import java.io.Serializable;

public class UniqueLauEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public String uri;

	public String code;
	public String name;
	public String national;
	public Integer area;
	public Integer population;

	public UniqueLauEntity(String uri) {
		this.uri = uri;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(uri);
		return sb.toString();
	}
}