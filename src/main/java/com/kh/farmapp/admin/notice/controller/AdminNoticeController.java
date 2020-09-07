package com.kh.farmapp.admin.notice.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.JsonObject;
import com.kh.farmapp.admin.model.service.AdminNoticeService;

import common.dto.Admin;
import common.dto.Notice;
import common.util.AdminPaging;

/**
 * 공지사항 & 자주 묻는 질문 관리 페이지
 */
@Controller
public class AdminNoticeController {

	// log 를 남기기 위한 Logger 객체
	private static final Logger logger = LoggerFactory.getLogger(AdminNoticeController.class);

	// service 객체
	@Autowired
	private AdminNoticeService adminNoticeService;
	
	// 공지사항 관리 목록 페이지
	@RequestMapping(value = "/adminnotice/list", method = RequestMethod.GET)
	public String adminNoticeList(
			Model model
			, @RequestParam(defaultValue = "0") String curPage
			, HttpSession session
			) {
		// 로그인이 안되어 있을 경우, 바로 로그인 페이지로 이동
		Admin loginAdmin = (Admin) session.getAttribute("adminInfo");
		if( loginAdmin == null) {
			
			return "redirect:/admin/login";
			
		}
		
		// 로그 찍기
		logger.info("/adminnotice/list - [GET] 요청");
		
		// 페이징 설정
		AdminPaging apaging = adminNoticeService.getPaging(curPage);
//		logger.info("AdminPaging: " + apaging.toString());
		
		// apaging 정보로 notice 목록 불러오기
		List<Map<String, Object>> pagingNList = adminNoticeService.selectNoticeByAPaging(apaging);
		//pagingNList test output
//		for(Map<String, Object> m : pagingNList) {
//			logger.info("m: " + m.toString());
//		}
		
		// model 값 넘겨주기
		// 페이징 객체 넘기기
		if( apaging != null ) {
			model.addAttribute("apaging", apaging);
		}
		// 페이징 처리된 목록 넘기기
		if( pagingNList != null ) {
			model.addAttribute("pagingNList", pagingNList);
		}
		
		return "admin/notice/admin_notice_list";
	}
	
	
	// 공지사항 관리 공지사항 상세 페이지
	@RequestMapping(value = "/adminnotice/noticedetail", method = RequestMethod.GET)
	public String adminNoticeDetail(Notice noticeNo) {
		return "";
	}
	
	// 공지사항 관리 공지사항 작성 폼 페이지
	@RequestMapping(value = "/adminnotice/write", method = RequestMethod.GET)
	public String adminNoticeWrite(
				HttpSession session
			){
		// 로그인이 안되어 있을 경우, 바로 로그인 페이지로 이동
		Admin loginAdmin = (Admin) session.getAttribute("adminInfo");
		if( loginAdmin == null) {
			
			return "redirect:/admin/login";
			
		}
		return "admin/notice/admin_notice_write";
	}
	
	// 공지사항 관리 공지사항 작성
	@RequestMapping(value = "/adminnotice/write", method = RequestMethod.POST)
	public String adminNoticeWriteProc(
				@ModelAttribute Notice notice
				, HttpSession session
				, Model model
			) {
		// 로그인이 안되어 있을 경우, 바로 로그인 페이지로 이동
		Admin writer = (Admin) session.getAttribute("adminInfo");
		if( writer == null) {
			
			return "redirect:/admin/login";
			
		}
		
		// logger 찍기 - 현재 어디 method 인지
		logger.info("/adminnotice/write - [POST] 요청");
		
		// RequestParam notice 객체 테스트 출력
		logger.debug("notice: " + notice.toString());
		
		// notice 객체의 adminNo set 하기
		notice.setAdminNo(writer.getAdminNo());
		
		int res = adminNoticeService.writeNotice(notice);
		
		if(res > 0) {
			logger.info("공지사항 작성 성공~");
			
			// 관리자 메인 화면으로 이동
			model.addAttribute("alertMsg", "공지사항이 성공적으로 작성됐습니다.");
			// 나중에 상세조회 페이지 만들면 그 페이지로 넘어가게끔 하기! 그렇다면 noticeNo 를 조회해서 보여줘야할 것 같네요!
			// 아예 noticeNo 는 select notice_seq.nextval from dual 로 조회하는 query를 사용해서 notice 에 저장해서 그 notice로 상세페이지를 조회하는 게 좋은 생각인거 같다.
			model.addAttribute("url", "list");
		} else {
			logger.info("공지사항 작성 실패...");
			model.addAttribute("alertMsg", "공지사항 작성이 실패했습니다...");
			model.addAttribute("url", "write");
		}
		
		return "common/result";
	}
	
	// 공지사항 이미지 업로드
	@RequestMapping(value = "/adminnotice/fileupload", method = RequestMethod.POST)
	@ResponseBody
	public String adminNoticeImgUpload(
			HttpServletRequest req
			, HttpServletResponse resp
			, MultipartHttpServletRequest multiFile
			){
		
		JsonObject json = new JsonObject();
		PrintWriter pw = null;
		OutputStream out = null;
		MultipartFile file = multiFile.getFile("upload");
		
		if( file != null ) {
			
			if( file.getSize() > 0 && StringUtils.isNotBlank(file.getName()) ) {
				
				if( file.getContentType().toLowerCase().startsWith("image/") ) {
					
					try {
						
						String fileName = file.getName();
						byte[] bytes = file.getBytes();
						String uploadPath = req.getServletContext().getRealPath("/resources") + "/ckimg";
//						logger.debug(uploadPath);
						File uploadFile = new File(uploadPath);
						
						if(!uploadFile.exists()) {
							
							uploadFile.mkdirs();
							
						}
						
						fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
						uploadPath = uploadPath + "/" + fileName;
//						logger.debug(uploadPath);
						out = new FileOutputStream(new File(uploadPath));
						out.write(bytes);
						out.flush();
						
						pw = resp.getWriter();
						resp.setContentType("text/html");
						String fileUrl = req.getContextPath() + "/resources/ckimg/" + fileName;
//						logger.debug(fileUrl);
						
						//json 데이터로 등록
						json.addProperty("uploaded", 1);
						json.addProperty("fileName", fileName);
						json.addProperty("url", fileUrl);
						
						pw.println(json);
						pw.flush();
						
					} catch (IOException e) {
					
						e.printStackTrace();
						
					} finally {
						
						try {
							if(out != null) {
								out.close();
							}
							if(pw != null) {
								pw.close();
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					
					
				}
				
			}
			
		}
		
		return null;
	}
	
	// 공지사항 관리 공지사항 삭제
	@RequestMapping(value = "/adminnotice/delete", method = RequestMethod.POST)
	@ResponseBody
	public int deleteNotice(
			// 삭제할 번호들
			@RequestParam Map<String, Object> deleteNums
			, HttpSession session 
			) {
		// 로그인이 안되어 있을 경우, 바로 로그인 페이지로 이동
		Admin loginAdmin = (Admin) session.getAttribute("adminInfo");
		if( loginAdmin == null) {
			
			toAdminLogin("/admin/login");
//			return "redirect:/admin/login";
			
		}
		
		// 현재 어떤 url 이 실행되고 있는지
		logger.info("/adminnotice/delete [POST] 요청");

		// ajax 반환 값
		int result = 1;
		
		// noticeNo -> ArrayList 로 변환
		String[] nums = deleteNums.get("noticeNo").toString().split(",");
		List<Object> noticeNoList =  new ArrayList<>(Arrays.asList(nums));
//		logger.info("noticeNoList: " + noticeNoList.toString());
//		logger.info("noticeNoList: " + noticeNoList.getClass().getName());
		
		// 삭제 처리
		int delRes = adminNoticeService.deleteNoticeByNoticeNo(noticeNoList);
		
		if(delRes >= 1) { // 삭제 결과 성공
			
			logger.info("삭제 성공~");
			return result;
			
		} else { // 삭제 결과 실패
			
			logger.info("삭제 실패~");
			result = 0;
			return result;
			
		}
		
	}
	
	// admin/login 으로 redirect
	public String toAdminLogin(String location) {
		return "redirect:" + location;
	}
	
}
