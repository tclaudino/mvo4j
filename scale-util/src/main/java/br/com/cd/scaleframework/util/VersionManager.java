package br.com.cd.scaleframework.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class VersionManager {

	private Set<Integer> tokens = new HashSet<Integer>();

	public VersionManager(String token) {
		this(ParserUtils.parserAll(Integer.class, token.split(".")));
	}

	public VersionManager(Integer... versions) {
		tokens.addAll(Arrays.asList(versions));
	}

	public boolean isMajor(String token) {
		return this.isMajor(ParserUtils.parserAll(Integer.class,
				token.split(".")));
	}

	public boolean isMajor(Integer... tokens) {
		VersionManager otherManager = new VersionManager(tokens);

		Iterator<Integer> iterator = this.tokens.iterator();
		Iterator<Integer> otherIterator = otherManager.tokens.iterator();
		while (iterator.hasNext()) {
			Integer version = iterator.next();
			if (otherIterator.hasNext()) {
				if (otherIterator.next() > version) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean isMajor(String token1, String token2) {
		return new VersionManager(token1).isMajor(token2);
	}
}
