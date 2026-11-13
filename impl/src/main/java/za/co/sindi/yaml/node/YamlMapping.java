/**
 * 
 */
package za.co.sindi.yaml.node;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import za.co.sindi.yaml.YamlBoolean;
import za.co.sindi.yaml.YamlNode;
import za.co.sindi.yaml.YamlNull;
import za.co.sindi.yaml.YamlNumber;
import za.co.sindi.yaml.YamlSequence;
import za.co.sindi.yaml.YamlString;

/**
 * @author Buhake Sindi
 * @since 07 November 2026
 */
public final class YamlMapping extends AbstractMap<String, YamlNode> implements za.co.sindi.yaml.YamlMapping {

	private final Map<String, YamlNode> entries;

	/**
	 * @param entries
	 */
	public YamlMapping(Map<String, YamlNode> entries) {
		super();
		this.entries = Collections.unmodifiableMap(new LinkedHashMap<>(entries));
	}

	@Override
	public YamlSequence getYamlSequence(String name) {
		// TODO Auto-generated method stub
		return (YamlSequence) entries.get(name);
	}

	@Override
	public za.co.sindi.yaml.YamlMapping getYamlMapping(String name) {
		// TODO Auto-generated method stub
		return (za.co.sindi.yaml.YamlMapping) entries.get(name);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <X extends Number> YamlNumber<X> getYamlNumber(String name) {
		// TODO Auto-generated method stub
		return (YamlNumber<X>) entries.get(name);
	}

	@Override
	public YamlString getYamlString(String name) {
		// TODO Auto-generated method stub
		return (YamlString) entries.get(name);
	}

	@Override
	public YamlBoolean getYamlBoolean(String name) {
		// TODO Auto-generated method stub
		return (YamlBoolean) entries.get(name);
	}

	@Override
	public String getString(String name) {
		// TODO Auto-generated method stub
		return getYamlString(name).value();
	}

	@Override
	public String getString(String name, String defaultValue) {
		// TODO Auto-generated method stub
		YamlNode node = get(name);
		if (node instanceof YamlString string) {
			return string.value();
		}
		return defaultValue;
	}

	@Override
	public int getInt(String name) {
		// TODO Auto-generated method stub
		YamlNumber<Integer> number = getYamlNumber(name);
		return number.value();
	}

	@Override
	public int getInt(String name, int defaultValue) {
		// TODO Auto-generated method stub
		YamlNode node = get(name);
		if (node instanceof YamlNumber number) {
			return (Integer)number.value();
		}
		return defaultValue;
	}

	@Override
	public boolean getBoolean(String name) {
		// TODO Auto-generated method stub
		return getYamlBoolean(name).value();
	}

	@Override
	public boolean getBoolean(String name, boolean defaultValue) {
		// TODO Auto-generated method stub
		YamlNode node = get(name);
		if (node instanceof YamlBoolean bool) {
			return bool.value();
		}
		return defaultValue;
	}

	@Override
	public boolean isNull(String name) {
		// TODO Auto-generated method stub
		YamlNode node = get(name);
		return node instanceof YamlNull;
	}

	@Override
	public Set<Entry<String, YamlNode>> entrySet() {
		// TODO Auto-generated method stub
		return entries.entrySet();
	}
}
