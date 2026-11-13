/**
 * 
 */
package za.co.sindi.yaml.node;

import java.util.AbstractList;
import java.util.List;

import za.co.sindi.yaml.YamlBoolean;
import za.co.sindi.yaml.YamlMapping;
import za.co.sindi.yaml.YamlNode;
import za.co.sindi.yaml.YamlNumber;
import za.co.sindi.yaml.YamlString;

/**
 * @author Buhake Sindi
 * @since 07 November 2026
 */
public final class YamlSequence extends AbstractList<YamlNode> implements za.co.sindi.yaml.YamlSequence {

	private final List<YamlNode> values;
	
	public YamlSequence(final List<YamlNode> values) {
		this.values = List.copyOf(values);
	}

	@Override
	public YamlMapping getYamlMapping(int index) {
		// TODO Auto-generated method stub
		return (YamlMapping) values.get(index);
	}

	@Override
	public za.co.sindi.yaml.YamlSequence getYamlSequence(int index) {
		// TODO Auto-generated method stub
		return (za.co.sindi.yaml.YamlSequence) values.get(index);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <X extends Number> YamlNumber<X> getYamlNumber(int index) {
		// TODO Auto-generated method stub
		return (YamlNumber<X>) values.get(index);
	}

	@Override
	public YamlString getYamlString(int index) {
		// TODO Auto-generated method stub
		return (YamlString) values.get(index);
	}

	@Override
	public YamlBoolean getYamlBoolean(int index) {
		// TODO Auto-generated method stub
		return (YamlBoolean) values.get(index);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends YamlNode> List<T> getValuesAs(Class<T> clazz) {
		// TODO Auto-generated method stub
		return (List<T>) values;
	}

	@Override
	public String getString(int index) {
		// TODO Auto-generated method stub
		return getYamlString(index).value();
	}

	@Override
	public String getString(int index, String defaultValue) {
		// TODO Auto-generated method stub
		YamlNode node = get(index);
		if (node instanceof YamlString string) return string.value();
		return defaultValue;
	}

	@Override
	public int getInt(int index) {
		// TODO Auto-generated method stub
		YamlNumber<Integer> number = getYamlNumber(index);
		return number.value();
	}

	@Override
	public int getInt(int index, int defaultValue) {
		// TODO Auto-generated method stub
		YamlNode node = get(index);
		if (node instanceof YamlNumber number) return (Integer)number.value();
		return defaultValue;
	}

	@Override
	public boolean getBoolean(int index) {
		// TODO Auto-generated method stub
		return getYamlBoolean(index).value();
	}

	@Override
	public boolean getBoolean(int index, boolean defaultValue) {
		// TODO Auto-generated method stub
		YamlNode node = get(index);
		if (node instanceof YamlBoolean bool) return bool.value();
		return defaultValue;
	}

	@Override
	public boolean isNull(int index) {
		// TODO Auto-generated method stub
		YamlNode node = get(index);
		return node instanceof YamlNull;
	}

	@Override
	public YamlNode get(int index) {
		// TODO Auto-generated method stub
		return values.get(index);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return values.size();
	}
}
