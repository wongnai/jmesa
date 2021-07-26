package org.jmesa.limit;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author xwx
 */
public class BaseFilter implements Filter{
    protected final String property;
    protected final Object[] value;
    protected final Comparison comparison;

    public BaseFilter(String property,Comparison operator,  Object... value) {
        this.property = property;
        this.value = value;
        this.comparison = operator;
    }

    @Override
    public Comparison getComparison() {
        return comparison;
    }

    /**
     * @return The Bean (Or Map) attribute used to reduce the results.
     */
    @Override
    public String getProperty() {
        return property;
    }

    /**
     * @return Will be used to reduce the results.
     */
    @Override
    public Object[] getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "{" +
                "property='" + property + '\'' +
                ", value=" + Arrays.toString(value) +
                ", comparison=" + comparison +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseFilter that = (BaseFilter) o;
        return Objects.equals(property, that.property) && Arrays.equals(value, that.value) && comparison == that.comparison;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(property, comparison);
        result = 31 * result + Arrays.hashCode(value);
        return result;
    }
}
