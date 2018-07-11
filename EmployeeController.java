package com.ems.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ems.bean.EmployeeBean;
import com.ems.exception.EmployeeException;
import com.ems.service.IEmployeeService;

@Controller
public class EmployeeController {
	
	@Autowired
	private IEmployeeService employeeService;
	
	@RequestMapping("showHomePage")
	public String showhomePage(){
		return "index";
	}
	@RequestMapping("update")
	public ModelAndView showUpdatePage(@RequestParam("id") int id,@RequestParam("name") String name,@RequestParam("salary") double salary){
		ModelAndView mv=new ModelAndView();
		mv.setViewName("update");
		mv.addObject("id",id);
		mv.addObject("name", name);
		mv.addObject("salary", salary);
		return mv;
	}
	
	//EmployeeBean bean == store in bean object,get the model attribute with name emp
	@RequestMapping(value="addEmployee",method=RequestMethod.POST)
	public ModelAndView addEmployee(@ModelAttribute("emp")EmployeeBean bean,BindingResult result){
		ModelAndView mv=new ModelAndView();
		if(result.hasErrors()){
			mv.setViewName("error");
			mv.addObject("message", "Binding Failed "+result);
		}
		else{
			try {
				int id=employeeService.addEmployee(bean);
				mv.setViewName("success");
				mv.addObject("id", id);
				mv.addObject("emp", bean);
			} catch (EmployeeException e) {
				mv.setViewName("error");
				mv.addObject("message", e.getMessage());
			}
			
		}
		return mv;
		
	}
	@RequestMapping("showAll")
	public ModelAndView showAllEmployees(){
		ModelAndView mv=new ModelAndView();
		try {
			List<EmployeeBean> list=employeeService.viewAllEmployees();
			mv.setViewName("viewAll");
			mv.addObject("list", list);
		} catch (EmployeeException e) {
			
			mv.setViewName("error");
			mv.addObject("message", e.getMessage());
		}
		
		return mv;
		
	}
	@RequestMapping("deleteEmployee")
	public ModelAndView deleteEmployee(@RequestParam("id") int id){
		ModelAndView mv=new ModelAndView();
		try {
			boolean isDeleted=employeeService.deleteEmployee(id);
			if(isDeleted){
			List<EmployeeBean> list=employeeService.viewAllEmployees();
			mv.setViewName("viewAll");
			mv.addObject("list", list);
			}
		} catch (EmployeeException e) {
			
			mv.setViewName("error");
			mv.addObject("message", e.getMessage());
		}
		
		return mv;		
	}
	@RequestMapping(value="updateEmployee" , method=RequestMethod.GET)
	public ModelAndView updateEmployee(@RequestParam("id") int id,@RequestParam("name") String name,@RequestParam("salary") double salary){
		ModelAndView mv=new ModelAndView();
		try {
			EmployeeBean bean=employeeService.updateEmployee(id, name, salary);
			mv.setViewName("updatedEmployee");
			mv.addObject("emp",bean);
		} catch (EmployeeException e) {
			mv.setViewName("error");
			mv.addObject("message", e.getMessage());
		}
		return mv;
	}
}
