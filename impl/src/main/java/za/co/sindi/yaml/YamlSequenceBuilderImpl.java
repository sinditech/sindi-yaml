/**
 * 
 */
package za.co.sindi.yaml;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import za.co.sindi.yaml.node.YamlBigDecimalNumber;
import za.co.sindi.yaml.node.YamlBigIntegerNumber;
import za.co.sindi.yaml.node.YamlBoolean;
import za.co.sindi.yaml.node.YamlDoubleNumber;
import za.co.sindi.yaml.node.YamlIntegerNumber;
import za.co.sindi.yaml.node.YamlLongNumber;
import za.co.sindi.yaml.node.YamlNull;
import za.co.sindi.yaml.node.YamlString;

/**
 * @author Buhake Sindi
 * @since 08 November 2026
 */
public class YamlSequenceBuilderImpl implements YamlSequenceBuilder {

	private List<YamlNode> sequenceList;

	/**
	 * 
	 */
	public YamlSequenceBuilderImpl() {
		super();
		// TODO Auto-generated constructor stub
		sequenceList = new ArrayList<>();
	}

	/**
	 * 
	 * @param sequence
	 */
	public YamlSequenceBuilderImpl(YamlSequence sequence) {
		sequenceList = new ArrayList<>();
		sequenceList.addAll(sequence);
    }

	/**
	 * 
	 * @param collection
	 */
	public YamlSequenceBuilderImpl(Collection<?> collection) {
		sequenceList = new ArrayList<>();
        populate(collection);
    }

	@Override
	public YamlSequenceBuilder add(YamlNode value) {
		// TODO Auto-generated method stub
		validateValue(value);
		sequenceList.add(value);
		return this;
	}

	@Override
	public YamlSequenceBuilder add(String value) {
		// TODO Auto-generated method stub
		validateValue(value);
		sequenceList.add(new YamlString(value));
		return this;
	}

	@Override
	public YamlSequenceBuilder add(BigDecimal value) {
		// TODO Auto-generated method stub
		validateValue(value);
		sequenceList.add(new YamlBigDecimalNumber(value));
		return this;
	}

	@Override
	public YamlSequenceBuilder add(BigInteger value) {
		// TODO Auto-generated method stub
		validateValue(value);
		sequenceList.add(new YamlBigIntegerNumber(value));
		return this;
	}

	@Override
	public YamlSequenceBuilder add(int value) {
		// TODO Auto-generated method stub
		sequenceList.add(new YamlIntegerNumber(value));
		return this;
	}

	@Override
	public YamlSequenceBuilder add(long value) {
		// TODO Auto-generated method stub
		sequenceList.add(new YamlLongNumber(value));
		return this;
	}

	@Override
	public YamlSequenceBuilder add(double value) {
		// TODO Auto-generated method stub
		sequenceList.add(new YamlDoubleNumber(value));
		return this;
	}

	@Override
	public YamlSequenceBuilder add(boolean value) {
		// TODO Auto-generated method stub
		sequenceList.add(new YamlBoolean(value));
		return this;
	}

	@Override
	public YamlSequenceBuilder addNull() {
		// TODO Auto-generated method stub
		sequenceList.add(new YamlNull());
		return this;
	}

	@Override
	public YamlSequenceBuilder add(YamlMappingBuilder builder) {
		// TODO Auto-generated method stub
		validateValue(builder);
		sequenceList.add(builder.build());
		return this;
	}

	@Override
	public YamlSequenceBuilder add(YamlSequenceBuilder builder) {
		// TODO Auto-generated method stub
		validateValue(builder);
		sequenceList.add(builder.build());
		return this;
	}

	@Override
	public YamlSequenceBuilder addAll(YamlSequenceBuilder builder) {
		// TODO Auto-generated method stub
		validateValue(builder);
		sequenceList.addAll(builder.build());
		return this;
	}

	@Override
	public YamlSequenceBuilder add(int index, YamlNode value) {
		// TODO Auto-generated method stub
		validateValue(value);
		sequenceList.add(index, value);
		return this;
	}

	@Override
	public YamlSequenceBuilder add(int index, String value) {
		// TODO Auto-generated method stub
		validateValue(value);
		sequenceList.add(index, new YamlString(value));
		return this;
	}

	@Override
	public YamlSequenceBuilder add(int index, BigDecimal value) {
		// TODO Auto-generated method stub
		validateValue(value);
		sequenceList.add(index, new YamlBigDecimalNumber(value));
		return this;
	}

	@Override
	public YamlSequenceBuilder add(int index, BigInteger value) {
		// TODO Auto-generated method stub
		validateValue(value);
		sequenceList.add(index, new YamlBigIntegerNumber(value));
		return this;
	}

	@Override
	public YamlSequenceBuilder add(int index, int value) {
		// TODO Auto-generated method stub
		sequenceList.add(index, new YamlIntegerNumber(value));
		return this;
	}

	@Override
	public YamlSequenceBuilder add(int index, long value) {
		// TODO Auto-generated method stub
		sequenceList.add(index, new YamlLongNumber(value));
		return this;
	}

	@Override
	public YamlSequenceBuilder add(int index, double value) {
		// TODO Auto-generated method stub
		sequenceList.add(index, new YamlDoubleNumber(value));
		return this;
	}

	@Override
	public YamlSequenceBuilder add(int index, boolean value) {
		// TODO Auto-generated method stub
		sequenceList.add(index, new YamlBoolean(value));
		return this;
	}

	@Override
	public YamlSequenceBuilder addNull(int index) {
		// TODO Auto-generated method stub
		sequenceList.add(index, new YamlNull());
		return this;
	}

	@Override
	public YamlSequenceBuilder add(int index, YamlMappingBuilder builder) {
		// TODO Auto-generated method stub
		validateValue(builder);
		sequenceList.add(index, builder.build());
		return this;
	}

	@Override
	public YamlSequenceBuilder add(int index, YamlSequenceBuilder builder) {
		// TODO Auto-generated method stub
		validateValue(builder);
		sequenceList.add(index, builder.build());
		return this;
	}

	@Override
	public YamlSequenceBuilder set(int index, YamlNode value) {
		// TODO Auto-generated method stub
		validateValue(value);
		sequenceList.set(index, value);
		return this;
	}

	@Override
	public YamlSequenceBuilder set(int index, String value) {
		// TODO Auto-generated method stub
		validateValue(value);
		sequenceList.set(index, new YamlString(value));
		return this;
	}

	@Override
	public YamlSequenceBuilder set(int index, BigDecimal value) {
		// TODO Auto-generated method stub
		validateValue(value);
		sequenceList.set(index, new YamlBigDecimalNumber(value));
		return this;
	}

	@Override
	public YamlSequenceBuilder set(int index, BigInteger value) {
		// TODO Auto-generated method stub
		validateValue(value);
		sequenceList.set(index, new YamlBigIntegerNumber(value));
		return this;
	}

	@Override
	public YamlSequenceBuilder set(int index, int value) {
		// TODO Auto-generated method stub
		sequenceList.set(index, new YamlIntegerNumber(value));
		return this;
	}

	@Override
	public YamlSequenceBuilder set(int index, long value) {
		// TODO Auto-generated method stub
		sequenceList.set(index, new YamlLongNumber(value));
		return this;
	}

	@Override
	public YamlSequenceBuilder set(int index, double value) {
		// TODO Auto-generated method stub
		sequenceList.set(index, new YamlDoubleNumber(value));
		return this;
	}

	@Override
	public YamlSequenceBuilder set(int index, boolean value) {
		// TODO Auto-generated method stub
		sequenceList.set(index, new YamlBoolean(value));
		return this;
	}

	@Override
	public YamlSequenceBuilder setNull(int index) {
		// TODO Auto-generated method stub
		sequenceList.set(index, new YamlNull());
		return this;
	}

	@Override
	public YamlSequenceBuilder set(int index, YamlMappingBuilder builder) {
		// TODO Auto-generated method stub
		validateValue(builder);
		sequenceList.set(index, builder.build());
		return this;
	}

	@Override
	public YamlSequenceBuilder set(int index, YamlSequenceBuilder builder) {
		// TODO Auto-generated method stub
		validateValue(builder);
		sequenceList.set(index, builder.build());
		return this;
	}

	@Override
	public YamlSequenceBuilder remove(int index) {
		// TODO Auto-generated method stub
		sequenceList.remove(index);
		return this;
	}

	@Override
	public YamlSequence build() {
		// TODO Auto-generated method stub
		return new za.co.sindi.yaml.node.YamlSequence(sequenceList);
	}
	
    private void validateValue(Object value) {
        if (value == null) {
            throw new NullPointerException("YAML Sequence builder cannot add null value other than YamlNull.");
        }
    }
    
    private void populate(Collection<?> collection) {
        for (Object value : collection) {
            if (value instanceof Optional) {
                ((Optional<?>) value).ifPresent(v ->
                        this.sequenceList.add(Values.of(v)));
            } else {
                this.sequenceList.add(Values.of(value));
            }
        }
    }

}
