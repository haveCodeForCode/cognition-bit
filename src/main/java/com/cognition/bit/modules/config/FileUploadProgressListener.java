package com.cognition.bit.modules.config;

import com.cognition.bit.common.until.DateUtil;
import org.apache.commons.fileupload.ProgressListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

/**
 * @author Worry
 * @version 2019/9/14
 */
@Component("FileUploadProgressListener")
public class FileUploadProgressListener implements ProgressListener {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadProgressListener.class);


    private HttpSession session;

    public void setSession(HttpSession session) {
        this.session = session;
        logger.info("upload_percent 0--------------------------------------" + DateUtil.getTime());
        session.setAttribute("upload_percent", 0);
    }

    @Override
    public void update(long bytesRead, long contentLength, int items) {
        int percent = (int) (bytesRead * 100.0 / contentLength);
        session.setAttribute("upload_percent", percent);
        logger.info(percent + "--------------------------------------" + DateUtil.getTime());
    }
}
