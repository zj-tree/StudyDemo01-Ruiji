package com.tree.Serivice.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.tree.Entity.AddressBook;
import com.tree.Mapper.AddressBookMapper;
import com.tree.Serivice.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {

}
