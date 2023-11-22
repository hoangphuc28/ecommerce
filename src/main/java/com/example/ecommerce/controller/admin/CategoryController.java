package com.example.ecommerce.controller.admin;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.service.CategoryService;
import com.example.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ProductService bookService;
    @GetMapping("/admin/category")
    public String showCategories(Model model) {
        model.addAttribute("cateEdit", new Category());
        model.addAttribute("category", new Category());
        model.addAttribute("listCategory", categoryService.getAllCategories());
        model.addAttribute("err", "");
        return "Admin/Category/categories";
    }
    @PostMapping("/admin/category/create")
    public String create(@Valid Category category, BindingResult res, Model model) {
        if(res.hasErrors()) {
            model.addAttribute("cateEdit", new Category());
            model.addAttribute("category", category);
            model.addAttribute("listCategory", categoryService.getAllCategories());
            model.addAttribute("err", "");

            return "Admin/Category/categories";
        }
        categoryService.addCategory(category.getName());
        return "redirect:/admin/category";

    }
    @PostMapping("/admin/category/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id, Model model) {
            var cate = categoryService.getCategory(id);
            if(cate.getBooks().size()>0) {
                model.addAttribute("cateEdit", new Category());
                model.addAttribute("category", cate);
                model.addAttribute("listCategory", categoryService.getAllCategories());
                model.addAttribute("err", "Có sản phẩm thuộc thể loại này nên không thể xóa!");
                return "Admin/Category/categories";

            }
            categoryService.remove(id);
            return "redirect:/admin/category";
    }
    @PostMapping("/admin/category/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        var cate = categoryService.getCategory(id);
        model.addAttribute("cateEdit", cate);
        model.addAttribute("category", new Category());
        model.addAttribute("listCategory", categoryService.getAllCategories());
        model.addAttribute("err", "");

        return "Admin/Category/categories";
    }
    @PostMapping("/admin/category/edit")
    public String edit(Model model, @Valid Category newCategory,BindingResult res) {
        if(res.hasErrors()) {
            model.addAttribute("cateEdit", newCategory);
            model.addAttribute("category", new Category());
            model.addAttribute("listCategory", categoryService.getAllCategories());
            model.addAttribute("err", "");

            return "Admin/Category/categories";
        }
        categoryService.updateCategory(newCategory);
        return "redirect:/admin/category";
    }
}
