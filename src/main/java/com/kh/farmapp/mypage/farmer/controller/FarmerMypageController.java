package com.kh.farmapp.mypage.farmer.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kh.farmapp.mypage.farmer.model.service.FarmerMypageService;

import common.dto.Application;
import common.dto.FarmingDailylog;
import common.dto.TBOrder;
import common.dto.page.Criteria;
import common.dto.page.PageMaker;

@Controller
public class FarmerMypageController {
	@Autowired
	private FarmerMypageService farmerMypageService;

	// 영농일지 작성 폼 화면
	@RequestMapping(value = "/mypage/dailyLogWrite", method = RequestMethod.GET)
	public void dailyLog() {
		System.out.println("접속완료");
	}

	// 영농일지 글 작성 삽입
	@RequestMapping(value = "/mypage/dailyLogwrite", method = RequestMethod.POST)
	public String write(FarmingDailylog farmingDailylog) {

		farmerMypageService.writeDailylog(farmingDailylog);
		System.out.println("삽입완료");

		return "redirect:/mypage/dailyLoglist";

	}

	// 영농일지 리스트 목록 조회
	@RequestMapping(value = "/mypage/dailyLoglist", method = RequestMethod.GET)
	public String dailyLoglist(Model model, Criteria cri) {
		model.addAttribute("list", farmerMypageService.dailyLoglist(cri));

		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(farmerMypageService.listCount());

		model.addAttribute("pageMaker", pageMaker);

		return "mypage/dailyLoglist";
	}
	
	
	@RequestMapping(value = "/mypage/dailyLogReadView", method = RequestMethod.GET)
	public String read(FarmingDailylog farmingDailylog , Model model) {
		model.addAttribute("read",farmerMypageService.read(farmingDailylog.getDailylogNo()));
		System.out.println(farmingDailylog.getDailylogNo());
		return "mypage/dailyLogReadView";
	}
	

	// 농장 체험 리스트 목록 조회
	@RequestMapping(value = "/mypage/activitylist", method = RequestMethod.GET)
	public String activitylist(Model model, Criteria cri) {

//		List <Map<String,Object>> testMap = (List<Map<String, Object>>)model.addAttribute("list", farmerMypageService.activitylist(cri));

		// 농장체험 리스트
		List<Map<String, Object>> testMap = farmerMypageService.activitylist(cri);
		
		//일손체험
		List<Map<String, Object>> testMap3 = farmerMypageService.activitylist3(cri);

//		for (int i = 0; i < testMap.size(); i++) {
//			System.out.println(testMap.get(i).toString());
//		}

		//농장체험
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(farmerMypageService.listCount2());

		//일손체험
		PageMaker pageMaker3 = new PageMaker();
		pageMaker3.setCri(cri);
		pageMaker3.setTotalCount(farmerMypageService.listCount3());
		
		//농장체험
		model.addAttribute("list", testMap);
		model.addAttribute("pageMaker", pageMaker);

		//일손체험
		model.addAttribute("list3",testMap3);
		model.addAttribute("pageMaker3",pageMaker3);
		
		return "mypage/activitylist";
	}
	

	/**
	 * controller 승인 미승인 업데이트 하기
	 * 
	 * @param application 신청내역 객체
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/mypage/activitylist2", method = RequestMethod.GET)
	public String updateIsApproval(int applicationNo) {

		Application app = new Application();
		app.setApplicationNo(applicationNo);

		return Integer.toString(farmerMypageService.updateIsApproval(app));

	}
	
	// 판매 페이지 리스트
	@RequestMapping(value = "/mypage/selllist", method = RequestMethod.GET)
	public String selllist(Model model, Criteria cri) {
	System.out.println("판매 페이지 접속완료");
	
	List<Map<String, Object>> testMap = farmerMypageService.selllist(cri);
	
	for (int i = 0; i < testMap.size(); i++) {
		System.out.println(testMap.get(i).toString());
	}
	
	PageMaker pageMaker = new PageMaker();
	pageMaker.setCri(cri);
	pageMaker.setTotalCount(farmerMypageService.listCount2());
	
	model.addAttribute("list", testMap);
	model.addAttribute("pageMaker", pageMaker);
	
	return "mypage/selllist";
	}
	
	/**
	 * controller 결제 처리 업데이트 하기
	 * 
	 * @param  결제내역 객체
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/mypage/selllist2", method = RequestMethod.GET)
	public String updatePayment(int orderNo) {

		TBOrder order = new TBOrder();
		order.setOrderNo(orderNo);
		System.out.println(order);
		return Integer.toString(farmerMypageService.updatePayment(order));

	}
	
	@RequestMapping(value = "/mypage/cal", method = RequestMethod.GET)
	public String cal() {
		System.out.println("접속");
		return "mypage/cal";
	}
	@RequestMapping(value = "/mypage/cal23", method = RequestMethod.GET)
	public String cal2() {
		System.out.println("접속");
		return "mypage/cal23";
	}
}
