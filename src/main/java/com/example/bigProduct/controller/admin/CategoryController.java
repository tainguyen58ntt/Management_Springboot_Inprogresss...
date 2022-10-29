package com.example.bigProduct.controller.admin;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.bigProduct.domain.Category;
import com.example.bigProduct.model.CategoryDTO;
import com.example.bigProduct.service.CategoryService;

@Controller
@RequestMapping("/admin/categories")
public class CategoryController {
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/add")
	public String add(Model model) {
		
		
		model.addAttribute("category", new CategoryDTO());
		return "admin/categories/addOrEdit";
	}

	@GetMapping("edit/{categoryId}")
	public ModelAndView edit(ModelMap model, @PathVariable("categoryId") Long categoryId) {
		Optional<Category> opt = categoryService.findById(categoryId);
		CategoryDTO dto = new CategoryDTO();
		if(opt.isPresent()) {
			Category entity = opt.get();
			BeanUtils.copyProperties(entity, dto);  // copy tu entity sang dto
			dto.setIsEdit(true);	
			model.addAttribute("category", dto);
			return new ModelAndView("admin/categories/addOrEdit", model);
		}
		model.addAttribute("message", "Category is not exists");
		return new ModelAndView("forward:/admin/categories",model);
	}
	
	@GetMapping("delete/{categoryId}")
	public ModelAndView delete(@PathVariable("categoryId") Long categoryId, ModelMap model) {
		
		categoryService.deleteById(categoryId);
		model.addAttribute("message", "category is deleted");
		return new ModelAndView("forward:/admin/categories/search",model);
	}
	
	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("category") CategoryDTO dto,BindingResult result ) {
		if(result.hasErrors()) {
			return new ModelAndView("admin/categories/addOrEdit");
		}
		Category entity = new Category();
		BeanUtils.copyProperties(dto, entity);
		
		categoryService.save(entity);
		model.addAttribute("message","Category is saved !");
		return new ModelAndView("forward:/admin/categories", model);

	}
	@RequestMapping("")
	public String list(ModelMap model) {
		List<Category> list =  categoryService.findAll();
		model.addAttribute("categories", list);	
		return "admin/categories/list";
	}
	@GetMapping("search")
	public String search(ModelMap model, @RequestParam(name = "name", required = false) String name) {
		List<Category> list = null;
		if(StringUtils.hasText(name)) {
			list = categoryService.findByNameContaining(name);
		}else {
			list = categoryService.findAll();
		}
		
		model.addAttribute("categories", list);
		
		return "/admin/categories/search";
	}
	
}
