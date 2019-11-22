package com.cognition.bit.modules.controller;

import com.cognition.bit.modules.entity.ModFileRecord;
import com.cognition.bit.modules.service.FileService;
import com.cognition.bit.system.persistence.BaseController;
import com.cognition.bit.common.config.ProjectConfig;
import com.cognition.bit.common.until.FileUtil;
import com.cognition.bit.common.until.PageUtils;
import com.cognition.bit.common.until.Query;
import com.cognition.bit.common.until.ResultData;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 文件上传
 *
 * @author taoya
 */
@Controller
@RequestMapping("/modules/sysFile")
public class FileController extends BaseController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private FileService fileService;

	private ProjectConfig projectConfig;

	@Autowired
	public void setFileService(FileService fileService) {
		this.fileService = fileService;
	}

	@Autowired
	public void setProjectConfig(ProjectConfig projectConfig) {
		this.projectConfig = projectConfig;
	}

//	@GetMapping()
//	@RequiresPermissions("common:sysFile:sysFile")
//	String sysFile(Model model) {
//		Map<String, Object> params = new HashMap<>(16);
//		return "modules/file/file";
//	}

//	@GetMapping("/index")
//	String sysFileIndex(Model model) {
//		Map<String, Object> params = new HashMap<>(16);
//		return "clienthtml/userFile";
//	}


	@ResponseBody
	@GetMapping("/list")
	@RequiresPermissions("common:sysFile:sysFile")
	public PageUtils list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Map<String, Object> query = new Query(params);
		List<ModFileRecord> sysModFileRecordList = fileService.list(query);
		int total = fileService.count(query);
		PageUtils pageUtils = new PageUtils(sysModFileRecordList, total);
		return pageUtils;
	}

	@GetMapping("/add")
	 @RequiresPermissions("common:bComments")
	String add() {
		return "common/sysFile/add";
	}

	@GetMapping("/edit")
	 @RequiresPermissions("common:bComments")
	String edit(Long id, Model model) {
		ModFileRecord sysModFileRecord = fileService.get(id);
		model.addAttribute("sysFile", sysModFileRecord);
		return "common/sysFile/edit";
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("common:info")
	public ResultData info(@PathVariable("id") Long id) {
		ModFileRecord sysModFileRecord = fileService.get(id);
        return ResultData.result(false).setData(sysModFileRecord);
	}

//	/**
//	 * 保存
//	 */
//	@ResponseBody
//	@PostMapping("/save")
//	@RequiresPermissions("common:save")
//	public ResultMap save(FileRecord sysFileRecord) {
//		if (fileService.save(sysFileRecord) > 0) {
//			return ResultMap.success();
//		}
//		return ResultMap.error();
//	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("common:update")
	public ResultData update(@RequestBody ModFileRecord sysModFileRecord) {
		fileService.update(sysModFileRecord);
		return ResultData.result(true);
	}

	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ResponseBody
	 @RequiresPermissions("common:remove")
	public ResultData remove(Long id, HttpServletRequest request) {
		String fileName = projectConfig.getUploadPath() + fileService.get(id).getUrl().replace("/files/", "");
		if (fileService.remove(id) > 0) {
			boolean b = FileUtil.deleteFile(fileName);
			if (!b) {
				return ResultData.result(false).setMsg("数据库记录删除成功，文件删除失败");
			}
			return ResultData.result(true);
		} else {
			return ResultData.result(false);
		}
	}

	/**
	 * 删除
	 */
	@PostMapping("/batchRemove")
	@ResponseBody
	@RequiresPermissions("common:remove")
	public ResultData remove(@RequestParam("ids[]") Long[] ids) {
		fileService.batchRemove(ids);
		return ResultData.result(true);
	}


	@ResponseBody
	@PostMapping("/upload")
    @Async("taskExecutor")
    ResultData upload(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) {
		//文件名
		String fileName = multipartFile.getOriginalFilename();
		String publicUrl = "";

		//文件名不为空
		if (fileName != null) {

			publicUrl = projectConfig.getUploadPath();
			//文件归属文件夹与标识
			Map<String, String> fileType = FileUtil.fileType(fileName);
			//文件随机生成uuid
			fileName = FileUtil.renameToUUID(fileName);
			if (!fileType.get("state").equals("500")) {
				String path = "/uploaded_files/"+fileType.get("Suffix")+"/";
				//文件上传地址与名字 multipartFile.getOriginalFilename()
				ModFileRecord sysModFileRecord = new ModFileRecord(multipartFile.getOriginalFilename(), Integer.valueOf(fileType.get("state")), path + fileName);
				sysModFileRecord.preInsert();
				try {
					File file = new File(publicUrl+path, fileName);
					if (!file.exists() && !file.isDirectory()) {
						logger.info("method[uploadPic]文件不存在，正在创建文件。。。。。");
						file.mkdirs();
						logger.info("method[uploadPic]文件创建完成 success。。。。。。。");
					}
					multipartFile.transferTo(file);
					if (fileService.save(sysModFileRecord) > 0) {
						return ResultData.result(true).setData(sysModFileRecord.getUrl());
					}
					System.err.println("保存失败！！");
					logger.info("保存失败！！");
					return ResultData.result(false);
				} catch (Exception e) {
					System.err.println(e.toString());
					logger.info(e.toString());
					return ResultData.result(false);
				}
			}
			return ResultData.result(false);
		} else {
			logger.info("选择的文件为空");
			return ResultData.result(false).setMsg("您选择的文件为空");
		}
	}




}
