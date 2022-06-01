package io.github.vampirestudios.artifice.api.resource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * A virtual resource representing an arbitrary string.
 */
public class StringResource implements ArtificeResource<String> {
	private final String data;

	/**
	 * @param lines Individual lines of the string this resource file contains.
	 */
	public StringResource(String... lines) {
		this.data = String.join("\n", lines);
	}

	public String getData() {
		return this.data;
	}

	public String toOutputString() {
		return this.data;
	}

	public InputStream toInputStream() {
		return new ByteArrayInputStream(this.data.getBytes());
	}
}
