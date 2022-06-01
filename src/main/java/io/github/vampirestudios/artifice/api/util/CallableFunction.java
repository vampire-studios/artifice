package io.github.vampirestudios.artifice.api.util;

public interface CallableFunction<A, B> {
	B get(A a) throws Exception;
}