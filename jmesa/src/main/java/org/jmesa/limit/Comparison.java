package org.jmesa.limit;

/**
 * Used for filters
 * @author xwx
 * since 4.3
 */
public enum Comparison {
    /**
     * =
     */
    IS,

    /**
     * !=
     */
    IS_NOT,

    /**
     * >
     */
    GT,

    /**
     * >=
     */
    GTE,

    /**
     * &lt;
     */
    LT,

    /**
     * &lt;=
     */
    LTE,

    /**
     * in (...)
     */
    IN,

    /**
     * not in (...)
     */
    NOT_IN,

    /**
     * is null
     */
    IS_NULL,
    /**
     * is not null
     */
    IS_NOT_NULL,
    /**
     * need sub query, not implemented yet
     */
    EXISTS,
    /**
     * need sub query, not implemented yet
     */
    NOT_EXISTS,

    /**
     * between (from_value, to_value) inclusive
     */
    BETWEEN,
    /**
     * not between (from_value, to_value) inclusive
     */
    NOT_BETWEEN,


    /**
     * like concat('%', value, '%')
     */
    CONTAIN,

    /**
     * like concat(value, '%')
     */
    START_WITH;


    // UPDATE: implicitly already the default so override not needed in this case
    @Override
    public String toString() {
        return this.name();
    }
}
