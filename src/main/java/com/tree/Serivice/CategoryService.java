package com.tree.Serivice;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tree.Entity.Category;

public interface CategoryService extends IService<Category> {

    void remove(long id);
}
