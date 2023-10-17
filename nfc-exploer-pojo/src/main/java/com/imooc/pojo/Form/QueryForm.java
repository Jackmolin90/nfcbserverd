package com.imooc.pojo.Form;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.imooc.form.BaseQueryForm;
import com.imooc.pojo.Param.BlockQueryParam;
import lombok.Data;

@Data
public class QueryForm{

    private String query;

    private Long current;

    private Long size;

    private Long index;
}
