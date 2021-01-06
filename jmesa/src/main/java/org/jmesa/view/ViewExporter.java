/*
 * Copyright 2004 original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jmesa.view;

import java.io.OutputStream;

/**
 * @since 2.0
 * @author Jeff Johnston
 */
public interface ViewExporter {
    View getView();
    void setView(View view);
    String getFileName();
    void setFileName(String fileName);
    void export() throws Exception;

    /**
     * @since 4.1
     * @param out output file etc. to that stream
     * @throws Exception any error
     */
    void export(OutputStream out) throws Exception;
}
