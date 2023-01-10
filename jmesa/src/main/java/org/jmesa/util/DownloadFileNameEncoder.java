package org.jmesa.util;

import jakarta.mail.internet.MimeUtility;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class DownloadFileNameEncoder {

    private static String encoding = "UTF-8";

    public static void responseHeaders(HttpServletResponse response, HttpServletRequest request,
                                       String fileName, String contextType) throws Exception {
        responseHeaders(response, fileName, getUserAgent(request), contextType);
    }

    /**
     * &lt;mime-mapping&gt;
     * &lt;extension&gt;xls&lt;/extension&gt;
     * &lt;mime-type&gt;application/vnd.ms-excel&lt;/mime-type&gt;
     * &lt;/mime-mapping&gt;
     */
    public static void responseHeaders(HttpServletResponse response, String fileName, String userAgent, String contextType) throws Exception {
        if (contextType == null) {
            contextType = "application/octet-stream";
        }
        response.setContentType(contextType);

        // added by xwx
        fileName = codedFileName(userAgent, fileName, encoding);

        response.setHeader("Content-Disposition", "attachment;filename" + fileName);
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.setDateHeader("Expires", (System.currentTimeMillis() + 1000));
    }

    public static String getUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent;
    }

    public static String codedFileName(String userAgent, String filename, String encoding)
            throws UnsupportedEncodingException {

        String newFilename = URLEncoder.encode(filename, encoding);
        // 如果没有UA，则默认使用IE的方式进行编码，因为毕竟IE还是占多数的
        String rtn = "=\"" + newFilename + "\"";
        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
            // IE浏览器，只能采用URLEncoder编码
            if (userAgent.contains("msie")) {
                rtn = "=\"" + newFilename + "\"";
            }
            // Opera浏览器只能采用filename*
            else if (userAgent.contains("opera")) {
                rtn = "*=\"UTF-8''" + newFilename + "\"";
            }
            // Safari浏览器，只能采用ISO编码的中文输出
            else if (userAgent.contains("safari")) {
                rtn = "=\""
                        + new String(filename.getBytes(encoding), "ISO8859-1")
                        + "\"";
            }
            // Chrome浏览器，只能采用MimeUtility编码或ISO编码的中文输出
            else if (userAgent.contains("applewebkit")) {
                newFilename = MimeUtility.encodeText(filename, "UTF8", "B");
                rtn = "=\"" + newFilename + "\"";
            }
            // FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
            else if (userAgent.contains("mozilla")) {
                rtn = "*=\"UTF-8''" + newFilename + "\"";
                ;
            }
        }

        return rtn;
    }


    public static void setEncoding(String encoding) {
        DownloadFileNameEncoder.encoding = encoding;
    }

}
