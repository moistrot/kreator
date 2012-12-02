package com.langtaojin.kreator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class StaticFileHandler {

    private static final String MIME_OCTET_STREAM = "application/octet-stream";
    private static final int MAX_BUFFER_SIZE = 4096;
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ServletContext servletContext;
    private long expires = 0L;
    private String maxAge = "";

    public StaticFileHandler(ServletConfig config) throws ServletException {
        this.servletContext = config.getServletContext();
        String expiresValue = config.getInitParameter("expires");
        if (expiresValue != null) {
            int n = Integer.parseInt(expiresValue);
            if (n > 0) {
                this.expires = (n * 1000L);
                this.maxAge = ("max-age=" + n);
                this.log.info("Static file's cache time is set to " + n + " seconds.");
            } else if (n < 0) {
                this.expires = -1L;
                this.log.info("Static file is set to no cache.");
            }
        }
    }

    public void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String url = request.getRequestURI();
        String path = request.getContextPath();
        url = url.substring(path.length());
        if (url.toUpperCase().startsWith("/WEB-INF/")) {
            response.sendError(404);
            return;
        }
        int n = url.indexOf('?');
        if (n != -1)
            url = url.substring(0, n);
        n = url.indexOf('#');
        if (n != -1)
            url = url.substring(0, n);
        File f = new File(this.servletContext.getRealPath(url));
        if (!f.isFile()) {
            response.sendError(404);
            return;
        }
        long ifModifiedSince = request.getDateHeader("If-Modified-Since");
        long lastModified = f.lastModified();
        if ((ifModifiedSince != -1L) && (ifModifiedSince >= lastModified)) {
            response.setStatus(304);
            return;
        }
        response.setDateHeader("Last-Modified", lastModified);
        response.setContentLength((int) f.length());

        if (this.expires < 0L) {
            response.setHeader("Cache-Control", "no-cache");
        } else if (this.expires > 0L) {
            response.setHeader("Cache-Control", this.maxAge);
            response.setDateHeader("Expires", System.currentTimeMillis() + this.expires);
        }

        response.setContentType(getMimeType(f));
        sendFile(f, response.getOutputStream());
    }

    String getMimeType(File f) {
        String mime = this.servletContext.getMimeType(f.getName());
        return mime == null ? "application/octet-stream" : mime;
    }

    void sendFile(File file, OutputStream output) throws IOException {
        InputStream input = null;
        try {
            input = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[4096];
            while (true) {
                int n = input.read(buffer);
                if (n == -1)
                    break;
                output.write(buffer, 0, n);
            }
            output.flush();
        } finally {
            if (input != null)
                try {
                    input.close();
                } catch (IOException e) {
                }
        }
    }

}
