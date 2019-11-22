package com.cognition.bit.modules.service.impl;

import com.cognition.bit.modules.config.FileUploadProgressListener;
import com.cognition.bit.modules.dao.FileRecordDao;
import com.cognition.bit.modules.entity.ModFileRecord;
import com.cognition.bit.modules.service.FileService;
import com.cognition.bit.common.config.ProjectConfig;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.RequestContext;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.fileupload.FileItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.Map;


/**
 * 文件服务类
 * @author 1122
 */
@Service
public class FileServiceImpl extends CommonsMultipartResolver implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * 文件上传记录
     */
    private FileRecordDao fileRecordDao;

    /**
     * 项目相关设置
     */
    private ProjectConfig projectConfig;

    /**
     * 上传线程监听工具
     */
    private FileUploadProgressListener uploadProgressListener;

    @Autowired
    public void setFileRecordDao(FileRecordDao fileRecordDao) {
        this.fileRecordDao = fileRecordDao;
    }

    @Autowired
    public void setProjectConfig(ProjectConfig projectConfig) {
        this.projectConfig = projectConfig;
    }

    @Autowired
    public void setUploadProgressListener(FileUploadProgressListener uploadProgressListener) {
        this.uploadProgressListener = uploadProgressListener;
    }

    @Override
    public ModFileRecord get(Long id) {
        return fileRecordDao.get(id);
    }

    @Override
    public List<ModFileRecord> list(Map<String, Object> map) {
        return fileRecordDao.findList(map);
    }

    @Override
    public int count(Map<String, Object> map) {
        return fileRecordDao.count(map);
    }

    @Override
    public int save(ModFileRecord sysModFileRecord) {
        return fileRecordDao.insert(sysModFileRecord);
    }

    @Override
    public int update(ModFileRecord sysModFileRecord) {
        return fileRecordDao.update(sysModFileRecord);
    }

    @Override
    public int remove(Long id) {
        return fileRecordDao.remove(id);
    }

    @Override
    public int batchRemove(Long[] ids) {
        return fileRecordDao.batchRemove(ids);
    }

    @Override
    public Boolean isExist(String url) {
        Boolean isExist = false;
        if (!StringUtils.isEmpty(url)) {
            String filePath = url.replace("/files/", "");
            filePath = projectConfig.getUploadPath() + filePath;
            File file = new File(filePath);
            if (file.exists()) {
                isExist = true;
            }
        }
        return isExist;
    }


    @Override
    protected MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
        String encoding = determineEncoding(request);
        FileUpload fileUpload = prepareFileUpload(encoding);
        //fileUpload.setFileSizeMax(1024 * 1024 * 500);// 单个文件最大500M
        //fileUpload.setSizeMax(1024 * 1024 * 500);// 一次提交总文件最大500M
        //向文件上傳進度監視器設置session用於存儲上傳進度
        uploadProgressListener.setSession(request.getSession());
        //將文件上傳進度監視器加入到fileUpload中
        fileUpload.setProgressListener(uploadProgressListener);

        try {
            RequestContext context = new ServletRequestContext(request);
            List<FileItem> fileItems = fileUpload.parseRequest(context);
            return parseFileItems(fileItems, encoding);
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        return null;
    }


}
