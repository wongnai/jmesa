package org.jmesa.limit;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author xwx
 */
public class BaseFilter implements Filter{
    protected final String property;
    protected final Object value;



    public BaseFilter(String property, Object value) {
        this.property = property;
        this.value = value;
    }

    /**
     * Equality is based on the property. Or, in other words no two Filter
     * Objects can have the same property.
     */
    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (!(o instanceof BaseFilter)) {
            return false;
        }

        BaseFilter that = (BaseFilter) o;

        return that.getProperty().equals(this.getProperty());
    }

    @Override
    public int hashCode() {

        int result = 17;
        int prop = this.getProperty() == null ? 0 : this.getProperty().hashCode();
        result = result * 37 + prop;
        return result;
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
    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this);
        builder.append("property", property);
        builder.append("value", value.toString());
        return builder.toString();
    }


}
