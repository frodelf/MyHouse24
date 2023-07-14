package com.avada.myHouse24.util;

import com.avada.myHouse24.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.stream.Collectors;

public class PaginationUtil {
    public static List<User> pagination(List<User> users, int maxValue, int currentPage){
        Page<User> res = new PageImpl<>(users, PageRequest.of(currentPage - 1, maxValue), users.size());
        return res.get().collect(Collectors.toList());
    }
}
