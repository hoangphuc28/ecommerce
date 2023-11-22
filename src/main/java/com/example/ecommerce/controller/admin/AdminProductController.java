package com.example.ecommerce.controller.admin;

import com.example.ecommerce.model.Product;
import com.example.ecommerce.service.CategoryService;
import com.example.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
public class AdminProductController {
    @Autowired
    private ProductService bookService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ResourceLoader resourceLoader;

    @GetMapping("/admin/products")
    public String Home(Model model) {
        List<Product> products = bookService.getAllBook();
            model.addAttribute("products", products);
        return "Admin/Product/index";
    }
    @GetMapping("/admin/product/create")
    public String CreateBook(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("listCategory", categoryService.getAllCategories());
        return "Admin/Product/create";
    }
    @PostMapping("/admin/product/create")
    public String CreateBook(@Valid Product product, BindingResult res, Model model,
                             @RequestParam MultipartFile imageProduct
                             ) throws IOException {
        if(res.hasErrors()) {
            model.addAttribute("book", product);
            model.addAttribute("listCategory", categoryService.getAllCategories());
            return "Admin/Product/create";
        }
        if(imageProduct != null && imageProduct.getSize() > 0)
        {
            try {
                File saveFile = new ClassPathResource("static/img").getFile();
                String newImageFile = UUID.randomUUID() +  ".png";
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + newImageFile);
                Files.copy(imageProduct.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                product.setImage(newImageFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        product.setIsPublish(false);
        bookService.addBook(product);
        return "redirect:/admin/products";
    }
    @DeleteMapping("/admin/product/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable("id") Long id) {
        System.out.println(id);
        bookService.removeBook(id);
        return ResponseEntity.ok("Request successful");
    }
    @GetMapping("/admin/product/delete/{id}")
    public String getDelete() {
        return "redirect:/";
    }

    @PostMapping("/admin/product/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        var product = bookService.getProduct(id);
        model.addAttribute("product", product);
        model.addAttribute("listCategory", categoryService.getAllCategories());
        return "Admin/Product/edit";
    }
    @PostMapping("/admin/product/edit")
    public String edit(Model model, @Valid Product newBook, BindingResult res,
                       @RequestParam MultipartFile imageProduct

                       ) {
        if(res.hasErrors()) {
            model.addAttribute("product", newBook);
            model.addAttribute("listCategory", categoryService.getAllCategories());
            return "Admin/Product/edit";
        }
        if(imageProduct != null && imageProduct.getSize() > 0)
        {
            try {
                File saveFile = new ClassPathResource("static/img").getFile();
                String newImageFile = UUID.randomUUID() +  ".png";
                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + newImageFile);
                Files.copy(imageProduct.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                newBook.setImage(newImageFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        bookService.update(newBook);
        return "redirect:/admin/products";
    }
    @PostMapping("/admin/product/publish/{id}")
    public String publish(@PathVariable("id") Long id) {
        var product = bookService.getProduct(id);
        product.setIsPublish(true);
        bookService.update(product);
        return "redirect:/admin/products";
    }
    @PostMapping("/admin/product/unpublish/{id}")
    public String unpublish(@PathVariable("id") Long id) {
        var product = bookService.getProduct(id);
        product.setIsPublish(false);
        bookService.update(product);
        return "redirect:/admin/products";
    }
}
