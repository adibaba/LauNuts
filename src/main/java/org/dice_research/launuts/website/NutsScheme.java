package org.dice_research.launuts.website;

import java.util.LinkedList;
import java.util.List;

public class NutsScheme {

	public String uri;
	public List<NutsEntity> nuts0 = new LinkedList<>();

	public NutsScheme(String uri) {
		this.uri = uri;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(uri);
		sb.append(" ");
		sb.append(nuts0.size());
		return sb.toString();
	}
}