package org.jmesa.facade;

import org.jmesa.core.CoreContext;
import org.jmesa.web.WebContext;

/**
 * @author xwx
 */
public interface ContextSupport {
    WebContext getWebContext();

    CoreContext getCoreContext();
}
