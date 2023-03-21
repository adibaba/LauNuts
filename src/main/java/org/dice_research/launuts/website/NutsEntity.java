package org.dice_research.launuts.website;

import java.util.LinkedList;
import java.util.List;

public class NutsEntity {

	public String uri;
	public List<NutsEntity> narrower = new LinkedList<>();

	public NutsEntity(String uri, NutsScheme nutsScheme) {
		this.uri = uri;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(uri);
		sb.append(" ");
		sb.append(narrower.size());
		return sb.toString();
	}
}