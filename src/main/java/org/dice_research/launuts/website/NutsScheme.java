package org.dice_research.launuts.website;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class NutsScheme implements Serializable {
	private static final long serialVersionUID = 1L;

	public String uri;
	public String eurostatUri;
	public List<UniqueNutsEntity> nuts0 = new LinkedList<>();

	public NutsScheme(String uri) {
		this.uri = uri;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(uri);
		sb.append(" ");
		sb.append(eurostatUri);
		sb.append(" ");
		sb.append(nuts0.size());
		return sb.toString();
	}
}