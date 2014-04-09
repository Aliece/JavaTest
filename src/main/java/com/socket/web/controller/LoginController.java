package com.socket.web.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.socket.domain.LoginForm;

/**
 * @title: LoginController.java
 * @description: 


 * @author saizhongzhang
 * @date 2014年3月7日
 * @version 1.0
 */

@Controller
public class LoginController {
	
	@RequestMapping(value="login", method = RequestMethod.POST)
    public ModelAndView login(HttpServletRequest request,HttpServletResponse response,LoginForm command ){
        String username = command.getUserName();
        ModelAndView mv = new ModelAndView("/index/index","command","LOGIN SUCCESS, " + username);
        return mv;
    }

	 @RequestMapping(value = "/upload.do")  
	    public String upload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, ModelMap model) {  
	  
	        System.out.println("开始");  
	        String path = request.getSession().getServletContext().getRealPath("upload");  
	        String fileName = file.getOriginalFilename();  
//	        String fileName = new Date().getTime()+".jpg";  
	        System.out.println(path);  
	        File targetFile = new File(request.getContextPath()+"/upload/", fileName);  
	        if(!targetFile.exists()){  
	            targetFile.mkdirs();  
	        }  
	  
	        //保存  
	        try {  
	            file.transferTo(targetFile);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        model.addAttribute("fileUrl", request.getContextPath()+"/upload/"+fileName);  
	  
	        return "result";  
	    } 
}
