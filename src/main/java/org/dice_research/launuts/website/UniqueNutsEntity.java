package org.dice_research.launuts.website;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class UniqueNutsEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public String uri;

	public String nutsEntityUri;
	public String code;
	public String label;
	public String related;
	public List<UniqueNutsEntity> narrowerNuts = new LinkedList<>();
	public List<UniqueLauEntity> lau = new LinkedList<>();

	public UniqueNutsEntity(String uri) {
		this.uri = uri;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(uri);
		sb.append(" ");
		sb.append(nutsEntityUri);
		sb.append(" ");
		sb.append(code);
		sb.append(" ");
		sb.append(label);
		sb.append(" ");
		sb.append(related);
		sb.append(" ");
		sb.append(narrowerNuts.size());
		return sb.toString();
	}
}